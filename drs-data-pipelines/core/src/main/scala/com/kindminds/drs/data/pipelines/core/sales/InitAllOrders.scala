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
import com.kindminds.drs.api.message.command.amazon.order.RequestAmazonOrder
import com.kindminds.drs.data.pipelines.api.message._

import com.kindminds.drs.data.pipelines.core.dto.OrderView
import com.kindminds.drs.data.pipelines.core.es.OrderES
import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel
import slick.jdbc.GetResult

import scala.collection.immutable
import scala.concurrent.{Await, Future}

object InitAllOrdes {

  def props(drsDPCmdBus: ActorRef): Props = Props(new InitAllOrdes(drsDPCmdBus))

}

class InitAllOrdes(drsDPCmdBus: ActorRef) extends Actor with ActorLogging {


  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  val name = self.path.name

  drsDPCmdBus ! RegisterCommandHandler(name,classOf[ProcessEBayOrderSalesChannel].getName ,self)
  //drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshAllOrders].getName ,self)


  override def receive: Receive = {


    case a : Int =>

      val offset = 100
      val start = ( a * offset)

      println("BBBBBBBBBBBBBBBBBBBBB")
       tranformLocalOrderTime(start)

    case d : ProcessLocalOrderTime =>
      tranformLocalOrderTime()

    case e : ProcessEBayOrderSalesChannel =>
      println("CCCCCCCCCCCc")
      transformSalesChannel()



  }


  def tranformLocalOrderTime(): Unit ={

    println("BBBBBBBBBBb")

    implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
    import session.profile.api._

    //  and local_order_time is null
    val done: Future[immutable.Seq[Int]] =  Slick.source(

      sql""" select id , order_time ,sales_channel from sales.all_orders
             where sales_channel is not null and  sales_channel <> ''
             and local_order_time is null
             order by id desc
         """.as[(Int, Timestamp , String)])
      .via(
        Slick.flow(
          rs =>{
            println(rs._1)


           // val sc = SalesChannel.fromDisplayName(rs._3)
           // val ot  = OffsetDateTime.ofInstant(rs._2.toInstant , ZoneOffset.UTC)

            val lot = processLocalOrderTime(rs._3 , rs._2)
            println("ccc")
            println(lot)
            println("ccc")

            val q = sqlu""" update sales.all_orders set local_order_time = #${lot}
                  where id = #${rs._1}
                  """
            println(q.statements)
            q

          }
        )
      ).runWith(Sink.seq)


    done.onComplete {
      case _ => {
        session.close()

        println("DoneDoneDoneDoneDone 222")                                                       // (10)
      }
    }



  }


  def tranformLocalOrderTime(start:Int): Unit ={

    implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
    import session.profile.api._

   val done: Future[immutable.Seq[Int]] =  Slick.source(

     sql""" select id , order_time ,sales_channel from sales.all_orders
             where sales_channel is not null
             order by id desc
           OFFSET  #$start limit 100
         """.as[(Int, Timestamp , String)])
      .via(
        Slick.flow(
          rs =>{
            println(rs._1)
            val lot = processLocalOrderTime(rs._3 , rs._2)
            println("ccc")
            println(lot)
            println("ccc")

           val q = sqlu""" update sales.all_orders set local_order_time = #${lot}
                  where id = #${rs._1}
                  """
            println(q.statements)
            q

          }
        )
      ).runWith(Sink.seq)


    done.onComplete {
      case _ => {
        session.close()

        println("DoneDoneDoneDoneDone 222")                                                       // (10)
      }
    }



  }

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



  def transformSalesChannel(): Unit ={


    implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
    import session.profile.api._


    var count = 0

   val done = Slick.source(
      sql""" select id , sales_channel , marketplace_order_id  from sales.all_orders
             where sales_channel is null or sales_channel = 'Non-Amazon'
             order by id desc
         """.as[(Int , String , String)])
      .via(
        Slick.flow(
          rs =>{
            println(rs._1)
            println(rs._2)
            println(rs._3)
            count +=1
            println(count)

            val sc = processSalesChannel(rs._2 , rs._3)
            sqlu""" update sales.all_orders set sales_channel = '#${sc}'
                  where id = ${rs._1}
                  """
          }
        )
      ).runWith(Sink.seq)

    done.onComplete {
      case _ => {
        session.close()

        println("DoneDoneDoneDoneDone 222")                                                       // (10)
      }
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
