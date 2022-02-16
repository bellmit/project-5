package com.kindminds.drs.api.data.access.usecase.report.amazon;

import com.kindminds.drs.api.v1.model.report.ManageFbaInventoryReportItem;

import java.util.Date;

public interface ManageFbaInventoryFileImporterDao {

    String queryDrsSkuByMarketplaceSku(String marketplaceSku, Integer marketplaceId);
    Boolean queryDataExists(Date today, String countryCode, String fnsku);
    Integer insertManageFbaInventoryItem(ManageFbaInventoryReportItem item);

}
