package com.kindminds.drs.api.data.access.rdb.ec;




import com.kindminds.drs.api.data.transfer.ec.AutoProcessECReport;
import com.kindminds.drs.api.data.transfer.ec.ProcessResult;

import java.time.ZonedDateTime;
import java.util.List;

public interface AutoProcessECReportDao {


    void save(AutoProcessECReport processECReport);

    List<ProcessResult> getProcessResultList(int reportType , ZonedDateTime startTime , ZonedDateTime endTime);


}