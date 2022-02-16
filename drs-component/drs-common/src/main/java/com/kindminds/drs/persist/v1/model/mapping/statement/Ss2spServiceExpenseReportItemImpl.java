package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.math.BigDecimal;




import com.kindminds.drs.api.v1.model.report.Ss2spServiceExpenseReport.Ss2spServiceExpenseReportItem;


public class Ss2spServiceExpenseReportItemImpl implements Ss2spServiceExpenseReportItem {
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="sku")
	private String sku;
	//@Column(name="sku_name")
	private String skuName;
	//@Column(name="item_name")
	private String itemName;	
	//@Column(name="note")
	private String note;		
	//@Column(name="quantity")
	private Integer quantity;
	//@Column(name="total")
	private BigDecimal total;

	public Ss2spServiceExpenseReportItemImpl() {
	}

	public Ss2spServiceExpenseReportItemImpl(int id, String sku, String skuName, String itemName, String note, Integer quantity, BigDecimal total) {
		this.id = id;
		this.sku = sku;
		this.skuName = skuName;
		this.itemName = itemName;
		this.note = note;
		this.quantity = quantity;
		this.total = total;
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
	public String getItemName() {
		return this.itemName;
	}

	@Override
	public String getNote() {
		return this.note;
	}
	
	@Override
	public Integer getQuantity() {
		return this.quantity;
	}

	@Override
	public String getAmountPerUnit() {
		return null;
	}

	@Override
	public String getAmountTotalStr() {
		return this.total.setScale(2).toString();
	}

	@Override
	public BigDecimal getAmountTotal() {
		return this.total;
	}
	
}
