package com.kindminds.drs.core

import akka.actor.{Actor, ActorRef}





case class CommandHandler(name: String, handler: ActorRef)
case class QueryHandler(name: String, handler: ActorRef)

case class RegisterCommandHandler(name: String, commandId: String, handler: ActorRef)
case class RegisterQueryHandler(name: String, queryId: String, handler: ActorRef)

case class Status()



class DrsCmdBus() extends Actor {

  //todo need refactoring
  val commandHandlers = scala.collection.mutable.Map[String, Vector[CommandHandler]]()

  var totalRegistered = 0

  def dispatchCommand(command: _root_.com.kindminds.drs.api.message.Command) = {

    if (commandHandlers.contains(command.getClass().getName)) {
      commandHandlers(command.getClass().getName) map { commandHandler =>
        commandHandler.handler.forward(command)
      }
    }

  }

  def receive = {

    case register: RegisterCommandHandler =>
      //println(s"CmdBus: registering: $register")
      registerCommandHandler(register.commandId, register.name, register.handler)
      //notifyStartWhenReady()

    case command: _root_.com.kindminds.drs.api.message.Command =>
      //println(s"CmdBus: dispatching command: $command")
      dispatchCommand(command)

    case status: Status =>
      //println(s"CmdBus: STATUS: has commandHandlers: $commandHandlers")


    case message: Any =>
      //println(s"CmdBus: received unexpected: $message")
  }

  def registerCommandHandler(commandId: String,
                             applicationId: String, handler: ActorRef) = {

    //println(commandId)
    if (!commandHandlers.contains(commandId)) {
      commandHandlers(commandId) = Vector[CommandHandler]()
    }

    commandHandlers(commandId) =
      commandHandlers(commandId) :+ CommandHandler(applicationId, handler)
  }

}

class DrsQueryBus() extends Actor {

  //todo need refactoring
  val queryHandlers = scala.collection.mutable.Map[String, Vector[QueryHandler]]()

  var totalRegistered = 0

  def dispatchQuery(query: _root_.com.kindminds.drs.api.message.Query) = {

    if (queryHandlers.contains(query.getClass().getName)) {
      queryHandlers(query.getClass().getName) map { qHandler =>
        qHandler.handler.forward(query)
      }
    }

  }

  def receive = {

    case register: RegisterQueryHandler =>
      //println(s"QueryBus: registering: $register")
      registerQueryHandler(register.queryId, register.name, register.handler)
    //notifyStartWhenReady()

    case query: _root_.com.kindminds.drs.api.message.Query =>
      //println(s"QueryBus: dispatching query: $query")
      dispatchQuery(query)

    case status: Status =>
      //println(s"QueryBus: STATUS: has queryHandlers: $queryHandlers")


    case message: Any =>
      //println(s"QueryBus: received unexpected: $message")
  }

  def registerQueryHandler(queryId: String, name: String, handler: ActorRef) = {


    if (!queryHandlers.contains(queryId)) {
      queryHandlers(queryId) = Vector[QueryHandler]()
    }

    queryHandlers(queryId) =
      queryHandlers(queryId) :+ QueryHandler(name, handler)
  }

}




