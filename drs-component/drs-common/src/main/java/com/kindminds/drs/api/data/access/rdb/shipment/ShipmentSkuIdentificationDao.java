package com.kindminds.drs.api.data.access.rdb.shipment;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v1.model.logistics.ShipmentSkuIdentification;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;

import java.util.List;

public interface ShipmentSkuIdentificationDao {

    List<Object[]> query(int drsTranId);

    List<Object[]> query(int drsTranId , String status);

    List<Object[]> queryCanBeRefund(Country unsDestinationCountry, String drsSku);

    List<Object[]> queryCanBeSold(Country unsDestinationCountry, String drsSku);

    List<Object[]> queryCanBeSoldBackRecovery(Country unsDestinationCountry, String drsSku);

    void update(SkuShipmentAllocationInfo skuShipmentAllocationInfo , int drsTransactionId);

    void update(int id , int drsTransactionId , String status , String remark);

    List<ShipmentSkuIdentification> doShipmentSkuIdentification(String unsName);

    void add( List<ShipmentSkuIdentification> shipmentSkuIdentification);

    List<Object[]> queryByUns(String unsName);

}
