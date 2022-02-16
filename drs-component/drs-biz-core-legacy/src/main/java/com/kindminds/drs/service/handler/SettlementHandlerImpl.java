package com.kindminds.drs.service.handler;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v2.biz.domain.model.SettlementHandler;
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;

import com.kindminds.drs.api.usecase.CalculateRetainmentRateUco;
import com.kindminds.drs.api.usecase.accounting.DoMs2ssSettlementUco;
import com.kindminds.drs.api.usecase.accounting.DoSs2spSettlementUco;
import com.kindminds.drs.api.usecase.accounting.ProcessMarketSideTransactionUco;
import com.kindminds.drs.api.usecase.settlement.AddSettlementPeriodUco;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.data.access.rdb.accounting.SettlementHandlerDao;
import com.kindminds.drs.api.data.access.rdb.accounting.SettlementPeriodDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SettlementHandlerImpl implements SettlementHandler {

	@Autowired private SettlementPeriodDao settlementPeriodRepo;
	@Autowired private SettlementHandlerDao dao;
	
	@Autowired private AddSettlementPeriodUco addSettlementPeriodUco;
	@Autowired private CalculateRetainmentRateUco calculateRetainmentRateUco;
	//@Autowired private ProcessMarketSideTransactionUco processMarketSideTransactionUco;
	@Autowired private DoMs2ssSettlementUco doMs2ssSettlementUco;
	@Autowired private DoSs2spSettlementUco doSs2spSettlementUco;

	/*
	@Override
	public void doAllSettlement(){
		this.addSettlementPeriodUco.addPeriod();
		this.calculateRetainmentRateUco.calculate();
	//	this.processMarketSideTransactionUco.collectAndProcessMarketSideTransactions();
		
		
	}*/
	
	@Override
	public void doMs2ssSettlement() {
		this.doMs2ssSettlementUco.doSettlement();
	}

	@Override
	public void doSs2spSettlementInDraft() {
		this.doSs2spSettlementUco.createDraftForAllSupplier();
	}

	@Override
	public List<Marketplace> getAmazonMarketplaces() {
		return Marketplace.getAmazonMarketplaces();
	}
	
	@Override
	public Map<String,List<String>> getAmazonSettlementReportPeriodReadyMarketplaces(){
		List<Object []> columnsList = this.settlementPeriodRepo.queryRecentPeriods(3);
		List<SettlementPeriod> periods = new ArrayList<SettlementPeriod>();
		for(Object[] columns:columnsList){
			int id =(int)columns[0];
			Date start =(Date)columns[1];
			Date end =(Date)columns[2];
			periods.add(new SettlementPeriodImpl(id, start, end));
		}

		Map<String,List<String>> periodReportReadyMarketplaces = new LinkedHashMap<>();
		for(SettlementPeriod period:periods){
			period.setFormat("yyyyMMdd");
			List<Integer> readyMarketplaceIds = this.dao.queryAmazonSettlementReportReadyMarketplaceIds(period.getId());
			String periodText = period.getStartDate()+"-"+period.getEndDate();
			List<String> readyMarketplaces =  readyMarketplaceIds.stream().map(m->Marketplace.fromKey(m).getName()).collect(Collectors.toList());
			periodReportReadyMarketplaces.put(periodText,readyMarketplaces);
		}
		return periodReportReadyMarketplaces;
	}
	
}
