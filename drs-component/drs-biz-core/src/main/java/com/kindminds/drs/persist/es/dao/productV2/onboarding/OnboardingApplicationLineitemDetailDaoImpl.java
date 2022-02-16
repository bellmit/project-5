package com.kindminds.drs.persist.es.dao.productV2.onboarding;

import com.kindminds.drs.api.data.es.dao.productV2.onboarding.OnboardingApplicationLineitemDetailDao;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.*;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;




import java.sql.Timestamp;
import java.time.ZoneId;

@Repository
public class OnboardingApplicationLineitemDetailDaoImpl extends Dao implements OnboardingApplicationLineitemDetailDao {




    @Override
    @Transactional("transactionManager")
    public void insert(OnboardingApplicationLineitemDetail detail) {

        String tableName = "";
        String sql = "insert into product." ;

        if(detail instanceof TrialListing){
            tableName = "onboarding_application_lineitem_trial_listing" ;
        }else if(detail instanceof EvalSample){
            tableName = "onboarding_application_lineitem_eval_sample" ;
        }else if(detail instanceof PresentSample){
            tableName = "onboarding_application_lineitem_present_sample" ;
        }else if(detail instanceof ApproveSample){
            tableName = "onboarding_application_lineitem_approve_sample" ;
        }else if(detail instanceof GiveFeedback) {
            tableName = "onboarding_application_lineitem_give_feedback";
        }else if(detail instanceof ProvideComment){
                tableName = "onboarding_application_lineitem_provide_comment" ;
        }else if(detail instanceof CheckInsurance){
            tableName = "onboarding_application_lineitem_check_insurance" ;
        }else if(detail instanceof ConfirmInsurance){
            tableName = "onboarding_application_lineitem_confirm_insurance" ;
        }else if(detail instanceof CheckProfitability){
            tableName = "onboarding_application_lineitem_check_profitability" ;
        }else if(detail instanceof CheckComplianceAndCertAvailability){
            tableName = "onboarding_application_lineitem_check_cp_ca" ;
        }

        sql=  sql + tableName
                + " ( onboarding_application_lineitem_id, create_time, data, submitted ) values "
                + "  ( :lineitemId , :createTime , CAST(:data as json) , :submitted )";


        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("lineitemId", detail.getOnboardingApplicationLineitemId());
        q.addValue("createTime",  Timestamp.valueOf(detail.getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("data", detail.getData());

        q.addValue("submitted",  Integer.toString(BooleanUtils.toInteger(detail.isSubmitted())));


        getNamedParameterJdbcTemplate().update(sql,q);

    }


}
