package com.kindminds.drs.persist.data.access.usecase.reviewtracker;


import com.kindminds.drs.api.data.access.usecase.reviewtracker.ImportAmazonReviewReportDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonReviewNotification;
import com.kindminds.drs.api.v1.model.amazon.AmazonReviewReportItem;
import com.kindminds.drs.persist.data.access.rdb.Dao;


import com.kindminds.drs.persist.v1.model.mapping.amazon.AmazonReviewNotificationImpl;
import com.kindminds.drs.util.RTDateUtil;


import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;

@Repository
public class ImportAmazonReviewReportDaoImpl extends Dao implements ImportAmazonReviewReportDao {



    @Override @Transactional("transactionManager")
    public List<AmazonReviewReportItem> insertAmazonReviews(List<AmazonReviewReportItem> amazonReviews) {

        String sqlInsert = "INSERT INTO amazon_product_review " +
                "(id, date_created, star_rating, product_id, marketplace_id, \"comment\", title, customer_name, helpful) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?); ";


        List<AmazonReviewReportItem> newReviews = new ArrayList<>();
        List<AmazonReviewReportItem> reviewsToUpdate = new ArrayList<>();

        int[][] updateCounts = this.getJdbcTemplate().batchUpdate(sqlInsert, amazonReviews, 50 ,
                new ParameterizedPreparedStatementSetter<AmazonReviewReportItem>() {
                    @Override
                    public void setValues(PreparedStatement pstmt, AmazonReviewReportItem record) throws SQLException {
                        //System.out.println("Date");
                        //System.out.println(record.getDateCreated());

                        if(!checkRecordExists(record.getId(), record.getMarketplaceId())) {

                            newReviews.add(record);
                            pstmt.setString(1, record.getId());
                            System.out.println("DateCreated: " + record.getDateCreated());
                            System.out.println("MarketplaceId: " + record.getMarketplaceId());

                            Date dateCreated = RTDateUtil.conformDateReviews(record.getMarketplaceId(), record.getDateCreated());
                            record.setDateCreated(new SimpleDateFormat("EEEE d MMMMM yyyy", Locale.US).format(dateCreated));

                            pstmt.setDate(2, new java.sql.Date(dateCreated.getTime()));

                            pstmt.setDouble(3, record.getStarRating());
                            pstmt.setInt(4, record.getProductId());
                            pstmt.setInt(5, record.getMarketplaceId());
                            pstmt.setString(6, record.getComment());
                            pstmt.setString(7, record.getTitle());
                            pstmt.setString(8, record.getCustomerName());
                            pstmt.setInt(9, record.getHelpful());



                        }else{
                            reviewsToUpdate.add(record);
                        }

                    }

                });


        updateAmazonReview(reviewsToUpdate);
        return newReviews;

    }

    @Transactional("transactionManager")
    private Integer updateAmazonReview(List<AmazonReviewReportItem> amazonReviews) {
        String sqlUpdate = "UPDATE amazon_product_review " +
                "SET date_created=?, star_rating=?, product_id=?, \"comment\"=?, title=?, customer_name=?, " +
                " helpful=? , datetime_updated=? " +
                "WHERE id=? AND marketplace_id=?; ";


        int[][] updateCounts = this.getJdbcTemplate().batchUpdate(sqlUpdate, amazonReviews, 50 ,
                new ParameterizedPreparedStatementSetter<AmazonReviewReportItem>() {
                    @Override
                    public void setValues(PreparedStatement pstmt, AmazonReviewReportItem record) throws SQLException {
                        //System.out.println("Date");
                        //System.out.println(record.getDateCreated());
                        Date dateCreated =  RTDateUtil.conformDateReviews(record.getMarketplaceId(), record.getDateCreated());
                        pstmt.setDate(1, new java.sql.Date(dateCreated.getTime()));

                        pstmt.setDouble(2, record.getStarRating());
                        pstmt.setInt(3, record.getProductId());
                        pstmt.setString(4, record.getComment());
                        pstmt.setString(5, record.getTitle());
                        pstmt.setString(6, record.getCustomerName());
                        pstmt.setInt(7, record.getHelpful());
                        pstmt.setTimestamp(8,Timestamp.valueOf(OffsetDateTime.now()
                                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));
                        //WHERE
                        pstmt.setString(9, record.getId());
                        pstmt.setInt(10, record.getMarketplaceId());

                    }

                });

        return amazonReviews.size();
    }

    private Boolean checkRecordExists(String id, int marketplaceId) {
        String sql = "select case when exists ( " +
                " select apr.id " +
                " from amazon_product_review apr " +
                " where apr.id = :id and apr.marketplace_id = :marketplace_id " +
                " ) " +
                " then cast (1 as bit) " +
                " else cast (0 as bit) " +
                "end;";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id", id);
        q.addValue("marketplace_id", marketplaceId);
        return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
    }

    @Override @Transactional("transactionManager")
    public Boolean deleteAmazonReview(String id, int marketplaceId) {
        String sql = "DELETE FROM amazon_product_review " +
                "WHERE id=:id AND marketplace_id=:marketplace_id";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id", id);
        q.addValue("marketplace_id", marketplaceId);
        return (getNamedParameterJdbcTemplate().update(sql,q) > 0);
    }

    @Override
    public AmazonReviewNotification queryAmazonReviewNotification(int productId) {
        String sql = "select distinct p.id, c.k_code as supplier_kcode, c.short_name_en_us as supplier_name, p.name_by_drs as product_name, pmia.asin " +
                "from pv.product_all_marketplace_info_amazon pmia  " +
                "inner join pv.product_all_marketplace_info pmi on pmia.product_marketplace_info_id = pmi.id  " +
                "inner join marketplace m on pmi.marketplace_id = m.id  " +
                "inner join product_sku ps on pmi.product_id = ps.product_id  " +
                "inner join product p on ps.product_id = p.id " +
                "inner join product_base pb on ps.product_base_id = pb.id  " +
                "inner join company c on pb.supplier_company_id = c.id " +
                "where p.id = :product_id";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("product_id", productId);


        return getNamedParameterJdbcTemplate().queryForObject(sql,q,(rs,rowNum) -> new AmazonReviewNotificationImpl(
                rs.getInt("id"),rs.getString("supplier_kcode"),rs.getString("supplier_name"),
                rs.getString("product_name"),rs.getString("asin")
        ));
    }
}