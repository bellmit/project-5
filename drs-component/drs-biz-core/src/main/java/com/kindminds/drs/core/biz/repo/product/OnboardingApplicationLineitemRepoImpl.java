package com.kindminds.drs.core.biz.repo.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.Filter;

import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitem;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitemStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.EvalProductInfoStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.OnboardingApplicationLineitemRepo;

import com.kindminds.drs.core.biz.product.ProductImpl;
import com.kindminds.drs.core.biz.product.onboarding.OnboardingApplicationLineitemImpl;

import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding.OnboardingApplicationLineitemDao;
import com.kindminds.drs.api.data.es.dao.productV2.onboarding.OnboardingApplicationLineitemDetailDao;

import com.kindminds.drs.service.util.SpringAppCtx;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OnboardingApplicationLineitemRepoImpl implements OnboardingApplicationLineitemRepo {

    private OnboardingApplicationLineitemDao dao = (OnboardingApplicationLineitemDao) SpringAppCtx.get().
            getBean(OnboardingApplicationLineitemDao.class);

    private OnboardingApplicationLineitemDetailDao detailDao = (OnboardingApplicationLineitemDetailDao) SpringAppCtx.get().
            getBean(OnboardingApplicationLineitemDetailDao.class);

    /*

    @Override
    public Optional<String> getId(String onboardingApplicationId, Country marketSide) {
        return dao.getId(onboardingApplicationId, marketSide);
    }

    @Override
    public Optional<List<String>> getIds(String onboardingApplicationId) {
        return dao.getIds(onboardingApplicationId);
    }
*/
    @Override
    public Optional<OnboardingApplicationLineitem> findById(String id) {


        List<Object[]> resultList = dao.findOnboardingApplicationLineitem(id);
        if(resultList.size() >0 ){

            Object [] obj = resultList.get(0);
            String itemId = obj[0].toString();
            OffsetDateTime createTime =
                    OffsetDateTime.ofInstant(((Timestamp)obj[1]).toInstant(), ZoneId.of("Asia/Taipei"));

            String onboardingApplicationId = obj[2].toString();
            OffsetDateTime onboardingApplicationCreateTime =
                    OffsetDateTime.ofInstant(((Timestamp)obj[3]).toInstant(), ZoneId.of("Asia/Taipei"));

            //todo product
            ProductImpl p = null;

            OnboardingApplicationLineitemStatusType status = OnboardingApplicationLineitemStatusType.fromKey(
                    Integer.parseInt(obj[8].toString()));

            List<EvalProductInfoStatusType> evalProductInfoStatus = new ArrayList<EvalProductInfoStatusType>();;
            String rawEvalPIStatus = obj[9] == null ? "" : obj[9].toString();

            if(StringUtils.hasText(rawEvalPIStatus)){
                ObjectMapper mapper = new ObjectMapper();
                try {
                    List<Integer> evalPIStatusKeyAry = mapper.readValue(rawEvalPIStatus ,
                            new TypeReference<List<Integer>>(){});
                    if(evalPIStatusKeyAry.size() > 0){
                        evalPIStatusKeyAry.forEach(x->{
                            evalProductInfoStatus.add(EvalProductInfoStatusType.fromKey(x));
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return  Optional.of(OnboardingApplicationLineitemImpl.valueOf(itemId ,createTime,onboardingApplicationId,
                    onboardingApplicationCreateTime , p ,status, evalProductInfoStatus));

        }

        return Optional.empty();
    }

    @Override
    public void add(OnboardingApplicationLineitem onboardingApplicationLineitem) {

        dao.save(onboardingApplicationLineitem);

        if(onboardingApplicationLineitem.getTrialList().isPresent()){
           detailDao.insert(onboardingApplicationLineitem.getTrialList().get());
        }else if(onboardingApplicationLineitem.getEvalSample().isPresent()){
            detailDao.insert(onboardingApplicationLineitem.getEvalSample().get());
        }else if(onboardingApplicationLineitem.getPresentSample().isPresent()){
            detailDao.insert(onboardingApplicationLineitem.getPresentSample().get());
        }else if(onboardingApplicationLineitem.getApproveSample().isPresent()){
            detailDao.insert(onboardingApplicationLineitem.getApproveSample().get());
        }else if(onboardingApplicationLineitem.getGiveFeedback().isPresent()){
            detailDao.insert(onboardingApplicationLineitem.getGiveFeedback().get());
        }else if(onboardingApplicationLineitem.getProvideComment().isPresent()){
            detailDao.insert(onboardingApplicationLineitem.getProvideComment().get());
        }else if(onboardingApplicationLineitem.getCheckComplianceAndCertAvailability().isPresent()){
            detailDao.insert(onboardingApplicationLineitem.getCheckComplianceAndCertAvailability().get());
        }else if(onboardingApplicationLineitem.getConfirmInsurance().isPresent()){
            detailDao.insert(onboardingApplicationLineitem.getConfirmInsurance().get());
        }else if(onboardingApplicationLineitem.getCheckInsurance().isPresent()){
            detailDao.insert(onboardingApplicationLineitem.getCheckInsurance().get());
        }else if(onboardingApplicationLineitem.getCheckProfitability().isPresent()){
            detailDao.insert(onboardingApplicationLineitem.getCheckProfitability().get());
        }


    }


    @Override
    public void edit(OnboardingApplicationLineitem onboardingApplicationLineitem) {
        throw new NotImplementedException();
    }

    @Override
    public void remove(OnboardingApplicationLineitem onboardingApplicationLineitem) {
        throw new NotImplementedException();
    }

    @Override
    public void add(List<OnboardingApplicationLineitem> items) {

    }

    @Override
    public List<OnboardingApplicationLineitem> find( Filter filter) {
        return null;
    }



    @Override
    public Optional<OnboardingApplicationLineitem> findOne( Filter filter) {
        return Optional.empty();
    }
}
