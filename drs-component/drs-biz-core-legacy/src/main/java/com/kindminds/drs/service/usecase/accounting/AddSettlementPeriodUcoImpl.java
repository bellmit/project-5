package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.api.usecase.settlement.AddSettlementPeriodUco;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.data.access.usecase.accounting.AddSettlementPeriodDao;
import com.kindminds.drs.api.data.access.rdb.accounting.SettlementPeriodDao;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AddSettlementPeriodUcoImpl implements AddSettlementPeriodUco {
	
	@Autowired private AddSettlementPeriodDao dao;
	@Autowired private SettlementPeriodDao settlementPeriodRepo;
	
	private int periodLengthDay = 14;

	private List<SettlementPeriod> processRecentPeriods(int counts) {
		List<Object []> columnsList = this.settlementPeriodRepo.queryRecentPeriods(counts);
		List<SettlementPeriod> periods = new ArrayList<SettlementPeriod>();
		for(Object[] columns:columnsList){
			int id =(int)columns[0];
			Date start =(Date)columns[1];
			Date end =(Date)columns[2];
			periods.add(new SettlementPeriodImpl(id, start, end));
		}

		return periods;
	}


	@Override
	public List<SettlementPeriod> getRecentPeriods() {
		return this.processRecentPeriods(3);
	}

	@Override
	public String addPeriod() {
		return this.addPeriod(new Date());
	}
	
	public String addPeriod(Date now){
		SettlementPeriod lastPeriod = this.getLastPeriod();
		Date lastPeriodEnd = lastPeriod.getEndPoint();
		Date nextPeriodEnd = DateHelper.getDaysAfter(lastPeriodEnd,this.periodLengthDay);
		SettlementPeriod period = new SettlementPeriodImpl(lastPeriodEnd,nextPeriodEnd);
		if(this.lateEnough(now, nextPeriodEnd)){
			if(this.settlementPeriodExisted(period)) return this.createFailMessageByDuplicatedPeriod(period);			
			this.dao.insertPeriod(period);
			return this.createSuccessfulMessage(period);
		}
		return this.createFailMessage(lastPeriod);
	}
	
	private SettlementPeriod getLastPeriod(){
		return this.processRecentPeriods(1).get(0);
	}
	
	private boolean lateEnough(Date now,Date nextPeriodEnd){
		return now.after(nextPeriodEnd)||now.compareTo(nextPeriodEnd)==0;
	}
	
	private boolean settlementPeriodExisted(SettlementPeriod period){
		return this.dao.selectPeriodExisted(period);		
	}
		
	private String createSuccessfulMessage(SettlementPeriod period){
		period.setFormat("yyyyMMdd");
		return "Period " + period.getStartDate() + " to "+ period.getEndDate() + " has been successfully added.";
	}
	
	private String createFailMessage(SettlementPeriod lastPeriod) {
		lastPeriod.setFormat("yyyyMMdd");
		return "No period added, current last period end is "+lastPeriod.getEndDate();
	}
	
	private String createFailMessageByDuplicatedPeriod(SettlementPeriod period) {
		period.setFormat("yyyyMMdd");
		return "Period " + period.getStartDate() + " to "+ period.getEndDate() + " is duplicated.";		
	}
	
}
