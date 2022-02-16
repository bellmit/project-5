package com.kindminds.drs.service.usecase.report.amazon;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonDetailPageSalesTrafficByChildItemReportUco;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriodImportingStatus;
import com.kindminds.drs.api.v1.model.amazon.AmazonDetailPageSalesTrafficByChildItemReportRawLine;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonDetailPageSalesTrafficByChildItemReportDao;
import com.kindminds.drs.v1.model.impl.SettlementPeriodImportingStatusImpl;
import com.kindminds.drs.service.helper.AmazonDetailPageSalesTrafficByChildItemReportHelper;
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImportAmazonDetailPageSalesTrafficByChildItemReportUcoImpl implements ImportAmazonDetailPageSalesTrafficByChildItemReportUco {
	
	@Autowired private ImportAmazonDetailPageSalesTrafficByChildItemReportDao dao;
	
	@Override
	public List<Marketplace> getMarketplaces() {
		return Marketplace.getAmazonMarketplaces();
	}

	@Override
	public String importFile(String marketplaceId,int periodId,byte[] fileBytes) {
		if(StringUtils.isEmpty(marketplaceId)) return "Please select marketplace";
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		try {
			AmazonDetailPageSalesTrafficByChildItemReportHelper reportHelper = new AmazonDetailPageSalesTrafficByChildItemReportHelper(); 
			List<AmazonDetailPageSalesTrafficByChildItemReportRawLine> rawLines = reportHelper.createReportRawLines(marketplace, fileBytes);
			Integer insertedLines = this.dao.insertRawLines(marketplace,periodId,rawLines);
			return insertedLines+" line(s) imported.";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	@Override
	public List<SettlementPeriod> getUnOccupiedSettlementPeriods(String marketplaceId) {
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		List<Integer> periodIdsToExclude = this.dao.queryOccupiedPeriodIdList(marketplace);
		if(periodIdsToExclude.isEmpty()){
			List<Object []> columnsList =  this.dao.querySettlementPeriodList();
			List<SettlementPeriod> listToReturn = new ArrayList<SettlementPeriod>();
			for(Object[] columns:columnsList){
				int id =(int)columns[0];
				Date start =(Date)columns[1];
				Date end =(Date)columns[2];
				listToReturn.add(new SettlementPeriodImpl(id, start, end));
			}
			return listToReturn;
		}
		else{
			List<Object []> columnsList  =  this.dao.querySettlementPeriodList(periodIdsToExclude);

			List<SettlementPeriod> listToReturn = new ArrayList<SettlementPeriod>();
			for(Object[] columns:columnsList){
				int id =(int)columns[0];
				Date start =(Date)columns[1];
				Date end =(Date)columns[2];
				listToReturn.add(new SettlementPeriodImpl(id, start, end));
			}
			return listToReturn;
		}
	}

	@Override
	public String deleteReportDataByPeriod(String marketplaceId,String periodId) {
		if(!StringUtils.hasText(periodId)) return null;
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		Integer settlementPeriodId = Integer.parseInt(periodId);
		this.dao.deleteReportDataByPeriod(marketplace,settlementPeriodId);
		return null;
	}

	@Override
	public List<SettlementPeriodImportingStatus> getSettlementPeriodImportingStatuses(String marketplaceId){
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		List<Integer> occupiedPeriodIdList = this.dao.queryOccupiedPeriodIdList(marketplace);
		List<Object []> columnsList = this.dao.querySettlementPeriodList();
		List<SettlementPeriod> allPeriods = new ArrayList<SettlementPeriod>();
		for(Object[] columns:columnsList){
			int id =(int)columns[0];
			Date start =(Date)columns[1];
			Date end =(Date)columns[2];
			allPeriods.add(new SettlementPeriodImpl(id, start, end));
		}

		List<SettlementPeriodImportingStatus> importingStatuses = new ArrayList<>();
		for(SettlementPeriod period:allPeriods) {
			Boolean isImported = occupiedPeriodIdList.contains(period.getId())?true:false;
			importingStatuses.add(new SettlementPeriodImportingStatusImpl(period,isImported));
		}
		return importingStatuses;
	}

}
