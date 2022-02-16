package com.kindminds.drs.data.pipelines.core.actor



import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.pubsub.DistributedPubSub
import com.kindminds.drs.api.data.access.rdb.ec.AutoProcessECReportDao
import com.kindminds.drs.api.data.transfer.ec.{DownloadECReport, ImportECReportToDRSDB, ImportECReportToHbase, SaveECReportToHDFS, StartProcessECReport, ValidateECReport}
import com.kindminds.drs.data.pipelines.api.message._


import org.springframework.context.annotation.AnnotationConfigApplicationContext



object ProcessAmzRptFailSubscriber {

  def props(springCtx: AnnotationConfigApplicationContext): Props =
    Props(new ProcessAmzRptFailSubscriber(springCtx))

}

class ProcessAmzRptFailSubscriber(springCtx: AnnotationConfigApplicationContext)
  extends Actor with ActorLogging {

  import akka.cluster.pubsub.DistributedPubSubMediator.Put
  val mediator = DistributedPubSub(context.system).mediator
  // register to the path
  mediator ! Put(self)

  val dao =   springCtx.getBean(classOf[AutoProcessECReportDao])
    .asInstanceOf[AutoProcessECReportDao]


  def receive = {

    case s : ProcessReportStartFailed =>

      dao.save( new StartProcessECReport(s.marketPlaceId, s.scheduledDate,s.reportType.id , false))

    case d : ReportDownloadFailed =>

      dao.save(new DownloadECReport(d.marketPlaceId,d.scheduledDate ,d.reportType.id , false))

    case v : ReportValidateFailed =>

      dao.save(new ValidateECReport(v.marketPlaceId,v.scheduledDate ,v.reportType.id , false))

    case sh : ReportSaveToHDFSFailed =>

      dao.save(new SaveECReportToHDFS(sh.marketPlaceId,sh.scheduledDate ,sh.reportType.id , false))

    case itd : ReportImportToDRSDBFailed =>

      dao.save(new ImportECReportToDRSDB(itd.marketPlaceId,itd.scheduledDate ,itd.reportType.id , false))

    case ith : ReportImportToHBASEFailed =>

      dao.save(new ImportECReportToHbase(ith.marketPlaceId,ith.scheduledDate ,ith.reportType.id , false))

    case message: Any =>
      log.info(s"ProcessAmzRptFailSubscriber: received unexpected: $message")
  }




}


