package com.kindminds.drs.data.pipelines.core.sales

import java.sql.Timestamp
import java.time._
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, Materializer}
import com.kindminds.drs.data.pipelines.api.message._

import com.kindminds.drs.data.pipelines.core.dto.{DailySale, Order, RetainmentRate}
import slick.sql.SqlStreamingAction

import scala.concurrent.{Await, Future}


object DailySalesReloader {

  def props(drsDPCmdBus: ActorRef): Props = Props(new DailySalesReloader(drsDPCmdBus))

}

class DailySalesReloader(drsDPCmdBus: ActorRef) extends Actor with ActorLogging {


  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  val name = self.path.name

  drsDPCmdBus ! RegisterCommandHandler(name,classOf[ProcessDailySales].getName ,self)


  override def receive: Receive = {


    case a : Int =>

      val offset = 100
      val start = ( a * offset)

    case d : ProcessDailySales =>

      processDailySales(d.kcode)


  }

  implicit val session = SlickSession.forConfig("slick-postgres")
  import session.profile.api._
  val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

  def doDalesChannel(kcode:String ,
                      st:ZonedDateTime , et:ZonedDateTime , byLocalOrderTime:Boolean): SqlStreamingAction[Vector[String], String, Effect] = {

    if(byLocalOrderTime){
      sql""" select distinct sales_channel from sales.all_orders
                        where com_code = '#${kcode}'
                        and ( sales_channel is not null and
                        sales_channel <> 'Non-Amazon' and sales_channel <> 'eBay'
                        and sales_channel <> 'Non-order'
                        and  sales_channel <> 'Webstore' and sales_channel <> '')
                        and ( local_order_time >= '#${Timestamp.from(st.toInstant)}' and
                         local_order_time <=  '#${Timestamp.from(et.toInstant)}' ) """.as[String]
    }else{
      sql""" select distinct sales_channel from sales.all_orders
                        where com_code = '#${kcode}'
                        and ( sales_channel is not null and
                        sales_channel <> 'Non-Amazon' and sales_channel <> 'eBay'
                        and sales_channel <> 'Non-order'
                        and  sales_channel <> 'Webstore' and sales_channel <> '')
                        and ( last_update_date >= '#${Timestamp.from(st.toInstant)}' and
                         last_update_date <=  '#${Timestamp.from(et.toInstant)}' ) """.as[String]
    }


  }

  def doProductBaseCode(kcode:String , sc:String ,
                        st:ZonedDateTime , et:ZonedDateTime , byLocalOrderTime:Boolean): SqlStreamingAction[Vector[String], String, Effect] = {

    if(byLocalOrderTime){
      sql""" select distinct base_code from sales.all_orders
                    where com_code = '#${kcode}'
                      and sales_channel = '#${sc}'
                      and ( local_order_time >= '#${Timestamp.from(st.toInstant)}' and
                        local_order_time <=  '#${Timestamp.from(et.toInstant)}' )
                   """.as[String]
    }else{
      sql""" select distinct base_code from sales.all_orders
                    where com_code = '#${kcode}'
                      and sales_channel = '#${sc}'
                      and ( last_update_date >= '#${Timestamp.from(st.toInstant)}' and
                        last_update_date <=  '#${Timestamp.from(et.toInstant)}' )
                   """.as[String]
    }

  }

  def doProductSkuCode(kcode:String , sc:String ,
                       st:ZonedDateTime , et:ZonedDateTime , byLocalOrderTime:Boolean): Unit ={

  }

  def processDailySales(kcode : String): Unit ={

    //Source(kcodeList).runForeach(kcode =>{

    println(kcode)

    if(kcode != "K101"){

      val q = sql""" select * from retainment_rate
                  where supplier_company_id = (select id from company where k_code = '#${kcode}' )
                   order by   country_id , date_start """.as[RetainmentRate]
      var retainmentRateDS : Vector[RetainmentRate] = null

      val r: Future[Unit] =   session.db.run(q).map { res =>
        // res is a Vector[String]
        retainmentRateDS = res

      }

      r.onComplete {
        case _ => {


          //todo ebay TrueToSource
          //todo truetosource need filter refunded

          val st: ZonedDateTime = ZonedDateTime.of(2019,10,30,0,0,0,0,ZoneOffset.UTC)
          val et: ZonedDateTime = st.plusDays(1).minusNanos(1)

          println(et)



          val done = Slick.source(doDalesChannel(kcode,st,et , true))
            .log("error logging")
            .runForeach(
              sc =>{
                println(sc)
                Slick.source(doProductBaseCode(kcode,sc,st,et,true))
                  .log("error logging")
              }.runForeach( bp =>{

                println("BBB")
                println(sc)
                println(bp)
                Slick.source(sql"""
                    select distinct sku_code_by_drs from sales.all_orders
                    where com_code = '#${kcode}'  and sales_channel = '#${sc}'
                            and base_code ='#${bp}'
                            and ( local_order_time >= '#${Timestamp.from(st.toInstant)}' and
                            local_order_time <=  '#${Timestamp.from(et.toInstant)}' )
                  """.as[String])

              }.runForeach(sku=>{

                println("SSSSSS")
                println(sku)
                Slick.source(sql"""
                    select distinct Date(local_order_time) as local_order_time
                    from sales.all_orders
                     where com_code = '#${kcode}'  and sales_channel = '#${sc}'
                            and base_code ='#${bp}'  and sku_code_by_drs = '#${sku}'
                            and (order_status <> 'Canceled' and order_status <>'CANCELLED' )
                            and ( local_order_time >= '#${Timestamp.from(st.toInstant)}' and
                            local_order_time <=  '#${Timestamp.from(et.toInstant)}' )
                            order by Date(local_order_time)
                             """.as[String])

              }.runForeach(d =>{

                println("EEEEEEEEEEEEEEEEEE")
                println(d)
                Slick.source(sql"""
                    select *  from sales.all_orders
                     where com_code = '#${kcode}'  and sales_channel = '#${sc}'
                            and base_code ='#${bp}'  and sku_code_by_drs = '#${sku}'
                            and (order_status <> 'Canceled' and order_status <>'CANCELLED' )
                            and  (local_order_time) ::date = date '#${d}'
                            order by Date(local_order_time)
                             """.as[Order]).fold(( List.empty[Order]))((acc, curr) => {
                  acc :+ curr
                })
              }.log("error logging")
                .runForeach(orders => {
                  println("Source fold result")

                  println(orders.size)

                  val o = orders(0)

                  val lot = OffsetDateTime.ofInstant(Instant.ofEpochMilli(o.local_order_time), ZoneOffset.UTC)

                  val m = com.kindminds.drs.Marketplace.fromName(o.sales_channel)
                  println("MMMMMMMMMMMMMMMMMMM")

                  println(d)

                  //if (o.marketplace_order_id == "113-6705478-7627457"){

                  println(m)


                  if(m == com.kindminds.drs.Marketplace.AMAZON_COM ||
                    m == com.kindminds.drs.Marketplace.TRUETOSOURCE ){

                    println("US")
                    println(orders(0).marketplace_order_id)
                    saveDailySales(orders, retainmentRateDS , 1)


                  }else{

                    println("Not US")

                    println(lot)
                    //val dateStr = d.toLocalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                    val stDate = lot.withHour(0).withMinute(0).withSecond(0).withNano(0)
                    val endDate = stDate.withHour(12)

                    /* todo currency date
                    val refDate = LocalDate.of(2019, 9, 6)
                    var stDate = d.toLocalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    var endDate = stDate + " 12:00:00"

                    if(d.toLocalDate.isAfter(refDate)){
                      if(OffsetDateTime.now.isAfter(OffsetDateTime.now().withHour(12).withMinute(0).withSecond(0))){
                        stDate = endDate
                        endDate =  d.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                      }
                    }*/


                    Slick.source(sql"""  select rate from currency_exchange
                                where date >= '#${stDate}' and date < '#${endDate}'
                            and src_currency_id = #${m.getCurrency.getKey} and dst_currency_id = 2
                            and interbank_rate = 0
                            order by date
                             """.as[BigDecimal]).runForeach(c=>{

                        saveDailySales(orders, retainmentRateDS , c)


                    })


                  }
                  //} // end test


                })


              )))
            )


          done.onComplete {
            case _ => {
              // session.close()
              drsDPCmdBus ! CompleteRefreshDailySales()
              println("DoneDoneDoneDoneDone 222")                                                       // (10)
            }
          }

        }
      }

    }else{
      //session.close()
      drsDPCmdBus ! CompleteRefreshDailySales()
    }


    //}) //end source


  }

  def checkDailySalesExisted(ds:DailySale): Boolean ={

    val sd = Timestamp.from(ds.sales_date
            .withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant)


    val q = sql"""select * from sales.daily_sales
                where sales_date = '#${sd}' and k_code = '#${ds.k_code}'
                and sales_channel = '#${ds.sales_channel}' and  product_base_code = '#${ds.product_base_code}'
                 and product_sku_code = '#${ds.product_sku_code}'
                  """.as[DailySale]

    println(q.statements)

    var dailySales : Vector[DailySale] = null
    Await.result(
      session.db.run(q).map { res =>
        dailySales = res
      }, timeout)

    println("ids =>" +  dailySales.size)

    if(dailySales.size > 0) true else false
  }

  def saveDailySales(orders: List[Order] , retainmentRateDS : Vector[RetainmentRate] ,
                     fxRate:BigDecimal): Unit ={

    val ds = createDailySales(orders, retainmentRateDS , fxRate)


    val isDailySalesExisted = checkDailySalesExisted(ds)

    println("CCCCCCCCCCCCCCc")

    Source(List(ds)).via(Slick.flow(x=>{

      val sd = Timestamp.from(ds.sales_date
        .withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant)

      val now = Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)

      if(isDailySalesExisted){

        //update
        val i =   (sqlu"""
                         UPDATE sales.daily_sales
                         SET
                          product_name='#${x.product_name}', asin='#${x.asin}', revenue=#${x.revenue},
                          revenue_usd=#${x.revenue_usd}, gross_profit=#${x.gross_profit},
                           gross_profit_usd=#${x.gross_profit_usd},
                          qty=#${x.qty},
                          update_time='#${now}',
                           revenue_excl_pndg=#${x.revenue_excl_pndg},
                           revenue_usd_excl_pndg=#${x.revenue_usd_excl_pndg},
                           gross_profit_excl_pndg=#${x.gross_profit_excl_pndg},
                            gross_profit_usd_excl_pndg=#${x.gross_profit_usd_excl_pndg},
                           qty_excl_pndg=#${x.qty_excl_pndg}, revenue_excl_refd=#${x.revenue_excl_refd},
                           revenue_usd_excl_refd=#${x.revenue_usd_excl_refd},
                           gross_profit_excl_refd=#${x.gross_profit_excl_refd},
                           gross_profit_usd_excl_refd=#${x.gross_profit_usd_excl_refd},
                            qty_excl_refd=#${x.qty_excl_refd}
                          where sales_date = '#${sd}' and k_code = '#${ds.k_code}'
                            and sales_channel = '#${ds.sales_channel}'
                            and  product_base_code = '#${ds.product_base_code}'
                            and product_sku_code = '#${ds.product_sku_code}'

                               );
                          """)

        println(i.statements)
        i
      }else{

        val i =   (sqlu"""
                            INSERT INTO sales.daily_sales
                           (sales_date, sales_channel, k_code, product_base_code,
                           product_sku_code, product_name, asin, revenue , revenue_usd , gross_profit ,
                           gross_profit_usd , qty , create_time , update_time ,
                            revenue_excl_pndg , revenue_usd_excl_pndg,
                            gross_profit_excl_pndg, gross_profit_usd_excl_pndg, qty_excl_pndg,
                            revenue_excl_refd , revenue_usd_excl_refd,
                            gross_profit_excl_refd, gross_profit_usd_excl_refd, qty_excl_refd
                            )
                           VALUES('#${sd}', '#${x.sales_channel}', '#${x.k_code}',
                            '#${x.product_base_code}', '#${x.product_sku_code}', '#${x.product_name}',
                             '#${x.asin}', #${x.revenue} , #${x.revenue_usd} , #${x.gross_profit}
                              , #${x.gross_profit_usd} , #${x.qty} ,
                               '#${now}',
                               '#${now}' ,
                               #${x.revenue_excl_pndg} , #${x.revenue_usd_excl_pndg} , #${x.gross_profit_excl_pndg},
                               #${x.gross_profit_usd_excl_pndg} , #${x.qty_excl_pndg} ,
                                #${x.revenue_excl_refd} , #${x.revenue_usd_excl_refd} , #${x.gross_profit_excl_refd},
                                #${x.gross_profit_usd_excl_refd} , #${x.qty_excl_refd}
                               );
                          """)

        println(i.statements)
        i
      }

    })).runWith(Sink.ignore)


  }



  def createDailySales(orders:List[Order] ,retainmentRateDS: Vector[RetainmentRate] , fxRate:BigDecimal): DailySale ={

    val o = orders(0)
    val d = OffsetDateTime.ofInstant(Instant.ofEpochMilli(o.local_order_time), ZoneOffset.UTC)

    println(o.local_order_time)
    println(d)


    val m = com.kindminds.drs.Marketplace.fromName(o.sales_channel)

    println("A")
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

    println("B")
    val r: Seq[RetainmentRate] = retainmentRateDS.filter(x=> (
      (x.date_start.toLocalDate.isBefore(d.toLocalDate) || x.date_start.toLocalDate.isEqual(d.toLocalDate))
        & (x.date_end.toLocalDate.isAfter(d.toLocalDate) || x.date_end.toLocalDate.isEqual(d.toLocalDate))
        & x.country_id == m.getCountry.getKey ))


    orders.foreach(order=>{

      println("C")
      if(m == com.kindminds.drs.Marketplace.TRUETOSOURCE){

        println("D")
        if(order.order_status == "paid"  || order.order_status == "partially_refunded"){
          qty += order.qty
          rev += order.actual_retail_price
          qty_excl_pndg += order.qty
          rev_excl_pndg += order.actual_retail_price
          if(r.size > 0){
            grossProfit += (rev * r(0).rate)
            grossProfit_excl_pndg += (rev_excl_pndg * r(0).rate)
          }

        }

        println("E")
        if(order.order_status == "refunded" ){
          qty_refd += order.qty
          rev_refd += order.actual_retail_price
          if(r.size > 0)
            grossProfit_refd += (rev_refd * r(0).rate)
        }


      }else {

        println("F")
        if((order.order_status == "Shipped" || order.order_status == "Pending") && order.item_price > 0 ){
          qty += order.qty
          rev += order.actual_retail_price
          if(r.size > 0)
            grossProfit += (rev * r(0).rate)
        }


        println("G")
        if(order.order_status == "Shipped" ){
          qty_excl_pndg += order.qty
          rev_excl_pndg += order.actual_retail_price
          if(r.size > 0)
            grossProfit_excl_pndg += (rev_excl_pndg * r(0).rate)
        }

        println("H")
        if(order.refund_dt_id != null ){
          if(order.refund_dt_id.toInt > 0){
            qty_refd += order.qty
            rev_refd += order.actual_retail_price
            if(r.size > 0)
              grossProfit_refd += (rev_refd * r(0).rate)
          }
        }



      }



    })

    println("I")
    grossProfitUSD  = {if(fxRate > 0) grossProfit * fxRate else null}
    revUSD  = {if(fxRate > 0) rev * fxRate else null}


    grossProfitUSD_excl_pndg  = {if(fxRate > 0) grossProfit_excl_pndg * fxRate else null}
    revUSD_excl_pndg  = {if(fxRate > 0) rev_excl_pndg * fxRate else null}


    qty_excl_refd = qty_excl_pndg - qty_refd
    rev_excl_refd = rev_excl_pndg - rev_refd
    grossProfit_excl_refd = grossProfit_excl_pndg - grossProfit_refd

    grossProfitUSD_excl_refd  = {if(fxRate > 0) grossProfit_excl_refd * fxRate else null}
    revUSD_excl_refd  = {if(fxRate > 0) rev_excl_refd * fxRate else null}


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

    println("J")


    DailySale(0, d ,o.sales_channel,o.com_code,o.base_code,
      o.sku_code_by_drs,o.product_name, o.asin , rev , revUSD , grossProfit , grossProfitUSD, qty ,
      OffsetDateTime.now , OffsetDateTime.now,
      rev_excl_pndg,revUSD_excl_pndg,
      grossProfit_excl_pndg,grossProfitUSD_excl_pndg,
      qty_excl_pndg ,
      rev_excl_refd,
      revUSD_excl_refd,
      grossProfit_excl_refd,
      grossProfitUSD_excl_refd,
      qty_excl_refd)



  }







}