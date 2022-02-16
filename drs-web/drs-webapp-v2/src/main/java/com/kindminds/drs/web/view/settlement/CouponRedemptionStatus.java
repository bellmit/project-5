package com.kindminds.drs.web.view.settlement;

import java.io.Serializable;
import java.util.List;

public class CouponRedemptionStatus implements Serializable{
	
	private static final long serialVersionUID = 8091312582159103809L;
	private List<InternationalTransaction> processedCouponRedemptionItems;
	private List<CouponRedemption> unprocessedCouponRedemptionItems;
		
	public List<InternationalTransaction> getProcessedCouponRedemptions(){
		return this.processedCouponRedemptionItems;		
	};
	
	public void setProcessedCouponRedemptions(List<InternationalTransaction> processedCouponRedemptionItems){
		this.processedCouponRedemptionItems = processedCouponRedemptionItems;		
	};
		
    public List<CouponRedemption> getUnprocessedCouponRedemptions(){
    	return this.unprocessedCouponRedemptionItems;  	
    };
	
    public void setUnprocessedCouponRedemptions(List<CouponRedemption> unprocessedCouponRedemptionItems){
    	this.unprocessedCouponRedemptionItems = unprocessedCouponRedemptionItems;  	
    };
      
}