package com.kindminds.drs.data.pipelines.core.accounting.journalentry.statement;


import com.kindminds.drs.data.pipelines.core.accounting.journalentry.*;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.data.pipelines.core.accounting.journalentry.dal.DrsJournalEntryDao;
import com.kindminds.drs.data.pipelines.core.accounting.journalentry.dal.ErpJournalEntryDao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class StatementEntriesRawDataImpl implements JournalEntriesRawData {

    private String period;
    private Date transactionDate;
    private Date periodStart;
    private Date periodEnd;
    private List<CompanyCodeStatement> companyCodeStatements;

   // private SettlementPeriodDao settlementPeriodRepo = SpringAppCtx.get().getBean(SettlementPeriodDao.class);
    private ErpJournalEntryDao dao = new ErpJournalEntryDao();
    private DrsJournalEntryDao drsJDao = new DrsJournalEntryDao();

    public StatementEntriesRawDataImpl() {}

    private void getBasicInfoOfStatement() {

        SettlementPeriod settlementPeriod = drsJDao.queryRecentPeriods().get(0);
        	/*
	* 	int id =(int)columnsList.get(0)[0];
		Date start =(Date)columnsList.get(0)[1];
		Date end =(Date)columnsList.get(0)[2];
		return new SettlementPeriodImpl(id, start, end);
	* */
     //   SettlementPeriod settlementPeriod = this.settlementPeriodRepo.queryPeriodById(140);
        periodStart = settlementPeriod.getStartPoint();
        periodEnd = settlementPeriod.getEndPoint();

        companyCodeStatements = drsJDao.queryCompanyCodeStatements(settlementPeriod.getStartPoint());

        period = settlementPeriod.getPeriodAsString();

        System.out.println(period);

        transactionDate = settlementPeriod.getDayBeforeEndPoint();

    }

    /**
     * int id =(int)columns[0];
     * 		Date start =(Date)columns[1];
     * 		Date end =(Date)columns[2];
     *         SettlementPeriod settlementPeriod =  new SettlementPeriodImpl(id, start, end);
     */



    public void generateEntriesRawData() {

        getBasicInfoOfStatement();


//        dao.deleteJournalEntriesByDate(transactionDate);

        for (CompanyCodeStatement c: companyCodeStatements) {

            JournalEntry entryRawData = new StatementEntryImpl(transactionDate, period, c.companyCode(),
                  c.statementName(), periodStart, periodEnd);

            entryRawData.generateEntryRawData();


            if (shouldSaveEntry(entryRawData)) {
                saveJournalEntry(entryRawData);
            }
        }

    }

    private Boolean shouldSaveEntry(JournalEntry entry) {
        for (JournalEntryItem item : entry.getJournalEntryItems()) {
            if (item.getDebit().compareTo(BigDecimal.ZERO) != 0
                    || item.getCredit().compareTo(BigDecimal.ZERO) != 0) {
                return true;
            }
        }
        return false;
    }

    private void saveJournalEntry(JournalEntry entry) {

        String ref = dao.queryRefJournalEntry(entry.getTransactionDate());

        Integer journalId = dao.queryJournalIdSettlement();

        Integer companyId = dao.queryCompanyIdKindMinds();

        Integer currencyId = dao.queryCurrencyIdTWD();

        dao.insertJournalEntry(entry, ref, journalId, companyId, currencyId);

        Integer sequence = 1;

        for (JournalEntryItem item : entry.getJournalEntryItems()) {
            Integer moveId = dao.queryAccountMoveIdByRef(ref);

            Integer partnerId = dao.queryPartnerIdByCode(entry.getCompanyCode());

            AccountCodeInfo accountInfo = dao.queryAccountCodeInfo(item.getAccountCode());

            Integer accountId = accountInfo.accountId();
            String internalType = accountInfo.accountInternalType();
            Integer rootId = accountInfo.accountRootId();
            String classify1 = accountInfo.classify1();
            String classify2 = accountInfo.classify2();
            String classify3 = accountInfo.classify3();
            String accountName = accountInfo.accountName();

            dao.insertJournalItem(moveId, transactionDate, ref, journalId, companyId, currencyId,
                    partnerId, accountId, internalType, rootId,
                    classify1, classify2, classify3, item, sequence, accountName);

            sequence++;
        }

    }


}
