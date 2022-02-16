package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.Warehouse;
import com.kindminds.drs.api.v1.model.accounting.AmazonMonthlyStorageFeeReportRawLine;

public interface ImportAmazonMonthlyStorageFeeReportDao {

	public int insertRawLines(Warehouse sourceWarehouse,List<AmazonMonthlyStorageFeeReportRawLine> lineList);
	public boolean queryExist(Warehouse sourceWarehouse,String monthOfCharge);
		
}