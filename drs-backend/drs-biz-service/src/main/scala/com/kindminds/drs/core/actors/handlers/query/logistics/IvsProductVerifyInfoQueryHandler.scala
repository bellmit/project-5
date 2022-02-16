package com.kindminds.drs.core.actors.handlers.query.logistics

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.query.customer.GetReturns
import com.kindminds.drs.api.message.query.ivs._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.core.actors.handlers.query.customer.CustomerQueryHandler
import com.kindminds.drs.core.biz.logistics.IvsImpl
import com.kindminds.drs.core.biz.repo.logistics.{IvsLineitemRepoImpl, IvsRepoImpl}
import com.kindminds.drs.api.data.access.nosql.mongo.IvsProductDocRequirementDao
import com.kindminds.drs.api.data.transfer.logistics.IvsProductVerifyInfo
import com.kindminds.drs.core.query.logistics.{IvsProductVerificationQueries, IvsQueries}
import com.kindminds.drs.persist.data.access.nosql.mongo.logistics.IvsProductDocRequirementDaoImpl




object IvsProductVerifyInfoQueryHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new IvsProductVerifyInfoQueryHandler(drsQueryBus))

}

class IvsProductVerifyInfoQueryHandler(drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetIvsProductVerifyInfo].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetIvsNumbers].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetBoxNumbers].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetIvsLineitemSku].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetIvsProdDocRequirement].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetIvsLineitemProdVerificationStatus].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetShipmentLineItem].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetExpectedShippingDate].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetIvsShippingCosts].getName, self)

  val om = new ObjectMapper()

  val queries = BizCoreCtx.get().getBean(classOf[
    IvsProductVerificationQueries])
    .asInstanceOf[IvsProductVerificationQueries]

  val ivsQueries = BizCoreCtx.get().getBean(classOf[IvsQueries]).asInstanceOf[IvsQueries]

  //val docReqDao = BizCoreCtx.get().getBean(classOf[IvsProductDocRequirementDao]).asInstanceOf[IvsProductDocRequirementDao]
  val docReqDao = new IvsProductDocRequirementDaoImpl()


  override def receive: Receive = {

    case b : GetBoxNumbers =>

      val result = ivsQueries.queryIvsLineitemBoxNum(b.ivsNumber,b.sku)

      val s = om.writeValueAsString(result)

      this.sender() ! s

    case sku : GetIvsLineitemSku =>

      val result = ivsQueries.queryIvsLineitemSku(sku.ivsNumber)

      val s = om.writeValueAsString(result)

      this.sender() ! s

    case in : GetIvsNumbers =>

      val result = ivsQueries.queryIvsNumbers(in.kcode,0,0)

      val s = om.writeValueAsString(result)

      this.sender() ! s

    case r : GetIvsProductVerifyInfo =>

     val result: IvsProductVerifyInfo =  queries.queryProductVerifyInfo(r.sku,r.destination)

      val repo = new IvsRepoImpl()
      val opIvs = repo.findByName(r.ivsName)

      //todo  need refactor
      if(opIvs.isPresent){

        val ivs = opIvs.get().asInstanceOf[IvsImpl]

        val countryCode = ivs.checkForEUdestination(r.destination)
        val allCostData = ivs.getShippingCostData(countryCode)
//        result.setInventoryPlacementFee(ivs.getInventoryPlacementFee(countryCode, ivs.getLineItems, allCostData))

        ivs.getLineItems.forEach(item=>{

          if(item.getSkuCode == r.sku){

            val costData = ivs.getShippingCostDataPerLine(countryCode, item)
            result.setInventoryPlacementFee(ivs.getUnitPlacmentFee(countryCode, item, costData))

            result.setBoxCbm(ivs.calculateBoxCBM(item))
            result.setDangerousGoods(ivs.isDangerousGoods(item.getProductBaseCode))

            if(costData != null){
              result.setOversized(ivs.isOversize(costData))
            }


            val qty = new java.math.BigDecimal(item.getQuantity)
            result.setQuantity(qty.intValue())
            result.setFcaTotal(result.getFcaPrice.multiply(new java.math.BigDecimal(result.getQuantity)))
          }
        })
      }


      val s = om.writeValueAsString(result)

      this.sender() ! s

    case r : GetIvsProdDocRequirement =>

      val result =  docReqDao.find(r.ivsNumber,r.boxNum,r.mixedBoxLineSeq)

      val s = om.writeValueAsString(result)

      this.sender() ! s

    case vp : GetIvsLineitemProdVerificationStatus =>

      val result =  queries.queryLineitemProdVerificationStatus(vp.ivsNumber,vp.boxNum,vp.mixedBoxLineSeq)

      var data = "{\"status\":\""
      if(result != null && result != "")
        data = data + result
      else
        data = data + "New"

      this.sender() !  data  +"\"}"


    case sli : GetShipmentLineItem =>

      val result = ivsQueries.queryShipmentLineItem(sli.ivsName, sli.boxNum ,sli.mixedBoxLineSeq)

      val s = om.writeValueAsString(result)

      this.sender() ! s

    case d : GetExpectedShippingDate =>

      val result = ivsQueries.query(d.ivsName)
      this.sender() ! "{\"shippingDate\":\""+ result.getExpectedExportDate +"\"}"

    case sc : GetIvsShippingCosts =>
      val ivs = new IvsImpl()

      val shippingCostString = ivs.getShippingCosts(sc.ivsName)

      this.sender() ! shippingCostString
  }

}