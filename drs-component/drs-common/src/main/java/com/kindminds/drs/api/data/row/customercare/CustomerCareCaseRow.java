package com.kindminds.drs.api.data.row.customercare;

import java.math.BigDecimal;

public  final class CustomerCareCaseRow {

    public final int id ;
    public final int supplierCompanyId;
    public final String dateCreate;

    public CustomerCareCaseRow(int id ,int supplierCompanyId,String dateCreate){
        this.id = id;
        this.supplierCompanyId = supplierCompanyId;
        this.dateCreate = dateCreate;
    }

}

