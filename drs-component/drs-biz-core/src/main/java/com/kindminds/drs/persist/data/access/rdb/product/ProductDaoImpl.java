package com.kindminds.drs.persist.data.access.rdb.product;

import com.kindminds.drs.api.data.access.rdb.product.ProductDao;

import com.kindminds.drs.api.data.transfer.productV2.ProductPackageDimWeightDto;
import com.kindminds.drs.api.data.transfer.productV2.SimpleBaseProductDto;
import com.kindminds.drs.api.data.transfer.productV2.SimpleSkuDto;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.product.SKU;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import java.util.*;




@Repository
public class ProductDaoImpl extends Dao implements ProductDao {


   
   public Map<String, String> queryActiveSkuCodeToDrsNameMap(int companyId )  {

        String sql = ("select ps.code_by_drs, p.name_by_drs "
                + "from product_sku ps "
                + "inner join product_base pb on pb.id = ps.product_base_id "
                + "inner join product_sku_status pss on pss.id = ps.status_id "
                + "inner join product p on p.id = ps.product_id "
                + "where pb.supplier_company_id = :companyId "
                + "and pss.name = :activeStatusName ");

      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("companyId", companyId);
        q.addValue("activeStatusName", SKU.Status.SKU_ACTIVE);
        Map activeSkuCodeToNameMap = new HashMap<String, String>();
        List<Object []> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        for ( Object[] ary : result) {

            activeSkuCodeToNameMap.put(ary[0].toString() , ary[1].toString());
        }


        return activeSkuCodeToNameMap;
    }


    public List<SimpleBaseProductDto> queryProductBaseCode(String supplierKcode )  {


        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select pb.code_by_drs, p.name_by_supplier ");
        sqlSb.append("from product_base pb ");
        sqlSb.append("inner join product p on p.id = pb.product_id ");
        sqlSb.append("inner join company splr on splr.id = pb.supplier_company_id ");
        sqlSb.append("where splr.k_code = :supplierKcode ");
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("supplierKcode", supplierKcode);

        List<Object []> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
        List pList = new ArrayList<SimpleBaseProductDto>();
        for ( Object[] r : result) {
            pList.add( new SimpleBaseProductDto(r[0].toString() ,r[1].toString()));

        }
        return pList;
    }


    public List<SimpleSkuDto> queryProductSkuCode(String productBaseCode )  {

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select ps.code_by_drs, p.name_by_supplier ");
        sqlSb.append("from product_sku ps ");
        sqlSb.append("inner join product p on p.id = ps.product_id ");
        sqlSb.append("inner join product_base pb on pb.id = ps.product_base_id ");
        sqlSb.append("where pb.code_by_drs = :productBaseCode ");

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productBaseCode", productBaseCode);

        List pList = new ArrayList<SimpleSkuDto>();
        List<Object []> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);

        for ( Object[] r : result) {
            pList.add( new SimpleSkuDto(r[0].toString() ,r[1].toString()));

        }
        return pList;
    }


    public String queryBaseProductBySku(String sku )  {
        String sql = "SELECT pb.code_by_drs, ps.code_by_drs " +
                " FROM product_sku ps " +
                " INNER join product_base pb on pb.id = ps.product_base_id " +
                " WHERE ps.code_by_drs = :sku";
      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("sku", sku);
        return getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
    }

    public Map<String, String> queryAllSkuToBaseProductMap(String companyCode )   {
        String  sql = "SELECT ps.code_by_drs as sku, pb.code_by_drs as base_product" +
                " FROM product_sku ps " +
                " INNER JOIN product_base pb on pb.id = ps.product_base_id " +
                " INNER JOIN company cpy on cpy.id = pb.supplier_company_id " +
                " WHERE cpy.k_code = :companyCode " +
                " order by pb.code_by_drs ";
      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("companyCode", companyCode);
        Map allSkuToBaseProductMap = new HashMap<String, String>();
        List<Object []> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        for ( Object[] ary : result) {
            allSkuToBaseProductMap.put(ary[0].toString() , ary[1].toString());
        }
        
        return allSkuToBaseProductMap;
    }
    
    public List<ProductPackageDimWeightDto> queryPackageDimWeight(String baseProduct)  {
        String  sql = "select * from (SELECT  " +
                "json_array_elements(CAST(data->>'products' as json))->>'packageDimension1' as package_dimension_L, " +
                "json_array_elements(CAST(data->>'products' as json))->>'packageDimension2' as package_dimension_W, " +
                "json_array_elements(CAST(data->>'products' as json))->>'packageDimension3' as package_dimension_H, " +
                "json_array_elements(CAST(data->>'products' as json))->>'packageDimensionUnit' as package_dimension_unit, " +
                "json_array_elements(CAST(data->>'products' as json))->>'packageWeight' as package_weight, " +
                "json_array_elements(CAST(data->>'products' as json))->>'packageWeightUnit' as package_weight_unit " +
                "  FROM product.product_view pv " +
                "  INNER JOIN public.country ctry on ctry.id = pv.market_side " +
                "  where product_base_code = :baseProduct " +
                "  order by update_time desc) dim " +
                "where dim.package_dimension_L != ''";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("baseProduct", baseProduct);

        List pList = new ArrayList<ProductPackageDimWeightDto>();
        List<Object []> result = getNamedParameterJdbcTemplate().query(sql.toString(),q,objArrayMapper);
        for (  Object [] r : result) {
            pList.add( new ProductPackageDimWeightDto(r[0].toString(), r[1].toString(), r[2].toString(),
                    r[3].toString(),r[4].toString(),r[5].toString()));

        }
        return pList;

    }

}