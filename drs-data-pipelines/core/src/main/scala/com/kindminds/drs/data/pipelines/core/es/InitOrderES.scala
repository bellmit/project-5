package com.kindminds.drs.data.pipelines.core.es

import java.time.{OffsetDateTime, ZoneId, ZoneOffset}

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.alpakka.elasticsearch.scaladsl.{ElasticsearchFlow, ElasticsearchSink}
import akka.stream.alpakka.elasticsearch.{ElasticsearchWriteSettings, RetryAtFixedRate, WriteMessage, WriteResult}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, Materializer}
import akka.{Done, NotUsed}
import com.kindminds.drs.data.pipelines.api.message.RefreshESAllOrders

import com.kindminds.drs.data.pipelines.core.dto.Order
import org.apache.http.HttpHost
import org.elasticsearch.client.{Request, RestClient}
import slick.jdbc.GetResult
import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

import scala.collection.immutable

object InitOrderES {

  def props(): Props = Props(new InitOrderES())

}

class InitOrderES() extends Actor with ActorLogging {



  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  val name = self.path.name

  //drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshESAllOrders].getName ,self)



  //ec2-18-179-112-14.ap-northeast-1.compute.amazonaws.com

  implicit val client: RestClient = RestClient.
   // builder(new HttpHost("ec2-13-231-119-151.ap-northeast-1.compute.amazonaws.com",
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

  implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
  import session.profile.api._



  override def receive: Receive = {


    case a : Int =>


      println("AAAAAAAAA " + a)
      val offset = 100
      val start = ( a * offset)
      var count = 0

      val done: Future[Done] =
        Slick
          .source(sql""" select * from sales.all_orders order by id OFFSET  #$start limit 100 """.as[Order])
          .map(movie => {

            /*
            try{

            }catch {
              case  e : Exception => {
              }
            }*/

            count +=1
            println(movie.id)
            //println(movie.last_update_date)
            println(count)
           val index: WriteMessage[Order, NotUsed] =  WriteMessage.createIndexMessage(movie.id.toString, movie)
            index
          })
          .runWith(
            ElasticsearchSink.create[Order]("drs-all_orders", "_doc"
            ))

      /*
      .runWith(
        ElasticsearchFlow.create[Product](
          indexName = "test_product",
          typeName = "_doc"
        )
      )*/

      /*
      done.onComplete {
        case _ =>
          client.close()                                                       // (10)
      }
      */
      implicit val ec =  scala.concurrent.ExecutionContext.global

      done.onComplete {
        case _ =>{
          println("DoneDoneDoneDoneDone")
        }
                                                            // (10)
      }


  }








}
