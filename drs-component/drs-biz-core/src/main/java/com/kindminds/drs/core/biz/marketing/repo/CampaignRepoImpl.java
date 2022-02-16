package com.kindminds.drs.core.biz.marketing.repo;

import com.kindminds.drs.api.data.row.marketing.CampaignRow;

import com.kindminds.drs.Filter;

import com.kindminds.drs.api.v2.biz.domain.model.CampaignRepo;
import com.kindminds.drs.api.v2.biz.domain.model.marketing.Campaign;
import com.kindminds.drs.core.biz.marketing.CampaignImpl;
import com.kindminds.drs.api.data.access.rdb.marketing.CampaignDao;
import com.kindminds.drs.service.util.SpringAppCtx;

import java.util.*;

public  class CampaignRepoImpl implements CampaignRepo {

    private CampaignDao dao = SpringAppCtx.get().getBean(CampaignDao.class);

    @Override
    public void add(Campaign item) {

    }

    @Override
    public void add(List<Campaign> items) {

    }

    @Override
    public void edit(Campaign item) {

    }

    @Override
    public void remove(Campaign item) {

    }

    @Override
    public Optional<Campaign> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Campaign> findOne(Filter filter) {
        return Optional.empty();
    }

    public List<Campaign> find(Filter filter) {

        List r = new ArrayList<Campaign>();
        List<CampaignRow> rList = dao.getCampaignByFilter(filter);

        rList.forEach(it -> {
            if(it != null){
                CampaignImpl ds =new CampaignImpl(it);
                r.add(ds);
            }
        });


        return r;


    }
}