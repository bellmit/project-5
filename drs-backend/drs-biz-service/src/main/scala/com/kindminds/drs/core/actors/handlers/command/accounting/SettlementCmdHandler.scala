package com.kindminds.drs.core.actors.handlers.command.accounting

import java.util
import java.util.concurrent.TimeUnit
import java.util._

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.Marketplace
import com.kindminds.drs.api.message.command.Settlement._
import com.kindminds.drs.api.message.command._
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.biz.service.util.BizCoreCtx

import com.kindminds.drs.core.biz.settlement.{AbstractMarketSideTransaction, MarketSideTransactionRepoImpl, MarketSideTransactionType}
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.api.data.access.usecase.accounting.ProcessMarketSideTransactionDao
import com.kindminds.drs.enums.Settlement
import com.kindminds.drs.service.helper.MarketSideTransactionHelper
import com.kindminds.drs.service.util.MailUtil
import com.kindminds.drs.util.SettlementPeriodHelper
import com.kindminds.drs.api.v1.model.accounting.{DrsTransaction, NonProcessedMarketSideTransaction, SettlementPeriod}
import com.kindminds.drs.v1.model.impl.accounting.{NonProcessedMarketSideTransactionImpl, SettlementPeriodImpl}
import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction
import com.kindminds.drs.api.v2.biz.domain.model.inventory.SkuShipmentStockAllocator
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo
import com.kindminds.drs.api.v2.biz.domain.model.settlement.MarketTransaction
import org.apache.commons.lang.exception.ExceptionUtils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.{Assert, StopWatch, StringUtils}



object SettlementCmdHandler {


  def props(drsCmdBus: ActorRef, drsEventBus: DrsEventBus ): Props =
    Props(new SettlementCmdHandler(drsCmdBus, drsEventBus))

}


class SettlementCmdHandler(drsCmdBus: ActorRef, drsEventBus: DrsEventBus) extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name, classOf[CollectMarketSideTransactions].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[ProcessMarketSideTransactions].getName, self)

   private val  dao : ProcessMarketSideTransactionDao =
     BizCoreCtx.get().getBean(classOf[ProcessMarketSideTransactionDao])
     .asInstanceOf[ProcessMarketSideTransactionDao]

  private val stockAllocator : SkuShipmentStockAllocator =  BizCoreCtx.get().getBean(classOf[SkuShipmentStockAllocator])
    .asInstanceOf[SkuShipmentStockAllocator]

  //@Autowired private val helper : MarketSideTransactionHelper = null

   private val settlementPeriodHelper : SettlementPeriodHelper =
    BizCoreCtx.get().getBean(classOf[SettlementPeriodHelper])
      .asInstanceOf[SettlementPeriodHelper]

  //@Autowired private val settlementPeriodRepo : SettlementPeriodDao = null
  private val mailUtil = null

  private val MAIL_ADDRESS_NOREPLY = "DRS.network <drs-noreply@tw.drs.network>"
  private val ACCOUNT_MANAGERS = Array("account.managers@tw.drs.network")
  private val SOFTWARE_ENGINEERS = Array("software.engineering@drs.network")
  private val REQUEST_TOO_MUCH = "Request too much."

  private val ignoreMerchantOrderIdPrefixs = util.Arrays.asList("USTTS", "RUSTTS", "sb", "Rsb", "ebay", "Rebay", "eBay", "ReBay")




  override def receive: Receive = {

    case cmt : CollectMarketSideTransactions =>


      val result = this.collectMarketSideTransactions(cmt.settlementPeriodId,cmt.kcode)

      drsCmdBus ! CompleteCollectingSupplierMarketSideTransactions(cmt.settlementPeriodId , cmt.kcode ,
        cmt.kcodeIndex,result)

    case pmt : ProcessMarketSideTransactions =>

      val result = this.processMarketSideTransactions(pmt.settlementPeriodId,pmt.kcode)
      drsCmdBus ! CompleteProcessingSupplierMarketSideTransactions(pmt.settlementPeriodId , pmt.kcode , pmt.kcodeIndex
        ,result)

    case pmt : ProcessMarketSideTransactionById =>

      val result = this.processMarketSideTransactionById(pmt.settlementPeriodId, pmt.mstId)

    case c : CommitSettlement =>

      //drsCmdBus ! CalculateOriginalAccountsReceivable()

      //todo merge uco
     // drsEventBus.publish(SettlementCommitted())
      //processMarketSideTransactions


    case message: Any =>
      log.info(s"SettlementCmdHandler: received unexpected: $message")

  }



  def getSettlementPeriodList: util.List[SettlementPeriod] = {
    val columnsList: util.List[Array[AnyRef]] = this.dao.querySettlementPeriodList
    val listToReturn: util.List[SettlementPeriod] = new util.ArrayList[SettlementPeriod]
    import scala.collection.JavaConversions._
    for (columns <- columnsList) {
      val id: Int = columns(0).asInstanceOf[Int]
      val start: Date = columns(1).asInstanceOf[Date]
      val `end`: Date = columns(2).asInstanceOf[Date]
      listToReturn.add(new SettlementPeriodImpl(id, start, `end`))
    }
    listToReturn
  }


  def collectMarketSideTransactions(settlementPeriodId: String , kcode:String): Int = {

   // if (!org.springframework.util.StringUtils.hasText(settlementPeriodId)) return "Settlement period error."
    if (!org.springframework.util.StringUtils.hasText(settlementPeriodId)) return 0

    val periodId: Int = settlementPeriodId.toInt
    val dateStartPoint: Date = this.settlementPeriodHelper.getPeriodStart(periodId)
    val dateEndPoint: Date = this.settlementPeriodHelper.getPeriodEnd(periodId)
    val dateLatestDrsSettlementEnd: Date = this.dao.queryLatestStatementPeriodEndDateUtc

    Assert.isTrue(dateStartPoint.compareTo(dateLatestDrsSettlementEnd) == 0, "dateStartPoint.compareTo(dateLatestDrsSettlementEnd)==0")
    Assert.isTrue(dateStartPoint.before(dateEndPoint), "dateStartPoint.before(dateEndPoint)")

    val savedCounts: Int = this.collectAndSaveMarketSideTransactions(dateStartPoint, dateEndPoint,kcode)
    savedCounts
    //savedCounts + " transaction(s) has been generated.\nInterval:" + dateStartPoint + " to " + dateEndPoint

  }

  /*
  def collectMarketSideTransactionsForTest(start: Date, `end`: Date): String = {
    val foundCounts: Int = this.collectAndSaveMarketSideTransactions(start, `end`)
    foundCounts + " transaction(s) has been collected."
  }
   */

  def processMarketSideTransactions(settlementPeriodId: String , kcode : String): Int = {

    //val stopWatch: StopWatch = new StopWatch
    //stopWatch.start()
    var processedCount = 0

    try {

     // if (!org.springframework.util.StringUtils.hasText(settlementPeriodId)) return "Settlement period error."
      if (!org.springframework.util.StringUtils.hasText(settlementPeriodId)) return 0

      val periodId: Int = settlementPeriodId.toInt
      val periodStartPoint: Date = this.settlementPeriodHelper.getPeriodStart(periodId)
      val periodEndPoint: Date = this.settlementPeriodHelper.getPeriodEnd(periodId)
      val dateLatestDrsSettlementEnd: Date = this.dao.queryLatestStatementPeriodEndDateUtc

      Assert.isTrue(periodStartPoint.compareTo(dateLatestDrsSettlementEnd) == 0,
        "periodStartPoint.compareTo(dateLatestDrsSettlementEnd)==0")

      val skusOutOfStock: util.Set[String] = new util.TreeSet[String]

      val repo = new MarketSideTransactionRepoImpl()
      val nonProcessedTransactionList = repo.findNonProcessedTransactions(kcode)



      import scala.collection.JavaConversions._
      for (nonProcessedTransaction <- nonProcessedTransactionList) {
        try {
          Assert.isTrue(!nonProcessedTransaction.getTransactionDate.before(periodStartPoint),
            "!nonProcessedTransaction.getTransactionDate().before(periodStartPoint)")
          Assert.isTrue(nonProcessedTransaction.getTransactionDate.before(periodEndPoint),
            "nonProcessedTransaction.getTransactionDate().before(periodEndPoint)")

          processedCount += this.processTransaction(periodStartPoint, periodEndPoint, nonProcessedTransaction, false)

        } catch {
          case e: Exception =>
            e.printStackTrace()
            if (REQUEST_TOO_MUCH == e.getMessage) skusOutOfStock.add(nonProcessedTransaction.getSku)
            this.dao.deleteMarketSideTransactionException(nonProcessedTransaction.getId)
            this.dao.insertMarketSideTransactionException(nonProcessedTransaction.getId, e.getMessage, ExceptionUtils.getStackTrace(e))
        }
      }

      // stopWatch.stop()
       sendNotificationForRequestedTooMuch(skusOutOfStock)
      // this.generateTimeSpentInfo(stopWatch.getTotalTimeSeconds.toLong)

    }
    catch {

      case e: Exception =>
        e.printStackTrace()
    }


    processedCount
  }


  def processMarketSideTransactionById(settlementPeriodId: String , mstId : Int): Int = {

    var processedCount = 0

    try {

      if (!org.springframework.util.StringUtils.hasText(settlementPeriodId)) return 0

      val periodId: Int = settlementPeriodId.toInt
      val periodStartPoint: Date = this.settlementPeriodHelper.getPeriodStart(periodId)
      val periodEndPoint: Date = this.settlementPeriodHelper.getPeriodEnd(periodId)

      val skusOutOfStock: util.Set[String] = new util.TreeSet[String]

      val repo = new MarketSideTransactionRepoImpl()
      val nonProcessedTransactionList = repo.findNonProcessedTransactionById(mstId)


      import scala.collection.JavaConversions._
      for (nonProcessedTransaction <- nonProcessedTransactionList) {
        try {

          processedCount += this.processTransaction(periodStartPoint, periodEndPoint, nonProcessedTransaction,
            false)

        } catch {
          case e: Exception =>
            e.printStackTrace()
            if (REQUEST_TOO_MUCH == e.getMessage) skusOutOfStock.add(nonProcessedTransaction.getSku)
            this.dao.deleteMarketSideTransactionException(nonProcessedTransaction.getId)
            this.dao.insertMarketSideTransactionException(nonProcessedTransaction.getId, e.getMessage, ExceptionUtils.getStackTrace(e))
        }
      }

    }
    catch {

      case e: Exception =>
        e.printStackTrace()
    }

    processedCount
  }


  def deleteNonProcessedTransactions: String = {
    val deletedRows: Int = this.dao.deleteNonProcessedTransactions
    deletedRows + " row(s) have been deleted."
  }




  private def collectAndSaveMarketSideTransactions(startPoint: Date, endPoint: Date ,kcode:String ) : Int = {

    val repo = new MarketSideTransactionRepoImpl()

    val transactions = repo.findRawTransactions(startPoint,endPoint,kcode)
    repo.add(transactions)
    transactions.size()

  }



  private def isMarketSideTransactionProcessed(period: SettlementPeriod) =
    this.dao.existMarketSideTransaction(period.getStartPoint, period.getEndPoint)

  @Transactional("transactionManager")
  private def processTransaction(periodStart: Date, periodEnd: Date, transaction: MarketSideTransaction, isTest: Boolean):Int= {

    Assert.notNull(transaction.getType, "transaction.getType()")
    if (transaction.getType == "Order" && transaction.getSourceId == "112-8687629-3146667") {
      this.dao.setTransactionProcessed(transaction.getId, true)
      0
    }
    if (transaction.getType == "Refund" && transaction.getSourceId == "111-1732008-6192200") {
      this.dao.setTransactionProcessed(transaction.getId, true)
      0
    }
    if (transaction.getType == "Refund" && transaction.getSourceId == "111-2650759-1619429") {
      this.dao.setTransactionProcessed(transaction.getId, true)
      0
    }

    val allocationInfos = this.getSkuShipmentAllocationInfos(transaction)

   //todo arthur
    (transaction.asInstanceOf[AbstractMarketSideTransaction]).setAllocationInfos(allocationInfos)


    //todo arthur change parameter
    val resultDtIdList = transaction.process(periodStart, periodEnd)

    if (!("FREE_REPLACEMENT_REFUND_ITEMS" == transaction.getDescription))
      this.updateSkuStockQuantity(transaction.getAllocationInfos)

    if (!isTest) this.dao.setTransactionProcessed(transaction.getId, true)

    if(resultDtIdList == null) 0 else resultDtIdList.size()


  }

  //todo arthur need modify it
  private def getSkuShipmentAllocationInfos(transaction: MarketSideTransaction): util.List[SkuShipmentAllocationInfo] = {

    if (transaction.needAllocationInfos) {
      val totalQuantityPurchased = transaction.getTotalQuantityPurchased()

      val sourceMarketplace = Marketplace.fromName(transaction.getSource)

      val unsDestinationCountry = sourceMarketplace.getUnsDestinationCountry

      return this.stockAllocator.requestAllocations(unsDestinationCountry, transaction, totalQuantityPurchased)
    }

    null
  }

  private def updateSkuStockQuantity(skuShipmentAllocationInfos: util.List[SkuShipmentAllocationInfo]): Unit = {
    if (skuShipmentAllocationInfos == null) return
    import scala.collection.JavaConversions._
    for (allocationInfo <- skuShipmentAllocationInfos) {
      this.stockAllocator.increaseStockQuantity(allocationInfo.getUnsName, allocationInfo.getIvsName, allocationInfo.getSku, 1)
    }
  }

  private def sendNotificationForRequestedTooMuch(skusOutOfStock: util.Set[String]): Unit = {
    if (!skusOutOfStock.isEmpty) {
      val subject = "SKUs potentially out of stock after processing transactions"
      val body = new StringBuilder().append("<p>Here are the SKUs that encountered the Requested Too Much exception.</p>")
      import scala.collection.JavaConversions._
      for (sku <- skusOutOfStock) {
        body.append(sku).append("<br>")
      }

      //todo arthur  not completed
      //body.append("<br>").append(mailUtil.getSignature(MailUtil.SignatureType.NO_REPLY))
      //this.mailUtil.SendMimeWithBcc(ACCOUNT_MANAGERS, SOFTWARE_ENGINEERS, MAIL_ADDRESS_NOREPLY, subject, body.toString)
    }
  }

  private def generateTimeSpentInfo(totalSeconds: Long) = {
    val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds)
    val seconds = totalSeconds % 60
    "Total time spent: " + minutes + " mins " + seconds + " secs."
  }






}
