package com.kindminds.drs.core.actors.handlers.command.p2m

import java.util
import java.util.{Arrays, List}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.core.{DrsCmdBus, DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.api.message.command.p2m.{ApplyToRemoveP2MApplication, ApproveP2MApplication, ApproveToRemoveP2MApplication, ChangeP2MApplication, CreateApplicationNumber, CreateP2MApplication, DeleteP2MApplication, ReApplyP2MApplication, RejectP2MApplication, RejectToRemoveP2MApplication, SaveInsurance, SaveMarketplaceInfo, SaveP2MProductInfo, SaveRegional, SaveShipping, SubmitP2MApplication, UpdateP2MApplication}
import com.kindminds.drs.api.message.event.{P2mApplicationApproved, P2mApplicationCreated, P2mApplicationRejected, P2mApplicationSubmitted, P2mApplicationApproveToRemove}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.api.data.access.nosql.mongo.p2m.{InsuranceDao, MarketplaceInfoDao, P2MProductInfoDao, ShippingDao}
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.{InsuranceDaoImpl, MarketplaceInfoDaoImpl, P2MApplicationDao, P2MProductDaoImpl, RegionalDao, ShippingDaoImpl}
import com.mongodb.client.model.Filters.eq
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId;


object P2MCmdHandler {
  def props(drsCmdBus: ActorRef , drsEventBus: DrsEventBus): Props =
    Props(new P2MCmdHandler(drsCmdBus,drsEventBus))

}

class P2MCmdHandler(drsCmdBus: ActorRef, drsEventBus: DrsEventBus ) extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name, classOf[CreateApplicationNumber].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[SubmitP2MApplication].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[ApplyToRemoveP2MApplication].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[RejectToRemoveP2MApplication].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[ApproveToRemoveP2MApplication].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[DeleteP2MApplication].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[CreateP2MApplication].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[UpdateP2MApplication].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[SaveMarketplaceInfo].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[SaveP2MProductInfo].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[SaveInsurance].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[SaveRegional].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[SaveShipping].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[ApproveP2MApplication].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[RejectP2MApplication].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[ChangeP2MApplication].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[ReApplyP2MApplication].getName, self)

  /*
  val om = new ObjectMapper()//.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
   */

  val p2mDao =  new P2MApplicationDao

  val mpiDao = new MarketplaceInfoDaoImpl

  val rDao = new RegionalDao

  val iDao = new InsuranceDaoImpl

  val pDao = new P2MProductDaoImpl

  val shDao = new ShippingDaoImpl()

  val productDao = new ProductDao

  var p2mApplicationCreated:String=null

  var p2mApplicationSubmitted:String=null

  private def createApplicationName(kcode:String ,serialNum:Int) : String = {

    "P2M-" + kcode + "-" + serialNum
  }

  override def receive: Receive = {


    case c : CreateApplicationNumber =>

      //todo arthur use dao first
      //do we have create interface
      //i use data_row to map rdb
      //what if mongodb , don't wnat to use dto.
      //so what is the class , model ?
      //domain ,
      // consider current resource and situation ,
      // it is better to implement rule on actor and only use dao.

      //use biz.model.simplification

    case ch : ChangeP2MApplication =>

     val doc =  this.p2mDao.findById(ch.p2mId)

      doc.remove("_id")
      val timestamp = System.currentTimeMillis
      val kcode = doc.get("supplierId").toString

      val serialNum = p2mDao.findMaxSerialNumber(kcode) + 1
      doc.put("serial_num", serialNum)
      doc.put("name" , createApplicationName(kcode , serialNum))
      doc.put("type","update")
      doc.put("appliedDate", timestamp)
      doc.put("createdBy", ch.userId)
      doc.put("updatedBy", ch.userId)
      doc.put("status", "Pending")
      doc.put("version", "1")
      doc.put("createdDateTime", timestamp)
      doc.put("updatedDateTime", timestamp)
      this.p2mDao.save(doc)

      this.sender() ! "Done"


    case up : UpdateP2MApplication =>
      val p2mSKU: Array[String] = new Array[String](100)
      val p2mPSKU: Array[String] = new Array[String](100)
      import java.util
      val mList = new util.ArrayList[AnyRef]
      val iList = new util.ArrayList[AnyRef]
      val rList = new util.ArrayList[AnyRef]
      val sList = new util.ArrayList[AnyRef]
      val pList = new util.ArrayList[AnyRef]
      var p2mSKUIndex = 0
      var p2mPSKUIndex = 0
      val doc = Document.parse(up.p2mApplication)
      doc.put("updatedBy", up.userId)

      val skus: util.List[AnyRef] = doc.get("appliedSkuCode").asInstanceOf[util.List[AnyRef]]

      val marketplaceInfoDoc = mpiDao.findDocumentByP2MApplicationId(up.p2mId)

      val insuranceDoc = iDao.findDocumentByP2MApplicationId(up.p2mId)

      val regionalDoc = rDao.findDocumentByP2MApplicationId(up.p2mId)

      val shippingDoc = shDao.findDocumentByP2MApplicationId(up.p2mId)

      val productAdvancedInfoDoc = pDao.findDocumentByP2MApplicationId(up.p2mId)

      val appliedMSku: util.List[AnyRef] = marketplaceInfoDoc.get("appliedSku").asInstanceOf[util.List[AnyRef]]
      val appliedISku: util.List[AnyRef] = insuranceDoc.get("appliedSku").asInstanceOf[util.List[AnyRef]]
      val appliedRSku: util.List[AnyRef] = regionalDoc.get("appliedSku").asInstanceOf[util.List[AnyRef]]
      val appliedSSku: util.List[AnyRef] = shippingDoc.get("appliedSku").asInstanceOf[util.List[AnyRef]]
      val appliedPSku: util.List[AnyRef] = productAdvancedInfoDoc.get("appliedSku").asInstanceOf[util.List[AnyRef]]
      val insuranceStep: util.List[AnyRef] = insuranceDoc.get("steps").asInstanceOf[util.List[AnyRef]]

      skus.forEach(x=>{
        p2mSKU(p2mSKUIndex) = x.toString
        p2mSKUIndex += 1
      })

      appliedPSku.forEach(appliedx => {
        p2mPSKU(p2mPSKUIndex) = appliedx.asInstanceOf[Document].get("skuCode").toString
        p2mPSKUIndex += 1
      })
      val process = insuranceDoc.get("process").toString
      if(process == "ph-4" || process == "ph-5" || process == "ph-6" || process == "ph-7"){
        insuranceStep.forEach(step => {
          val stepName = step.asInstanceOf[Document].get("name").toString
          if(stepName == "insurance.step4" || stepName == "insurance.step5" || stepName == "insurance.step6" || stepName == "insurance.step7"){
            step.asInstanceOf[Document].append("state","")
          }
        })

        insuranceDoc.append("steps",insuranceStep)
        insuranceDoc.append("process","ph-3")
      }

      val productdoc = Document.parse("{\"skuInfo\":" + up.skuInfo + "}")
      val skuInfo: util.List[AnyRef] = productdoc.get("skuInfo").asInstanceOf[util.List[AnyRef]]
      for (m <- 0 until p2mSKUIndex) {
          if (p2mPSKU.contains(p2mSKU(m))) {
          } else{
          p2mPSKU(p2mPSKUIndex) = p2mSKU(m)
          p2mPSKUIndex += 1
            skuInfo.forEach(skui => {
            if (skui.asInstanceOf[Document].get("sellerSku").toString.equals(p2mSKU(m))) {
              val newMSku = setnewMSku(skui.asInstanceOf[Document],p2mSKU(m))
              val newISku = setnewISku(p2mSKU(m))
              val newRSku = setnewRSku(p2mSKU(m))
              val newSSku = setnewSSku(p2mSKU(m))
              val newPSku = setnewPSku(skui.asInstanceOf[Document],p2mSKU(m))

              appliedMSku.add(newMSku)
              appliedISku.add(newISku)
              appliedRSku.add(newRSku)
              appliedSSku.add(newSSku)
              appliedPSku.add(newPSku)
            }
          })
        }
      }
      for (m <- 0 until p2mPSKUIndex) {
        if (p2mSKU.contains(p2mPSKU(m))) {
          mList.add(appliedMSku.get(m))
          iList.add(appliedISku.get(m))
          rList.add(appliedRSku.get(m))
          sList.add(appliedSSku.get(m))
          pList.add(appliedPSku.get(m))
        }
      }

      marketplaceInfoDoc.append("appliedSku",mList)
      insuranceDoc.append("appliedSku",iList)
      regionalDoc.append("appliedSku",rList)
      shippingDoc.append("appliedSku",sList)
      productAdvancedInfoDoc.append("appliedSku",pList)

      this.p2mDao.updateSku(doc)
      mpiDao.save(marketplaceInfoDoc.toJson)
      iDao.save(insuranceDoc.toJson)
      rDao.save(regionalDoc)
      shDao.save(shippingDoc.toJson)
      pDao.save(productAdvancedInfoDoc.toJson)

      def setnewMSku(document: Document, sku: String) = {
        val doc = new Document()
          .append("sellerSku", sku)
          .append("productId", new Document().append("name", "Product ID").append("value", document.get("productId").asInstanceOf[Document].get("value").toString))
          .append("productIdType", new Document().append("name", "Product ID Type").append("value", document.get("productIdType").asInstanceOf[Document].get("value").toString))
          .append("noIdProvide", document.get("noIdProvide").toString)
          .append("variationTheme", new Document().append("name", "Variation Theme").append("value", document.get("variationTheme").asInstanceOf[Document].get("value").toString))
          .append("variable", new Document().append("name", "Variable").append("value", document.get("variable").asInstanceOf[Document].get("value").toString))
          .append("skuCode", sku)
          .append("variationNameForMarketplace", "")
          .append("mainImgUrl", "")
          .append("img", util.Arrays.asList())
          .append("title", "")
          .append("description", "")
          .append("feature", util.Arrays.asList("","","","",""))
          .append("keyword", util.Arrays.asList("","","","",""))
          .append("comment", new Document().append("variationNameForMarketplace", "").append("img", "").append("title", "").append("description", "").append("feature", "").append("keyword", ""))
        doc
      }

      def setnewISku(sku: String) = {
        val doc = new Document()
          .append("skuCode", sku)
          .append("supplierInsuranceFile", util.Arrays.asList(new Document().append("name", "").append("insuredProductName", "").append("insuredRegions", util.Arrays.asList()).append("insuredValidDate", "2021-11-29T01:50:14.979Z").append("agreement", "")))
        doc
      }

      def setnewRSku(sku: String) = {
        val doc = new Document()
          .append("skuCode", sku)
          .append("productImg", util.Arrays.asList())
          .append("certificateFile", util.Arrays.asList())
          .append("patentFile", util.Arrays.asList())
          .append("otherFile", util.Arrays.asList())
        doc
      }

      def setnewSSku(sku: String) = {
        val doc = new Document()
          .append("skuCode", sku)
          .append("netWidth", "")
          .append("netHeight", "")
          .append("netLength", "")
          .append("netWeight", "")
          .append("includePackageWidth", "")
          .append("includePackageHeight", "")
          .append("includePackageLength", "")
          .append("includePackageWeight", "")
          .append("fcaPrice", "")
        doc
      }

      def setnewPSku(document: Document, sku: String) = {
        var productIdByDrs = ""
        if(document.get("productIdByDrs") != null){
          productIdByDrs = document.get("productIdByDrs").toString
        }
        val doc = new Document()
          .append("skuCode", sku)
          .append("productId", new Document().append("name", "Product ID").append("value", document.get("productId").asInstanceOf[Document].get("value").toString))
          .append("noIdProvide", document.get("noIdProvide").toString)
          .append("productIdByDrs", productIdByDrs)
          .append("url", "")
          .append("manufactureDays", "")
          .append("manufacturePlace", "")
          .append("suggestedPricingNoTax", "")
          .append("suggestedPricingTax", "")
          .append("suggestedRetailPriceNoTax", "")
          .append("suggestedRetailPriceTax", "")
          .append("ddpUnitPrice", "")
          .append("modelNumber", "")
          .append("packageFile", util.Arrays.asList())
          .append("packageImg", util.Arrays.asList())
          .append("manualFile", util.Arrays.asList())
          .append("manualImg", util.Arrays.asList())
          .append("exportSideHsCode", "")
          .append("salesSideHsCode", "")
          .append("ingredientFile", util.Arrays.asList())
          .append("woodenFile", util.Arrays.asList())
          .append("wirelessFile", util.Arrays.asList())
          .append("batteryFile", util.Arrays.asList())
        doc
      }

      this.sender() ! "Done"


    case ca : CreateP2MApplication =>

      //p2mApplicationCreated += ca.p2mApplication
      //val status = "draft"

      val status = "Pending"
      val doc = Document.parse(ca.p2mApplication)
      val timestamp = System.currentTimeMillis
      val kcode = doc.get("supplierId").toString

      val serialNum = p2mDao.findMaxSerialNumber(kcode) + 1
      doc.append("serial_num", serialNum)
      doc.put("name" , createApplicationName(kcode , serialNum))

      doc.remove("appliedDate")
      doc.append("appliedDate", timestamp)
      doc.append("createdBy", ca.userId)
      doc.append("updatedBy", ca.userId)
      doc.append("status", status)
      doc.append("version", "1")
      doc.append("createdDateTime", timestamp)
      doc.append("updatedDateTime", timestamp)

      val p2mId = p2mDao.save(doc)

      val subApplication =  doc.get("subApplication").asInstanceOf[Document]

      var marketPlaceInfo = subApplication.get("marketPlaceInfo").asInstanceOf[Document].toJson

      marketPlaceInfo = marketPlaceInfo.substring(0,marketPlaceInfo.length - 1 )

      marketPlaceInfo = marketPlaceInfo+ "," + "\"p2mApplicationId\":{ \"$oid\" : \"" + p2mId + "\"}," + "\"version\" : 1" + "}"

      var insurance = subApplication.get("insurance").asInstanceOf[Document].toJson

      insurance = insurance.substring(0,insurance.length - 1 )

      insurance = insurance+ "," + "\"p2mApplicationId\":{ \"$oid\" : \"" + p2mId + "\"}," + "\"version\" : 1" + "}"

      var regional = subApplication.get("regional").asInstanceOf[Document].toJson

      regional = regional.substring(0,regional.length - 1 )

      regional = regional+ "," + "\"p2mApplicationId\":{ \"$oid\" : \"" + p2mId + "\"}," + "\"version\" : 1" + "}"

      var shipping = subApplication.get("shipping").asInstanceOf[Document].toJson

      shipping = shipping.substring(0,shipping.length - 1 )

      shipping = shipping+ "," + "\"p2mApplicationId\":{ \"$oid\" : \"" + p2mId + "\"}," + "\"version\" : 1" + "}"

      var productInfo = subApplication.get("productInfo").asInstanceOf[Document].toJson

      productInfo = productInfo.substring(0,productInfo.length - 1 )

      productInfo = productInfo+ "," + "\"p2mApplicationId\":{ \"$oid\" : \"" + p2mId + "\"}," + "\"version\" : 1" + "}"

      mpiDao.intoMongodb(marketPlaceInfo)

      iDao.intoMongodb(insurance)

      rDao.intoMongodb(regional)

      shDao.intoMongodb(shipping)

      pDao.intoMongodb(productInfo)

      //System.out.//println(p2mId)
      //todo arthur
      this.sender() ! "Done"
      drsEventBus.publish(P2mApplicationCreated(p2mId , doc.get("bpId").toString))


    case sb : SubmitP2MApplication =>

//      val status = "Submit"
      //todo arthur

      val doc = Document.parse(sb.p2mApplication)
      val p2mApplicationId = doc.get("p2mApplicationId").toString
      val status = "In Review"
      val userId=sb.userId
      doc.append("status",status);
      doc.append("updatedBy",userId);
      doc.append("updatedDateTime",System.currentTimeMillis());
      //System.out.//println(doc)
      p2mDao.save(doc)
      println("AAAA")
      this.sender() ! "Done"
      drsEventBus.publish(P2mApplicationSubmitted(p2mApplicationId , doc.get("bpId").toString , sb.kcode))


    case atr : ApplyToRemoveP2MApplication =>

      val doc =  this.p2mDao.findById(atr.p2mId)
      val status = "Removal in Review"
      val userId = atr.userId
      doc.append("status",status);
      doc.append("updatedBy",userId);
      doc.append("updatedDateTime",System.currentTimeMillis())
      p2mDao.save(doc)
      this.sender() ! "Done"


    case rtr : RejectToRemoveP2MApplication =>

      val doc = Document.parse(rtr.p2mApplication)

      val status = "Approved"
      val userId = rtr.userId
      val timestamp = System.currentTimeMillis

      doc.append("status",status);
      doc.append("updatedBy",userId);
      doc.append("updatedDateTime",System.currentTimeMillis());

      p2mDao.save(doc)

      this.sender() ! "Done"

    case aprtr : ApproveToRemoveP2MApplication =>

      val doc = Document.parse(aprtr.p2mApplication)
      val p2mApplicationId = doc.get("_id").toString
      val productName = doc.get("productNameEN").toString

      val status = "Removed"
      val userId=aprtr.userId

      doc.put("status",status);
      doc.put("updatedBy",userId);
      doc.put("updatedDateTime",System.currentTimeMillis());

      p2mDao.save(doc)
      this.sender() ! "Done"

      drsEventBus.publish(P2mApplicationApproveToRemove(p2mApplicationId,productName))


    case dl : DeleteP2MApplication =>

      val doc =  this.p2mDao.findById(dl.p2mId)
      val userId=dl.userId
      doc.append("updatedBy",userId)
      doc.append("updatedDateTime",System.currentTimeMillis())
      //System.out.//println(doc)
      p2mDao.delete(doc)

      this.sender() ! "Done"




    case smpi : SaveMarketplaceInfo =>

      //println("AAA")
      //println(smpi.marketplaceInfo)
      mpiDao.save(smpi.marketplaceInfo)

      //todo arthur
      this.sender() ! "Done"

    case sp : SaveP2MProductInfo =>

      pDao.save(sp.p2mProductInfo)

      //todo arthur
      this.sender() ! "Done"

    case sr : SaveRegional =>

      val doc = Document.parse(sr.regional)
      rDao.save(doc)

      //todo arthur
      this.sender() ! "Done"

    case si : SaveInsurance =>

      iDao.save(si.insurance)

      //todo arthur
      this.sender() ! "Done"

    case ssh : SaveShipping =>

      shDao.save(ssh.shipping)

      //todo arthur
      this.sender() ! "Done"

    case apr : ApproveP2MApplication =>

      //todo arthur

      val doc = Document.parse(apr.p2mApplication)
      val p2mApplicationId = doc.get("p2mApplicationId").toString

      val status = "Approved"
      val userId=apr.userId
      val timestamp = System.currentTimeMillis

      doc.remove("approvedDate")
      doc.append("approvedDate", timestamp)
      doc.put("status",status);
      doc.put("updatedBy",userId);
      doc.put("updatedDateTime",System.currentTimeMillis());

      p2mDao.save(doc)
      this.sender() ! "Done"
      val createdBy = doc.get("createdBy").asInstanceOf[Integer]

      val productNameEN = doc.get("productNameEN").toString

      drsEventBus.publish(P2mApplicationApproved(p2mApplicationId , productNameEN ,"",doc.get("bpId").toString  , createdBy ))


    case re : RejectP2MApplication =>

      //todo arthur

      val doc = Document.parse(re.p2mApplication)
      val p2mApplicationId = doc.get("p2mApplicationId").toString
      val productName = doc.get("productNameEN").toString

      val status = "Rejected"
      val userId=re.userId
      val timestamp = System.currentTimeMillis

      doc.append("rejectedDate", timestamp)
      doc.append("status",status);
      doc.append("updatedBy",userId);
      doc.append("updatedDateTime",System.currentTimeMillis());

      p2mDao.save(doc)

      this.sender() ! "Done"
      val createdBy = doc.get("createdBy").asInstanceOf[Integer]
      drsEventBus.publish(P2mApplicationRejected(p2mApplicationId,productName ,"",createdBy ))

    case re : ReApplyP2MApplication =>

      val doc =  this.p2mDao.findById(re.p2mId)

      val timestamp = System.currentTimeMillis
      doc.put("updatedBy", re.userId)
      doc.put("status", "Pending")
      doc.put("updatedDateTime", timestamp)
      this.p2mDao.save(doc)

      this.sender() ! "Done"

    case message: Any =>
      log.info(s"P2MHandler: received unexpected: $message")

  }



}