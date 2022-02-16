package com.kindminds.drs.core.actors.handlers.query.product

import java.util

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.kindminds.drs.api.message.query.onboardingApplication._
import com.kindminds.drs.api.message.query.product._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.{ProductDao, ProductMarketingMaterialDao, ProductVariationDao}
import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductViewQueries
import com.kindminds.drs.api.data.transfer.productV2.ProductDto
import com.kindminds.drs.api.v1.model.common.{DtoList, Pager}


import com.kindminds.drs.v1.model.impl.{BaseProductOnboardingSKUItemImpl, BaseProductOnboardingWithSKUImpl}
import org.springframework.util.Assert


object ProductViewHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new ProductViewHandler(drsQueryBus))

}

class ProductViewHandler (drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name,classOf[GetProductList].getName ,self)
  drsQueryBus ! RegisterQueryHandler(name,classOf[GetProduct].getName ,self)
  drsQueryBus ! RegisterQueryHandler(name,classOf[GetProductInfoMarketSide].getName ,self)

  drsQueryBus ! RegisterQueryHandler(name,classOf[GetProductWithSKU].getName ,self)
  drsQueryBus ! RegisterQueryHandler(name,classOf[GetProductList4DRS].getName ,self)

  drsQueryBus ! RegisterQueryHandler(name,classOf[GetProductVariationarketSides].getName ,self)

  drsQueryBus ! RegisterQueryHandler(name,classOf[GetProductBaseCode].getName ,self)
  drsQueryBus ! RegisterQueryHandler(name,classOf[GetProductSkuCode].getName ,self)

  drsQueryBus ! RegisterQueryHandler(name,classOf[GetAmazonProductReview].getName ,self)


  private val sizePerPage = 20


  val productViewQueries = BizCoreCtx.get().getBean(classOf[ProductViewQueries])
    .asInstanceOf[ProductViewQueries]

  val productVariationDao = BizCoreCtx.get().getBean(classOf[ProductVariationDao])
    .asInstanceOf[ProductVariationDao]

  val productMarketingMaterialDao = BizCoreCtx.get().getBean(classOf[ProductMarketingMaterialDao])
    .asInstanceOf[ProductMarketingMaterialDao]

  val CompanyDao = BizCoreCtx.get().getBean(classOf[com.kindminds.drs.api.data.access.rdb.CompanyDao])
    .asInstanceOf[com.kindminds.drs.api.data.access.rdb.CompanyDao]

  val productTDao = BizCoreCtx.get().getBean(classOf[
    com.kindminds.drs.api.data.access.rdb.product.ProductDao])
    .asInstanceOf[  com.kindminds.drs.api.data.access.rdb.product.ProductDao]

  val prDao = BizCoreCtx.get().getBean(classOf[
    com.kindminds.drs.api.data.access.rdb.product.AmazonProductReviewDao])
    .asInstanceOf[  com.kindminds.drs.api.data.access.rdb.product.AmazonProductReviewDao]

  val om = new ObjectMapper()

  def receive = {

    case gms : GetProductVariationarketSides =>

      val ms = productVariationDao.findMarketSides(gms.productBaseCode,gms.productVariationCode)

      this.sender() ! ms

    case v : GetProductWithSKU =>

      val baseProductOnboardingWithSKU = processVariations(v.productBaseCode)

      val s = om.writeValueAsString(baseProductOnboardingWithSKU)

      this.sender() ! s


    case bpm : GetProductInfoMarketSide =>

     val product = this.productViewQueries.getProductInfoMarketSide(bpm.companyKcode, bpm.productBaseCode, bpm.marketSide)

      val s = om.writeValueAsString(product)

      this.sender() ! s

    case bp : GetProduct =>


      if (this.CompanyDao.isSupplier(bp.companyKcode)) {
        val supplierKcode = this.productViewQueries.getSupplierKcodeOfBaseProductOnboarding(bp.productBaseCode)

        Assert.isTrue(bp.companyKcode == supplierKcode)
      }

      val product = this.productViewQueries.getActivateBaseProductOnboarding(bp.productBaseCode)

      val s = om.writeValueAsString(product)

      this.sender() ! s

    case bpl : GetProductList =>


      val list =retrieveBaseProductOnboardingList(bpl)

      val s = om.writeValueAsString(list)

      this.sender() ! s

    case drsbpl : GetProductList4DRS =>


      val list =retrieveBaseProductOnboardingList4DRS(drsbpl)

      val s = om.writeValueAsString(list)

      this.sender() ! s


    case bp : GetProductBaseCode =>

      //println(bp.companyKcode)
      val list = productTDao.queryProductBaseCode(bp.companyKcode)


      val s = om.writeValueAsString(list)

      //println(s)

      this.sender() ! s

    case sku : GetProductSkuCode =>


      val list = productTDao.queryProductSkuCode(sku.productBaseCode)

      val s = om.writeValueAsString(list)

      //println(s)

      this.sender() ! s

    case pr : GetAmazonProductReview =>


      val list = prDao.getAmazonProductReviews(pr.companyKcode.getOrElse("All") ,  pr.marketPlace.getOrElse(null),
        pr.productBaseCode.getOrElse(null), pr.productSkuCode.getOrElse(null))

      val s = om.writeValueAsString(list)


      this.sender() ! s



    case message: Any =>
      log.info(s"ProductHandler: received unexpected: $message")
  }

  def retrieveBaseProductOnboardingList(bpl : GetProductList): DtoList[ProductDto] ={

    val list = new DtoList[ProductDto]
    var totalSize  = 0
    var pager : Pager = null
    var result: util.List[ProductDto] = null


    totalSize = productViewQueries.getBaseProductOnboardingTotalSize(bpl.companyKcode)
    pager = new Pager(bpl.pageIndex, totalSize, this.sizePerPage)
    result  = productViewQueries.getBaseProductOnboardingList(pager.getStartRowNum, pager.getPageSize, bpl.companyKcode)

    list.setTotalRecords(totalSize)
    list.setPager(pager)
    list.setItems(result)

    list

  }

  def retrieveBaseProductOnboardingList4DRS(drsbpl : GetProductList4DRS): DtoList[ProductDto] ={

    val list = new DtoList[ProductDto]
    var totalSize  = 0
    var pager : Pager = null
    var result: util.List[ProductDto] = null

    if (drsbpl.companyKcode.equals("All")){

      totalSize = productViewQueries.getBaseProductOnboardingTotalSize()
      pager = new Pager(drsbpl.pageIndex, totalSize, this.sizePerPage)
      result = productViewQueries.getBaseProductOnboardingList(pager.getStartRowNum, pager.getPageSize)

    }else{

      totalSize = productViewQueries.getBaseProductOnboardingTotalSize(drsbpl.companyKcode)
      pager = new Pager(drsbpl.pageIndex, totalSize, this.sizePerPage)
      result  = productViewQueries.getBaseProductOnboardingList(pager.getStartRowNum, pager.getPageSize,
        drsbpl.companyKcode)

    }

    list.setTotalRecords(totalSize)
    list.setPager(pager)
    list.setItems(result)

    list

  }

  def processVariations(productBaseCode : String): BaseProductOnboardingWithSKUImpl ={

    val baseProduct = this.productViewQueries.getActivateBaseProductOnboarding(productBaseCode)



    val productInfoSource = baseProduct.getProductInfoSource
    val productInfoSourceData = productInfoSource.getData
    val productInfoSourceJsonNode = om.readValue(productInfoSourceData, classOf[JsonNode])
    val products = productInfoSourceJsonNode.get("products").asText
    val productsJsonNode = om.readValue(products, classOf[JsonNode])

    val baseProductOnboardingWithSKU = new BaseProductOnboardingWithSKUImpl
    val skuList = new util.ArrayList[BaseProductOnboardingSKUItemImpl]
    var i = 0
    while ( {
      i < productsJsonNode.size
    }) {
      val sku = new BaseProductOnboardingSKUItemImpl
      sku.setIndex(i)
      sku.setOriginalSKU(productsJsonNode.asInstanceOf[ArrayNode].get(i).get("SKU").asText)
      sku.setUpdatedSKU(productsJsonNode.asInstanceOf[ArrayNode].get(i).get("SKU").asText)
      sku.setType1(productsJsonNode.asInstanceOf[ArrayNode].get(i).get("type1").asText)
      sku.setType1Value(productsJsonNode.asInstanceOf[ArrayNode].get(i).get("type1Value").asText)
      sku.setType2(productsJsonNode.asInstanceOf[ArrayNode].get(i).get("type2").asText)
      sku.setType2Value(productsJsonNode.asInstanceOf[ArrayNode].get(i).get("type2Value").asText)
      sku.setGTINValue(productsJsonNode.asInstanceOf[ArrayNode].get(i).get("GTINValue").asText)
      val applicableRegionList = new util.TreeSet[String]
      val applicableRegions = productsJsonNode.asInstanceOf[ArrayNode].get(i).get("applicableRegionList").elements
      while ( {
        applicableRegions.hasNext
      }) applicableRegionList.add(applicableRegions.next.asText)
      sku.setApplicableRegionList(applicableRegionList)
      skuList.add(sku)

      {
        i += 1; i - 1
      }
    }
    baseProductOnboardingWithSKU.setProductBaseCode(productInfoSourceJsonNode.get("baseProductCode").asText)
    baseProductOnboardingWithSKU.setSupplierKcode(productInfoSourceJsonNode.get("supplierKcode").asText)

    //temp mark
    //baseProductOnboardingWithSKU.setStatus(productInfoSourceJsonNode.get("status").asText)

    baseProductOnboardingWithSKU.setVariationType1(productInfoSourceJsonNode.get("variationType1").asText)
    baseProductOnboardingWithSKU.setVariationType2(productInfoSourceJsonNode.get("variationType2").asText)
    baseProductOnboardingWithSKU.setProductWithVariation(productInfoSourceJsonNode.get("productWithVariation").asText)
    baseProductOnboardingWithSKU.setSKULineItem(skuList)

    baseProductOnboardingWithSKU

  }




}

