package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.data.access.rdb.common.SettlementPeriodListDao;
import com.kindminds.drs.api.usecase.MaintainRemittanceUco;
import com.kindminds.drs.api.v1.model.accounting.Remittance;
import com.kindminds.drs.api.v1.model.accounting.RemittanceSearchCondition;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.v1.model.report.RemittanceImportItem;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.accounting.MaintainRemittanceDao;
import com.kindminds.drs.v1.model.impl.report.RemittanceImportItemImpl;
import com.kindminds.drs.util.DateHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("maintainRemittanceUco")
public class MaintainRemittanceUcoImpl implements MaintainRemittanceUco {

    @Autowired private MaintainRemittanceDao dao;
    @Autowired private CompanyDao companyRepo;
    @Autowired private SettlementPeriodListDao settlementDao;

    @Override
    public List<String> getKcodeList() {
        return this.companyRepo.queryAllCompanyKcodeList();
    }

    @Override
    public Map<String,String> getKcodeToNameMap() {
        return this.companyRepo.queryAllCompanyKcodeToShortEnUsNameMap();
    }

    @Override
    public DtoList<Remittance> retrieveList(RemittanceSearchCondition condition,int pageIndex) {
        DtoList<Remittance> list = new DtoList<Remittance>();
        list.setTotalRecords(this.dao.getCount(condition));
        list.setPager(new Pager(pageIndex,list.getTotalRecords(),50));
        list.setItems(this.dao.queryList(condition,list.getPager().getStartRowNum(),list.getPager().getPageSize()));
        return list;
    }

    @Override
    public Remittance retrieve(int id) {
        return dao.query(id);
    }

    @Override
    public int create(Remittance remittance) {
        Date dateSend = this.generateDateObjectFromStr(remittance.getUtcDateSent());
        Date dateRcvd = this.generateDateObjectFromStr(remittance.getUtcDateReceived());
        Assert.isTrue(dateSend != null, "Remittance date sent is null.");
        Assert.isTrue(dateRcvd != null, "Remittance date received is null.");

        validateDates(dateSend, dateRcvd);
        return this.dao.insert(remittance,dateRcvd,dateSend);
    }

    private void validateDates(Date sent, Date received) {

        Instant lastSettlementEnd = settlementDao.queryLastSettlementEnd().toInstant();
        Instant dateSent = sent.toInstant();
        Instant dateReceived = received.toInstant();

        Assert.isTrue(!dateSent.isBefore(lastSettlementEnd), "Remittance date sent is before last settlement end.");
        Assert.isTrue(!dateReceived.isBefore(lastSettlementEnd), "Remittance date received is before last settlement end.");
    }

    private Date generateDateObjectFromStr(String origDateStr){
        if(!StringUtils.hasText(origDateStr)) return null;
        return DateHelper.toDate(origDateStr+" UTC","yyyy-MM-dd z");
    }

    @Override
    public int update(Remittance remittance) {
        if(this.isModifiable(remittance.getRemittanceId())){
            Date dateRcvd = this.generateDateObjectFromStr(remittance.getUtcDateReceived());
            Date dateSent = this.generateDateObjectFromStr(remittance.getUtcDateSent());
            return this.dao.update(remittance,dateRcvd,dateSent);
        }
        return 0;
    }

    @Override
    public boolean delete(int id) {
        if(this.isModifiable(id)){
            return dao.delete(id);
        }
        return false;
    }

    private boolean isModifiable(int remittanceId) {
        Remittance rmt = this.dao.query(remittanceId);
        Date dateSent = this.generateDateObjectFromStr(rmt.getUtcDateSent());
        if (dateSent == null) return false;
        Date latestSettlementEnd1 = this.dao.queryLatestSettlementEnd(rmt.getSender(), rmt.getReceiver());
        Date latestSettlementEnd2 = this.dao.queryLatestSettlementEnd(rmt.getReceiver(), rmt.getSender());
        if(latestSettlementEnd1!=null) return dateSent.after(latestSettlementEnd1)||dateSent.equals(latestSettlementEnd1);
        if(latestSettlementEnd2!=null) return dateSent.after(latestSettlementEnd2)||dateSent.equals(latestSettlementEnd2);
        return true;
    }

    @Override
    public String getEarliestAvailableUtcDate() {
        Date lastSettlementPeriodEnd = this.dao.queryLastSettlementEnd();
        return DateHelper.toString(lastSettlementPeriodEnd, "yyyy-MM-dd", "UTC");
    }

    @Override
    public String importRemittanceData(byte[] fileBytes) {
        List<CSVRecord> records = getRecordsFromFile(fileBytes);
        List<RemittanceImportItem> itemList = new ArrayList<>();
        for (CSVRecord record : records) {
            if (record.size() != 11) break;
            if (!StringUtils.hasText(record.get(0))) continue;
            String dateSent = record.get(0);
            String dateReceived = record.get(1);
            Date sent = DateHelper.toDate(dateSent+" UTC","yyyyMMdd z");
            Date received = DateHelper.toDate(dateReceived+" UTC","yyyyMMdd z");
            Assert.isTrue(sent != null, "date sent is null");
            Assert.isTrue(received != null, "date received is null");
            validateDates(sent, received);
            Integer fromId = companyRepo.queryIdFromKcode(record.get(2).toUpperCase());
            Integer toId = companyRepo.queryIdFromKcode(record.get(3).toUpperCase());
            Integer currencyId = Currency.getId(record.get(4).toUpperCase());
            BigDecimal amount = new BigDecimal(record.get(5));
            String reference = record.get(6);
            BigDecimal feeAmount = new BigDecimal(record.get(7));
            Boolean feeIncluded = Boolean.valueOf(record.get(8));
            String statementName = record.get(9);
            BigDecimal bankPayment = new BigDecimal(record.get(10));

            RemittanceImportItem item = new RemittanceImportItemImpl(
                    dateSent, dateReceived, fromId, toId, amount, currencyId, reference,
                    feeAmount, feeIncluded, statementName, bankPayment);
            itemList.add(item);
        }
        if (itemList.isEmpty()) return "Incorrect data or no data found";
        int insertCount = dao.insertFromCSV(itemList);
        return insertCount + " record(s) inserted";
    }

    private static List<CSVRecord> getRecordsFromFile(byte[] fileData) {
        try {
            Reader reader = new StringReader(new String(fileData));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            List<CSVRecord> records = parser.getRecords();
            parser.close();
            return records;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}