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
import com.kindminds.drs.data.pipelines.api.message.{ImportReceivedInventoryRpt, RegisterCommandHandler}
import com.kindminds.drs.data.pipelines.core.util.HdfsHelper

import scala.collection.immutable
import scala.concurrent.{Await, Future}
import scala.util.Success

object AmzReceivedInventoryHandler {

  def props(drsCmdBus:ActorRef): Props =
    Props(new AmzReceivedInventoryHandler(drsCmdBus))
}

class AmzReceivedInventoryHandler(drsCmdBus:ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[ImportReceivedInventoryRpt].getName ,self)

  implicit val session = SlickSession.forConfig("slick-postgres")
  import session.profile.api._
  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  override def receive ={
    case ImportReceivedInventoryRpt(path) =>
      process(path)
  }

  def cleanseCsvData(csvData: Map[String, ByteString]): Map[String, String] =
    csvData
      .filterNot { case (key, _) => key.isEmpty }
      .mapValues(_.utf8String)

  val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

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

    val bs: ByteString =  ByteString.fromArray(arr)
    val byteStringSource =  Source.single(bs)
    //val file: Path = Paths.get(filePath)
    //val byteStringSource: Source[ByteString, Future[IOResult]] =  FileIO.fromPath(file)
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

        val gr =x.groupBy(x => (x.get("received-date").get ,x.get("fnsku").get,x.get("fba-shipment-id").get,x.get("fulfillment-center-id").get))


        gr.foreach(x =>{

          val grCount=x._2.size
          val existId = queryExistId(x._1._1 , x._1._2 , x._1._3 , x._1._4)
          val idCount = existId.size

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

    def queryExistId(rd:String,fnsku:String,fsi:String,fci:String):Array[Int]={

      var tempIdList = new Array[Int](99)

      var idCount =0;

      val query =   sql"""
                select id
                FROM inventory.amazon_received_inventory
                where received_date='#${rd}'
                and fnsku='#${fnsku}'
                and  fba_shipment_id='#${fsi}'
                and fulfillment_center_id='#${fci}'
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
                   INSERT INTO inventory.amazon_received_inventory(
	        received_date, fnsku, sku, product_name, quantity, fba_shipment_id, fulfillment_center_id)
          VALUES('#${y.get("received-date").get}' , '#${y.get("fnsku").get}', '#${y.get("sku").get}',
          '#${y.get("product-name").get.replace("'","")}', '#${y.get("quantity").get}',
          '#${y.get("fba-shipment-id").get}' ,'#${y.get("fulfillment-center-id").get}'); """


      //println(master.statements)
      Await.result(
        session.db.run(master).map { res =>
          //println(res)
        }, timeout)
    }

    def update(y:Map[String,String],id:Int )={
      val master =   sqlu"""
             update inventory.amazon_received_inventory
	     SET sku= '#${y.get("sku").get}', product_name= '#${y.get("product-name").get.replace("'","")}',
	     quantity='#${y.get("quantity").get}'
       where id ='#${id}' ;
                """


      //println(master.statements)
      Await.result(
        session.db.run(master).map { res =>
          //println(res)
        }, timeout)
    }

    def delete(id:Int)={
      val del =sql"""
          delete from inventory.amazon_received_inventory where id='#${id}'
           """.as[String]

      //println(del.statements)
      Await.result(
        session.db.run(del).map { res =>
          //println(res)
        }, timeout)
    }


  }


}
