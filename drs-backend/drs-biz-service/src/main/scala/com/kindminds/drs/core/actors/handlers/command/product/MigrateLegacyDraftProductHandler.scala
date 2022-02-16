package com.kindminds.drs.core.actors.handlers.command.product

import java.io.IOException
import java.util

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.kindminds.drs.Country
import com.kindminds.drs.api.message.command.manageProduct.{MigrateLegacyDraftProductToProductV2, MigrateProducts}
import com.kindminds.drs.api.message.event.OnboardingApplicationSaved
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.biz.product.ProductImpl
import com.kindminds.drs.core.biz.product.onboarding.OnboardingApplicationCreator
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding.{OnboardingApplicationDao, OnboardingApplicationLineitemDao}
import com.kindminds.drs.api.data.transfer.productV2.{LegacyProductDetail, ProductDetail}
import com.kindminds.drs.api.v2.biz.domain.model.product.Product
import com.kindminds.drs.enums.OnboardingApplicationStatusType

import org.springframework.util.Assert




object MigrateLegacyDraftProductHandler {

  def props(drsCmdBus: ActorRef , drsEventBus: DrsEventBus ): Props =
    Props(new MigrateLegacyDraftProductHandler(drsCmdBus ,drsEventBus))

}

class MigrateLegacyDraftProductHandler(drsCmdBus: ActorRef, drsEventBus: DrsEventBus)
  extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[MigrateLegacyDraftProductToProductV2].getName ,self)

  val onboardingApplicationDao = BizCoreCtx.get().getBean(classOf[OnboardingApplicationDao]).asInstanceOf[OnboardingApplicationDao]

  val CompanyDao = BizCoreCtx.get().getBean(classOf[com.kindminds.drs.api.data.access.rdb.CompanyDao])
    .asInstanceOf[com.kindminds.drs.api.data.access.rdb.CompanyDao]

  val onboardingApplicationLineitemDao = BizCoreCtx.get().getBean(classOf[OnboardingApplicationLineitemDao]).asInstanceOf[OnboardingApplicationLineitemDao]


  def receive = {


    case m : MigrateLegacyDraftProductToProductV2 =>

      val id = migrate(m)

      sender() ! "123"

    case message: Any =>
      log.info(s"MigrateLegacyDraftProductHandler: received unexpected: $message")
  }


  def migrate(s : MigrateLegacyDraftProductToProductV2 ) ={

    val objectMapper: ObjectMapper = new ObjectMapper

    val productInfoSourceJsonNode: JsonNode = objectMapper.readValue(
      s.legacyProduct.getProductInfoSource.getData, classOf[JsonNode])

    val bpCode: String = productInfoSourceJsonNode.get("baseProductCode").asText
    var supplierKcode = productInfoSourceJsonNode.get("supplierKcode").asText

    var leagcyPiCoreStatus = s.legacyProduct.getProductInfoSource.getStatus


    if (s.isDrsUser) Assert.notNull(supplierKcode)
    else {
      Assert.isTrue(this.CompanyDao.isSupplier(s.userKcode))
      supplierKcode = s.userKcode
    }


    val products = this.createProducts(supplierKcode, bpCode , s.legacyProduct.getProductInfoSource,
      s.legacyProduct.getProductInfoMarketSide,   s.legacyProduct.getProductMarketingMaterialSource , s.legacyProduct.getProductMarketingMaterialMarketSide)

    drsCmdBus ! MigrateProducts(products)


    /*
    val oa = OnboardingApplicationCreator.createMigrateOnboardingApplication(supplierKcode, bpCode,
      products , OnboardingApplicationStatusType.MIGRATED)


    onboardingApplicationDao.save(oa)

    drsEventBus.publish(OnboardingApplicationSaved(oa))

    oa.getId
    */

  }

  private def createProducts(supplierKcode : String, bpCode :String,
                             productInfoSource: ProductDetail,
                             productInfoMarketSide: java.util.List[LegacyProductDetail],
                             productMarketingMaterialSource: LegacyProductDetail,
                             productMarketingMaterialMarketSide: java.util.List[LegacyProductDetail],
                            ): util.List[Product] = {

    val productList = new util.ArrayList[Product]
    val om = new ObjectMapper

    try {
      val pNode = om.readValue(productInfoSource.getData, classOf[JsonNode])
      val pmNode = om.readValue(om.writeValueAsString(productMarketingMaterialSource), classOf[JsonNode])

      val leagcyPCoreStatus = productInfoSource.getStatus
      val leagcyMmStatus = productMarketingMaterialSource.getStatus


      val coreProdcut = ProductImpl.createLeagcyProduct(supplierKcode, bpCode ,pNode, pmNode , leagcyPCoreStatus)
      productList.add(coreProdcut)

      val applicableRegionBP = pNode.get("applicableRegionBP")
      if (applicableRegionBP.isArray) if (applicableRegionBP.size > 0) {

        //val pmsNode = om.readValue(om.writeValueAsString(productInfoMarketSide), classOf[JsonNode])
        val pmsmNode = om.readValue(om.writeValueAsString(productMarketingMaterialMarketSide), classOf[JsonNode])

        //use product to assign lineitem status

        applicableRegionBP.forEach(x=>{
          import scala.util.control.Breaks._

          breakable { for (i <- 0 to productInfoMarketSide.size() -1) {

            // val marketSideJsonNode = om.readValue(pmsNode.get(i).get("jsonData").asText() ,
            // classOf[JsonNode])


            val marketSideJsonNode = om.readValue( productInfoMarketSide.get(i).getJsonData,
              classOf[JsonNode])

            val marketSide =  marketSideJsonNode.get("country").asText()
            val leagcyPiMarketSideStatus =productInfoMarketSide.get(i).getStatus //pmsNode.get(i).get("status").asText()


            if (marketSide == x.asText()) {

              var marketingJsonNode : JsonNode = null
              breakable { for (j <- 0 to pmsmNode.size() -1) {
                if (pmsmNode.get(j).get("country").asText == marketSide) {
                  marketingJsonNode = pmsmNode.get(j)
                  break
                }}
              }

              productList.add(ProductImpl.createLeagcyMarketSideProduct(supplierKcode, bpCode,
                coreProdcut.getId, coreProdcut.getCreateTime,
                marketSideJsonNode , marketingJsonNode, Country.valueOf(marketSide) ,
                leagcyPiMarketSideStatus))

              break
            }
          } }

        })


      }
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }

    productList

  }

}