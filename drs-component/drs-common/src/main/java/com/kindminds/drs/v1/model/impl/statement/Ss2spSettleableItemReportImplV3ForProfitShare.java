package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.v1.model.report.Ss2spSettleableItemReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

public class Ss2spSettleableItemReportImplV3ForProfitShare implements Ss2spSettleableItemReport {

	private TransactionLineType settleableItem;
	private StatementInfo info;
	private Currency currency;
	private List<Ss2spSettleableItemReportLineItem> lineItems=null;

	public void setSettleableItem(TransactionLineType item){
		this.settleableItem = item;
	}
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	
	public void setCurrency(Currency currency){
		this.currency = currency;
	}

	public void setLineItems(List<Ss2spSettleableItemReportLineItem> lineItems){
		this.lineItems=lineItems;
	}

	@Override
	public String toString() {
		return "Ss2spSettleableItemReportImplV3ForProfitShare [getSettleableItemName()=" + getSettleableItemName()
				+ ", getDateStart()=" + getDateStart() + ", getDateEnd()=" + getDateEnd() + ", getIsurKcode()="
				+ getIsurKcode() + ", getRcvrKcode()=" + getRcvrKcode() + ", getCurrency()=" + getCurrency()
				+ ", getTotal()=" + getTotal() + ", getItemList()=" + getItemList() + "]";
	}

	@Override
	public String getSettleableItemName(){
		Assert.notNull(settleableItem);
		return this.settleableItem.getName();
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
	public String getCurrency() {
		return this.currency.name();
	}

	@Override
	public String getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for(Ss2spSettleableItemReportLineItem item:this.lineItems){
			total=total.add(item.getNumericAmount());
		}
		return total.toPlainString();
	}

	@Override
	public List<Ss2spSettleableItemReportLineItem> getItemList() {
		return this.lineItems;
	}
	
}
