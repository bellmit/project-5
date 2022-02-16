package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.Context;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.usecase.accounting.MaintainInternationalTransactionUco;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.CashFlowDirection;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.InternationalTransactionLineItem;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.accounting.MaintainInternationalTransactionDao;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.v1.model.impl.accounting.InternationalTransactionImpl;
import com.kindminds.drs.v1.model.impl.accounting.InternationalTransactionLineItemImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

@Service
public class MaintainInternationalTransactionUcoImpl implements MaintainInternationalTransactionUco {

	@Autowired private MaintainInternationalTransactionDao dao;
	@Autowired private CompanyDao CompanyDao;

	@Override
	public List<CashFlowDirection> getCashFlowDirections() {
		return Arrays.asList(CashFlowDirection.values());
	}

	@Override
	public Map<String,String> getMsdcKcodeNameMap(){
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		Assert.isTrue(Context.getCurrentUser().isDrsUser());
		return this.dao.queryMsdcKcodeNameMap(userCompanyKcode);
	}

	@Override
	public Map<String,String> getSsdcKcodeNameMap(){
		Assert.isTrue(Context.getCurrentUser().isDrsUser());
		return this.dao.querySsdcKcodeNameMap("K2");
	}

	@Override
	public Map<String,String> getSplrKcodeNameMap(String ssdcKcode){
		Assert.isTrue(this.CompanyDao.isDrsCompany(ssdcKcode));
		return this.CompanyDao.querySupplierKcodeToShortEnUsNameMap(ssdcKcode);
	}

	@Override
	public Currency getCurrency(String msdcKcode){
		return this.CompanyDao.queryCompanyCurrency(msdcKcode);
	}

	@Override
	public Map<Integer,String> getLineItemKeyNameMap(int cashFlowDirectionKey){
		return this.dao.queryLineItemKeyNameMap(cashFlowDirectionKey);
	}

	@Override
	public DtoList<InternationalTransaction> getList(int pageIndex) {
		DtoList<InternationalTransaction> list = new DtoList<>();
		list.setTotalRecords(this.dao.queryTotalCounts());
		list.setPager(new Pager(pageIndex,list.getTotalRecords(),50));
		List<Object []>  columnsList = this.dao.queryList(list.getPager().getStartRowNum(), list.getPager().getPageSize());

		List<InternationalTransaction> transactions = new ArrayList<>();
		for(Object[] columns:columnsList){
			InternationalTransactionImpl it = new InternationalTransactionImpl();
			it.setId((Integer)columns[0]);
			it.setTransactionDate((Date)columns[1]);
			it.setCashFlowDirectionKey((Integer)columns[2]);
			it.setMsdcKcode((String)columns[3]);
			it.setMsdcName((String)columns[4]);
			it.setSsdcKcode((String)columns[5]);
			it.setSsdcName((String)columns[6]);
			it.setSplrKcode((String)columns[7]);
			it.setSplrName((String)columns[8]);
			it.setCurrencyId((Integer)columns[9]);
			it.setTotal((BigDecimal)columns[10]);
			transactions.add(it);
		}


		this.setIsEditable(transactions);
		list.setItems(transactions);
		return list;
	}

	private void setIsEditable(List<InternationalTransaction> transactions){
		String earliestAvailableUtcDate = this.getEarliestAvailableUtcDate();
		for(InternationalTransaction t:transactions){
			InternationalTransactionImpl orig = (InternationalTransactionImpl)t;
			if(orig.getUtcDate().compareTo(earliestAvailableUtcDate)<0)
				orig.setIsEditable(false);
			else
				orig.setIsEditable(true);
		}
	}

	@Override
	public String getEarliestAvailableUtcDate() {
		Date lastSettlementPeriodEnd = this.dao.queryLastSettlementEnd();
		return DateHelper.toString(lastSettlementPeriodEnd, "yyyy-MM-dd", "UTC");
	}

	@Override
	public InternationalTransaction get(Integer id) {
		Object [] columns = this.dao.query(id);

		InternationalTransactionImpl transaction = new InternationalTransactionImpl();
		transaction.setId((Integer)columns[0]);
		transaction.setTransactionDate((Date)columns[1]);
		transaction.setCashFlowDirectionKey((Integer)columns[2]);
		transaction.setMsdcKcode((String)columns[3]);
		transaction.setMsdcName((String)columns[4]);
		transaction.setSsdcKcode((String)columns[5]);
		transaction.setSsdcName((String)columns[6]);
		transaction.setSplrKcode((String)columns[7]);
		transaction.setSplrName((String)columns[8]);
		transaction.setCurrencyId((Integer)columns[9]);
		transaction.setTotal((BigDecimal)columns[10]);
		transaction.setNote((String)columns[11]);

		List<Object []> columnsList = this.dao.queryLineItems(id);
		List<InternationalTransactionLineItem> resultList = new ArrayList<>();
		for(Object[] columns2:columnsList){
			InternationalTransactionLineItemImpl item = new InternationalTransactionLineItemImpl();
			item.setLineSeq((Integer)columns2[0]);
			item.setItemKey((Integer)columns2[1]);
			item.setItemName((String)columns2[2]);
			item.setItemNote((String)columns2[3]);
			item.setSubtotal((BigDecimal)columns2[4]);
			item.setCurrencyId((Integer)columns2[5]);
			item.setVatRate((BigDecimal)columns2[6]);
			item.setVatAmount((BigDecimal)columns2[7]);
			resultList.add(item);
		}

		transaction.setLineItems(resultList);

		InternationalTransactionImpl orig = (InternationalTransactionImpl)transaction;
		String earliestAvailableUtcDate = this.getEarliestAvailableUtcDate();
		Boolean isEditable = orig.getUtcDate().compareTo(earliestAvailableUtcDate)<0?false:true;
		orig.setIsEditable(isEditable);
		return transaction;
	}

	@Override
	public List<InternationalTransaction> get(List<Integer> ids) {
		List<InternationalTransaction> internationalTransactions = new ArrayList<InternationalTransaction>();
		for(Integer id : ids) {
			internationalTransactions.add(get(id));
		}
		return internationalTransactions;
	}

	@Override
	public Integer create(InternationalTransaction transaction) {
		Date transactionDate = DateHelper.toDate(transaction.getUtcDate()+" UTC","yyyy-MM-dd z");
		BigDecimal total = this.calculateTotal(transaction.getLineItems());
		if(transactionDate.before(this.dao.queryLastSettlementEnd())) return null;
		return this.dao.insert(transaction,transactionDate,total);
	}

	private BigDecimal calculateTotal(List<InternationalTransactionLineItem> lineItems){
		Assert.isTrue(!lineItems.isEmpty());
		BigDecimal total = BigDecimal.ZERO;
		for(InternationalTransactionLineItem item:lineItems){
			total = total.add(new BigDecimal(item.getSubtotal()));
		}
		return total;
	}

	@Override
	public Integer update(InternationalTransaction transaction) {
		Date transactionDate = DateHelper.toDate(transaction.getUtcDate()+" UTC","yyyy-MM-dd z");
		if(transactionDate.before(this.dao.queryLastSettlementEnd())) return null;
		BigDecimal total = this.calculateTotal(transaction.getLineItems());
		return this.dao.update(transaction,transactionDate,total);
	}

	@Override
	public void delete(Integer id) {
		this.dao.delete(id);
	}

	@Override
	public Integer createForSettlementTest(InternationalTransaction t) {
		Date transactionDate = DateHelper.toDate(t.getUtcDate()+" UTC","yyyy-MM-dd z");
		BigDecimal total = this.calculateTotal(t.getLineItems());
		return this.dao.insert(t,transactionDate,total);
	}

	@Override
	public int countCouponTransactionsProcessed() {
		return this.dao.countCouponTransactionsProcessed();
	}

}