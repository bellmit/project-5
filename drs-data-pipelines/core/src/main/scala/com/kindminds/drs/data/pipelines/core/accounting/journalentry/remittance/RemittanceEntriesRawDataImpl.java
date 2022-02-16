package com.kindminds.drs.data.pipelines.core.accounting.journalentry.remittance;




import com.kindminds.drs.data.pipelines.core.accounting.journalentry.*;
import com.kindminds.drs.data.pipelines.core.accounting.journalentry.dal.DrsJournalEntryDao;
import com.kindminds.drs.data.pipelines.core.accounting.journalentry.dal.ErpJournalEntryDao;

import com.kindminds.drs.api.data.access.rdb.accounting.SettlementPeriodDao;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class RemittanceEntriesRawDataImpl implements JournalEntriesRawData {

    private String period;
    private Date transactionDate;
    private List<RemittanceInfo> remittanceInfoList;

    private SettlementPeriodDao settlementPeriodRepo = SpringAppCtx.get().getBean(SettlementPeriodDao.class);
    private ErpJournalEntryDao dao = new ErpJournalEntryDao();
    private DrsJournalEntryDao drsJDao = new DrsJournalEntryDao();

    public RemittanceEntriesRawDataImpl() {}

    private void getRemittanceInfo() {

        SettlementPeriod settlementPeriod = drsJDao.queryRecentPeriods().get(0);
//        Object[] result = this.settlementPeriodRepo.queryPeriodById(140);
//        SettlementPeriod settlementPeriod = new SettlementPeriodImpl((Integer)result[0],
//                (Date)result[1],(Date)result[2]);

        period = settlementPeriod.getPeriodAsString();

        System.out.println(period);

        Instant transactionInstant = settlementPeriod.getEndPoint().toInstant().plus(10, ChronoUnit.DAYS);


        transactionDate = Date.from(transactionInstant);
        Date transactionDateEnd = Date.from(transactionInstant.plus(1, ChronoUnit.DAYS));
        System.out.println("transactionDate: " + transactionDate);


        remittanceInfoList = drsJDao.queryRemittanceList(transactionDate, transactionDateEnd);


    }


    public void generateEntriesRawData() {

        getRemittanceInfo();


//        dao.deleteJournalEntriesByDate(transactionDate);

        JournalEntry entryRawData = new RemittanceEntryImpl(transactionDate, period, remittanceInfoList);

        entryRawData.generateEntryRawData();

        saveJournalEntry(entryRawData);
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

            Integer partnerId = StringUtils.hasText(item.getPartner()) ?
                dao.queryPartnerIdByName(item.getPartner()) : dao.queryPartnerIdByCode(item.getCompanyCode());

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
