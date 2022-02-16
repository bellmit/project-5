package com.kindminds.drs.data.pipelines.core.inventory.uns

import java.time.{OffsetDateTime, ZoneOffset}
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.slick.javadsl.SlickSession
import com.kindminds.drs.data.pipelines.core.dto.{Order, RetainmentRate}
import com.kindminds.drs.data.pipelines.core.settlement.IntTran.timeout
import slick.jdbc.GetResult

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Await, Future}

case class ShipmentQty(uns_id:Int,
                       uns_name : String,
                       ivs_id : Int,
                       ivs_name: String,
                       sku_id: Int,
                       code_by_drs:String,
                       index: String,
                       qty_invoice: Int,
                       qty_sold: Int,
                       qty_returned: Int,
                       qty_current_inventory: Int,
                       k_code: String,
                      uns_line_item_id:Int)

case class AmazonDailyInventory(
                                 snapshot_date : OffsetDateTime,
                                 fnsku :String,
                                 sku : String,
                                 product_name : String,
                                var quantity: Int,
                                 fulfillment_center_id : String,
                                 detailed_disposition : String,
                                 country : String,
                                var uns_line_item_id:Int
                               )




object ShipmentStorageLocationEtl {

  implicit val system = ActorSystem("drsDP")
  implicit val session = SlickSession.forConfig("slick-postgres")
  import session.profile.api._
  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()
  val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

  implicit val getShipementQtyResult = GetResult(r => {

    val uns_id = r.nextInt
    val uns_name : String = r.nextString()
    val ivs_id : Int = r.nextInt()
    val ivs_name: String = r.nextString()
    val sku_id: Int = r.nextInt()
    val code_by_drs:String = r.nextString()
    val index: String = r.nextString()
    val qty_invoice: Int = r.nextInt()
    val qty_sold: Int = r.nextInt()
    val qty_returned: Int = r.nextInt()
    val qty_current_inventory: Int = r.nextInt()
    val k_code: String = r.nextString()
    val uns_line_item_id: Int = r.nextInt()

    ShipmentQty(uns_id,
      uns_name, ivs_id , ivs_name, sku_id, code_by_drs, index, qty_invoice,
      qty_sold,qty_returned,qty_current_inventory,k_code,uns_line_item_id
    )
    //r.nextString, r.nextString)
  })

  implicit val getAmazonDailyInventoryResult = GetResult(r => {

    val sd = r.nextTimestamp()
    val  snapshot_date : OffsetDateTime = OffsetDateTime.ofInstant(sd.toInstant , ZoneOffset.UTC)
    val fnsku :String = r.nextString()
    val sku : String = r.nextString()
    val product_name : String = r.nextString()
    val quantity: Int = r.nextInt()
    val fulfillment_center_id : String = r.nextString()
    val detailed_disposition : String = r.nextString()
    val country : String = r.nextString()


    AmazonDailyInventory(snapshot_date,fnsku,sku,
      product_name , quantity , fulfillment_center_id ,
      detailed_disposition , country,0

    )
    //r.nextString, r.nextString)
  })

  def main(args: Array[String]): Unit = {
    //transform()
   verifyUnknown()
  }


  def transform(): Unit = {


    val kcode = "K598"

    val qProd = sql""" select distinct(code_by_drs) from pv.shipment_qty3
                  where k_code ='#${kcode}' and qty_current_inventory > 0
                     order by  code_by_drs   """.as[String]

    var skuDS : Vector[String] = null

    Await.result(
      session.db.run(qProd).map { res =>
        skuDS = res
      }, timeout)

    val q = sql""" select * from pv.shipment_qty3
                  where k_code ='#${kcode}' and qty_current_inventory > 0
                     order by  code_by_drs , ivs_id  """.as[ShipmentQty]

    var shipmentQtyDS : Vector[ShipmentQty] = null

    Await.result(
      session.db.run(q).map { res =>
        println(res.size)
        shipmentQtyDS = res
      }, timeout)

    var resultMap : scala.collection.mutable.Map[String,
      mutable.ArrayBuffer[AmazonDailyInventory] ] = new mutable.HashMap[String, mutable.ArrayBuffer[AmazonDailyInventory] ]

    skuDS.foreach(sku => {

      val q2 = sql""" select * from inventory.amazon_daily_inventory_history adih
                    where date(snapshot_date)  = '2020-12-05'
                     and  sku ='#${sku}' order by fulfillment_center_id
                     """.as[AmazonDailyInventory]

      var amazonDailyInventoryDS : Vector[AmazonDailyInventory] = null

      Await.result(
        session.db.run(q2).map { res =>
          println(res.size)
          amazonDailyInventoryDS = res
        }, timeout)

      val buf: mutable.ArrayBuffer[AmazonDailyInventory] = collection.mutable.ArrayBuffer(amazonDailyInventoryDS.toArray : _*)

      val ivsProd: Seq[ShipmentQty] = shipmentQtyDS.filter(s => (s.code_by_drs == sku))

      var ivsList : scala.collection.mutable.ListBuffer[(Int,String,Int)]= new mutable.ListBuffer[(Int,String,Int)]


      //todo arthur have to process unfuilable
      ivsProd.foreach(i =>{

        var subTotal = 0
        buf.foreach(b=>{

          subTotal+= b.quantity
          if(i.qty_current_inventory >= subTotal){
            //ivsList.append((i.uns_line_item_id, b.fulfillment_center_id, b.quantity))
            if(b.uns_line_item_id == 0)b.uns_line_item_id = i.uns_line_item_id
          }else{

            println("offsetoffsetoffsetoffsetoffset")
            val notAddedQty = subTotal - b.quantity

            if(i.qty_current_inventory - notAddedQty > 0){
              //separte
              buf.insert(
                buf.lastIndexWhere(_.fulfillment_center_id == b.fulfillment_center_id) + 1,
                AmazonDailyInventory(b.snapshot_date,b.fnsku,b.sku,b.product_name,
                  (b.quantity - (i.qty_current_inventory -notAddedQty))
                  ,b.fulfillment_center_id,b.detailed_disposition,b.country,0))

              println(i.uns_line_item_id)
              println(b.fulfillment_center_id)
              println(notAddedQty)
              println(b.quantity - (i.qty_current_inventory -notAddedQty))

              b.quantity = i.qty_current_inventory - notAddedQty
             if(b.uns_line_item_id ==0) b.uns_line_item_id = i.uns_line_item_id
              println(b.quantity)
              println("offsetoffsetoffsetoffsetoffset")
             // ivsList.append((i.uns_line_item_id, b.fulfillment_center_id, b.quantity))

            }
          }

        })

      })

//      buf.foreach(x=>{
//        ivsList.append((x.uns_line_item_id, x.fulfillment_center_id, x.quantity))
//      })

      resultMap.put(sku , buf)

    })


    shipmentQtyDS


    println(resultMap.size)

    resultMap.foreach(kv=>{
      kv._2.foreach(a =>{
        val lineitem =   sqlu""" INSERT INTO public.shipment_storage_location
                  (uns_line_item_id, warehouse, stored_qty, create_date, update_date)
                     VALUES(#${a.uns_line_item_id}, '#${a.fulfillment_center_id}', #${a.quantity}, null, null);  """

        println(lineitem.statements)


        Await.result(
          session.db.run(lineitem).map { res =>
            println(res)
          }, timeout)

      })
    })


  }

  implicit val getTableResult = GetResult(r => (r.nextInt, r.nextInt))

  def verifyUnknown(): Unit ={


    val kcode = "K598"

    val q = sql""" select uns_line_item_id , sum(stored_qty) from shipment_storage_location ssl
                group by uns_line_item_id   """.as[(Int,Int)]

    var ds : Vector[(Int,Int)] = null

    Await.result(
      session.db.run(q).map { res =>
        println(res.size)
        ds = res
      }, timeout)


    ds.foreach( d=>{

      val q2 = sql""" select * from pv.shipment_qty3
                  where uns_line_item_id ='#${d._1}'  """.as[ShipmentQty]

      var shipmentQtyDS : Vector[ShipmentQty] = null
      Await.result(
        session.db.run(q2).map { res =>
          shipmentQtyDS = res
        }, timeout)


      if(shipmentQtyDS.size > 0){

        if(shipmentQtyDS(0).qty_current_inventory != d._2){
          if(shipmentQtyDS(0).qty_current_inventory  > d._2){
            val offset = shipmentQtyDS(0).qty_current_inventory - d._2

            val lineitem =   sqlu""" INSERT INTO public.shipment_storage_location
                  (uns_line_item_id, warehouse, stored_qty, create_date, update_date)
                     VALUES(#${d._1}, 'unknown', #${offset}, null, null);  """

            println(lineitem.statements)

            Await.result(
              session.db.run(lineitem).map { res =>
                println(res)
              }, timeout)

          }else{

            println("GreaterGreaterGreaterGreaterGreaterGreater")

          }
        }

      }

    })

  }





}
