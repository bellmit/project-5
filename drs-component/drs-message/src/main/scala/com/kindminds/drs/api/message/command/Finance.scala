package com.kindminds.drs.api.message.command


import com.kindminds.drs.api.message.Command

  case class CalculateOriginalAccountsReceivable() extends Command

  case class GenerateAccountsReceivableAgingReport() extends Command

  case class CommitSettlement() extends Command

  case class RequestBiweeklyStatementJournalEntries() extends Command

  case class RequestRemittanceJournalEntries() extends Command