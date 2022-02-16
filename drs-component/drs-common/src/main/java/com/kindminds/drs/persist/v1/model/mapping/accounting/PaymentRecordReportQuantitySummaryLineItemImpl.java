package com.kindminds.drs.persist.v1.model.mapping.accounting;


import com.kindminds.drs.api.v1.model.report.InventoryPaymentReport;

public class PaymentRecordReportQuantitySummaryLineItemImpl implements InventoryPaymentReport.InventoryPaymentReportQuantitySummaryLineItem {

	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="sku")
	private String sku;

	private Integer quantityOriginalInvoice=0;
	private Integer amountOriginalInvoice=0;

	private Integer quantityReturned=0;
	private Integer amountReturned=0;
	//@Column(name="quantity_order")
	private Integer quantityOrder=0;
	private Integer amountOrder=0;
	//@Column(name="quantity_fba_return_to_supplier")
	private Integer quantityFbaReturnToSupplier=0;
	private Integer amountFbaReturnToSupplier=0;

	private Integer quantitySoldBackRecovery =0;
	private Integer amountSoldBackRecovery =0;

	//@Column(name="quantity_other_transaction")
	private Integer quantityOtherTransaction=0;
	private Integer amountOtherTransaction=0;
	//@Column(name="quantity_refund")
	private Integer quantityRefund =0;
	private Integer amountRefund =0;


	private Integer quantityPaid =0;
	private Integer amountPaid =0;

	public PaymentRecordReportQuantitySummaryLineItemImpl() {
	}

	public PaymentRecordReportQuantitySummaryLineItemImpl(int id, String sku, Integer quantityOrder,
														  Integer quantityFbaReturnToSupplier, Integer quantitySoldBackRecovery,
														  Integer quantityOtherTransaction, Integer quantityRefund) {
		this.id = id;
		this.sku = sku;
		this.quantityOrder = quantityOrder;
		this.quantityFbaReturnToSupplier = quantityFbaReturnToSupplier;
		this.quantitySoldBackRecovery = quantitySoldBackRecovery;
		this.quantityOtherTransaction = quantityOtherTransaction;
		this.quantityRefund = quantityRefund;
	}

	public PaymentRecordReportQuantitySummaryLineItemImpl(String sku, Integer amountOrder, Integer amountFbaReturnToSupplier, Integer amountSoldBackRecovery, Integer amountOtherTransaction, Integer amountRefund) {
		this.sku = sku;
		this.amountReturned = amountReturned;this.amountOrder = amountOrder;
		this.amountFbaReturnToSupplier = amountFbaReturnToSupplier;
		this.amountSoldBackRecovery = amountSoldBackRecovery;
		this.amountOtherTransaction = amountOtherTransaction;
		this.amountRefund = amountRefund;
	}

	public void setOriginalInvoiceQuantity(int qty){ this.quantityOriginalInvoice=qty; }

	public void setOriginalInvoiceAmount(int amount){ this.amountOriginalInvoice=amount; }

	@Override
	public String toString() {
		return "PaymentRecordReportQuantitySummaryLineItemImpl [getSku()=" + getSku()
				+ ", getQuantityOriginalInvoice()=" + getQuantityOriginalInvoice() + ", getQuantityReturned()="
				+ getQuantityReturned() + ", getQuantityActualPurchase()=" + getQuantityActualPurchase()
				+ ", getQuantityMarketSideFulfilled()=" + getQuantityMarketSideFulfilled() + "]";
	}

	@Override
	public String getSku() {
		return this.sku;
	}
	
	@Override
	public Integer getQuantityOriginalInvoice() {
		return this.quantityOriginalInvoice;
	}

	@Override
	public Integer getAmountOriginalInvoice() {
		return this.amountOriginalInvoice;
	}

	@Override
	public Integer getQuantityReturned() {
		return this.quantityReturned;
	}

	@Override
	public Integer getAmountReturned() {
		return this.amountReturned;
	}

	@Override
	public Integer getQuantityActualPurchase() {
		return this.quantityOriginalInvoice-this.getQuantityReturned();
	}

	@Override
	public Integer getAmountActualPurchase() {
		return this.amountOriginalInvoice-this.getAmountReturned();
	}
	
	@Override
	public Integer getQuantityMarketSideFulfilled() {
		return this.quantityOrder==null?0:this.quantityOrder;
	}

	@Override
	public Integer getAmountMarketSideFulfilled() {
		return this.amountOrder==null?0:this.amountOrder;
	}

	@Override
	public Integer getQuantitySoldBack() {
		return this.quantityFbaReturnToSupplier==null?0:this.quantityFbaReturnToSupplier;
	}

	@Override
	public Integer getAmountSoldBack() {
		return this.amountFbaReturnToSupplier==null?0:this.amountFbaReturnToSupplier;
	}

	@Override
	public Integer getQuantitySoldBackRecovery() {
		return this.quantitySoldBackRecovery==null?0: this.quantitySoldBackRecovery;
	}

	@Override
	public Integer getAmountSoldBackRecovery() {
		return this.amountSoldBackRecovery==null?0: this.amountSoldBackRecovery;
	}

	@Override
	public Integer getQuantityOther() {
		Integer otherTransactionQty = this.quantityOtherTransaction==null?0:this.quantityOtherTransaction;
		return otherTransactionQty;
	}

	@Override
	public Integer getAmountOther() {
		Integer otherTransactionAmount = this.amountOtherTransaction==null?0:this.amountOtherTransaction;
		return otherTransactionAmount;
	}

	@Override
	public Integer getQuantityPaid() {

		return this.getQuantityMarketSideFulfilled()+ this.getQuantityOther()+this.getQuantitySoldBack();
		//return this.quantityPaid;

	}

	@Override
	public Integer getAmountPaid() {

		return this.getAmountMarketSideFulfilled()+ this.getAmountOther()+this.getAmountSoldBack();
		//return this.quantityPaid;

	}

	@Override
	public Integer getQuantityRefunded() {
		return this.quantityRefund==null?0:this.quantityRefund;
	}

	@Override
	public Integer getAmountRefunded() {
		return this.amountRefund==null?0:this.amountRefund;
	}

	@Override
	public Integer getQuantityNetPaid() {
		return this.getQuantityPaid()-this.getQuantityRefunded() + this.getQuantitySoldBackRecovery();
	}

	@Override
	public Integer getAmountNetPaid() {
		return this.getAmountPaid()-this.getAmountRefunded() + this.getAmountSoldBackRecovery();
	}

	@Override
	public Integer getQuantityOutstanding() {
		return this.getQuantityActualPurchase()-this.getQuantityNetPaid();
	}

	@Override
	public Integer getAmountOutstanding() {
		return this.getAmountActualPurchase()-this.getAmountNetPaid();
	}

	@Override
	public void setQuantityPaid(Integer quantityPaid) {
		this.quantityPaid = quantityPaid;
	}

	@Override
	public void setQuantityRefund(Integer quantityRefund) {
		this.quantityRefund = quantityRefund;
	}

	@Override
	public void setAmountRefund(Integer amountRefund) {
		this.amountRefund = amountRefund;
	}

	@Override
	public void setAmountPaid(Integer amountPaid) {
		this.amountPaid = amountPaid;
	}
}
