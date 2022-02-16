package com.kindminds.drs.data.pipelines.core.inventory.uns

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import com.kindminds.drs.data.pipelines.api.message.{CompleteInitKcodeSkuSerialNo, CreateDailySales, DoSkuSerialNoException, DoSkuSerialNoOther, DoSkuSerialNoRefund, DoSkuSerialNoSellBack, RegisterCommandHandler, TransformSkuSerialNo, UpdateSkuSerialNo}
import slick.jdbc.GetResult

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt



case class ShipmentIVSLineitem( uns_shipment_id : Int,
                                ivs_shipment_id:Int,
                                ivs_shipment_line_item_id : Int,
                                ivs_name: String,
                                line_seq: Int,
                                sku_id:Int,
                                code_by_drs: String,
                                box_num: Int,
                                mixed_box_line_seq: Int,
                                carton_number_start: Int,
                                carton_number_end: Int , line_qty : Int , destination_country:String)

object InitShipmentSkuSerialNo {

  def props(drsCmdBus: ActorRef) = Props(new InitShipmentSkuSerialNo(drsCmdBus))

}

class InitShipmentSkuSerialNo (drsCmdBus: ActorRef) extends Actor with ActorLogging {


  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[TransformSkuSerialNo].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[UpdateSkuSerialNo].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[DoSkuSerialNoRefund].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[DoSkuSerialNoException].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[DoSkuSerialNoSellBack].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[DoSkuSerialNoOther].getName ,self)


  implicit val session = SlickSession.forConfig("slick-postgres")
  import session.profile.api._
  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()


  implicit val getShipmentIVSLineitemResult = GetResult(r => {

    val  uns_shipment_id  = r.nextInt
    val ivs_shipment_id = r.nextInt
    val ivs_shipment_line_item_id : Int = r.nextInt()
    val ivs_name: String = r.nextString()
    val line_seq : Int = r.nextInt()
    val sku_id: Int = r.nextInt()
    val code_by_drs = r.nextString()
    val box_num = r.nextInt()
    val mixed_box_line_seq: Int = r.nextInt()
    val carton_number_start: Int = r.nextInt()
    val carton_number_end: Int = r.nextInt()
    val line_qty: Int = r.nextInt()
    val destination_country: String = r.nextString()

    ShipmentIVSLineitem(uns_shipment_id ,ivs_shipment_id,
      ivs_shipment_line_item_id, ivs_name , line_seq, sku_id, code_by_drs, box_num, mixed_box_line_seq,
      carton_number_start,carton_number_end,line_qty,destination_country
    )
    //r.nextString, r.nextString)
  })


  override def receive: Receive = {

    case t : TransformSkuSerialNo =>
      transform(t.kcode)
      self ! UpdateSkuSerialNo(t.kcode)

    case u : UpdateSkuSerialNo =>
      update(u.kcode)
      self ! DoSkuSerialNoRefund(u.kcode)

    case r : DoSkuSerialNoRefund =>
      handleRefund(r.kcode)
      self ! DoSkuSerialNoException(r.kcode)

    case ex : DoSkuSerialNoException =>
      handleException(ex.kcode)
      self ! DoSkuSerialNoSellBack(ex.kcode)

    case sb : DoSkuSerialNoSellBack =>
      handleFBAReturnToSupplier(sb.kcode)
      self ! DoSkuSerialNoOther(sb.kcode)

    case o : DoSkuSerialNoOther =>
      handleOtherTran(o.kcode)
      drsCmdBus ! CompleteInitKcodeSkuSerialNo()

  }

  def transform(kcode:String): Unit = {
    val timeout = scala.concurrent.duration.FiniteDuration(30, TimeUnit.SECONDS)

    val q = sql"""
                 select uns_shipment_id , ivs_shipment_id , ivs_shipment_line_item_id , ivs_name  ,
                  line_seq  , sku_id , code_by_drs  , box_num , mixed_box_line_seq  ,
                 carton_number_start , carton_number_end , line_qty , destination_country from (
                  SELECT t_ivs.id AS ivs_shipment_id,
                     t_ivs.name AS ivs_name,
                     ivs_sli.id as ivs_shipment_line_item_id ,
                     ivs_sli.line_seq,
                     ivs_sli.sku_id,
                     psv.code_by_drs,
                     psv.code_by_supplier,
                     ivs_sli.gross_weight_per_ctn_kg,
                     ivs_sli.ctn_dim_1_cm,
                     ivs_sli.ctn_dim_2_cm,
                     ivs_sli.ctn_dim_3_cm,
                     ivs_sli.box_num,
                     ivs_sli.mixed_box_line_seq,
                     ivs_sli.require_packaging,
                     ivs_sli.gui_invoice_number,
                     ivs_sli.gui_file_name,
                     ivs_sli.units_per_ctn,
                     ivs_sli.numbers_of_ctn,
                     ivs_sli.unit_amount,
                     ivs_sli.qty_order,
                     ivs_sli.qty_sold,
                     ivs_sli.qty_returned,
                     ivs_sli.units_per_ctn *
                         CASE
                             WHEN ivs_sli.numbers_of_ctn = 0 THEN 1
                             ELSE ivs_sli.numbers_of_ctn
                         END AS line_qty,
                     t_uns.uns_shipment_id,
                     t_uns.uns_name,
                     t_ivs.status AS ivs_status,
                     c_s.k_code,
                     c_s.short_name_local AS seller_name,
                     ct.code AS destination_country,
                     sfl.name AS shipping_location,
                     sm.name AS shipping_method,
                     sii.pick_up_requester_id,
                     sii.confirmor_id,
                     sii.internal_note,
                     t_ivs.invoice_number,
                     t_ivs.amount_tax,
                     t_ivs.sales_tax_rate,
                     t_ivs.amount_total,
                     sii.special_request,
                     sii.num_of_repackaging,
                     sii.repackaging_fee,
                     sii.required_po,
                     sii.po_number,
                     t_ivs.date_created,
                     t_ivs.date_confirmed,
                     sii.date_purchased,
                     sii.fca_delivery_date AS delivery_date,
                     sii.expected_export_date ,
                     ivs_sli.carton_number_start ,
                     ivs_sli.carton_number_end
                    FROM shipment t_ivs
                      JOIN company c_s ON t_ivs.seller_company_id = c_s.id
                      JOIN shipment_line_item ivs_sli ON t_ivs.id = ivs_sli.shipment_id AND ivs_sli.source_shipment_id IS NULL
                      JOIN product_sku_view psv ON ivs_sli.sku_id = psv.id
                      LEFT JOIN ( SELECT DISTINCT uns_sli.source_shipment_id,
                             uns_s.id AS uns_shipment_id,
                             uns_s.name AS uns_name,
                             uns_sli.sku_id
                            FROM shipment_line_item uns_sli
                              JOIN shipment uns_s ON uns_sli.shipment_id = uns_s.id AND uns_s.type::text = 'Unified'::text) t_uns ON t_ivs.id = t_uns.source_shipment_id AND ivs_sli.sku_id = t_uns.sku_id
                      JOIN country ct ON t_ivs.destination_country_id = ct.id
                      JOIN shipping_method sm ON t_ivs.shipping_method_id = sm.id
                      LEFT JOIN shipment_info_ivs sii ON t_ivs.id = sii.shipment_id
                      LEFT JOIN shipment_fca_location sfl ON sii.shipment_fca_location_id = sfl.id
                   WHERE t_ivs.type::text = 'Supplier Inventory'::text and k_code = '#${kcode}'
                   ORDER BY t_ivs.id, ivs_sli.id)
                   as b
                    """.as[ShipmentIVSLineitem]

    var ivsLineitemsDS : Vector[ShipmentIVSLineitem] = null

    Await.result(
      session.db.run(q).map { res =>
        println(res.size)
        ivsLineitemsDS = res
      }, timeout)



    ivsLineitemsDS.foreach( x =>{

      var sn = 1;
      while (sn <= x.line_qty ){

        val ivs_sku_serial_no = x.ivs_name  +"_" + x.code_by_drs + "_" + x.box_num +"-" + x.mixed_box_line_seq +"_"+ sn

        println(ivs_sku_serial_no)

        val lineitem =   sqlu""" INSERT INTO public.shipment_sku_identification
        (uns_shipment_id, ivs_shipment_id, ivs_shipment_line_item_id, seq, sku_id, destination_country, ivs_sku_serial_no,
            drs_transaction_id, status)
                  VALUES(#${x.uns_shipment_id} ,#${x.ivs_shipment_id} ,
                  #${x.ivs_shipment_line_item_id}, #${sn} ,#${x.sku_id} ,  '#${x.destination_country}'
                  ,'#${ivs_sku_serial_no}', null, '');   """

        println(lineitem.statements)

        Await.result(
          session.db.run(lineitem).map { res =>
            println(res)
          }, timeout)

        sn = sn+1
      }


    })

  }

  def update(kcode:String):Unit ={
    val timeout = scala.concurrent.duration.FiniteDuration(30, TimeUnit.SECONDS)

    val q = sql""" select dt.id , dt.source_transaction_id , dt.quantity  ,
                s2.id as ivs_id  , dt.product_sku_id ,
                 s2.destination_country_id , dt.shipment_ivs_name  from drs_transaction dt
              inner join shipment s2 on dt.shipment_ivs_name  = s2."name"
              inner  JOIN company c_s ON s2.seller_company_id = c_s.id
                where dt.type = 'Order' and s2.type = 'Supplier Inventory' and c_s.k_code = '#${kcode}'
            order by dt.id    """.as[(Int,String , Int , Int ,Int , Int , String)]

    var ds : Vector[(Int,String , Int , Int , Int , Int , String)] = null

    Await.result(
      session.db.run(q).map { res =>
        println(res.size)
        ds = res
      }, timeout)

    ds.foreach(d=>{


      val qLineitem = sql"""  select id from shipment_line_item sli where shipment_id = #${d._4}
                              and sku_id = #${d._5}  order by id   """.as[(Int)]

      var lineItemIds : Vector[(Int)] = null

      Await.result(
        session.db.run(qLineitem).map { res =>
          println(res.size)
          lineItemIds = res
        }, timeout)


      if(lineItemIds.size > 0){


        val q2 = sql"""select id , ivs_shipment_line_item_id from shipment_sku_identification ssi
                  where ivs_shipment_line_item_id in (#${lineItemIds.mkString(",")})  and status = ''
                   order by id   """.as[(Int,Int)]

        var skuDs : Vector[(Int,Int)] = null

        Await.result(
          session.db.run(q2).map { res =>
            println(res.size)
            skuDs = res
          }, timeout)

        if(skuDs.size > 0){
          val qty = d._3
          var count = 0
          while(count < qty){

            if(count <= (skuDs.size -1) ){
              val shipmentSkuIdentificationId = skuDs(count)._1


              val lineitem =   sqlu""" UPDATE public.shipment_sku_identification
                               SET drs_transaction_id='#${d._1}', status='Sold'
                               where id = #${shipmentSkuIdentificationId}   """

              println(lineitem.statements)

              Await.result(
                session.db.run(lineitem).map { res =>
                  println(res)
                }, timeout)

            }else{

              val lineitem =   sqlu""" INSERT INTO public.shipment_sku_identification_exception
                             (drs_transaction_id, source_transaction_id, "type", ivs_name, sku_id)
                            VALUES(  #${d._1}, '#${d._2}' , 'Order' ,'#${d._7}', #${d._5});   """

              println(lineitem.statements)

              Await.result(
                session.db.run(lineitem).map { res =>
                  println(res)
                }, timeout)


            }


            count +=1
          }
        }else{

          val lineitem =   sqlu""" INSERT INTO public.shipment_sku_identification_exception
                             ( drs_transaction_id, source_transaction_id, "type", ivs_name, sku_id)
                            VALUES(  #${d._1}, '#${d._2}' , 'Order' ,'#${d._7}', #${d._5});   """


          println(lineitem.statements)

          Await.result(
            session.db.run(lineitem).map { res =>
              println(res)
            }, timeout)

        }

      }else{
        val lineitem =   sqlu""" INSERT INTO public.shipment_sku_identification_exception
                             ( drs_transaction_id, source_transaction_id, "type", ivs_name, sku_id)
                            VALUES(  #${d._1}, '#${d._2}' , 'Order' ,'#${d._7}', #${d._5});   """


        println(lineitem.statements)

        Await.result(
          session.db.run(lineitem).map { res =>
            println(res)
          }, timeout)


      }


    })
  }

  def handleRefund(kcode:String): Unit ={
    val timeout = scala.concurrent.duration.FiniteDuration(30, TimeUnit.SECONDS)

    val q = sql""" select dt.id , dt.source_transaction_id , dt.quantity  ,
                s2.id as ivs_id  , dt.product_sku_id , s2.destination_country_id ,dt.shipment_ivs_name
                from drs_transaction dt
                 inner join drs_transaction_line_item_source dtlis on dt.id = dtlis.drs_transaction_id
              inner join shipment s2 on dt.shipment_ivs_name  = s2."name"
                inner  JOIN company c_s ON s2.seller_company_id = c_s.id
                where
                 (  dt.type = 'Refund' or  (dt.type ='other-transaction' and dtlis.pretax_principal_price <= 0 ))
                and s2.type = 'Supplier Inventory' and c_s.k_code = '#${kcode}'
            order by dt.id    """.as[(Int,String , Int , Int ,Int , Int, String)]

    var ds : Vector[(Int,String , Int , Int , Int , Int , String)] = null

    Await.result(
      session.db.run(q).map { res =>
        println(res.size)
        ds = res
      }, timeout)


    ds.foreach(r =>{

      val qLineitem = sql"""  select id from shipment_line_item sli where shipment_id = #${r._4}
                  and sku_id = #${r._5}  order by id   """.as[(Int)]

      var lineItemIds : Vector[(Int)] = null

      Await.result(
        session.db.run(qLineitem).map { res =>
          println(res.size)
          lineItemIds = res
        }, timeout)

      val q2 = sql"""select id , drs_transaction_id , status , remark from shipment_sku_identification ssi
                  where ivs_shipment_line_item_id in (#${lineItemIds.mkString(",")} )  and status = 'Sold'
                   order by id   """.as[(Int , String , String , String)]

      var skuDs : Vector[(Int , String , String , String)] = null

      Await.result(
        session.db.run(q2).map { res =>
          println(res.size)
          skuDs = res
        }, timeout)


      //val qty = r._3
      //var count = 0
      //while(count < qty){

      // var marketplace_order_id = if(r._2!= null && r._2 != "") r._2 else r._1

      println(r._5)

      if(skuDs.size > 0){

        var remark =
          if(skuDs(0)._4 != null && skuDs(0)._4 !="")
            skuDs(0)._4  + "/" + skuDs(0)._2 +"_"+ skuDs(0)._3
          else
            "/" + skuDs(0)._2 +"_"+ skuDs(0)._3

        // if(count <= (skuDs.size -1) ){
        val shipment_sku_identification_id = skuDs(0)._1

        val lineitem =   sqlu""" UPDATE public.shipment_sku_identification
                               SET drs_transaction_id='#${r._1}', status='Refund'
                                , remark='#${remark}'
                               where id = #${shipment_sku_identification_id}   """

        println(lineitem.statements)

        Await.result(
          session.db.run(lineitem).map { res =>
            println(res)
          }, timeout)

      } else{


        val lineitem =   sqlu""" INSERT INTO public.shipment_sku_identification_exception
                             ( drs_transaction_id, source_transaction_id, "type", ivs_name, sku_id)
                            VALUES( #${r._1}, '#${r._2}' , 'Refund' ,'#${r._7}', #${r._5});   """

        println(lineitem.statements)

         Await.result(
         session.db.run(lineitem).map { res =>
         println(res)
        }, timeout)


      }


      //count +=1
      // }


    })


  }


  //Sellback
  def handleFBAReturnToSupplier(kcode:String): Unit ={
    val timeout = scala.concurrent.duration.FiniteDuration(30, TimeUnit.SECONDS)

    val q = sql""" select dt.id , dt.source_transaction_id , dt.quantity  ,
                s2.id as ivs_id , dt.product_sku_id , s2.destination_country_id ,
                  dt.shipment_ivs_name from drs_transaction dt
              inner join shipment s2 on dt.shipment_ivs_name  = s2."name"
                inner  JOIN company c_s ON s2.seller_company_id = c_s.id
                where dt.type in ('FBA Return to Supplier', 'FBA Returns Taiwan', 'FBA Returns Other', 'FBA Returns Dispose')
                 and s2.type = 'Supplier Inventory' and c_s.k_code = '#${kcode}'
            order by dt.id    """.as[(Int,String , Int , Int ,Int , Int,String)]

    var ds : Vector[(Int,String , Int , Int , Int , Int, String)] = null

    Await.result(
      session.db.run(q).map { res =>
        println(res.size)
        ds = res
      }, timeout)

    ds.foreach(r =>{

      val qLineitem = sql"""  select id from shipment_line_item sli where shipment_id = #${r._4}
                    and sku_id = #${r._5}  order by id   """.as[(Int)]

      var lineItemIds : Vector[(Int)] = null

      Await.result(
        session.db.run(qLineitem).map { res =>
          println(res.size)
          lineItemIds = res
        }, timeout)

      if(lineItemIds.size > 0){
        val q2 = sql"""select id , drs_transaction_id , status , remark ,ivs_shipment_line_item_id from shipment_sku_identification ssi
                  where ivs_shipment_line_item_id in (#${lineItemIds.mkString(",")} )  and ( status = '' or status = 'Refund')
                   order by id   """.as[(Int , String , String , String , Int)]

        var skuDs : Vector[(Int , String , String , String , Int)] = null

        Await.result(
          session.db.run(q2).map { res =>
            println(res.size)
            skuDs = res
          }, timeout)

        if(skuDs.size > 0){

          val qty = r._3
          var count = 0
          println(r._1)
          println(r._5)
          while(count < qty){

            println(r._5)

            var remark =
              if(skuDs(count)._4 != null && skuDs(count)._4 !="")
                skuDs(count)._4  + "/" + skuDs(count)._2 +"_"+ skuDs(count)._3
              else
                "/" + skuDs(count)._2 +"_"+ skuDs(count)._3

            if(count <= (skuDs.size -1) ){
              val shipment_sku_identification_id = skuDs(count)._1

              val lineitem =   sqlu""" UPDATE public.shipment_sku_identification
                                   SET drs_transaction_id='#${r._1}', status='FbaReturnToSupplier'
                                    , remark='#${remark}'
                                   where id = #${shipment_sku_identification_id}   """

              println(lineitem.statements)

              Await.result(
                session.db.run(lineitem).map { res =>
                  println(res)
                }, timeout)

            }else{

              val lineitem =   sqlu""" INSERT INTO public.shipment_sku_identification_exception
                             ( drs_transaction_id, source_transaction_id, "type", ivs_name, sku_id)
                            VALUES( #${r._1}, '#${r._2}' , 'FbaReturnToSupplier' ,'#${r._7}', #${r._5});   """

              println(lineitem.statements)

              Await.result(
                session.db.run(lineitem).map { res =>
                  println(res)
                }, timeout)


            }


            count +=1

          }

        }else{


          val lineitem =   sqlu""" INSERT INTO public.shipment_sku_identification_exception
                             ( drs_transaction_id, source_transaction_id, "type", ivs_name, sku_id)
                            VALUES(  #${r._1}, '#${r._2}' , 'FbaReturnToSupplier' ,'#${r._7}', #${r._5});   """


          println(lineitem.statements)

          Await.result(
            session.db.run(lineitem).map { res =>
              println(res)
            }, timeout)


        }

      }else{

        val lineitem =   sqlu""" INSERT INTO public.shipment_sku_identification_exception
                             ( drs_transaction_id, source_transaction_id, "type", ivs_name, sku_id)
                            VALUES(  #${r._1}, '#${r._2}' , 'FbaReturnToSupplier' ,'#${r._7}', #${r._5});   """


        println(lineitem.statements)

        Await.result(
          session.db.run(lineitem).map { res =>
            println(res)
          }, timeout)


      }





    })


  }


  def handleException(kcode:String):Unit ={

    val timeout = scala.concurrent.duration.FiniteDuration(30, TimeUnit.SECONDS)
    val q = sql""" select dt.id , dt.source_transaction_id , dt.quantity  ,
                s2.id as ivs_id  , dt.product_sku_id , s2.destination_country_id ,
                   dt.shipment_ivs_name from drs_transaction dt
              inner join shipment s2 on dt.shipment_ivs_name  = s2."name"
              inner  JOIN company c_s ON s2.seller_company_id = c_s.id
                where dt.type = 'Order' and s2.type = 'Supplier Inventory' and c_s.k_code = '#${kcode}'
                and dt.id in (Select drs_transaction_id from shipment_sku_identification_exception )
            order by dt.id    """.as[(Int,String , Int  ,Int, Int , Int,String)]

    var ds : Vector[(Int,String , Int , Int , Int , Int,String)] = null

    Await.result(
      session.db.run(q).map { res =>
        println(res.size)
        ds = res
      }, timeout)

    ds.foreach(d=>{


      val qLineitem = sql"""  select id from shipment_line_item sli where shipment_id = #${d._4}
                   and sku_id = #${d._5}  order by id   """.as[(Int)]

      var lineItemIds : Vector[(Int)] = null

      Await.result(
        session.db.run(qLineitem).map { res =>
          println(res.size)
          lineItemIds = res
        }, timeout)

      if(lineItemIds.size > 0){

        val q2 = sql"""select id , drs_transaction_id , status , remark , ivs_shipment_line_item_id  from shipment_sku_identification ssi
                  where ivs_shipment_line_item_id in (#${lineItemIds.mkString(",")} )  and ( status = '' or status = 'Refund')
                   order by id   """.as[(Int , String , String , String ,Int)]

        var skuDs : Vector[(Int , String , String , String , Int)] = null

        Await.result(
          session.db.run(q2).map { res =>
            println(res.size)
            skuDs = res
          }, timeout)

        val qty = d._3
        var count = 0
        var canDelete = true
        while(count < qty){

          if(count <= (skuDs.size -1) ){

            val shipment_sku_identification_id = skuDs(count)._1

            var remark =
              if(skuDs(0)._4 != null && skuDs(0)._4 !="")
                skuDs(0)._4  + "/" + skuDs(0)._2 +"_"+ skuDs(0)._3
              else
                "/" + skuDs(0)._2 +"_"+ skuDs(0)._3

            val lineitem =   sqlu""" UPDATE public.shipment_sku_identification
                               SET drs_transaction_id='#${d._1}', status='Sold'
                                , remark='#${remark}'
                               where id = #${shipment_sku_identification_id}   """

            println(lineitem.statements)

            Await.result(
              session.db.run(lineitem).map { res =>
                println(res)
              }, timeout)


          }else{
            canDelete = false
          }

          count +=1
        }

        if(canDelete ){
          val dEexception =   sqlu""" Delete from public.shipment_sku_identification_exception
                               where drs_transaction_id = #${d._1}   """

          println(dEexception.statements)

          Await.result(
            session.db.run(dEexception).map { res =>
              println(res)
            }, timeout)
        }
      }



    })
  }



  def handleOtherTran(kcode:String): Unit ={

    val timeout = scala.concurrent.duration.FiniteDuration(30, TimeUnit.SECONDS)
    val q = sql""" select dt.id , dt.source_transaction_id , dt.quantity  ,
                s2.id as ivs_id  , dt.product_sku_id , s2.destination_country_id ,
                 dt.shipment_ivs_name  from drs_transaction dt
                   inner join drs_transaction_line_item_source dtlis on dt.id = dtlis.drs_transaction_id
              inner join shipment s2 on dt.shipment_ivs_name  = s2."name"
                inner  JOIN company c_s ON s2.seller_company_id = c_s.id
                where   (dt.type ='other-transaction' and dtlis.pretax_principal_price > 0 )
                 and s2.type = 'Supplier Inventory' and c_s.k_code = '#${kcode}'
            order by dt.id    """.as[(Int,String , Int  ,Int, Int , Int , String)]

    var ds : Vector[(Int,String , Int , Int , Int , Int , String)] = null

    Await.result(
      session.db.run(q).map { res =>
        println(res.size)
        ds = res
      }, timeout)

    ds.foreach(r =>{

      val qLineitem = sql"""  select id from shipment_line_item sli where shipment_id = #${r._4}
                   and sku_id = #${r._5}  order by id   """.as[(Int)]

      var lineItemIds : Vector[(Int)] = null

      Await.result(
        session.db.run(qLineitem).map { res =>
          println(res.size)
          lineItemIds = res
        }, timeout)

      if(lineItemIds.size > 0){

        val q2 = sql"""select id , drs_transaction_id , status , remark , ivs_shipment_line_item_id from shipment_sku_identification ssi
                  where ivs_shipment_line_item_id in (#${lineItemIds.mkString(",")} )   and ( status = '' or status = 'Refund')
                   order by id   """.as[(Int , String , String , String , Int)]

        var skuDs : Vector[(Int , String , String , String , Int)] = null

        Await.result(
          session.db.run(q2).map { res =>
            println(res.size)
            skuDs = res
          }, timeout)

        if(skuDs.size > 0){

          val qty = r._3
          var count = 0
          println(r._1)
          println(r._5)
          while(count < qty){

            println(r._5)

            var remark =
              if(skuDs(count)._4 != null && skuDs(count)._4 !="")
                skuDs(count)._4  + "/" + skuDs(count)._2 +"_"+ skuDs(count)._3
              else
                "/" + skuDs(count)._2 +"_"+ skuDs(count)._3

            if(count <= (skuDs.size -1) ){
              val shipment_sku_identification_id = skuDs(count)._1

              val lineitem =   sqlu""" UPDATE public.shipment_sku_identification
                                   SET drs_transaction_id='#${r._1}', status='other-transaction'
                                    , remark='#${remark}'
                                   where id = #${shipment_sku_identification_id}   """

              println(lineitem.statements)

              Await.result(
                session.db.run(lineitem).map { res =>
                  println(res)
                }, timeout)

            }else{


              val lineitem =   sqlu""" INSERT INTO public.shipment_sku_identification_exception
                             ( drs_transaction_id, source_transaction_id, "type", ivs_name, sku_id)
                            VALUES(  #${r._1}, '#${r._2}' , 'other-transaction' ,'#${r._7}', #${r._5});   """




              println(lineitem.statements)

              Await.result(
                session.db.run(lineitem).map { res =>
                  println(res)
                }, timeout)


            }


            count +=1

          }

        }else{



          val lineitem =   sqlu""" INSERT INTO public.shipment_sku_identification_exception
                             ( drs_transaction_id, source_transaction_id, "type", ivs_name, sku_id)
                            VALUES(   #${r._1}, '#${r._2}' , 'other-transaction' ,'#${r._7}', #${r._5});   """


          println(lineitem.statements)

          Await.result(
            session.db.run(lineitem).map { res =>
              println(res)
            }, timeout)


        }

      }else{
        val lineitem =   sqlu""" INSERT INTO public.shipment_sku_identification_exception
                             ( drs_transaction_id, source_transaction_id, "type", ivs_name, sku_id)
                            VALUES(   #${r._1}, '#${r._2}' , 'other-transaction' ,'#${r._7}', #${r._5});   """


        println(lineitem.statements)

        Await.result(
          session.db.run(lineitem).map { res =>
            println(res)
          }, timeout)


      }








    })


  }


}
