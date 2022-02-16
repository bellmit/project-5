package com.kindminds.drs.api.data.access.nosql.mongo;



import com.kindminds.drs.api.data.transfer.maketing.MarketingActivity;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.mongodb.client.result.DeleteResult;

import java.util.*;

public interface ManageMarketingActivityDao{

    DtoList<MarketingActivity> find(int pageIndex) ;

    DtoList<MarketingActivity> find(String marketplace , String supplierCode ,
                                    Date startDate , Date endDate, int pageIndex) ;

    MarketingActivity findActivityById(String activityId ) ;

    MarketingActivity create(String activityJson  ) ;

    MarketingActivity update(String activityJson ) ;

    DeleteResult delete(String activityId ) ;
}