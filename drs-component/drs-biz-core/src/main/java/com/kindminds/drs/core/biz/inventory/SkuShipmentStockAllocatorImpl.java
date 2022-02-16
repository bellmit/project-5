package com.kindminds.drs.core.biz.inventory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import com.kindminds.drs.api.data.access.rdb.inventory.SkuShipmentStockAllocatorDao;
import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction;
import com.kindminds.drs.api.v2.biz.domain.model.inventory.ShipmentStockAvailableInfo;
import com.kindminds.drs.api.v2.biz.domain.model.inventory.SkuShipmentStockAllocator;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;
import com.kindminds.drs.v1.model.impl.stock.IvsInfoImpl;
import com.kindminds.drs.v1.model.impl.stock.UnsInfoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kindminds.drs.Country;





@Service
public class SkuShipmentStockAllocatorImpl implements SkuShipmentStockAllocator {

	@Autowired
	private SkuShipmentStockAllocatorDao dao;

	//private SkuShipmentStockAllocatorDao dao = (SkuShipmentStockAllocatorDao) SpringAppCtx
			//.get().getBean(SkuShipmentStockAllocatorDao.class);

	private List<ShipmentStockAvailableInfo> getAvailableInfos(Country unsDestinationCountry, MarketSideTransaction transaction){

		List<Object []> columnsList = this.dao.queryShipmentStockAvailableInfo(unsDestinationCountry,
				transaction.getSku());


		List<ShipmentStockAvailableInfo> availableInfos = new ArrayList<>();
		for(Object[] columns:columnsList){

			String unsName = (String)columns[0];
			String ivsName = (String)columns[1];
			//Integer qty = (Integer)columns[2];

			Integer ivsSkuIdentificationId = ((Long)columns[2]).intValue();
			String ivsSkuIdentificationSerialNo = (String) columns[3];
			String ivsSkuIdentificationRemark =  columns[4] != null ? (String)columns[4] : "" ;
			Integer ivsSkuIdentificationDrsTranId = columns[5] != null ? (Integer)columns[5] : 0 ;
			String ivsSkuIdentificationStatus =  columns[6] != null ? (String)columns[6] : "" ;

			availableInfos.add(new ShipmentStockAvailableInfoImpl(unsName,ivsName,1 ,
					ivsSkuIdentificationId,ivsSkuIdentificationSerialNo , ivsSkuIdentificationRemark ,
					ivsSkuIdentificationDrsTranId,ivsSkuIdentificationStatus));
		}

		return availableInfos;

	}

	@Override
	public List<SkuShipmentAllocationInfo> requestAllocations(Country unsDestinationCountry, MarketSideTransaction transaction,
															  Integer quantity) {



		List<ShipmentStockAvailableInfo> availableInfos = this.getAvailableInfos(unsDestinationCountry,transaction);

		//todo arthur narf
		if(availableInfos.size() == 0 && unsDestinationCountry == Country.CA){
			if(dao.queryShipmentInfo(unsDestinationCountry,transaction.getSku()).size() == 0)
			availableInfos = this.getAvailableInfos(Country.US,transaction);
		}

		//todo arthur eu/uk
		if(availableInfos.size() == 0 && unsDestinationCountry == Country.UK){
			//if(dao.queryShipmentInfo(unsDestinationCountry,transaction.getSku()).size() == 0)
				availableInfos = this.getAvailableInfos(Country.EU,transaction);
		}

		//todo arthur eu/uk
		if(availableInfos.size() == 0 && unsDestinationCountry == Country.EU){
			//if(dao.queryShipmentInfo(unsDestinationCountry,transaction.getSku()).size() == 0)
				availableInfos = this.getAvailableInfos(Country.UK,transaction);
		}

		//todo arhtur fr/de/eu
		if(availableInfos.size() == 0 && ( unsDestinationCountry == Country.DE || unsDestinationCountry == Country.FR )){
			//if(dao.queryShipmentInfo(unsDestinationCountry,transaction.getSku()).size() == 0)
				availableInfos = this.getAvailableInfos(Country.EU,transaction);
		}

		//todo arhtur fr/de/uk
		if(availableInfos.size() == 0 && ( unsDestinationCountry == Country.DE || unsDestinationCountry == Country.FR)){
			//if(dao.queryShipmentInfo(unsDestinationCountry,transaction.getSku()).size() == 0)
				availableInfos = this.getAvailableInfos(Country.UK,transaction);
		}

		Assert.isTrue(quantity <= this.getTotalAvailableQuantity(availableInfos), "Request too much." + unsDestinationCountry );


		List<SkuShipmentAllocationInfo> skuShipmentAllocationInfos = new ArrayList<>();

		//todo arthur possible error
		for(int i=0;i<quantity;i++){

			Assert.isTrue(availableInfos.size() >= 1, "availableInfos.size()>=1");
			//Assert.isTrue(availableInfos.get(0).getQuantity() >= 1, "availableInfos.get(0).getQuantity()>=1");


			ShipmentStockAvailableInfo target=availableInfos.get(0);

			Object []  columns1 = this.dao.queryIvsInfo(availableInfos.get(0).getIvsName(),transaction.getSku());

			String ivsName = (String)columns1[0];
			int fcaCurrencyId = (int)columns1[1];
			BigDecimal fcaPrice = (BigDecimal)columns1[2];
			BigDecimal salesTaxRate = (BigDecimal)columns1[3];
			SkuShipmentAllocationInfo.IvsInfo ivsInfo = new IvsInfoImpl(ivsName, fcaCurrencyId, fcaPrice, salesTaxRate);


			Object [] columns = this.dao.queryUnsInfo(availableInfos.get(0).getUnsName(),transaction.getSku());

			String unsName = (String)columns[0];
			int ddpCurrencyId = (int)columns[1];
			BigDecimal ddpAmount = (BigDecimal)columns[2];
			BigDecimal fxRateFromFcaCurrencyToDestinationCountryCurrency = (BigDecimal)columns[3];
			BigDecimal fxRateFromFcaCurrencyToEur = (BigDecimal)columns[4];
			UnsInfoImpl unsInfo = new UnsInfoImpl(unsName, ddpCurrencyId, ddpAmount, fxRateFromFcaCurrencyToDestinationCountryCurrency, fxRateFromFcaCurrencyToEur);


			skuShipmentAllocationInfos.add(
					new SkuShipmentAllocationInfoImplSvc(
							target.getIvsSkuIdentificationId() , target.getIvsSkuIdentificationSerialNo(),
							target.getIvsSkuIdentificationRemark(),target.getIvsSkuIdentificationDrsTranId(),
							target.getIvsSkuIdentificationStatus(),
							transaction.getSku(),ivsInfo,unsInfo));

			//target.setQuantity(target.getQuantity()-1);
			//if(target.getQuantity()==0) availableInfos.remove(0);

			availableInfos.remove(0);

		}

		Assert.isTrue(skuShipmentAllocationInfos.size()==quantity,
				"skuShipmentAllocationInfos.size()==quantity");

		return skuShipmentAllocationInfos;
	}

	@Override
	public List<SkuShipmentAllocationInfo> requestAllocations(Country unsDestinationCountry,String drsSku,Integer quantity) {

		List<Object[]> columnsList =
				this.dao.queryShipmentStockAvailableInfo(unsDestinationCountry, drsSku);


		List<ShipmentStockAvailableInfo> availableInfos = new ArrayList<>();
		for(Object[] columns:columnsList){
			String unsName = (String)columns[0];
			String ivsName = (String)columns[1];
			//Integer qty = (Integer)columns[2];

			Integer ivsSkuIdentificationId = ((Long)columns[2]).intValue();
			String ivsSkuIdentificationSerialNo = (String) columns[3];
			String ivsSkuIdentificationRemark =  columns[4] != null ? (String)columns[4] : "" ;
			Integer ivsSkuIdentificationDrsTranId = columns[5] != null ? (Integer)columns[5] : 0 ;
			String ivsSkuIdentificationStatus =  columns[6] != null ? (String)columns[6] : "" ;

			availableInfos.add(new ShipmentStockAvailableInfoImpl(unsName,ivsName,1 ,
					ivsSkuIdentificationId,ivsSkuIdentificationSerialNo,ivsSkuIdentificationRemark,
					ivsSkuIdentificationDrsTranId ,ivsSkuIdentificationStatus));
		}

		Assert.isTrue(quantity<=this.getTotalAvailableQuantity(availableInfos),
				"Request too much.");

		List<SkuShipmentAllocationInfo> skuShipmentAllocationInfos = new ArrayList<>();

		//todo arthur possible error
		for(int i=0;i<quantity;i++){

			Assert.isTrue(availableInfos.size()>=1,"availableInfos.size()>=1");
			//Assert.isTrue(availableInfos.get(0).getQuantity()>=1,"availableInfos.get(0).getQuantity()>=1");

			ShipmentStockAvailableInfo target=availableInfos.get(0);

			Object [] columns1 = this.dao.queryIvsInfo(availableInfos.get(0).getIvsName(),drsSku);
			String ivsName = (String)columns1[0];
			int fcaCurrencyId = (int)columns1[1];
			BigDecimal fcaPrice = (BigDecimal)columns1[2];
			BigDecimal salesTaxRate= (BigDecimal)columns1[3];
			SkuShipmentAllocationInfo.IvsInfo ivsInfo = new IvsInfoImpl(ivsName, fcaCurrencyId, fcaPrice, salesTaxRate);


			Object [] columns = this.dao.queryUnsInfo(availableInfos.get(0).getUnsName(),drsSku);
			String unsName = (String)columns[0];
			int ddpCurrencyId = (int)columns[1];
			BigDecimal ddpAmount = (BigDecimal)columns[2];
			BigDecimal fxRateFromFcaCurrencyToDestinationCountryCurrency = (BigDecimal)columns[3];
			BigDecimal fxRateFromFcaCurrencyToEur = (BigDecimal)columns[4];
			UnsInfoImpl unsInfo = new UnsInfoImpl(unsName, ddpCurrencyId, ddpAmount, fxRateFromFcaCurrencyToDestinationCountryCurrency, fxRateFromFcaCurrencyToEur);

			skuShipmentAllocationInfos.add(
					new SkuShipmentAllocationInfoImplSvc(target.getIvsSkuIdentificationId() ,
							target.getIvsSkuIdentificationSerialNo(),
							target.getIvsSkuIdentificationRemark() ,
							target.getIvsSkuIdentificationDrsTranId(),
							target.getIvsSkuIdentificationStatus(),
							drsSku,ivsInfo,unsInfo));

			//target.setQuantity(target.getQuantity()-1);
			//if(target.getQuantity()==0) availableInfos.remove(0);
			availableInfos.remove(0);

		}

		Assert.isTrue(skuShipmentAllocationInfos.size()==quantity,
				"skuShipmentAllocationInfos.size()==quantity");

		return skuShipmentAllocationInfos;
	}


	private Integer getTotalAvailableQuantity(List<ShipmentStockAvailableInfo> availableInfos){
		/*
		Integer result=0;
		for(ShipmentStockAvailableInfo info:availableInfos){
			result+=info.getQuantity();
		}
		return result;
		 */
		return  availableInfos.size();
	}

	@Override
	public void increaseStockQuantity(String unsName, String ivsName, String drsSku, int quantity) {

		List<Object[]> ivsStockQuantity = dao.queryIvsStockQuantity(unsName, ivsName, drsSku);
		int updateQty;
		for (Object[] lineQuantity: ivsStockQuantity) {
			Integer lineSeq = (Integer) lineQuantity[0];
			Integer qtyOrder = (Integer) lineQuantity[1];
			Integer qtySold = (Integer) lineQuantity[2];
			Integer qtyReturned = (Integer) lineQuantity[3];
			if (qtySold + quantity - qtyReturned > qtyOrder) {
				updateQty = qtyOrder - qtySold + qtyReturned;
			} else {
				updateQty = quantity;
			}
			this.dao.increaseStockQuantity(unsName, ivsName, drsSku, updateQty, lineSeq);
			quantity = quantity - updateQty;
			if (quantity <= 0) {
				break;
			}
		}

		//todo arthur temp comment
		//Assert.isTrue(quantity <= 0, "UNS does not have enough stock.");
	}

	@Override
	public Integer updateQuantityReturnToSupplier(String ivsName, String unsName,
												  String drsSku, int quantity) {
		List<Object[]> ivsStockQuantity = dao.queryIvsStockQuantity(unsName, ivsName, drsSku);
		int updateQty;

		for (Object[] lineQuantity: ivsStockQuantity) {
			Integer lineSeq = (Integer) lineQuantity[0];
			Integer qtyOrder = (Integer) lineQuantity[1];
			Integer qtySold = (Integer) lineQuantity[2];
			Integer qtyReturned = (Integer) lineQuantity[3];
			if (qtySold + quantity - qtyReturned > qtyOrder) {
				updateQty = qtyOrder - qtySold + qtyReturned;
			} else {
				updateQty = quantity;
			}
			this.dao.increaseStockQuantity(unsName, ivsName, drsSku, updateQty, lineSeq);
			quantity = quantity - updateQty;

			if (quantity <= 0) break;
		}

		return quantity;
	}

	@Override
	public void updateQuantityReturnToSupplierAllowOverflow(String ivsName, String unsName,
															String drsSku, int quantity) {

		List<Object[]> ivsStockQuantity = dao.queryIvsStockQuantity(unsName, ivsName, drsSku);
		int updateQty;
		int listCount = 1;

		for (Object[] lineQuantity: ivsStockQuantity) {
			Integer lineSeq = (Integer) lineQuantity[0];
			Integer qtyOrder = (Integer) lineQuantity[1];
			Integer qtySold = (Integer) lineQuantity[2];
			Integer qtyReturned = (Integer) lineQuantity[3];

			if (listCount != ivsStockQuantity.size() &&
					qtySold + quantity - qtyReturned > qtyOrder) {

				updateQty = qtyOrder - qtySold + qtyReturned;

			} else {
				updateQty = quantity;
			}

			this.dao.increaseStockQuantity(unsName, ivsName, drsSku, updateQty, lineSeq);
			quantity = quantity - updateQty;

			listCount++;
			if (quantity <= 0) break;
		}

	}



}
