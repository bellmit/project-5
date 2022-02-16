package com.kindminds.drs.persist.data.access.rdb;

import com.kindminds.drs.api.data.cqrs.store.read.queries.MigrateDraftProductQueries;


import com.kindminds.drs.api.data.transfer.productV2.LegacyProduct;
import com.kindminds.drs.api.data.transfer.productV2.LegacyProductDetail;
import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductDetailImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;




import java.util.ArrayList;
import java.util.List;

@Repository
public class MigrateDraftProductQueriesImpl extends Dao implements MigrateDraftProductQueries {




    @Override @SuppressWarnings("unchecked")
    public LegacyProduct getBaseProductOnboardingApplication(String productBaseCode) {
        String sql = "select pis.key as id, "
                + "  supplier.k_code as supplier_kcode "
                + "from draft_product_info_source pis "
                + "inner join company supplier on supplier.id = pis.supplier_company_id "
                + "where pis.key = :productBaseCode";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferToOnboardingApplication(columnsList,productBaseCode);
    }


    private LegacyProduct transferToOnboardingApplication(List<Object[]> columnsList, String productBaseCode){
        String id ="";
        String supplierKcode ="";
        for(Object[] columns:columnsList){
            id = (String)columns[0];
            supplierKcode  = (String)columns[1];
        }

        LegacyProduct dbp =  new LegacyProduct();

        dbp.setProductBaseCode(id);
        dbp.setSupplierKcode(supplierKcode);

        dbp.setProductInfoSource(this.queryProductInfoSource(productBaseCode));

        dbp.setProductMarketingMaterialSource(this.queryProductMarketingMaterialSource(productBaseCode));

        dbp.setProductInfoMarketSide(this.queryProductInfoMarketSide(productBaseCode));

        dbp.setProductMarketingMaterialMarketSide(
                this.queryProductMarketingMaterialMarketSideOnboardingApplication(productBaseCode));


        return dbp;
    }



    @SuppressWarnings("unchecked")
    private ProductDetail queryProductInfoSource(String productBaseCode){
        String sql = "select    pis.key as id, "
                + "	 	   supplier.k_code as supplier_kcode, "
                + " CAST(pis.data as text) as json_data, "
                + "             pis.status as status "
                + "from draft_product_info_source pis "
                + "inner join company supplier on supplier.id = pis.supplier_company_id "
                + "where pis.key = :productBaseCode";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferToProductDetail(columnsList);
    }

    @SuppressWarnings("unchecked")
    private LegacyProductDetail queryProductMarketingMaterialSource(String productBaseCode){
        String sql = "select pmms.product_base_code as id, "
                + "	 	            supplier.k_code as supplier_kcode, "
                + "         CAST(pmms.data as text) as json_data, "
                + "                     pmms.status as status "
                + "from draft_product_marketing_material_source pmms "
                + "inner join company supplier on supplier.id = pmms.supplier_company_id "
                + "where pmms.product_base_code = :productBaseCode";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferToProductMMDetail(columnsList);
    }

    @SuppressWarnings("unchecked")
    private List<LegacyProductDetail> queryProductInfoMarketSide(String productBaseCode){
        String sql = "select pim.product_base_code as id, "
                + "	 	           supplier.k_code as supplier_kcode, "
                + "         CAST(pim.data as text) as json_data, "
                + "                     pim.status as status "
                + "from draft_product_info_marketside pim "
                + "inner join company supplier on supplier.id = pim.supplier_company_id "
                + "where pim.product_base_code = :productBaseCode";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferToListOfOnboardingBaseProductDetail(columnsList);
    }

    @SuppressWarnings("unchecked")
    private List<LegacyProductDetail> queryProductMarketingMaterialMarketSide(String productBaseCode){
        String sql = "select pmmm.product_base_code as id, "
                + "	 	            supplier.k_code as supplier_kcode, "
                + "         CAST(pmmm.data as text) as json_data "
                //+ "                            NULL as status "
                + "from draft_product_marketing_material_marketside pmmm "
                + "inner join company supplier on supplier.id = pmmm.supplier_company_id "
                + "where pmmm.product_base_code = :productBaseCode";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferToListOfOnboardingBaseProductDetailForProductMarketingMaterialMarketSide(columnsList);
    }

    private List<LegacyProductDetail> queryProductMarketingMaterialMarketSideOnboardingApplication(String productBaseCode){
        String sql = "select pmmm.product_base_code as id, "
                + "	 	            supplier.k_code as supplier_kcode, "
                + "					c.code as country_code, "
                + "         CAST(pmmm.data as text) as json_data "
                //+ "                            NULL as status "
                + "from draft_product_marketing_material_marketside pmmm "
                + "inner join company supplier on supplier.id = pmmm.supplier_company_id "
                + "inner join country c on pmmm.country_id = c.id "
                + "where pmmm.product_base_code = :productBaseCode";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        List<LegacyProductDetail> listOfOnboardingBaseProductDetail = new ArrayList<LegacyProductDetail>();
        for(Object[] columns:columnsList){
            String id = (String)columns[0];
            String supplierKcode = (String)columns[1];
            String countryCode = (String)columns[2];
            String jsonData = (String)columns[3];
            String status = null;

            listOfOnboardingBaseProductDetail.add(new LegacyProductDetail(id, id ,
                    supplierKcode,jsonData,status,countryCode));
        }
        return listOfOnboardingBaseProductDetail;
    }

    private List<LegacyProductDetail> transferToListOfOnboardingBaseProductDetailForProductMarketingMaterialMarketSide(List<Object[]> columnsList){
        List<LegacyProductDetail> ListOfOnboardingBaseProductDetail = new ArrayList<LegacyProductDetail>();
        for(Object[] columns:columnsList){
            String id = (String)columns[0];
            String supplierKcode = (String)columns[1];
            String data = (String)columns[2];
            String status = null;
            ListOfOnboardingBaseProductDetail.add(new LegacyProductDetail(id, id,supplierKcode,data,status ,""));
        }
        return ListOfOnboardingBaseProductDetail;
    }

    private ProductDetail transferToProductDetail(List<Object[]> columnsList){

        for(Object[] columns:columnsList){
            String id = (String)columns[0];
            String supplierKcode = (String)columns[1];
            String data = (String)columns[2];
            String status = (String)columns[3];

            return new ProductDetailImpl(id, id,supplierKcode,data,status ,"");
        }
        return null;
    }

    private LegacyProductDetail transferToProductMMDetail(List<Object[]> columnsList){

        for(Object[] columns:columnsList){
            String id = (String)columns[0];
            String supplierKcode = (String)columns[1];
            String data = (String)columns[2];
            String status = (String)columns[3];

            System.out.println("QueryQueryQueryQueryQueryQueryQuery");
            System.out.println(id);
            System.out.println(data);

            return new LegacyProductDetail(id, id,supplierKcode,data,status ,"");
        }
        return null;
    }

    private List<LegacyProductDetail> transferToListOfOnboardingBaseProductDetail(List<Object[]> columnsList){
        List<LegacyProductDetail> ListOfOnboardingBaseProductDetail = new ArrayList<LegacyProductDetail>();
        for(Object[] columns:columnsList){
            String id = (String)columns[0];
            String supplierKcode = (String)columns[1];
            String data = (String)columns[2];
            String status = (String)columns[3];

            ListOfOnboardingBaseProductDetail.add(new LegacyProductDetail(id, id,supplierKcode,data,status ,""));
        }
        return ListOfOnboardingBaseProductDetail;
    }



}
