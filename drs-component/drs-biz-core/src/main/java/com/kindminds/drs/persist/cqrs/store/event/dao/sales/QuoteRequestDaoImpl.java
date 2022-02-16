package com.kindminds.drs.persist.cqrs.store.event.dao.sales;

import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequest;
import com.kindminds.drs.api.data.cqrs.store.event.dao.sales.QuoteRequestDao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;




@Repository
public class QuoteRequestDaoImpl extends Dao implements QuoteRequestDao {




    @Override
    @Transactional("transactionManager")
    public void insert(QuoteRequest quoteRequest) {

        String sql = "INSERT INTO sales.quote_request( " +
                " quote_request_id, create_time, supplier_company_id," +
                " quotation_id, quotation_create_time, status) " +
                " VALUES (" +
                " :quote_request_id, :create_time, :supplier_company_id, " +
                " :quotation_id, :quotation_create_time, :status)";

        System.out.println("insert sql : " + sql);
        //System.out.println("entityManager: "  + entityManager);
      MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("quote_request_id", quoteRequest.getId());
        q.addValue("create_time",  Timestamp.valueOf(quoteRequest.getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("supplier_company_id", quoteRequest.getSupplierCompanyId());

        q.addValue("quotation_id", quoteRequest.getQuotation().get().getId());
        q.addValue("quotation_create_time", Timestamp.valueOf(
                quoteRequest.getQuotation().get().getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("status", quoteRequest.getStatus().getKey());


        getNamedParameterJdbcTemplate().update(sql,q);
    }

    @Override @Transactional("transactionManager")
    public void insert(Iterable<QuoteRequest> quoteRequests) {

        /*
        String sql = "INSERT INTO sales.quote_request( " +
                " quote_request_id, create_time, supplier_company_id," +
                " quotation_id, quotation_create_time, status) " +
                " VALUES (?, ?, ?, ?, ?, ?)";

        Session session = this.entityManager.unwrap(Session.class);
        session.doWork((Connection conn)-> {
            int insertCount = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)){
                for(QuoteRequest quoteRequest: quoteRequests){
                    pstmt.setString(1, quoteRequest.getId());

                    pstmt.setTimestamp(2, Timestamp.valueOf(quoteRequest.getCreateTime()
                            .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

                    pstmt.setInt(3, quoteRequest.getSupplierCompanyId());
                    pstmt.setString(4, quoteRequest.getQuotation().get().getId());

                    pstmt.setTimestamp(5, Timestamp.valueOf(
                            quoteRequest.getQuotation().get().getCreateTime()
                                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

                    pstmt.setInt(6, quoteRequest.getStatus().getKey());

                    pstmt.addBatch();
                    insertCount++;
                    if(insertCount % 50 == 0){
                        pstmt.executeBatch();
                    }
                }
                pstmt.executeBatch();
            }
        });
        */


    }

    @Override
    public void insertQuoteRequestOnboardingApp(String quoteRequestId, String onboardingId) {

    }

    @Override
    public List<Object[]> getQuoteRequestById(String id) {
        return null;
    }

    @Override
    public String queryOnboardingIdByQuoteRequestId(String quoteRequestId) {
        return null;
    }

    @Override
    public String queryCompanyCodeById(Integer companyId) {
        return null;
    }
/*
    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> getQuoteRequestById(String id) {
        String sql = "SELECT qt.quotation_id, qt.create_time as quote_create_time, serial_number, qt.supplier_company_id, \n" +
                "\tfee_type, sale_price, preferential_price, final_price, \n" +
                "\tsummary, accepted, rejected, \n" +
                "\tqr.quote_request_id, qr.create_time, qr.status \n" +
                "FROM sales.quotation qt \n" +
                "INNER JOIN sales.quote_request qr \n" +
                "ON qt.quotation_id = qr.quotation_id AND qt.create_time = qr.quotation_create_time \n" +
                "WHERE qr.quote_request_id = :quote_request_id \n" +
                "ORDER BY qr.create_time DESC LIMIT 1 ";

        Query q = entityManager.createNativeQuery(sql);
        q.addValue("quote_request_id", id);

        return (List<Object[]>) q.getResultList();
    }

    //quote_request_onboarding_application
    @Override @Transactional("transactionManager")
    public void insertQuoteRequestOnboardingApp(String quoteRequestId, String onboardingId) {
        String sql = "INSERT INTO sales.quote_request_onboarding_application \n" +
                " (quote_request_id, onboarding_application_id) \n" +
                " values (:quote_request_id, :onboarding_application_id)";

        Query q = entityManager.createNativeQuery(sql);
        q.addValue("quote_request_id", quoteRequestId);
        q.addValue("onboarding_application_id", onboardingId);

        q.executeUpdate();

    }

    @Override
    public String queryOnboardingIdByQuoteRequestId(String quoteRequestId) {
        String sql = "SELECT onboarding_application_id \n" +
                " FROM sales.quote_request_onboarding_application \n" +
                " WHERE quote_request_id = :quote_request_id ";
        Query q = entityManager.createNativeQuery(sql);
        q.addValue("quote_request_id", quoteRequestId);
        return (String) q.getSingleResult();
    }

    @Override
    public String queryCompanyCodeById(Integer companyId) {
        String sql = "SELECT k_code " +
                " FROM public.company " +
                " WHERE id = :company_id ";
        System.out.println("entityManager: " + entityManager);
        System.out.println("sql: " + sql);
      MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("company_id", companyId);
        return (String) q.getSingleResult();
    }*/

}
