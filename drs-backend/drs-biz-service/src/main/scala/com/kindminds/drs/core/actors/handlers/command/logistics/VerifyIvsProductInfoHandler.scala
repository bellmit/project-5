package com.kindminds.drs.core.actors.handlers.command.logistics

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.message.command.ivs.{ConfirmIvsProductVerifyInfo, InitVerifyIvsProductInfo,
  RejectIvsProductVerifyInfo, UpdateIvsStatus}
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus
import com.kindminds.drs.core.RegisterCommandHandler

import com.kindminds.drs.core.biz.repo.logistics.{IvsLineitemRepoImpl, IvsRepoImpl}




object VerifyIvsProductInfoHandler {

  def props(drsCmdBus: ActorRef ): Props =
    Props(new VerifyIvsProductInfoHandler(drsCmdBus))

}

class VerifyIvsProductInfoHandler(drsCmdBus: ActorRef ) extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name, classOf[InitVerifyIvsProductInfo].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[ConfirmIvsProductVerifyInfo].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[RejectIvsProductVerifyInfo].getName, self)


  override def receive: Receive = {

    //todo arhtur ivs line query ken and ivs name
    case s : InitVerifyIvsProductInfo =>

      val repo = new IvsLineitemRepoImpl()
      val opIvsLineitem = repo.findByName(s.ivsNumber,s.boxNum,s.mixedBoxLineSeq)

      if(opIvsLineitem.isPresent()){
        val item = opIvsLineitem.get()
        item.verifyProduct()

        repo.edit(item)

        drsCmdBus ! UpdateIvsStatus(s.ivsNumber , None,
          item.getSkuCode, item.getBoxNum)

      }

    case c : ConfirmIvsProductVerifyInfo =>

      val repo = new IvsLineitemRepoImpl()

      val opIvsLineitem = repo.findByName(c.ivsNumber,c.boxNum,c.mixedBoxLineSeq)

      if(opIvsLineitem.isPresent()){
        val item = opIvsLineitem.get()
        item.updateDocRequirement(c.docReq)
        item.confirmProduct()

        repo.edit(item)

        drsCmdBus ! UpdateIvsStatus(c.ivsNumber , None,
          item.getSkuCode, item.getBoxNum)
      }


    case r : RejectIvsProductVerifyInfo =>

      val repo = new IvsLineitemRepoImpl()
      val opIvsLineitem = repo.findByName(r.ivsNumber,r.boxNum,r.mixedBoxLineSeq)

      if(opIvsLineitem.isPresent()){
        val item = opIvsLineitem.get()

        item.updateDocRequirement(r.docReq)
        item.rejectProduct()

        repo.edit(item)

        drsCmdBus ! UpdateIvsStatus(r.ivsNumber , Some(ShipmentStatus.SHPT_AWAIT_PLAN),
          item.getSkuCode, item.getBoxNum)
      }



    case message: Any =>
      log.info(s"VerifyIvsProductInfoHandler: received unexpected: $message")

  }



}
