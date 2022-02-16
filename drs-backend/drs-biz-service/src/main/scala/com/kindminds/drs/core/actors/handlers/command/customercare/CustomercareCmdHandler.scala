package com.kindminds.drs.core.actors.handlers.command.customercare

import java.text.SimpleDateFormat

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.command._
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}


object CustomercareCmdHandler {

  def props(drsCmdBus: ActorRef ,  drsEventBus: DrsEventBus): Props =
    Props(new CustomercareCmdHandler(drsCmdBus , drsEventBus))

}

class CustomercareCmdHandler(drsCmdBus: ActorRef, drsEventBus: DrsEventBus ) extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name, classOf[UpdateCustomercareCase].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[SaveCustomercareCase].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[DeleteCustomercareCase].getName, self)

  drsCmdBus ! RegisterCommandHandler(name, classOf[UpdateCustomercareCaseIssue].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[SaveCustomercareCaseIssue].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[DeleteCustomercareCaseIssue].getName, self)



  override def receive: Receive = {

    case u : UpdateCustomercareCaseIssue =>

      //todo merge uco
      drsEventBus.publish(CustomercareCaseIssueUpdated(u.issueId))

    case s : SaveCustomercareCaseIssue =>

      //todo merge uco
      drsEventBus.publish(CustomercareCaseIssueSaved(s.issueId))

    case d : DeleteCustomercareCaseIssue =>

      //todo merge uco
      drsEventBus.publish(CustomercareCaseIssueDeleted(d.issueId))



    case u : UpdateCustomercareCase =>

      //todo merge uco
      //println("UpdateUpdateUpdate")
      drsEventBus.publish(CustomercareCaseUpdated(u.caseId))

    case s : SaveCustomercareCase =>

      //todo merge uco
      drsEventBus.publish(CustomercareCaseSaved(s.caseId))

    case d : DeleteCustomercareCase =>

      //todo merge uco
      drsEventBus.publish(CustomercareCaseDeleted(d.caseId))


    case message: Any =>
      log.info(s"CustomercareCmdHandler: received unexpected: $message")
  }



}
