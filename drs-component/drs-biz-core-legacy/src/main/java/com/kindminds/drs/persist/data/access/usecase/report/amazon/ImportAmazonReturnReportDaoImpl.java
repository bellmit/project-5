package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonReturnReportDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonReturnReportRawLine;

@Repository
public class ImportAmazonReturnReportDaoImpl extends Dao implements ImportAmazonReturnReportDao {



	@Override @SuppressWarnings("unchecked")
	public Map<String,Date> queryFulfillmentCenterIdToLatestReturnEventDateMap() {
		String sql = "select fulfillment_center_id, max(return_date) from amazon_return_report group by fulfillment_center_id ";

		List<Object[]> columnsList = getJdbcTemplate().query(sql,objArrayMapper);
		if(columnsList.isEmpty()) return null;
		Map<String,Date> fulfillmentCenterIdToLastestEventDateMap = new HashMap<>();
		for(Object[] columns:columnsList){
			String fulfillmentCenterId = (String)columns[0];
			Date latestReturnDate = (Date)columns[1];
			fulfillmentCenterIdToLastestEventDateMap.put(fulfillmentCenterId,latestReturnDate);
		}
		return fulfillmentCenterIdToLastestEventDateMap;
	}

	@Override @Transactional("transactionManager")
	public int insertLineTimes(List<AmazonReturnReportRawLine> rawLineList) {
		String sql = "insert into amazon_return_report "
				+ "( return_date, order_id,  sku,  asin,  fnsku, product_name,  quantity, fulfillment_center_id, detailed_disposition,  reason,  status,  license_plate_number,  customer_comments ) values "
				+ "( :returnDate, :orderId, :sku, :asin, :fnsku, :productName, :quantity,  :fulfillmentCenterId, :detailedDisposition, :reason, :status, :license_plate_number, :customer_comments ) ";
		int insertedRows = 0;
		for(AmazonReturnReportRawLine line:rawLineList){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("returnDate", line.getReturnDate());
			q.addValue("orderId", line.getOrderId());
			q.addValue("sku", line.getSku());
			q.addValue("asin", line.getAsin());
			q.addValue("fnsku", line.getFnsku());
			q.addValue("productName", line.getProductName());
			q.addValue("quantity",  line.getQuantity());
			q.addValue("fulfillmentCenterId", line.getFulfillmentCenterId());
			q.addValue("detailedDisposition", line.getDetailedDisposition());
			q.addValue("reason", line.getReason());
			q.addValue("status",line.getStatus());
			q.addValue("license_plate_number",line.getLicensePlateNumber());
			q.addValue("customer_comments",line.getCustomerComments());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			insertedRows++;
		}
		return insertedRows;
	}
}
