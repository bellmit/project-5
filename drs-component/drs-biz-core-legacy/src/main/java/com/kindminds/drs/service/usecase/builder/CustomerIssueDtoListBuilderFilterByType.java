package com.kindminds.drs.service.usecase.builder;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CustomerIssueDtoListBuilderFilterByType extends CustomerIssueDtoListBuilder {

	@Override
	public void setTotalRecordCounts() {
		Assert.notNull(this.dtoList);
		this.dtoList.setTotalRecords(this.dao.queryIssueCount(this.filterKcode,this.filterCategoryId,this.filterTypeId));
	}

	@Override
	public void setItems() {
		Assert.notNull(this.dtoList.getPager());
		this.dtoList.setItems(this.dao.queryIssueList(this.filterKcode,this.filterCategoryId,this.filterTypeId,this.dtoList.getPager().getStartRowNum(),this.dtoList.getPager().getPageSize()));
	}

}
