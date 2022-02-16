package com.kindminds.drs.data.pipelines.core.settlement

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.util.ByteString
import slick.jdbc.GetResult

import scala.collection.immutable
import scala.concurrent.Future
import scala.util.{Failure, Success}

object CustomDuty  {


  implicit val system = ActorSystem("drsDP")
  implicit val session = SlickSession.forConfig("slick-postgres" )
  import session.profile.api._
  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()


  val currency = Map(
    "TWD" -> 1,
    "USD" -> 2,
    "GBP" -> 3 ,
    "CAD" -> 4,
    "EUR" -> 5
  )


  def main(args: Array[String]): Unit = {

//    processMaster()
//    processDetail()
   processMasterAmount()

  }

  def cleanseCsvData(csvData: Map[String, ByteString]): Map[String, String] =
    csvData
      .filterNot { case (key, _) => key.isEmpty }
      .mapValues(_.utf8String)

 // case class IntTran(id:Int , kcode:String , , ,, )
  case class RawCustomDuty(id:Int , uns:String  , ivs:String ,  sku:String )
  case class IntTranId(id:Int)

  /*
  * INSERT INTO public.import_duty_transaction_line_item
(transaction_id, shipment_uns_line_item_id, amount)
VALUES(0, 0, 0);
*/

  case class CustomDutyItem(id:Int , dutyId:Int ,unsItemId:Int  , amount: BigDecimal )

//  val filePath = "C:/Users/Ralph Lin/Documents/transaction import/DRS 應收帳款明細2020 (confidential) - Import Customs Fee.csv"
  val filePath = "C:/Users/cliff0504/Documents/Settlement/DRS10.csv"
  val transactionDate = "2021-10-21"


  def processMaster():Unit = {

    implicit val getIntTranIdResult = GetResult(r => {
      IntTranId(r.nextInt())
    })


    val file = Paths.get(filePath)
    val f: Source[ByteString, Future[IOResult]] =  FileIO.fromPath(file)

    val r: Future[immutable.Seq[Map[String, String]]] = f.via(CsvParsing.lineScanner())
      .via(CsvToMap.toMap())                                       //: Map[String, ByteString] (5)
      .map(cleanseCsvData)                                         //: Map[String, String]     (6)
      .runWith(Sink.seq)

    r onComplete {
      case Success(x) => {


        val gr = x.groupBy(_.get("DRS-UNS").get).filter(_._1 != "N/A")

        Source(gr)
          .via(Slick.flow( r => {

            println(r._1)
            val curkey = currency.get(r._2(0).get("Import Customs Fee Currency").get)
            println(curkey.get)


            val q =
              sqlu"""
                  INSERT INTO import_duty_transaction (shipment_uns_id, transaction_date, currency_id, amount_total, pay_by_ssdc)
                  select s.id , '#${transactionDate}' , #${curkey.get} , 0 , true
                  from shipment as s  where s.name = '#${r._1}' and s.type ='Unified'  """

            println(q.statements)
            q

          }
          )).runWith(Sink.ignore)



      }
      case Failure(e) => println(e)
    }


    //  session.close()
    println("Done")

  }

  def processDetail():Unit = {



    implicit val getIntTranIdResult = GetResult(r => {
      IntTranId(r.nextInt())
    })


    val file = Paths.get(filePath)
    val f: Source[ByteString, Future[IOResult]] =  FileIO.fromPath(file)

    var count = 1



    val r = f.via(CsvParsing.lineScanner())
      .via(CsvToMap.toMap())                                       //: Map[String, ByteString] (5)
      .map(cleanseCsvData)                                         //: Map[String, String]     (6)
      .filter(_.get("DRS-UNS").get !="N/A")
      .runForeach(r => {

        println(r.get("DRS-UNS").get)

        Slick.source(sql""" select id , shipment_uns_id from import_duty_transaction
                     where shipment_uns_id = (select s.id from  shipment as s where s.name = '#${r.get("DRS-UNS").get}'
                     and s.type ='Unified')
            and transaction_date = '#${transactionDate}'""".as[(Int,Int)] )

          .runWith(
            Slick.sink(
              ids =>{
                // println(user2.id)
                println(count)
                count +=1

                //println(ids._1)
                //println(ids._2)
                val qty = (r.get("Qty").get).toInt
                //println(r.get("Qty").get)
                //println(r.get("PO# / IVS#").get)
                //println(r.get("SKU#").get)


                val amt = BigDecimal(r.get("Import Customs Fee").get.drop(1))
                println(amt)


                ( sqlu""" INSERT INTO import_duty_transaction_line_item
                  (transaction_id, shipment_uns_line_item_id, amount)
                  select ${ids._1} , s.id , ${amt}
                  from shipment_line_item as s where
                     s.shipment_id = ${ids._2} and
                                        s.source_shipment_id =
                                          (select id from shipment where name = '#${r.get("PO# / IVS#").get}' and type ='Supplier Inventory')
                                          and s.qty_order = ${qty}
                                          and sku_id = (select id from product_sku where code_by_drs = '#${r.get("SKU#").get}')
                  """ )

              }
            ))



      })


    //  session.close()
    println("Done processDetail")
  }

  def processMasterAmount():Unit = {



    implicit val getIntTranIdResult = GetResult(r => {
      IntTranId(r.nextInt())
    })


    val file = Paths.get(filePath)
    val f: Source[ByteString, Future[IOResult]] =  FileIO.fromPath(file)

    val r: Future[immutable.Seq[Map[String, String]]] = f.via(CsvParsing.lineScanner())
      .via(CsvToMap.toMap())                                       //: Map[String, ByteString] (5)
      .map(cleanseCsvData)                                         //: Map[String, String]     (6)
      .runWith(Sink.seq)

    r onComplete {
      case Success(x) => {

        val gr = x.groupBy(_.get("DRS-UNS").get).filter(_._1 != "N/A")

        Source(gr)
          .map( r => {
            println(r._1)
            Slick.source(sql""" select id from import_duty_transaction
                     where shipment_uns_id = (select s.id from shipment as s where s.name = '#${r._1}' and s.type ='Unified')
                      and transaction_date = '#${transactionDate}' """.as[Int])
              .runForeach(
                id =>{
                  println(id)
                  Slick.source(sql""" select sum(amount) from import_duty_transaction_line_item
                       where transaction_id = #${id} """.as[BigDecimal])
                } .via(Slick.flow( amt => {

                  println(amt)

                  (sqlu"""
                    update import_duty_transaction set amount_total = #${amt}
                    where id = #${id} """)
                })).runWith(Sink.ignore)
              )
          }
          ).runWith(Sink.ignore)

        println("Done processMasterAmount")
      }
      case Failure(e) => println(e)
    }


    //  session.close()

  }

















}
