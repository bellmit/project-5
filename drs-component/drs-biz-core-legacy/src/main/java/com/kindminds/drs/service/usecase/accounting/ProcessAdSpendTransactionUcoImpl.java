package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.api.usecase.ProcessAdSpendTransactionUco;
import com.kindminds.drs.api.usecase.accounting.MaintainInternationalTransactionUco;
import com.kindminds.drs.api.usecase.common.SettlementPeriodListUco;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;
import com.kindminds.drs.v1.model.impl.accounting.InternationalTransactionImpl;
import com.kindminds.drs.v1.model.impl.accounting.InternationalTransactionLineItemImpl;
import com.kindminds.drs.api.v1.model.marketing.AdSpendTransaction;
import com.kindminds.drs.api.data.access.usecase.accounting.ProcessAdSpendTransactionDao;
import com.kindminds.drs.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class ProcessAdSpendTransactionUcoImpl implements ProcessAdSpendTransactionUco {

    @Autowired
    private ProcessAdSpendTransactionDao dao;
    @Autowired
    private MaintainInternationalTransactionUco internationalTransactionUco;
    @Autowired
    private SettlementPeriodListUco settlementPeriodListUco;

    private enum AD_SPEND_TYPE {
        SPONSORED_AD (" Sponsored Ad"),
        HEADLINE_AD (" Headline Ad Spend");

        private String note;

        AD_SPEND_TYPE(String note){
            this.note = note;
        }

        @Override
        public String toString() {
            return this.note;
        }
    }

    @Override
    public List<InternationalTransaction> processAdSpendTransaction(String userCompanyKcode) {
        boolean processed = dao.isAdSpendProcessed();

        List<InternationalTransaction> internationalTransactions = new ArrayList<InternationalTransaction>();

        if(!processed) {
            List<Integer> addedInternationalTransactions = new ArrayList<>();
            List<AdSpendTransaction> sponsoredAdTransactions = dao.getSponsoredAdTransactions();
            if(sponsoredAdTransactions != null && !sponsoredAdTransactions.isEmpty())
                addedInternationalTransactions.addAll(generateTransaction(AD_SPEND_TYPE.SPONSORED_AD, sponsoredAdTransactions, userCompanyKcode));
            List<AdSpendTransaction> headlineAdTransactions = dao.getHeadlineAdTransactions();
            if(headlineAdTransactions != null && !headlineAdTransactions.isEmpty())
                addedInternationalTransactions.addAll(generateTransaction(AD_SPEND_TYPE.HEADLINE_AD, headlineAdTransactions, userCompanyKcode));
            if(!addedInternationalTransactions.isEmpty())
                internationalTransactions.addAll(internationalTransactionUco.get(addedInternationalTransactions));
        }
        return internationalTransactions;
    }

    private List<Integer> generateTransaction(AD_SPEND_TYPE type, List<AdSpendTransaction> adSpendTransactions, String userCompanyKcode){
        List<Integer> addedInternationalTransactions = new ArrayList<>();
        Date transactionDate = subtractOneDay(settlementPeriodListUco.getLatestSettlementPeriod().getEndPoint());
        for(AdSpendTransaction adSpendTransaction : adSpendTransactions) {
            Date periodStartDate = DateHelper.toDate(adSpendTransaction.getPeriodStartUtc() + " UTC", "yyyyMMdd z");
            if (periodStartDate.before(this.settlementPeriodListUco.getLastSettlementEnd())) continue;
            InternationalTransactionImpl internationalTransaction = new InternationalTransactionImpl();
            internationalTransaction.setTransactionDate(transactionDate);
            // Supplier to MSDC
            internationalTransaction.setCashFlowDirectionKey(0);
            internationalTransaction.setMsdcKcode(adSpendTransaction.getMsdcKcode());
            internationalTransaction.setSsdcKcode(userCompanyKcode);

            String supplierCode = adSpendTransaction.getSupplierKcode();
            internationalTransaction.setSplrKcode(supplierCode);
            BigDecimal vatRate = calculateVatRate(adSpendTransaction.getMsdcKcode());
            BigDecimal vatAmount = vatRate.multiply(adSpendTransaction.getSum());

            internationalTransaction.setCurrencyId(adSpendTransaction.getCurrencyId());
            internationalTransaction.setNote(adSpendTransaction.getPeriodStartUtc() + "-" + DateHelper.toString(transactionDate, "yyyyMMdd") + type.note);
            List<InternationalTransaction.InternationalTransactionLineItem> itlil = new ArrayList<InternationalTransaction.InternationalTransactionLineItem>();
            InternationalTransactionLineItemImpl itli = new InternationalTransactionLineItemImpl();
            itli.setItemKey(5);
            itli.setSubtotal(adSpendTransaction.getSum());
            itli.setVatRate(vatRate);
            itli.setVatAmount(vatAmount);
            itli.setCurrencyId(adSpendTransaction.getCurrencyId());
            itli.setLineSeq(1);
            itli.setItemNote(adSpendTransaction.getPeriodStartUtc() + "-" + DateHelper.toString(transactionDate, "yyyyMMdd") + type.note);
            itlil.add(itli);
            internationalTransaction.setLineItems(itlil);
            Integer id = internationalTransactionUco.create(internationalTransaction);
            if(id != null) addedInternationalTransactions.add(id);
        }
        return addedInternationalTransactions;
    }

    private BigDecimal calculateVatRate(String msdcCode) {
        if (msdcCode.equals("K4")) {
            return BigDecimal.ZERO;
        } else if (msdcCode.equals("K6")) {
            //todo arthur vat rate
            //return BigDecimal.valueOf(.19);
            return BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    private Date subtractOneDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}