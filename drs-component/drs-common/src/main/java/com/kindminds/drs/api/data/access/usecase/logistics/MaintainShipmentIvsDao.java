package com.kindminds.drs.api.data.access.usecase.logistics;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;

import com.kindminds.drs.api.v1.model.sales.PurchaseOrderInfo;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderSkuInfo;

public interface MaintainShipmentIvsDao {

	//int queryCounts(String userCompanyKcode, IvsSearchCondition condition);
	//List<Ivs> queryList(String userCompanyKcode, int startIndex, int size, IvsSearchCondition condition);


	ShipmentStatus queryStatus(String shipmentName);
    ShipmentIvs query(String name);

	Integer queryMaxSerialIdOfDraftShipments(String supplierKcode);
	Integer queryMaxSerialIdOfNonDraftShipments(String supplierKcode);
	String insert(String supplierKcode,Ivs shipment,Integer serialId,String draftName,BigDecimal salesTaxRate,BigDecimal subtotal,BigDecimal tax,BigDecimal total,Currency handlerCurrency,ShipmentStatus status);
	String updateData(Ivs shipment,BigDecimal salesTaxRate,BigDecimal subtotal,BigDecimal salesTax,BigDecimal total);
	String updateNameAndSerialId(String origName,String newName,int serialId);
	String updateStatus(String shipmentName, ShipmentStatus status);
	String updatePurchasedDate(String shipmentName,Date purchasedDate);
	void updateDateConfirmed(String shipmentName,Date date);
	String updatedInternalNote(String shipmentName,String internalNote);
	void updateInvoiceNumber(String shipmentName,String invoiceNumber);
	String delete(String shipmentName);
	String queryBuyerKcode(String shipmentName);
	String querySellerKcode(String shipmentName);
	String queryBuyerNameLocal(String shipmentName);
	String queryShipmentPickupRequesterEmail(String shipmentName);
	Map<String, String> queryAllActiveSkuCodeToSupplierNameMap();
	Map<String, String> queryActiveAndOnboardingSkuCodeToSupplierNameMap(String kcode);
	Map<Integer,String> queryAvailableFcaLocationIdToNameMap();
	int queryDaysBeforeExportForFcaDelivery(Country targetCountry, ShippingMethod shippingMethod, int fcaLocationId);
	Currency queryCurrency(String shipmentName);
	void setPickupRequsterForShipment(String shipmentName,int userId);
	void setConfirmor(String shipmentName,int userId);
	Map<String, BigDecimal> queryShipmentNameToPaymentTotalMap(List<String> shipmentNameList);
	BigDecimal queryShipmentPaymentTotal(String shipmentName);
	Map<String, String> queryShipmentNameToUnsNameMap(List<String> shipmentNameList);
	ProductSkuStock queryProductSkuStock(String shipmentName, String skuCodeByDrs);
	Boolean guiFileNameExists(String guiFileName, String shipmentName,
							  int boxNum, int mixedBoxLineSeq);
	void updateGuiInvoiceData(Ivs shipment);

	String queryCountryOfOrigin(String productBaseCode);
	PurchaseOrderInfo queryPurchaseOrderInfo(String shipmentName);
	List<PurchaseOrderSkuInfo> queryPurchaseOrderInfoList(String shipmentName);

	Object[] queryShippingCostData(String productBaseCode, String countryCode);
	String queryBatteryType(String productBaseCode) ;

	BigDecimal queryFreightFeeRate(
			String destination, ShippingMethod shippingMethod,
			Boolean msFlag, BigDecimal chargeableWeight);
}
