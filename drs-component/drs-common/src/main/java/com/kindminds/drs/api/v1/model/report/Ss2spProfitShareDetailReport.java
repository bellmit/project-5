package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Currency;

import java.util.List;
import java.util.Map;

public interface Ss2spProfitShareDetailReport {
	public int getVersionNumber();
	public String getDateStart();
	public String getDateEnd();
	public String getIsurKcode();
	public String getRcvrKcode();
	public String getSubTotal();
	public Currency getCurrency();
	public List<Ss2spProfitShareDetailReportSkuProfitShareItem> getSkuProfitShareItems();
	public List<Ss2spProfitShareDetail> getProfitShareDetailList();
	public List<Ss2spProfitShareDetailOfProductSku> getDetailListOfProductSku();
	public Map<String,String> getOtherItemAmounts();
	
	public interface Ss2spProfitShareDetailReportSkuProfitShareItem{
		String getSku();
		String getName();
		String getShippedQty();
		String getRefundedQty();
		String getSubtotal();
	}
	
	public interface Ss2spProfitShareDetail{
		public String getSku();
		public String getSkuName();
		public String getSubtotal();
		public Map<String,String> getItemNameToAmountMap();
	}
	
	public interface Ss2spProfitShareDetailOfProductSku{
		public String getProductSkuCode();
		public String getProductSkuName();
		public String getSubtotal();
		public Map<String,String> getItemNameToAmountMap();
	}
	
	
}
