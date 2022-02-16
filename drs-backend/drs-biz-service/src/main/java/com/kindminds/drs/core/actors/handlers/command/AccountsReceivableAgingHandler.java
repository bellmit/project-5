package com.kindminds.drs.core.actors.handlers.command;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.accounting.AccountsReceivableAgingDao;
import com.kindminds.drs.api.message.command.CalculateOriginalAccountsReceivable;
import com.kindminds.drs.api.message.command.GenerateAccountsReceivableAgingReport;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.biz.service.util.BizCoreCtx;
import com.kindminds.drs.core.RegisterCommandHandler;


import com.kindminds.drs.service.util.MailUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class AccountsReceivableAgingHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props(ActorRef drsCmdBus ) {

        return Props.create(AccountsReceivableAgingHandler.class ,
                () -> new AccountsReceivableAgingHandler(drsCmdBus) );
    }

    private final ActorRef drsCmdBus ;
    private final AnnotationConfigApplicationContext springCtx = BizCoreCtx.get();
    private final String name = self().path().name();

    private static final String SOFTWARE_ENGINEERS = "arthur.wu@drs.network";
    private static final String ADDRESS_NO_REPLY = "drs-noreply@tw.drs.network";
    private static final String MAIL_SUBJECT = "AccountsReceivableAgingHandler";

    private AccountsReceivableAgingDao arAgingDao = springCtx.getBean(AccountsReceivableAgingDao.class);

    private CompanyDao companyDao = springCtx.getBean(CompanyDao.class);

    private MailUtil mailUtil = springCtx.getBean(MailUtil.class);

    public AccountsReceivableAgingHandler(ActorRef drsCmdBus) {
        this.drsCmdBus = drsCmdBus;


        drsCmdBus.tell(new RegisterCommandHandler(name , CalculateOriginalAccountsReceivable.class.getName()
                , self()) , ActorRef.noSender());

        drsCmdBus.tell(new RegisterCommandHandler(name , GenerateAccountsReceivableAgingReport.class.getName()
                , self()) , ActorRef.noSender());


    }

    private void mailMessage(String subject, String message) {
        message = subject + ":\n" + message;
        mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, MAIL_SUBJECT, message);
    }

    @Override
    public Receive createReceive() {
        StringWriter errors = new StringWriter();

        return receiveBuilder()
                .match(CalculateOriginalAccountsReceivable.class, cal -> {
                    try {
                        //System.out.//println("CalculateOriginalAccountsReceivable");

                        calculateOriginalAccountsReceivable();

                        mailMessage("CalculateOriginalAccountsReceivable", "Completed");

                    } catch (Exception e) {

                        e.printStackTrace(new PrintWriter(errors));
                        mailMessage("CalculateOriginalAccountsReceivable", errors.toString());
                    }
                })
                .match(GenerateAccountsReceivableAgingReport.class, gen -> {
                    try {
                        generateAccountsReceivableAgingReport();

                        mailMessage("GenerateAccountsReceivableAgingReport", "Completed");
                    } catch (Exception e) {

                        e.printStackTrace(new PrintWriter(errors));
                        mailMessage("GenerateAccountsReceivableAgingReport", errors.toString());
                    }
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }


    private void calculateOriginalAccountsReceivable() {
        Instant reportDate = Instant.now();

        //uncomment this reportDate, change date, and use TestAccountsReceivableAgingScalaHandler
        // to manually update statement_accounts_receivable
//        reportDate = LocalDateTime
//                .parse("2021-03-10 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                .atZone(ZoneId.of("UTC"))
//                .toInstant();

        Instant latestEnd = arAgingDao.queryLastSettlementPeriodEnd(reportDate);
        //System.out.//println("latestEnd: " + latestEnd);

        if (arAgingDao.accountReceivableExists(latestEnd)) {
            arAgingDao.deleteExistingAccountReceivable(latestEnd);
        }
        calculateOrigAccRecForAllSuppliers(latestEnd);
    }

    private void calculateOrigAccRecForAllSuppliers(Instant latestEnd) {
        List<BillStatement> statementList = arAgingDao.queryStatementListBySupplier(latestEnd);
        BigDecimal originalAR;
        Instant invoiceDate = latestEnd.plus(10, ChronoUnit.DAYS);
        //System.out.//println("invoiceDate: " + invoiceDate);
        //System.out.//println(statementList.size());

        for (BillStatement statement : statementList) {
            originalAR = calculateOrigAccRecForSupplier(statement);
            //System.out.//println("originalAR: " + originalAR);
            //System.out.//println(invoiceDate);
            //System.out.//println(statement.getDisplayName());
            arAgingDao.insertOriginalAccountsReceivable(statement.getId(), invoiceDate, originalAR);
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

    private void generateAccountsReceivableAgingReport() {
        Instant reportDate = Instant.now();

        //uncomment this reportDate, change date, and use TestAccountsReceivableAgingScalaHandler
        // to manually update supplier_accounts_receivable
//        reportDate = LocalDateTime.parse("2021-03-10 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                .atZone(ZoneId.of("UTC")).toInstant();

        Instant latestEnd = arAgingDao.queryLastSettlementPeriodEnd(reportDate);
        //System.out.//println("latestEnd: " + latestEnd);
        if (reportDate.minus(7, ChronoUnit.DAYS).compareTo(latestEnd) < 0) {
            return;
        }

        List<BillStatement> statements = arAgingDao.queryStatementListBySupplier(latestEnd);

        reportDate = latestEnd.plus(10, ChronoUnit.DAYS);

        Map<String, BigDecimal> supplierToRemittanceMap = arAgingDao.querySupplierToRemittanceMap(latestEnd, reportDate);
        BigDecimal usdTwExchangeRate = arAgingDao.queryUSDtoTWDexchangeRate(reportDate);


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
//                    //System.out.//println("writeOff B+C+E: " + writeOff);
                } else if (remittanceReceived.compareTo(BigDecimal.ZERO) == 0) {

                    writeOff = total.subtract(remittanceSent).max(BigDecimal.ZERO).add(remittanceSum);
//                    //System.out.//println("writeOff max(B-D, 0) + E: " + writeOff);
                }
            } else {
                if (remittanceReceived.compareTo(BigDecimal.ZERO) >= 0) {

                    writeOff = remittanceReceived.add(remittanceSum);
//                    //System.out.//println("writeOff C+E: " + writeOff);
                }
            }
        }

        Integer sarId = arAgingDao.queryMaxSupplierAccountsReceivableId() + 1;
        arAgingDao.insertSupplierAccountsReceivable(sarId, statement.getReceiverKcode(),
                reportDate, writeOff, usdTwExchangeRate);

        return writeOff;
    }

    private void calculateAccountsReceivableBalanceHistory(BillStatement statement,
                                                           Instant reportDate,
                                                           BigDecimal writeOff,
                                                           Map<String, BigDecimal> supplierToRemittanceMap) {

        List<Object[]> results = arAgingDao.queryHistoricalStatementDetail(
                statement.getReceiverKcode(), reportDate);

        Integer companyId = companyDao.queryIdFromKcode(statement.getReceiverKcode());
        Integer sarId = arAgingDao.querySupplierAccountsReceivableId(companyId).intValue();
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

            Integer sarDetailId = arAgingDao.queryMaxSupplierAccountsReceivableDetailId() + 1;
            arAgingDao.insertSupplierAccountsReceivableDetail(sarDetailId, sarId,
                    (Integer) result[3], newArBalance);
        }


        //11.Get AR balance for most recent period
        BigDecimal arBalance = getAccountReceivableBalance(statement, oldWriteOff, supplierToRemittanceMap);

        //12. Insert table supplier_accounts_receivable_detail with
        //Supplier_accounts_receivable_id of current report_date, recent period statement_id, AR balance
        Integer sardId = arAgingDao.queryMaxSupplierAccountsReceivableDetailId() + 1;

        arAgingDao.insertSupplierAccountsReceivableDetail(sardId, sarId,
                statement.getId(), arBalance);

        //13. Calculate supplier_accounts_receivable_balance = sum
        // (statement_accounts_receivable_balance) with the same supplier_accounts_receivable_id
        BigDecimal supplierAccountsReceivableBalance = arAgingDao.querySumSupplierAccountsReceivableBalance(sarId);

        //14. Update “supplier_accounts_receivable_balance” in table “supplier_accounts_receivable”
        //  with the same supplier_accounts_receivable_id.
        arAgingDao.updateSupplierAccountsReceivableBalance(sarId, supplierAccountsReceivableBalance);

    }

    private BigDecimal getAccountReceivableBalance(BillStatement statement, BigDecimal oldWriteOff,
                                                   Map<String, BigDecimal> supplierToRemittanceMap) {
        //Get statement_original_accounts_receivable and balance of the
        // designated supplier from table “bill_statement” and "statement_accounts_receivable”
        // of most recent period.
        BigDecimal arBalance = BigDecimal.ZERO;

        BigDecimal originalAccountsReceivable = arAgingDao.queryOriginalAccountsReceivable(statement.getId());

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