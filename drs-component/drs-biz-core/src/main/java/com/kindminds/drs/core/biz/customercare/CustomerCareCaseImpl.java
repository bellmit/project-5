package com.kindminds.drs.core.biz.customercare;


import com.kindminds.drs.api.data.row.customercare.CustomerCareCaseRow;
import com.kindminds.drs.api.v2.biz.domain.model.customercare.CustomerCareCase;


public  class CustomerCareCaseImpl implements CustomerCareCase {

    private final CustomerCareCaseRow customerCareCaseRow;

    public int getId() {
        return this.customerCareCaseRow.id;
    }

    @Override
    public int supplierCompanyId() {
        return this.customerCareCaseRow.supplierCompanyId;
    }


    public String getDateCreate() {
        return this.customerCareCaseRow.dateCreate;
    }

    public CustomerCareCaseImpl( CustomerCareCaseRow customerCareCaseRow) {

        this.customerCareCaseRow = customerCareCaseRow;
    }
}