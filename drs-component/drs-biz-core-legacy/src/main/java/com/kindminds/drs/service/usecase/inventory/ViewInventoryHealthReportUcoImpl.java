package com.kindminds.drs.service.usecase.inventory;

import com.kindminds.drs.Context;
import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.inventory.ViewInventoryHealthReportUco;
import com.kindminds.drs.v1.model.impl.report.InventoryHealthReportLineItemImpl;
import com.kindminds.drs.api.v1.model.report.InventoryHealthReport;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.inventory.ViewInventoryHealthReportDao;
import com.kindminds.drs.v1.model.impl.report.InventoryHealthReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ViewInventoryHealthReportUcoImpl implements ViewInventoryHealthReportUco {
	
	@Autowired private CompanyDao companyRepo;
	@Autowired private ViewInventoryHealthReportDao dao;
	
	@Override
	public Map<String,String> getSupplierKcodeNames() {
		return this.companyRepo.querySupplierKcodeToShortEnUsNameMap();
	}

	@Override
	public List<Marketplace> getMarketplaces() {
		List<Marketplace> marketplaces = new ArrayList<>();
		marketplaces.add(Marketplace.AMAZON_COM);
		marketplaces.add(Marketplace.AMAZON_CO_UK);
		marketplaces.add(Marketplace.AMAZON_CA);
		marketplaces.add(Marketplace.AMAZON_DE);
		return marketplaces;
	}
	
	@Override
	public Currency getCurrency(String marketplaceId) {
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		return marketplace.getCurrency();
	}

	@Override
	public InventoryHealthReport getReport(String marketplaceId, String assignedSupplierKcode) {
		Assert.isTrue(StringUtils.hasText(marketplaceId));
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		String actualSupplierKcode = this.getActualSupplierKcode(assignedSupplierKcode);
		InventoryHealthReportImpl report = new InventoryHealthReportImpl();
		report.setMarketplace(marketplace);
		report.setSupplierKcode(actualSupplierKcode);
		report.setSnapshotDate(this.dao.querySnapshotDate(marketplace.getKey()));

		List<Object []> columnsList = this.dao.queryLineItems(marketplace.getKey(), actualSupplierKcode);
		List<InventoryHealthReport.InventoryHealthReportLineItem> lineItems = new ArrayList<>();
		for(Object[] columns:columnsList){
			String sku = (String)columns[0];
			String productName = (String)columns[1];
			String condition = (String)columns[2];
			Integer unsellableQuantity = (Integer)columns[3];
			Integer sellableQuantity = (Integer)columns[4];
			Integer qtyToBeChargedLtsf6Mo = (Integer)columns[5];
			Integer qtyToBeChargedLtsf12Mo = (Integer)columns[6];
			BigDecimal projectedLtsf6Mo = (BigDecimal)columns[7];
			BigDecimal projectedLtsf12Mo = (BigDecimal)columns[8];
			Integer unitsShippedLast30Days = (Integer)columns[9];
			BigDecimal weeksOfCoverT30 = (BigDecimal)columns[10];
			lineItems.add(new InventoryHealthReportLineItemImpl(Integer.parseInt(marketplaceId), sku, productName, condition, unsellableQuantity, sellableQuantity, qtyToBeChargedLtsf6Mo, qtyToBeChargedLtsf12Mo, projectedLtsf6Mo, projectedLtsf12Mo, unitsShippedLast30Days, weeksOfCoverT30));
		}
		report.setLineItems(lineItems);
		return report;
	}
	
	private String getActualSupplierKcode(String assignedSupplierKcode){
		if(Context.getCurrentUser().isSupplier()) return Context.getCurrentUser().getCompanyKcode();
		else return StringUtils.hasText(assignedSupplierKcode)?assignedSupplierKcode:null;
	}

}
