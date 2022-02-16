package com.kindminds.drs.service.helper;


import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction;
import com.kindminds.drs.core.biz.strategy.MarketSideTransactionProcessor;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionType;
import com.kindminds.drs.enums.AmazonTransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MarketSideTransactionHelper {
	
	@Autowired private ApplicationContext appContext;
	
	private final String processorBeanIdOrderUS = "marketSideTransactionProcessorOrderUS";
	private final String processorBeanIdOrderUK = "marketSideTransactionProcessorOrderUK";
	private final String processorBeanIdOrderCA = "marketSideTransactionProcessorOrderCA";
	private final String processorBeanIdOrderDE = "marketSideTransactionProcessorOrderDE";
	private final String processorBeanIdOrderIT = "marketSideTransactionProcessorOrderIT";
	private final String processorBeanIdOrderFR = "marketSideTransactionProcessorOrderFR";
	private final String processorBeanIdOrderES = "marketSideTransactionProcessorOrderES";
	private final String processorBeanIdOrderMX = "marketSideTransactionProcessorOrderMX";

	private final String processorBeanIdRefund = "marketSideTransactionProcessorRefund";
	private final String processorBeanIdFbaLostDamage = "marketSideTransactionProcessorFbaLostDamage";
	private final String processorBeanIdFbaReimbursement = "marketSideTransactionProcessorFbaReimbursement";
	private final String processorBeanIdFbaReturnToSellable = "marketSideTransactionProcessorFbaReturnToSellable";
	private final String processorBeanIdFbaReturnToSupplier = "marketSideTransactionProcessorFbaReturnToSupplier";
	private final String processorBeanIdShopifyOrder = "marketSideTransactionProcessorShopifyOrder";
	private final String processorBeanIdEbayOrder = "marketSideTransactionProcessorEbayOrder";
	private final String processorBeanIdCompensatedClawback = "marketSideTransactionProcessorCompensatedClawback";
	
	private String getProcessorBeanId(MarketSideTransaction transaction){
		String type = transaction.getType();
		String source = transaction.getSource();

		if(type.equals(AmazonTransactionType.ORDER.getValue())){
			if(source.equals("Amazon.com"))   return this.processorBeanIdOrderUS;
			if(source.equals("TrueToSource")) return this.processorBeanIdShopifyOrder;
			if(source.equals("eBay.com"))     return this.processorBeanIdEbayOrder;
			if(source.equals("Amazon.co.uk")) return this.processorBeanIdOrderUK;
			if(source.equals("Amazon.ca"))    return this.processorBeanIdOrderCA;
			if(source.equals("Amazon.de"))    return this.processorBeanIdOrderDE;
			if(source.equals("Amazon.it"))    return this.processorBeanIdOrderIT;
			if(source.equals("Amazon.fr"))    return this.processorBeanIdOrderFR;
			if(source.equals("Amazon.es"))    return this.processorBeanIdOrderES;
			if(source.equals("Amazon.com.mx"))    return this.processorBeanIdOrderMX;
			if(source.equals("eBay.de"))      return this.processorBeanIdEbayOrder;
			if(source.equals("eBay.it"))      return this.processorBeanIdEbayOrder;
			if(source.equals("eBay.co.uk"))   return this.processorBeanIdEbayOrder;
			if(source.equals("eBay.ca"))   return this.processorBeanIdEbayOrder;
		}
		else if(type.equals(AmazonTransactionType.OTHER.getValue())){
			if(transaction.getDescription().equals("WAREHOUSE_LOST"))         return this.processorBeanIdFbaLostDamage;
			if(transaction.getDescription().equals("WAREHOUSE_LOST_MANUAL"))         return this.processorBeanIdFbaLostDamage;
			if(transaction.getDescription().equals("REMOVAL_ORDER_LOST"))         return this.processorBeanIdFbaLostDamage;
			if(transaction.getDescription().equals("WAREHOUSE_DAMAGE"))       return this.processorBeanIdFbaLostDamage;
			if(transaction.getDescription().equals("MISSING_FROM_INBOUND"))   return this.processorBeanIdFbaLostDamage;
			if(transaction.getDescription().equals("FREE_REPLACEMENT_REFUND_ITEMS"))   return this.processorBeanIdFbaLostDamage;
			if(transaction.getDescription().equals("REVERSAL_REIMBURSEMENT")) return this.processorBeanIdFbaReimbursement;
			if(transaction.getDescription().equals("COMPENSATED_CLAWBACK")) return this.processorBeanIdCompensatedClawback;
		}
		else if(type.equals(AmazonTransactionType.REFUND.getValue()))                 return this.processorBeanIdRefund; 
		else if(type.equals("FBA Return To Sellable"))                                return this.processorBeanIdFbaReturnToSellable;
		else if(DrsTransactionType.containsType(type))	return this.processorBeanIdFbaReturnToSupplier;

		Assert.isTrue(false,"no ProcessorBeanId");
		return null;
	}

	public MarketSideTransactionProcessor getProcessor(MarketSideTransaction transaction) {
		String processorBeanId = this.getProcessorBeanId(transaction);
		Assert.notNull(processorBeanId,"processorBeanId null");
		return (MarketSideTransactionProcessor)this.appContext.getBean(processorBeanId);
	}
	
}
