package com.kindminds.drs.data.pipelines.core.sales

import java.sql.Timestamp
import java.time.{OffsetDateTime, ZoneId, ZoneOffset, ZonedDateTime}
import java.util.concurrent.TimeUnit

import akka.Done
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.Sink
import com.kindminds.drs.api.message.command.RefreshAllOrdersTransaction
import com.kindminds.drs.api.message.command.amazon.order.RequestAmazonOrder

import com.kindminds.drs.data.pipelines.api.message._

import com.kindminds.drs.data.pipelines.core.dto.{Order, OrderView}
import com.kindminds.drs.data.pipelines.core.es.OrderES
import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel
import slick.jdbc.GetResult

import scala.concurrent.{Await, Future, TimeoutException}

object AllOrders {

  def props(drsDPCmdBus: ActorRef): Props = Props(new AllOrders(drsDPCmdBus))

}

class AllOrders(drsDPCmdBus: ActorRef) extends Actor with ActorLogging {


  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  val name = self.path.name

  drsDPCmdBus ! RegisterCommandHandler(name,classOf[StartRefreshAllOrders].getName ,self)
  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshAllOrders].getName ,self)
  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshMissingAllOrders].getName ,self)
  drsDPCmdBus ! RegisterCommandHandler(name,classOf[StartRefreshMissingOrders].getName ,self)
  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshAllOrdersTransaction].getName ,self)
  drsDPCmdBus ! RegisterCommandHandler(name,classOf[StartRefreshOrdersByDate].getName ,self)
  drsDPCmdBus ! RegisterCommandHandler(name,classOf[StartRefreshNotUpdatedOrders].getName ,self)



  override def receive: Receive = {

    case s : StartRefreshAllOrders =>

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._

      val db = session.db

      val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)
      val timeout2 = scala.concurrent.duration.FiniteDuration(30, TimeUnit.SECONDS)

      val refreshPv = sqlu""" refresh materialized view pv.all_orders  """
      var isRefresh = true

      try {
        Await.result(
          db.run(refreshPv).map { res =>
            // res is a Vector[String]
            //log.info(res)

          }, timeout2)
      } catch {
        case e: TimeoutException => {
          log.info("Time out Time out Time out Time out Time out Time out Time out Time out Time out ")
          session.close()
          isRefresh = false
        }
      }

      if(isRefresh){

        try {
          val q = sql""" select max(last_update_date) from sales.all_orders """.as[java.sql.Timestamp]
          var d2 : ZonedDateTime  = null

          Await.result(
            db.run(q).map { res =>
              d2 = ZonedDateTime.ofInstant(res(0).toInstant, ZoneOffset.UTC)
            }, timeout)

          //println(d2)


          var ids : String = ""
          val q2 = sql""" select distinct marketplace_order_id from pv.all_orders
                    where last_update_date > '#${Timestamp.from(d2.toInstant)}' """.as[String]
          Await.result(
            db.run(q2).map { res =>

              //log.info(res.size)

              res.foreach(x=>{
                ids += "'" + x +"',"
              })

            }, timeout)


          if(ids.length > 0){
            ids = ids.substring(0,ids.length-1)
            log.info(ids)


            println(ids)
            val q3 = sql"""  select id  from sales.all_orders where marketplace_order_id in  (#${ids}) """.as[Int]

            var allOrderId : List[Int] = null
            Await.result(
              db.run(q3).map { res =>

                //log.info(res.size)

                allOrderId = res.toList

              }, timeout)

            log.info(d2.toString)

            drsDPCmdBus ! RefreshAllOrders(allOrderId,d2 ,d2.plusDays(1))
            log.info("HAS DDATA")
          }

        } catch {
          case e: TimeoutException => {
            log.info("Time out Time out Time out Time out Time out Time out Time out Time out Time out ")
            session.close()
          }
        }


        session.close()

      }


    case m : StartRefreshMissingOrders =>

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._

      val db = session.db

      val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

        try {

          var missingMarketPlaceOrderIds: List[String] = null
          val q2 = sql""" select distinct(s.marketplace_order_id)  from pv.all_orders as s
                        		left join sales.all_orders as a on s.marketplace_order_id = a.marketplace_order_id
                        where a.id is null """.as[String]
          Await.result(
            db.run(q2).map { res =>

              //log.info(res.size)

              res.foreach(x => {
                log.info(x)
              })

              missingMarketPlaceOrderIds = res.toList


            }, timeout)


          if(missingMarketPlaceOrderIds.length > 0) {

             drsDPCmdBus ! RefreshMissingAllOrders(missingMarketPlaceOrderIds)
            log.info("HAS DDATA")
          }


        } catch {
          case e: TimeoutException => {
            log.info("Time out Time out Time out Time out Time out Time out Time out Time out Time out ")
            session.close()
          }
        }


        session.close()


    case refresh : RefreshAllOrders =>

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._
      val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

      refresh.allOrderIds.foreach(x=>{
        log.info(x.toString)
      })


      log.info("CCCCCCCCCCCCCCCC")
      log.info(refresh.startTime.toString)


      var ids : String = ""
      refresh.allOrderIds.foreach(x=>{
        ids += x + ","
      })

      if(ids.length > 0){
        ids = ids.substring(0,ids.length -1)
        log.info(ids)
        val q3 = sql"""  delete from sales.all_orders where id in ( #$ids )""".as[String]

        Await.result(
          session.db.run(q3).map { res =>
            //log.info(res.size)
          }, timeout)
      }


      var c1 = 0
      log.info("insertinsertinsertinsert")

      val querySql =  sql""" select * from pv.all_orders where
                  last_update_date > '#${Timestamp.from(refresh.startTime.toInstant)}'
                  and  last_update_date < '#${Timestamp.from(refresh.endTime.toInstant)}'
                  order by last_update_date """.as[OrderView]

      log.info(querySql.statements.toString())

      val done: Future[Done] =
      Slick
        .source(querySql)
        .runWith(
          Slick.sink(o => {

            log.info("AAAAAAAAAAAAA")
            val lud = if (o.last_update_date != null) "'" + o.last_update_date + "'" else null
            log.info(lud)
            val ot = if (o.order_time != null) "'" + o.order_time + "'" else null
            log.info(ot)

            val lot = processLocalOrderTime(o.sales_channel, o.order_time)
            //val lot = null
            log.info(lot)

            val sc = processSalesChannel(o.sales_channel, o.marketplace_order_id)
            log.info(sc)

            val tt = if (o.transaction_time != null) "'" + o.transaction_time + "'" else null

            val pn = if (o.product_name.contains("'")) o.product_name.replace("'", "''") else o.product_name

            val buyer = if (o.buyer.contains("'")) o.buyer.replace("'", "''") else o.buyer

            val city = if (o.city.contains("'")) o.city.replace("'", "''") else o.city

            c1 += 1
            log.info(c1.toString)

            val i = (sqlu"""
                    INSERT INTO sales.all_orders
                        (source_id, last_update_date, order_time,
                         local_order_time,
                        transaction_time, marketplace_order_id,
                        shopify_order_id, promotion_id,
                        order_status, asin, com_code, base_code, sku_code ,
                         sku_code_by_drs , sku_code_by_supplier , product_name,
                        item_price, actual_retail_price, actual_shipping_price,
                        actual_total_shipping_price , qty, buyer,
                         buyer_email, sales_channel, fulfillment_center, refund_dt_id, city, state, country)
                         VALUES(#${o.id},
                         #${lud}, #${ot}, #${lot} , #${tt},
                         '#${o.marketplace_order_id}', '#${o.shopify_order_id}',
                         '#${o.promotion_id}', '#${o.order_status}',
                         '#${o.asin}', '#${o.com_code}', '#${o.base_code}',
                         '#${o.sku_code}' ,  '#${o.sku_code_by_drs}',
                             '#${o.sku_code_by_supplier}' , '#${pn}',
                          #${o.item_price}, #${o.actual_retail_price}, #${o.actual_shipping_price},
                             #${o.actual_total_shipping_price},  #${o.qty},
                           '#${buyer}', '#${o.buyer_email}', '#${sc}', '#${o.fulfillment_center}',
                           #${o.refund_dt_id}, '#${city}', '#${o.state}', '#${o.country}');
                    """)

            log.info(i.statements.toString())

            i
          }
          ))


      done.onComplete {
        case _ => {
          session.close()
          drsDPCmdBus ! RefreshESAllOrders(refresh.allOrderIds , refresh.startTime , refresh.endTime )
          log.info("DoneDoneDoneDoneDone 222")                                                       // (10)
        }
      }


    //

    case mo : RefreshMissingAllOrders =>

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._
      val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

      mo.missingMarketPlaceOrderIds.foreach(x=>{
        log.info(x)
      })


      log.info("CCCCCCCCCCCCCCCC")
     // log.info(refresh.lastUpdateDate)


      var c1 = 0
      log.info("insertinsertinsertinsert")

      val done: Future[Done] =
        Slick.source(
            sql"""  select s.*  from pv.all_orders as s
                 	left join sales.all_orders as a on s.marketplace_order_id = a.marketplace_order_id
                    where a.id is null """.as[OrderView])
        .runWith(Slick.sink(o=>{
          log.info("AAAAAAAAAAAAA")

          val lud = if(o.last_update_date != null) "'"+ o.last_update_date +"'" else null
          log.info(lud)

          val ot = if(o.order_time != null) "'"+ o.order_time +"'" else null
          log.info(ot)

          val lot = processLocalOrderTime(o.sales_channel , o.order_time)
          log.info(lot)

          val sc = processSalesChannel(o.sales_channel,o.marketplace_order_id)
          log.info(sc)

          val tt = if(o.transaction_time != null) "'"+ o.transaction_time +"'" else null

          val pn = if(o.product_name.contains("'")) o.product_name.replace("'"  , "''") else o.product_name

          val buyer = if(o.buyer.contains("'")) o.buyer.replace("'"  , "''") else o.buyer

          val city = if(o.city.contains("'")) o.city.replace("'"  , "''") else o.city

          c1 += 1
          log.info(c1.toString)

          val i =   (sqlu"""
                    INSERT INTO sales.all_orders
                        (source_id, last_update_date, order_time, local_order_time,
                        transaction_time, marketplace_order_id, shopify_order_id, promotion_id,
                        order_status, asin, com_code, base_code, sku_code,  sku_code_by_drs , sku_code_by_supplier  , product_name,
                        item_price, actual_retail_price, actual_shipping_price,
                        actual_total_shipping_price , qty, buyer,
                         buyer_email, sales_channel, fulfillment_center, refund_dt_id, city, state, country)
                         VALUES(#${o.id},
                         #${lud}, #${ot}, #${lot} , #${tt},
                         '#${o.marketplace_order_id}', '#${o.shopify_order_id}', '#${o.promotion_id}', '#${o.order_status}',
                         '#${o.asin}', '#${o.com_code}', '#${o.base_code}', '#${o.sku_code}', '#${o.sku_code_by_drs}',
                               '#${o.sku_code_by_supplier}', '#${pn}',
                          #${o.item_price}, #${o.actual_retail_price}, #${o.actual_shipping_price},
                             #${o.actual_total_shipping_price},  #${o.qty},
                           '#${buyer}', '#${o.buyer_email}', '#${sc}', '#${o.fulfillment_center}',
                           #${o.refund_dt_id}, '#${city}', '#${o.state}', '#${o.country}');
                    """)

          log.info(i.statements.toString())
          i
        }))





      done.onComplete {
        case _ => {
          session.close()
          drsDPCmdBus ! RefreshMissingESAllOrders(mo.missingMarketPlaceOrderIds)
          log.info("DoneDoneDoneDoneDone 333333")                                                       // (10)
        }
      }



    case t : RefreshAllOrdersTransaction =>

      println("RefreshAllOrdersTransaction----")

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._
      val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)



      val baseDate =  ZonedDateTime.now(ZoneOffset.UTC)
        .withDayOfMonth(1).withHour(23).withMinute(59)
        .withSecond(59).withNano(59)
        .minusDays(1).minusMonths(1)


      //val baseDate =   OffsetDateTime.of(2019,7,31 ,0,0,0,0,ZoneOffset.UTC)


      val u =
        sqlu"""
              UPDATE sales.all_orders
                          SET
                            order_status = aov.order_status,
                           transaction_time = aov.transaction_time ,
                           refund_dt_id= aov.refund_dt_id
                          FROM (
                            select order_status,   transaction_time  , marketplace_order_id , refund_dt_id
                                  FROM  pv.all_orders
                                  where transaction_time > '#${Timestamp.from(baseDate.toInstant)}'
                                ) AS aov
                            WHERE sales.all_orders.marketplace_order_id =aov.marketplace_order_id;
            """

      Await.result(
        session.db.run(u).map { res =>
          // res is a Vector[String]
          log.info(res.toString)

        }, timeout)


      session.close()

      drsDPCmdBus ! RefreshESAllOrdersTransaction()

      drsDPCmdBus ! RefreshDailySalesExclRefundOrders()

    case o : StartRefreshOrdersByDate =>

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._

      val db = session.db

      val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)
      val timeout2 = scala.concurrent.duration.FiniteDuration(30, TimeUnit.SECONDS)



        try {

          //val st : ZonedDateTime = ZonedDateTime.of(2019,9,3,
          //  0,0 ,0,0,ZoneId.of("Asia/Taipei"))

          val et = o.startTime.plusDays(1)


          var ids : String = ""
          val q2 = sql""" select distinct marketplace_order_id from pv.all_orders
                    where last_update_date >= '#${Timestamp.from(o.startTime.toInstant)})'
                     and   last_update_date <= '#${Timestamp.from(et.toInstant)}' """.as[String]

          println(q2.statements)

          Await.result(
            db.run(q2).map { res =>

              //log.info(res.size)

              res.foreach(x=>{
                ids += "'" + x +"',"
              })

            }, timeout)


          println(ids.length)

          if(ids.length > 0){
            ids = ids.substring(0,ids.length-1)
            log.info(ids)


            val q3 = sql"""  select id  from sales.all_orders where marketplace_order_id in  (#${ids}) """.as[Int]

            var allOrderId : List[Int] = null
            Await.result(
              db.run(q3).map { res =>

                // log.info(res.size)

                allOrderId = res.toList

              }, timeout)


            drsDPCmdBus ! RefreshAllOrders(allOrderId,o.startTime , et)
            log.info("HAS DDATA")
          }

        } catch {
          case e: TimeoutException => {
            log.info("Time out Time out Time out Time out Time out Time out Time out Time out Time out ")
            session.close()
          }
        }


        session.close()

    case nup : StartRefreshNotUpdatedOrders =>

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._
      val db = session.db
      val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)


      val done: Future[Done] =
        Slick.source(
          sql""" select * from sales.all_orders ao
               left join pv.all_orders aov on ao.source_id = aov.id
               where aov.last_update_date is null  and ao.last_update_date is not null
                order by ao.order_time desc """.as[Order])
      .runForeach(o =>{

        drsDPCmdBus ! RefreshOrder(o.marketplace_order_id)

      })





  }

  /*
  def tranformLocalOrderTime(start:Int): Unit ={

    Slick.source(
      sql""" select id , order_time ,sales_channel from sales.all_orders
             where sales_channel is not null
             order by id desc
           OFFSET  #$start limit 50
         """.as[(Int, Timestamp , String)])
      .via(
        Slick.flow(
          rs =>{
            log.info(rs._1)


            val sc = SalesChannel.fromDisplayName(rs._3)
            val ot  = OffsetDateTime.ofInstant(rs._2.toInstant , ZoneOffset.UTC)
            val zt = ot.atZoneSameInstant(ZoneId.of(sc.getTimeZoneAssigned))

            val lot: Timestamp = Timestamp.valueOf(zt.toLocalDateTime)

             sqlu""" update sales.all_orders set local_order_time = ${lot}
                  where id = ${rs._1}
                  """

          }
        )
      ).runWith(Sink.seq)

  }*/

  def processLocalOrderTime(salesChannel:String , ot : Timestamp): String = {


    log.info(salesChannel)
    if(salesChannel != null ){

      val sc = SalesChannel.fromDisplayName(salesChannel)

      if (ot != null && sc != null) {

        val zot = ZonedDateTime.ofInstant(ot.toInstant,ZoneId.of(sc.getTimeZoneAssigned))

        "'" + Timestamp.from(ZonedDateTime.of(zot.getYear,zot.getMonthValue,zot.getDayOfMonth,
          zot.getHour,zot.getMinute,zot.getSecond,zot.getNano,ZoneOffset.UTC).toInstant) + "'"

      } else {
        null

      }
    }else{
      null
    }

  }

  def processSalesChannel(salesChannel:String , marketPlaceOrderId:String): String ={

    if(salesChannel == null || salesChannel == "Non-Amazon"){

      if(marketPlaceOrderId.contains("sb") || marketPlaceOrderId.contains("ebay")){
        "eBay"
      }else{
        "Non-order"
      }
    }else{
      salesChannel
    }



  }

}
