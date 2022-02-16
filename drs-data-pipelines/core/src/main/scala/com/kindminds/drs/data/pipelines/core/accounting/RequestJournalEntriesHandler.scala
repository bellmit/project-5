package com.kindminds.drs.data.pipelines.core.accounting

import java.time.Instant
import java.time.temporal.ChronoUnit

import akka.actor.{Actor, ActorLogging, Props}
import com.kindminds.drs.api.message.command.{RequestBiweeklyStatementJournalEntries, RequestRemittanceJournalEntries}
import com.kindminds.drs.data.pipelines.core.accounting.journalentry.dal.DrsJournalEntryDao
import com.kindminds.drs.data.pipelines.core.accounting.journalentry.remittance.RemittanceEntriesRawDataImpl
import com.kindminds.drs.data.pipelines.core.accounting.journalentry.statement.StatementEntriesRawDataImpl


object RequestJournalEntriesHandler {

  def props(): Props =
    Props(new RequestJournalEntriesHandler())

}



class RequestJournalEntriesHandler()  extends Actor with ActorLogging {

  private val name = self.path.name

  //todo arthur
  val dao =  new DrsJournalEntryDao
//  val dao = null

  override def receive: Receive = {

    case s: RequestBiweeklyStatementJournalEntries =>

      println("RequestBiweeklyStatementJournalEntries----")

      //todo arthur
      val journalEntries = new StatementEntriesRawDataImpl

      journalEntries.generateEntriesRawData()


    case r: RequestRemittanceJournalEntries =>
      System.out.println("Running remittance scheduler.")

      val now =  Instant.now()

      //todo arthur
      val latestEndPlus10days = dao.queryLatestPeriodEnd().toInstant.plus(10, ChronoUnit.DAYS)
//      val latestEndPlus10days= null

      if (now.isAfter(latestEndPlus10days)) {
        System.out.println("Generate Remittance Entries Raw Data.")
        val journalEntries = new RemittanceEntriesRawDataImpl

        journalEntries.generateEntriesRawData()
      }

  }

}