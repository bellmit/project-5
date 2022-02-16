package com.kindminds.drs.api.v1.model.logistics;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;

import java.util.List;

public interface ShipmentUns {
	
	String getName();
	ShipmentType getType();
    String getInvoiceNumber();
    String getTrackingNumber();
    String getFbaId();
    Forwarder getForwarder();
	String getSellerCompanyKcode();
	String getBuyerCompanyKcode();
	Currency getCurrency();
	ShipmentStatus getStatus();
	String getDestinationCountry();
	String getActualDestination();
	ShippingMethod getShippingMethod();
	String getDateCreated();
	String getAmountTotal();
	String getCifAmountTotal();
	String getExportDate();
	String getExportSrcCurrency();
	String getExportDstCurrency();
	String getExportCurrencyExchangeRate();
	String getExportFxRateToEur();
	String getDestinationReceivedDate();
	String getExpectArrivalDate();	
	List<ShipmentUnsLineItem> getLineItems();
	
	public interface ShipmentUnsLineItem{
		int getLineSeq();
		Integer getBoxNum();
		Integer getMixedBoxLineSeq();
		Integer getCartonNumberStart();
		Integer getCartonNumberEnd();
		String getSourceInventoryShipmentName();
		String getSkuCode();
		String getNameBySupplier();
		String getPerCartonGrossWeightKg();
		String getPerCartonUnits();
		String getCartonCounts();
		String getQuantity();
		String getUnitAmount();
		String getUnitCifAmount();
		String getSubtotal();
		String getCifSubtotal();
		String getCartonDimensionCm1();
		String getCartonDimensionCm2();
		String getCartonDimensionCm3();
	}	
	public enum Forwarder{
		UPS("https://www.ups.com/asia/tw/chtindex.html?flash=false"),
		DHL("http://www.dhl.com.tw/zt.html"),
		FedEx("https://www.fedex.com/tw/index.html"),
		Evatrans("http://www.evatrans.com.tw/");
		private String value;
		Forwarder(String value){
			this.value=value;
		}
		public String getValue(){
			return this.value;
		}
		
	}
	
}
