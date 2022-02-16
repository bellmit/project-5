package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareDetailReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Ss2spProfitShareDetailReportImplV4 implements Ss2spProfitShareDetailReport {
	
	private final int versionNumber = 4;
	private StatementInfo info;
	private List<Ss2spProfitShareDetailReportSkuProfitShareItem> skuProfitShareItems;
	private List<Ss2spProfitShareDetailOfProductSku> detailListOfSku = null;
	private Map<String,BigDecimal> otherItemAmounts;
	private Currency currency;
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}

	public void setCurrency(Currency currency){this.currency=currency;}
	public void setSkuProfitShareItems(List<Ss2spProfitShareDetailReportSkuProfitShareItem> items){this.skuProfitShareItems=items;}
	public void setDetailListOfSku(List<Ss2spProfitShareDetailOfProductSku> list){ this.detailListOfSku=list; }
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
		return this.detailListOfSku;
	}
	@Override
	public List<Ss2spProfitShareDetail> getProfitShareDetailList() {
		return null;
	}

	@Override
	public String getSubTotal() {
		BigDecimal subtotal = BigDecimal.ZERO;
		for(Ss2spProfitShareDetailReportSkuProfitShareItem item:this.getSkuProfitShareItems()){
			subtotal = subtotal.add(((Ss2spProfitShareDetailReportSkuProfitShareItemImpl)item).getRawSubtotal());
		}
		if(this.detailListOfSku!=null){
			for(Ss2spProfitShareDetailOfProductSku skuDetail:this.detailListOfSku){
				subtotal=subtotal.add(new BigDecimal(skuDetail.getSubtotal()));
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

		Map<String,String> result = new LinkedHashMap<>();
		for(String item:this.otherItemAmounts.keySet()){
			if(item.contains("&")) {
				String[] idtivs =item.split("&",2);
				String amountivs =idtivs[1]+" "+(otherItemAmounts.get(item).toPlainString());
				result.put(idtivs[0],amountivs);

			}else{
				result.put(item, otherItemAmounts.get(item).toPlainString());
			}
		}
		return result;
	}

}
