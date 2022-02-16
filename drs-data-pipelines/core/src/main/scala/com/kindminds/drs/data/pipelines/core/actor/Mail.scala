
package com.kindminds.drs.data.pipelines.core.actor


import akka.actor.{Actor, ActorLogging, Props}
import com.kindminds.drs.data.pipelines.api.message.{SendReportFailedMail, SendReportOKMail, SendScrapeAmzRptFailedMail}

import org.springframework.context.annotation.AnnotationConfigApplicationContext

//import com.kindminds.drs.service.util.MailUtil



object Mail {

  def props(springCtx: AnnotationConfigApplicationContext): Props = Props(new Mail(springCtx))

}

class Mail( springCtx: AnnotationConfigApplicationContext) extends Actor with ActorLogging {


  val applicationId = self.path.name


  def receive = {



    case s : SendScrapeAmzRptFailedMail =>

      /*

      val mailAddressNoReply = "DRS.network  <drs-noreply@tw.drs.network>"
      val mails = Seq("arthur.wu@drs.network")
      val x = springCtx.getBean(classOf[MailUtil])

      val mailSender = x.asInstanceOf[MailUtil]


      val subject = s.marketPlace.getOrElse("") + " Amazon report scrape failed"

      mailSender.Send(mails.toArray,mailAddressNoReply,subject,s.message + s.stackTrace)

      */
      /*
    case ok : SendReportOKMail =>

      val mailAddressNoReply = "DRS.network  <drs-noreply@tw.drs.network>"
      val mails = Seq("arthur.wu@drs.network")
      val mailSender = springCtx.getBean(classOf[com.kindminds.drs.service.util.MailUtil])
        .asInstanceOf[com.kindminds.drs.service.util.MailUtil]

      val subject = ok.report + " OK"

      mailSender.Send(mails.toArray,mailAddressNoReply,subject, subject)

    case failed : SendReportFailedMail  =>

      val mailAddressNoReply = "DRS.network  <drs-noreply@tw.drs.network>"
      val mails = Seq("arthur.wu@drs.network")
      val mailSender = springCtx.getBean(classOf[com.kindminds.drs.service.util.MailUtil])
        .asInstanceOf[com.kindminds.drs.service.util.MailUtil]

      val subject = failed.report + " failed"

      mailSender.Send(mails.toArray,mailAddressNoReply,subject, subject)
      */


  }



}