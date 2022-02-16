package com.kindminds.drs.persist.data.access.rdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.Country;


import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductViewQueries;
import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductDetailImpl;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductDtoImpl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;




import java.util.*;


@Repository
public class ProductViewQueriesImpl extends Dao implements ProductViewQueries {




    @Override
    public ProductDto getActivateBaseProductOnboarding(String productBaseCode) {

        String sql = "select pis.product_base_code as id, "
                + "  supplier.k_code as supplier_kcode , pis.product_id  "
                + "from product.product_view pis "
                + "inner join company supplier on supplier.id = pis.supplier_company_id "
                + " where pis.product_base_code = :productBaseCode and pis.market_side = 0 " +
                " and pis.activate='1' ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferToOnboardingBaseProduct(columnsList,productBaseCode , "1");

    }

    @Override
    public ProductDto getInActivateBaseProductOnboarding(String productBaseCode) {

        String sql = "select pis.product_base_code as id, "
                + "  supplier.k_code as supplier_kcode , pis.product_id  "
                + "from product.product_view pis "
                + "inner join company supplier on supplier.id = pis.supplier_company_id "
                + " where pis.product_base_code = :productBaseCode and pis.market_side = 0 " +
                " and pis.activate='0' ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferToOnboardingBaseProduct(columnsList,productBaseCode ,"0");

    }

    private ProductDto transferToOnboardingBaseProduct(List<Object[]> columnsList,
                                                       String productBaseCode  ,
                                                       String isActivated){

        String  productId = "";
        String id ="";
        String supplierKcode ="";
        for(Object[] columns:columnsList){
           id  = (String)columns[0];
           supplierKcode = (String)columns[1];
           productId = (String)columns[2];
        }

        return new ProductDtoImpl(id,supplierKcode,"",
                this.queryProductInfoSource(productBaseCode , isActivated) ,
                this.queryProductMarketingMaterialSource(productId) ,
                isActivated.equals("1") ?
                        this.getActivateProductInfoMarketSide(productBaseCode) :
                        this.getInactivateProductInfoMarketSide(productBaseCode) ,
                this.queryProductMarketingMaterialMarketSide(productId));

    }

    private ProductDetailImpl queryProductInfoSource(String productBaseCode ,
                                                                   String isActivated   ){

        String sql = "select pis.product_base_code as id, "
                + "	 	   supplier.k_code as supplier_kcode, "
                + " CAST(pis.data as text) as json_data, "
                + "             pis.editing_status as status "
                + "from product.product_view pis "
                + "inner join company supplier on supplier.id = pis.supplier_company_id "
                + "where pis.product_base_code = :productBaseCode  " +
                " and pis.market_side = 0  and pis.activate= :isActivated" ;

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        q.addValue("isActivated", isActivated);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferToOnboardingBaseProductDetail(columnsList);
    }


    private ProductDetailImpl queryProductMarketingMaterialSource(String productId){

        String sql = "select product_base_code as id, "
                + "	 	            supplier_kcode as supplier_kcode, "
                + "         CAST(data as text) as json_data, "
                + "                     status as status "
                + "from product.marketing_material_view  "
                + "where product_id = :pid and product_market_side = 0";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("pid", productId);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);


        return this.transferToOnboardingBaseProductDetail(columnsList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductDetail> getActivateProductInfoMarketSide(String productBaseCode){
        String sql = "select pim.product_base_code as id, "
                + "	 	           supplier.k_code as supplier_kcode, "
                + "         CAST(pim.data as text) as json_data, pim.editing_status as status "
                + "from product.product_view pim "
                + "inner join company supplier on supplier.id = pim.supplier_company_id "
                + "where pim.product_base_code = :productBaseCode and pim.market_side > 0 " +
                " and pim.activate= '1' ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferMarketSideToListOfOnboardingBaseProductDetail(columnsList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductDetail> getInactivateProductInfoMarketSide(String productBaseCode){
        String sql = "select pim.product_base_code as id, "
                + "	 	           supplier.k_code as supplier_kcode, "
                + "         CAST(pim.data as text) as json_data, pim.editing_status as status "
                + "from product.product_view pim "
                + "inner join company supplier on supplier.id = pim.supplier_company_id "
                + "where pim.product_base_code = :productBaseCode and pim.market_side > 0 " +
                " and pim.activate= '0' ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferMarketSideToListOfOnboardingBaseProductDetail(columnsList);
    }

    @Override @SuppressWarnings("unchecked")
    public ProductDetail getProductInfoMarketSide(String supplierKcode, String productBaseCode, String country) {

        String sql = "select pim.product_base_code as id, "
                + "	 	               splr.k_code as supplier_kcode, "
                + "         CAST(pim.data as text) as json_data, "
                + "                     pim.editing_status as status "
                + "from product.product_view pim "
                + "inner join company splr on splr.id = pim.supplier_company_id "
                + "where pim.product_base_code = :productBaseCode "
                + "and pim.market_side = :countryId and pim.activate='1' "
                + "and splr.k_code = :supplierKcode ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode",productBaseCode);
        q.addValue("countryId", Country.valueOf(country).getKey());
        q.addValue("supplierKcode",supplierKcode);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferToOnboardingBaseProductDetail(columnsList);
    }

    @SuppressWarnings("unchecked")
    private List<ProductDetail> queryProductMarketingMaterialMarketSide(
            String productId ){


        String sql = "select product_base_code as id, "
                + "	 	            supplier_kcode as supplier_kcode, "
                + "         CAST(data as text) as json_data, "
                + "                     status as status "
                + "from product.marketing_material_view  "
                + "where product_id = :pid and product_market_side  > 0 ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("pid", productId);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        return this.transferToListOfOnboardingBaseProductDetailForProductMarketingMaterialMarketSide(columnsList);
    }

    //=========================================================================

    private ProductDetailImpl transferToOnboardingBaseProductDetail(List<Object[]> columnsList){

        for(Object[] columns:columnsList){
            String id = (String)columns[0];
            String supplierKcode = (String)columns[1];
            String data = (String)columns[2];
            String status = ProductEditingStatusType.fromKey(Integer.parseInt(columns[3].toString())).getText();
             return  new ProductDetailImpl(id, id ,supplierKcode,data,status ,"");
        }
        return null;
    }

    private ProductDetailImpl transferToOnboardingBaseProductDetailForProductMarketingMaterialMarketSide(List<Object[]> columnsList){

        for(Object[] columns:columnsList){
            String id = (String)columns[0];
            String supplierKcode = (String)columns[1];
            String data = (String)columns[2];
            String status = null;
            return new ProductDetailImpl(id, id ,supplierKcode,data,status,"");
        }
        return null;
    }

    private List<ProductDetail> transferMarketSideToListOfOnboardingBaseProductDetail(List<Object[]> columnsList){


        List<ProductDetail> ListOfOnboardingBaseProductDetail = new ArrayList<ProductDetail>();
        ObjectMapper objectMapper = new ObjectMapper();

        for(Object[] columns:columnsList){
            String id = (String)columns[0];
            String supplierKcode = (String)columns[1];

            String data = (String)columns[2];


            String status = ProductEditingStatusType.fromKey(Integer.parseInt(columns[3].toString())).getText();
            ListOfOnboardingBaseProductDetail.add(new ProductDetailImpl(id,id,supplierKcode,data,status,""));
        }
        return ListOfOnboardingBaseProductDetail;
    }

    private List<ProductDetail> transferToListOfOnboardingBaseProductDetail(List<Object[]> columnsList){
        List<ProductDetail> ListOfOnboardingBaseProductDetail = new ArrayList<ProductDetail>();
        for(Object[] columns:columnsList){
            String id = (String)columns[0];
            String supplierKcode = (String)columns[1];
            String data = (String)columns[2];

            String status = ProductEditingStatusType.fromKey(Integer.parseInt(columns[3].toString())).getText();
            ListOfOnboardingBaseProductDetail.add(new ProductDetailImpl(id,id ,supplierKcode,data,status,""));
        }
        return ListOfOnboardingBaseProductDetail;
    }

    private List<ProductDetail> transferToListOfOnboardingBaseProductDetailForProductMarketingMaterialMarketSide(List<Object[]> columnsList){
        List<ProductDetail> ListOfOnboardingBaseProductDetail = new ArrayList<ProductDetail>();
        for(Object[] columns:columnsList){
            String id = (String)columns[0];
            String supplierKcode = (String)columns[1];
            String data = (String)columns[2];
            String status = null;
            ListOfOnboardingBaseProductDetail.add(new ProductDetailImpl(id,id ,supplierKcode,data,status,""));
        }
        return ListOfOnboardingBaseProductDetail;
    }

    //=========================================================================

    @Override
    public List<ProductDto> getBaseProductOnboardingList(int startIndex, int size) {

        String sql = "select pis.product_base_code as id, "
                + "             NULL as supplier_kcode, "
                + "         pis.data as json_data "
                + "from product.product_view pis where pis.market_side = 0  and pis.activate='1' "
                + "order by pis.product_base_code asc "
                + "limit :size offset :start ";

        System.out.println(startIndex);
        System.out.println(size);

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("size", size);
        q.addValue("start", startIndex-1);

        return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs,rowNum) -> new ProductDtoImpl(
                rs.getString("id"),rs.getString("supplier_kcode") ,rs.getString("json_data"),
                null,null,null,null
        ));
    }

    @Override
    public String getSupplierKcodeOfBaseProductOnboarding(String productBaseCode) {
        String sql = "select com.k_code from company com "
                + "inner join product.product_view pis on pis.supplier_company_id = com.id "
                + "where pis.product_base_code = :productBaseCode and market_side= 0 and pis.activate='1' ";

      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);
        List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        Assert.isTrue(resultList.size()==1,"Size: "+resultList.size() );

        return resultList.get(0);
    }

    @Override
    public List<ProductDto> getBaseProductOnboardingList(int startIndex, int size,
                                                         String companyKcode) {
        String sql = "select pis.product_base_code as id, "
                + "             NULL as supplier_kcode, "
                + "         pis.data as json_data "
                + "from product.product_view pis "
                + "inner join company com on com.id = pis.supplier_company_id "
                + "where com.k_code = :companyKcode and pis.market_side = 0 and pis.activate='1' "
                + "order by pis.product_base_code asc "
                + "limit :size offset :start ";

//        System.out.println(startIndex);
//        System.out.println(size);
//        System.out.println(companyKcode);

    MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("companyKcode", companyKcode);
        q.addValue("size", size);
        q.addValue("start", startIndex-1);

        return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs,rowNum) -> new ProductDtoImpl(
                rs.getString("id"),rs.getString("supplier_kcode") ,rs.getString("json_data"),
                null,null,null,null
        ));

    }


    @Override
    public int getBaseProductOnboardingTotalSize() {
        String sql = "select count(pis.*) from product.product_view pis " +
                " where pis.market_side = 0 and pis.activate='1' ";


        Integer o = null;
        try{
             o = getJdbcTemplate().queryForObject(sql,Integer.class);
        } catch (EmptyResultDataAccessException e) {
        }

        if(o==null) return 0;
        return o;
    }

    @Override
    public int getBaseProductOnboardingTotalSize(String companyKcode) {
        String sql = "select count(pis.*) from product.product_view pis "
                + "inner join company com on com.id = pis.supplier_company_id "
                + "where com.k_code = :companyKcode and pis.market_side = 0 and pis.activate='1' ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("companyKcode",companyKcode);

        Integer o = null;
        try{
             o = getNamedParameterJdbcTemplate().queryForObject(sql,q ,Integer.class);
        }catch (EmptyResultDataAccessException ex){

        }

        if(o==null) return 0;
        return o;
    }

    @Override @SuppressWarnings("unchecked")
    public Map<String, List<String>> getBaseCodesToMarketplacesMap(String companyCode) {

        String sql = "select pbm.product_base_code, string_agg(pbm.country_code, ',') " +
                " from " +
                "(SELECT pv.product_base_code, cty.code as country_code " +
                " FROM product.onboarding_application_lineitem_view poalv " +
                " INNER JOIN product.product_view pv ON pv.product_id = poalv.product_id and pv.activate='1'" +
                " INNER JOIN company cpy ON cpy.id = pv.supplier_company_id " +
                " INNER JOIN country cty ON cty.id = pv.market_side " +
                " where cpy.k_code = :companyCode " +
                " group by pv.product_base_code, cty.code " +
                ") pbm " +
                " group by pbm.product_base_code " +
                " order by pbm.product_base_code ";
      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("companyCode", companyCode);
        List<Object[]> results = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        Map<String, List<String>> baseCodesToMarketplacesMap = new TreeMap<>();
        for (Object[] column : results) {
            String baseProduct = (String) column[0];
            String marketplaces = (String) column[1];
            List<String> marketplacesList = Arrays.asList(marketplaces.split(","));
            baseCodesToMarketplacesMap.put(baseProduct, marketplacesList);
        }
        return baseCodesToMarketplacesMap;
    }





}
