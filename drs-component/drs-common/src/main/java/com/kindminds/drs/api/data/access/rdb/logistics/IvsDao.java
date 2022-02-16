package com.kindminds.drs.api.data.access.rdb.logistics;

import com.kindminds.drs.Currency;

import com.kindminds.drs.api.data.row.logistics.*;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import java.math.BigDecimal;
import java.util.*;


public  interface  IvsDao {

    String insert(String supplierKcode, Ivs shipment,
                        int serialId, String draftName ,
                BigDecimal salesTaxRate,BigDecimal subtotal,BigDecimal tax,
                BigDecimal total, Currency handlerCurrency, ShipmentStatus status);

    void update(Ivs ivs );

    int queryMaxSerialIdOfDraftShipments(String supplierKcode);

    String queryBuyerKcode(String shipmentName);

    List<IvsRow> queryByName(String name);

    List<IvsLineitemRow> queryLineItems(String name);

    BigDecimal queryShipmentPaymentTotal(String shipmentName);

    ShipmentStatus queryStatus(String shipmentName);

    int queryMaxSerialIdOfNonDraftShipments(String supplierKcode);

    String updateStatus(String shipmentName , ShipmentStatus status);

    String updateNameAndSerialId(String origName, String newName, int serialId);

    void setPickupRequsterForShipment(String shipmentName, int userId);

    void deleteShippingCost(String shipmentName);

    void insertShippingCost(String shipmentName, BigDecimal freightFeeOriginal,
                            BigDecimal freightFeeNonMerged,
                            int freightFeeRateIdOriginal,int freightFeeRateIdNonMerged,
                            BigDecimal truckCost, BigDecimal inventoryPlacementFee,
                            BigDecimal hsCodeFee, BigDecimal otherExpense,
                            BigDecimal shippingCostOriginal, BigDecimal shippingCostNonMerged);

    String updatePurchasedDate(String shipmentName, Date purchasedDate);

    void updateDateConfirmed(String shipmentName, Date date );

    void setConfirmor(String shipmentName, int userId);

    String delete(String shipmentName);

    Map<String, BigDecimal> queryShipmentNameToPaymentTotalMap( List<String> shipmentNameList);

    Map<String, String> queryShipmentNameToUnsNameMap(List<String> shipmentNameList );

    String querySellerKcode(String shipmentName);

    String queryShipmentPickupRequesterEmail(String shipmentName);

    List<SkuBoxNumRow> queryNewSkuBoxNum(String shipmentName);

    FreightFeeRateRow queryFreightFeeRate(String countryCode, ShippingMethod shippingMethod,
                                          Boolean msFlag, BigDecimal chargeableWeight);

    IvsShippingCostsRow queryShippingCosts(String shipmentName);

    //todo
    List queryShippingCostData(String skuCode, String countryCode);
    String queryBatteryType(String skuCode);

    BigDecimal querySalesTaxRate(String ivsName);

    BigDecimal[] queryUnitAmtAndSalesTaxRate(String ivsName , String sku);


}