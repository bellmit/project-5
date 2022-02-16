package com.kindminds.drs.core.actors.handlers.command.process

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.{Criteria, DailySalesQueryField, Filter, Notification, UserQueryField, UserTaskType}
import com.kindminds.drs.api.message.command.notification.SendNotification
import com.kindminds.drs.api.message.command.p2m._
import com.kindminds.drs.api.message.event.{P2mApplicationCreated, P2mApplicationSubmitted}
import com.kindminds.drs.api.message.process.CreateUserTask
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.actors.handlers.command.p2m.P2MCmdHandler
import com.kindminds.drs.core.biz.process.{UserTaskImpl, UserTaskRepo}
import com.kindminds.drs.core.biz.repo.UserRepoImpl
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.api.data.access.nosql.mongo.p2m._
import com.kindminds.drs.api.data.access.nosql.mongo.userTask.UserTaskDao
import com.kindminds.drs.persist.data.access.nosql.mongo.process.UserTaskDaoImpl


object UserTaskCmdHandler {
  def props(drsCmdBus: ActorRef ): Props =
    Props(new UserTaskCmdHandler(drsCmdBus))
}


class UserTaskCmdHandler(drsCmdBus: ActorRef ) extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name, classOf[CreateUserTask].getName, self)


  /*
  val om = new ObjectMapper()//.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
   */

  val userTaskDao = new UserTaskDaoImpl

    override def receive: Receive = {


    case c : CreateUserTask =>

      //todo arthur temp for review p2m
      val repo =  new UserTaskRepo
      val f = new Filter
      f.addCriteria(new Criteria(UserQueryField.role , "DCP_SS_REVIEWER"))
      val userRepo = new UserRepoImpl()
      val userInfoList = userRepo.find(f)

      var topic = ""
      if(c.userTaskType == UserTaskType.ReviewP2MApplication){
        topic = c.kcode + " submitted  P2M Application"
      }

      if(userInfoList != null){
        userInfoList.forEach(u=>{
          val ut = new UserTaskImpl(c.userTaskType , c.refId , c.kcode)
          ut.assign(u)
          repo.save(ut)
          drsCmdBus ! SendNotification(topic,
            ut.generateNotification(),ut.getTaskUrl,u.getUserId)
        })
      }

    case message: Any =>
      log.info(s"UserTaskCmdHandler: received unexpected: $message")

  }



}