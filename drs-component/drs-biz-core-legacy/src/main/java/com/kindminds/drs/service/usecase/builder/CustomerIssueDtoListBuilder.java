package com.kindminds.drs.service.usecase.builder;

import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue;
import com.kindminds.drs.api.data.access.usecase.customercare.MaintainCustomerCareCaseIssueDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public abstract class CustomerIssueDtoListBuilder {
	
	@Autowired protected MaintainCustomerCareCaseIssueDao dao;
	
	protected int pageIndex;
	protected int sizePerPage=50;
	protected String filterKcode=null;
	protected Integer filterCategoryId=null;
	protected Integer filterTypeId=null;
	protected DtoList<CustomerCareCaseIssue> dtoList=null;
	
	public void initialize(int pageIndex, String filterKcode,Integer filterCategoryId, Integer filterTypeId){
		this.pageIndex = pageIndex;
		this.filterKcode = filterKcode;
		this.filterCategoryId = filterCategoryId;
		this.filterTypeId = filterTypeId;
		this.dtoList = new DtoList<CustomerCareCaseIssue>();
	}
	
	public abstract void setTotalRecordCounts();
	
	public void setPager(){
		Assert.notNull(this.dtoList);
		this.dtoList.setPager(new Pager(this.pageIndex,this.dtoList.getTotalRecords(),this.sizePerPage));
	}
	
	public abstract void setItems();
	
	public DtoList<CustomerCareCaseIssue> getDtoList(){
		Assert.notNull(this.dtoList);
		return this.dtoList;
	}
	
}
