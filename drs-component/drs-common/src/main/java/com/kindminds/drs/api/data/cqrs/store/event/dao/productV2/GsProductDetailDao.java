package com.kindminds.drs.api.data.cqrs.store.event.dao.productV2;


public interface GsProductDetailDao {

    String save(String ean, String codeByDrs, String marketplaceSku, int marketplaceId, String nameByDrs, boolean containLithiumIonBattery);

}
