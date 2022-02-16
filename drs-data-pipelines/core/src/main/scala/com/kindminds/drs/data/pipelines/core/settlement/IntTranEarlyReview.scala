package com.kindminds.drs.data.pipelines.core.settlement


import java.sql.Timestamp
import java.time.{Instant, ZoneOffset, ZonedDateTime}
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.scaladsl.{Sink, Source}
import com.kindminds.drs.data.pipelines.core.dto.{EarlyReviewIntTran, SettlementDates}
import slick.jdbc.GetResult

import scala.concurrent.{Await, Future, TimeoutException}


object IntTranEarlyReview {

  def main(args: Array[String]): Unit = {

    //will use latest settlement start and end if these are left null
    var start: Instant = null
    var end: Instant = null

//    start = ZonedDateTime.of(
//      2021, 1, 3, 0, 0, 0, 0,
//      ZoneOffset.UTC).toInstant
//
//    end = ZonedDateTime.of(
//      2021, 1, 17, 0, 0, 0, 0,
//      ZoneOffset.UTC).toInstant

    processMaster(start, end)

  }



  case class IntTranEarlyReview(id:Int , kcode:String  , note:String ,  item:Int , msdc:String , amount:BigDecimal,direction:Int)
  case class IntTranEarlyReviewId(id:Int)


  val marketplaceMsdc = Map(
    1 -> "K3",
    4 -> "K4",
    5 -> "K5",
    6 -> "K6",
    7 -> "K7",
    8 -> "K8",
    9 -> "K9"
  )

  val msdcKcode = Map(
    "K3 Beanworthy" -> "K3",
    "K4 BW-UK" -> "K4",
    "K5 BW-CA" -> "K5" ,
    "K6 BW DE" -> "K6",
    "K7 BW FR" -> "K7",
    "K8 BW IT" -> "K8" ,
    "K9 BW ES" -> "K9"
  )

  val marketplaceCurrency = Map(
    1 -> 2,
    4 -> 3,
    5 -> 4 ,
    6 -> 5,
    7 -> 5,
    8 -> 5 ,
    9 -> 5
  )


  val dirKey = Map("Supplier to MSDC" -> 0,
    "MSDC to Supplier" -> 1
  )


  val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

  implicit val getIntTranEarlyReviewIdResult = GetResult(r => {
    IntTranEarlyReviewId(r.nextInt())
  })



  def processMaster(startTime : Instant, endTime : Instant):Unit = {

    implicit val system = ActorSystem("drsDP")
    implicit val session = SlickSession.forConfig("slick-postgres")
    import session.profile.api._
    implicit val ec =  scala.concurrent.ExecutionContext.global
    implicit val materializer: Materializer = ActorMaterializer()

    val db = session.db

    var periodStart = startTime
    var periodEnd = endTime


    //get latest settlement if start or end are null
    if (periodStart == null || periodEnd == null) {

      val querySql =  sql""" SELECT id, period_start, period_end
                       FROM public.settlement_period
                       order by period_end desc limit 1 """.as[SettlementDates]

      var settlementDates : SettlementDates = null
      try {
        Await.result(
          db.run(querySql).map { res =>

            settlementDates = res(0)
            periodStart = settlementDates.period_start.toInstant
            periodEnd = settlementDates.period_end.toInstant

          }, timeout)

      } catch {

        case e: TimeoutException =>
          println("Time out ")

      }
    }



    var earlyReviewerList: List[EarlyReviewIntTran] = null
    val querySql =  sql""" select id, marketplace_id, inserted_on, date_time,
                       description, marketplace, other_transaction_fees, total
                       FROM public.amazon_date_range_report
                       where description like '%Early Reviewer%'
                       and date_time >= '#${Timestamp.from(periodStart)}'
                       and date_time < '#${Timestamp.from(periodEnd)}'
                  order by date_time """.as[EarlyReviewIntTran]
    try {
      Await.result(
        db.run(querySql).map { res =>

          earlyReviewerList = res.toList

        }, timeout)

      if (earlyReviewerList.isEmpty) {
        println("earlyReviewerList is empty")

      } else {
        earlyReviewerList.foreach(x => {


          val msdc = marketplaceMsdc(x.marketplace_id)
          val curr = marketplaceCurrency(x.marketplace_id)
          val direction = 0
          println(msdc)
          println(x.description)
          val splitDescription = x.description.split(" ")
          val parentAsin = splitDescription(splitDescription.length - 1)
          println(parentAsin)

          //use parent asin to get sku
          val skuQuerySql = sql""" SELECT marketplace_sku
                   FROM public.amazon_detail_page_sales_traffic_report_by_childitem_daily adp
                   inner join product_marketplace_info_amazon pmia on pmia.asin = adp.child_asin
                   inner join product_marketplace_info pmi on pmi.id = pmia.product_marketplace_info_id
                   where parent_asin = '#$parentAsin' limit 1 """.as[String]

          var sku: String = null

          Await.result(
            db.run(skuQuerySql).map { res =>

              sku = res(0)

            }, timeout)

          val companyCode = sku.substring(0, 4)
          println(companyCode)

          val newDescription = "Early Reviewer Program fee for " + sku

          val total = x.total.abs


          val insertTranSql =   sqlu""" INSERT INTO international_transaction (msdc_company_id,
                    ssdc_company_id, splr_company_id, transaction_date, currency_id, total, note,
                    cash_flow_direction_key) select (select id from company where k_code ='#$msdc' )
                    , 2, c.id, '#${x.date_time}', #$curr, '#$total', 'Early Reviewer Program ', 0
          from company as c  where c.k_code ='#$companyCode' """

          Await.result(
            session.db.run(insertTranSql).map { res =>
              println(res)
            }, timeout)


          //get international transaction id for line item
          val q = sql"""
            select id from international_transaction
            where msdc_company_id = (select id from company where k_code ='#$msdc')
            and ssdc_company_id = 2 and splr_company_id = (select id from
            company where k_code = '#$companyCode') and transaction_date = '#${x.date_time}'""".as[Int]

          println(q.statements)

          var id:Int = 0
          Await.result(
            session.db.run(q).map { res =>

              id = res(0)
            }, timeout)



          val lineitem =   sqlu""" INSERT INTO public.international_transaction_line_item(
                         transaction_id, line_seq, type_id, subtotal, note)
                         VALUES ('#$id', 1, 5, '#$total', '#$newDescription');"""

//          println(lineitem.statements)
          Await.result(
            session.db.run(lineitem).map { res =>
              println(res)
            }, timeout)


        })


      }


    } catch {

      case e: TimeoutException =>
        println("Time out ")
        session.close()

    }


    session.close()

  }



}
