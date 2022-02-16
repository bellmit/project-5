package com.kindminds.drs.api.adapter;


import com.kindminds.drs.Marketplace;
import com.kindminds.drs.MwsReportType;

import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.util.List;

public interface AmazonMwsReportAdapter {

    String requestReport(MwsReportType reportType , Marketplace marketplace);

    String requestReport(MwsReportType reportType , Marketplace marketplace ,
                         ZonedDateTime startDate , ZonedDateTime endDate);

    OutputStream getReport(String reportId , Marketplace marketplace);

    String getReportId(String requestId ,MwsReportType reportType , Marketplace marketplace);

    List<String> getReportIds(MwsReportType reportType , Marketplace marketplace);

    List<String> getScheduledReportIds(MwsReportType reportType , Marketplace marketplace);

    String getReportListByNextToken(String token , String requestId , Marketplace marketplace);


}
