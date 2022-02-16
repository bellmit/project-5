package com.kindminds.drs.core.biz.repo.product;

import com.kindminds.drs.Filter;


import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplication;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.OnboardingApplicationRepo;
import com.kindminds.drs.core.biz.product.onboarding.OnboardingApplicationImpl;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding.OnboardingApplicationDao;
import com.kindminds.drs.enums.OnboardingApplicationStatusType;

import com.kindminds.drs.service.util.SpringAppCtx;


import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class OnboardingApplicationRepoImpl implements OnboardingApplicationRepo {

    private OnboardingApplicationDao dao = (OnboardingApplicationDao) SpringAppCtx.get().
            getBean(OnboardingApplicationDao.class);




    @Override
    public Optional<OnboardingApplication> findById(String id) {


        List<Object[]> resultList = dao.findOnboardingApplicationById(id);
        if(resultList.size() >0 ){

            Object [] obj = resultList.get(0);
            String oaId = obj[0].toString();
            OffsetDateTime createTime =
                    OffsetDateTime.ofInstant(((Timestamp)obj[1]).toInstant(), ZoneId.of("Asia/Taipei"));

            int supplierCompanyId = Integer.parseInt(obj[2].toString());

            String serialNumber = obj[3] == null ? "":obj[3].toString();
            OnboardingApplicationStatusType status =
                    OnboardingApplicationStatusType.fromKey(Integer.parseInt(obj[4].toString()));


            return  Optional.of(OnboardingApplicationImpl.valueOf(oaId , createTime , supplierCompanyId ,
                      serialNumber , status));

        }

        return Optional.empty();
    }


    @Override
    public void add(OnboardingApplication item) {

    }

    @Override
    public void add(List<OnboardingApplication> items) {

    }

    @Override
    public void edit(OnboardingApplication item) {

    }

    @Override
    public void remove(OnboardingApplication item) {

    }

    @Override
    public List<OnboardingApplication> find( Filter filter) {
        return null;
    }

    @Override
    public Optional<OnboardingApplication> findOne( Filter filter) {
        return Optional.empty();
    }
}
