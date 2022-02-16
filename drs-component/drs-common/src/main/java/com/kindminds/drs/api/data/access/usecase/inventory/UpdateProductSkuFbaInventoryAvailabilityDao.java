package com.kindminds.drs.api.data.access.usecase.inventory;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.api.v1.model.product.ProductSkuFbaInventoryAvailabilityLine;

public interface UpdateProductSkuFbaInventoryAvailabilityDao {
	Boolean selectIfNullDataExist(Date date);
	int updateProductSkuInventoryHistoryAvailability(Date date,List<ProductSkuFbaInventoryAvailabilityLine> lines);
	int updateProductSkuInventoryHistoryAvailabilityWithNullValue(Date date,Boolean newValue);
	int updateProductSkuInventoryHistoryAvailabilityAsNull(Date date);
	List<Date> queryDatesWithAvailabilityNull(Date start,Date end);
	List<Date> queryDatesWithAvailabilityNotNull(Date start,Date end);
}
