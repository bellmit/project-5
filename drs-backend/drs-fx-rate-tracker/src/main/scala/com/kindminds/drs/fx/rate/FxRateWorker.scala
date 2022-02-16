package com.kindminds.drs.fx.rate

import java.time.format.DateTimeFormatter
import java.time.{OffsetDateTime, ZoneOffset}
import java.util

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.Currency
import com.kindminds.drs.fx.rate.{Complete, Start}
import com.kindminds.drs.fx.rate.constant.InterBankRate
import org.springframework.context.annotation.AnnotationConfigApplicationContext

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success

object FxRateWorker {

  def props(springCtx: AnnotationConfigApplicationContext): Props =
    Props(new FxRateWorker(springCtx))

}

case class StartScrapeFxRate()
case class StartScrapeDailyFxRate()

class FxRateWorker(springCtx: AnnotationConfigApplicationContext) extends Actor with ActorLogging {

  implicit val timeout = Timeout(60 seconds)
  implicit val ec =  scala.concurrent.ExecutionContext.global

  val name = self.path.name

  val currencyDao = springCtx.getBean(classOf[com.kindminds.drs.api.data.access.rdb.CurrencyDao])
    .asInstanceOf[com.kindminds.drs.api.data.access.rdb.CurrencyDao]

  def receive = {

    case st : StartScrapeFxRate =>


      val fxRateRetriever = new FxRateRetriever
      val fxrate = new FxRate(fxRateRetriever)

      val now =   OffsetDateTime.of(2015,1,1 ,0,0,0,0,ZoneOffset.UTC)
      process(fxrate , now)

      fxRateRetriever.close()

    case st : StartScrapeDailyFxRate =>

      val fxRateRetriever = new FxRateRetriever
      val fxrate = new FxRate(fxRateRetriever)

      processDailyRate(fxrate)

      fxRateRetriever.close()



    case message: Any =>
      log.info(s"FxRateWorker: received unexpected: $message")
  }

  def process(fxrate:FxRate , baseDate : OffsetDateTime){


    //val date = baseDate.toLocalDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))

    if(baseDate.isBefore(OffsetDateTime.of(2016,1,1 ,0,0,0,0,ZoneOffset.UTC))){

      scrape(fxrate , baseDate )

      process(fxrate,baseDate.plusDays(1))

    }


  }

  def processDailyRate(fxrate:FxRate){

    val now = OffsetDateTime.now(ZoneOffset.UTC).withMinute(0).withSecond(0).withNano(0)

   // val date = now.toLocalDateTime.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))

    scrape(fxrate , now )


  }

  def scrape(fxrate:FxRate ,  date : OffsetDateTime): Unit ={


    //System.out.//println("-- SS2SP")
    fxrate.retrieve(Currency.USD, Currency.TWD, date, InterBankRate.PERCENTAGE_1)
    currencyDao.insertExchangeRate(Currency.USD, Currency.TWD,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    fxrate.retrieve(Currency.GBP, Currency.TWD, date, InterBankRate.PERCENTAGE_1)
    currencyDao.insertExchangeRate(Currency.GBP, Currency.TWD,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    fxrate.retrieve(Currency.CAD, Currency.TWD, date, InterBankRate.PERCENTAGE_1)
    currencyDao.insertExchangeRate(Currency.CAD, Currency.TWD,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    fxrate.retrieve(Currency.EUR, Currency.TWD, date, InterBankRate.PERCENTAGE_1)
    currencyDao.insertExchangeRate(Currency.EUR, Currency.TWD,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    //System.out.//println("-- MS2SS")
    fxrate.retrieve(Currency.GBP, Currency.USD, date, InterBankRate.PERCENTAGE_1)
    currencyDao.insertExchangeRate(Currency.GBP, Currency.USD,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    fxrate.retrieve(Currency.CAD, Currency.USD, date, InterBankRate.PERCENTAGE_1)
    currencyDao.insertExchangeRate(Currency.CAD, Currency.USD,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    fxrate.retrieve(Currency.EUR, Currency.USD, date, InterBankRate.PERCENTAGE_1)
    currencyDao.insertExchangeRate(Currency.EUR, Currency.USD,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    //System.out.//println("-- RTMNT RATE")
    fxrate.retrieve(Currency.GBP, Currency.USD, date, InterBankRate.PERCENTAGE_0)
    currencyDao.insertExchangeRate(Currency.GBP, Currency.USD,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    fxrate.retrieve(Currency.CAD, Currency.USD, date, InterBankRate.PERCENTAGE_0)
    currencyDao.insertExchangeRate(Currency.CAD, Currency.USD,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    fxrate.retrieve(Currency.EUR, Currency.USD, date, InterBankRate.PERCENTAGE_0)
    currencyDao.insertExchangeRate(Currency.EUR, Currency.USD,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    //System.out.//println("-- CUSTOMER CASE")
    fxrate.retrieve(Currency.USD, Currency.GBP, date, InterBankRate.PERCENTAGE_0)
    currencyDao.insertExchangeRate(Currency.USD, Currency.GBP,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    fxrate.retrieve(Currency.USD, Currency.CAD, date, InterBankRate.PERCENTAGE_0)
    currencyDao.insertExchangeRate(Currency.USD, Currency.CAD,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

    fxrate.retrieve(Currency.USD, Currency.EUR, date, InterBankRate.PERCENTAGE_0)
    currencyDao.insertExchangeRate(Currency.USD, Currency.EUR,fxrate.getDate,
      fxrate.getRate,fxrate.getInterBankRate)

  }

}

