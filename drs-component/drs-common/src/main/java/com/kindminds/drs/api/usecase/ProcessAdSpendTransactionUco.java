package com.kindminds.drs.api.usecase;

import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;

import java.util.List;

public interface ProcessAdSpendTransactionUco {
    List<InternationalTransaction> processAdSpendTransaction(String userCompanyKcode);
}