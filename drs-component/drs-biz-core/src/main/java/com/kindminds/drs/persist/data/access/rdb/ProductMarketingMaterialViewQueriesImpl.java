package com.kindminds.drs.persist.data.access.rdb;

import com.kindminds.drs.Country;

import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductDetailImpl;

import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductMarketingMaterialViewQueries;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;




import java.util.List;


@Repository
public class ProductMarketingMaterialViewQueriesImpl extends  Dao  implements ProductMarketingMaterialViewQueries {



    @Override
    public ProductDetail findProductMarketingMaterial(
            String productBaseCode , String productVariationCode , Country country) {

        String sql = "select product_base_code as id, "
                + "	 	                supplier_kcode as supplier_kcode, "
                + "         CAST(data as text) as json_data, "
                + "                     status as status , marketing_material_id "
                + "from product.marketing_material_view  "
                + "where product_base_code =:bpcode and variation_code = :code "
                + "and product_market_side = :market_side ";

      MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("bpcode",productBaseCode);
        q.addValue("code",productVariationCode);
        q.addValue("market_side",country.getKey());
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return this.transferToOnboardingBaseProductDetail(columnsList);
    }

    private ProductDetail transferToOnboardingBaseProductDetail(List<Object[]> columnsList){

        for(Object[] columns:columnsList){
            String id = (String)columns[0];
            String supplierKcode = (String)columns[1];
            String data = (String)columns[2];
            String status = ProductEditingStatusType.fromKey(Integer.parseInt(columns[3].toString())).getText();
            String mid = (String)columns[4];

            return  new ProductDetailImpl(mid, id,supplierKcode,data,status ,"");

        }
        return null;
    }


}
