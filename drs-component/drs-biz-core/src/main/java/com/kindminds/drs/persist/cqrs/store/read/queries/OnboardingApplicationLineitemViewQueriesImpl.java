package com.kindminds.drs.persist.cqrs.store.read.queries;

import com.kindminds.drs.api.data.cqrs.store.read.queries.OnboardingApplicationLineitemViewQueries;
import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductViewQueries;

import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.data.transfer.productV2.onboarding.OnboardingApplicationLineitem;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.Optional;

@Repository
public class OnboardingApplicationLineitemViewQueriesImpl extends Dao implements OnboardingApplicationLineitemViewQueries {



    @Autowired
    private ProductViewQueries pViewQueries;

    @Override
    public Optional<OnboardingApplicationLineitem> getOnboardingApplicationLineitem(String onboardingApplicationLineitemId) {


        String sql = "select onboarding_application_lineitem_id  , " +
                "  product_base_code , status " +
                "from product.onboarding_application_lineitem_view  " +
                " where onboarding_application_lineitem_id = :id and product_market_side = 0 ";


      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",onboardingApplicationLineitemId);


        List<Object[]> rList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        if(rList.size()>0){

            for (Object[] columns : rList) {

                String bpCode = (String)columns[1];
                ProductDto p =
                       pViewQueries.getInActivateBaseProductOnboarding(bpCode);



                OnboardingApplicationLineitem item =
                        new OnboardingApplicationLineitem(
                                (String)columns[0],
                                bpCode,
                                "",
                                p,
                                (int)columns[2] ,
                                "");


               return Optional.of(item);
            }

        }


        return Optional.empty();
    }




}
