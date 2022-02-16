package com.kindminds.drs.core.actors.handlers.command.logistics

import java.util

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.AccountManager
import com.kindminds.drs.api.message.command.ivs.UpdateIvsStatus
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus
import com.kindminds.drs.core.RegisterCommandHandler
import com.kindminds.drs.core.biz.repo.logistics.IvsRepoImpl
import com.kindminds.drs.api.data.access.rdb.CompanyDao
import com.kindminds.drs.service.util.MailUtil
import com.typesafe.config.{Config, ConfigFactory}


object IvsHandler {

  def props(drsCmdBus: ActorRef ): Props =
    Props(new IvsHandler(drsCmdBus))

}

class IvsHandler(drsCmdBus: ActorRef ) extends Actor with ActorLogging {

  val name = self.path.name

  private val companyDao = BizCoreCtx.get().getBean(classOf[CompanyDao])
    .asInstanceOf[CompanyDao]

  private val mailUtil = BizCoreCtx.get().getBean(classOf[MailUtil])
    .asInstanceOf[MailUtil]

  drsCmdBus ! RegisterCommandHandler(name, classOf[UpdateIvsStatus].getName, self)

  private val logistics = "logistics@drs.network"
  private val emailTest = "robert.lee@drs.network"
  private val MAIL_ADDRESS_NOREPLY = "DRS.network <drs-noreply@tw.drs.network>"

  private val config = ConfigFactory.load("application.conf")


  override def receive: Receive = {

    case u : UpdateIvsStatus =>

      val repo = new IvsRepoImpl()
      val opIvs = repo.findByName(u.ivsNumber)
      opIvs.get().getSellerCompanyKcode

      if(opIvs.isPresent()){
        val ivs = opIvs.get()

        if(u.status.isEmpty){
          if (ivs.isAllConfirmed()) {
            ivs.changeStatus(ShipmentStatus.SHPT_FC_BOOKING_CONFIRM)
            repo.edit(ivs)

          } else if (ivs.isAllInitiallyVerified()) {
            if (ivs.getStatus != ShipmentStatus.SHPT_INITIAL_VERIFIED) {
              ivs.changeStatus(ShipmentStatus.SHPT_INITIAL_VERIFIED)
              repo.edit(ivs)
              sendInitiallyVerifyEmail(ivs.getSellerCompanyKcode, u.ivsNumber)
            }

          }
        }else{
          //reject path
          ivs.changeStatus(u.status.get)
          repo.edit(ivs)
          if (u.status.get == ShipmentStatus.SHPT_AWAIT_PLAN) {
            sendRejectEmail(ivs.getSellerCompanyKcode, u.ivsNumber, u.sku, u.boxNum)
          }
        }

      }


    case message: Any =>
      log.info(s"VerifyIvsProductInfoHandler: received unexpected: $message")

  }


  def sendRejectEmail(companyCode:String, ivsName:String, sku:String, boxNum:Int): Unit = {
    var to = AccountManager.getEmail(companyDao.queryAccountManager(companyCode))
//    //println(to)
//    to = emailTest
    val subject = getRejectEmailSubject(ivsName)
    val body = getEmailBody(subject, ivsName, sku, boxNum)

    sendEmail(to, subject, body)
  }


  def sendInitiallyVerifyEmail(companyCode:String, ivsName:String) : Unit = {
    var to = logistics
//    //println(to)
//    to = emailTest
    val subject = getIntiallyVerifyEmailSubject(ivsName)
    var body = getEmailBody(subject, ivsName, "", 0)

    sendEmail(to, subject, body)
  }


  def sendEmail(to:String, subject:String, body:String): Unit = {
    val sendNotify = config.getBoolean("drs.sendNotify")

    if (sendNotify) {
      mailUtil.SendMime(to.split(","), MAIL_ADDRESS_NOREPLY, subject, body)
    }
  }


  def getRejectEmailSubject(shipmentName: String): String = {
    "Shipment " + shipmentName + " has been changed from \"Initially verified\" to \"Awaiting Plan\" "
  }

  def getIntiallyVerifyEmailSubject(shipmentName: String): String = {
    "Shipment " + shipmentName + " is initially verified "
  }


  def getEmailBody(emailSubject: String, shipmentName: String, sku:String, boxNum:Int): String = {
    emailSubject + getEmailSkuBoxNum(sku, boxNum) + getEmailLink(shipmentName)
  }

  def getEmailSkuBoxNum(sku: String, boxNum:Int):String = {

    var skuBoxNum = "<br>"
    if (!sku.isEmpty) {
      skuBoxNum = "Line item: " + sku + ", box number: " + boxNum + " <br>"
    }

    skuBoxNum
  }

  def getEmailLink(shipmentName: String): String = {
    "\n<br><a href='https://access.drs.network/InventoryShipments/" + shipmentName + "'> Link to " + shipmentName + "</a><br><br> "
  }

}
