package com.kindminds.drs.api.data.transfer.ec;

import java.time.LocalDate;

public interface AutoProcessECReport extends ECReport {

    Integer getMarketPlaceId();


    Integer getReportType();


    LocalDate getScheduledDate();


    Boolean getSuccess();
}
