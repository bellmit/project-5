package com.kindminds.drs.persist.cqrs.store.read.dao.sales;



import com.kindminds.drs.api.data.cqrs.store.read.dao.sales.QuoteRequestViewDao;
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequest;
import org.springframework.stereotype.Repository;


import java.util.List;



@Repository
public class QuoteRequestViewDaoImpl implements QuoteRequestViewDao {
    @Override
    public void insertView(QuoteRequest QuoteRequest) {

    }

    @Override
    public void deleteView(String quoteRequestId) {

    }

    @Override
    public List<Object[]> getViewById(String quoteRequestId) {
        return null;
    }



   /* @Override
    @Transactional("transactionManager")
    public void insertView(QuoteRequest quoteRequest) {
        String sql = "INSERT INTO sales.quote_request_view(\n" +
                "\t quote_request_id, supplier_company_id, quotation_id, quotation_serial_number, \n" +
                "\tquotation_sale_price, quotation_preferential_price, quotation_final_price, \n" +
                "\tquotation_summary, status, onboarding_application_id, onboarding_serial_number, create_time, update_time)\n" +
                "\t SELECT \n" +
                "\t :quote_request_id, :supplier_company_id, :quotation_id, :quotation_serial_number, \n" +
                "\t:quotation_sale_price, :quotation_preferential_price, :quotation_final_price, \n" +
                "\t:quotation_summary, :status, oa.onboarding_application_id, oa.serial_number, :create_time, :update_time \n" +
                "\t FROM product.onboarding_application oa" +
                "\t INNER JOIN sales.quote_request_onboarding_application qroa " +
                "\t ON qroa.onboarding_application_id = oa.onboarding_application_id " +
                "\t WHERE qroa.quote_request_id = :quote_request_id " +
                "\t GROUP BY oa.onboarding_application_id, oa.serial_number ";

        Query q = entityManager.createNativeQuery(sql);

        q.setParameter("quote_request_id", quoteRequest.getId());
        q.setParameter("supplier_company_id", quoteRequest.getSupplierCompanyId());

        Quotation quotation = quoteRequest.getQuotation().get();
        q.setParameter("quotation_id", quotation.getId());
        q.setParameter("quotation_serial_number", quotation.getSerialNumber());
        q.setParameter("quotation_sale_price", nullCheck(quotation.getSalePrice()));
        q.setParameter("quotation_preferential_price", nullCheck(quotation.getPreferentialPrice()));
        q.setParameter("quotation_final_price", nullCheck(quotation.getFinalPrice()));
        q.setParameter("quotation_summary", quotation.getSummary());
        q.setParameter("status", quoteRequest.getStatus().getKey());

        q.setParameter("create_time", Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));
        q.setParameter("update_time", Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        assertEquals(1, q.executeUpdate());

    }

    private BigDecimal nullCheck(BigDecimal num) {
        return num == null ? BigDecimal.ZERO : num;
    }

    @Override
    @Transactional("transactionManager")
    public void deleteView(String quoteRequestId) {
        String sql = "DELETE FROM sales.quote_request_view " +
                " WHERE quote_request_id = :quote_request_id ";

        Query q = entityManager.createNativeQuery(sql);

        q.setParameter("quote_request_id", quoteRequestId);

        q.executeUpdate();
    }

    @Override @SuppressWarnings("unchecked")
    public List<Object[]> getViewById(String quoteRequestId) {
        String sql = "SELECT quote_request_id, cpy.k_code, supplier_company_id, quotation_id, \n" +
                "\tquotation_serial_number, quotation_sale_price, \n" +
                "\tquotation_preferential_price, quotation_final_price, \n" +
                "\tquotation_summary, status, onboarding_application_id, \n" +
                "\tonboarding_serial_number, create_time, update_time \n" +
                "\t FROM sales.quote_request_view qrv \n" +
                "\t INNER JOIN company cpy ON cpy.id = qrv.supplier_company_id \n" +
                "\t WHERE quote_request_id =  :quoteRequestId ";

        Query q = entityManager.createNativeQuery(sql);
        q.setParameter("quoteRequestId", quoteRequestId);

        return (List<Object[]>) q.getResultList();
    }*/

}
