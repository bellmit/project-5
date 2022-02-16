package com.kindminds.drs.persist.data.access.rdb.accounting;

import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.accounting.AccountsReceivableAgingDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.persist.v1.model.mapping.close.BillStatementImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Repository
public class AccountsReceivableAgingDaoImpl extends Dao implements AccountsReceivableAgingDao {

    //@PersistenceContext(unitName = "default")  private EntityManager entityManager;

    // view "accounts_receivable_aging_detail"

    @Autowired
    CompanyDao companyDao;



    @Override @SuppressWarnings("unchecked")
    public  List<Object[]> queryAccountReceivableAgingList() {
        String sql = "SELECT report_date, k_code, supplier_name, statement_name,  " +
                "\tcurrency_code, period_start, period_end, accounts_receivable_on_report_date,  " +
                "\toriginal_accounts_receivable, invoice_date, aging, aging_range,  " +
                "\taccount_manager_in_charge, special_payment_term,  " +
                "\taccounts_receivable_on_report_date_usd, splr_company_id  " +
                "\t FROM finance.temp_accounts_receivable_aging_detail ";

       // Query q = entityManager.createNativeQuery(sql);
        List<Object []> resultList = getJdbcTemplate().query(sql ,objArrayMapper);


        /*
        List<AccountsReceivableAgingDetail> agingList = new ArrayList<>();
        for (Object[] result : resultList) {
            agingList.add(new AccountsReceivableAgingDetailImpl(result));
        }
        return agingList;
        */
        return resultList;
    }


    @Override
    public Instant queryLastSettlementPeriodEnd(Instant reportDate) {


        String sql = "SELECT period_end from public.bill_statement "
                + " WHERE period_end < :reportDate "
                + " ORDER BY period_end desc LIMIT 1";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("reportDate", Timestamp.from(reportDate));


        Timestamp result =  getNamedParameterJdbcTemplate().queryForObject(
                sql, namedParameters, Timestamp.class);

        return  result.toInstant();

    }

    public Boolean accountReceivableExists(Instant periodEnd) {

        String sql = "SELECT exists (select 1 from finance.statement_accounts_receivable " +
                " WHERE invoice_date > :periodEnd )";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("periodEnd", Timestamp.from(periodEnd));

        return  getNamedParameterJdbcTemplate().queryForObject(sql, namedParameters , Boolean.class);
    }

    @Override @Transactional("transactionManager")
    public void deleteExistingAccountReceivable(Instant periodEnd) {

        String sql = "DELETE FROM finance.statement_accounts_receivable " +
                " WHERE invoice_date > :periodEnd";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("periodEnd", Timestamp.from(periodEnd));

       int deleteCount = getNamedParameterJdbcTemplate().update(sql ,q );

        System.out.println("deleteExistingAccountReceivable delete count: " + deleteCount);
    }

    @Override @SuppressWarnings("unchecked")
    public List<BillStatement> queryStatementListBySupplier(Instant periodEnd) {
        String sql = "select        bs.id as id, "
                + "               bs.name as display_name, "
                + "          period_start as date_start, "
                + "            period_end as date_end, "
                + "                 total as total, "
                + "      previous_balance as previous_balance, "
                + "               balance as balance, "
                + "           rcvr.k_code as rcvr_kcode, "
                + "           isur.k_code as isur_kcode, "
                + "       remittance_sent as rmtnce_sent, "
                + "   remittance_received as rmtnce_received, "
                + "                c.name as currency_name "
                + "from bill_statement bs "
                + "inner join company rcvr on rcvr.id=bs.receiving_company_id "
                + "inner join company isur on isur.id=bs.issuing_company_id "
                + "left  join currency c   on    c.id=bs.currency_id "
                + " Where bs.period_end = :periodEnd "
                + " AND isur.k_code = 'K2'"
                + " ORDER BY rcvr.k_code ";

        SqlParameterSource q = new MapSqlParameterSource()
                .addValue("periodEnd", Timestamp.from(periodEnd));

        List<BillStatement> resultList =  getNamedParameterJdbcTemplate().query(sql , q , (rs, rowNum) ->
                new BillStatementImpl(
                        rs.getInt("id"), rs.getString("display_name"), rs.getTimestamp("date_start"),
                        rs.getTimestamp("date_end") , rs.getString("isur_kcode"), rs.getString("rcvr_kcode"),
                        rs.getBigDecimal("rmtnce_sent"), rs.getBigDecimal("rmtnce_received"),
                        rs.getString("currency_name"), rs.getBigDecimal("total"), rs.getBigDecimal("previous_balance"),
                        rs.getBigDecimal("balance")
                ));

        Assert.isTrue(resultList.size()!=0,"resultList.size() must not be empty");
        return resultList;
    }

    @Override @Transactional
    public void insertOriginalAccountsReceivable(Integer statementId, Instant invoiceDate, BigDecimal originalAR){

        String sql = "INSERT INTO finance.statement_accounts_receivable(  " +
                "\tstatement_id, invoice_date, original_accounts_receivable)  " +
                "\tVALUES (:statementId, :invoiceDate, :originalAR); ";

        MapSqlParameterSource q = new MapSqlParameterSource();
//        System.out.println("insert invoiceDate: " + invoiceDate);
//        System.out.println("insert timestamp: " + Timestamp.from(invoiceDate));
        q.addValue("statementId", statementId);
        q.addValue("invoiceDate", Timestamp.from(invoiceDate));
        q.addValue("originalAR", originalAR);

        int insertCount = getNamedParameterJdbcTemplate().update(sql ,q );

        Assert.isTrue(insertCount == 1, "insert count must be 1");
    }


    @Override @SuppressWarnings("unchecked")
    public Map<String, BigDecimal> querySupplierToRemittanceMap(Instant latestEnd, Instant reportDate) {

        String sql = "SELECT  cpy.k_code, sum(amount) as amount  " +
                "\tFROM remittance rm  " +
                "\tINNER JOIN company cpy ON cpy.id = rm.sender_company_id  " +
                "\tWHERE date_sent > :latestEnd  " +
                "\t AND date_sent < :reportDate " +
                "\tgroup by cpy.k_code ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("latestEnd", Timestamp.from(latestEnd));
        q.addValue("reportDate", Timestamp.from(reportDate));

        List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql , q , objArrayMapper);
        Map<String, BigDecimal> supplierToRemittanceMap = new HashMap<>();

        for (Object[] result : resultList) {
            supplierToRemittanceMap.put((String) result[0], (BigDecimal) result[1]);
        }

        return supplierToRemittanceMap;
    }


    @Override
    public BigDecimal queryUSDtoTWDexchangeRate(Instant reportDate) {
        String sql = "SELECT rate " +
                "\tFROM currency_exchange  " +
                "\tWHERE src_currency_id = 2  " +
                "\tand dst_currency_id = 1  " +
                "\tand date = (SELECT MAX(date) FROM currency_exchange  " +
                "\t\tWHERE src_currency_id = 2  " +
                "\t\tand dst_currency_id = 1  " +
                "\t\tand date < :reportDate) ";


        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("reportDate", Timestamp.from(reportDate));

        return  getNamedParameterJdbcTemplate().queryForObject(sql,q , BigDecimal.class);
    }

    @Override
    public Integer queryMaxSupplierAccountsReceivableId() {
        String sql = "SELECT MAX(id) from finance.supplier_accounts_receivable ";

        return getJdbcTemplate().queryForObject(sql , Integer.class);
    }

    @Override @Transactional
    public void insertSupplierAccountsReceivable(Integer sarId, String supplierCode, Instant reportDate,
                                                 BigDecimal writeOff, BigDecimal exchangeRate) {
        String sql = "INSERT INTO finance.supplier_accounts_receivable(  " +
                "\t id, splr_company_id, report_date, " +
                " report_date_write_off, usd_to_twd_exchange_rate) " +
                "\t SELECT :sarId, cpy.id, :reportDate, :writeOff, :exchangeRate " +
                "\t FROM company cpy WHERE k_code = :supplierCode ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("sarId", sarId);
        q.addValue("supplierCode", supplierCode);
        q.addValue("reportDate", Timestamp.from(reportDate));
        q.addValue("writeOff", writeOff);
        q.addValue("exchangeRate", exchangeRate);

        getNamedParameterJdbcTemplate().update(sql,q);
    }



    @Override @SuppressWarnings("unchecked")
    public List<Object[]> queryHistoricalStatementDetail(String supplierCode, Instant reportDate) {
        String sql = "SELECT sard.id, supplier_accounts_receivable_id, sar.splr_company_id, " +
                " statement_id,  statement_accounts_receivable_balance  " +
                "\tFROM finance.supplier_accounts_receivable_detail sard  " +
                "\tINNER JOIN finance.supplier_accounts_receivable sar ON sar.id = sard.supplier_accounts_receivable_id  " +
                "\tINNER JOIN company cpy ON cpy.id = sar.splr_company_id  " +
                "\twhere cpy.k_code = :supplierCode  " +
                "\tand report_date = (select max(report_date) from  finance.supplier_accounts_receivable " +
                "      where report_date < :reportDate ) " +
                "\tand sard.statement_accounts_receivable_balance < 0 " +
                "\t ORDER BY statement_id ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("supplierCode", supplierCode);
        q.addValue("reportDate", Timestamp.from(reportDate));
        return getNamedParameterJdbcTemplate().query(sql,q ,objArrayMapper);
    }

    @Override
    public Integer queryMaxSupplierAccountsReceivableDetailId() {
        String sql = "SELECT MAX(id) from finance.supplier_accounts_receivable_detail";

        return getJdbcTemplate().queryForObject(sql,Integer.class);
    }

    @Override @Transactional
    public void insertSupplierAccountsReceivableDetail(Integer sarDetailId,
                                                       Integer sarId,
                                                       Integer statementId,
                                                       BigDecimal arBalance) {
        String sql = "INSERT INTO finance.supplier_accounts_receivable_detail( " +
                "\t id, supplier_accounts_receivable_id, statement_id, statement_accounts_receivable_balance) " +
                "\t VALUES ( :sarDetailId, :sarId, :statementId, :arBalance) ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("sarDetailId", sarDetailId);
        q.addValue("sarId", sarId);
        q.addValue("statementId", statementId);
        q.addValue("arBalance", arBalance);

        getNamedParameterJdbcTemplate().update(sql,q);
    }



    @Override @SuppressWarnings("unchecked")
    public BigDecimal queryOriginalAccountsReceivable(Integer statementId) {
        String sql = "SELECT original_accounts_receivable FROM finance.statement_accounts_receivable " +
                "WHERE statement_id = :statementId  " +
                "ORDER BY invoice_date DESC LIMIT 1";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("statementId", statementId);
        return getNamedParameterJdbcTemplate().queryForObject(sql,q,BigDecimal.class);
    }

    @Override
    public Integer querySupplierAccountsReceivableId(Integer companyId) {
        String sql = "SELECT MAX(id) FROM finance.supplier_accounts_receivable " +
                "WHERE splr_company_id = :companyId ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("companyId", companyId);
        return getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
    }

    @Override
    public BigDecimal querySumSupplierAccountsReceivableBalance(Integer sarId) {
        String sql = "SELECT sum(statement_accounts_receivable_balance) " +
                " FROM finance.supplier_accounts_receivable_detail " +
                " WHERE supplier_accounts_receivable_id = :sarId ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("sarId", sarId);
        return getNamedParameterJdbcTemplate().queryForObject(sql,q,BigDecimal.class);
    }

    @Override @Transactional
    public void updateSupplierAccountsReceivableBalance(Integer sarId, BigDecimal balance) {
        String sql = "UPDATE finance.supplier_accounts_receivable " +
                " SET supplier_accounts_receivable_balance = :balance " +
                " WHERE id = :sarId";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("balance", balance);
        q.addValue("sarId", sarId);
        int updated = getNamedParameterJdbcTemplate().update(sql,q);
        Assert.isTrue(updated == 1, "updated count must be 1, instead is: " + updated);
    }




    public void calculateOriginalAccountsReceivable() {
        Instant reportDate = Instant.now();

//        reportDate = LocalDateTime.parse("2020-12-16 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                .atZone(ZoneId.of("UTC")).toInstant();

        Instant latestEnd = queryLastSettlementPeriodEnd(reportDate);
        System.out.println("latestEnd: " + latestEnd);

        if (accountReceivableExists(latestEnd)) {
            deleteExistingAccountReceivable(latestEnd);
        }
        calculateOrigAccRecForAllSuppliers(latestEnd);
    }

    private void calculateOrigAccRecForAllSuppliers(Instant latestEnd) {
        List<BillStatement> statementList = queryStatementListBySupplier(latestEnd);
        BigDecimal originalAR;
        Instant invoiceDate = latestEnd.plus(10, ChronoUnit.DAYS);
        System.out.println("invoiceDate: " + invoiceDate);

        for (BillStatement statement : statementList) {
            originalAR = calculateOrigAccRecForSupplier(statement);
//            System.out.println("originalAR: " + originalAR);

            insertOriginalAccountsReceivable(statement.getId(), invoiceDate, originalAR);
        }
    }

    private BigDecimal calculateOrigAccRecForSupplier(BillStatement statement) {
        if (statement.getTotal().compareTo(BigDecimal.ZERO) < 0
                && statement.getBalance().compareTo(BigDecimal.ZERO) < 0) {

            if (statement.getTotal().compareTo(statement.getBalance()) >= 0) {
                return statement.getTotal();
            }
            return statement.getBalance();
        }
        return BigDecimal.ZERO;
    }


    //Generate accounts receivable aging report

    public void generateAccountsReceivableAgingReport() {
        Instant reportDate = Instant.now();

//        reportDate = LocalDateTime.parse("2020-12-16 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                .atZone(ZoneId.of("UTC")).toInstant();

        Instant latestEnd = queryLastSettlementPeriodEnd(reportDate);
        if (reportDate.minus(7, ChronoUnit.DAYS).compareTo(latestEnd) < 0) {
            return;
        }

        List<BillStatement> statements = queryStatementListBySupplier(latestEnd);

        reportDate = latestEnd.plus(10, ChronoUnit.DAYS);

        Map<String, BigDecimal> supplierToRemittanceMap = querySupplierToRemittanceMap(latestEnd, reportDate);
        BigDecimal usdTwExchangeRate = queryUSDtoTWDexchangeRate(reportDate);


        for (BillStatement statement : statements) {
            if (statement.getPreviousBalance().compareTo(BigDecimal.ZERO) < 0 ||
                    statement.getBalance().compareTo(BigDecimal.ZERO) < 0) {

                BigDecimal writeOff = calculateWriteOffAmount(
                        reportDate, usdTwExchangeRate, statement, supplierToRemittanceMap);

                calculateAccountsReceivableBalanceHistory(statement, reportDate,
                        writeOff, supplierToRemittanceMap);
            }
        }
    }

    private BigDecimal calculateWriteOffAmount(Instant reportDate, BigDecimal usdTwExchangeRate,
                                               BillStatement statement,
                                               Map<String, BigDecimal> supplierToRemittanceMap) {

        BigDecimal writeOff = BigDecimal.ZERO;
        BigDecimal previous = statement.getPreviousBalance();
        BigDecimal total = statement.getTotal();
        BigDecimal remittanceReceived = statement.getRemittanceRcvrToIsur();
        BigDecimal remittanceSent = statement.getRemittanceIsurToRcvr();
        BigDecimal remittanceSum = BigDecimal.ZERO;

        if (supplierToRemittanceMap.containsKey(statement.getReceiverKcode())) {
            remittanceSum = supplierToRemittanceMap.get(statement.getReceiverKcode());
        }

        if (previous.compareTo(BigDecimal.ZERO) < 0) {

            if (total.compareTo(BigDecimal.ZERO) >= 0) {

                if (remittanceReceived.compareTo(BigDecimal.ZERO) > 0) {

                    writeOff = total.add(remittanceReceived).add(remittanceSum);
//                    System.out.println("writeOff B+C+E: " + writeOff);
                } else if (remittanceReceived.compareTo(BigDecimal.ZERO) == 0) {

                    writeOff = total.subtract(remittanceSent).max(BigDecimal.ZERO).add(remittanceSum);
//                    System.out.println("writeOff max(B-D, 0) + E: " + writeOff);
                }
            } else {
                if (remittanceReceived.compareTo(BigDecimal.ZERO) >= 0) {

                    writeOff = remittanceReceived.add(remittanceSum);
//                    System.out.println("writeOff C+E: " + writeOff);
                }
            }
        }

        Integer sarId = queryMaxSupplierAccountsReceivableId() + 1;
        insertSupplierAccountsReceivable(sarId, statement.getReceiverKcode(),
                reportDate, writeOff, usdTwExchangeRate);

        return writeOff;
    }

    private void calculateAccountsReceivableBalanceHistory(BillStatement statement,
                                                           Instant reportDate,
                                                           BigDecimal writeOff,
                                                           Map<String, BigDecimal> supplierToRemittanceMap) {

        List<Object[]> results = queryHistoricalStatementDetail(
                statement.getReceiverKcode(), reportDate);

        Integer companyId = companyDao.queryIdFromKcode(statement.getReceiverKcode());
        Integer sarId = querySupplierAccountsReceivableId(companyId).intValue();
        BigDecimal oldWriteOff = writeOff;
        BigDecimal newWriteOff, newArBalance, oldArBalance;

        for (Object[] result : results) {

            oldArBalance = (BigDecimal) result[4];
            if (oldWriteOff.compareTo(BigDecimal.ZERO) <= 0) {
                newArBalance = oldArBalance;
            } else {
                newWriteOff = oldArBalance.add(oldWriteOff);
                if (newWriteOff.compareTo(BigDecimal.ZERO) < 0) {
                    newArBalance = newWriteOff;
                } else {
                    newArBalance = BigDecimal.ZERO;
                }
                oldWriteOff = newWriteOff;

            }

            Integer sarDetailId = queryMaxSupplierAccountsReceivableDetailId() + 1;
            insertSupplierAccountsReceivableDetail(sarDetailId, sarId,
                    (Integer) result[3], newArBalance);
        }


        //11.Get AR balance for most recent period
        BigDecimal arBalance = getAccountReceivableBalance(statement, oldWriteOff, supplierToRemittanceMap);

        //12. Insert table supplier_accounts_receivable_detail with
        //Supplier_accounts_receivable_id of current report_date, recent period statement_id, AR balance
        Integer sardId = queryMaxSupplierAccountsReceivableDetailId() + 1;

        insertSupplierAccountsReceivableDetail(sardId, sarId,
                statement.getId(), arBalance);

        //13. Calculate supplier_accounts_receivable_balance = sum
        // (statement_accounts_receivable_balance) with the same supplier_accounts_receivable_id
        BigDecimal supplierAccountsReceivableBalance = querySumSupplierAccountsReceivableBalance(sarId);

        //14. Update “supplier_accounts_receivable_balance” in table “supplier_accounts_receivable”
        //  with the same supplier_accounts_receivable_id.
        updateSupplierAccountsReceivableBalance(sarId, supplierAccountsReceivableBalance);

    }

    private BigDecimal getAccountReceivableBalance(BillStatement statement, BigDecimal oldWriteOff,
                                                   Map<String, BigDecimal> supplierToRemittanceMap) {
        //Get statement_original_accounts_receivable and balance of the
        // designated supplier from table “bill_statement” and "statement_accounts_receivable”
        // of most recent period.
        BigDecimal arBalance = BigDecimal.ZERO;

        BigDecimal originalAccountsReceivable = queryOriginalAccountsReceivable(statement.getId());

        BigDecimal balance = statement.getBalance();

        BigDecimal remittanceReceived = BigDecimal.ZERO;

        if (supplierToRemittanceMap.containsKey(statement.getReceiverKcode())) {
            remittanceReceived = supplierToRemittanceMap.get(statement.getReceiverKcode());
        }

        if (originalAccountsReceivable.compareTo(BigDecimal.ZERO) != 0) {
            if (oldWriteOff.compareTo(BigDecimal.ZERO) <= 0) {
                arBalance = originalAccountsReceivable;
            } else if (originalAccountsReceivable.compareTo(BigDecimal.ZERO) < 0) {
                arBalance = balance.add(remittanceReceived);
            }
        }

        return arBalance;
    }

}
