package com.kindminds.drs.v1.model.impl.statement;


import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareDetailReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ss2spProfitShareDetailReportImplV1 implements Ss2spProfitShareDetailReport{
	
	private final int versionNumber = 1;
	
	private StatementInfo info;
	private List<Ss2spProfitShareDetailReportSkuProfitShareItem> skuProfitShareItems;
	private List<Ss2spProfitShareDetail> detailList;
	private Map<String,BigDecimal> otherItemAmounts;
	private Currency currency;
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	public void setCurrency(Currency currency){this.currency=currency;}
	public void setSkuProfitShareItems(List<Ss2spProfitShareDetailReportSkuProfitShareItem> items){this.skuProfitShareItems=items;}
	public void setItemList(List<Ss2spProfitShareDetail> detailList){this.detailList = detailList;}
	public void setOtherItemAmounts(Map<String,BigDecimal> itemAmounts){ this.otherItemAmounts = itemAmounts; }
	public Map<String,BigDecimal> getRawOtherItemAmounts(){ return this.otherItemAmounts; }

	@Override
	public int getVersionNumber() {
		return this.versionNumber;
	}

	@Override
	public String getDateStart() {
		return this.info.getDateStart();
	}
	
	@Override
	public String getDateEnd() {
		return this.info.getDateEnd();
	}
	
	@Override
	public String getIsurKcode() {
		return this.info.getIsurKcode();
	}

	@Override
	public String getRcvrKcode() {
		return this.info.getRcvrKcode();
	}
	
	@Override
	public List<Ss2spProfitShareDetailReportSkuProfitShareItem> getSkuProfitShareItems() {
		return this.skuProfitShareItems;
	}

	@Override
	public List<Ss2spProfitShareDetailOfProductSku> getDetailListOfProductSku() {
		return null;
	}

	@Override
	public List<Ss2spProfitShareDetail> getProfitShareDetailList() {
		return this.detailList;
	}

	@Override
	public String getSubTotal() {
		BigDecimal subtotal = BigDecimal.ZERO;
		for(Ss2spProfitShareDetailReportSkuProfitShareItem item:this.getSkuProfitShareItems()){
			subtotal = subtotal.add(((Ss2spProfitShareDetailReportSkuProfitShareItemImpl)item).getRawSubtotal());
		}
		if(this.detailList!=null){
			for(Ss2spProfitShareDetail detail:this.detailList){
				subtotal=subtotal.add(((Ss2spProfitShareDetailImplV1)detail).getNumericSubtotal());
			}
		}
		if(this.otherItemAmounts!=null){
			for(BigDecimal otherAmount:this.otherItemAmounts.values()){
				subtotal=subtotal.add(otherAmount);
			}
		}
		return subtotal.setScale(this.getCurrency().getScale(),RoundingMode.HALF_UP).toString();
	}

	@Override
	public Currency getCurrency() {
		Assert.notNull(this.currency);
		return this.currency;
	}

	@Override
	public Map<String,String> getOtherItemAmounts() {
		if(this.otherItemAmounts==null) return null;
		Map<String,String> result = new HashMap<>();
		for(String item:this.otherItemAmounts.keySet()){
			result.put(item, otherItemAmounts.get(item).toPlainString());
		}
		return result;
	}

}
