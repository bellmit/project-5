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

import com.kindminds.drs.data.pipelines.core.dto.{DailyRetainment, DailySale, Order, RetainmentRate}
import com.kindminds.drs.data.pipelines.core.settlement.DTran.timeout
import slick.jdbc.GetResult

import scala.concurrent.{Await, Future}



object InitRetainment {

  def props(drsDPCmdBus: ActorRef): Props = Props(new InitRetainment(drsDPCmdBus))

}

class InitRetainment(drsDPCmdBus: ActorRef) extends Actor with ActorLogging {


  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  val name = self.path.name

  drsDPCmdBus ! RegisterCommandHandler(name,classOf[CreateDailyRetainment].getName ,self)


  override def receive: Receive = {


    case a : Int =>

      val offset = 100
      val start = ( a * offset)

    case d : CreateDailyRetainment =>

      println(d.kcode)

      processDailyRetainment(d.kcode)


  }

  implicit val session = SlickSession.forConfig("slick-postgres")
  import session.profile.api._

  def processDailyRetainment(kcode : String): Unit ={

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

                //todo arthur filter by local order time
                // 2020 01 01 ~ 2020 ~ 02 -29
                Slick.source(sql""" select distinct DATE(local_order_time) from sales.all_orders
                     where com_code = '#${kcode}'  and sales_channel = '#${sc}'
                            and base_code ='#${bp}'  and sku_code_by_drs = '#${sku}'
                            and (order_status <> 'Canceled' and order_status <>'CANCELLED' )
                            and local_order_time::date >= date '2020-05-24'
                            and local_order_time::date < date '2020-06-01'
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

                  println(o.local_order_time)

                  val lot = OffsetDateTime.ofInstant(Instant.ofEpochMilli(o.local_order_time), ZoneOffset.UTC)
                  println(lot)

                  val m = com.kindminds.drs.Marketplace.fromName(o.sales_channel)
                  println("MMMMMMMMMMMMMMMMMMM")

                  println(d)

                  //if (o.marketplace_order_id == "113-6705478-7627457"){

                  println(m)

                  if(m == com.kindminds.drs.Marketplace.AMAZON_COM ||
                    m == com.kindminds.drs.Marketplace.TRUETOSOURCE ){

                    println("US")
                    println(orders(0).marketplace_order_id)

                    val ds = createDailyRetainment(orders, retainmentRateDS , 1)

                    println("CCCCCCCCCCCCCCc")

                    Source(List(ds)).via(Slick.flow(x=>{

                      val salesDate = x.sales_date.toLocalDate

                      val i =   (sqlu"""
                            INSERT INTO sales.daily_retainment
                           (sales_date, sales_channel, k_code, product_base_code,
                           product_sku_code, product_name, asin, retainment ,
                           retainment_usd , qty , create_time , update_time
                            )
                           VALUES('#${salesDate}', '#${x.sales_channel}', '#${x.k_code}',
                            '#${x.product_base_code}', '#${x.product_sku_code}', '#${x.product_name}',
                             '#${x.asin}', #${x.retainment} , #${x.retainment_usd} ,#${x.qty} ,
                               '#${Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)}',
                               '#${Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)}'
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

                    val q3 = sql"""  select date(sp.period_end) from settlement_period sp
                                      where sp.period_start ::date <= date '#${d}'
                                        and sp.period_end ::date >= date '#${d}' """.as[String]

                    var sed : String = null
                    Await.result(
                      session.db.run(q3).map { res =>

                        // log.info(res.size)
                        sed = res(0)

                      }, timeout)

                    import java.time.LocalDate
                    import java.time.format.DateTimeFormatter

                    println("SEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDS")
                    println(sed)
                    println(sed)
                    println(sed)
                    println("SEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDSSEDS")

                    val cd = LocalDate.parse(sed , DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    val nd = cd.minusDays(2)

                    println("FRIDAYFRIDAYFRIDAYFRIDAYFRIDAYFRIDAYFRIDAY")
                    println(nd)
                    println(m.getCurrency.getKey)
                    println("FRIDAYFRIDAYFRIDAYFRIDAYFRIDAYFRIDAYFRIDAY")

                    Slick.source(sql"""  select rate from currency_exchange
                                where date ::date = date '#${nd}'
                            and src_currency_id = #${m.getCurrency.getKey} and dst_currency_id = 2
                            and interbank_rate = 0
                            order by date
                             """.as[BigDecimal]).runForeach(c=>{

                      println("get get currency rate")
                      println(nd)
                      println(c)
                      println("get get currency rate")



                      val ds = createDailyRetainment(orders, retainmentRateDS , c)

                      println("CCCCCCCCCCCCCCc")

                      Source(List(ds)).via(Slick.flow(x=>{

                        val salesDate = x.sales_date.toLocalDate

                        val i =   (sqlu"""
                            INSERT INTO sales.daily_retainment
                           (sales_date, sales_channel, k_code, product_base_code,
                           product_sku_code, product_name, asin, retainment , retainment_usd
                           , qty , create_time , update_time
                            )
                           VALUES('#${salesDate}', '#${x.sales_channel}', '#${x.k_code}',
                            '#${x.product_base_code}', '#${x.product_sku_code}', '#${x.product_name}',
                             '#${x.asin}', #${x.retainment} , #${x.retainment_usd} , #${x.qty} ,
                               '#${Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)}',
                               '#${Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)}'
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
              drsDPCmdBus ! CompleteRefreshDailyRetainment()
              println("DoneDoneDoneDoneDone 222")                                                       // (10)
            }
          }

        }
      }

    }else{
      //session.close()
      drsDPCmdBus ! CompleteRefreshDailyRetainment()
    }


    //}) //end source


  }



  def createDailyRetainment(orders:List[Order] ,retainmentRateDS: Vector[RetainmentRate] ,
                            fxRate:BigDecimal): DailyRetainment ={

    val o = orders(0)
    val d = OffsetDateTime.ofInstant(Instant.ofEpochMilli(o.local_order_time), ZoneOffset.UTC)

    val m = com.kindminds.drs.Marketplace.fromName(o.sales_channel)

    println("A")

    var retainment : BigDecimal = 0
    var retainmentUSD : BigDecimal = 0
    var qty = 0

    println("B")

    val r: Seq[RetainmentRate] = retainmentRateDS.filter(x=> (
      (x.date_start.toLocalDate.isBefore(d.toLocalDate) || x.date_start.toLocalDate.isEqual(d.toLocalDate))
        & (x.date_end.toLocalDate.isAfter(d.toLocalDate) || x.date_end.toLocalDate.isEqual(d.toLocalDate))
        & x.country_id == m.getCountry.getKey ))


    println("OOOOOOOOOOOOOOOOOO size  size size")
    println(orders.size)
    println("OOOOOOOOOOOOOOOOOO size size size" )

    println("rate ratearae")
    r(0).rate
    println("rate ratearae")

    orders.foreach(order=>{

      println("C")
      if(m == com.kindminds.drs.Marketplace.TRUETOSOURCE){

        println("D")
        if(order.order_status == "paid"  || order.order_status == "partially_refunded"){
          qty += order.qty
          if(r.size > 0){
            retainment += (order.item_price * r(0).rate)
          }

        }


      }else {

        println("F")
        if((order.order_status == "Shipped" || order.order_status == "Pending") && order.item_price > 0 ){
          qty += order.qty

          if(r.size > 0)
            retainment += (order.item_price * r(0).rate)
        }



      }



    })

    println("I")

    retainmentUSD  = {if(fxRate > 0) retainment * fxRate else null}

    println("retainmentretainmentretainmentretainmentretainmentretainmentretainmentretainmentretainment")
    println(retainment)
    println(retainmentUSD)

    println("J")


    DailyRetainment(0, d ,o.sales_channel,o.com_code,o.base_code,
      o.sku_code_by_drs,o.product_name, o.asin , retainment , retainmentUSD , qty ,
      OffsetDateTime.now , OffsetDateTime.now)



  }







}