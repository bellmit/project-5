package com.kindminds.drs.data.pipelines.core.sales

import java.sql.Timestamp
import java.time._
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.Sink
import com.kindminds.drs.Marketplace
import com.kindminds.drs.data.pipelines.api.message.{RefreshDailySales, RefreshDailySalesExclRefundOrders, RefreshMissingDailySales, RegisterCommandHandler, StartRefreshAllOrders}
import com.kindminds.drs.data.pipelines.core.dto.{DailySale, Order, RetainmentRate}
import com.kindminds.drs.data.pipelines.core.settlement.DTran.timeout

import scala.concurrent.Await

object DailySalesHandler {

  def props(drsDPCmdBus: ActorRef): Props = Props(new DailySalesHandler(drsDPCmdBus))

}

class DailySalesHandler(drsDPCmdBus: ActorRef) extends Actor with ActorLogging {


  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()
  val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

  val name = self.path.name

  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshDailySalesExclRefundOrders].getName ,self)
  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshDailySales].getName ,self)
  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshMissingDailySales].getName ,self)



  override def receive: Receive = {


    case refd : RefreshDailySalesExclRefundOrders =>

      val today = LocalDateTime.now
      val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
        .minusMonths(3)
      val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)


     // processDailySalesToExclRefund(st , et)


    case r : RefreshDailySales =>

      val today = ZonedDateTime.now(ZoneOffset.UTC)
      val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
      val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)


      processDailySales(st , et)

    case rm : RefreshMissingDailySales =>

      val days = Period.between(rm.startTime.toLocalDate,rm.endTime.toLocalDate).getDays

      //println("AAAAAAAAAAAAAAAAAAA")
      //println(days)

      val r = 1 to days
      val baseDateTime = rm.startTime.minusDays(2)
      r.foreach(x=>{
        println(x)
        val st = baseDateTime.plusDays(x).withHour(0).withMinute(0).withSecond(0).withNano(0)
        val et = baseDateTime.plusDays(x).withHour(23).withMinute(59).withSecond(59).withNano(999)

        //todo change localdatetime
       // processDailySales(st , et)
      })



  }


  def processDailySales(startTime:ZonedDateTime , endTime : ZonedDateTime): Unit ={


    //every one hour , get this hour order data to settle

    implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
    import session.profile.api._

    //last update date need to use taipei time ?
    val st = startTime.toInstant()
    val et = endTime.toInstant()

    //2019-07-29 23:59:59

    //  and item_price > 0
    val q = sql"""select * from sales.all_orders
                   where
                   last_update_date <= '#${Timestamp.from(et)}'
                   and last_update_date >= '#${Timestamp.from(st)}'
                    and ( sales_channel is not null and
                          sales_channel <> 'Non-Amazon' and sales_channel <> 'eBay'
                          and sales_channel <> 'Non-order'
                          and  sales_channel <> 'Webstore' and sales_channel <> '')
                    and local_order_time is not null
                   order by  com_code desc """.as[Order]


    var orders : Vector[Order] = null
    Await.result(
      session.db.run(q).map { res =>
        // res is a Vector[String]
        orders = res
      }, timeout)


    val r: Map[String, Vector[Order]] = orders.groupBy(x=>x.com_code)

    r.foreach(kv=>{

      //r rate

      //println(kv._1)

      //todo k101 per unit gross profit
      if(kv._1 != "K101"){

        val retainmentRates = getRetainmentRate(kv._1 , session)

        val gr: Map[(String, String, String), Vector[Order]] =  kv._2.groupBy(x=> (
          x.sales_channel , x.base_code , x.sku_code_by_drs  ))

        gr.foreach(x =>{

          val gr2: Map[LocalDate, Vector[Order]] =  x._2.groupBy(y=> {
            val lot = OffsetDateTime.ofInstant(Instant.ofEpochMilli(y.local_order_time),
              ZoneOffset.UTC)
            lot.toLocalDate
          })

          processOrderGroup(gr2 , x , retainmentRates , kv._1 , session)

        })

      }

    })


    session.close()

  }



  def processOrderGroup( orderGroup : Map[LocalDate, Vector[Order]] ,
          x: ((String, String, String), Vector[Order]) ,
          retainmentRates :Vector[RetainmentRate] , kcode : String ,session: SlickSession): Unit ={

    import session.profile.api._

    orderGroup.foreach(z=>{

      val m = com.kindminds.drs.Marketplace.fromName(x._1._1)

      val retainmentRate = retainmentRates.filter(x=> (
        (x.date_start.toLocalDate.isBefore(z._1) || x.date_start.toLocalDate.isEqual(z._1))
        & ( x.date_end.toLocalDate.isAfter(z._1) ||  x.date_end.toLocalDate.isEqual(z._1)) )
          & x.country_id == m.getCountry.getKey)

      val order = z._2(0)


      val localOrderSt =  ZonedDateTime.of(z._1,LocalTime.of(0,0,0,0),ZoneOffset.UTC)
      val localOrderEt =  localOrderSt.plusDays(1) .toInstant()

      val currencyExchangeRate = getCurrencyExchagneRate(localOrderSt , m.getCurrency.getKey , session)


      val q2 = sql"""
                   select *  from sales.all_orders
                      where com_code = '#${order.com_code}'
                      and sales_channel = '#${order.sales_channel}'
                      and base_code ='#${order.base_code}'
                      and sku_code_by_drs = '#${order.sku_code_by_drs}'
                      and local_order_time >= '#${Timestamp.from(localOrderSt.toInstant)}'
                      and local_order_time < '#${Timestamp.from(localOrderEt)}'
                      order by Date(local_order_time)
        """.as[Order]


      //println(q2.statements)

      var dailyOrders : Vector[Order] = null
      Await.result(
        session.db.run(q2).map { res =>
          // res is a Vector[String]
          dailyOrders = res
        }, timeout)


      var grossProfit : BigDecimal = 0
      var rev : BigDecimal = 0
      var qty = 0
      var grossProfit_excl_pndg : BigDecimal = 0
      var rev_excl_pndg : BigDecimal = 0
      var qty_excl_pndg = 0


      dailyOrders.foreach(dOrder=>{

        if(m == com.kindminds.drs.Marketplace.TRUETOSOURCE) {

          if(order.order_status == "paid"  || order.order_status == "partially_refunded"){
            qty += order.qty
            rev += order.actual_retail_price
            qty_excl_pndg += order.qty
            rev_excl_pndg += order.actual_retail_price

            if (retainmentRate.size > 0) {
              grossProfit += (rev * retainmentRate(0).rate)
              grossProfit_excl_pndg += (rev_excl_pndg * retainmentRate(0).rate)
            }

          }


        }else{

          if((dOrder.order_status == "Shipped" || dOrder.order_status == "Pending") && dOrder.item_price > 0){
            qty += dOrder.qty
            rev += dOrder.actual_retail_price
            if(retainmentRate.size > 0)
              grossProfit += (rev * retainmentRate(0).rate)
          }

          if(dOrder.order_status == "Shipped" ){
            qty_excl_pndg += dOrder.qty
            rev_excl_pndg += dOrder.actual_retail_price
            if(retainmentRate.size > 0)
              grossProfit_excl_pndg += (rev_excl_pndg * retainmentRate(0).rate)
          }

        }

      })


      var grossProfitUSD : BigDecimal = {if(currencyExchangeRate > 0) grossProfit * currencyExchangeRate else null}
      val revUSD : BigDecimal = {if(currencyExchangeRate > 0) rev * currencyExchangeRate else null}


      var grossProfitUSD_excl_pndg : BigDecimal = {if(currencyExchangeRate > 0) grossProfit_excl_pndg * currencyExchangeRate else null}
      val revUSD_excl_pndg : BigDecimal = {if(currencyExchangeRate > 0) rev_excl_pndg * currencyExchangeRate else null}


      if(grossProfit ==0 ){
        grossProfit = null
        grossProfitUSD = null
      }

      if(grossProfit_excl_pndg ==0 ){
        grossProfit_excl_pndg= null
        grossProfitUSD_excl_pndg = null
      }

      val salesdDt = Timestamp.from(localOrderSt.toInstant)

      //get daily sales
      val q = sql"""select * from sales.daily_sales
                where sales_date = '#${salesdDt}' and k_code = '#${kcode}'
                and sales_channel = '#${x._1._1}' and  product_base_code = '#${x._1._2}'
                 and product_sku_code = '#${x._1._3}'
                  """.as[DailySale]

      println(q.statements)

      var dailySales : Vector[DailySale] = null
      Await.result(
        session.db.run(q).map { res =>
          dailySales = res
        }, timeout)

      println("ids =>" +  dailySales.size)

      val ct =   Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)

      if(dailySales.size > 0){

        println(dailySales(0).id)
        println(dailySales(0).product_sku_code)
        println(qty)

        //update
        val update = sqlu""" UPDATE sales.daily_sales
                              SET revenue=#${ rev },
                              revenue_usd=#${revUSD},
                               gross_profit=#${grossProfit},
                                gross_profit_usd=#${grossProfitUSD },
                                qty=#${qty},
                                revenue_excl_pndg =#${rev_excl_pndg},
                                revenue_usd_excl_pndg=#${revUSD_excl_pndg},
                                gross_profit_excl_pndg=#${grossProfit_excl_pndg},
                                gross_profit_usd_excl_pndg=#${grossProfitUSD_excl_pndg},
                                qty_excl_pndg=#${qty_excl_pndg},
                              update_time= '#${ct}'
                              WHERE id= #${dailySales(0).id};
                                """

        println(update.statements)

        Await.result(
          session.db.run(update).map { res =>
            if(res != null ) println(res)
          }, timeout)

      }else{

        val o = z._2(0)

        val insert = sqlu""" INSERT INTO sales.daily_sales
                                   (sales_date, sales_channel, k_code, product_base_code,
                                    product_sku_code, product_name, asin, revenue, revenue_usd,
                                     gross_profit, gross_profit_usd, qty,
                                       create_time, update_time ,
                                      revenue_excl_pndg , revenue_usd_excl_pndg,
                                      gross_profit_excl_pndg, gross_profit_usd_excl_pndg, qty_excl_pndg
                                     )
                                   VALUES(
                                   '#${salesdDt}', '#${o.sales_channel}', '#${o.com_code}',
                                    '#${o.base_code}', '#${o.sku_code_by_drs}', '#${o.product_name}',
                                     '#${o.asin}',
                                     #${rev}, #${revUSD}, #${grossProfit}, #${grossProfitUSD}, #${qty},
                                      '#${ct}', '#${ct}' ,
                                      #${rev_excl_pndg}, #${revUSD_excl_pndg},
                                      #${grossProfit_excl_pndg},#${grossProfitUSD_excl_pndg}, #${qty_excl_pndg} );
                                """

        println(insert.statements)

        Await.result(
          session.db.run(insert).map { res =>
            println(res)
          }, timeout)


      }

      println("end")



    })



  }

  def getRetainmentRate(kcode : String , session: SlickSession  ): Vector[RetainmentRate]  = {

    import session.profile.api._

    val q = sql""" select * from retainment_rate
                  where supplier_company_id = (select id from company where k_code = '#${kcode}' )
                   order by   country_id , date_start """.as[RetainmentRate]

    var retainmentRateDS : Vector[RetainmentRate] = null
    Await.result(
      session.db.run(q).map { res =>
        // res is a Vector[String]
        retainmentRateDS = res

      }, timeout)

    retainmentRateDS

  }

  val refDate = LocalDate.of(2019, 9, 6)

  def getCurrencyExchagneRate(d:ZonedDateTime , currencyKey : Int , session: SlickSession): BigDecimal ={

    //
    import session.profile.api._

    if(currencyKey != 2){


      var stDate = Timestamp.from(d.toInstant)

      var endDate = Timestamp.from(d.withHour(12).withMinute(0)
        .withSecond(0).withNano(0).toInstant)

      if(d.toLocalDate.isAfter(refDate)){

        val bd = ZonedDateTime.now(ZoneOffset.UTC)
        val noon = bd.withDayOfMonth(12).withMinute(0).withSecond(0)
            .withNano(0)

        if(bd.isAfter(noon)){
          stDate = endDate
          endDate = Timestamp.from(d.plusDays(1).toInstant())
        }
      }


      val q = sql"""  select rate from currency_exchange
                         where date >= '#${stDate}' and date < '#${endDate}'
                         and src_currency_id = #${currencyKey} and dst_currency_id = 2
                         and interbank_rate = 0
                         order by date """.as[BigDecimal]



      var r : Vector[BigDecimal] = null
      Await.result(
        session.db.run(q).map { res =>
          // res is a Vector[String]
          if(res != null){
            r = res
          }
        }, timeout)

      if(r != null && r.size > 0){
        r(0)
      }else{
        0
      }
    }else{
      1
    }

  }



  def processDailySalesToExclRefund(startTime:LocalDateTime , endTime : LocalDateTime): Unit ={


    //every one hour , get this hour order data to settle

    implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
    import session.profile.api._


    //  and item_price > 0
    val q = sql"""select * from sales.all_orders
                   where
                   local_order_time <= '#${endTime}' and local_order_time >= '#${startTime}'
                    and ( sales_channel is not null and
                          sales_channel <> 'Non-Amazon' and sales_channel <> 'eBay'
                          and sales_channel <> 'Non-order'
                          and  sales_channel <> 'Webstore' and sales_channel <> '')
                   order by  com_code desc """.as[Order]

    println(q.statements)
    var orders : Vector[Order] = null
    Await.result(
      session.db.run(q).map { res =>
        // res is a Vector[String]
        orders = res
      }, timeout)

    val r: Map[String, Vector[Order]] = orders.groupBy(x=>x.com_code)

    r.foreach(kv=>{

      //r rate

      println(kv._1)

      //todo k101 per unit gross profit
      if(kv._1 == "K510"){

        val retainmentRates = getRetainmentRate(kv._1 , session)

        val gr: Map[(String, String, String), Vector[Order]] =  kv._2.groupBy(x=> (
          x.sales_channel , x.base_code , x.sku_code_by_drs  ))

        gr.foreach(x=>{

          val gr2: Map[LocalDate, Vector[Order]] =  x._2.groupBy(y=> {
            val lot = OffsetDateTime.ofInstant(Instant.ofEpochMilli(y.local_order_time),
              ZoneOffset.UTC)
              lot.toLocalDate
          })

          gr2.foreach(z=>{

            val m = com.kindminds.drs.Marketplace.fromName(x._1._1)
            val retainmentRate = retainmentRates.filter(x=> (
              (x.date_start.toLocalDate.isBefore(z._1) || x.date_start.toLocalDate.isEqual(z._1))
                & ( x.date_end.toLocalDate.isAfter(z._1) ||  x.date_end.toLocalDate.isEqual(z._1))
              & x.country_id == m.getCountry.getKey
            ))

            val localOrderSt =  ZonedDateTime.of(z._1,LocalTime.of(0,0,0,0),ZoneOffset.UTC)
            val currencyExchangeRate = getCurrencyExchagneRate(localOrderSt , m.getCurrency.getKey , session)


            //todo need fix below code
            val d = z._1
            val order = z._2(0)

            val q2 = sql"""
                   select *  from sales.all_orders
                                        where com_code = '#${order.com_code}'
                                        and sales_channel = '#${order.sales_channel}'
                                               and base_code ='#${order.base_code}'
                                                and sku_code_by_drs = '#${order.sku_code_by_drs}'
                                               and local_order_time::date = date '#${d}'
                                               order by DATE(local_order_time)""".as[Order]


            var dailyOrders : Vector[Order] = null
            Await.result(
              session.db.run(q2).map { res =>
                // res is a Vector[String]
                dailyOrders = res
              }, timeout)


            var grossProfit : BigDecimal = 0
            var rev : BigDecimal = 0
            var grossProfitUSD : BigDecimal = 0
            var revUSD : BigDecimal = 0
            var qty = 0

            var grossProfit_excl_pndg : BigDecimal = 0
            var rev_excl_pndg : BigDecimal = 0
            var grossProfitUSD_excl_pndg : BigDecimal = 0
            var revUSD_excl_pndg : BigDecimal = 0
            var qty_excl_pndg = 0

            var grossProfit_excl_refd : BigDecimal = 0
            var rev_excl_refd : BigDecimal = 0
            var grossProfitUSD_excl_refd : BigDecimal = 0
            var revUSD_excl_refd : BigDecimal = 0
            var qty_excl_refd = 0

            var grossProfit_refd : BigDecimal = 0
            var rev_refd : BigDecimal = 0
            var qty_refd = 0


            dailyOrders.foreach(dOrder=>{


              if(m == com.kindminds.drs.Marketplace.TRUETOSOURCE) {

                if(order.order_status == "paid"  || order.order_status == "partially_refunded"){
                  qty += order.qty
                  rev += order.actual_retail_price
                  qty_excl_pndg += order.qty
                  rev_excl_pndg += order.actual_retail_price
                  if (retainmentRate.size > 0) {
                    grossProfit += (rev * retainmentRate(0).rate)
                    grossProfit_excl_pndg += (rev_excl_pndg * retainmentRate(0).rate)
                  }

                }

                if(order.order_status == "refunded" ){
                  qty_refd += order.qty
                  rev_refd += order.actual_retail_price
                  if(retainmentRate.size > 0)
                    grossProfit_refd += (rev_refd * retainmentRate(0).rate)
                }


              }else{

                if((dOrder.order_status == "Shipped" || dOrder.order_status == "Pending") && dOrder.item_price > 0){
                  qty += dOrder.qty
                  rev += dOrder.actual_retail_price
                  if(retainmentRate.size > 0)
                    grossProfit += (rev * retainmentRate(0).rate)
                }

                if(dOrder.order_status == "Shipped" ){
                  qty_excl_pndg += dOrder.qty
                  rev_excl_pndg += dOrder.actual_retail_price
                  if(retainmentRate.size > 0)
                    grossProfit_excl_pndg += (rev_excl_pndg * retainmentRate(0).rate)
                }

                if(order.refund_dt_id != null ){
                  if(order.refund_dt_id.toInt > 0){
                    qty_refd += order.qty
                    rev_refd += order.actual_retail_price
                    if(retainmentRate.size > 0)
                      grossProfit_refd += (rev_refd * retainmentRate(0).rate)
                  }
                }


              }



            })


            grossProfitUSD  = {if(currencyExchangeRate > 0) grossProfit * currencyExchangeRate else null}
            revUSD  = {if(currencyExchangeRate > 0) rev * currencyExchangeRate else null}


            grossProfitUSD_excl_pndg  = {if(currencyExchangeRate > 0) grossProfit_excl_pndg * currencyExchangeRate else null}
            revUSD_excl_pndg  = {if(currencyExchangeRate > 0) rev_excl_pndg * currencyExchangeRate else null}


            qty_excl_refd = qty_excl_pndg - qty_refd
            rev_excl_refd = rev_excl_pndg - rev_refd
            grossProfit_excl_refd = grossProfit_excl_pndg - grossProfit_refd

            grossProfitUSD_excl_refd  = {if(currencyExchangeRate > 0) grossProfit_excl_refd * currencyExchangeRate else null}
            revUSD_excl_refd  = {if(currencyExchangeRate > 0) rev_excl_refd * currencyExchangeRate else null}

            if(grossProfit ==0 ){
              grossProfit = null
              grossProfitUSD = null
            }

            if(grossProfit_excl_pndg ==0 ){
              grossProfit_excl_pndg= null
              grossProfitUSD_excl_pndg = null
            }

            if(grossProfit_excl_refd ==0 ){
              grossProfit_excl_refd= null
              grossProfitUSD_excl_refd = null
            }


            //get daily sales
            val q = sql"""select * from sales.daily_sales
                where sales_date = '#${z._1}' and k_code = '#${kv._1}'
                and sales_channel = '#${x._1._1}' and  product_base_code = '#${x._1._2}'
                 and product_sku_code = '#${x._1._3}'
                  """.as[DailySale]

            println(q.statements)

            var dailySales : Vector[DailySale] = null
            Await.result(
              session.db.run(q).map { res =>
                dailySales = res
              }, timeout)

            println("ids =>" +  dailySales.size)


            if(dailySales.size > 0){

              println(dailySales(0).id)

              println(dailySales(0).product_sku_code)
              println(qty)

              //update
              val update = sqlu""" UPDATE sales.daily_sales
                              SET revenue=#${ rev },
                              revenue_usd=#${revUSD},
                               gross_profit=#${grossProfit},
                                gross_profit_usd=#${grossProfitUSD },
                                qty=#${qty},
                                revenue_excl_pndg =#${rev_excl_pndg},
                                revenue_usd_excl_pndg=#${revUSD_excl_pndg},
                                gross_profit_excl_pndg=#${grossProfit_excl_pndg},
                                gross_profit_usd_excl_pndg=#${grossProfitUSD_excl_pndg},
                                qty_excl_pndg=#${qty_excl_pndg},
                                 revenue_excl_refd =#${rev_excl_refd},
                                revenue_usd_excl_refd=#${revUSD_excl_refd},
                                 gross_profit_excl_refd=#${grossProfit_excl_refd},
                                 gross_profit_usd_excl_refd=#${grossProfitUSD_excl_refd},
                                  qty_excl_refd=#${qty_excl_refd},
                              update_time= '#${Timestamp.valueOf(OffsetDateTime.now().toLocalDateTime)}'
                              WHERE id= #${dailySales(0).id};
                                """

              println(update.statements)

              Await.result(
                session.db.run(update).map { res =>
                  if(res != null ) println(res)
                }, timeout)

            }else{

              val insert = sqlu""" INSERT INTO sales.daily_sales
                                   (sales_date, sales_channel, k_code, product_base_code,
                                    product_sku_code, product_name, asin, revenue, revenue_usd,
                                     gross_profit, gross_profit_usd, qty,
                                       create_time, update_time ,
                                      revenue_excl_pndg , revenue_usd_excl_pndg,
                                      gross_profit_excl_pndg, gross_profit_usd_excl_pndg, qty_excl_pndg,
                                     revenue_excl_refd , revenue_usd_excl_refd ,
                                     gross_profit_excl_refd, gross_profit_usd_excl_refd, qty_excl_refd
                                     )
                                   VALUES(
                                   '#${z._1}', '#${order.sales_channel}', '#${order.com_code}',
                                    '#${order.base_code}', '#${order.sku_code_by_drs}', '#${order.product_name}',
                                     '#${order.asin}',
                                     #${rev}, #${revUSD}, #${grossProfit}, #${grossProfitUSD}, #${qty},
                                      '#${Timestamp.valueOf(OffsetDateTime.now().toLocalDateTime())}',
                                       '#${Timestamp.valueOf(OffsetDateTime.now().toLocalDateTime())}' ,
                                      #${rev_excl_pndg}, #${revUSD_excl_pndg},
                                      #${grossProfit_excl_pndg},#${grossProfitUSD_excl_pndg}, #${qty_excl_pndg} ,
                                      #${rev_excl_refd}, #${revUSD_excl_refd},
                                      #${grossProfit_excl_refd},#${grossProfitUSD_excl_refd}, #${qty_excl_refd}
                                       );
                                """

              println(insert.statements)

              Await.result(
                session.db.run(insert).map { res =>
                  println(res)
                }, timeout)


            }

            println("end")



          })

        })

      }

    })


    session.close()

  }

}