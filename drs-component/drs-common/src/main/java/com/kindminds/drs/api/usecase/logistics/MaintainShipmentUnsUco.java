package com.kindminds.drs.api.usecase.logistics;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;

import com.kindminds.drs.api.v1.model.logistics.ShipmentUns;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns.Forwarder;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;

public interface MaintainShipmentUnsUco {
	DtoList<ShipmentUns> getList(int pageIndex);
	ShipmentUns get(String name);
	List<IvsLineItem> getIvsLineItem(String ivsName);
	List<String> getDestinationCountryCodes(String sellerKcode);
	List<String> getActualDestinationCountryCodes(String destinationCountry);
	List<String> getAvailableIvsNames(String sellerKcode,String destinationCountryCode);
	Map<String,String> getAvailableSkusInInventoryShipment(String shipmentName);
	List<ShipmentStatus> getShipmentStatusList();
	Map<String,String> getDrsCompanyKcodeToNameMap();
	String getCompanyCurrency(String kcode);
	String insertDraft(ShipmentUns shipment);
	String update(ShipmentUns shipment);
	String freeze(String name);
	String deleteDraft(String name);
	String getDrsCompanyKcode(String countryCode);
	List<Forwarder> getForwarderList();
	ProductSkuStock getProductSkuStock(String shipmentName,String skuCodeByDrs);
}
