package com.kindminds.drs.fx.rate.constant;

public enum InterBankRate{
	PERCENTAGE_0("+/- 0%","0.00"),
	PERCENTAGE_1("+/- 1%","0.01");
	private String percentageText;
	private String valueText;
	InterBankRate(String percentageText,String valueText) {this.percentageText=percentageText;this.valueText=valueText;}
	public String getPercentageText() {return this.percentageText;}
	public String getValueText() {return this.valueText;}
}