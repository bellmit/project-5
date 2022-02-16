package com.kindminds.drs.data.pipelines.core

import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.{LocalDateTime, OffsetDateTime, ZoneId, ZoneOffset}
import java.time.format.DateTimeFormatter
import java.util.{Calendar, Date}

import akka.Done
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.{ActorMaterializer, IOResult, Materializer}
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.alpakka.elasticsearch.{ElasticsearchWriteSettings, ReadResult, RetryAtFixedRate, WriteMessage}
import akka.stream.alpakka.elasticsearch.scaladsl.{ElasticsearchFlow, ElasticsearchSink, ElasticsearchSource}

import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString
import com.kindminds.drs.Marketplace

import com.kindminds.drs.api.message.command.amazon.report.RequestReport
import com.kindminds.drs.data.pipelines.api.message.{RequestAdvReport, _}
import com.kindminds.drs.data.pipelines.api.util.{Amazon, DateHelper}
//import com.kindminds.drs.data.pipelines.core.accounting.journalentry.dal.{DrsJournalEntryDao, ErpJournalEntryDao}
import com.kindminds.drs.data.pipelines.core.actor._
import com.kindminds.drs.data.pipelines.core.util.HdfsHelper
import com.kindminds.drs.service.util.DrsBizCoreAnnotationConfig4DP

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import slick.jdbc.GetResult
import spray.json.JsonFormat

import scala.concurrent.{ExecutionContext, Future}
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

/*
import com.kindminds.drs.service.util.DrsBizCoreAnnotationConfig
import com.kindminds.drs.service.util.{DrsBizCoreAnnotationConfig4DP, DrsBizCoreAnnotationConfigNoDB}
import org.elasticsearch.client.Reques
*/



object Main  {


  def main(args: Array[String]): Unit = {

    startDp()
    //test()
    //testESQuery()
    //startMongo()

  }

  def startDp(): Unit ={
    //println(conf.getConfig("akka.cluster.seed-nodes"))


    implicit val system = ActorSystem("drs")

    val scheduler = QuartzSchedulerExtension(system)

    val eventBus = new DrsMarketingEventBus

    println(scheduler.schedules.size)
    scheduler.schedules.foreach(x=> println(x._1))


    val springCtx: AnnotationConfigApplicationContext =
      new AnnotationConfigApplicationContext(classOf[DrsBizCoreAnnotationConfig4DP])

    system.actorOf(Mail.props(springCtx) , "mail")
    val drs =  system.actorOf(DrsProcessor.props(springCtx) , "drsProcessor")
    val idrs =  system.actorOf(DrsImportProcessor.props(eventBus , springCtx) , "drsImportProcessor")

    system.actorOf(ReportTransformer.props() , "reportTransformer")
    val reportBot = system.actorOf(ReportBot.props(springCtx) , "reportBot")

    val pSuccess = system.actorOf(ProcessAmzRptSuccessSubscriber.props(springCtx) ,
      "processAmzRptSuccessSubscriber" )

    val pFail = system.actorOf(ProcessAmzRptFailSubscriber.props(springCtx) ,
      "processAmzRptFailSubscriber" )

    eventBus.subscribe( pSuccess, AutoProcess)

    eventBus.subscribe( pFail, AutoProcessFailed)



    //===================

   // Thread.sleep(3000)

    //idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20210911") ,
      //"hdfs://kindminds01:9000/user/Amazon/US/Traffic/2021/September/Traffic_US_20210911.csv", "us")

    //check report

    /*
    scheduler.schedule("cronCheckCp" , reportBot , CheckCampaignPerformance())
    scheduler.schedule("cronCheckSt" , reportBot , CheckSearchTerm())
    scheduler.schedule("cronCheckHc" , reportBot , CheckHSACampaign())
    scheduler.schedule("cronCheckHk" , reportBot , CheckHSAKeyword())
    scheduler.schedule("cronCheckBpt" , reportBot , CheckBiWeeklyPageTraffic())

    scheduler.schedule("cronCheckPsv" , reportBot , CheckPaymentsAllStatementsV2())
    scheduler.schedule("cronCheckCr" , reportBot , CheckCustomerReturn())

    scheduler.schedule("cronCheckPd" , reportBot , CheckPaymentsDateRange())
    scheduler.schedule("cronCheckMfi" , reportBot , CheckManageFBAInventory())
    scheduler.schedule("cronCheckInv" , reportBot , CheckInventory())
    */


    //reportBot ! CheckCampaignPerformance()

   // doHsaK(idrs)

    //idrs ! ImportBiWeeklyTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200704") ,
      //"hdfs://kindminds01:9000/user/Amazon/CA/Bi-Weekly Settlement Traffic/2020/July/" +
       // "Traffic_DE_20200621-20200704.csv","ca")


    //  idrs ! ImportSearchTermRpt(DateHelper.convertStrToJavaLocalDate("20200824") ,
  //  "/user/Amazon/FR/Sponsored Products/Search Term/Daily/2020/August/Search_FR_20200824.xlsx","fr")

   // idrs ! ImportCampaignPerformanceRpt(DateHelper.convertStrToJavaLocalDate("20200821") ,
     // "/user/Amazon/US/Sponsored Products/Advertised Product/Daily/2020/August/Campaign_US_20200821.xlsx","us")

/*
    idrs ! ImportCampaignPerformanceRpt(DateHelper.convertStrToJavaLocalDate("20200629") ,
      "/user/Amazon/UK/Sponsored Products/Advertised Product/Daily/2020/June/Campaign_UK_20200629.xlsx","uk")


    idrs ! ImportCampaignPerformanceRpt(DateHelper.convertStrToJavaLocalDate("20200629") ,
      "/user/Amazon/DE/Sponsored Products/Advertised Product/Daily/2020/June/Campaign_DE_20200629.xlsx","de")

    idrs ! ImportCampaignPerformanceRpt(DateHelper.convertStrToJavaLocalDate("20200629") ,
      "/user/Amazon/FR/Sponsored Products/Advertised Product/Daily/2020/June/Campaign_FR_20200629.xlsx","fr")

    idrs ! ImportCampaignPerformanceRpt(DateHelper.convertStrToJavaLocalDate("20200629") ,
      "/user/Amazon/IT/Sponsored Products/Advertised Product/Daily/2020/June/Campaign_IT_20200629.xlsx","it")

    idrs ! ImportCampaignPerformanceRpt(DateHelper.convertStrToJavaLocalDate("20200629") ,
      "/user/Amazon/ES/Sponsored Products/Advertised Product/Daily/2020/June/Campaign_ES_20200629.xlsx","es")
*/
    
    //==

    /*
    val mp = "IT"
    val fileName = ""

    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200616.csv",mp.toLowerCase())

    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200617.csv",mp.toLowerCase())

    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200618.csv",mp.toLowerCase())

    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200619.csv",mp.toLowerCase())

    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200620.csv",mp.toLowerCase())

    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200621.csv",mp.toLowerCase())

    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200622.csv",mp.toLowerCase())

    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200623.csv",mp.toLowerCase())

    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200624.csv",mp.toLowerCase())

    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200625.csv",mp.toLowerCase())


    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200626.csv",mp.toLowerCase())

    idrs ! ImportTrafficRpt(DateHelper.convertStrToJavaLocalDate("20200630") ,
      "hdfs://kindminds01:9000/user/Amazon/"+ mp +"/Traffic/2020/June/Traffic_"+ mp +"_20200627.csv",mp.toLowerCase())
    */




  //todo monthly storagefee onley need clac onece


   // drs ! CalcMonthlyStorageFeeRpt("hdfs://kindminds01:9000/user/Amazon/US/Monthly Storage Fee/MonthlyStorageFee_US_01_2020.txt")


   // println("AAAAAAAAAAAAA")

    //idrs ! ImportCampaignPerformanceRpt(DateHelper.convertStrToJavaLocalDate("20200205") ,
    //"hdfs://kindminds01:9000/user/Amazon/temp/CP_CA_01.xlsx","ca")

   //  idrs ! ImportCampaignPerformanceRpt(DateHelper.convertStrToJavaLocalDate("20200220") ,
     // "hdfs://kindminds01:9000/user/Amazon/US/Sponsored Products/Advertised Product/Daily/2020/February/Campaign_US_20200220.xlsx","us")


   // idrs ! ImportHSACampaignRpt(DateHelper.convertStrToJavaLocalDate("20200205") ,
     //"hdfs://kindminds01:9000/user/Amazon/temp/HSA_C_CA_01.xlsx","ca")


    //idrs ! ImportHSAKeywordRpt(DateHelper.convertStrToJavaLocalDate("20200205") ,
    //"hdfs://kindminds01:9000/user/Amazon/temp/HSA_K_CA_01.xlsx","ca")

    //idrs ! ImportSearchTermRpt(DateHelper.convertStrToJavaLocalDate("20200615") ,
    //"/user/Amazon/UK/Sponsored Products/Search Term/Daily/2020/June/Search_UK_20200615.xlsx","uk")



  }


  def doHsaC(drsImportProcessor:ActorRef): Unit ={


    val mps = Seq( "us" )
    val months = Seq( "November")
    //val mps = Seq("us")

    months.foreach( m =>{
      //mps.foreach( x =>{
      // HdfsHelper.campaignPath.foreach(x => {

      // HdfsHelper.listFileStatus(HdfsHelper.campaignPath(x)).foreach(y=>{
      //val path = HdfsHelper.hsaCampaignPath(x) + "/Daily/2018/October"
      val path = HdfsHelper.hsaCampaignPath("ca") + "/Daily/2019/"+m
      println(path)
      HdfsHelper.listFileStatus(path).foreach(y=>{

        drsImportProcessor ! ImportHSACampaignRpt(
          DateHelper.convertStrToJavaLocalDate("20191113"), path + "/HSA_Campaign_CA_20191113.xlsx" ,"ca")


      })
      //})
    })



  }

  def doHsaK(drsImportProcessor:ActorRef): Unit ={


    //val mps = Seq( "us", "uk")
    val months = Seq( "November")
    //val mps = Seq("us")

    months.foreach(m =>{
      //mps.foreach( x =>{
      // HdfsHelper.campaignPath.foreach(x => {

      // HdfsHelper.listFileStatus(HdfsHelper.campaignPath(x)).foreach(y=>{
      //val path = HdfsHelper.hsaCampaignPath(x) + "/Daily/2018/October"
      val path = HdfsHelper.hsaKeywordPath("ca") + "/Daily/2019/" + m
      //HdfsHelper.listFileStatus(path).foreach(y=>{

      drsImportProcessor ! ImportHSAKeywordRpt( DateHelper.convertStrToJavaLocalDate("20191113"),
        path+"/HSA_Keyword_CA_20191113.xlsx" ,"ca")


      //})
      //})

    })



  }








  case class MSActivity(country: String,code: String,name: String,doneBy: String,
                        startDate:java.util.Date,endDate: java.util.Date,platform: String, activity: String,
                        initialAmount: String,budgetFinalAmount: String, discount: String,
                        unitOfMeasure: String,details: String,link1: String, link2: String,
                        link3: String,link4: String,link5: String, link6: String,
                        link7: String,link8: String,link9: String,link10: String,
                        considerationsProblemToSolve:String,originalText: String,newText: String,suggestionsForSupplier: String)



  def cleanseCsvData(csvData: Map[String, ByteString]): Map[String, String] =
    csvData
      .filterNot { case (key, _) => key.isEmpty }
      .mapValues(_.utf8String)










}
