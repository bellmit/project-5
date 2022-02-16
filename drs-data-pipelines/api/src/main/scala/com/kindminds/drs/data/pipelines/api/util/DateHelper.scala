package com.kindminds.drs.data.pipelines.api.util

import java.text.SimpleDateFormat
import java.util.{Calendar, Date, Locale}

import org.joda.time.{DateTimeZone, LocalDateTime}
import org.joda.time.format.DateTimeFormat


object DateHelper {

  def convertStrToJavaLocalDate(date:String): java.time.LocalDate ={

    java.time.LocalDate.parse(date , java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))

  }

  def convertStrToJavaLocalDate(date:String , format : String): java.time.LocalDate ={

    java.time.LocalDate.parse(date , java.time.format.DateTimeFormatter.ofPattern(format))

  }

  def today(): String = {
    val calendar = Calendar.getInstance
    val sdf = new SimpleDateFormat("yyyyMMdd")
    sdf.format(calendar.getTime)
  }

  def today(format : String): String = {
    val calendar = Calendar.getInstance
    val sdf = new SimpleDateFormat(format)
    sdf.format(calendar.getTime)
  }

  def currentTime(): String = {
    val calendar = Calendar.getInstance
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    sdf.format(calendar.getTime)
  }

  def getPreviousMonth(): Int ={

    val dt: LocalDateTime =  LocalDateTime.now()
    dt.minusMonths(1).getMonthOfYear

  }

  def getPreviousYear(): Int ={

    val dt: LocalDateTime =  LocalDateTime.now()
    dt.minusYears(1).getYear

  }

  def getCurrentYear(): Int ={

    val dt: LocalDateTime =  LocalDateTime.now()
    dt.getYear

  }

  def getPreviousMonthShortName(): String ={

    val dt: LocalDateTime =  LocalDateTime.now()
    val d = dt.minusMonths(1).withDayOfMonth(1)

    val sdf = new SimpleDateFormat("MMM")

    sdf.format(d.toDate)


  }

  def getPreviousMonthFirstDay(marketPlace:String): String ={

    val dt: LocalDateTime =  LocalDateTime.now()
    val d = dt.minusMonths(1).withDayOfMonth(1)

    var pattern = "MMM d, yyyy"
    if(marketPlace == "uk")pattern = "d MMM yyyy"
    else if(marketPlace == "ca")pattern = "d MMM, yyyy"

    val sdf = new SimpleDateFormat(pattern)
    sdf.format(d.toDate)


  }


  def getPreviousMonthLastDay(marketPlace:String): String ={

    val dt: LocalDateTime =  LocalDateTime.now()
    val d = dt.withDayOfMonth(1).minusDays(1)

    var pattern = "MMM d, yyyy"
    if(marketPlace == "uk")pattern = "d MMM yyyy"
    else if(marketPlace == "ca")pattern = "d MMM, yyyy"

    val sdf = new SimpleDateFormat(pattern)

    sdf.format(d.toDate())

  }

  def getCurrentMonthDisplayName : String = {

    val c = Calendar.getInstance()
    c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())

  }

  def getUSToday(): String ={

    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val dt: LocalDateTime =  LocalDateTime.now(DateTimeZone.forID("America/Los_Angeles"))
    sdf.format(dt.toDate)

  }

  def getUSPast60Day(): String ={

    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val dt: LocalDateTime =  LocalDateTime.now(DateTimeZone.forID("America/Los_Angeles"))
    val dt60: LocalDateTime = dt.minusDays(60)
    sdf.format(dt60.toDate)

  }

  def getEUToday(timeZone : String = "GMT"): String ={

    var tid = "Europe/Paris"
    if(timeZone == "BDT")tid = "Europe/London"

    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val dt: LocalDateTime =  LocalDateTime.now(DateTimeZone.forID(tid))
    sdf.format(dt.toDate)

  }

  def getNACurrenttime(): LocalDateTime ={

    val dt: LocalDateTime =  LocalDateTime.now(DateTimeZone.forID("America/Los_Angeles"))
    dt

  }

  def getEUCurrenttime(timeZone : String = "GMT" ): LocalDateTime ={

    var tid = "Europe/Paris"
    if(timeZone == "BDT")tid = "Europe/London"

    val dt: LocalDateTime =  LocalDateTime.now(DateTimeZone.forID(tid))
    dt

  }

  def getEUPast60Day(timeZone : String = "GMT"): String ={

    var tid = "Europe/Paris"
    if(timeZone == "BDT")tid = "Europe/London"

    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val dt: LocalDateTime =  LocalDateTime.now(DateTimeZone.forID(tid))
    val dt60: LocalDateTime = dt.minusDays(60)
    sdf.format(dt60.toDate)

  }

  def convertEUTime(euTime:String): LocalDateTime = {

    val format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    val sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    LocalDateTime.parse(sdf2.format(sdf.parse(euTime)),format)


  }

  def convertEUDate(euDate:String): String = {

    val sdf = new SimpleDateFormat("dd/MM/yyyy")
    val sdf2 = new SimpleDateFormat("yyyy-MM-dd")
    sdf2.format(sdf.parse(euDate))

  }

  def convertUSTime4AMPM(usTime:String): LocalDateTime = {

    val format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    val sdf = new SimpleDateFormat("M/dd/yy hh:mm:ss a")
    val sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    LocalDateTime.parse(sdf2.format(sdf.parse(usTime)),format)


  }

  def convertUSDate(usDate:String): String ={

    val sdf = new SimpleDateFormat("M/dd/yy")
    val sdf2 = new SimpleDateFormat("yyyy-MM-dd")
    sdf2.format(sdf.parse(usDate))
  }

  def convertToHDFSDte(date:String , format : String): String ={

    val sdf = new SimpleDateFormat(format)
    val sdf2 = new SimpleDateFormat("yyyyMMdd")
    sdf2.format(sdf.parse(date))
  }

  def getUSDailyPageTrafficDate(): String ={

    val calendar = Calendar.getInstance
    calendar.add(Calendar.DATE,-2)
    val sdf2 = new SimpleDateFormat("MM/dd/yyyy")
    sdf2.format(calendar.getTime)

  }

  def getUSCustomerReturnEndDate(): String ={

    val calendar = Calendar.getInstance
    val sdf2 = new SimpleDateFormat("MM/dd/yyyy")
    sdf2.format(calendar.getTime)

  }

  def getUSDailyPageTrafficDate(baseDate:String): String ={

    val sdf = new SimpleDateFormat("yyyy/MM/dd");
    val date = sdf.parse(baseDate)

    val calendar = Calendar.getInstance
    calendar.setTime(date)
    calendar.add(Calendar.DATE,-2)

    val sdf2 = new SimpleDateFormat("MM/dd/yyyy")
    sdf2.format(calendar.getTime)

  }


  def getUS14DaysDailyPageTrafficDate():Seq[String] ={


    val sdf2 = new SimpleDateFormat("MM/dd/yyyy")

    val x = List.range(0, 14)
    x.map(y=>{
      val calendar = Calendar.getInstance
      calendar.add(Calendar.DATE, (-2-y))
      sdf2.format(calendar.getTime)
    })


  }

  def getUSDailyPageTrafficFileDate(): String ={

    val sdf = new SimpleDateFormat("M-d-yy")
    val dt: LocalDateTime =  LocalDateTime.now(DateTimeZone.forID("America/Los_Angeles"))
    sdf.format(dt.toDate)

  }

  def getEUDailyPageTrafficFileDate(): String ={

    val sdf = new SimpleDateFormat("dd-MM-YYYY")
    val dt: LocalDateTime =  LocalDateTime.now(DateTimeZone.forID("Europe/London"))
    sdf.format(dt.toDate)

  }



  def getUSBiWeekPageTrafficDate():Tuple2[String,String] ={

    val calendar = Calendar.getInstance
    calendar.add(Calendar.DATE,-3)
    val sdf2 = new SimpleDateFormat("MM/dd/yyyy")
    val eDate = sdf2.format(calendar.getTime)

    calendar.add(Calendar.DATE,-13)
    val sDate = sdf2.format(calendar.getTime)

    (sDate,eDate)
  }


  def getUSBiWeekPageTrafficDate(baseDate:String):Tuple2[String,String] ={

    val sdf = new SimpleDateFormat("yyyy/MM/dd");
    val date = sdf.parse(baseDate)

    val calendar = Calendar.getInstance
    calendar.setTime(date)
    calendar.add(Calendar.DATE,-3)

    val sdf2 = new SimpleDateFormat("MM/dd/yyyy")
    val eDate = sdf2.format(calendar.getTime)

    calendar.add(Calendar.DATE,-13)
    val sDate = sdf2.format(calendar.getTime)

    (sDate,eDate)
  }

  def getEUDailyPageTrafficDate(): String ={


    val calendar = Calendar.getInstance
    calendar.add(Calendar.DATE,-2)
    val sdf3 = new SimpleDateFormat("dd/MM/yyyy")
    sdf3.format(calendar.getTime)

  }


  def getEU14DaysDailyPageTrafficDate():Seq[String] ={


    val sdf2 = new SimpleDateFormat("dd/MM/yyyy")

    val x = List.range(0, 14)
    x.map(y=>{
      val calendar = Calendar.getInstance
      calendar.add(Calendar.DATE, (-2-y))
      sdf2.format(calendar.getTime)
    })


  }

  def getEUCustomerReturnEndDate(): String ={

    val calendar = Calendar.getInstance
    val sdf3 = new SimpleDateFormat("dd/MM/yyyy")
    sdf3.format(calendar.getTime)

  }

  def getNACustomerReturnEndDate(): String ={

    val calendar = Calendar.getInstance
    val sdf3 = new SimpleDateFormat("MM/dd/yyyy")
    sdf3.format(calendar.getTime)

  }

  def getEUDailyPageTrafficDate(baseDate:String): String ={

    val sdf = new SimpleDateFormat("yyyy/MM/dd");
    val date = sdf.parse(baseDate)

    val calendar = Calendar.getInstance
    calendar.setTime(date)

    calendar.add(Calendar.DATE,-2)
    val sdf3 = new SimpleDateFormat("dd/MM/yyyy")
    sdf3.format(calendar.getTime)

  }

  def getEUBiWeekPageTrafficDate():Tuple2[String,String] ={

    val calendar = Calendar.getInstance
    calendar.add(Calendar.DATE,-3)
    val sdf3 = new SimpleDateFormat("dd/MM/yyyy")
    val eDate = sdf3.format(calendar.getTime)

    calendar.add(Calendar.DATE,-13)
    val sDate = sdf3.format(calendar.getTime)

    (sDate,eDate)
  }

  def getEUBiWeekPageTrafficDate(baseDate:String):Tuple2[String,String] ={

    val sdf = new SimpleDateFormat("yyyy/MM/dd");
    val date = sdf.parse(baseDate)

    val calendar = Calendar.getInstance
    calendar.setTime(date)

    calendar.add(Calendar.DATE,-3)
    val sdf3 = new SimpleDateFormat("dd/MM/yyyy")
    val eDate = sdf3.format(calendar.getTime)

    calendar.add(Calendar.DATE,-13)
    val sDate = sdf3.format(calendar.getTime)

    (sDate,eDate)
  }

  def getAdvRptStartDateMs(baseDate:String): Long ={


    val sdf = new SimpleDateFormat("yyyyMMdd")
    val date = sdf.parse(baseDate)

    val calendar = Calendar.getInstance
    calendar.setTime(date)

    calendar.add(Calendar.DAY_OF_MONTH, -60)
    calendar.getTime.getTime

  }

  def getDailyAdvRptStartDateMs(baseDate:String , minusDays : Int): Long ={

    val sdf = new SimpleDateFormat("yyyyMMdd")
    val date = sdf.parse(baseDate)

    val calendar = Calendar.getInstance
    calendar.setTime(date)

    calendar.add(Calendar.DAY_OF_MONTH, (-2) - minusDays)
    calendar.getTime.getTime

  }

  def getDailyAdvProductRptStartDateMs( baseDate:String , minusDays : Int): Long ={

    val sdf = new SimpleDateFormat("yyyyMMdd")
    val date: Date = sdf.parse(baseDate)

    val calendar = Calendar.getInstance
    calendar.setTime(date)

    calendar.add(Calendar.DAY_OF_MONTH, minusDays)
    calendar.getTime.getTime

  }



  def getInventoryRptToday(): String = {
    val calendar = Calendar.getInstance
    val sdf = new SimpleDateFormat("MM-dd-yyyy")
    sdf.format(calendar.getTime)
  }





}
