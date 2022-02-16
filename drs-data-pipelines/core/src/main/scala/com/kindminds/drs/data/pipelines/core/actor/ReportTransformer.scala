package com.kindminds.drs.data.pipelines.core.actor

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSelection, Props}
import akka.cluster.pubsub.DistributedPubSub
import akka.util.Timeout
import com.kindminds.drs.data.pipelines.api.message._

import scala.concurrent.Await
import scala.concurrent.duration._

object ReportTransformer {

  def props(): Props = Props(new ReportTransformer())

}

class ReportTransformer extends Actor with ActorLogging {

  import akka.cluster.pubsub.DistributedPubSubMediator.Put
  val mediator = DistributedPubSub(context.system).mediator
  // register to the path
  mediator ! Put(self)



  import context.dispatcher

  implicit val resolveTimeout = Timeout(5 seconds)

  val drsImportProcessor: ActorRef = Await.result(context.system.actorSelection("user/drsImportProcessor").resolveOne(),
    resolveTimeout.duration)


  def receive = {

    case cp : TransformCampaignPerformanceRpt =>

      //context.system.actorSelection("user/cpHandler") ! cp


      context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,
        ImportCampaignPerformanceRpt(cp.scheduledDate , cp.filePath.getOrElse("") , cp.marketPlace,
          cp.modificationTime))

    case st : TransformSearchTermRpt =>

      context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,
        ImportSearchTermRpt(st.scheduledDate , st.filePath.getOrElse("") ,st.marketPlace ,
          st.modificationTime))

    case rawSt : TransformSearchTermRptRawData =>

     //context.system.actorSelection("user/stHandler")  ! rawSt
      
    case pt : TransformTrafficRpt =>
      context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,
        ImportTrafficRpt(pt.scheduledDate , pt.filePath.getOrElse("") ,
        doMarketPlace(pt.filePath.getOrElse("")) , pt.modificationTime))
    case bpt : TransformBiWeeklyTrafficRpt =>
      context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,  ImportBiWeeklyTrafficRpt(bpt.scheduledDate , bpt.filePath.getOrElse("") ,
        doMarketPlace(bpt.filePath.getOrElse("")) , bpt.modificationTime))
    case as : TransformAllStatementsRpt =>
      context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,  ImportAllStatementsRpt(as.scheduledDate , as.filePath.getOrElse("") ,
        doMarketPlace(as.filePath.getOrElse("")) , as.modificationTime))
    case cr : TransformCustomerReturnRpt =>
      context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,  ImportCustomerReturnRpt(cr.scheduledDate ,cr.filePath.getOrElse("") ,
        doMarketPlace(cr.filePath.getOrElse("")) , cr.modificationTime))

    case msf : TransformMonthlyStorageFeeRpt =>
      context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,  ImportMonthlyStorageFeeRpt(
        msf.scheduledDate , msf.filePath.getOrElse("") , msf.marketPlace , msf.modificationTime))

    case mfi : TransformManageFBAInventoryRpt =>
      context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,  ImportManageFBAInventoryRpt(mfi.scheduledDate,
        mfi.filePath.getOrElse("") ,
        doMarketPlace(mfi.filePath.getOrElse("")) , mfi.modificationTime))

    case ir : TransformInventoryRpt =>
      context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,  ImportInventoryRpt(ir.scheduledDate ,
        ir.filePath.getOrElse("") ,
        doMarketPlace(ir.filePath.getOrElse("")) , ir.modificationTime))
    case pd: TransformPaymentsDateRangeRpt =>
        context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,  ImportPaymentsDateRangeRpt(
          pd.scheduledDate ,pd.filePath.getOrElse("") ,
          doMarketPlace(pd.filePath.getOrElse("")) , pd.modificationTime))
    case hc: TransformHSACampaignRpt =>
      context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,  ImportHSACampaignRpt(
        hc.scheduledDate , hc.filePath.getOrElse("") , hc.marketPlace , hc.modificationTime))
    case hk: TransformHSAKeywordRpt =>
      context.system.scheduler.scheduleOnce(1.minute, drsImportProcessor,
        ImportHSAKeywordRpt(hk.scheduledDate , hk.filePath.getOrElse("") , hk.marketPlace, hk.modificationTime))



  }

  def doMarketPlace(path:String): String ={

    //todo here

    val pathAry: Array[String] = path.split("/")
    val fileAry = pathAry(pathAry.length-1).split("_")
    if(path.contains("HSA"))
      fileAry(2).toLowerCase()
    else if(path.contains("Campaign_SalesNum"))
      fileAry(2).toLowerCase()
    else
      fileAry(1).toLowerCase()
  }

}
