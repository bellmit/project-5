package com.kindminds.drs.data.pipelines.core.product

import java.util
import java.util.List
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.{ActorMaterializer, Materializer}
import com.kindminds.drs.api.data.access.rdb.product.ViewKeyProductStatsDaoV2
import com.kindminds.drs.api.usecase.ViewKeyProductStatsUco
import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport
import com.kindminds.drs.data.pipelines.api.message.{RefreshSkuRetailPrice, RegisterCommandHandler}
import com.kindminds.drs.data.pipelines.core.BizCoreCtx
import com.kindminds.drs.data.pipelines.core.sales.AllOrders
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.{P2MApplicationDao, P2MProductDaoImpl}
import org.bson.Document


object ProductRetailPriceLoader {

  def props(drsDPCmdBus: ActorRef): Props = Props(new ProductRetailPriceLoader(drsDPCmdBus))

}

class ProductRetailPriceLoader(drsDPCmdBus: ActorRef) extends Actor with ActorLogging {

  implicit val ec =  scala.concurrent.ExecutionContext.global
  implicit val materializer: Materializer = ActorMaterializer()

  val name = self.path.name

  drsDPCmdBus ! RegisterCommandHandler(name,classOf[RefreshSkuRetailPrice].getName ,self)

  val productDao = new ProductDao
  val p2MApplicationDao = new P2MApplicationDao
  val p2MProductInfoDao = new P2MProductDaoImpl

  override def receive: Receive = {

    case s: RefreshSkuRetailPrice =>
      println("AAAAAAAA")
      val kcoeList = Seq("K488", "K510", "K618", "K520", "K533", "K591", "K624", "K492",
        "K504", "K535", "K555", "K582", "K598", "K601", "K616", "K622", "K578", "K508", "K549",
        "K612", "K633", "K636", "K635", "K637", "K639", "K641", "K644", "K643", "K613", "K646",
        "K647", "K650", "K652")

      kcoeList.foreach(k =>{
        println("11111111111")
        var suggestedRetailPriceNoTax = "未定價"
        val productDoc = productDao.findByKcodeList(k)
        if (productDoc.size() != 0) {
          productDoc.forEach(doc => {
            println(doc.toJson)
            if(doc != null) {
              val productName = doc.get("productNameEN").toString
              val p2mDoc = p2MApplicationDao.findByProductNameList(productName)
              val skus: util.List[AnyRef] = doc.get("skus").asInstanceOf[util.List[AnyRef]]
              skus.forEach(x=>{
                val sku = x.asInstanceOf[Document]
                val productSku = sku.get("sellerSku")
                if (p2mDoc.size() != 0) {
                  p2mDoc.forEach(x => {
                    val id = x.get("_id").toString
                    val selectedPlatform = x.get("selectedPlatform").toString
                    val p2mProductAdvancedInfoDoc = p2MProductInfoDao.findDocumentByP2MApplicationId(id)

                    if (p2mProductAdvancedInfoDoc != null) {
                      val appliedSku: util.List[AnyRef] = p2mProductAdvancedInfoDoc.get("appliedSku").asInstanceOf[util.List[AnyRef]]
                      appliedSku.forEach(appliedx => {
                        val paisku = appliedx.asInstanceOf[Document]

                        if (paisku.get("skuCode") == productSku) {
                          if(paisku.get("suggestedRetailPriceNoTax")!=null) {
                            suggestedRetailPriceNoTax = paisku.get("suggestedRetailPriceNoTax").toString
                          }

                          if(selectedPlatform.equals("Amazon.com")){
                            sku.put("retailPriceInUs",suggestedRetailPriceNoTax)
                          } else if(selectedPlatform.equals("TureToSource")){
                            sku.put("retailPriceInTts",suggestedRetailPriceNoTax)
                          } else if(selectedPlatform.equals("Amazon.co.uk")){
                            sku.put("retailPriceInUk",suggestedRetailPriceNoTax)
                          } else if(selectedPlatform.equals("Amazon.ca")){
                            sku.put("retailPriceInCa",suggestedRetailPriceNoTax)
                          } else if(selectedPlatform.equals("Amazon.de")){
                            sku.put("retailPriceInDe",suggestedRetailPriceNoTax)
                          } else if(selectedPlatform.equals("Amazon.fr")){
                            sku.put("retailPriceInFr",suggestedRetailPriceNoTax)
                          } else if(selectedPlatform.equals("Amazon.it")){
                            sku.put("retailPriceInIt",suggestedRetailPriceNoTax)
                          } else if(selectedPlatform.equals("Amazon.es")){
                            sku.put("retailPriceInEs",suggestedRetailPriceNoTax)
                          }
                        }
                      })
                    }
                  })
                  productDao.updateProduct(doc)
                }
              })
            }
          })
        }
      })
  }

}
