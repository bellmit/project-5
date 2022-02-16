package com.kindminds.drs.api.v1.model.logistics;

import com.kindminds.drs.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface UnifiedShipment {
	
	public int getUnifiedShipmentId();
	public String getName();
	public String getType();
	public String getInvoiceNumber();
	public String getSellerCompany();
	public String getBuyerCompany();
	public Currency getCurrency();
	public BigDecimal getSalesTaxRate();
	public BigDecimal getAmountTaxRate();	
	public String getStatus();
	public String getUltimateDestination();
	public String getShippingMethod();
	public String getDateCreated();	
	public List<UnifiedShipmentLineItem> getLineItems();
	public String getTotalAmount();
	
	public interface UnifiedShipmentLineItem {
		
		public String getInventoryShipmentId();
		public String getSKUs();
		public BigDecimal getCartonDimension1();
		public BigDecimal getCartonDimension2();
		public BigDecimal getCartonDimension3();
		public BigDecimal getUnitTax();
		public String getGrossWeight();
		public String getUnits();
		public String getNumber();		
		public BigDecimal getQuantity();
		public BigDecimal getUnitPrice();
		public BigDecimal getAmount();
				
	}
	
}