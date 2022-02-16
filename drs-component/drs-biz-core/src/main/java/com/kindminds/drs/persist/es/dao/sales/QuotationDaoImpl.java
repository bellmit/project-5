package com.kindminds.drs.persist.es.dao.sales;

import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v2.biz.domain.model.sales.Quotation;
import com.kindminds.drs.api.data.es.dao.sales.QuotationDao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;




import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;




@Repository
public class QuotationDaoImpl extends Dao implements QuotationDao {


    @Override
    public Integer queryNextSerialNumber(Integer companyId) {
        return null;
    }

    @Override
    @Transactional("transactionManager")
    public void insert(Quotation quotation) {


        String sql = " INSERT INTO sales.quotation( " +
                " quotation_id, create_time, serial_number, supplier_company_id, \n" +
                " fee_type, sale_price, preferential_price, final_price, \n" +
                " summary, accepted, rejected) \n"+
                " VALUES (" +
                " :quotation_id, :create_time, :serial_number, :supplier_company_id, \n" +
                " :fee_type, :sale_price, :preferential_price, :final_price, \n" +
                " :summary, :accepted, :rejected )";

        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("quotation_id", quotation.getId());
        q.addValue("create_time",  Timestamp.valueOf(quotation.getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("serial_number", quotation.getSerialNumber());
        q.addValue("supplier_company_id", quotation.getSupplierCompanyId());
        q.addValue("fee_type", quotation.getFeeType().getKey());
        q.addValue("sale_price", nullCheck(quotation.getSalePrice()));
        q.addValue("preferential_price", nullCheck(quotation.getPreferentialPrice()));
        q.addValue("final_price", nullCheck(quotation.getFinalPrice()));


        q.addValue("summary", quotation.getSummary());
        q.addValue("accepted", quotation.isAccepted());
        q.addValue("rejected",  quotation.isRejected());

        getNamedParameterJdbcTemplate().update(sql,q);

    }

    @Override
    public void insert(Iterable<Quotation> quotations) {

    }

    @Override
    public List<Object[]> queryQuotationById(String quotationId) {
        return null;
    }

    @Override
    public String querySummaryByCompanyId(Integer companyId) {
        return null;
    }

    @Override
    public BigDecimal queryMonthlyServiceFee(String regionCount, Integer bpCount) {
        return null;
    }

    private BigDecimal nullCheck(BigDecimal num) {
        return num == null ? BigDecimal.ZERO : num;
    }

  /*  @Override
    @Transactional("transactionManager")
    public void insert(Iterable<Quotation> quotations) {


        String sql = "INSERT INTO sales.quotation( \n" +
                "\tquotation_id, create_time, serial_number, supplier_company_id, \n" +
                "\tfee_type, sale_price, preferential_price, final_price, \n" +
                "\tsummary, accepted, rejected)\n" +
                "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Session session = this.entityManager.unwrap(Session.class);
        session.doWork((Connection conn)-> {
            int insertCount = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)){
                for(Quotation quotation: quotations){

                    pstmt.setString(1, quotation.getId());
                    pstmt.setTimestamp(2,  Timestamp.valueOf(quotation.getCreateTime()
                            .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

                    pstmt.setString(3, quotation.getSerialNumber());
                    pstmt.setInt(4, quotation.getSupplierCompanyId());
                    pstmt.setInt(5, quotation.getFeeType().getKey());
                    pstmt.setBigDecimal(6, quotation.getSalePrice());
                    pstmt.setBigDecimal(7, quotation.getPreferentialPrice());
                    pstmt.setBigDecimal(8, quotation.getFinalPrice());

                    pstmt.setString(9, quotation.getSummary());
                    pstmt.setInt(10, BooleanUtils.toInteger(quotation.isAccepted()));
                    pstmt.setInt(11,  BooleanUtils.toInteger(quotation.isRejected()));

                    pstmt.addBatch();
                    insertCount++;
                    if(insertCount % 50 == 0){
                        pstmt.executeBatch();
                    }
                }
                pstmt.executeBatch();
            }
        });
    }

    @Override @SuppressWarnings("unchecked")
    public List<Object[]> queryQuotationById(String quotationId) {
        String sql = "SELECT quotation_id, create_time, serial_number, \n" +
                " supplier_company_id, fee_type, sale_price, preferential_price, \n" +
                " final_price, summary, accepted, rejected \n" +
                "\tFROM sales.quotation \n" +
                "\tWHERE quotation_id = :quotation_id \n" +
                "\tORDER BY create_time DESC LIMIT 1";

        Query q = entityManager.createNativeQuery(sql);
        q.addValue("quotation_id", quotationId);

        return q.getResultList();
    }

    @Override @SuppressWarnings("unchecked")
    public String querySummaryByCompanyId(Integer companyId) {
        String sql = "SELECT count(distinct product_base_code) as bp_count, " +
                " count(distinct market_side) as country_count \n" +
                " FROM product.product_view \n" +
                " where supplier_company_id  =  :companyId ";

        Query q = entityManager.createNativeQuery(sql);
        q.addValue("companyId", companyId);
        List<Object[]> results = (List<Object[]>) q.getResultList();

        Integer bpCount = 0, mpCount = 0;
        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            bpCount = ((BigInteger) result[0]).intValue();
            mpCount = ((BigInteger) result[1]).intValue() - 1;
        }

        return bpCount + " BPs x " + mpCount + " selling regions";
    }


    @Override
    public BigDecimal queryMonthlyServiceFee(String regionCount, Integer bpCount) {
        String sql = "SELECT MAX(\"" + regionCount.toLowerCase() + "\")" +
                " FROM sales.monthly_service_fee \n" +
                " WHERE id < :bpCount ";
        Query q = entityManager.createNativeQuery(sql);
        q.addValue("bpCount", bpCount+4);
        return (BigDecimal) q.getSingleResult();
    }

    @Override @SuppressWarnings("unchecked")
    public Integer queryNextSerialNumber(Integer companyId) {
        String sql = "SELECT serial_number " +
                " FROM sales.quotation " +
                " WHERE supplier_company_id = :companyId " +
                " ORDER BY create_time DESC ";
        Query q = entityManager.createNativeQuery(sql);
        q.addValue("companyId", companyId);

        List<String> resultList = (List<String>) q.getResultList();
        if (resultList.isEmpty() || resultList.get(0) == null) {
            return 1;
        }
        String result = resultList.get(0);
        System.out.println("RESULT  : " + result);
        return Integer.valueOf(result.split("-")[3].trim()) + 1;
    }*/

}
