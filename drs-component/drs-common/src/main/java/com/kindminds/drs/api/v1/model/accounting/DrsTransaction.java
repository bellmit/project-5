package com.kindminds.drs.api.v1.model.accounting;

import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface DrsTransaction {
	public enum DrsTransactionType{
		FBA_RETURN_TO_SUPPLIER("FBA Return to Supplier"),
		FBA_RETURNS_TAIWAN("FBA Returns Taiwan"),
		FBA_RETURNS_OTHER("FBA Returns Other"),
		FBA_RETURNS_RECOVERY("FBA Returns Recovery"),
		FBA_RETURNS_DISPOSE("FBA Returns Dispose");

		private String textValue;
		private static List<String> fbaReturnRecoveryTypes = initializeFbaTypes();
		private static List<String> fbaReturnTypes = initializeFbaTypesNoRecovery();

		private static List<String> initializeFbaTypes() {
			List<String> fbaTypes = new ArrayList<>();
			fbaTypes.add(FBA_RETURN_TO_SUPPLIER.getTextValue());
			fbaTypes.add(FBA_RETURNS_TAIWAN.getTextValue());
			fbaTypes.add(FBA_RETURNS_OTHER.getTextValue());
			fbaTypes.add(FBA_RETURNS_DISPOSE.getTextValue());
			fbaTypes.add(FBA_RETURNS_RECOVERY.getTextValue());
			return fbaTypes;
		}

		private static List<String> initializeFbaTypesNoRecovery() {
			List<String> fbaTypes = new ArrayList<>();
			fbaTypes.add(FBA_RETURN_TO_SUPPLIER.getTextValue());
			fbaTypes.add(FBA_RETURNS_TAIWAN.getTextValue());
			fbaTypes.add(FBA_RETURNS_OTHER.getTextValue());
			fbaTypes.add(FBA_RETURNS_DISPOSE.getTextValue());
			return fbaTypes;
		}

		DrsTransactionType(String textValue) {
			this.textValue=textValue;
		}
		public String getTextValue() {
			return this.textValue;
		}

		public static List<String> getFbaReturnTypes() {
			return fbaReturnRecoveryTypes;
		}

		public static List<String> getFbaReturnTypesNoRecovery() {
			return fbaReturnTypes;
		}

		public static Boolean containsType(String type) {
			return fbaReturnRecoveryTypes.contains(type);
		}
	}
	String getType();
	Date getTransactionDate();
	Integer getTransactionSequence();
	String getProductSku();
	Integer getQuantity();
	Marketplace getMarketplace();
	String getSourceTransactionId();
	String getShipmentIvsName();
	String getShipmentUnsName();
	DrsTransactionLineItemSource getSettleableItemElements();
	List<DrsTransactionLineItem> getSettleableItemList();
	
	public interface DrsTransactionLineItemSource{
		Currency getCurrency();
		BigDecimal getPretaxPrincipalPrice();
		BigDecimal getMsdcRetainment();
		BigDecimal getMarketplaceFee();
		BigDecimal getMarketplaceFeeNonRefundable();
		BigDecimal getFulfillmentFee();
		BigDecimal getSsdcRetainment();
		BigDecimal getFcaInMarketsideCurrency();
		BigDecimal getSpProfitShare();
		BigDecimal getRefundFee();
	}
	
	public interface DrsTransactionLineItem{
		Integer getLineSeq();
		String getIsurKcode();
		String getRcvrKcode();
		String getItemName();
		String getCurrency();
		BigDecimal getAmount();
	}
	
}
