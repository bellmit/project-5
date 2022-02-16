package com.kindminds.drs.core.biz.marketing.repo;



import com.kindminds.drs.Filter;


import com.kindminds.drs.api.v2.biz.domain.model.CampaignRepo;
import com.kindminds.drs.api.v2.biz.domain.model.CampaignSetRepo;
import com.kindminds.drs.api.v2.biz.domain.model.marketing.Campaign;
import com.kindminds.drs.api.v2.biz.domain.model.marketing.CampaignSet;
import com.kindminds.drs.core.biz.marketing.CampaignSetImpl;

import java.util.*;

public class CampaignSetRepoImpl implements CampaignSetRepo {

    CampaignRepo campaignRepo = new  CampaignRepoImpl();


    @Override
    public void add(CampaignSet item) {

    }

    @Override
    public void add(List<CampaignSet> items) {

    }

    @Override
    public void edit(CampaignSet item) {

    }

    @Override
    public void remove(CampaignSet item) {

    }

    @Override
    public Optional<CampaignSet> findById(String id) {
        return Optional.empty();
    }

    public  Optional<CampaignSet>  findOne(Filter filter){


        List<Campaign> cList = campaignRepo.find(filter);
        CampaignSet set = new CampaignSetImpl(cList);

        return Optional.of(set);


    }

    @Override
    public List<CampaignSet> find(Filter filter) {
        return null;
    }


}