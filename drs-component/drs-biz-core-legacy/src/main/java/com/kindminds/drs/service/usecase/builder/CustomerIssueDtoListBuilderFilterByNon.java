package com.kindminds.drs.service.usecase.builder;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CustomerIssueDtoListBuilderFilterByNon extends CustomerIssueDtoListBuilder {

	@Override
	public void setTotalRecordCounts() {
		Assert.notNull(this.dtoList);
		this.dtoList.setTotalRecords(this.dao.queryIssueCount(this.filterKcode));
	}

	@Override
	public void setItems() {
		Assert.notNull(this.dtoList.getPager());
		this.dtoList.setItems(this.dao.queryIssueList(this.filterKcode, this.dtoList.getPager().getStartRowNum(),this.dtoList.getPager().getPageSize()));
	}

}
