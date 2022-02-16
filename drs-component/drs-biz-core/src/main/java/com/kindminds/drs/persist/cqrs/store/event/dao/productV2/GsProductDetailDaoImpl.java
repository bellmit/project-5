package com.kindminds.drs.persist.cqrs.store.event.dao.productV2;

import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.GsProductDetailDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Repository
public class GsProductDetailDaoImpl  extends Dao implements GsProductDetailDao {

    @Override @Transactional("transactionManager")
    public String save(String ean, String codeByDrs, String marketplaceSku, int marketplaceId, String nameByDrs, boolean containLithiumIonBattery) {

        String sql = "insert into product.gs_product_detail "
                + "( ean, code_by_drs, marketplace_sku, marketplace_id, name_by_drs, contain_lithium_ion_battery ) values "
                + "(:ean,  :codeByDrs, :marketplaceSku, :marketplaceId, :nameByDrs,  :containLithiumIonBattery   )";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("ean", ean);
        q.addValue("codeByDrs", codeByDrs);
        q.addValue("marketplaceSku", marketplaceSku);
        q.addValue("marketplaceId", marketplaceId);
        q.addValue("nameByDrs", nameByDrs);
        q.addValue("containLithiumIonBattery", containLithiumIonBattery);
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        return "";
    }
}
