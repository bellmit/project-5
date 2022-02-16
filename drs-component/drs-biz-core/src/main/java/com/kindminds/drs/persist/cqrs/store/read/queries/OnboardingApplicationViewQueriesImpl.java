package com.kindminds.drs.persist.cqrs.store.read.queries;

import com.kindminds.drs.api.data.cqrs.store.read.queries.OnboardingApplicationViewQueries;

import com.kindminds.drs.api.data.transfer.productV2.onboarding.OnboardingApplication;
import com.kindminds.drs.data.transfer.productV2.onboarding.OnboardingApplicationLineitem;
import com.kindminds.drs.enums.OnboardingApplicationStatusType;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.util.Encryptor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OnboardingApplicationViewQueriesImpl extends Dao implements OnboardingApplicationViewQueries {






    @Override
    public Optional<String> getSerialNumber(String productBaseCode) {

        String sql = "select serial_number " +
                "from product.onboarding_application_view poav " +
                "where poav.product_base_code = :product_base_code";
      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("product_base_code", productBaseCode);

        String seq = null;
        try{
            seq =  getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
        } catch (EmptyResultDataAccessException e) {
        }

        return Optional.ofNullable(seq);

    }

    @Override
    public Optional<List<String>> getSerialNumbersBySupplier(String companyKcode) {
        String sql = "select serial_number " +
                "from product.onboarding_application_view poav " +
                "inner join company c on poav.supplier_company_id = c.id " +
                "where c.k_code = :company_k_code and poav.status = 0 ";
      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("company_k_code", companyKcode);
        List rList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        return rList.size() > 0 ? Optional.of(rList) : Optional.of(new ArrayList<String>());
    }

    @Override
    public List<OnboardingApplication> getOnboardingApplications(String kcode) {

        String sql = "select poav.onboarding_application_id , " +
                "poav.create_time, poav.update_time, poav.serial_number, poav.status " +
                "from product.onboarding_application_view poav " +
                "where poav.supplier_company_id = " +
                "(select c.id from company c where c.k_code = :kcode)";
      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("kcode", kcode);

        List<OnboardingApplication> onList = new ArrayList<OnboardingApplication>();
        List<Object[]> rList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        for (Object[] columns : rList) {
            OnboardingApplication on =
                    new OnboardingApplication(
                            columns[0].toString(),
                            kcode ,
                            OnboardingApplicationStatusType.fromKey((int)columns[4]) ,
                            columns[3] == null ? "" : columns[3].toString() , null);

            on.setOnboardingApplicationLineitems(this.getOaLineitems(on.getId(), on.getSupplierKcode(),on.getSerialNumber()));

            onList.add(on);
        }

        return onList;
    }

    @Override
    public List<OnboardingApplication> getOnboardingApplications() {

        String sql = "select poav.onboarding_application_id, " +
                "poav.create_time, poav.update_time, poav.serial_number, poav.status, c.k_code " +
                "from product.onboarding_application_view poav " +
                "inner join company c on poav.supplier_company_id = c.id ";


      MapSqlParameterSource q = new MapSqlParameterSource();

        List<OnboardingApplication> onList = new ArrayList<OnboardingApplication>();
        List<Object[]> rList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        for (Object[] columns : rList) {

            OnboardingApplication on =
                    new OnboardingApplication(
                            (String)columns[0],
                            (String)columns[5] ,
                            OnboardingApplicationStatusType.fromKey((int)columns[4]) ,
                            columns[3] == null ? "" : columns[3].toString() , null);

            on.setOnboardingApplicationLineitems(this.getOaLineitems(on.getId(), on.getSupplierKcode(),on.getSerialNumber()));

            onList.add(on);
        }

        return onList;
    }

    private List<OnboardingApplicationLineitem> getOaLineitems(String onboardingApplicationId ,
                                                               String kcode,
                                                               String serialNumber){

        String sql = "select onboarding_application_lineitem_id  , " +
                "  product_base_code , status " +
                "from product.onboarding_application_lineitem_view  " +
                " where onboarding_application_id = :id and product_market_side = 0 ";

      MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", Encryptor.decrypt(onboardingApplicationId , false));

        List<OnboardingApplicationLineitem> itemList = new ArrayList<OnboardingApplicationLineitem>();
        List<Object[]> rList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);



        for (Object[] columns : rList) {

            OnboardingApplicationLineitem item =
                    new OnboardingApplicationLineitem(
                            (String)columns[0],
                            (String)columns[1] ,
                            kcode,
                            null,
                            (int)columns[2] ,
                            serialNumber);


            itemList.add(item);
        }

        return itemList;
    }





}
