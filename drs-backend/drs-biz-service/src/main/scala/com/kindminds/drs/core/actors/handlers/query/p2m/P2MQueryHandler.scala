package com.kindminds.drs.core.actors.handlers.query.p2m

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.command.p2m.{CreateApplicationNumber, CreateP2MApplication}
import com.kindminds.drs.api.message.query.p2m.{GetAllApplicationList, GetAllComment, GetApplicationList, GetCanBeChangedApplications, GetP2MApplication, GetP2MInsurance, GetP2MMarketplaceInfo, GetP2MProductInfo, GetP2MRegional, GetP2MShipping, GetTotalAppliedSkuNumber, GetTotalOnSaleSkuNumber}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.core.actors.handlers.query.customer.CustomerQueryHandler
import com.kindminds.drs.api.data.access.nosql.mongo.p2m.{InsuranceDao, MarketplaceInfoDao, P2MProductInfoDao, ShippingDao}
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.{InsuranceDaoImpl, MarketplaceInfoDaoImpl, P2MApplicationDao, P2MProductDaoImpl, RegionalDao, ShippingDaoImpl}

object P2MQueryHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new P2MQueryHandler(drsQueryBus))

}


class P2MQueryHandler(drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  val om = new ObjectMapper()//.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))


  drsQueryBus ! RegisterQueryHandler(name, classOf[GetApplicationList].getName, self)
  drsQueryBus ! RegisterQueryHandler(name,classOf[GetAllApplicationList].getName, self)
  drsQueryBus ! RegisterQueryHandler(name,classOf[GetAllComment].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetP2MApplication].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetP2MMarketplaceInfo].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetP2MProductInfo].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetP2MInsurance].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetP2MRegional].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetP2MShipping].getName, self)

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetTotalAppliedSkuNumber].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetTotalOnSaleSkuNumber].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetCanBeChangedApplications].getName, self)

  val p2mDao = new P2MApplicationDao

  val mpiDao = new MarketplaceInfoDaoImpl()

  val rDao = new RegionalDao

  val iDao = new InsuranceDaoImpl()

  val pDao = new P2MProductDaoImpl

  val shDao = new ShippingDaoImpl()

  val productDao = new ProductDao

  override def receive: Receive = {


    case gl : GetApplicationList =>

      val size = p2mDao.countByFilter(gl.supplierId , gl.country , gl.status , gl.kcode , gl.product)

      val totalPages = BigDecimal(size.toDouble/10).setScale(0, BigDecimal.RoundingMode.UP)

      val p2mList = p2mDao.findList(gl.supplierId , gl.pageIndex , gl.country , gl.status , gl.kcode , gl.product)

      val result = "{\"p2mApplications\":"+ p2mList +" , \"totalPages\":"+ totalPages+" , \"pageIndex\":"+ gl.pageIndex +"}"

      this.sender() ! result

    case gal : GetAllApplicationList =>

      val p2mList = p2mDao.findAllList(gal.supplierId, gal.kcode, gal.product)

      val result = "{\"p2mApplications\":"+ p2mList  +"}"

      this.sender() ! result

    case p2m : GetP2MApplication =>

      val result = p2mDao.findById(p2m.id);

      this.sender() ! (if(result != null) result.toJson() else {})

    case gac : GetAllComment =>

      val mcomment = mpiDao.findCommentByP2MId(gac.p2mApplicationId)
      val rcomment = rDao.findCommentByP2MId(gac.p2mApplicationId)
      val scomment = shDao.findCommentByP2MId(gac.p2mApplicationId)
      val pcomment = pDao.findCommentByP2MId(gac.p2mApplicationId)

      val result = "{"+mcomment + "\"insurance\" : {}," + rcomment + scomment + pcomment+"}"

      //println(result)
      this.sender() ! result

    case m : GetP2MMarketplaceInfo =>

      val result = mpiDao.findByP2MApplicationId(m.p2mApplicationId)
      //println(result)
      this.sender() ! result

    case s : GetP2MShipping =>

      val result = shDao.findByP2MApplicationId(s.p2mApplicationId)
      //println(result)
      this.sender() ! result

    case r : GetP2MRegional =>

      val result = rDao.findByP2MApplicationId(r.p2mApplicationId)
      //println(result)
      this.sender() ! result

    case i : GetP2MInsurance =>

      val result = iDao.findByP2MApplicationId(i.p2mApplicationId)
      //println(result)
      this.sender() ! result

    case p : GetP2MProductInfo =>

      val result = pDao.findByP2MApplicationId(p.p2mApplicationId)
      //println(result)
      this.sender() ! result

    case as : GetTotalAppliedSkuNumber =>

      val result = p2mDao.findTotalAppliedSkuNumber(as.kcode)
      //println(result)
      this.sender() ! result.toString

    case oss : GetTotalOnSaleSkuNumber =>

      val result = p2mDao.findTotalOnSaleSkuNumber(oss.kcode)
      this.sender() ! result.toString

    case cbc : GetCanBeChangedApplications =>

      //todo arthur
      p2mDao.findCanBeChanged(cbc.kcode)

    case message: Any =>
      log.info(s"P2MQueryHandler: received unexpected: $message")

  }



}
