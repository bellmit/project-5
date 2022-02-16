package com.kindminds.drs.data.pipelines.core.sales

import java.sql.Timestamp
import java.time.{OffsetDateTime, ZoneId, ZoneOffset, ZonedDateTime}
import java.util.concurrent.TimeUnit

import akka.Done
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.Sink
import akka.stream.{ActorMaterializer, Materializer}

import com.kindminds.drs.data.pipelines.api.message._

import com.kindminds.drs.data.pipelines.core.dto.{Order, OrderView}
import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel
import slick.jdbc.GetResult

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, Future}


object SingleOrder {

  def props(drsDPCmdBus: ActorRef): Props = Props(new SingleOrder(drsDPCmdBus))

}



class SingleOrder(drsDPCmdBus: ActorRef) extends Actor with ActorLogging {


  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  val name = self.path.name


  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshOrder].getName ,self)


  override def receive: Receive = {


    case refresh : RefreshOrder =>

      println(refresh.marketPlaceOrderId)




      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._
      val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)


      println("CCCCCCCCCCCCCCCC")
      println(refresh.marketPlaceOrderId)

      var c1 = 0
      println("insertinsertinsertinsert")


      var orders : Vector[Order] = null
      val allOrderQuery = sql""" select *  from sales.all_orders
            where marketplace_order_id = '#${refresh.marketPlaceOrderId}' """.as[Order]

      Await.result(
        session.db.run(allOrderQuery).map { res =>
          // res is a Vector[String]
          orders = res
        }, timeout)

      var sourceIds : Option[List[Int]] = None
      if(orders!= null ){

        var sids = new ListBuffer[Int]()
        var ids : String = ""
        orders.foreach(x=>{
          ids += x.id + ","
          sids += x.id
        })

        if(ids.length > 0){
          ids = ids.substring(0,ids.length -1)
          sourceIds = Some(sids.toList)

          val q3 = sql"""  delete from sales.all_orders where id in ( #$ids )""".as[String]

          Await.result(
            session.db.run(q3).map { res =>
              //log.info(res.size)
            }, timeout)

        }

      }



      val done: Future[Done] =
      Slick
        .source(
          sql""" select * from pv.all_orders where marketplace_order_id = '#${refresh.marketPlaceOrderId}'
                  order by last_update_date """.as[OrderView])
        .via(

          Slick.flow(o =>{

            println("AAAAAAAAAAAAA")
            val lud = if(o.last_update_date != null) "'"+ o.last_update_date +"'" else null
            println(lud)
            val ot = if(o.order_time != null) "'"+ o.order_time +"'" else null
            println(ot)

            //val lot = processLocalOrderTime(o.sales_channel , o.order_time)
            val lot = null
            println(lot)

            val tt = if(o.transaction_time != null) "'"+ o.transaction_time +"'" else null

            val pn = if(o.product_name.contains("'")) o.product_name.replace("'"  , "''") else o.product_name

            val buyer = if(o.buyer.contains("'")) o.buyer.replace("'"  , "''") else o.buyer

            c1 += 1
            println(c1)

            val i =   (sqlu"""
                    INSERT INTO sales.all_orders
                        (source_id, last_update_date, order_time, local_order_time,
                        transaction_time, marketplace_order_id, shopify_order_id, promotion_id,
                        order_status, asin, com_code, base_code, sku_code,  sku_code_by_drs , sku_code_by_supplier ,
                         product_name,
                        item_price, actual_retail_price, actual_shipping_price,
                        actual_total_shipping_price , qty, buyer,
                         buyer_email, sales_channel, fulfillment_center, refund_dt_id, city, state, country)
                         VALUES(#${o.id},
                         #${lud}, #${ot}, #${lot} , #${tt},
                         '#${o.marketplace_order_id}', '#${o.shopify_order_id}', '#${o.promotion_id}', '#${o.order_status}',
                         '#${o.asin}', '#${o.com_code}', '#${o.base_code}', '#${o.sku_code}',
                             '#${o.sku_code_by_drs}', '#${o.sku_code_by_supplier}','#${pn}',
                          #${o.item_price}, #${o.actual_retail_price}, #${o.actual_shipping_price},
                             #${o.actual_total_shipping_price},  #${o.qty},
                           '#${buyer}', '#${o.buyer_email}', '#${o.sales_channel}', '#${o.fulfillment_center}',
                           #${o.refund_dt_id}, '#${o.city}', '#${o.state}', '#${o.country}');
                    """)

            println(i.statements)
            i


          })
        ).runWith(Sink.ignore)

      done.onComplete {
        case _ => {
          session.close()
          drsDPCmdBus ! RefreshESOrder(refresh.marketPlaceOrderId ,sourceIds)
          println("DoneDoneDoneDoneDone 222")                                                       // (10)
        }
      }







    case a : Int =>

      val offset = 50
      val start = ( a * offset)

      println("BBBBBBBBBBBBBBBBBBBBB")
     // tranformLocalOrderTime(start)


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
            println(rs._1)


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


    println(salesChannel)
    if(salesChannel != null ){

      var sc = SalesChannel.fromDisplayName(salesChannel)

      if(salesChannel == "Webstore"){
        sc = SalesChannel.AMAZON_COM
      }


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

  def processSalesChannel(): Unit ={

  }

}
