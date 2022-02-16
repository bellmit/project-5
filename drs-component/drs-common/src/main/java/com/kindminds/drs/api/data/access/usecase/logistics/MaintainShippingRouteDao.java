package com.kindminds.drs.api.data.access.usecase.logistics;

import java.util.Date;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;

public interface MaintainShippingRouteDao {
		
	public Date queryShippingDate(Date startDate, Country exportCountry, Country marketCountry, ShippingMethod shippingMethod);
	public Date queryShippingDate(Date startDate,Date endDate,Country exportCountry,Country marketCountry,ShippingMethod shippingMethod);		

}