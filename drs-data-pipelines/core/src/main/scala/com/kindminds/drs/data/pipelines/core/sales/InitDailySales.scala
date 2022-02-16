package com.kindminds.drs.data.pipelines.core.sales

import java.sql.Timestamp
import java.time._
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import akka.Done
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import com.kindminds.drs.Currency
import com.kindminds.drs.data.pipelines.api.message._

import com.kindminds.drs.data.pipelines.core.dto.{DailySale, Order, RetainmentRate}
import com.kindminds.drs.data.pipelines.core.settlement.DTran.timeout
import slick.jdbc.GetResult

import scala.concurrent.{Await, Future}

object InitDailySales {

  def props(drsDPCmdBus: ActorRef): Props = Props(new InitDailySales(drsDPCmdBus))

}

class InitDailySales(drsDPCmdBus: ActorRef) extends Actor with ActorLogging {


  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  val name = self.path.name

  drsDPCmdBus ! RegisterCommandHandler(name,classOf[CreateDailySales].getName ,self)


  override def receive: Receive = {


    case a : Int =>

      val offset = 100
      val start = ( a * offset)

    case d : CreateDailySales =>

      processDailySales(d.kcode)


  }

  implicit val session = SlickSession.forConfig("slick-postgres")
  import session.profile.api._

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


          val done = Slick.source(sql""" select distinct sales_channel from sales.all_orders
                        where com_code = '#${kcode}'
                        and ( sales_channel is not null and
                        sales_channel <> 'Non-Amazon' and sales_channel <> 'eBay'
                        and sales_channel <> 'Non-order'
                        and  sales_channel <> 'Webstore' and sales_channel <> '') """.as[String])
            .log("error logging")
            .runForeach(
              sc =>{
                println(sc)
                Slick.source(sql""" select distinct base_code from sales.all_orders
                    where com_code = '#${kcode}'
                      and sales_channel = '#${sc}' """.as[String])
                  .log("error logging")
              }.runForeach( bp =>{

                println("BBB")
                println(sc)
                println(bp)
                Slick.source(sql""" select distinct sku_code_by_drs from sales.all_orders
                    where com_code = '#${kcode}'  and sales_channel = '#${sc}'
                            and base_code ='#${bp}' """.as[String])

              }.runForeach(sku=>{

                println("SSSSSS")
                println(sku)
                Slick.source(sql""" select distinct DATE(local_order_time) from sales.all_orders
                     where com_code = '#${kcode}'  and sales_channel = '#${sc}'
                            and base_code ='#${bp}'  and sku_code_by_drs = '#${sku}'
                            and (order_status <> 'Canceled' and order_status <>'CANCELLED' )
                            order by DATE(local_order_time)
                             """.as[String])

              }.runForeach(d =>{

                println("EEEEEEEEEEEEEEEEEE")
                println(d)
                Slick.source(sql""" select *  from sales.all_orders
                     where com_code = '#${kcode}'  and sales_channel = '#${sc}'
                            and base_code ='#${bp}'  and sku_code_by_drs = '#${sku}'
                            and (order_status <> 'Canceled' and order_status <>'CANCELLED' )
                            and local_order_time::date = date '#${d}'
                            order by DATE(local_order_time)
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

                    val ds = createDailySales(orders, retainmentRateDS , 1)

                    println("CCCCCCCCCCCCCCc")

                    Source(List(ds)).via(Slick.flow(x=>{

                      val salesDate = x.sales_date.toLocalDate

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
                           VALUES('#${salesDate}', '#${x.sales_channel}', '#${x.k_code}',
                            '#${x.product_base_code}', '#${x.product_sku_code}', '#${x.product_name}',
                             '#${x.asin}', #${x.revenue} , #${x.revenue_usd} , #${x.gross_profit}
                              , #${x.gross_profit_usd} , #${x.qty} ,
                               '#${Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)}',
                               '#${Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)}' ,
                               #${x.revenue_excl_pndg} , #${x.revenue_usd_excl_pndg} , #${x.gross_profit_excl_pndg},
                               #${x.gross_profit_usd_excl_pndg} , #${x.qty_excl_pndg} ,
                                #${x.revenue_excl_refd} , #${x.revenue_usd_excl_refd} , #${x.gross_profit_excl_refd},
                                #${x.gross_profit_usd_excl_refd} , #${x.qty_excl_refd}
                               );
                          """)

                      println(i.statements)
                      i
                    })).runWith(Sink.ignore)

                  }else{

                    println("Not US")

                    println(d)
                    //val dateStr = d.toLocalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                    val stDate = lot.withHour(0).withMinute(0).withSecond(0).withNano(0)
                    val endDate = stDate.withHour(12)

                    /*
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


                      val ds = createDailySales(orders, retainmentRateDS , c)

                      println("CCCCCCCCCCCCCCc")

                      Source(List(ds)).via(Slick.flow(x=>{

                        val salesDate = x.sales_date.toLocalDate

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
                           VALUES('#${salesDate}', '#${x.sales_channel}', '#${x.k_code}',
                            '#${x.product_base_code}', '#${x.product_sku_code}', '#${x.product_name}',
                             '#${x.asin}', #${x.revenue} , #${x.revenue_usd} , #${x.gross_profit}
                              , #${x.gross_profit_usd} , #${x.qty} ,
                               '#${Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)}',
                               '#${Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)}' ,
                               #${x.revenue_excl_pndg} , #${x.revenue_usd_excl_pndg} , #${x.gross_profit_excl_pndg},
                               #${x.gross_profit_usd_excl_pndg} , #${x.qty_excl_pndg},
                                #${x.revenue_excl_refd} , #${x.revenue_usd_excl_refd} , #${x.gross_profit_excl_refd},
                                #${x.gross_profit_usd_excl_refd} , #${x.qty_excl_refd}
                                );
                          """)

                        println(i.statements)
                        i
                      })).runWith(Sink.ignore)

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



  def createDailySales(orders:List[Order] ,retainmentRateDS: Vector[RetainmentRate] , fxRate:BigDecimal): DailySale ={

    val o = orders(0)
    val d = OffsetDateTime.ofInstant(Instant.ofEpochMilli(o.local_order_time), ZoneOffset.UTC)

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