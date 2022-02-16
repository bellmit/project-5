package com.kindminds.drs.data.pipelines.core.inventory.dal

import java.sql.Timestamp
import java.util
import java.util.Date

import com.kindminds.drs.data.pipelines.core.inventory.{ManageFbaInventoryReportItem, MarketplaceIdSku}
import com.kindminds.drs.data.pipelines.core.util.jooq.DrsDriver
import org.jooq.impl.DSL
import org.jooq.{DSLContext, Record, Result, SQLDialect}


class  ManageFbaInventoryReportDao {

  private val dsl: DSLContext = DSL.using(
    DrsDriver.getConnection(),
    SQLDialect.POSTGRES
  )


  def queryDrsSkuByMarketplaceSku(): java.util.Map[MarketplaceIdSku, String] = {

    val sql = "SELECT marketplace_id, marketplace_sku, code_by_drs FROM ( \n" +
      "\tSELECT product_id, marketplace_sku, marketplace_id FROM product_k101_marketplace_info \n" +
      "\tUNION \n" +
      "\tSELECT product_id, marketplace_sku, marketplace_id FROM product_marketplace_info \n" +
      " ) products \n" +
      "inner join product_sku ps on ps.product_id = products.product_id \n" +
      "order by code_by_drs, marketplace_id "


    val resultList: Result[Record] = dsl.fetch(sql )

    if (resultList.isEmpty) return null

    val marketplaceIdSkuMap = new java.util.HashMap[MarketplaceIdSku, String]()
    resultList.forEach(r => marketplaceIdSkuMap
      .put(new MarketplaceIdSku(r.getValue(0).toString.toInt, r.getValue(1).toString), r.getValue(2).toString))

    marketplaceIdSkuMap

  }


  def queryDataExists(snapshotDate: Date, countryCode: String): Boolean = {

    val sql = "SELECT EXISTS(SELECT 1 FROM inventory.amazon_manage_fba_inventory_report \n" +
      "       WHERE snapshot_date = ? " +
      "       AND inventory_country_code = ? ) "

    val resultList: Result[Record] = dsl.fetch(sql , new Timestamp(snapshotDate.getTime), countryCode)

    resultList.get(0).getValue(0).toString.toBoolean
false
  }

  def insertManageFbaInventoryItem(item: ManageFbaInventoryReportItem): Integer = {
    val sql = "INSERT INTO inventory.amazon_manage_fba_inventory_report(\n" +
      "\tsnapshot_date, inventory_country_code, marketplace_sku, fnsku, " +
      " asin, amazon_product_name, condition, your_price, mfn_listing_exists, " +
      " mfn_fulfillable_qty, afn_listing_exists, afn_warehouse_qty, " +
      " afn_fulfillable_qty, afn_unsellable_qty, afn_reserved_qty, " +
      " afn_total_qty, per_unit_volume, afn_inbound_working_qty, " +
      " afn_inbound_shipped_qty, afn_inbound_receiving_qty, " +
      " afn_researching_qty, afn_reserved_future_supply, afn_future_supply_buyable, " +
      " region, code_by_drs, date_created, date_updated)\n" +
      "\tVALUES " +
      "(?, ?, ?, ?, " +
      " ?, ?, ?, ?, ?, " +
      " ?, ?, ?, " +
      " ?, ?, ?, " +
      " ?, ?, ?, " +
      " ?, ?, " +
      " ?, ?, ?, " +
      " ?, ?, now(), now());"

    var insertCount = 0

    DSL.using(dsl.configuration()).transaction(c =>{

      insertCount =  c.dsl().query(sql ,
        new java.sql.Timestamp(item.getSnapshotDate.getTime), item.getCountryCode, item.getSku, item.getFnsku,
        item.getAsin, item.getProductName, item.getCondition, item.getYourPrice, item.getMfnListingExists,
        item.getMfnFulfillableQty, item.getAfnListingExists, item.getAfnWarehouseQty,
        item.getAfnFulfillableQty, item.getAfnUnsellableQty, item.getAfnReservedQty,
        item.getAfnTotalQty, item.getPerUnitVolume, item.getAfnInboundWorkingQty,
        item.getAfnInboundShippedQty, item.getAfnInboundReceivingQty,
        item.getAfnResearchingQty, item.getAfnReservedFutureSupply, item.getAfnFutureSupplyBuyable,
        item.getRegion, item.getCodeByDrs
      ).execute()

    })

    insertCount
    0
  }



}



