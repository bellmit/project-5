package com.kindminds.drs.core.query.logistics;


import com.kindminds.drs.api.data.transfer.logistics.IvsProductVerifyInfo;
import com.kindminds.drs.core.query.IvsProductVerifyInfoImpl;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;




@Repository
public class IvsProductVerificationQueries  extends Dao {

    //todo arthur use supplier sku not code by drs
    //maybe wrong
    public IvsProductVerifyInfo queryProductVerifyInfo(String sku, String destination   )    {

        String  sql = " SELECT distinct pv.market_side, pv.product_base_code , cty.code, " +
                " pv.data->>'productNameEnglish' as product_name, " +
                " pv.data->>'productNameLocal' as product_name_local,  " +
                " pv.data->>'brandEng' as brand_eng, " +
                " pv.data->>'originalPlace' as country_of_origin, " +
                " pv.data->>'hsCode' as hs_code, " +
                " json_array_elements(CAST(pv.data->>'products' as json)) ->> 'FCAPrice' as FCA_price," +
                " json_array_elements(CAST(pv.data->>'products' as json)) ->> 'packageWeight' as product_weight," +
                " json_array_elements(CAST(pv.data->>'products' as json)) ->> 'packageWeightUnit' as product_weight_unit ," +
                " json_array_elements(CAST(pv.data->>'products' as json)) ->> 'packageDimension1' as product_dimension_L," +
                " json_array_elements(CAST(pv.data->>'products' as json)) ->> 'packageDimension2' as product_dimension_W," +
                " json_array_elements(CAST(pv.data->>'products' as json)) ->> 'packageDimension3' as product_dimension_H," +
                " json_array_elements(CAST(pv.data->>'products' as json)) ->> 'packageDimensionUnit' as product_dimension_unit" +
                " FROM product.variation_view vv " +
                " inner join product p on p.code_by_supplier  = vv.variation_code " +
                " inner join product_sku ps on ps.product_id = p.id " +
                " inner join product.product_view  pv on vv.product_id  = pv.product_id" +
                " LEFT JOIN country cty ON cty.id = pv.market_side" +
                "  where ps.code_by_drs  = :skuCode " +
                " ORDER BY pv.market_side asc";


        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("skuCode", sku);
        List resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

       // Assert.isTrue(resultList.size > 0, "resultList size is not > 0   " + resultList.size)


        if(resultList.size()>0){

            Object [] coreProduct = (Object [] )resultList.get(0);

            String  bp = (String) coreProduct[1];

            String productName = (String) coreProduct[3] ;
            String productNameLocal = (coreProduct[4]  == null) ? (String)coreProduct[4]  : "";
            String brandEng = (coreProduct[5] == null) ? (String)coreProduct[5] : "";
            String countryOfOrigin = (coreProduct[6]   == null) ?  (String)coreProduct[6] : "";
            String hsCode = (coreProduct[7]   == null) ? (String) coreProduct[7] : "";

            String fcaPrice =(coreProduct[8]   == null) ? (String)coreProduct[8]   : "";

            String weightUnit = (coreProduct[10]   == null) ? (String) coreProduct[10]   : "";
            String dimensionUnit = (coreProduct[14]   == null) ? (String)  coreProduct[14]   : "";

            String marketHsCode     = "";
            String length    = "";
            String width    = "";
            String height    = "";
            String netWeight    ="";

            for (Object columns : resultList    ) {
                Object [] colAry = (Object [] ) columns ;

                if (colAry[2]   == null ) {
                    if( destination == (String) colAry[2] ){
                        marketHsCode = (colAry[7]  == null) ? (String) colAry[7]   : "";
                        netWeight = (colAry[9]   == null) ?(String)  colAry[9]   : "";
                        length = (colAry[11]   == null) ? (String)  colAry[11]   : "";
                        width = (colAry[12]   == null) ? (String)  colAry[12]   : "";
                        height = (colAry[13]   == null) ? (String)  colAry[13]   : "";

                        break;
                    }
                }
            }

            return new IvsProductVerifyInfoImpl(sku, productName, bp,
                    productName, productNameLocal, brandEng,
                    countryOfOrigin, marketHsCode    , hsCode,
                     (StringUtils.isEmpty(fcaPrice)) ? new BigDecimal(0) :
                             new BigDecimal(fcaPrice.trim()),
                     (StringUtils.isEmpty(netWeight)) ? new BigDecimal(0) :
                       new BigDecimal(netWeight.trim())
                                .divide(new BigDecimal(1000)).setScale(2, RoundingMode.HALF_UP),
                    weightUnit.trim(),
                     (StringUtils.isEmpty(length)) ? new BigDecimal(0) : new BigDecimal(length.trim()),
                     (StringUtils.isEmpty(width)) ? new BigDecimal(0) : new BigDecimal(width.trim()),
                     (StringUtils.isEmpty(height)) ? new BigDecimal(0) : new BigDecimal(height.trim()),
                    dimensionUnit);
        }

        return new IvsProductVerifyInfoImpl();



    }


    /*
    *  SELECT product_view.product_base_code,
            concat("substring"(product_view.product_base_code    text, 4, 4), '-', json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'SKU'    text) AS sku,
            json_array_elements((json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'marketplace'    text)    json) ->> 'SSBP'    text AS sales_price,
            product_view.data ->> 'country'    text AS country,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'status'    text AS status,
            product_view.data ->> 'note'    text AS note,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'packageDimension1'    text AS package_dimension_l,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'packageDimension2'    text AS package_dimension_w,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'packageDimension3'    text AS package_dimension_h,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'packageDimensionUnit'    text AS package_dimension_unit,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'packageWeight'    text AS package_weight,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'packageWeightUnit'    text AS package_weight_unit,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'variationProductQuantity'    text AS quantity_per_carton,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'cartonPackageDimension1'    text AS carton_dimension_l,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'cartonPackageDimension2'    text AS carton_dimension_w,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'cartonPackageDimension3'    text AS carton_dimension_h,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'cartonPackageDimensionUnit'    text AS carton_dimension_unit,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'cartonPackageWeight'    text AS carton_weight,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'cartonPackageWeightUnit'    text AS carton_weight_unit,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'DDPprice'    text AS ddp_price,
            product_view.data ->> 'dutyRate'    text AS import_duty_rate,
            product_view.data ->> 'hsCode'    text AS hs_code,
            json_array_elements((product_view.data ->> 'products'    text)    json) ->> 'disposeOfUnfulfillableInventory'    text AS unfulfillable_inventory_removal_settings
           FROM product.product_view
          WHERE product_view.market_side = 2
    *
    *
    * */

    public  String queryLineitemProdVerificationStatus(String ivsName  ,int boxNum , int mixLineSeq )    {


        StringBuilder sqlSb = new StringBuilder()
                .append("select  shpli.line_status ")
                .append("from shipment_line_item shpli ")
                .append("inner join shipment shp on shp.id = shpli.shipment_id ")
                .append("where shp.name =  :shipmentName and  shpli.box_num=  :boxNum " +
                        "and mixed_box_line_seq =  :mLineSeq ")
                .append("order by shpli.line_seq ");

        MapSqlParameterSource q = new MapSqlParameterSource();
        q .addValue("shipmentName", ivsName);
        q .addValue("boxNum", boxNum);
        q .addValue("mLineSeq", mixLineSeq);

        List rList =  getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(), q,String.class);
        if(!rList.isEmpty()){
            return rList.get(0) == null ? "New" : (String) rList.get(0);
        }else{
            return "";
        }

    }


}