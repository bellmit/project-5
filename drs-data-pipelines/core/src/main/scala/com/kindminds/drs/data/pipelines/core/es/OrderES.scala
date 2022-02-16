package com.kindminds.drs.data.pipelines.core.es

import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.time.{OffsetDateTime, ZoneId, ZoneOffset}
import java.util.concurrent.TimeUnit

import akka.{Done, NotUsed}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.elasticsearch._
import akka.stream.alpakka.elasticsearch.scaladsl.{ElasticsearchFlow, ElasticsearchSink, ElasticsearchSource}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import com.kindminds.drs.data.pipelines.api.message._

import com.kindminds.drs.data.pipelines.core.dto.{Order, OrderView}
import org.apache.http.HttpHost
import org.elasticsearch.client.{Request, RestClient}
import slick.jdbc.GetResult
import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

import scala.collection.immutable
import scala.concurrent.{Await, Future}



object OrderES {

  def props(drsDPCmdBus: ActorRef): Props = Props(new OrderES(drsDPCmdBus))

}

class OrderES(drsDPCmdBus: ActorRef) extends Actor with ActorLogging {



  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  val name = self.path.name

  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshESAllOrders].getName ,self)
  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshMissingESAllOrders].getName ,self)
  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshESOrder].getName ,self)
  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshESAllOrdersTransaction].getName ,self)


  implicit val client: RestClient = RestClient.
    //builder(new HttpHost("ec2-13-115-85-230.ap-northeast-1.compute.amazonaws.com",
    builder(new HttpHost("ec2-18-179-112-14.ap-northeast-1.compute.amazonaws.com",
      // builder(new HttpHost("10.0.0.253",
      9200, "http")).build()

  import scala.concurrent.Future
  import scala.concurrent.duration.DurationInt

  val response = client.performRequest(new Request("GET", "/"))


  val sinkSettings =
    ElasticsearchWriteSettings()
      .withBufferSize(100)
      .withVersionType("internal")
      .withRetryLogic(RetryAtFixedRate(maxRetries = 5, retryInterval = 1.second))


  import com.kindminds.drs.data.pipelines.core.OrderJsonProtocol._
  import spray.json._

  val indexName = "drs-all_orders"

  
  override def receive: Receive = {

    case refresh : RefreshESAllOrders =>

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._

      val requests  =  refresh.allOrderIds.map(x=>{
        WriteMessage.createDeleteMessage(id = x.toString ).asInstanceOf[WriteMessage[Order, NotUsed]]
      })


      val writeResults: Future[immutable.Seq[WriteResult[Order, NotUsed]]] = Source(requests.toList)
        .via(
          ElasticsearchFlow.create[Order](
            indexName,
            "_doc"
          )
        )
        .runWith(Sink.seq)

      writeResults.onComplete {
        case _ =>{
          println("DoneDoneDoneDoneDone")

          val done: Future[Done] =
            Slick
              .source(
                sql""" select * from sales.all_orders
                       where last_update_date > '#${ Timestamp.from(refresh.startTime.toInstant)}'
                       and last_update_date < '#${ Timestamp.from(refresh.endTime.toInstant)}'
                order by last_update_date """.as[Order])
              .map(o => {

                println(o.marketplace_order_id)

                println("AAAAAAAAAAA")
                println(o.id)
                //println(movie.last_update_date)

                val index: WriteMessage[Order, NotUsed] =  WriteMessage.createIndexMessage(o.id.toString, o)
                index
              })
              .runWith(
                ElasticsearchSink.create[Order](indexName, "_doc"
                ))


          done.onComplete {
            case _ =>{
              session.close()
              println("DoneDoneDoneDoneDone")                                                       // (10)
            }

          }

        }
                                                           // (10)
      }

    case mo : RefreshMissingESAllOrders =>

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._


      var ids : String = ""
      mo.missingMarketPlaceOrderIds.foreach(x=>{
        ids += "'" + x + "',"
      })

      if(ids.length > 0){
        println(ids)
        ids = ids.substring(0,ids.length -1)
        println(ids)
      }

      val done: Future[Done] =
        Slick
          .source(
            sql""" select * from sales.all_orders where marketplace_order_id in ( #${ids} )
                order by last_update_date  desc """.as[Order])
          .map(o => {


            println(o.marketplace_order_id)

            println("AAAAAAAAAAA")
            println(o.id)
            //println(movie.last_update_date)


            val index: WriteMessage[Order, NotUsed] =  WriteMessage.createIndexMessage(o.id.toString, o)
            index
          })
          .runWith(
            ElasticsearchSink.create[Order](indexName, "_doc"
            ))


      done.onComplete {
        case _ =>{
          session.close()
          println("DoneDoneDoneDoneDone")                                                       // (10)
        }

      }




    case r : RefreshESOrder =>


      println("AAAAAAAAAAAAAAAAAA")
      println(r.marketPlaceOrderId)


      r.sourceIds  match {
        case sids =>

          val requests  =  sids.get.map(x=>{
            WriteMessage.createDeleteMessage(id = x.toString ).asInstanceOf[WriteMessage[Order, NotUsed]]
          })

          val writeResults: Future[immutable.Seq[WriteResult[Order, NotUsed]]] = Source(requests.toList)
            .via(
              ElasticsearchFlow.create[Order](
                indexName,
                "_doc"
              )
            )
            .runWith(Sink.seq)

          writeResults.onComplete {
            case _ =>{

              println("DoneDoneDoneDoneDone")

              createIndexByMarketplaceOrderId(r.marketPlaceOrderId)

            }
          }


        case None =>
          createIndexByMarketplaceOrderId(r.marketPlaceOrderId)
      }


    case t : RefreshESAllOrdersTransaction =>

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._
      val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)



      val baseDate =  OffsetDateTime.now().withDayOfMonth(1).withHour(23).withMinute(59)
        .withSecond(59).minusDays(1).minusMonths(1)


     // val baseDate =   OffsetDateTime.of(2019,7,31 ,0,0,0,0,ZoneOffset.UTC)


      val q = sql"""  select  * FROM  sales.all_orders  where transaction_time > '#${baseDate}' """.as[Order]
      var orders : Vector[Order] = null
      Await.result(
        session.db.run(q).map { res =>
          // res is a Vector[String]
          orders = res
        }, timeout)

      val requests  =  orders.map(x=>{
        WriteMessage.createDeleteMessage(id = x.id.toString ).asInstanceOf[WriteMessage[Order, NotUsed]]
      })


      val writeResults: Future[immutable.Seq[WriteResult[Order, NotUsed]]] = Source(requests.toList)
        .via(
          ElasticsearchFlow.create[Order](
            indexName,
            "_doc"
          )
        )
        .runWith(Sink.seq)


      writeResults.onComplete {
        case _ =>{
          println("DoneDoneDoneDoneDone")


          val done: Future[Done] =
            Slick
              .source(
                sql""" select * FROM  sales.all_orders  where transaction_time > '#${baseDate}'  """.as[Order])
              .map( o => {

                println(o.marketplace_order_id)

                println("AAAAAAAAAAA")
                println(o.id)
                //println(movie.last_update_date)

                val index: WriteMessage[Order, NotUsed] =  WriteMessage.createIndexMessage(o.id.toString, o)
                index
              })
              .runWith(
                ElasticsearchSink.create[Order](indexName, "_doc"
                ))


          done.onComplete {
            case _ =>{
              session.close()
              println("DoneDoneDoneDoneDone")                                                       // (10)
            }

          }



        }
      }

  }


  def createIndexByMarketplaceOrderId(marketPlaceOrderId:String): Unit ={

    implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
    import session.profile.api._


    val done: Future[Done] =
      Slick
        .source(
          sql""" select * from sales.all_orders where marketplace_order_id = '#${marketPlaceOrderId}'
                order by last_update_date """.as[Order])
        .map(o => {

          println(o.marketplace_order_id)

          println("AAAAAAAAAAA")
          println(o.id)
          //println(movie.last_update_date)

          val index: WriteMessage[Order, NotUsed] =  WriteMessage.createIndexMessage(o.id.toString, o)
          index
        })
        .runWith(
          ElasticsearchSink.create[Order](indexName, "_doc"
          ))


    done.onComplete {
      case _ =>{
        session.close()
        println("DoneDoneDoneDoneDone")                                                       // (10)
      }

    }

  }






}
