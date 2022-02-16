package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ms2ssPurchaseAllowanceReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Ms2ssPurchaseAllowanceReportImpl implements Ms2ssPurchaseAllowanceReport {
	
	private List<Ms2ssPurchaseAllowanceReportItemGroupBySku> itemListGroupBySku;
	private Currency currency;
	private StatementInfo info;
	private Map<String,BigDecimal> otherItemAmounts;

	public Ms2ssPurchaseAllowanceReportImpl(List<Ms2ssPurchaseAllowanceReportRawItem> rawLineItems){
		this.itemListGroupBySku = this.toDetailList(rawLineItems);
	}
	
	public void setInfo(StatementInfo info) { this.info = info; }
	public void setOtherItemAmounts(Map<String,BigDecimal> itemAmounts){ this.otherItemAmounts = itemAmounts; }
	
	private List<Ms2ssPurchaseAllowanceReportItemGroupBySku> toDetailList(List<Ms2ssPurchaseAllowanceReportRawItem> rawLineItems){
		List<Ms2ssPurchaseAllowanceReportItemGroupBySku> detailList = new ArrayList<Ms2ssPurchaseAllowanceReportItemGroupBySku>();
		for(Ms2ssPurchaseAllowanceReportRawItem rawLineItem : rawLineItems){
			Ms2ssPurchaseAllowanceReportItemGroupBySku target = null;
			for(Ms2ssPurchaseAllowanceReportItemGroupBySku detailItem:detailList){
				if(detailItem.getSku().equals(rawLineItem.getSku())){
					target=detailItem;
					break;
				}
			}
			if(target==null){
				target = new Ms2ssPurchaseAllowanceReportItemGroupBySkuImpl(rawLineItem.getSku(), new ArrayList<Ms2ssPurchaseAllowanceReportRawItem>());
				detailList.add(target);
			}
			target.getRawItems().add(rawLineItem);
		}
		return detailList;
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
	public List<Ms2ssPurchaseAllowanceReportItemGroupBySku> getItemsGroupBySku() {
		return this.itemListGroupBySku;
	}

	@Override
	public String getSubTotal() {
		BigDecimal subtotal = BigDecimal.ZERO;
		for(Ms2ssPurchaseAllowanceReportItemGroupBySku detail:this.itemListGroupBySku){
			subtotal=subtotal.add(detail.getSubtotal());
		}
		for(BigDecimal otherAmount:this.otherItemAmounts.values()){
			subtotal=subtotal.add(otherAmount);
		}
		return subtotal.stripTrailingZeros().toPlainString();
	}

	@Override
	public Currency getCurrency() {
		return this.currency;
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
	public Map<String, String> getOtherItemAmounts() {
		if(this.otherItemAmounts==null) return null;
		Map<String,String> result = new LinkedHashMap<>();
		for(String item:this.otherItemAmounts.keySet()){
			result.put(item, otherItemAmounts.get(item).setScale(2).toPlainString());
		}
		return result;
	}

}
