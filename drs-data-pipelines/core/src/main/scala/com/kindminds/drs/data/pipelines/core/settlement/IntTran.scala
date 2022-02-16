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
import com.kindminds.drs.data.pipelines.core.settlement.CustomDuty.{IntTranId, cleanseCsvData, currency}
import com.kindminds.drs.data.pipelines.core.settlement.IntTran.currency
import slick.jdbc.GetResult

import scala.collection.immutable
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object IntTran  {




  def main(args: Array[String]): Unit = {

    processMaster()

  }

  def cleanseCsvData(csvData: Map[String, ByteString]): Map[String, String] =
    csvData
      .filterNot { case (key, _) => key.isEmpty }
      .mapValues(_.utf8String)

 // case class IntTran(id:Int , kcode:String , , ,, )
  case class IntTran(id:Int , kcode:String  , note:String ,  item:Int , msdc:String , amount:BigDecimal,direction:Int)
  case class IntTranId(id:Int)


  val intTranItemType = Map(
    ""->0,
    "國際-INVENTORY_PLACEMENT_PROGRAM_COST" -> 3,
    "國際-MARKET_SIDE_ADVERTISING_COST" -> 5,
    "國際-MARKET_SIDE_MARKETING_ACTIVITY" -> 76 ,
    "國際-FBA_REMOVAL_FEE" -> 80,
    "國際-FBA_LONG_TERM_STORAGE" -> 81,
    "國際-OTHER_REFUND_AND_ALLOWANCE_FROM_MARKET_SIDE_TO_CUSTOMER" ->  89 ,
    "國際-SHIPPING" -> 91 ,
    "國際-BALANCE_ADJUSTMENT_SUBTRACTION" -> 92 ,
    "國際-FBA_MONTHLY_STORAGE" -> 95,
    "國際-PARTIAL_REFUND" -> 101
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

  val currency = Map(
    "K3 Beanworthy" -> 2,
    "K4 BW-UK" -> 3,
    "K5 BW-CA" -> 4 ,
    "K6 BW DE" -> 5,
    "K7 BW FR" -> 5,
    "K8 BW IT" -> 5 ,
    "K9 BW ES" -> 5
  )



  val dirKey = Map("Supplier to MSDC" -> 0,
    "MSDC to Supplier" -> 1
  )


//  val filePath = "C:/Users/Ralph Lin/Documents/transaction import/DRS 應收帳款明細2020 (confidential) - Sheet22.csv"
  val filePath = "C:/Users/cliff0504/Documents/Settlement/DRSInt.csv"
  val transactionDate = "2022-01-14"
  val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

  implicit val getIntTranIdResult = GetResult(r => {
    IntTranId(r.nextInt())
  })

  def processMaster():Unit = {


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

        val gr = x.groupBy( x=> (x.get("Supplier").get , x.get("MSDC").get) )
        println("######"+ gr.size)

        gr.foreach(x => {

          if(x._1._1 != ""){
            println(x._1)

            val msdc = msdcKcode.get(x._1._2).get
            val curr = currency.get(x._1._2).get
            val direction = dirKey.get(x._2(0).get("Cash flow direction").get).get

          val master =   sqlu""" INSERT INTO international_transaction (msdc_company_id,
         ssdc_company_id, splr_company_id, transaction_date, currency_id, total, note, orig_transaction_id,
          cash_flow_direction_key) select (select c2.id from company as c2 where c2.k_code = '#${msdc}') as msdcCid ,
           2, c.id , '#${transactionDate}' ,  #${curr}  ,
            0 , '' , 0  , #${direction} from company as c  where c.k_code ='#${x._1._1}' """



            Await.result(
              session.db.run(master).map { res =>
                println(res)
              }, timeout)


            val q = sql"""
              select id from international_transaction
              where msdc_company_id = (select id from company where k_code ='#${msdc}')
              and ssdc_company_id = 2 and splr_company_id = (select id from
              company where k_code = '#${x._1._1}') and transaction_date = '#${transactionDate}'""".as[Int]

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

              if(id.size > 0){
                val typeId =  intTranItemType.get(y.get("Item name").getOrElse("")).get
                val amount =    BigDecimal(y.get("Amount").get)
                //total+= amount
                val note =    y.get("Note").get

                val lineitem =   sqlu""" INSERT INTO international_transaction_line_item
                      (transaction_id, line_seq, type_id, subtotal, note) VALUES( #${id(0)},#${seq} ,
                      #${typeId}, #${amount} , '#${note}' )"""

                println(lineitem.statements)
                Await.result(
                  session.db.run(lineitem).map { res =>
                    println(res)
                  }, timeout)
              }


            })

            val  totalSql =sqlu""" update international_transaction
                             set total = (select sum(subtotal)
                from international_transaction_line_item
                where transaction_id =#${id(0)})
                             where id =  #${id(0)}"""

            println(totalSql.statements)
            Await.result(
              session.db.run(totalSql).map { res =>
                println(res)
              }, timeout)

            /*
            val updateMaster =   sqlu""" update international_transaction set total = #${total}
                      where id = #${id(0)} """

            println(updateMaster.statements)
            Await.result(
              session.db.run(updateMaster).map { res =>
                println(res)
              }, timeout)

*/
          }


          //

        })

        println("done")
        session.close()


      }
      case Failure(e) => println(e)
    }


    //  session.close()

  }













}
