package com.kindminds.drs.data.pipelines.core.reimbursements

import java.nio.file.{Path, Paths}
import java.sql.Timestamp
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.stream.alpakka.csv.scaladsl.CsvParsing.{Backslash, Comma, DoubleQuote}
import akka.stream.{ActorMaterializer, IOResult, Materializer}
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString
import com.fasterxml.uuid.Generators
import com.kindminds.drs.data.pipelines.api.message.{ImportReimbursementRpt, RegisterCommandHandler}
import com.kindminds.drs.data.pipelines.core.settlement.CustomDuty.{cleanseCsvData, filePath, processMasterAmount}
import com.kindminds.drs.data.pipelines.core.settlement.IntTran.{timeout, transactionDate}
import com.kindminds.drs.data.pipelines.core.util.HdfsHelper

import scala.collection.immutable
import scala.concurrent.{Await, Future}
import scala.util.Success

object ReimbursementsEtl {

  def props(drsCmdBus:ActorRef): Props =
    Props(new ReimbursementsEtl(drsCmdBus))
}

class ReimbursementsEtl(drsCmdBus:ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[ImportReimbursementRpt].getName ,self)

  implicit val session = SlickSession.forConfig("slick-postgres")
  import session.profile.api._
  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()



  def cleanseCsvData(csvData: Map[String, ByteString]): Map[String, String] =
    csvData
      .filterNot { case (key, _) => key.isEmpty }
      .mapValues(_.utf8String)

  val filePath = "C:/Users/Ralph Lin/Documents/reimbursement/removal_shipment_detail_UK20211125.txt"
  val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)


  def process(path:String) ={

    println(path)
//    val arr: Array[Byte] = HdfsHelper.getFile(path)


    // : Source[ByteString, _] =

    //val abc = ByteString("2020-11-17T00:00:00-08:00\t9868483506\tK601-9789868483507\t??????? Huo Ling Huo Xian\t1\tBHM1\tSELLABLE\tUS")
    //println(abc.utf8String)




    val delimiter: Byte = Comma
    val quoteChar: Byte = DoubleQuote
    val escapeChar: Byte = Backslash
    val tabChar: Byte = akka.stream.alpakka.csv.scaladsl.CsvParsing.Tab
    val endOfLine = "\r\n"

    val file: Path = Paths.get(filePath)
    val byteStringSource: Source[ByteString, Future[IOResult]] =  FileIO.fromPath(file)

//    val bs: ByteString =  ByteString.fromArray(arr)
//    val byteStringSource =  Source.single(bs)
    //val byteStringSource: Source[ByteString, _] = Source.single(bs)



    val result: Future[immutable.Seq[List[String]]] =
      byteStringSource
        .via(CsvParsing.lineScanner(tabChar))
        .map(_.map(_.utf8String)).runWith(Sink.seq)

    val r: Future[immutable.Seq[Map[String, String]]] =
      byteStringSource.via(CsvParsing.lineScanner(tabChar))
        .via(CsvToMap.toMap())                                       //: Map[String, ByteString] (5)
        .map(cleanseCsvData)                                         //: Map[String, String]     (6)
        .runWith(Sink.seq)

    r onComplete {
      case Success(x) => {

        val gr = x.groupBy( x=> (x.get("reimbursement-id").get) )

        gr.foreach(x => {

          val ct = Timestamp.valueOf(OffsetDateTime.now().toLocalDateTime)

          println(x._1)

          val deletesql =sql""" DELETE FROM finance.amazon_reimbursement
                                where reimbursement_id = '#${x._1}' """.as[String]

          println(deletesql.statements)
          Await.result(
            session.db.run(deletesql).map { res =>

            }, timeout)


          x._2.foreach(y=>{

            //println(y.get("reimbursement-id"))

            val id = Generators.randomBasedGenerator.generate.toString

            val ad = y.get("approval-date").get
            val caseId =  y.get("case-id").get
            val amazonOrderId = y.get("amazon-order-id").get
            val reason = y.get("reason").get
            val sku = y.get("sku").get
            val fnsku = y.get("fnsku").get
            val asin = y.get("asin").get

            val pName = if(y.get("product-name").get.contains("'"))
              y.get("product-name").get.replace("'","''")
            else y.get("product-name").get

            val condtion = y.get("condition").get
            val currency = y.get("currency-unit").get
            val amazonPerUnit = y.get("amount-per-unit").get
            val amountTotal = y.get("amount-total").get

            val qtyRc = if(y.get("quantity-reimbursed-cash").get == "") null else
              y.get("quantity-reimbursed-cash").get
            val qtyRi = if(y.get("quantity-reimbursed-inventory").get == "" ) null else
              y.get("quantity-reimbursed-inventory").get

            val qtyRt = if( y.get("quantity-reimbursed-total").get == "") null else
              y.get("quantity-reimbursed-total").get

            val oRid = y.get("original-reimbursement-id").get
            val oRtype = y.get("original-reimbursement-type").get



            val master =   sqlu"""  INSERT INTO finance.amazon_reimbursement
             (id, create_time, reimbursement_id, approval_date, case_id, amazon_order_id,
             reason, sku, fnsku, asin, product_name, "condition", currency_unit,
             amount_per_unit, amount_total, quantity_reimbursed_cash, quantity_reimbursed_inventory,
             quantity_reimbursed_total, original_reimbursement_id, original_reimbursement_type)
            VALUES( '#${id}', '#${ct}', '#${x._1}', '#${ad}', '#${caseId}', '#${amazonOrderId}', '#${reason}', '#${sku}',
            '#${fnsku}','#${asin}', '#${pName}', '#${condtion}', '#${currency}', #${amazonPerUnit},
             #${amountTotal}, #${qtyRc}, #${qtyRi}, #${qtyRt}, '#${oRid}', '#${oRtype}');
              """


            println(master.statements)

            Await.result(
              session.db.run(master).map { res =>
                println(res)
              }, timeout)



          })

        })

      println("Done")
      }
    }




  }

  def processDetail(): Unit = {

  }

  override def receive = {
    case ImportReimbursementRpt(path) =>
      process(path)

  }
}
