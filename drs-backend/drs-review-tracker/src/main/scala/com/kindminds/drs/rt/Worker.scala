package com.kindminds.drs.rt

import java.util

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.viewProductMarketplaceInfo.GetLiveAmazonAsins
import com.kindminds.drs.api.v1.model.product.AmazonAsin
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxOptions}
import org.springframework.context.annotation.AnnotationConfigApplicationContext

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success

object Worker {

  def props(drsCmdBus: ActorRef , drsQueryBus: ActorRef , springCtx: AnnotationConfigApplicationContext): Props =
    Props(new Worker(drsCmdBus ,drsQueryBus ,springCtx))

}

class Worker(drsCmdBus: ActorRef , drsQueryBus: ActorRef ,springCtx: AnnotationConfigApplicationContext) extends Actor with ActorLogging {

  implicit val timeout = Timeout(60 seconds)
  implicit val ec =  scala.concurrent.ExecutionContext.global

  val name = self.path.name
  var count = 0
  //var index = 4
  var index = 1

  drsCmdBus ! RegisterCommandHandler(name,classOf[Complete].getName ,self)

  val router2: ActorRef = context.system.actorOf(new RoundRobinPool(5).props(AmazonReviewScraper.props(drsCmdBus,
    springCtx)), "router3")

  var asinList : java.util.List[AmazonAsin] = null

  def receive = {

    case st : Start =>

      //println("Start")
      val result: Future[Any] = drsQueryBus ? GetLiveAmazonAsins()
      //println("Start")

      result.onComplete {

        case Success(amazonAsins) => {

          val mapper = new ObjectMapper
          val amazonAsinsList: util.List[AmazonAsin] = mapper.readValue(amazonAsins.toString,
            new TypeReference[util.List[AmazonAsin]]() {})

          //println("Asin Size => " + amazonAsinsList.size())

          if(amazonAsinsList.size() > 0){
            asinList = amazonAsinsList
            //println("asinList size => " + asinList.size())

            /*
            asinList.forEach(x=>{
              //println(x.getAsin)
              if(x.getAsin() == "B07FSXGPT5"){
                //println("AAAAAAAAAAAAAAAAA")
                router2 ! ScrapeAmazonReview(x , this.createDriver , this.createDriver)
              }
            }
            )*/



            /*
           if(asinList.get(0).getAsin == "B077XCRN48")
            if(asinList.get(1).getAsin == "B077XCRN48")

*/

            router2 ! ScrapeAmazonReview(asinList.get(0) , this.createDriver , this.createDriver)
            router2 ! ScrapeAmazonReview(asinList.get(1) , this.createDriver, this.createDriver)


            //router2 ! ScrapeAmazonReview(asinList.get(2))
            //router2 ! ScrapeAmazonReview(asinList.get(3))
            //router2 ! ScrapeAmazonReview(asinList.get(4))

            /*
            router2 ! ScrapeAmazonReview(st.amazonAsinsList.get(2))
            router2 ! ScrapeAmazonReview(st.amazonAsinsList.get(3))
            router2 ! ScrapeAmazonReview(st.amazonAsinsList.get(4))
            router2 ! ScrapeAmazonReview(st.amazonAsinsList.get(5))
            router2 ! ScrapeAmazonReview(st.amazonAsinsList.get(6))
            router2 ! ScrapeAmazonReview(st.amazonAsinsList.get(7))
            router2 ! ScrapeAmazonReview(st.amazonAsinsList.get(8))
            router2 ! ScrapeAmazonReview(st.amazonAsinsList.get(9))
            router2 ! ScrapeAmazonReview(st.amazonAsinsList.get(10))
            router2 ! ScrapeAmazonReview(st.amazonAsinsList.get(11))
            */


          }


        }
      }


    case s : Complete =>

      count += 1
      index += 1

      if(index < asinList.size()){
        Thread.sleep(5000)
        router2 ! ScrapeAmazonReview(asinList.get(index) , s.masterDriver , s.itemDriver)
      }else{
        index = 1
        count = 0
        if(s.masterDriver != null)s.masterDriver.quit()
        if(s.itemDriver!=null)s.itemDriver.quit()
      }

      //println("============================================================================")
      //println("Finish processing " + count)
      //println("Finish processing " + count)
      //println("Finish processing " + count)
      //println("Finish processing " + count)
      //println("Finish processing " + count)
      //println("============================================================================")
      log.info("============================================================================")
      log.info("Finish processing " + count)
      log.info("Finish processing " + count)
      log.info("Finish processing " + count)
      log.info("Finish processing " + count)
      log.info("Finish processing " + count)
      log.info("============================================================================")

    case message: Any =>
      log.info(s"OrderHandler: received unexpected: $message")
  }

  private def createDriver = {
    val options = new FirefoxOptions
    //options.setHeadless(true);
    var driver : WebDriver = null
    try
      driver = new FirefoxDriver(options)
    catch {
      case ex: Exception =>
        log.error("Create Firefox driver Error!", ex.getCause)
        ex.printStackTrace()
    }
    driver
  }

}

