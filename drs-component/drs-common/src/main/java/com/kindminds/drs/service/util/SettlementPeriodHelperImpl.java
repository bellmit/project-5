package com.kindminds.drs.service.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kindminds.drs.api.data.access.rdb.accounting.SettlementPeriodHelperDao;
import com.kindminds.drs.util.SettlementPeriodHelper;

@Service
public class SettlementPeriodHelperImpl implements SettlementPeriodHelper {
	
	@Autowired private SettlementPeriodHelperDao dao;

	@Override
	public Date getPeriodStart(int settlementPeriodId) {
		return this.dao.queryPeriodStart(settlementPeriodId);
	}

	@Override
	public Date getPeriodEnd(int settlementPeriodId) {
		return this.dao.queryPeriodEnd(settlementPeriodId);
	}

}
