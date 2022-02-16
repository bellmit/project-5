package com.kindminds.drs.service.schedule.job;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.Warehouse;
import com.kindminds.drs.api.usecase.inventory.UpdateProductSkuInventoryHistoryUco;
import com.kindminds.drs.api.v1.model.product.ProductSkuInventoryHistory;
import com.kindminds.drs.api.data.access.usecase.inventory.UpdateProductSkuInventoryHistoryDao;
import com.kindminds.drs.v1.model.impl.ProductSkuInventoryHistoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class UpdateProductSkuInventoryHistoryUcoImpl implements UpdateProductSkuInventoryHistoryUco {
	
	@Autowired private UpdateProductSkuInventoryHistoryDao dao;

	@Override
	public void update() {
		Date now = new Date();
		Date snapshotDate = this.removeMinSecMilliSec(now);
		Date qtySoldEnd = (Date)snapshotDate.clone();
		Date qtySoldStart = this.getDaysAfter(snapshotDate,-1);
		for(Warehouse warehouse:Warehouse.values()){
			List<Integer> marketplaceIds = this.dao.queryMarketplaceIds(warehouse.getKey());
			System.out.println(warehouse.getKey());
			if (!marketplaceIds.isEmpty()) {
				List<Integer> productSkuIds = this.dao.queryProductSkuIds(marketplaceIds);
				List<ProductSkuInventoryHistory> historyList = this.createProductSkuInventoryHistory(warehouse.getKey(), snapshotDate, productSkuIds);
				this.appendQtyInStock(warehouse, historyList);
				this.appendQtyOrderedLastDay(qtySoldStart, qtySoldEnd, marketplaceIds, historyList);
				this.dao.insertHistoryList(historyList);
			}
		}
	}

	@Override
	public void update(Integer year, Integer month, Integer day) {
		OffsetDateTime snapshotOffsetDate = OffsetDateTime.of(
				year, month, day,0,0,0,0, ZoneOffset.UTC);
		Date snapshotDate = Date.from(snapshotOffsetDate.toInstant());
		Date qtySoldEnd = (Date)snapshotDate.clone();
		Date qtySoldStart = this.getDaysAfter(snapshotDate,-1);
		for(Warehouse warehouse:Warehouse.values()){
			List<Integer> marketplaceIds = this.dao.queryMarketplaceIds(warehouse.getKey());
			System.out.println(warehouse.getKey());
			if (!marketplaceIds.isEmpty()) {
				List<Integer> productSkuIds = this.dao.queryProductSkuIds(marketplaceIds);
				List<ProductSkuInventoryHistory> historyList = this.createProductSkuInventoryHistory(warehouse.getKey(), snapshotDate, productSkuIds);
				this.appendQtyInStock(warehouse, historyList);
				this.appendQtyOrderedLastDay(qtySoldStart, qtySoldEnd, marketplaceIds, historyList);
				this.dao.insertHistoryList(historyList);
			}
		}
	}
	
	private void appendQtyInStock(Warehouse warehouse,List<ProductSkuInventoryHistory> historyList){
		Marketplace representMarketplace = this.getRepresentMarketplace(warehouse);
		Map<Integer,Integer> skuQtyMap = this.dao.queryFbaQtyInStockPlusFbaQtyTransfer(representMarketplace);
		for(ProductSkuInventoryHistory history:historyList){
			if(skuQtyMap.containsKey(history.getProductSkuId())){
				((ProductSkuInventoryHistoryImpl)history).setFbaQtyInStockPlusfbaQtyTransfer(skuQtyMap.get(history.getProductSkuId()));
			}
		}
	}
	
	private Marketplace getRepresentMarketplace(Warehouse warehouse){
		Marketplace marketplace=null;
		if(warehouse==Warehouse.FBA_US) marketplace = Marketplace.AMAZON_COM;
		else if(warehouse==Warehouse.FBA_UK) marketplace = Marketplace.AMAZON_CO_UK;
		else if(warehouse==Warehouse.FBA_CA) marketplace = Marketplace.AMAZON_CA;
		else if(warehouse==Warehouse.FBA_DE) marketplace = Marketplace.AMAZON_DE;
		else if(warehouse==Warehouse.FBA_EU) marketplace = Marketplace.AMAZON_DE;
		else if(warehouse==Warehouse.FBA_FR) marketplace = Marketplace.AMAZON_FR;
		Assert.notNull(marketplace);
		return marketplace;
	}
	
	private void appendQtyOrderedLastDay(Date start,Date end,List<Integer> marketplaceIds,List<ProductSkuInventoryHistory> historyList) {
		Map<Integer,Integer> totalQtyOrdered = this.getTotalQtyOrdered(marketplaceIds,start, end);
		for(ProductSkuInventoryHistory history:historyList){
			if(totalQtyOrdered.containsKey(history.getProductSkuId())){
				((ProductSkuInventoryHistoryImpl)history).setQtyOrderedLastDay(totalQtyOrdered.get(history.getProductSkuId()));
			}
		}
	}
	
	private Map<Integer,Integer> getTotalQtyOrdered(List<Integer> marketplaceIds,Date from,Date to){
		Map<Integer,Integer> totalQtyOrdered = new HashMap<>();
		for(Integer marketplaceId:marketplaceIds){
			Marketplace marketplace = Marketplace.fromKey(marketplaceId);
			if(marketplace.isAmazonMarketplace()){
				Map<Integer,Integer> amazonQtyOrdered = this.dao.queryAmazonQtyOrdered(marketplace,from,to);
				this.accumulate(totalQtyOrdered, amazonQtyOrdered);
			}
			if(marketplace.isShopifyMarketplace()){
				Map<Integer,Integer> shopifyQtyOrdered = this.dao.queryShopifyQtyOrdered(marketplace,from,to);
				this.accumulate(totalQtyOrdered, shopifyQtyOrdered);
			}
		}
		return totalQtyOrdered;
	}
	
	private void accumulate(Map<Integer,Integer> total,Map<Integer,Integer> subTotal) {
		for(Integer subTotalSkuId :subTotal.keySet()){
			if(!total.containsKey(subTotalSkuId))
				total.put(subTotalSkuId, 0);
			total.put(subTotalSkuId,total.get(subTotalSkuId)+subTotal.get(subTotalSkuId));
		}
	}

	private List<ProductSkuInventoryHistory> createProductSkuInventoryHistory(int warehouseId, Date snapshotDate, List<Integer> productSkuIds){
		List<ProductSkuInventoryHistory> history = new ArrayList<>();
		for(Integer productSkuId:productSkuIds){
			history.add(new ProductSkuInventoryHistoryImpl(warehouseId, snapshotDate, productSkuId));
		}
		return history;
	}
	
	private Date removeMinSecMilliSec(Date date){
		Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.clear(Calendar.MILLISECOND);
	    c.clear(Calendar.MINUTE);
	    c.clear(Calendar.SECOND);
	    return c.getTime();
	}
	
	private Date getDaysAfter(Date date,int days){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}
	
}
