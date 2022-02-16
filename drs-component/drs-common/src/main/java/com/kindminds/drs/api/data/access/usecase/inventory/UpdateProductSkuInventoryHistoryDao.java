package com.kindminds.drs.api.data.access.usecase.inventory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.product.ProductSkuInventoryHistory;

public interface UpdateProductSkuInventoryHistoryDao {
	List<Integer> queryMarketplaceIds(int warehouseId);
	List<Integer> queryProductSkuIds(List<Integer> marketplaceIds);
	Map<Integer,Integer> queryFbaQtyInStockPlusFbaQtyTransfer(Marketplace representMarketplace);
	Date queryLatestStatementPeriodEnd(String string);
	Map<Integer,Integer> queryAmazonQtyOrdered(Marketplace marketplace,Date from,Date to);
	Map<Integer,Integer> queryShopifyQtyOrdered(Marketplace marketplace,Date from,Date to);
	void insertHistoryList(List<ProductSkuInventoryHistory> histories);
}
