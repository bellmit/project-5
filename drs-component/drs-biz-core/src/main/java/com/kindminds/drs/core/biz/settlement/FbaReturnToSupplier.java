package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.TransactionLineType;


import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
import com.kindminds.drs.api.data.access.rdb.shipment.ShipmentSkuIdentificationDao;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItem;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionType;
import com.kindminds.drs.v1.model.impl.DrsTransactionLineItemSvcImpl;
import com.kindminds.drs.v1.model.impl.DrsTransactionSvcImpl;
import com.kindminds.drs.api.v2.biz.domain.model.inventory.SkuShipmentStockAllocator;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FbaReturnToSupplier extends AmazonTransaction {


	private DrsTransactionDao dtRepo = (DrsTransactionDao) SpringAppCtx
			.get().getBean(DrsTransactionDao.class);

	private ShipmentSkuIdentificationDao skuIdentifyDao = (ShipmentSkuIdentificationDao)SpringAppCtx
			.get().getBean(ShipmentSkuIdentificationDao.class);

	private SkuShipmentStockAllocator stockAllocator = (SkuShipmentStockAllocator)SpringAppCtx
			.get().getBean(SkuShipmentStockAllocator.class);

//	@Autowired private SkuShipmentStockAllocator stockAllocator;

	public FbaReturnToSupplier(Integer id, Date transactionDate, String type,
							   String source, String sourceId, String sku) {

		super(id, transactionDate, type, source, sourceId, sku);
	}

	public FbaReturnToSupplier(Integer id, Date transactionDate, String type,
							   String source, String sourceId, String sku, String description) {

		super(id, transactionDate, type, source, sourceId, sku, description);
	}


	@Override
	public List<Integer> process(Date periodStart, Date periodEnd) {

		Assert.isTrue(DrsTransactionType.containsType(this.getType()),"DrsTransactionType.containsType(transaction.getType())");

		Marketplace sourceMarketplace = Marketplace.fromName(this.getSource());
		String drsSku = this.getSku();
		String msdcKcode = this.dao.queryMsdcKcode(sourceMarketplace.getCountry());
		String splrKcode = this.dao.querySupplierKcode(drsSku);
		String hdlrKcode = this.dao.queryHandlerDrsKcode(splrKcode);

		Integer quantity = this.dao.queryFbaReturnToSupplierQuantity(this.getTransactionDate(),sourceMarketplace.getName(),drsSku);

		//String ivsName =this.dao.queryFbaReturnToSupplierIvsName(this.getTransactionDate(),sourceMarketplace.getName(),drsSku);
		//String unsName = this.dao.queryFbaReturnToSupplierUnsName(this.getTransactionDate(),sourceMarketplace.getName(),drsSku);

		//update stock quantity, if ivs empty, then move onto the next ivs
		//updateStockQtyAccountForOverFlow(drsSku, ivsName, quantity);


//
		List<Integer> resultIds = new ArrayList<>();

		for(int i=0;i<quantity;i++){

			//todo arthur need modfiy
			String sellbackType = getSellbackType(this.getType());

			List<Object []> resultList = null;

			if(sellbackType.equals("SS2SP_Inventory_Sell_Back_Recovery")){
				resultList =this.skuIdentifyDao.
						queryCanBeSoldBackRecovery(sourceMarketplace.getUnsDestinationCountry(), drsSku);

			}else{
				System.out.println("AAAAAAAAAAaAA");
				System.out.println(sourceMarketplace.getUnsDestinationCountry());
				System.out.println("AAAAAAAAAAaAA");
				resultList =this.skuIdentifyDao.
						queryCanBeSold(sourceMarketplace.getUnsDestinationCountry(), drsSku);
				Assert.isTrue(resultList.size()>0,"not enough qty for sellback");
			}


			String ivsName =  resultList.get(0)[4].toString();
			String unsName =  resultList.get(0)[5].toString();


			BigDecimal fcaPayment = this.getFcaPayment(ivsName, drsSku);
			Currency fcaCurrency = this.dao.queryShipmentCurrency(ivsName);

			DrsTransactionSvcImpl dt = new DrsTransactionSvcImpl(this.getType(),this.getTransactionDate(),
					i+1,drsSku,1,sourceMarketplace,this.getSourceId(),ivsName,unsName);

			int lineSeq=1;
			List<DrsTransactionLineItem> items = new ArrayList<DrsTransactionLineItem>();

			items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,msdcKcode,hdlrKcode,
					TransactionLineType.MS2SS_PRODUCT_INVENTORY_RETURN.getName(),Currency.USD.name(),BigDecimal.ZERO));


			if(sellbackType.equals("SS2SP_Inventory_Sell_Back_Recovery")){
				items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,hdlrKcode,splrKcode,
						TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT.getName(),   fcaCurrency.name(),fcaPayment.negate()));

				items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,hdlrKcode,splrKcode, sellbackType,
						fcaCurrency.name(),fcaPayment));
			}else{
				items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,hdlrKcode,splrKcode,
						TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT.getName(),   fcaCurrency.name(),fcaPayment));

				items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,hdlrKcode,splrKcode, sellbackType,
						fcaCurrency.name(),fcaPayment.negate()));
			}


			dt.setSettleableItemList(items);

			int dtId = this.dtRepo.insert(dt);
			this.doShipmentSkuIdentification(dtId, drsSku ,resultList);

			//todo arthur need refactoring
			stockAllocator.updateQuantityReturnToSupplierAllowOverflow(ivsName, unsName, drsSku, 1);

			resultIds.add(dtId);

		}

		return resultIds;
	}

	private void doShipmentSkuIdentification(int drsTranId  , String sku, List<Object []> resultList){

		Integer id = ((Long) resultList.get(0)[0]).intValue();
		Integer oldDrsTranId =  resultList.get(0)[1] != null ?  (Integer) resultList.get(0)[1] : 0;
		String status =  resultList.get(0)[2] != null ? resultList.get(0)[2].toString() : "";
		String remark =  resultList.get(0)[3] != null ? resultList.get(0)[3] .toString() : "";

		remark = remark + "/" + oldDrsTranId + "_" + status;

		this.skuIdentifyDao.update(id , drsTranId,this.getType(),remark);


	}

//	private void updateStockQtyAccountForOverFlow(String drsSku, String ivsName, Integer quantity) {
//		List<Object[]> ivsUnsList = dao.queryFBAIvsUnsListBySku(drsSku, ivsName);
//		Integer remainingQty = quantity;
//
//		for (Object[] ivsUns : ivsUnsList) {
//			ivsName = (String) ivsUns[0];
//			String unsName = (String) ivsUns[1];
//			remainingQty = stockAllocator.updateQuantityReturnToSupplier(
//					ivsName, unsName, drsSku, remainingQty);
//
//			if (remainingQty <= 0) break;
//		}
//
//		Assert.isTrue(remainingQty <= 0,
//				"UNS does not have enough stock. quantity needed: " + remainingQty);
//	}
	
	private String getSellbackType(String transactionType) {
		if (transactionType.equalsIgnoreCase("FBA Returns Taiwan")) {
			return TransactionLineType.SS2SP_INVENTORY_SELL_BACK_TAIWAN.getName();
		} else if (transactionType.equalsIgnoreCase("FBA Returns Other")) {
			return TransactionLineType.SS2SP_INVENTORY_SELL_BACK_OTHER.getName();
		} else if (transactionType.equalsIgnoreCase("FBA Returns Dispose")) {
			return TransactionLineType.SS2SP_INVENTORY_SELL_BACK_DISPOSE.getName();
		} else if (transactionType.equalsIgnoreCase("FBA Returns Recovery")) {
			return TransactionLineType.SS2SP_INVENTORY_SELL_BACK_RECOVERY.getName();
		}

		return TransactionLineType.SS2SP_UNIT_INVENTORY_SELL_BACK.getName();
	}

	private BigDecimal getFcaPayment(String ivsName,String drsSku){
		BigDecimal taxExcludedFcaAmount = this.dao.queryTaxExcludedFcaAmount(ivsName,drsSku);
		BigDecimal salesTaxRate = this.dao.querySalesTaxRate(ivsName);
		BigDecimal salesTax = taxExcludedFcaAmount.multiply(salesTaxRate);
		return taxExcludedFcaAmount.add(salesTax);

	}

}
