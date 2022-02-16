package com.kindminds.drs.data.pipelines.core.es

import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.{OffsetDateTime, ZoneId, ZoneOffset}
import java.util.concurrent.TimeUnit

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.alpakka.elasticsearch.scaladsl.{ElasticsearchSink, ElasticsearchSource}
import akka.stream.alpakka.elasticsearch.{ElasticsearchWriteSettings, RetryAtFixedRate, WriteMessage}

import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult, Materializer}
import akka.util.ByteString
import com.kindminds.drs.data.pipelines.core.actor._


import org.elasticsearch.client.Request
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import slick.jdbc.GetResult
import spray.json

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
//import com.kindminds.drs.service.util.DrsSpringAnnotationConfig
//import org.springframework.context.annotation.AnnotationConfigApplicationContext
//import com.kindminds.drs.service.util.DrsSpringAnnotationConfig
//import org.springframework.context.annotation.AnnotationConfigApplicationContext
//import slick.jdbc.JdbcBackend.{Database}
//import slick.driver
//import com.kindminds.drs.backend.handlers.AmazonDateRangeRptHandler

import com.kindminds.drs.data.pipelines.api.message.Events._
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient



object OrderJob {


  def main(args: Array[String]): Unit = {

    //startDp()
    //InitOrderEs()
    InitCCCEs()
    //InitCCCIEs()
    //testSlick()

    //testESQuery()



  }




  def test():Unit = {

    implicit val system = ActorSystem("drsDP")
    implicit val materializer: Materializer = ActorMaterializer()

    //#init-mat
    //#init-client


    //val oe = system.actorOf(OrderES.props() , "oe")

    //User(r.nextInt, r.nextString))

    // This import enables the use of the Slick sql"...",
    // sqlu"...", and sqlt"..." String interpolators.
    // See also: "http://slick.lightbend.com/doc/3.2.1/sql.html#string-interpolation"



   //  implicit val format: JsonFormat[Order] = jsonFormat22(Order)
    // Stream the results of a query

 //  implicit val format: JsonFormat[Order] = jsonFormat22(Order)


    Thread.sleep(10000)
    val totalCount : BigDecimal = 282284
    val pages : BigDecimal =  (totalCount/ 100).setScale(0, BigDecimal.RoundingMode.UP)
    println(pages)


    //(pages.toInt -1)
   /*
    for( a <- 0 to 1){
      println( "Value of a: " + a )
      oe ! a

    }*/

    //oe ! 0


    /*
    val source = Source(0 to (pages -1).toInt)
   //val source = Source(0 to 1)
    source.runForeach(x=>{
      Thread.sleep(1000)
      oe ! x
    })
    */

    //oe ! "AAA"



    println("CCCCCCCCCCCCCCCc")

  }

  def InitOrderEs():Unit = {

    implicit val system = ActorSystem("drsDP")
    implicit val materializer: Materializer = ActorMaterializer()

    val oe = system.actorOf(InitOrderES.props() , "initoe")

    Thread.sleep(10000)
    val totalCount : BigDecimal = 286765
    val pages : BigDecimal =  (totalCount/ 100).setScale(0, BigDecimal.RoundingMode.UP)
    println(pages)


    val source = Source(0 to (pages -1).toInt)
   //val source = Source(0 to 1)
    source.runForeach(x=>{
      Thread.sleep(1000)
      oe ! x
    })


    //oe ! "AAA"



    println("CCCCCCCCCCCCCCCc")

  }

  def InitCCCEs():Unit = {

    implicit val system = ActorSystem("drsDP")
    implicit val materializer: Materializer = ActorMaterializer()

    val oe = system.actorOf(InitCCCES.props() , "initccc")

    Thread.sleep(10000)


    val totalCount : BigDecimal = 3127
    val pages : BigDecimal =  (totalCount/ 100).setScale(0, BigDecimal.RoundingMode.UP)
    println(pages)


   val source = Source(0 to (pages -1).toInt)
   // val source = Source(3 to 3)
    source.runForeach(x=>{
      //Thread.sleep(2000)
      oe ! x
    })


    //oe ! 1

    //oe ! "AAA"



    println("CCCCCCCCCCCCCCCc")

  }

  def InitCCCIEs():Unit = {

    implicit val system = ActorSystem("drsDP")
    implicit val materializer: Materializer = ActorMaterializer()

    val oe = system.actorOf(InitCCCIES.props() , "initccci")

    //Thread.sleep(10000)


    val totalCount : BigDecimal = 1224
    val pages : BigDecimal =  (totalCount/ 100).setScale(0, BigDecimal.RoundingMode.UP)
    println(pages)


   val source = Source(0 to (pages -1).toInt)
     //val source = Source(3 to 4)
    source.runForeach(x=>{
      Thread.sleep(1000)
      oe ! x
    })


    //oe ! 1

    //oe ! "AAA"



    println("CCCCCCCCCCCCCCCc")

  }


}
