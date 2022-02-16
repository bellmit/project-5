package com.kindminds.drs.data.pipelines.core.inventory.etl

import java.nio.file.{Path, Paths}
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.stream.alpakka.csv.scaladsl.CsvParsing.{Backslash, Comma, DoubleQuote}
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult, Materializer}
import akka.util.ByteString
import com.kindminds.drs.data.pipelines.api.message.{ImportReservedInventoryRpt, RegisterCommandHandler}
import com.kindminds.drs.data.pipelines.core.util.HdfsHelper

import scala.collection.immutable
import scala.concurrent.{Await, Future}
import scala.util.Success

object AmzReservedInventoryEtl {

  def props(drsCmdBus:ActorRef): Props =
    Props(new AmzReservedInventoryEtl(drsCmdBus))
}

class AmzReservedInventoryEtl(drsCmdBus:ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[ImportReservedInventoryRpt].getName ,self)


  implicit val session = SlickSession.forConfig("slick-postgres")
  import session.profile.api._
  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()



  def cleanseCsvData(csvData: Map[String, ByteString]): Map[String, String] =
    csvData
      .filterNot { case (key, _) => key.isEmpty }
      .mapValues(_.utf8String)

  //val filePath = "C:/aie/ReservedInventory_20201211.txt"
  val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

  override def receive = {
    case ImportReservedInventoryRpt(path) =>

      process(path)
  }

  def process(path:String) ={



   val arr: Array[Byte] = HdfsHelper.getFile(path)

   // : Source[ByteString, _] =
   //val bs: ByteString =  ByteString.fromArray(arr)
    //println(bs.utf8String)

    val delimiter: Byte = Comma
    val quoteChar: Byte = DoubleQuote
    val escapeChar: Byte = Backslash
    val tabChar: Byte = akka.stream.alpakka.csv.scaladsl.CsvParsing.Tab
    val endOfLine = "\r\n"

    //val file: Path = Paths.get(filePath)
    //val byteStringSource: Source[ByteString, Future[IOResult]] =  FileIO.fromPath(file)

    val bs: ByteString =  ByteString.fromArray(arr)
    val byteStringSource =  Source.single(bs)

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


        x.foreach(y=>{

          val master =   sqlu"""
          INSERT INTO inventory.amazon_reserved_inventory(sku, fnsku, asin, product_name, reserved_qty,
          reserved_customer_orders, reserved_fc_transfers, reserved_fc_processing)
          VALUES( '#${y.get("fnsku").get}', '#${y.get("sku").get}', '#${y.get("asin").get}',
          '#${y.get("product-name").get.replace("'","")}', '#${y.get("reserved_qty").get}'
          ,'#${y.get("reserved_customerorders").get}' ,
          '#${y.get("reserved_fc-transfers").get}' ,'#${y.get("reserved_fc-processing").get}'); """

          Await.result(
            session.db.run(master).map { res =>
             // println(res)
            }, timeout)


        })
      }
    }



  }


}
