package com.kindminds.drs.api.data.access.usecase.logistics;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;

import com.kindminds.drs.api.v1.model.logistics.ShipmentUns;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns.Forwarder;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;

public interface MaintainShipmentUnsDao {
	public Integer queryNonDraftInventoryShpliCount(String shpName,String skuKcode);
	public Integer queryAllShipmentsCounts(int userId);
	public List<ShipmentUns> queryShipmentList(int userId,int startIndex,int size);
	public Integer queryMaxSerialIdOfDraftShipments(String companyKcode);
	public Integer queryMaxSerialIdOfNonDraftShipments(String companyKcode);
	public List<String> queryAvailableInventoryShipmentNameList(String sellerKcode, String dstCountry);
	public Map<String,String> queryAvailableSkuToDrsNameMapForInventoryShipment(String shipmentName);
	public ShipmentUns query(String shipmentName);
	public List<IvsLineItem> queryInventoryShipmentLineItem(String inventoryShipmentName);
	public String queryCountryOfCompany(String kcode);
	public String insert(Integer serialId,String name,String invoiceNumber,
						 String status,int currencyId, Date exportDate,
						 Date arrivalDate,Date expectArrivalDate, ShipmentUns shipment,
						 Integer warehouseId, BigDecimal ddpTotal,BigDecimal cifTotal);
	public void updateStatus(String shpName, ShipmentStatus status);
	public void updateInvoiceNumver(String shipmentName, String invoiceNumber);
	public void updateExportDate(String shpName,Date date);
	public void updateExportFxRate(String shpName,String rate);
	public void updateExportFxRateToEur(String shpName,String rate);
	public void updateDestinationReceivedDate(String shpName,Date date);
	public void updateExpectArrivalDate(String shpName,Date date);
	public void updateTrackingNumber(String shpName,String trackingNumber);
	public void updateFbaId(String shpName,String fbaId);
	public void updateForwarder(String shpName,Forwarder forwarder);
	public String updateNameAndSerialId(String origName, String newName, int serialId);
	public String updateData(ShipmentUns shipment,BigDecimal ddpTotal,BigDecimal cifTotal);
	public String deleteDraft(String shipmentName);
	public ShipmentStatus queryStatus(String shipmentName);
	public String queryCompanyKcode(int countryId);
	public String querySellerKcode(String name);
	public void updateIvsStatus(Set<String> ivsNameSet, String status);
	public int queryQtyOrdered(String shipmentName,String sku,
							   Integer boxNum, Integer mixedBoxLineSeq);
	public int queryQtySold(String shipmentName,String sku,
							Integer boxNum, Integer mixedBoxLineSeq);
	public void updateSoldQty(String shpName,String sku,int soldQty,
							  Integer boxNum, Integer mixedBoxLineSeq);
	public ProductSkuStock queryProductSkuStock(String shipmentName, String skuCodeByDrs);
	public List<ShipmentUns.ShipmentUnsLineItem> queryShipmentOfInventoryLineItems(String shipmentName);
}
