package com.kindminds.drs.core.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.kindminds.drs.api.message.command.Settlement.ProcessMarketSideTransactionById
import com.kindminds.drs.core.actors.handlers.command.accounting.SettlementCmdHandler
import com.kindminds.drs.core.{DrsCmdBus, DrsEventBus, DrsQueryBus}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class TestNonProcess  extends TestKit(ActorSystem("system"))
  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll{

  val probe = TestProbe()
  val drsQueryBus = TestActorRef.create(system, Props.create(classOf[DrsQueryBus]), "DrsQueryBus")
  val drsCmdBus = TestActorRef.create(system, Props.create(classOf[DrsCmdBus]), "DrsCmdBus")
  val drsEventBus = new DrsEventBus


  val  settlement = TestActorRef(SettlementCmdHandler.props(drsCmdBus, drsEventBus))

  val nonProcessIds =Array(151438 , 151439 ,150537    ,151442    ,150551    ,150557    ,150558    ,150561    ,150562    ,150563
    ,150569    ,151446    ,151448    ,150603    ,150631    ,150632    ,150636    ,150641
    ,151457    ,150651    ,151463    ,150668    ,150672    ,150676    ,150678    ,150687
    ,150689    ,150692    ,150695    ,150698    ,150700    ,150703    ,150704    ,150711
    ,151474    ,150715    ,150716    ,150719    ,150721    ,150722    ,150725    ,150728
    ,150729    ,150730    ,150732    ,151484    ,150736    ,150737    ,150739    ,150740
    ,150743    ,150744    ,150758    ,150759    ,150760    ,150761    ,150771    ,150774
    ,150780    ,150781    ,150783    ,150784    ,150789    ,150791    ,150792    ,150795
    ,151498    ,150808    ,150806    ,151502    ,150811    ,150812    ,150817    ,150819
    ,150820    ,150821    ,150828    ,150830    ,150838    ,150848    ,150854    ,150856
    ,151513    ,150870    ,150876    ,150880    ,150882    ,150886    ,150926    ,150930
    ,150938    ,150952    ,150954    ,150956    ,150960)

  "Actor" must {

    nonProcessIds.foreach(x =>{
      within(30000 millis) {

        //  BizCoreCtx.get

        //138853
        // settlement ! ProcessMarketSideTransactionById("163", 136746)

        settlement ! ProcessMarketSideTransactionById("172", x)


      }
    })

  }

}
