package com.kindminds.drs.core.actors.handlers.event.customercare

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorPath, ActorRef, Props}
import akka.util.Timeout
import com.kindminds.drs.api.message.command.{DeleteESCustomercareCase, DeleteESCustomercareCaseIssue, RefreshESCustomercareCase, RefreshESCustomercareCaseIssue}
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.{MarketingMaterialEditingRequestViewDao, ProductVariationViewDao}

import scala.concurrent.Await
import scala.concurrent.duration.Duration._


object CaseSubscriber {

  def props(): Props =
    Props(new CaseSubscriber())

}

class CaseSubscriber() extends Actor with ActorLogging {


  implicit val resolveTimeout = Timeout(5 ,TimeUnit.SECONDS)


  def receive = {


    case id : CustomercareCaseIssueDeleted =>

      val path = ActorPath.fromString("akka://drs@localhost:5003/user/cccaseIssueES")

      val ccciEs: ActorRef = Await.result(context.system.actorSelection(path).resolveOne(),
        resolveTimeout.duration)

      ccciEs ! DeleteESCustomercareCaseIssue(id.issueId)

      log.info("")


    case iu : CustomercareCaseIssueUpdated =>

      val path = ActorPath.fromString("akka://drs@localhost:5003/user/cccaseIssueES")

      val ccciEs: ActorRef = Await.result(context.system.actorSelection(path).resolveOne(),
        resolveTimeout.duration)

      ccciEs ! RefreshESCustomercareCaseIssue(iu.issueId)

      log.info("")


    case is : CustomercareCaseIssueSaved =>

      val path = ActorPath.fromString("akka://drs@localhost:5003/user/cccaseIssueES")

      val ccciEs: ActorRef = Await.result(context.system.actorSelection(path).resolveOne(),
        resolveTimeout.duration)

      ccciEs ! RefreshESCustomercareCaseIssue(is.issueId)

      log.info("")


    case caseDeleted : CustomercareCaseDeleted =>

      val path = ActorPath.fromString("akka://drs@localhost:5003/user/cccEs")

      val cccEs: ActorRef = Await.result(context.system.actorSelection(path).resolveOne(),
        resolveTimeout.duration)

      cccEs ! DeleteESCustomercareCase(caseDeleted.caseId)

      log.info("")


    case caseUpdated : CustomercareCaseUpdated =>

      val path = ActorPath.fromString("akka://drs@localhost:5003/user/cccEs")

      val cccEs: ActorRef = Await.result(context.system.actorSelection(path).resolveOne(),
        resolveTimeout.duration)

      //println("EventEventEvent")

      cccEs ! RefreshESCustomercareCase(caseUpdated.caseId)

      log.info("")


    case caseSaved : CustomercareCaseSaved =>

      val path = ActorPath.fromString("akka://drs@localhost:5003/user/cccEs")

      val cccEs: ActorRef = Await.result(context.system.actorSelection(path).resolveOne(),
        resolveTimeout.duration)

      cccEs ! RefreshESCustomercareCase(caseSaved.caseId)

      log.info("")

    case message: Any =>
      log.info(s"CaseSubscriber: received unexpected: $message")
  }


}
