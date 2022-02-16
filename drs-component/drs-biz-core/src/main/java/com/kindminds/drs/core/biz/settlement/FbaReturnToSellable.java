package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.enums.AmazonTransactionType;
import com.kindminds.drs.enums.Settlement;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;


public class FbaReturnToSellable extends AmazonTransaction {

	public FbaReturnToSellable(int id, Date transactionDate, String type,
							String source, String sourceId, String sku) {

		super(id, transactionDate, type, source, sourceId, sku);
	}

	public FbaReturnToSellable(int id, Date transactionDate, String type,
							String source, String sourceId, String sku, String description) {

		super(id, transactionDate, type, source, sourceId, sku, description);
	}

	
	@Override
	public List<Integer> process(Date periodStart, Date periodEnd) {
		Assert.isTrue(!this.getSku().equals("K489-RW70056-01"),"If assertion fail, remove this assertion and notify Bruce that the missed record has appeared.");
		if(this.getSku().equals("K489-RW70056-01")) return null;
		Object[] rawColumns = this.dao.queryReturnRawColumns(
				this.getTransactionDate(),this.getSourceId(),this.getSku(),
				Settlement.AmazonReturnReportDetailedDisposition.SELLABLE.getValue());

		String orderId = (String)rawColumns[0];
		String productSku = (String)rawColumns[1];
		Integer quantity = (Integer)rawColumns[2];
		String unsNameOrdr = this.dao.queryShipmentUnsName(orderId,productSku,AmazonTransactionType.ORDER.getValue());
		String ivsNameOrdr = this.dao.queryShipmentIvsName(orderId,productSku,AmazonTransactionType.ORDER.getValue());

		BigInteger lineItemId = this.dao.queryShipmentLineItemId(unsNameOrdr,ivsNameOrdr,productSku);

		//todo arthur 20211001
		//todo arthur we have to separate return (inventory) and settlement
  		this.dao.incrementReturnQuantityByLineItemId(lineItemId,quantity);
		return null;
	}
}
