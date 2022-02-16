package com.kindminds.drs.api.data.access.usecase.inventory;

import java.util.Date;
import java.util.List;

public interface ViewInventoryHealthReportDao {
	Date querySnapshotDate(int marketplaceId);
	List<Object []> queryLineItems(int marketplaceId, String supplierKcode);
}
