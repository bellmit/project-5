package com.kindminds.drs.service.usecase.builder;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CustomerIssueDtoListBuilderFilterByCategory extends CustomerIssueDtoListBuilder {

	@Override
	public void setTotalRecordCounts() {
		Assert.notNull(this.dtoList);
		this.dtoList.setTotalRecords(this.dao.queryIssueCount(this.filterKcode,this.filterCategoryId));
	}

	@Override
	public void setItems() {
		Assert.notNull(this.dtoList.getPager());
		this.dtoList.setItems(this.dao.queryIssueList(this.filterKcode,this.filterCategoryId,this.dtoList.getPager().getStartRowNum(),this.dtoList.getPager().getPageSize()));
	}

}
