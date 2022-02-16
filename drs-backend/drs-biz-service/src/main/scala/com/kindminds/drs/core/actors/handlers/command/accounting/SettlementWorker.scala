package com.kindminds.drs.core.actors.handlers.command.accounting

import java.util
import java.util.concurrent.TimeUnit
import java.util.{ArrayList, Arrays, Collections, Comparator, Date, List, Set, TreeSet}

import akka.actor.{Actor, ActorLogging, ActorPath, ActorRef, Props}
import akka.routing.RoundRobinPool
import akka.util.Timeout
import com.kindminds.drs.api.message.command.CommitSettlement
import com.kindminds.drs.api.message.command.Settlement.{CollectMarketSideTransactions, CompleteCollectingSupplierMarketSideTransactions, CompleteProcessingSupplierMarketSideTransactions, ProcessMarketSideTransactions, StartCollectingMarketSideTransactions, StartProcessingMarketSideTransactions}
import com.kindminds.drs.api.message.event.{MarketSideTransactionsCollected, MarketSideTransactionsProcessed, SettlementCommitted}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.{Country, Marketplace}
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.api.data.access.rdb.CompanyDao
import com.kindminds.drs.enums.Settlement
import com.kindminds.drs.service.helper.MarketSideTransactionHelper
import com.kindminds.drs.service.util.MailUtil
import com.kindminds.drs.util.SettlementPeriodHelper
import com.kindminds.drs.api.v1.model.accounting.{DrsTransaction, NonProcessedMarketSideTransaction, SettlementPeriod}
import com.kindminds.drs.v1.model.impl.accounting.{NonProcessedMarketSideTransactionImpl, SettlementPeriodImpl}
import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction
import com.kindminds.drs.api.v2.biz.domain.model.inventory.SkuShipmentStockAllocator
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo
import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.{Assert, StopWatch, StringUtils}




import scala.concurrent.Await


object SettlementWorker {

  def props(drsCmdBus: ActorRef , drsEventBus: DrsEventBus): Props =
    Props(new SettlementWorker(drsCmdBus,drsEventBus))

}


class SettlementWorker(drsCmdBus: ActorRef , drsEventBus: DrsEventBus ) extends Actor with ActorLogging {

  val name = self.path.name
  drsCmdBus ! RegisterCommandHandler(name, classOf[StartCollectingMarketSideTransactions].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[CompleteCollectingSupplierMarketSideTransactions].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[StartProcessingMarketSideTransactions].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[CompleteProcessingSupplierMarketSideTransactions].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[CommitSettlement].getName, self)


  val companyDao = BizCoreCtx.get().getBean(classOf[CompanyDao])
    .asInstanceOf[CompanyDao]


  implicit val resolveTimeout = Timeout(5 ,TimeUnit.SECONDS)
  val path = ActorPath.fromString("akka://drs@localhost:5001/user/settlementRouter")
  val settlementRouter: ActorRef = Await.result(context.system.actorSelection(path).resolveOne(),
    resolveTimeout.duration)

  var kcodeList : List[String]= null;
  var processMTkcodeList : List[String]= null;
  var index = 2
  var collectingMarketSideTransactionsDone = false
  var processMarketSideTransactionsDone = false
  var pMTindex = 2
  var marketSideTransactionsCollectedResult:Int =0
  var marketSideTransactionsProcessedResult:Int =0

  override def receive: Receive = {

    case s : StartCollectingMarketSideTransactions =>

      collectingMarketSideTransactionsDone = false

      kcodeList  = companyDao.queryActivatedSupplierKcodeList()
      settlementRouter ! CollectMarketSideTransactions(s.settlementPeriodId,kcodeList.get(0),0)
      settlementRouter ! CollectMarketSideTransactions(s.settlementPeriodId,kcodeList.get(1),1)
      settlementRouter ! CollectMarketSideTransactions(s.settlementPeriodId,kcodeList.get(2),2)


    case c : CompleteCollectingSupplierMarketSideTransactions =>

      marketSideTransactionsCollectedResult += c.result

      if(!collectingMarketSideTransactionsDone)  index += 1

      if(index < kcodeList.size() && !collectingMarketSideTransactionsDone){
        //Thread.sleep(5000)
        settlementRouter ! CollectMarketSideTransactions(c.settlementPeriodId,kcodeList.get(index),index)
      }else{
        if(c.kcodeIndex == kcodeList.size() -1){
          index = 2
          collectingMarketSideTransactionsDone = true
          drsEventBus.publish(MarketSideTransactionsCollected(marketSideTransactionsCollectedResult.toString))
          marketSideTransactionsCollectedResult = 0
        }

      }


    case spmt : StartProcessingMarketSideTransactions =>

      processMarketSideTransactionsDone = false

      processMTkcodeList  = companyDao.queryActivatedSupplierKcodeList()
      settlementRouter ! ProcessMarketSideTransactions(spmt.settlementPeriodId,processMTkcodeList.get(0),0)
      settlementRouter ! ProcessMarketSideTransactions(spmt.settlementPeriodId,processMTkcodeList.get(1),1)
      settlementRouter ! ProcessMarketSideTransactions(spmt.settlementPeriodId,processMTkcodeList.get(2),2)


    case cp : CompleteProcessingSupplierMarketSideTransactions =>

      marketSideTransactionsProcessedResult += cp.result
      if(!processMarketSideTransactionsDone)  pMTindex += 1

      if(pMTindex < processMTkcodeList.size() && !processMarketSideTransactionsDone){
        //Thread.sleep(5000)
        settlementRouter ! ProcessMarketSideTransactions(cp.settlementPeriodId,
          processMTkcodeList.get(pMTindex),pMTindex)
      }else{

        if(cp.kcodeIndex == processMTkcodeList.size() -1){
          pMTindex = 2
          processMarketSideTransactionsDone = true
          drsEventBus.publish(MarketSideTransactionsProcessed(marketSideTransactionsProcessedResult.toString))
          marketSideTransactionsProcessedResult = 0
        }

      }



    case cm : CommitSettlement =>

      //todo merge uco
      drsEventBus.publish(SettlementCommitted())


  }


}
