package com.kindminds.drs.api.data.access.rdb.accounting;

import com.kindminds.drs.api.v1.model.close.BillStatement;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface AccountsReceivableAgingDao {

    List<Object[]> queryAccountReceivableAgingList();

    Instant queryLastSettlementPeriodEnd(Instant reportDate);

    Boolean accountReceivableExists(Instant periodEnd);

    void deleteExistingAccountReceivable(Instant periodEnd);

    List<BillStatement> queryStatementListBySupplier(Instant periodEnd);

    void insertOriginalAccountsReceivable(Integer statementId,
                                          Instant invoiceDate,
                                          BigDecimal originalAR);

    Map<String, BigDecimal> querySupplierToRemittanceMap(Instant latestEnd, Instant reportDate);

    BigDecimal queryUSDtoTWDexchangeRate(Instant reportDate);

    Integer queryMaxSupplierAccountsReceivableId();

    void insertSupplierAccountsReceivable(Integer sarId, String supplierCode, Instant reportDate,
                                          BigDecimal writeOff, BigDecimal exchangeRate);

    List<Object[]> queryHistoricalStatementDetail(String supplierCode, Instant reportDate);

    Integer queryMaxSupplierAccountsReceivableDetailId();

    void insertSupplierAccountsReceivableDetail(Integer sarDetailId,
                                                Integer sarId,
                                                Integer statementId,
                                                BigDecimal arBalance);

    BigDecimal queryOriginalAccountsReceivable(Integer statementId);

    Integer querySupplierAccountsReceivableId(Integer companyId);

    BigDecimal querySumSupplierAccountsReceivableBalance(Integer sarId);

    void updateSupplierAccountsReceivableBalance(Integer sarId, BigDecimal balance);


//    void calculateOriginalAccountsReceivable();
//
//    void generateAccountsReceivableAgingReport();


}
