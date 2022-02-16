package com.kindminds.drs.v1.model.impl.statement;

import java.math.BigDecimal;


import com.kindminds.drs.api.data.access.rdb.logistics.IvsDao;
import com.kindminds.drs.api.v1.model.report.Ss2spSellBackReport;
import com.kindminds.drs.persist.v1.model.mapping.close.BillStatementLineItemImpl;
import com.kindminds.drs.service.util.SpringAppCtx;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;


public class Ss2spSellBackReportLineItemImpl implements Ss2spSellBackReport.Ss2spSellBackReportLineItem {

	private String ivsName;
	private String sku;
	private String skuName;
	private String itemType;
	private Integer quantity;
	private BigDecimal unitAmount;
	private BigDecimal unitAmountExclTax;
	private BigDecimal subtotal;
	private Currency statementCurrency;

	private IvsDao ivaDao = (IvsDao) SpringAppCtx.get().getBean(IvsDao.class);
	
	public Ss2spSellBackReportLineItemImpl(
			String ivsName,
			String sku,
			String skuName,
			String itemType,
			Integer originalCurrencyId,
			Integer statementCurrencyId,
			BigDecimal subtotalOriginalCurrency,
			BigDecimal subtotalStatementCurrency,
			Integer quantity){
		Assert.isTrue(originalCurrencyId.equals(statementCurrencyId));
		this.statementCurrency = Currency.fromKey(statementCurrencyId);
		this.ivsName = ivsName;
		this.sku = sku;
		this.skuName = skuName;
		this.itemType = itemType;
		this.quantity = quantity;
		this.unitAmount = subtotalOriginalCurrency.divide(new BigDecimal(this.quantity));
		this.subtotal = subtotalStatementCurrency;

	}
	
	public BigDecimal getNumericTotalAmount() {

		return this.subtotal;
	}
	
	@Override
	public String toString() {
		return "Ss2spSellBackReportLineItemImpl [getNumericTotalAmount()=" + getNumericTotalAmount() + ", getIvsName()="
				+ getIvsName() + ", getSku()=" + getSku() + ", getSkuName()=" + getSkuName() + ", getItemType()="
				+ getItemType() + ", getUnitAmount()=" + getUnitAmount() + ", getQuantity()=" + getQuantity()
				+ ", getSubtotal()=" + getSubtotal() + ", getCurrency()=" + getCurrency() + "]";
	}

	@Override
	public String getIvsName() {
		return this.ivsName;
	}

	@Override
	public String getSku() {
		return this.sku;
	}
	
	@Override
	public String getSkuName() {
		return this.skuName;
	}
	
	@Override
	public String getItemType() {
		return this.itemType;
	}
	
	@Override
	public String getUnitAmount() {
		return this.unitAmount.toPlainString();
	}
	
	@Override
	public Integer getQuantity() {
		return this.quantity;
	}
	
	@Override
	public String getSubtotal() {
		return this.subtotal.setScale(
				this.statementCurrency.getScale()).toPlainString();
	}
	
	@Override
	public String getCurrency() {
		return this.statementCurrency.name();
	}


	public void doExclTaxUnitAMount(){
		this.unitAmount = this.unitAmountExclTax;
	}

	private BigDecimal salesTax;
	@Override
	public BigDecimal getTaxAmount() {

		//BigDecimal salesTaxRate = this.ivaDao.querySalesTaxRate(ivsName);

		BigDecimal [] result = this.ivaDao.queryUnitAmtAndSalesTaxRate(ivsName,sku);
		this.unitAmountExclTax = result[0];
		BigDecimal subTotalExclTax =
				result[0].multiply(new BigDecimal(this.quantity));

		subTotalExclTax = (subtotal.compareTo(BigDecimal.ZERO) > 0 ?   subTotalExclTax : subTotalExclTax.negate());

		if(this.subtotal != subTotalExclTax ){
			this.subtotal = subTotalExclTax;
		}

		this.salesTax = this.subtotal.setScale(
				this.statementCurrency.getScale()).multiply(result[1]);


		return this.salesTax;
	}







}
