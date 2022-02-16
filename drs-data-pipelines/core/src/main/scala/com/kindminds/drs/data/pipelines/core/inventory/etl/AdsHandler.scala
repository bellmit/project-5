package com.kindminds.drs.data.pipelines.core.inventory.etl

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.stream.alpakka.csv.scaladsl.CsvParsing.{Backslash, Comma, DoubleQuote}
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult, Materializer}
import akka.util.ByteString
import com.kindminds.drs.data.pipelines.api.message.{ImportAllOrderRpt, RegisterCommandHandler}

import java.nio.file.{Path, Paths}
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import scala.collection.immutable
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}


class AdsHandler(drsCmdBus:ActorRef) extends Actor with ActorLogging {
  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[ImportAllOrderRpt].getName ,self)

  implicit val session = SlickSession.forConfig("slick-postgres")
  import session.profile.api._
  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  override def receive = {
    case ImportAllOrderRpt(path) =>
      process(path)

  }

  def cleanseCsvData(csvData: Map[String, ByteString]): Map[String, String] =
    csvData
      .filterNot { case (key, _) => key.isEmpty }
      .mapValues(_.utf8String)
  val filePath = "C:/allOrder/8.txt"
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
//        val gr =x.groupBy(x => (x.get("snapshot-date").get ,x.get("transaction-type").get ,x.get("fnsku").get,x.get("fulfillment-center-id").get))


        val gr =x.groupBy(x => (x.get("Campaigns").get ,x.get("Type").get, x.get("Targeting").get,x.get("Start date").get))
//        print(x.groupBy(x => (x.get("amazon-order-id").get)))


        gr.foreach(x =>{

          val grCount=x._2.size
          val existId = queryExistId(x._1._1 , x._1._2 , x._1._3 , x._1._4)
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

    def queryExistId(aoid:String,pdate:String,sc:String,sku:String):Array[Int]={

      var tempIdList = new Array[Int](99)

      var idCount =0;

      val query =   sql"""
                select id
                FROM k648.all_orders
                where amazon_order_id='#${aoid}'
                and purchase_date='#${pdate}'
                and sales_channel='#${sc}'
                and sku='#${sku}'
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
                   INSERT INTO k648.all_orders( amazon_order_id,
    merchant_order_id, purchase_date, last_updated_date, order_status, fulfillment_channel, sales_channel, order_channel, url, ship_service_level, product_name, sku, asin, item_status, quantity, currency, item_price, item_tax, shipping_price, shipping_tax, gift_wrap_price, gift_wrap_tax, item_promotion_discount, ship_promotion_discount, ship_city, ship_state, ship_postal_code, ship_country, promotion_ids, is_business_order, purchase_order_number, price_designation)
    VALUES('#${y.get("amazon-order-id").get}', '#${y.get("merchant-order-id").get}', '#${y.get("purchase-date").get}', '#${y.get("last-updated-date").get}', '#${y.get("order-status").get}', '#${y.get("fulfillment-channel").get}', '#${y.get("sales-channel").get}', '#${y.get("order-channel").get}', '#${y.get("url").get}', '#${y.get("ship-service-level").get}', '#${y.get("product-name").get.replace("'","''")}',
      '#${y.get("sku").get}', '#${y.get("asin").get}', '#${y.get("item-status").get}', '#${y.get("quantity").get}', '#${y.get("currency").get}',
      '#${nullOrExist(y.get("item-price").get)}', '#${nullOrExist(y.get("item-tax").get)}', '#${nullOrExist(y.get("shipping-price").get)}', '#${nullOrExist(y.get("shipping-tax").get)}', '#${nullOrExist(y.get("gift-wrap-price").get)}', '#${nullOrExist(y.get("gift-wrap-tax").get)}', '#${nullOrExist(y.get("item-promotion-discount").get)}', '#${nullOrExist(y.get("ship-promotion-discount").get)}', '#${y.get("ship-city").get}', '#${y.get("ship-state").get}', '#${y.get("ship-postal-code").get}', '#${y.get("ship-country").get}',
      '#${y.get("promotion-ids").get}', '#${y.get("is-business-order").get}', '#${y.get("purchase-order-number").get}', '#${y.get("price-designation").getOrElse("")}'); """


//      println(y)

      println(master.statements)
      Await.result(
        session.db.run(master).map { res =>
          //println(res)
        }, timeout)
    }

    def nullOrExist(t:String)={
      if(!t.equals("")){
        t
      }else{
        "0"
      }
    }

    def update(y:Map[String,String],id:Int )={
      val master =   sqlu"""
             update k648.all_orders
	    SET merchant_order_id='#${y.get("merchant-order-id").get}', last_updated_date='#${y.get("last-updated-date").get}', order_status='#${y.get("order-status").get}', fulfillment_channel='#${y.get("fulfillment-channel").get}', order_channel='#${y.get("order-channel").get}', url='#${y.get("url").get}', ship_service_level='#${y.get("ship-service-level").get}', product_name='#${y.get("product-name").get.replace("'","''")}',
      asin='#${y.get("asin").get}', item_status='#${y.get("item-status").get}', quantity='#${y.get("quantity").get}', currency='#${y.get("currency").get}',
      item_price='#${nullOrExist(y.get("item-price").get)}', item_tax='#${nullOrExist(y.get("item-tax").get)}', shipping_price='#${nullOrExist(y.get("shipping-price").get)}', shipping_tax='#${nullOrExist(y.get("shipping-tax").get)}', gift_wrap_price='#${nullOrExist(y.get("gift-wrap-price").get)}', gift_wrap_tax='#${nullOrExist(y.get("gift-wrap-tax").get)}', item_promotion_discount='#${nullOrExist(y.get("item-promotion-discount").get)}', ship_promotion_discount='#${nullOrExist(y.get("ship-promotion-discount").get)}', ship_city='#${y.get("ship-city").get}', ship_state='#${y.get("ship-state").get}', ship_postal_code='#${y.get("ship-postal-code").get}', ship_country='#${y.get("ship-country").get}',
      promotion_ids='#${y.get("promotion-ids").get}',
      is_business_order='#${y.get("is-business-order").get}',
      purchase_order_number='#${y.get("purchase-order-number").get}',
      price_designation='#${y.get("price-designation").getOrElse("")}'
	    where id ='#${id}' ;"""


      println(master.statements)
      Await.result(
        session.db.run(master).map { res =>
          //println(res)
        }, timeout)
    }

    def delete(id:Int)={
      val del =sql"""
          delete from k648.all_orders where id='#${id}'
           """.as[String]

      //println(del.statements)
      Await.result(
        session.db.run(del).map { res =>
          //println(res)
        }, timeout)
    }



  }


}
