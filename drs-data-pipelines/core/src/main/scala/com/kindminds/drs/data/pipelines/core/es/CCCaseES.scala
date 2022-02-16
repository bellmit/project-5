package com.kindminds.drs.data.pipelines.core.es

import java.time.{OffsetDateTime, ZoneId}
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.alpakka.elasticsearch._
import akka.stream.alpakka.elasticsearch.scaladsl.{ElasticsearchFlow, ElasticsearchSink, ElasticsearchSource}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, Materializer}
import akka.{Done, NotUsed}
import com.kindminds.drs.api.message.command.{DeleteESCustomercareCase, RefreshESCustomercareCase}
import com.kindminds.drs.data.pipelines.api.message._
import com.kindminds.drs.data.pipelines.core.dto.{CustomercareCase, Order}
import org.apache.http.HttpHost
import org.elasticsearch.client.{Request, RestClient}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

import scala.collection.immutable
import scala.concurrent.Await
import scala.util.{Failure, Success}

object CCCaseES {

  def props(): Props = Props(new CCCaseES())

}

class CCCaseES() extends Actor with ActorLogging {



  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  val name = self.path.name

  //drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshESCustomercareCase].getName ,self)


  object CCCJsonProtocol extends DefaultJsonProtocol {
    implicit object CCCJsonFormat extends RootJsonFormat[CustomercareCase] {
      def write(o: CustomercareCase) = JsObject(

        "id" ->   JsNumber(o.id),
        "issue_id" ->   JsNumber(o.issue_id),
        "marketplace_name" ->  JsString(o.marketplace_name),
        "marketplace_order_id" ->   JsString(o.marketplace_order_id),
        "type" ->   JsString(o._type),
        "status" -> JsString(o.status),
        "date_create" -> JsNumber(o.date_create),
        "seconds_from_last_activity" ->  JsNumber(o.seconds_from_last_activity),
        "last_update_time" ->    JsNumber(o.last_update_time),
        "supplier_en_name" ->  JsString(o.supplier_en_name),
        "supplier_local_name" ->  JsString(o.supplier_local_name),
        "product_base_code" ->  JsString(o.product_base_code),
        "base_product_name" ->  JsString(o.base_product_name),
        "product_sku_code" ->   JsString(o.product_sku_code),
        "sku_product_name" ->   JsString(o.sku_product_name),
        "issue_type_name" ->  JsString(o.issue_type_name),
        "issue_type_category_name" ->  JsString(o.issue_type_category_name),
        "issue_name" ->  JsString(o.issue_name),
        "customer_name" ->  JsString(o.customer_name)
      )

      def read(value: JsValue) = {
        value.asJsObject.getFields("name", "red", "green", "blue") match {
          case Seq(JsString(name), JsNumber(red), JsNumber(green), JsNumber(blue)) =>
            //new Color(name, red.toInt, green.toInt, blue.toInt)
            null
          /*
          *    def read(value: JsValue) = value match {
      case
        JsArray(Vector
          (
          JsNumber(id),
          JsString(last_update_date)
          ,JsString(order_time)
          ,JsString(transaction_time)
          ,JsString(marketplace_order_id)
          ,JsString(shopify_order_id)
          ,JsString(promotion_id)
          ,JsString(order_status)
          ,JsString(asin)
          ,JsString(com_code)
          ,JsString(base_code)
          ,JsString(sku_code)
          ,JsString(product_name)
          ,JsNumber(item_price)
          ,JsNumber(actual_retail_price)
          ,JsNumber(qty)
          ,JsString(buyer)
          ,JsString(buyer_email)
          ,JsString(sales_channel)
          ,JsString(fulfillment_center)
          ,JsString(refund_dt_id)
          ,JsString(city)
        )) => {

        println("BBBBBBBBBBBBB")
        new Order(id.toInt, last_update_date, order_time, transaction_time ,
          marketplace_order_id, shopify_order_id, promotion_id ,order_status,
          asin, com_code ,  base_code, sku_code, product_name ,item_price, actual_retail_price, qty.toInt ,
          buyer, buyer_email, sales_channel ,fulfillment_center,refund_dt_id,city            )

      }*/
          case _ => throw new DeserializationException("Color expected")
        }
      }
    }
  }

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


  import CCCJsonProtocol._



  override def receive: Receive = {

    case r : DeleteESCustomercareCase =>

      val done = ElasticsearchSource
        .create(
          indexName = "drs-customer_care_cases_view",
          typeName = "_doc",
          query = s""" {"terms": {"id": ["${r.caseId}"] }}"""
        ).map(c =>{

        //println(c.id)
        //println(c.source)
        WriteMessage.createDeleteMessage(id =  c.id ).asInstanceOf[WriteMessage[CustomercareCase, NotUsed]]
      }).via(
          ElasticsearchFlow.create[CustomercareCase](
            "drs-customer_care_cases_view",
            "_doc"
          )
        )
        .runWith(Sink.seq)


      done.onComplete {
        case _ =>{
          println("DoneDoneDoneDoneDone")
        }

      }



    case r : RefreshESCustomercareCase =>



      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._
      val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

      println("DDDDDDDDDDDDDDDDDDDdddddd")
      println(r.caseId)

      //var d2 : String = OffsetDateTime.now().minusDays(1).plusHours(9).minusMinutes(20).toString
      //println(d2)

      var ids : Vector[CustomercareCase] = null
      /*
      val q2 = sql""" select * from customer_care_cases_view
                    where last_update_time > '#$d2' """.as[CustomercareCase]
                    */

      val q2 = sql""" select * from customer_care_cases_view
                    where id = #${r.caseId}  order by last_update_time desc """.as[CustomercareCase]

      Await.result(
        session.db.run(q2).map { res =>

          ids = res
          ids.foreach(x=>{
            println(x.id)
          })

        }, timeout)


      val requests  =  ids.map(c =>{
        WriteMessage.createDeleteMessage(id =  (c.id.toString+"#"+c.issue_id+"#"+ c.product_base_code +"#"+c.product_sku_code) )
          .asInstanceOf[WriteMessage[CustomercareCase, NotUsed]]
      })


      val writeResults: Future[immutable.Seq[WriteResult[CustomercareCase, NotUsed]]] = Source(requests.toList)
        .via(
          ElasticsearchFlow.create[CustomercareCase](
            "drs-customer_care_cases_view",
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
                //sql""" select * from customer_care_cases_view where last_update_time > '#${d2}'
                sql""" select * from customer_care_cases_view where id = #${r.caseId}
                order by last_update_time desc """.as[CustomercareCase])
              .map(c => {

                /*
                try{

                }catch {
                  case  e : Exception => {


                  }
                }*/

                //println(movie.last_update_date)

                val index: WriteMessage[CustomercareCase, NotUsed] =  WriteMessage.createIndexMessage(
                  c.id.toString+"#"+c.issue_id+"#"+ c.product_base_code +"#"+c.product_sku_code, c)
                index
              })
              .runWith(
                 ElasticsearchSink.create[CustomercareCase]("drs-customer_care_cases_view", "_doc"
                ))


          done.onComplete {
            case _ =>{
              session.close()
              println("DoneDoneDoneDoneDone")
            }

          }


        }



      }


  }








}
