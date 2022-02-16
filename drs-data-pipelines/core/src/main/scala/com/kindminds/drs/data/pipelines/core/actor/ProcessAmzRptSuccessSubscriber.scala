package com.kindminds.drs.data.pipelines.core.actor



import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.pubsub.DistributedPubSub
import com.kindminds.drs.api.data.access.rdb.ec.AutoProcessECReportDao
import com.kindminds.drs.api.data.transfer.ec.{DownloadECReport, ImportECReportToDRSDB, ImportECReportToHbase, SaveECReportToHDFS, StartProcessECReport, ValidateECReport}
import com.kindminds.drs.data.pipelines.api.message.{ProcessReportStarted, ReportDownloaded, ReportImportedToDRSDB, ReportImportedToHBASE, ReportSavedToHDFS, ReportValidated}
import org.springframework.context.annotation.AnnotationConfigApplicationContext


object ProcessAmzRptSuccessSubscriber {

  def props(springCtx: AnnotationConfigApplicationContext): Props =
    Props(new ProcessAmzRptSuccessSubscriber(springCtx))

}

class ProcessAmzRptSuccessSubscriber(springCtx: AnnotationConfigApplicationContext) extends Actor with ActorLogging {

  import akka.cluster.pubsub.DistributedPubSubMediator.Put
  val mediator = DistributedPubSub(context.system).mediator
  // register to the path
  mediator ! Put(self)

  val dao =   springCtx.getBean(classOf[AutoProcessECReportDao])
    .asInstanceOf[AutoProcessECReportDao]


  def receive = {

    case s : ProcessReportStarted =>

      dao.save( new StartProcessECReport(s.marketPlaceId, s.scheduledDate,s.reportType.id , true))

    case d : ReportDownloaded =>

      dao.save(new DownloadECReport(d.marketPlaceId,d.scheduledDate ,d.reportType.id , true))

    case v : ReportValidated =>

      dao.save(new ValidateECReport(v.marketPlaceId,v.scheduledDate ,v.reportType.id , true))

    case sh : ReportSavedToHDFS =>

      dao.save(new SaveECReportToHDFS(sh.marketPlaceId,sh.scheduledDate ,sh.reportType.id , true))

    case itd : ReportImportedToDRSDB =>

      dao.save(new ImportECReportToDRSDB(itd.marketPlaceId,itd.scheduledDate ,itd.reportType.id , true))

    case ith : ReportImportedToHBASE =>

      dao.save(new ImportECReportToHbase(ith.marketPlaceId,ith.scheduledDate ,ith.reportType.id , true))


    case message: Any =>
      log.info(s"ProcessAmzRptSuccessSubscriber: received unexpected: $message")
  }




}
