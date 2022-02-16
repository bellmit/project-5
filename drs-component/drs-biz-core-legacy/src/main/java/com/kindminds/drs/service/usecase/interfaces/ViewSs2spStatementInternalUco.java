package com.kindminds.drs.service.usecase.interfaces;

import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport;
import com.kindminds.drs.enums.BillStatementType;

public interface ViewSs2spStatementInternalUco {

    /**
     *  This method is used internally from the backend to bypass the user check
     * @param type  - The type of Bill Statement
     * @param statementName - The name of the Statement
     * @return Ss2spProfitShareReport
     */
    Ss2spProfitShareReport getSs2spProfitShareReportInternal(BillStatementType type, String statementName);
}
