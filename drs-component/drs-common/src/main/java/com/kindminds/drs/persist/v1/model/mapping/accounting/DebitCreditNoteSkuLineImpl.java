package com.kindminds.drs.persist.v1.model.mapping.accounting;

import java.math.BigDecimal;
import java.math.RoundingMode;





import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote.DebitCreditNoteItem.DebitCreditNoteSkuLine;


public class DebitCreditNoteSkuLineImpl implements DebitCreditNoteSkuLine {
	
	//@Id //@Column(name="sku_code")
	private String skuCode;
	//@Column(name="description")
	private String description;

	//@Column(name="sum_amount")
	private BigDecimal sum;

	public DebitCreditNoteSkuLineImpl() {
	}

	public DebitCreditNoteSkuLineImpl(String skuCode, String description, BigDecimal sum) {
		this.skuCode = skuCode;
		this.description = description;
		this.sum = sum;
	}

	@Override
	public String toString() {
		return "DebitCreditNoteSkuLineImpl [getSkuCode()=" + getSkuCode()
				+ ", getDescription()=" + getDescription()
				+ ", getSumForSku()=" + getSumForSku() + "]";
	}

	@Override
	public String getSkuCode() {
		return this.skuCode;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getSumForSku() {
		return this.sum.setScale(2,RoundingMode.HALF_UP).toPlainString();
	}

}
