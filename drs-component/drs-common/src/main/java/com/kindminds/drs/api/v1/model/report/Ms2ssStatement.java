package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;
import java.util.List;

public interface Ms2ssStatement {
	public String getDateStart();
	public String getDateEnd();
	public String getTotal();
	public String getCurrency();
	public String getIsurKcode();
	public String getRcvrKcode();
	public String getPreviousBalance();
	public String getRemittanceIsurToRcvr();
	public String getRemittanceRcvrToIsur();
	public String getBalance();
	public List<Ms2ssStatementItem> getStatementItems();
	public List<Ms2ssStatementItemPaymentOnBehalf> getStatementItemsPaymentOnBehalf();
	public Ms2ssStatementItemMsdcPaymentOnBehalf getMs2ssStatementItemMsdcPaymentOnBehalf();
	public Ms2ssStatementItemProductInventoryReturn getProductInventoryReturnItem();
	public interface Ms2ssStatementItem{
		public enum Ms2ssStatementItemType{
			PAYMENT_REFUND("Payment & Refund","PaymentRefund"),
			PURCHASE_ALWNC("Purchase Allowance","PurchaseAllowance");
			private String displayStr;
			private String urlText;
			Ms2ssStatementItemType(String displayStr, String urlText){
				this.displayStr=displayStr;
				this.urlText=urlText;
			}
			public String getDisplayStr(){
				return this.displayStr;
			}
			public String getUrlText(){
				return this.urlText;
			}
		}
		public String getDisplayName();
		public String getAmountStr();
		public String getNote();
		public String getShipmentName();
		public String getUrlPath();
		public BigDecimal getAmount();
	}
	public interface Ms2ssStatementItemPaymentOnBehalf{
		public String getDisplayName();
		public String getAmountStr();
		public String getUrlPath();
		public BigDecimal getAmount();
	}
	public interface Ms2ssStatementItemMsdcPaymentOnBehalf{
		String getName();
		String getAmount();
	}
	public interface Ms2ssStatementItemProductInventoryReturn{
		public String getDisplayName();
		public String getAmountStr();
		public BigDecimal getAmount();
	}
}
