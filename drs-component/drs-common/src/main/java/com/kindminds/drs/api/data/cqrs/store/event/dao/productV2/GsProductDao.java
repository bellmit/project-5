package com.kindminds.drs.api.data.cqrs.store.event.dao.productV2;

import com.kindminds.drs.api.data.transfer.productV2.GsProductData;

public interface GsProductDao {
    String insertUs(GsProductData gsProductData);
    String insertUk(GsProductData gsProductData);
    String insertCa(GsProductData gsProductData);
    String insertDe(GsProductData gsProductData);
    String insertFr(GsProductData gsProductData);
    String insertIt(GsProductData gsProductData);
    String insertEs(GsProductData gsProductData);
}
