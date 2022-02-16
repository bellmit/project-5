package com.kindminds.drs.core.actors.handlers.query.product

import java.util

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.query.manageProduct.{GetBaseProductById, GetBaseProducts, GetNextPage, GetSimpleProductList, GetTotalProductNumber, GetTotalSkuNumber}
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao
import org.bson
import org.bson.Document


object ManageProductQueryHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new ManageProductQueryHandler(drsQueryBus))

}

class ManageProductQueryHandler(drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name


  drsQueryBus ! RegisterQueryHandler(name, classOf[GetBaseProducts].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetNextPage].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetBaseProductById].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetSimpleProductList].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetTotalProductNumber].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetTotalSkuNumber].getName, self)


  val productDao = new ProductDao
  val om = new ObjectMapper()//.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

  override def receive: Receive = {

    case gpl : GetSimpleProductList =>

     // var bplist = "{\"bpList\":[{\"value\":\"All\",\"label\":\"All\"},"
      var bplist = "{\"bpList\":["
      val resultList =  this.productDao.findSimpleProductList(gpl.kcode)

      resultList.forEach(r=>{
        val doc = new Document()
        val bpid = r.get("_id").toString
        val productNameEN = r.get("productNameEN").toString
        doc.append("value", bpid)
        doc.append("label", productNameEN)
        bplist = bplist + doc.toJson + ","
      })

      if(resultList.size() > 0 )bplist = bplist.substring(0, bplist.length - 1)
      bplist = bplist + "]}"

      this.sender() ! bplist


    case gbp : GetBaseProducts =>

      val size = this.productDao.countByKcode(gbp.kcode)
      val totalPages = BigDecimal(size.toDouble/10).setScale(0, BigDecimal.RoundingMode.UP)
      val result = this.productDao.findByKcode(gbp.kcode, gbp.pageIndex)

      this.sender() ! "{ \"products\":" +  result + ", \"totalPages\":"+ totalPages+" , \"pageIndex\":"+ gbp.pageIndex +"}"

    case np : GetNextPage =>

      val size = this.productDao.countByKcode( np.kcode)
      val totalPages = BigDecimal(size.toDouble/10).setScale(0, BigDecimal.RoundingMode.UP)
      val result = this.productDao.findNextpage(
        np.kcode, np.pageIndex)

      this.sender() ! "{ \"products\":" +  result + ", \"totalPages\":"+ totalPages+" , \"pageIndex\":"+ np.pageIndex +"}"

    case pId : GetBaseProductById =>

      val result = this.productDao.findById(pId.productId)

      //println("GetBaseProductById: " + result.toJson)

      this.sender() ! result.toJson

    case pn : GetTotalProductNumber =>

      val result = productDao.findTotalProductNumber(pn.kcode)
      this.sender() ! result.toString

    case sn : GetTotalSkuNumber =>

      val result = productDao.findTotalSkuNumber(sn.kcode)
      this.sender() ! result.toString


    case message: Any =>
      log.info(s"ManageProductQueryHandler: received unexpected: $message")

  }



}
