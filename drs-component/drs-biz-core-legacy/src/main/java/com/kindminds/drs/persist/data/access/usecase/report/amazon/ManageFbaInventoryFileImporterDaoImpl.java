package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import com.kindminds.drs.api.v1.model.report.ManageFbaInventoryReportItem;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import com.kindminds.drs.api.data.access.usecase.report.amazon.ManageFbaInventoryFileImporterDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;




import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public class ManageFbaInventoryFileImporterDaoImpl extends Dao implements ManageFbaInventoryFileImporterDao {

   

    @Override @SuppressWarnings("unchecked")
    public String queryDrsSkuByMarketplaceSku(String marketplaceSku, Integer marketplaceId) {
        String sql = "SELECT code_by_drs FROM product_sku " +
                " WHERE product_id = (SELECT product_id FROM (\n " +
                "    SELECT product_id, marketplace_sku, marketplace_id FROM product_k101_marketplace_info " +
                "    UNION " +
                "    SELECT product_id, marketplace_sku, marketplace_id FROM product_marketplace_info " +
                "  ) products\n" +
                "  WHERE marketplace_sku = :marketplaceSku " +
                "  AND marketplace_id = :marketplaceId )";

      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplaceSku", marketplaceSku);
        q.addValue("marketplaceId", marketplaceId);

        List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);

    }

    @Override
    public Boolean queryDataExists(Date snapshotDate, String countryCode, String fnsku) {

        String sql = "SELECT EXISTS(SELECT 1 FROM inventory.amazon_manage_fba_inventory_report \n" +
                "       WHERE snapshot_date = :snapshotDate " +
                "       AND inventory_country_code = :countryCode " +
                "       AND fnsku = :fnsku) ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("snapshotDate", snapshotDate);
        q.addValue("countryCode", countryCode);
        q.addValue("fnsku", fnsku);

        return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
    }

    @Override @Transactional
    public Integer insertManageFbaInventoryItem(ManageFbaInventoryReportItem item) {

        String sql = "INSERT INTO inventory.amazon_manage_fba_inventory_report(\n" +
                "\tid, snapshot_date, inventory_country_code, marketplace_sku, fnsku, " +
                " asin, amazon_product_name, condition, your_price, mfn_listing_exists, " +
                " mfn_fulfillable_qty, afn_listing_exists, afn_warehouse_qty, " +
                " afn_fulfillable_qty, afn_unsellable_qty, afn_reserved_qty, " +
                " afn_total_qty, per_unit_volume, afn_inbound_working_qty, " +
                " afn_inbound_shipped_qty, afn_inbound_receiving_qty, " +
                " afn_researching_qty, afn_reserved_future_supply, afn_future_supply_buyable, " +
                " region, code_by_drs, date_created, date_updated)\n" +
                "\tVALUES " +
                "(:id, :snapshotDate, :countryCode, :sku, :fnsku, " +
                " :asin, :productName, :condition, :yourPrice, :mfnListingExists, " +
                " :mfnFulfillableQty, :afnListingExist, :afnWarehouseQty, " +
                " :afnFulfillableQty, :afnUnsellableQty, :afnReservedQty, " +
                " :afnTotalQty, :perUnitVolume, :afnInboundWorkingQty, " +
                " :afnInboundShippedQty, :afnInboundReceivingQty, " +
                " :afnResearchingQty, :afnReservedFutureSupply, :afnFutureSupplyBuyable, " +
                " :region, :codeByDrs, now(), now());";
        MapSqlParameterSource q = new MapSqlParameterSource();

        //Lets Hibernate know what type to expect to prevent exception when inserting null
        initializeQueryParameterTypes(q);

        //actually set the real parameter values
        q.addValue("id", item.getId());
        q.addValue("snapshotDate", item.getSnapshotDate());
        q.addValue("countryCode", item.getCountryCode());
        q.addValue("sku", item.getSku());
        q.addValue("fnsku", item.getFnsku());
        q.addValue("asin", item.getAsin());
        q.addValue("productName", item.getProductName());
        q.addValue("condition", item.getCondition());
        q.addValue("yourPrice", item.getYourPrice());
        q.addValue("mfnListingExists", item.getMfnListingExists());
        q.addValue("mfnFulfillableQty", item.getMfnFulfillableQty());
        q.addValue("afnListingExist", item.getAfnListingExists());
        q.addValue("afnWarehouseQty", item.getAfnWarehouseQty());
        q.addValue("afnFulfillableQty", item.getAfnFulfillableQty());
        q.addValue("afnUnsellableQty", item.getAfnUnsellableQty());
        q.addValue("afnReservedQty", item.getAfnReservedQty());
        q.addValue("afnTotalQty", item.getAfnTotalQty());
        q.addValue("perUnitVolume", item.getPerUnitVolume());
        q.addValue("afnInboundWorkingQty", item.getAfnInboundWorkingQty());
        q.addValue("afnInboundShippedQty", item.getAfnInboundShippedQty());
        q.addValue("afnInboundReceivingQty", item.getAfnInboundReceivingQty());

        q.addValue("afnResearchingQty", item.getAfnResearchingQty());
        q.addValue("afnReservedFutureSupply", item.getAfnReservedFutureSupply());
        q.addValue("afnFutureSupplyBuyable", item.getAfnFutureSupplyBuyable());
        q.addValue("region", item.getRegion());
        q.addValue("region", item.getRegion());
        q.addValue("codeByDrs", item.getCodeByDrs());

        return getNamedParameterJdbcTemplate().update(sql,q);
    }

    private void initializeQueryParameterTypes(MapSqlParameterSource q) {
        q.addValue("yourPrice", BigDecimal.ZERO);
        q.addValue("mfnFulfillableQty", 0);
        q.addValue("afnWarehouseQty", 0);
        q.addValue("afnFulfillableQty", 0);
        q.addValue("afnUnsellableQty", 0);
        q.addValue("afnReservedQty", 0);
        q.addValue("afnTotalQty", 0);
        q.addValue("perUnitVolume", BigDecimal.ZERO);
        q.addValue("afnInboundWorkingQty", 0);
        q.addValue("afnInboundShippedQty", 0);
        q.addValue("afnInboundReceivingQty", 0);
        q.addValue("afnResearchingQty", 0);
        q.addValue("afnReservedFutureSupply", 0);
        q.addValue("afnFutureSupplyBuyable", 0);
    }

}
