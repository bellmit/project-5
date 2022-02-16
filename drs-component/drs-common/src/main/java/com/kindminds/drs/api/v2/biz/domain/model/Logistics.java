package com.kindminds.drs.api.v2.biz.domain.model;

import java.util.Date;

import com.kindminds.drs.Warehouse;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpent;

public interface Logistics {

	public Date calculateRecommendedFcaDeliveryDate(Date expectedStockDepletionDate, ReplenishmentTimeSpent timeSpent, Warehouse warehouse);
	public Date calculateEstimatedReceivingCompletionDate(Date expectedStockDepletionDate, ReplenishmentTimeSpent timeSpent,Warehouse warehouse);
	
}
