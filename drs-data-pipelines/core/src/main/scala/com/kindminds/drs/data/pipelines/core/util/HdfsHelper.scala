package com.kindminds.drs.data.pipelines.core.util


import java.text.SimpleDateFormat
import java.util.Calendar

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataInputStream, FileStatus, FileSystem, Path}



object HdfsHelper{



  //val confPath = "/home/hadoop/hadoop/conf/core-site.xml"

  //Directory: marketing -> Amazon -> US/CA/UK/DE/FR/IT/ES -> Campaign/Search Term/Traffic

  val campaignPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Campaign",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Campaign",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Campaign" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Campaign",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Campaign",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Campaign" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Campaign" )

  val oldCampaignPath = Map(
    "us" -> "hdfs://kindminds01:9000/user/Amazon/US/Sponsored Products/Advertised Product/Old Reports",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Sponsored Products/Advertised Product/Old Reports",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Sponsored Products/Advertised Product/Old Reports" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Sponsored Products/Advertised Product/Old Reports",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Sponsored Products/Advertised Product/Old Reports",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Sponsored Products/Advertised Product/Old Reports" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Sponsored Products/Advertised Product/Old Reports" )

  val pageTrafficPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Traffic",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Traffic",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Traffic" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Traffic",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Traffic",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Traffic" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Traffic" )

  val biWeeklySettlementTrafficPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Bi-Weekly Settlement Traffic",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Bi-Weekly Settlement Traffic",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Bi-Weekly Settlement Traffic" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Bi-Weekly Settlement Traffic",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Bi-Weekly Settlement Traffic",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Bi-Weekly Settlement Traffic" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Bi-Weekly Settlement Traffic" )


  val advertisedProductPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Sponsored Products/Advertised Product",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Sponsored Products/Advertised Product",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Sponsored Products/Advertised Product" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Sponsored Products/Advertised Product",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Sponsored Products/Advertised Product",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Sponsored Products/Advertised Product" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Sponsored Products/Advertised Product" )

  val purchaseProductPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Sponsored Products/Purchased Product",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Sponsored Products/Purchased Product",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Sponsored Products/Purchased Product" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Sponsored Products/Purchased Product",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Sponsored Products/Purchased Product",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Sponsored Products/Purchased Product" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Sponsored Products/Purchased Product" )

  val spKeywordPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Sponsored Products/Keyword",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Sponsored Products/Keyword",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Sponsored Products/Keyword" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Sponsored Products/Keyword",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Sponsored Products/Keyword",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Sponsored Products/Keyword" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Sponsored Products/Keyword" )

  val hsaKeywordPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Headline Search Ads/Keyword",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Headline Search Ads/Keyword",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Headline Search Ads/Keyword" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Headline Search Ads/Keyword",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Headline Search Ads/Keyword",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Headline Search Ads/Keyword" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Headline Search Ads/Keyword" )

  val hsaCampaignPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Headline Search Ads/Campaign",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Headline Search Ads/Campaign",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Headline Search Ads/Campaign" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Headline Search Ads/Campaign",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Headline Search Ads/Campaign",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Headline Search Ads/Campaign" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Headline Search Ads/Campaign" )


  val oldSearchTermPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Search Term",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Search Term",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Search Term" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Search Term",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Search Term",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Search Term" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Search Term" )

  val searchTermPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Sponsored Products/Search Term",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Sponsored Products/Search Term",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Sponsored Products/Search Term" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Sponsored Products/Search Term",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Sponsored Products/Search Term",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Sponsored Products/Search Term" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Sponsored Products/Search Term" )

  val dailySearchTermPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Sponsored Products/Search Term/Daily",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Sponsored Products/Search Term/Daily",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Sponsored Products/Search Term/Daily" ,
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Sponsored Products/Search Term/Daily",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Sponsored Products/Search Term/Daily",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Sponsored Products/Search Term/Daily" ,
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Sponsored Products/Search Term/Daily" )


  val monthlyStorageFeePath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Monthly Storage Fee",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Monthly Storage Fee",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Monthly Storage Fee")

  val longTermStorageFeePath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Long Term Storage Fee",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Long Term Storage Fee",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Long Term Storage Fee" )

  val allStatementsPath = Map(
    "us" -> "hdfs://kindminds01:9000/user/Amazon/US/All Statements",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/All Statements",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/All Statements",
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/All Statements",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/All Statements",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/All Statements",
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/All Statements")

  val paymentsDateRangePath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Payments Date Range",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Payments Date Range",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Payments Date Range",
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Payments Date Range",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Payments Date Range",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Payments Date Range",
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Payments Date Range")

  val customerReturnPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Customer Return",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Customer Return",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Customer Return" )

  val campaignPathD = Map("us" -> "hdfs://dev-vm:9000/Amazon/US/Campaign",
    "ca" -> "hdfs://dev-vm:9000/Amazon/CA/Campaign",
    "uk" -> "hdfs://dev-vm:9000/Amazon/UK/Campaign" ,
    "de" -> "hdfs://dev-vm:9000/Amazon/DE/Campaign",
    "fr" -> "hdfs://dev-vm:9000/Amazon/FR/Campaign",
    "it" -> "hdfs://dev-vm:9000/Amazon/IT/Campaign" ,
    "es" -> "hdfs://dev-vm:9000/Amazon/ES/Campaign" )

  val searchTermPathD = Map("us" -> "hdfs://dev-vm:9000/Amazon/US/Search Term",
    "ca" -> "hdfs://dev-vm:9000/Amazon/CA/Search Term",
    "uk" -> "hdfs://dev-vm:9000/Amazon/UK/Search Term" ,
    "de" -> "hdfs://dev-vm:9000/Amazon/DE/Search Term",
    "fr" -> "hdfs://dev-vm:9000/Amazon/FR/Search Term",
    "it" -> "hdfs://dev-vm:9000/Amazon/IT/Search Term" ,
    "es" -> "hdfs://dev-vm:9000/Amazon/ES/Search Term" )


  val manageFBAInventoryPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Inventory/Manage FBA Inventory",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Inventory/Manage FBA Inventory",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Inventory/Manage FBA Inventory",
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Inventory/Manage FBA Inventory",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Inventory/Manage FBA Inventory",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Inventory/Manage FBA Inventory",
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Inventory/Manage FBA Inventory" )

  val inventoryRptPath = Map("us" -> "hdfs://kindminds01:9000/user/Amazon/US/Inventory/Inventory Report",
    "ca" -> "hdfs://kindminds01:9000/user/Amazon/CA/Inventory/Inventory Report",
    "uk" -> "hdfs://kindminds01:9000/user/Amazon/UK/Inventory/Inventory Report",
    "de" -> "hdfs://kindminds01:9000/user/Amazon/DE/Inventory/Inventory Report",
    "fr" -> "hdfs://kindminds01:9000/user/Amazon/FR/Inventory/Inventory Report",
    "it" -> "hdfs://kindminds01:9000/user/Amazon/IT/Inventory/Inventory Report",
    "es" -> "hdfs://kindminds01:9000/user/Amazon/ES/Inventory/Inventory Report" )





  def isFileExisted(path:String) : Boolean = {

    val fs = FileSystem.get( com.kindminds.drs.data.pipelines.core.hadoopConf)
    fs.isFile(new Path(path))


    // you need to pass in your hdfs path

    //System.out.println(today)

    /*
      var i = 0
      while (i < status.length) {
        {
          calendar.setTimeInMillis(status(i).getModificationTime)
          if (calendar.getTime.after(today)) {
            System.out.println(calendar.getTime)
            System.out.println(status(i).getPath)
          }
        }
        {
          i += 1;
          i - 1
        }
      }*/


  }

  def listFileStatus(path:String) : Array[FileStatus] = {



    val fs = FileSystem.get(com.kindminds.drs.data.pipelines.core.hadoopConf)
    fs.listStatus(new Path(path))


    // you need to pass in your hdfs path

    //System.out.println(today)

    /*
      var i = 0
      while (i < status.length) {
        {
          calendar.setTimeInMillis(status(i).getModificationTime)
          if (calendar.getTime.after(today)) {
            System.out.println(calendar.getTime)
            System.out.println(status(i).getPath)
          }
        }
        {
          i += 1;
          i - 1
        }
      }*/


  }


  def getFile(path:String) : Array[Byte] = {

    val fs = FileSystem.get(com.kindminds.drs.data.pipelines.core.hadoopConf)

    val hdfsPath: Path = new Path(path)

    val len  = fs.getFileStatus(hdfsPath).getLen
    val in: FSDataInputStream = fs.open(hdfsPath)
    val arr = new Array[Byte](len.toInt)
    in.readFully(arr)

    arr

  }


}



