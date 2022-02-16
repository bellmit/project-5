package com.kindminds.drs.data.pipelines.core.settlement

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import akka.Done
import akka.actor.ActorSystem
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult, Materializer}
import akka.util.ByteString
import com.typesafe.config.ConfigFactory

import scala.collection.immutable
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object DTran  {


  def main(args: Array[String]): Unit = {
   // importFee()
   // importFeeDetail()
    processMaster()

  }

  def cleanseCsvData(csvData: Map[String, ByteString]): Map[String, String] =
    csvData
      .filterNot { case (key, _) => key.isEmpty }
      .mapValues(_.utf8String)



  val dTranItemType = Map(
    ""->0,
    "撰稿服務" -> 18,
    "圖像準備服務" -> 19,
    "Export fee" -> 21 ,
    "Product related adjustments" -> 22,
    "退貨運費" -> 23,
    "退運運費" -> 23 ,
    "運費" ->  26 ,
    "Supplier related adjustments" -> 27 ,
    "廣告設置管理服務" -> 68 ,
    "市場分析服務" -> 82 ,

    "產品管理服務 - Standard" -> 83,
    "產品管理服務 - Premium" -> 84,
    "折扣設定" -> 85 ,
    "其他行銷活動" -> 86,
    "其他" -> 87,
    "管理月費" ->  99 ,
    "國內-管理月費" -> 99,
    "跨境與上架可行性評估服務" -> 93 ,
    "產品上架專案報價" -> 94

  )
  val filePath = "C:/Users/Ralph Lin/Documents/transaction import/DRS 應收帳款明細2020 (confidential) - 國內交易 (2).csv"
  //val filePath = "/Users/arthur/Documents/settlement/DRS 應收帳款明細2020 (confidential) - 國內交易 (17).csv"
  val transactionDate = "2021-10-07"

  val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

  def processMaster():Unit = {

    val referenceConfig = ConfigFactory.defaultReference
    println(referenceConfig.toString)
    //ConfigFactory.load()
      //.resolve()

    implicit val system = ActorSystem("drsDP")
    implicit val session = SlickSession.forConfig("slick-postgres")
    import session.profile.api._
    implicit val ec =  scala.concurrent.ExecutionContext.global
    implicit val materializer: Materializer = ActorMaterializer()


    val file = Paths.get(filePath)
    val f: Source[ByteString, Future[IOResult]] =  FileIO.fromPath(file)

    val r: Future[immutable.Seq[Map[String, String]]] = f.via(CsvParsing.lineScanner())
      .via(CsvToMap.toMap())                                       //: Map[String, ByteString] (5)
      .map(cleanseCsvData)                                         //: Map[String, String]     (6)
      .runWith(Sink.seq)

    r onComplete {
      case Success(x) => {

        val gr = x.groupBy( x=> (x.get("Supplier").get ) )

        gr.foreach(x => {

          if(x._1 != ""){
            println(x._1)

            val master =   sqlu""" INSERT INTO domestic_transaction (transaction_date,
            ssdc_company_id, splr_company_id, tax_rate, currency_id, amount_subtotal, amount_tax, amount_total)
            select  '#${transactionDate}' , 2, c.id , 0.05  , 1 , 0 , 0 , 0
            from company as c  where c.k_code ='#${x._1}' """

            Await.result(
              session.db.run(master).map { res =>
                println(res)
              }, timeout)


            val q = sql"""
              select id from domestic_transaction
              where ssdc_company_id = 2 and splr_company_id = (select id from
              company where k_code = '#${x._1}') and transaction_date = '#${transactionDate}'""".as[Int]

            println(q.statements)

            var id : Vector[Int] = null
            Await.result(
              session.db.run(q).map { res =>
                // res is a Vector[String]
                id = res
              }, timeout)


            //var total = 0
            var seq = 0
            x._2.foreach(y=>{

              seq +=1
              println(y.get( "Item").getOrElse(""))
              val typeId =  dTranItemType.get(y.get( "Item").getOrElse("")).get
              val amount =    BigDecimal(y.get("original amount (untaxed)").get)
              //total+= amount
              val note =    y.get("Note").get


              val lineitem =   sqlu""" INSERT INTO domestic_transaction_line_item
                      (transaction_id, line_seq, type_id, note , amount) VALUES( #${id(0)},#${seq} ,
                      #${typeId},  '#${note}' , #${amount}  )"""

              println(lineitem.statements)
              Await.result(
                session.db.run(lineitem).map { res =>
                  println(res)
                }, timeout)


              /*
              val updateMaster =   sqlu""" update domestic_transaction set amount_subtotal = #${amount}
                      where id = #${id(0)} """

              println(updateMaster.statements)
              Await.result(
                session.db.run(updateMaster).map { res =>
                  println(res)
                }, timeout)
                */

            })

            val getSubtotal =
              sql"""select sum(amount)
                from domestic_transaction_line_item
                where transaction_id =#${id(0)}
                 """.as[Int]

            println(getSubtotal.statements)
            var subtotal : Vector[Int] = null
            Await.result(
              session.db.run(getSubtotal).map { res =>
                // res is a Vector[String]
                subtotal = res
              }, timeout)

            val calculateTotal =
              sqlu"""update domestic_transaction
                  set amount_subtotal = #${subtotal(0)} ,
                  amount_tax = Round(#${subtotal(0)} * tax_rate),
                  amount_total =  Round(#${subtotal(0)} * (1+ tax_rate))
                  where id = #${id(0)}
                  """

            println(calculateTotal.statements)
            Await.result(
              session.db.run(calculateTotal).map { res =>
                println(res)
              }, timeout)


          }


          //

        })


        session.close()


      }
      case Failure(e) => println(e)
    }


    //  session.close()

  }





}
