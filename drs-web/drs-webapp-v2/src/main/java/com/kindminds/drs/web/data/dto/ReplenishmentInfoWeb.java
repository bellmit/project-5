package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpentInfo;

import java.util.ArrayList;
import java.util.List;



public class ReplenishmentInfoWeb {

	private List<ReplenishmentTimeSpentInfoImpl> lineItems = new ArrayList<>();
	
	public ReplenishmentInfoWeb(){};
		
	public ReplenishmentInfoWeb(List<ReplenishmentTimeSpentInfo> origlist){
		for(ReplenishmentTimeSpentInfo origItem:origlist){
			this.lineItems.add(new ReplenishmentTimeSpentInfoImpl(origItem));			
		}
		
	};
		
	public List<ReplenishmentTimeSpentInfo> getLineItems() {
		List<ReplenishmentTimeSpentInfo> items = new ArrayList<ReplenishmentTimeSpentInfo>();
		for (ReplenishmentTimeSpentInfo item : this.lineItems) {
			if(item.getWarehouseName()!= null){				
				items.add(item);
			}				
		}
		return items;
	}
		
	public List<ReplenishmentTimeSpentInfoImpl> getLineItem() {
		return this.lineItems;
	}
	
	public void setLineItem(List<ReplenishmentTimeSpentInfoImpl> lineItem) {
		this.lineItems = lineItem;		
	}
		
}