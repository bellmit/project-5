package com.kindminds.drs.enums;

public enum AmazonTransactionType {
	ORDER("Order"),
	REFUND("Refund"),
	ADJUSTMENT_REFUND("AdjustmentRefund"),
	OTHER("other-transaction"),
	;
	private String value;
	AmazonTransactionType(String value) {
		this.value = value;
	}
	public String getValue() {
		return this.value;
	}
	public static AmazonTransactionType fromValue(String value) {
		for (AmazonTransactionType typeEnum : AmazonTransactionType.values()) {
			if (typeEnum.getValue().equals(value)) {
				return typeEnum;
			}
		}
		return null;
	}
}