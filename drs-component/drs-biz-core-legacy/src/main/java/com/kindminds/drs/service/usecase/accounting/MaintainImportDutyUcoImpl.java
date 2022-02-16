package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.usecase.MaintainImportDutyUco;
import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction;
import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction.ImportDutyTransactionLineItem;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.data.access.usecase.accounting.MaintainImportDutyDao;
import com.kindminds.drs.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class MaintainImportDutyUcoImpl implements MaintainImportDutyUco {
	
	@Autowired private MaintainImportDutyDao dao;

	@Override
	public DtoList<ImportDutyTransaction> getList(int pageIndex) {
		int totalCount = this.dao.queryTotalCount();
		Pager pager = new Pager(pageIndex,totalCount,20);
		DtoList<ImportDutyTransaction> dtoList = new DtoList<ImportDutyTransaction>();
		dtoList.setTotalRecords(totalCount);
		dtoList.setPager(pager);
		dtoList.setItems(this.dao.queryList(pager.getStartRowNum(),pager.getPageSize()));
		return dtoList;
	}
	
	@Override
	public ImportDutyTransaction get(String unsName) {
		return this.dao.query(unsName);
	}

	@Override
	public String create(ImportDutyTransaction duty) {
		BigDecimal totalAmount = this.getTotalAmount(duty.getLineItems());
		return this.dao.insert(duty,this.toUtcDate(duty.getUtcDate()),totalAmount);
	}

	@Override
	public String update(ImportDutyTransaction duty) {
		BigDecimal totalAmount = this.getTotalAmount(duty.getLineItems());
		return this.dao.update(duty, this.toUtcDate(duty.getUtcDate()), totalAmount);
	}

	@Override
	public void delete(String unsName) {
		this.dao.delete(unsName);
		return;
	}

	@Override
	public List<String> getUnsNameList() {
		return this.dao.queryUnsNameList();
	}

	@Override
	public String getCountry(String unsName) {
		return this.dao.queryCountry(unsName);
	}

	@Override
	public String getCurrency(String countryName) {
		return Country.valueOf(countryName).getCurrency().name();
	}

	@Override
	public List<ImportDutyTransactionLineItem> getLineItemInfoForCreate(String unsName) {
		return this.dao.queryLineItemInfoForCreate(unsName);
	}
	
	private BigDecimal getTotalAmount(List<ImportDutyTransactionLineItem> lineItems){
		BigDecimal totalAmount = BigDecimal.ZERO;
		for(ImportDutyTransactionLineItem item:lineItems){
			totalAmount = totalAmount.add(new BigDecimal(item.getAmount())); 
		}
		return totalAmount;
	}

	private Date toUtcDate(String dateStr){
		return DateHelper.toDate(dateStr+" UTC", "yyyy-MM-dd z");
	}

}
