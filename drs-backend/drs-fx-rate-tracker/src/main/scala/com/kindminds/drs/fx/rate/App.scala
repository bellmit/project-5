package com.kindminds.drs.fx.rate

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

import akka.actor.ActorSystem
import com.kindminds.drs.fx.rate.constant.InterBankRate
import com.kindminds.drs.service.util.{DrsBizCoreAnnotationConfig4DP, DrsBizCoreAnnotationConfigNoDB}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import org.springframework.context.annotation.AnnotationConfigApplicationContext

object App {

  def main(args: Array[String]): Unit = {


    val system = ActorSystem("drsFxRate")
    val scheduler = QuartzSchedulerExtension(system)

    //todo here
    val springCtx: AnnotationConfigApplicationContext = null
     // new AnnotationConfigApplicationContext(classOf[DrsBizCoreAnnotationConfig4DP])



    val w = system.actorOf(FxRateWorker.props(springCtx) , "fxRateWorker")


    //w ! StartScrapeDailyFxRate()

    //scheduler.schedule("cronFXAM" , w , StartScrapeDailyFxRate())
    //scheduler.schedule("cronFXPM" , w , StartScrapeDailyFxRate())





  }

}
