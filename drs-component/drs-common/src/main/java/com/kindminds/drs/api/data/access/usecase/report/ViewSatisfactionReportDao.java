package com.kindminds.drs.api.data.access.usecase.report;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.enums.ProductNameDisplayOption;
import com.kindminds.drs.api.v1.model.report.Satisfaction;

public interface ViewSatisfactionReportDao {
	public Date queryEnd();
	public Date queryStart(int periodsBefore);
	public List<Satisfaction> query(int marketPlaceId, String supplierKCode, List<String> countedReturnReasons, ProductNameDisplayOption productNameDisplayOption);
}
