package com.kindminds.drs.api.data.access.rdb.marketing;

import com.kindminds.drs.Filter;


import java.util.List;

public interface CampaignDao {

    List getCampaignByFilter( Filter filter);
}
