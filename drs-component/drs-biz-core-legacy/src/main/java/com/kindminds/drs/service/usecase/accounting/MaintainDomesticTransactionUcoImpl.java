package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.Context;
import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.usecase.accounting.MaintainDomesticTransactionUco;
import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction;
import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction.DomesticTransactionLineItem;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.accounting.MaintainDomesticTransactionDao;
import com.kindminds.drs.util.BigDecimalHelper;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.v1.model.impl.accounting.DomesticTransactionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class MaintainDomesticTransactionUcoImpl implements MaintainDomesticTransactionUco {
	
	@Autowired private MaintainDomesticTransactionDao dao;
	@Autowired private CompanyDao companyRepo;
	
	@Override
	public DtoList<DomesticTransaction> getList(int pageIndex) {
		DtoList<DomesticTransaction> list = new DtoList<>();
		list.setTotalRecords(this.dao.queryTotalCounts());
		list.setPager(new Pager(pageIndex, list.getTotalRecords(),50));
		List<Object []> columnsList = this.dao.queryList(list.getPager().getStartRowNum(),list.getPager().getPageSize());

		List<DomesticTransaction> resultList = new ArrayList<>();
		for(Object[] columns:columnsList){
			DomesticTransactionImpl dt = new DomesticTransactionImpl();
			dt.setId((Integer)columns[0]);
			dt.setTransactionDate((Date)columns[1]);
			dt.setSsdcKcode((String) columns[2]);
			dt.setSsdcName((String) columns[3]);
			dt.setSplrKcode((String) columns[4]);
			dt.setSplrName((String) columns[5]);
			dt.setInvoiceNumber((String) columns[6]);
			dt.setCurrencyId((Integer) columns[7]);
			dt.setAmountTotal((BigDecimal) columns[8]);
			resultList.add(dt);
		}

		this.setIsEditable(resultList);
		list.setItems(resultList);
		return list;
	}
	
	private void setIsEditable(List<DomesticTransaction> transactions){

		for(DomesticTransaction transaction : transactions){
			transaction.setIsEditable(true);
//			transaction.setIsEditable(isEditable(transaction.getTransactionDate()));
		}
	}

	@Override
	public Map<String,String> getSsdcKcodeNames() {
		Map<String,String> ssdcKcodeNames = new TreeMap<>();
		ssdcKcodeNames.put("K2", "Kindminds");
		return ssdcKcodeNames;
	}

	@Override
	public Map<String,String> getSplrKcodeNames(String ssdcKcode) {
		return this.companyRepo.querySupplierKcodeToShortEnUsNameMap(ssdcKcode);
	}

	@Override
	public Map<Integer,String> getLineItemTypeKeyName() {
		String typeClassName = "Domestic";
		return this.dao.queryLineItemTypeKeyName(typeClassName);
	}

	@Override
	public Integer createForSettlementTest(DomesticTransaction dt) {
		Date transactionDate = this.getUtcDate(dt.getUtcDate());
		return this.create(dt,transactionDate);
	}

	@Override
	public Integer create(DomesticTransaction dt) {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		Assert.isTrue(Context.getCurrentUser().isDrsUser()&&dt.getSsdcKcode().equals(userCompanyKcode));
		Date transactionDate = this.getUtcDate(dt.getUtcDate());
		if(transactionDate.before(this.dao.queryLastSettlementEnd())) return null;
		return this.create(dt, transactionDate);
	}
	
	private Date getUtcDate(String dateStr){
		return DateHelper.toDate(dateStr+" UTC","yyyy-MM-dd z");
	}
	
	private Integer create(DomesticTransaction dt,Date date){
		Assert.isTrue(dt.getLineItems().size()>=1);
		Currency currency = Currency.fromKey(this.dao.queryCurrencyId(dt.getSsdcKcode()));
		BigDecimal taxRate = this.calculateTaxRate(dt.getTaxPercentage());
		BigDecimal subtotal = this.calculateSubtotal(dt.getLineItems(),currency.getKey());
		BigDecimal tax = subtotal.multiply(taxRate).setScale(dt.getCurrency().getScale(),RoundingMode.HALF_UP);
		BigDecimal total = subtotal.add(tax);
		return this.dao.insert(dt, date, taxRate, currency.getKey(), subtotal, tax, total);
	}

	private BigDecimal calculateTaxRate(String taxPercentageStr) {
		BigDecimal taxPercentage = new BigDecimal(taxPercentageStr);
		return taxPercentage.divide(new BigDecimal("100"));
	}

	private BigDecimal calculateSubtotal(List<DomesticTransactionLineItem> lineItems,int scale) {
		BigDecimal subtotal = BigDecimal.ZERO;
		for(DomesticTransactionLineItem lineItem:lineItems){
			BigDecimal amount = new BigDecimal(lineItem.getAmount());
			subtotal = subtotal.add(amount);
		}
		return subtotal.setScale(scale,RoundingMode.HALF_UP);
	}

	@Override
	public DomesticTransaction get(Integer id) {
		Object [] columns = this.dao.query(id);

		DomesticTransactionImpl dt = new DomesticTransactionImpl();
		dt.setId((Integer)columns[0]);
		dt.setTransactionDate((Date)columns[1]);
		dt.setSsdcKcode((String) columns[2]);
		dt.setSsdcName((String) columns[3]);
		dt.setSplrKcode((String) columns[4]);
		dt.setSplrName((String) columns[5]);
		dt.setInvoiceNumber((String) columns[6]);
		dt.setTaxRate((BigDecimal) columns[7]);
		dt.setCurrencyId((Integer) columns[8]);
		dt.setAmountSubtotal((BigDecimal) columns[9]);
		dt.setAmountTax((BigDecimal) columns[10]);
		dt.setAmountTotal((BigDecimal) columns[11]);
		dt.setLineItems(this.dao.queryLineItems(dt.getId()));


		dt.setIsEditable(true);
//		t.setIsEditable(isEditable(t.getTransactionDate()));

		return dt;
	}

	@Override
	public void delete(int id) {
		this.dao.delete(id);
	}

	@Override
	public Integer update(DomesticTransaction transaction) {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();

		if (StringUtils.hasText(transaction.getUtcDate())) {
			Date transactionDate = DateHelper.toDate(
					transaction.getUtcDate() + " UTC", "yyyy-MM-dd z");

			if (isAllEditable(transactionDate)) {
				Assert.isTrue(Context.getCurrentUser().isDrsUser() && transaction.getSsdcKcode().equals(userCompanyKcode));
				Assert.isTrue(transaction.getLineItems().size() >= 1);
				Currency currency = Currency.fromKey(this.dao.queryCurrencyId(userCompanyKcode));
				BigDecimal taxRate = this.calculateTaxRate(transaction.getTaxPercentage());
				BigDecimal subtotal = this.calculateSubtotal(transaction.getLineItems(), currency.getKey());
				BigDecimal tax = subtotal.multiply(taxRate).setScale(transaction.getCurrency().getScale(), RoundingMode.HALF_UP);
				BigDecimal total = subtotal.add(tax);
				return this.dao.update(transaction, transactionDate, taxRate, currency.getKey(), subtotal, tax, total);
			}
		}

		return dao.updateInvoiceNumber(transaction);
	}

	@Override
	public String getEarliestAvailableUtcDate() {
		Date lastSettlementPeriodEnd = this.dao.queryLastSettlementEnd();
		return DateHelper.toString(lastSettlementPeriodEnd, "yyyy-MM-dd", "UTC");
	}

	@Override
	public Boolean isAllEditable(Date transactionDate) {
		return !dao.queryBillStatementExists(transactionDate);
	}

	@Override
	public Currency getCurrency(String ssdcKcode) {
		return this.companyRepo.queryCompanyCurrency(ssdcKcode);
	}

	@Override
	public String getDefaultSalesTaxPercentage() {
		if(!Context.getCurrentUser().isDrsUser()) return null;
		BigDecimal salesTaxRate = null;
		Country handlerCountry = Country.fromKey(this.companyRepo.queryCompanyCountryId(Context.getCurrentUser().getCompanyKcode()));
		salesTaxRate = handlerCountry.getSalesTaxRate();
		return salesTaxRate==null?null:salesTaxRate.multiply(BigDecimalHelper.ONE_HUNDRED).stripTrailingZeros().toPlainString();
	}

}
