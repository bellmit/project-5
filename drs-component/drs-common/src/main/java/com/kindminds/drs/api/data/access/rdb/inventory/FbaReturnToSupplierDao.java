package com.kindminds.drs.api.data.access.rdb.inventory;



import com.kindminds.drs.api.data.transfer.fba.FbaReturnToSupplierItem;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public interface FbaReturnToSupplierDao {

    Boolean queryFbaDataExists(OffsetDateTime dateTime);
    Timestamp queryLastPeriodEnd();
    Map<String, List<String>> queryIvsToUnsMarketplace(List<String> ivsList);
    Integer insertSellbackRecords(List<FbaReturnToSupplierItem> sellbackRecords);
}
