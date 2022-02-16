package com.kindminds.drs.data.pipelines.core.product

import java.util
import java.util.List

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.{ActorMaterializer, Materializer}
import com.kindminds.drs.api.data.access.rdb.product.ViewKeyProductStatsDaoV2
import com.kindminds.drs.api.usecase.ViewKeyProductStatsUco
import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport
import com.kindminds.drs.data.pipelines.api.message.{RefreshSkuStatistics, RegisterCommandHandler}
import com.kindminds.drs.data.pipelines.core.BizCoreCtx
import com.kindminds.drs.data.pipelines.core.sales.AllOrders
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao
import org.bson.Document


object ProductDataLoader {

  def props(drsDPCmdBus: ActorRef): Props = Props(new ProductDataLoader(drsDPCmdBus))

}

//todo athur name
case class ProductStatistics(var qty1 :Int , var qty2 : Int ,var qty3 : Int)

class ProductDataLoader(drsDPCmdBus: ActorRef) extends Actor with ActorLogging {

  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  val name = self.path.name

  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshSkuStatistics].getName ,self)

  val productDao = new ProductDao

  val uco: ViewKeyProductStatsUco = BizCoreCtx.get().getBean(classOf[ViewKeyProductStatsUco])
    .asInstanceOf[ViewKeyProductStatsUco]

  override def receive: Receive = {

    case s: RefreshSkuStatistics =>

      val kcoeList = Seq("K488", "K510", "K618", "K520", "K533", "K591", "K624", "K492",
        "K504", "K535", "K555", "K582", "K598", "K601", "K616", "K622", "K578", "K508", "K549",
        "K612", "K633", "K636", "K635", "K637", "K639", "K641", "K644", "K643", "K613", "K646",
        "K647", "K650", "K652")

        kcoeList.foreach(k =>{

        var  productMap:Map[String,ProductStatistics] = Map()
        val keyProductStatsReport = uco.getKeyProductStatsReport(true, k)
        keyProductStatsReport.getKeyProductStatsByCountryList.forEach(p=>{

          System.out.println(p.getCountry)
          p.getBaseToKeySkuStatsListMap.entrySet().forEach( kv => {

            println("BBBBB")

            var baseCode = ""
            var doc :Document = null
            kv.getValue.forEach(item => {
              val pmKey = item.getBaseCodeByDrs + "/" + item.getSkuCode
              if(productMap.contains(pmKey)){
                val ps = productMap.get(pmKey)
                ps.get.qty1 += item.getFbaQtyInStock.toInt
                ps.get.qty2 += item.getQtyOrderedInLastSevenDays.toInt
                ps.get.qty3 += item.getQtyOrderedInCurrentSettlementPeriod.toInt
              }else{
                productMap += (pmKey -> ProductStatistics(item.getFbaQtyInStock.toInt,
                  item.getQtyOrderedInLastSevenDays.toInt
                  ,item.getQtyOrderedInCurrentSettlementPeriod.toInt))
              }
            })

          })
        })

        //there is a problem with fba qty
        productMap.foreach(kv =>{
          val keyAry =  kv._1.split("/")
          println(keyAry(0))
          val doc = productDao.findByBaseCode(keyAry(0))
          if(doc != null){
            val skus: util.List[AnyRef] = doc.get("skus").asInstanceOf[util.List[AnyRef]]
            skus.forEach(x=>{
              val sku = x.asInstanceOf[Document]
              if(sku.get("sellerSku") == keyAry(1)){
                println("AAAAAAAA")
                println(sku.get("sellerSku"))

                println(kv._2.qty2)
                println(kv._2.qty3)
                sku.put("lastSevenDaysOrder",kv._2.qty2)
                sku.put("settlementsPeriodOrder",kv._2.qty3)

              }

            })

            productDao.updateProduct(doc)
          }

        })

      })



  }

}
