package com.kindminds.drs.api.data.access.rdb.marketing;

import java.time.Instant;
import java.util.List;

public interface BuyBoxReminderDao {

    Instant queryLatestReportDate();

    List<Object []> queryLowBuyBoxSkus(Instant reportDate);



}
