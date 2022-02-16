package com.kindminds.drs.data.pipelines.core.inventory.etl

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.alpakka.csv.scaladsl.CsvParsing.{Backslash, Comma, DoubleQuote}
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult, Materializer}
import akka.util.ByteString
import com.kindminds.drs.data.pipelines.api.message.{ImportRemovalShipmentRpt, RegisterCommandHandler}

import java.nio.file.{Path, Paths}
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import scala.collection.immutable
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object AmzRemovalShipmentHandler {

  def props(drsCmdBus:ActorRef): Props =
    Props(new AmzRemovalShipmentHandler(drsCmdBus))
}

class AmzRemovalShipmentHandler(drsCmdBus:ActorRef) extends Actor with ActorLogging {
  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[ImportRemovalShipmentRpt].getName ,self)

  implicit val session = SlickSession.forConfig("slick-postgres")
  import session.profile.api._
  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  override def receive = {
    case ImportRemovalShipmentRpt(path) =>
      process(path)

  }

  def cleanseCsvData(csvData: Map[String, ByteString]): Map[String, String] =
    csvData
      .filterNot { case (key, _) => key.isEmpty }
      .mapValues(_.utf8String)
  val filePath = "C:/aie/removal_shipment_detail_US20211125.txt"
  //val filePath = "C:/aie/il20211105.txt"
  val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)



  def process(path:String) ={

//   val arr: Array[Byte] = HdfsHelper.getFile(path)

   // : Source[ByteString, _] =

    //val abc = ByteString("2020-11-17T00:00:00-08:00\t9868483506\tK601-9789868483507\t??????? Huo Ling Huo Xian\t1\tBHM1\tSELLABLE\tUS")
    //println(abc.utf8String)


   //val bs: ByteString =  ByteString.fromArray(arr)
    //println(bs.utf8String)

    val delimiter: Byte = Comma
    val quoteChar: Byte = DoubleQuote
    val escapeChar: Byte = Backslash
    val tabChar: Byte = akka.stream.alpakka.csv.scaladsl.CsvParsing.Tab
    val endOfLine = "\r\n"

    val file: Path = Paths.get(filePath)
    val byteStringSource: Source[ByteString, Future[IOResult]] =  FileIO.fromPath(file)
    var reportMarketplace = ""

    if (path=="UK"){
       reportMarketplace = "UK"
    }else{
       reportMarketplace = "US"
    }

    //val byteStringSource: Source[ByteString, _] = Source.single(bs)
//    val bs: ByteString =  ByteString.fromArray(arr)
//    val byteStringSource =  Source.single(bs)



    val result: Future[immutable.Seq[List[String]]] =
      byteStringSource
        .via(CsvParsing.lineScanner(tabChar))
        .map(_.map(_.utf8String)).runWith(Sink.seq)

    val r: Future[immutable.Seq[Map[String, String]]] =
      byteStringSource.via(CsvParsing.lineScanner(tabChar))
      .via(CsvToMap.toMap())                                       //: Map[String, ByteString] (5)
      .map(cleanseCsvData)                                         //: Map[String, String]     (6)
      .runWith(Sink.seq)

    val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/uuuu")


    result.onComplete{
      case Success(value) =>
        println(value.size)
        value.foreach(x=>{
          //println(x)
          //println(x.size)
        })

      case Failure(e) => e.printStackTrace
    }

    r onComplete {
      case Success(x) => {

        val gr =x.groupBy(x => (x.get("order-id").get ,x.get("shipment-date").get,
          x.get("sku").get))


        gr.foreach(x =>{

          val grCount=x._2.size
          val existId = queryExistId(x._1._1 , x._1._2 , x._1._3)
          val idCount = existId.size


//          println("time : "+LocalDate.parse(x._1._1,dateFormatter))
          //println("grcount = "+ grCount)
          //println("idcount = "+ idCount)
          existId.foreach(s =>{
            //println(s)
          })

          if(idCount==0) {
            x._2.foreach(y =>{
              insert(y)
            })

          }else if(grCount > idCount) {
            for(i <- 0 until grCount) {
              val y = x._2(i)
              if(i< idCount){
                update(y,existId(i))
              }else{
                insert(y)
              }
            }

          }else if(grCount == idCount){
            for(i <- 0 until grCount) {
              val y = x._2(i)
              update(y,existId(i))
            }

          }else if(grCount < idCount){
            for(i <- 0 until idCount){
              if(i< grCount){
                val y = x._2(i)
                update(y,existId(i))
              }else{
                delete(existId(i))
              }
            }
          }

          //println("---------")

        })
        println("Done")
      }
    }

    def queryExistId(oi:String,sd:String,sku:String):Array[Int]={

      var tempIdList = new Array[Int](99)

      var idCount =0;

      val query =   sql"""
                select id
                FROM inventory.amazon_removal_shipment
                where order_id='#${oi}'
                and shipment_date='#${sd}'
                and sku ='#${sku}'
                order by id asc
                  """.as[String]

      //println(query.statements)
      Await.result(
        session.db.run(query).map { res =>
          if(res.size != 0){
            idCount = res.size
            var i =0
            res.foreach(r =>{
              tempIdList(i)=Integer.valueOf(r)
              i=i+1
            })
          }
        }, timeout)

      var idList =new Array[Int](idCount)
      for( i <- 0 until idCount){
        idList(i) =tempIdList(i)
      }

      return idList
    }

    def insert(y:Map[String,String] )={
      val master =   sqlu"""
                   INSERT INTO inventory.amazon_removal_shipment(
	    report_marketplace, country, request_date, order_id, shipment_date, sku, fnsku, disposition, shipped_quantity, carrier, tracking_number, removal_order_type)
	    VALUES ('#${reportMarketplace}','#${getCountry(y.get("order-id").get)}', '#${y.get("request-date").get}', '#${y.get("order-id").get}', '#${y.get("shipment-date").get}',
	     '#${y.get("sku").get}', '#${y.get("fnsku").get}', '#${y.get("disposition").get}','#${y.get("shipped-quantity").get}', '#${y.get("carrier").get}',
	     '#${y.get("tracking-number").get}', '#${y.get("removal-order-type").get}'); """


      println(y)

      println(master.statements)
      Await.result(
        session.db.run(master).map { res =>
          //println(res)
        }, timeout)
    }

    def update(y:Map[String,String],id:Int )={
      val master =   sqlu"""
             update inventory.amazon_removal_shipment
	   SET report_marketplace='#${reportMarketplace}', country ='#${getCountry(y.get("order-id").get)}', request_date='#${y.get("request-date").get}',
	    fnsku='#${y.get("fnsku").get}', disposition='#${y.get("disposition").get}', shipped_quantity='#${y.get("shipped-quantity").get}', carrier='#${y.get("carrier").get}',
	    tracking_number='#${y.get("tracking-number").get}', removal_order_type='#${y.get("removal-order-type").get}'
	    where id ='#${id}' ;"""


      //println(master.statements)
      Await.result(
        session.db.run(master).map { res =>
          //println(res)
        }, timeout)
    }

    def delete(id:Int)={
      val del =sql"""
          delete from inventory.amazon_removal_shipment where id='#${id}'
           """.as[String]

      //println(del.statements)
      Await.result(
        session.db.run(del).map { res =>
          //println(res)
        }, timeout)
    }

    def getCountry(orderId:String): String={
      val country = orderId.slice(0,2)
        country match {
          case "UK" => "UK"
          case "EU" => "EU"
          case "DE" => "DE"
          case "FR" => "FR"
          case "CZ" => "CZ"
          case "US" => "US"
          case "CA" => "CA"
          case _ =>"Undefined"


      }

    }



  }


}
