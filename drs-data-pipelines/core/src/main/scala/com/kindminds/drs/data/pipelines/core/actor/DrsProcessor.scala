package com.kindminds.drs.data.pipelines.core.actor



import akka.actor.{Actor, ActorLogging, Props}
import com.kindminds.drs.data.pipelines.api.message._
import com.kindminds.drs.api.usecase.CalculateMonthlyStorageFeeUco
import org.springframework.context.annotation.AnnotationConfigApplicationContext



object DrsProcessor {

  def props(springCtx: AnnotationConfigApplicationContext): Props = Props(new DrsProcessor(springCtx))

}



class DrsProcessor(springCtx: AnnotationConfigApplicationContext) extends Actor with ActorLogging {



  def receive = {

    case msf : CalcMonthlyStorageFeeRpt =>
      processMonthlyStorageFee(msf)
  }



  def processMonthlyStorageFee(msf : CalcMonthlyStorageFeeRpt): Unit =
  {
    try{


      log.info("{} Calc Monthly Storage Fee report  " , msf.filePath)

      val uco = springCtx.getBean(classOf[CalculateMonthlyStorageFeeUco])
        .asInstanceOf[CalculateMonthlyStorageFeeUco]

      val pathAry: Array[String] = msf.filePath.split("/")
      val fileAry = pathAry(pathAry.length-1).split("_")

      val m = fileAry(fileAry.length-2)

      uco.calculate(fileAry(fileAry.length-1).split("\\.")(0),
        if(m.length == 1) "0"+ m else m)

      log.info("{} Calc Monthly Storage Fee report  is done " , msf.filePath)


    }
    catch {
      case  e : Exception => {
        //e.printStackTrace()

        if(e.getCause != null){
          log.error( e.getCause.getMessage)
        }else {
          log.error(e.toString)
        }

        //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")

        //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))
      }
    }
  }




}


