package com.kindminds.drs.persist.data.access.usecase.product;

import com.kindminds.drs.api.data.access.usecase.product.ConfirmMarketingMaterialDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.product.ConfirmMarketingMaterialComment;
import com.kindminds.drs.persist.v1.model.mapping.product.ConfirmMarketingMaterialCommentImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;




import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class ConfirmMarketingMaterialDaoImpl extends Dao implements ConfirmMarketingMaterialDao {



    @Override @SuppressWarnings("unchecked")
    public List<String> querySupplierEmailsByBaseCode(String productBaseCode) {
        String sql = "SELECT cse.email "
                + " FROM company_service_email cse "
                + " INNER JOIN product_base pb on pb.supplier_company_id = cse.company_id "
                + " WHERE pb.code_by_drs = :product_base_code ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("product_base_code", productBaseCode);
        return  getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
    }

    @Override
    public Boolean marketingMaterialMarketsideExists(int marketplaceId, String productBaseCode) {
        String sql = " select exists(select 1 from marketing_material_marketside "
                + " WHERE marketplace_id = :marketplace_id "
                + " AND product_base_code = :product_base_code ) ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplace_id", marketplaceId);
        q.addValue("product_base_code", productBaseCode);
        return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
    }

    @Override @Transactional("transactionManager")
    public void insertMarketingMaterialMarketside(int marketplaceId, String productBaseCode,
                                                  String productCodeName, String supplierKCode) {
        String sql = " INSERT INTO marketing_material_marketside "
                + " (marketplace_id, product_base_code, product_code_name, company_kcode) "
                + " VALUES (:marketplace_id, :product_base_code, :product_code_name, :company_kcode)";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplace_id", marketplaceId);
        q.addValue("product_base_code", productBaseCode);
        q.addValue("product_code_name", productCodeName);
        q.addValue("company_kcode", supplierKCode);
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
    }

    @Override
    public Integer queryMarketingMaterialMarketsideId(int marketplaceId, String productBaseCode) {
        String sql = " SELECT id from marketing_material_marketside "
                + " WHERE marketplace_id = :marketplace_id "
                + " AND product_base_code = :product_base_code ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplace_id", marketplaceId);
        q.addValue("product_base_code", productBaseCode);
        return getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
    }

    @Override
    public Integer queryDrsStaffId(int marketplaceId, String productBaseCode) {
        String sql = " SELECT drs_staff_id FROM marketing_material_marketside "
                + " WHERE marketplace_id = :marketplace_id "
                + " AND product_base_code = :product_base_code ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplace_id", marketplaceId);
        q.addValue("product_base_code", productBaseCode);
        return getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
    }

    @Override @SuppressWarnings("unchecked")
    public List<Object[]> queryProductBaseListToRenotify() {
        String sql = " SELECT marketplace_id, product_base_code, "
                + " product_code_name, company_kcode "
                + " FROM marketing_material_marketside "
                + " WHERE renotify_supplier = TRUE "
                + " AND date_last_updated < now() - INTERVAL '1 DAY'";
       MapSqlParameterSource q = new MapSqlParameterSource();
        return getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
    }

    @Override @Transactional("transactionManager")
    public void updateDrsStaffId(int drsStaffId, int marketplaceId, String productBaseCode) {
        String sql = " UPDATE marketing_material_marketside "
                + " SET drs_staff_id = :drs_staff_id "
                + " WHERE marketplace_id = :marketplace_id "
                + " AND product_base_code = :product_base_code ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("drs_staff_id", drsStaffId);
        q.addValue("marketplace_id", marketplaceId);
        q.addValue("product_base_code", productBaseCode);
        Assert.isTrue( getNamedParameterJdbcTemplate().update(sql,q)==1);
    }

    @Override @Transactional("transactionManager")
    public void updateMarketingMaterialStatus(String status,
                                              Boolean renotify,
                                              int marketplaceId,
                                              String productBaseCode) {
        String sql = " UPDATE marketing_material_marketside "
                + " SET status = :status, "
                + " date_last_updated = now(), "
                + " renotify_supplier = :renotify_supplier "
                + " WHERE marketplace_id = :marketplace_id "
                + " AND product_base_code = :product_base_code ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("status", status);
        q.addValue("renotify_supplier", renotify);
        q.addValue("marketplace_id", marketplaceId);
        q.addValue("product_base_code", productBaseCode);
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
    }

    @Override @SuppressWarnings("unchecked")
    public List<ConfirmMarketingMaterialComment> queryCommentByProductBaseCode(
            int marketplaceId, String productBaseCode) {
        String sql = "SELECT * FROM marketing_material_marketside_comment "
                + " WHERE marketplace_id = :marketplace_id "
                + " AND product_base_code = :product_base_code "
                + " ORDER BY line_seq DESC ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplace_id", marketplaceId);
        q.addValue("product_base_code", productBaseCode);

        return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ConfirmMarketingMaterialCommentImpl(
                rs.getInt("id"),rs.getInt("marketing_material_marketside_id"),rs.getInt("marketplace_id"),
                rs.getString("product_base_code"),rs.getInt("line_seq"),
                rs.getString("creator_id") , rs.getString("creator_name") , rs.getString("date_created"),
                rs.getString("contents")
        ));
    }

    @Override @Transactional("transactionManager")
    public String insertComment(int materialMarketsideId, int marketplaceId,
                                String productBaseCode, int creatorId,
                                String contents) {
        String sql = " INSERT INTO marketing_material_marketside_comment ( "
                + " marketing_material_marketside_id, marketplace_id, "
                + " product_base_code, line_seq, creator_id, creator_name, date_created, contents) "
                + " SELECT :marketing_material_marketside_id, :marketplace_id, "
                + " :product_base_code, :lineSeq, :creator_id, user_display_name, :date_created, :contents "
                + " FROM user_info ui WHERE ui.user_id = :creator_id ";
        int maxLineSeq = this.queryMaxLineSeqOfComment(marketplaceId, productBaseCode);
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketing_material_marketside_id", materialMarketsideId);
        q.addValue("marketplace_id", marketplaceId);
        q.addValue("product_base_code", productBaseCode);
        q.addValue("lineSeq", maxLineSeq+1);
        q.addValue("creator_id", creatorId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        String dateCreated = sdf.format(new Date());
        q.addValue("date_created", dateCreated);
        q.addValue("contents", contents);
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q) ==1);
        return productBaseCode;
    }

    private Integer queryMaxLineSeqOfComment(int marketplaceId, String productBaseCode) {
        String sql = "SELECT max(line_seq) FROM marketing_material_marketside_comment mmmc "
                + "WHERE mmmc.marketplace_id = :marketplace_id "
                + "AND mmmc.product_base_code = :product_base_code ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplace_id", marketplaceId);
        q.addValue("product_base_code", productBaseCode);
        Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
        if (o == null) return 0;
        return o;
    }

}