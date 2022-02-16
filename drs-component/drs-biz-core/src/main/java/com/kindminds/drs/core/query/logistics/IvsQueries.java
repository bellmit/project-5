package com.kindminds.drs.core.query.logistics;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsSearchCondition;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.persist.v1.model.mapping.sales.PurchaseOrderSkuInfoImpl;

import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderInfo;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderSkuInfo;
import com.kindminds.drs.persist.v1.model.mapping.logistics.ShipmentIvsImpl;
import com.kindminds.drs.persist.v1.model.mapping.logistics.ShipmentIvsLineItemImpl;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductSkuStockImpl;
import com.kindminds.drs.persist.v1.model.mapping.sales.PurchaseOrderInfoImpl;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;




import java.math.BigDecimal;
import java.util.*;


@Component
public class IvsQueries extends Dao {



    public int queryCounts(String userCompanyKcode, IvsSearchCondition condition) {
        StringBuilder sqlSb = new StringBuilder()
                .append("select count(1) from shipment shp ")
                .append("inner join company selr on selr.id = shp.seller_company_id ")
                .append("inner join company buyr on buyr.id = shp.buyer_company_id ")
                .append("inner join country on country.id = shp.destination_country_id ")
                .append("inner join shipping_method sm on sm.id = shp.shipping_method_id ")
                .append("where true ")
                .append("and ( selr.k_code = :userCompanyKcode or buyr.k_code = :userCompanyKcode ) ")
                .append("and shp.type = :inventoryShipmentTypeName ")
                .append(this.composeSqlConditionString(condition));
     MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("userCompanyKcode", userCompanyKcode);
        q.addValue("inventoryShipmentTypeName", ShipmentType.INVENTORY.getValue());
        this.setQueryParameters(q,condition);
        @SuppressWarnings("rawtypes")
        List<Integer> rList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,Integer.class);
        return rList.get(0);
    }


    public List<ShipmentIvs> queryList(String userCompanyKcode, int startIndex, int size, IvsSearchCondition condition) {
        StringBuilder sqlSb = new StringBuilder()
                .append("select                         shp.id as id, ")
                .append("                             shp.name as name, ")
                .append("                                 NULL as type, ")
                .append("             sii.expected_export_date as expected_export_date, ")
                .append("         sii.shipment_fca_location_id as shipment_fca_location_id, ")
                .append("   to_char(sii.fca_delivery_date,:fm) as fca_delivery_date, ")
                .append("                   shp.invoice_number as invoice_number, ")
                .append("                          selr.k_code as seller_company_kcode, ")
                .append("                          buyr.k_code as buyer_company_kcode, ")
                .append("                        currency.name as currency_name, ")
                .append("                                 NULL as subtotal, ")
                .append("                                 NULL as sales_tax_rate, ")
                .append("                                 NULL as amount_tax, ")
                .append("                     shp.amount_total as amount_total, ")
                .append("                         country.code as destination_country_code, ")
                .append("                              sm.name as shipping_method, ")
                .append("                                 NULL as date_created, ")
                .append("                                 NULL as date_purchased, ")
                .append("                                 NULL as internal_note, ")
                .append("                                 NULL as special_request, ")
                .append("                                 NULL as num_of_repackaging, ")
                .append("                                 NULL as repackaging_fee, ")
                .append("                                 NULL as required_po, ")
                .append("                                 NULL as po_number, ")
                .append("                           shp.status as status ")
                .append("from shipment shp ")
                .append("left join shipment_info_ivs sii on sii.shipment_id = shp.id ")
                .append("inner join company selr on selr.id = shp.seller_company_id ")
                .append("inner join company buyr on buyr.id = shp.buyer_company_id ")
                .append("inner join currency on currency.id = shp.currency_id ")
                .append("inner join country on country.id = shp.destination_country_id ")
                .append("inner join shipping_method sm on sm.id = shp.shipping_method_id ")
                .append("where true ")
                .append("and ( selr.k_code = :userCompanyKcode or buyr.k_code = :userCompanyKcode ) ")
                .append("and shp.type = :inventoryShipmentTypeName ")
                .append(this.composeSqlConditionString(condition))
                .append(this.composeSqlForOrderBy())
                .append("limit :size offset :start ");
       MapSqlParameterSource q = new MapSqlParameterSource();
        this.setQueryParameters(q, condition);
        q.addValue("fm", "YYYY-MM-DD");
        q.addValue("userCompanyKcode", userCompanyKcode);
        q.addValue("inventoryShipmentTypeName", ShipmentType.INVENTORY.getValue());
        q.addValue("size", size);
        q.addValue("start", startIndex-1);


        List<ShipmentIvsImpl> resultList =  getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,(rs , rowNum) -> new ShipmentIvsImpl(
                rs.getInt("id") , rs.getString("name"),rs.getString("type"),rs.getString("expected_export_date"),
                rs.getString("shipment_fca_location_id"),rs.getString("fca_delivery_date"),
                rs.getString("invoice_number"),rs.getString("seller_company_kcode"),
                rs.getString("buyer_company_kcode")
                ,rs.getString("currency_name") ,rs.getBigDecimal("sales_tax_rate") ,
                rs.getBigDecimal("subtotal"),rs.getBigDecimal("amount_tax"), rs.getBigDecimal("amount_total") ,
                rs.getString("destination_country_code"),
                rs.getString("shipping_method"),
                rs.getString("date_created"),rs.getString("status"),
                rs.getString("date_purchased"),
                rs.getString("internal_note"),
                rs.getString("special_request"),rs.getInt("num_of_repackaging"),rs.getBigDecimal("repackaging_fee"),
                rs.getBoolean("required_po"),rs.getString("po_number")
        ));

        return (List) resultList;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> queryActiveAndOnboardingSkuCodeToSupplierNameMap(String kcode) {
        StringBuilder sqlSb = new StringBuilder()
                .append("select ps.code_by_drs, p.name_by_supplier ")
                .append("from product_sku ps ")
                .append("inner join product_base pb on pb.id = ps.product_base_id ")
                .append("inner join product_sku_status pss on pss.id = ps.status_id ")
                .append("inner join product p on p.id = ps.product_id ")
                .append("inner join company splr on splr.id = pb.supplier_company_id ")
                .append("where splr.k_code = :kcode and splr.is_supplier is TRUE ")
                .append("and pss.name in( :activeStatusName, :onboardingStatusName )");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("kcode", kcode);
        q.addValue("activeStatusName", SKU.Status.SKU_ACTIVE.name());
        q.addValue("onboardingStatusName", SKU.Status.SKU_PREPARING_LAUNCH.name());
        Map<String,String> activeSkuCodeToNameMap = new TreeMap<>();
        List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
        for(Object[] items:result){
            activeSkuCodeToNameMap.put((String)items[0], (String)items[1]);
        }
        return activeSkuCodeToNameMap;
    }

    public List<String>getApprovedCountryList(String kcode) {
        String marketplaceTable = "product_marketplace_info";
        if (kcode.equals("K101")) marketplaceTable="product_k101_marketplace_info";
        StringBuilder sqlSb = new StringBuilder()
                .append("select distinct c.code ")
                .append("from "+marketplaceTable+" pmi ")
                .append("inner join product p on p.id = pmi.product_id ")
                .append("inner join product_sku ps on ps.product_id = p.id ")
                .append("inner join product_base pb on pb.id = ps.product_base_id ")
                .append("inner join company splr on splr.id = pb.supplier_company_id ")
                .append("inner join marketplace m on m.id = pmi.marketplace_id ")
                .append("inner join country c on c.id = m.country_id ")
                .append("where splr.k_code = :kcode and splr.is_supplier is TRUE ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("kcode", kcode);
        List<String> getCountryList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);

        return getCountryList;
    }

    public List<String> getApprovedSkuByCountryList(String kcode, int marketplaceId) {
        String marketplaceTable = "product_marketplace_info";
        if (kcode.equals("K101")) marketplaceTable="product_k101_marketplace_info";
        StringBuilder sqlSb = new StringBuilder()
                .append("select ps.code_by_drs ")
                .append("from "+marketplaceTable+"  pmi ")
                .append("inner join product p on p.id = pmi.product_id ")
                .append("inner join product_sku ps on ps.product_id = p.id ")
                .append("inner join product_base pb on pb.id = ps.product_base_id ")
                .append("inner join company splr on splr.id = pb.supplier_company_id ")
                .append("where splr.k_code = :kcode and splr.is_supplier is TRUE ")
                .append("and pmi.marketplace_id = :marketplace_id ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("kcode", kcode);
        q.addValue("marketplace_id", marketplaceId);
        List<String> list = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
        List<String> getSkuList = new ArrayList<String>();
        for(int i = 0; i <list.size(); i++) {
            if (!getSkuList.contains(list.get(i))) {
                getSkuList.add(list.get(i));
            }
        }
        return getSkuList;
    }

    public List<String> getApprovedSkuByEUList(String kcode) {
        String marketplaceTable = "product_marketplace_info";
        if (kcode.equals("K101")) marketplaceTable="product_k101_marketplace_info";
        StringBuilder sqlSb = new StringBuilder()
                .append("select distinct ps.code_by_drs ")
                .append("from "+marketplaceTable+" pmi ")
                .append("inner join product p on p.id = pmi.product_id ")
                .append("inner join product_sku ps on ps.product_id = p.id ")
                .append("inner join product_base pb on pb.id = ps.product_base_id ")
                .append("inner join company splr on splr.id = pb.supplier_company_id ")
                .append("where splr.k_code = :kcode and splr.is_supplier is TRUE ")
                .append("and pmi.marketplace_id in (6,7,8,9) ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("kcode", kcode);
        List<String> list = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
        List<String> getSkuList = new ArrayList<String>();
        for(int i = 0; i <list.size(); i++) {
            if (!getSkuList.contains(list.get(i))) {
                getSkuList.add(list.get(i));
            }
        }
        return getSkuList;
    }



    public String queryCountryOfOrigin(String skuCode) {

        String sql = " SELECT data->>'originalPlace' as country_of_origin " +
                "  FROM  product.product_view  pv " +
                "  inner join product_base pb on pb.code_by_drs = pv.product_base_code " +
                "  inner  join product_sku  ps on ps.product_base_id  = pb.id  " +
                "  where ps.code_by_drs = :skuCode and pv.market_side = 0  ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("skuCode",skuCode);

        List rList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
        if(rList.size()>0){
            return rList.get(0).toString() ;
        }

        return "";


    }

    @SuppressWarnings("unchecked")
    public BidiMap<Integer,String> queryAvailableFcaLocationIdToNameMap() {
        String sql = "SELECT sfl.id, sfl.name \n" +
                " FROM shipment_fca_location sfl  ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        BidiMap<Integer,String> fcaLocationIdToNameMap = new TreeBidiMap<Integer,String>();
        List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        for(Object[] items:result){
            fcaLocationIdToNameMap.put((Integer)items[0], (String)items[1]);
        }
        return fcaLocationIdToNameMap;
    }


    @SuppressWarnings("unchecked")
    public ShipmentIvs query(String name) {
        StringBuilder sqlSb = new StringBuilder()
                .append("select                         shp.id as id, ")
                .append("                             shp.name as name, ")
                .append("                             shp.type as type, ")
                .append("to_char(sii.expected_export_date,:fm) as expected_export_date, ")
                .append("         sii.shipment_fca_location_id as shipment_fca_location_id, ")
                .append("   to_char(sii.fca_delivery_date,:fm) as fca_delivery_date, ")
                .append("                   shp.invoice_number as invoice_number, ")
                .append("                          sndr.k_code as seller_company_kcode, ")
                .append("                          rcvr.k_code as buyer_company_kcode, ")
                .append("                             cur.name as currency_name, ")
                .append("                   shp.sales_tax_rate as sales_tax_rate, ")
                .append("                         shp.subtotal as subtotal, ")
                .append("                       shp.amount_tax as amount_tax, ")
                .append("                     shp.amount_total as amount_total, ")
                .append("                         country.code as destination_country_code, ")
                .append("                              sm.name as shipping_method, ")
                .append("    shp.date_created at time zone :tz as date_created, ")
                .append("    				sii.date_purchased as date_purchased, ")
                .append("    				 sii.internal_note as internal_note, ")
                .append("                  sii.special_request as special_request, ")
                .append("               sii.num_of_repackaging as num_of_repackaging, ")
                .append("                  sii.repackaging_fee as repackaging_fee, ")
                .append("                      sii.required_po as required_po, ")
                .append("                        sii.po_number as po_number, ")
                .append("                           shp.status as status ")
                .append("from shipment shp ")
                .append("left join shipment_info_ivs sii on sii.shipment_id = shp.id ")
                .append("inner join company sndr on sndr.id = shp.seller_company_id ")
                .append("inner join company rcvr on rcvr.id = shp.buyer_company_id ")
                .append("inner join currency cur on cur.id = shp.currency_id ")
                .append("inner join country on country.id = shp.destination_country_id ")
                .append("inner join shipping_method sm on sm.id = shp.shipping_method_id ");
        sqlSb.append("where shp.name = :name ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("name", name);
        q.addValue("fm", "YYYY-MM-DD");
        q.addValue("tz", "UTC");
        //this.entityManager.clear();

        List<ShipmentIvsImpl> resultList =  getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,(rs , rowNum) -> new ShipmentIvsImpl(
                rs.getInt("id") , rs.getString("name"),rs.getString("type"),rs.getString("expected_export_date"),
                rs.getString("shipment_fca_location_id"),rs.getString("fca_delivery_date"),
                rs.getString("invoice_number"),rs.getString("seller_company_kcode"),
                rs.getString("buyer_company_kcode")
                ,rs.getString("currency_name") ,rs.getBigDecimal("sales_tax_rate") ,
                rs.getBigDecimal("subtotal"),rs.getBigDecimal("amount_tax"), rs.getBigDecimal("amount_total") ,
                rs.getString("destination_country_code"),
                rs.getString("shipping_method"),
                rs.getString("date_created"),rs.getString("status"),
                rs.getString("date_purchased"),
                rs.getString("internal_note"),
                rs.getString("special_request"),rs.getInt("num_of_repackaging"),rs.getBigDecimal("repackaging_fee"),
                rs.getBoolean("required_po"),rs.getString("po_number")
        ));

        Assert.isTrue(resultList.size()<=1);
        if ((resultList == null) || (resultList.size() == 0)) return null;
        ShipmentIvsImpl shp = resultList.get(0);
        shp.setLineItems(this.queryShipmentOfInventoryLineItems(name));
        shp.setPaymentTotal(this.queryShipmentPaymentTotal(name));

        return shp;
    }

    @SuppressWarnings("unchecked")
    public BigDecimal queryShipmentPaymentTotal(String shipmentName) {
        String sql = "select sum(bsli.statement_amount) "
                + "from bill_statementlineitem bsli "
                + "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
                + "where bsli.reference = :shipmentName "
                + "and slt.name not in (:sellBackType) ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName",shipmentName);
        q.addValue("sellBackType", StatementLineType.getSellbackTypes());
        List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
        Assert.isTrue(resultList.size()==1,"No or more than one result found.");
        return resultList.get(0)==null?BigDecimal.ZERO:resultList.get(0);
    }

     @SuppressWarnings("unchecked")
    public String querySellerKcode(String shipmentName) {

        StringBuilder sqlSb = new StringBuilder()
                .append("select c.k_code from shipment shp  ")
                .append("inner join company c on c.id = shp.seller_company_id ")
                .append("where shp.name = :shipmentName ");
     MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);
        List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
        Assert.isTrue(resultList.size()==1);
        return resultList.get(0);
    }


    @SuppressWarnings("unchecked")
    public int queryDaysBeforeExportForFcaDelivery(Country targetCountry, ShippingMethod shippingMethod, int fcaLocationId) {

        StringBuilder sqlSb = new StringBuilder()
                .append("select days_before_export_for_fca_delivery ")
                .append("from shipment_fca_location_available_info sflai ")
                .append("inner join shipping_method sm on sm.id = sflai.shipping_method_id ")
                .append("where sflai.target_country_id = :targetCountryKey ")
                .append("and sm.name = :shippingMethodName ")
                .append("and sflai.shipment_fca_location_id = :fcaLocationId ");

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("targetCountryKey",targetCountry.getKey());
        q.addValue("shippingMethodName",shippingMethod.name());
        q.addValue("fcaLocationId",fcaLocationId);
        List<Short> resultList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,Short.class);

        Assert.isTrue(resultList.size()==1);
        Assert.notNull(resultList.get(0));

        return resultList.get(0).intValue();
    }


    @SuppressWarnings("unchecked")
    public ShipmentIvs.ShipmentIvsLineItem queryShipmentLineItem(String ivsName , Integer boxNum ,Integer mixedBoxLineSeq) {
        StringBuilder sqlSb = new StringBuilder()
                .append("select                shpli.id as id, ")
                .append("                shpli.line_seq as line_seq, ")
                .append("                 shpli.box_num as box_num, ")
                .append("      shpli.mixed_box_line_seq as mixed_box_line_seq, ")
                .append("                  src_shp.name as source_shipment_name, ")
                .append("                ps.code_by_drs as code_by_drs, ")
                .append("          pps.name_by_supplier as name_by_supplier, ")
                .append("               shpli.qty_order as quantity, ")
                .append("             shpli.unit_amount as unit_amount, ")
                .append("            shpli.ctn_dim_1_cm as ctn_dim_1_cm, ")
                .append("            shpli.ctn_dim_2_cm as ctn_dim_2_cm, ")
                .append("            shpli.ctn_dim_3_cm as ctn_dim_3_cm, ")
                .append(" shpli.gross_weight_per_ctn_kg as gross_weight_per_ctn_kg, ")
                .append("           shpli.units_per_ctn as units_per_ctn, ")
                .append("          shpli.numbers_of_ctn as numbers_of_ctn, ")
                .append("       shpli.require_packaging as require_packaging, ")
                .append("      shpli.gui_invoice_number as gui_invoice_number, ")
                .append("           shpli.gui_file_name as gui_file_name, ")
                .append("                shpli.gui_uuid as gui_uuid, ")
                .append("     shpli.carton_number_start as carton_number_start, ")
                .append("       shpli.carton_number_end as carton_number_end , shpli.line_status ")
                .append("from shipment_line_item shpli ")
                .append("inner join shipment shp on shp.id = shpli.shipment_id ")
                .append("left join shipment src_shp on src_shp.id = shpli.source_shipment_id ")
                .append("inner join product_sku ps on ps.id = shpli.sku_id ")
                .append("inner join product pps on pps.id = ps.product_id ")
                .append("where  shp.name = :shipmentName " +
                        " and shpli.box_num= :boxNum and  shpli.mixed_box_line_seq = :mLineSeq  ");


        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", ivsName);
        q.addValue("boxNum", boxNum);
        q.addValue("mLineSeq", mixedBoxLineSeq);

        List<ShipmentIvs.ShipmentIvsLineItem> resultList =
                getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,(rs, rowNum) -> new ShipmentIvsLineItemImpl(
                rs.getInt("id"),rs.getInt("line_seq"),rs.getInt("box_num"),rs.getInt("mixed_box_line_seq"),
                rs.getString("code_by_drs"),rs.getString("name_by_supplier"),
                rs.getBigDecimal("quantity"), rs.getBigDecimal("unit_amount"),
                rs.getBigDecimal("ctn_dim_1_cm"), rs.getBigDecimal("ctn_dim_2_cm"), rs.getBigDecimal("ctn_dim_3_cm"),
                rs.getBigDecimal("gross_weight_per_ctn_kg"), rs.getInt("units_per_ctn"),
                rs.getInt("numbers_of_ctn"), rs.getBoolean("require_packaging"),
                rs.getString("gui_invoice_number"), rs.getString("gui_file_name"),
                rs.getString("gui_uuid"), rs.getInt("carton_number_start"),
                rs.getInt("carton_number_end"), rs.getString("line_status")
        ));

        Assert.isTrue(resultList.size() == 1, "result list size must be 1, size: " + resultList.size());
        ShipmentIvs.ShipmentIvsLineItem result = resultList.get(0);
        Assert.notNull(result, "result must not be null");

        return result;
    }

    //======= private

    private List<ShipmentIvs.ShipmentIvsLineItem> queryShipmentOfInventoryLineItems(String shipmentName) {
        StringBuilder sqlSb = new StringBuilder()
                .append("select                shpli.id as id, ")
                .append("                shpli.line_seq as line_seq, ")
                .append("                 shpli.box_num as box_num, ")
                .append("      shpli.mixed_box_line_seq as mixed_box_line_seq, ")
                .append("                  src_shp.name as source_shipment_name, ")
                .append("                ps.code_by_drs as code_by_drs, ")
                .append("          pps.name_by_supplier as name_by_supplier, ")
                .append("               shpli.qty_order as quantity, ")
                .append("             shpli.unit_amount as unit_amount, ")
                .append("            shpli.ctn_dim_1_cm as ctn_dim_1_cm, ")
                .append("            shpli.ctn_dim_2_cm as ctn_dim_2_cm, ")
                .append("            shpli.ctn_dim_3_cm as ctn_dim_3_cm, ")
                .append(" shpli.gross_weight_per_ctn_kg as gross_weight_per_ctn_kg, ")
                .append("           shpli.units_per_ctn as units_per_ctn, ")
                .append("          shpli.numbers_of_ctn as numbers_of_ctn, ")
                .append("       shpli.require_packaging as require_packaging, ")
                .append("      shpli.gui_invoice_number as gui_invoice_number, ")
                .append("           shpli.gui_file_name as gui_file_name, ")
                .append("                shpli.gui_uuid as gui_uuid, ")
                .append("     shpli.carton_number_start as carton_number_start, ")
                .append("       shpli.carton_number_end as carton_number_end , shpli.line_status ")
                .append("from shipment_line_item shpli ")
                .append("inner join shipment shp on shp.id = shpli.shipment_id ")
                .append("left join shipment src_shp on src_shp.id = shpli.source_shipment_id ")
                .append("inner join product_sku ps on ps.id = shpli.sku_id ")
                .append("inner join product pps on pps.id = ps.product_id ")
                .append("where shp.name = :shipmentName ")
                .append("order by shpli.box_num, shpli.mixed_box_line_seq ");

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);
        return (List) getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,(rs, rowNum) -> new ShipmentIvsLineItemImpl(
                rs.getInt("id"),rs.getInt("line_seq"),rs.getInt("box_num"),rs.getInt("mixed_box_line_seq"),
                rs.getString("code_by_drs"),rs.getString("name_by_supplier"),
                rs.getBigDecimal("quantity"), rs.getBigDecimal("unit_amount"),
                rs.getBigDecimal("ctn_dim_1_cm"), rs.getBigDecimal("ctn_dim_2_cm"), rs.getBigDecimal("ctn_dim_3_cm"),
                rs.getBigDecimal("gross_weight_per_ctn_kg"), rs.getInt("units_per_ctn"),
                rs.getInt("numbers_of_ctn"), rs.getBoolean("require_packaging"),
                rs.getString("gui_invoice_number"), rs.getString("gui_file_name"),
                rs.getString("gui_uuid"), rs.getInt("carton_number_start"),
                rs.getInt("carton_number_end"), rs.getString("line_status")
        ));
    }


    private String composeSqlConditionString(IvsSearchCondition condition){
        StringBuilder sqlSb = new StringBuilder();
        if(condition.getSellerCompanyKcode()!=null){
            sqlSb.append("and selr.k_code = :sellerCompanyKcode ");
        }
        if(condition.getDestinationCountry()!=null){
            sqlSb.append("and country.code = :destinationCountry ");
        }
        if(condition.getShippingMethod()!=null){
            sqlSb.append("and sm.name = :shippingMethod ");
        }
        if(condition.getStatus()!=null){
            sqlSb.append("and shp.status = :status ");
        }
        return sqlSb.toString();
    }

    private void setQueryParameters(MapSqlParameterSource q,IvsSearchCondition condition){
        if(condition.getSellerCompanyKcode()!=null){
            q.addValue("sellerCompanyKcode",condition.getSellerCompanyKcode());
        }
        if(condition.getDestinationCountry()!=null){
            q.addValue("destinationCountry",condition.getDestinationCountry());
        }
        if(condition.getShippingMethod()!=null){
            q.addValue("shippingMethod",condition.getShippingMethod().name());
        }
        if(condition.getStatus()!=null){
            q.addValue("status",condition.getStatus().name());
        }
    }

    private String composeSqlForOrderBy(){
        return "order by case "
                + "    when shp.status = 'SHPT_DRAFT'         then 1 "
                + "    when shp.status = 'SHPT_EXCEPTION'     then 2 "
                + "    when shp.status = 'SHPT_AWAIT_PLAN'    then 3 "
                + "    when shp.status = 'SHPT_INITIAL_VERIFIED'    then 4 "
                + "    when shp.status = 'SHPT_FC_BOOKING_CONFIRM' then 5"
                + "    when shp.status = 'SHPT_PLANNING'      then 6 "
                + "    when shp.status = 'SHPT_CONFIRMED'     then 7 "
                + "    when shp.status = 'SHPT_AWAIT_PICK_UP' then 8 "
                + "    when shp.status = 'SHPT_IN_TRANSIT'    then 9 "
                + "    when shp.status = 'SHPT_RECEIVING'     then 10 "
                + "    when shp.status = 'SHPT_RECEIVED'      then 11 "
                + "    else 99 "
                + "end asc, "
                + "shp.id desc ";
    }


    public List<String> queryIvsNumbers(String userCompanyKcode, int startIndex, int size) {
       //todo arthur need paging?

        StringBuilder sqlSb = new StringBuilder()
                .append(" select  name " +
                        "  from shipment shp " +
                        "  left join shipment_info_ivs sii on sii.shipment_id = shp.id " +
                        "  inner join company selr on selr.id = shp.seller_company_id " +
                        "  inner join company buyr on buyr.id = shp.buyer_company_id " +
                        "  where  " +
                        "  selr.k_code = :kcode " +
                        "  and shp.type = 'Supplier Inventory' " +
                        "  order by serial_id  ");

              //  .append("limit :size offset :start ");

     MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("kcode", userCompanyKcode);
        //q.addValue("inventoryShipmentTypeName", ShipmentType.INVENTORY.getValue());
        //q.addValue("size", size);
        //q.addValue("start", startIndex-1);

        return getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
    }



    public List<String> queryIvsLineitemSku(String ivsNumber) {

        StringBuilder sqlSb = new StringBuilder()
                .append("  select   distinct    ps.code_by_drs "
                       + "   from shipment_line_item shpli "
                       + " inner join shipment shp on shp.id = shpli.shipment_id "
                       + " inner join product_sku ps on ps.id = shpli.sku_id "
                       + " inner join product pps on pps.id = ps.product_id "
                       + " where shp.name =  :ivsNum ");

     MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("ivsNum", ivsNumber);

        return getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);

    }

    public List<String> queryIvsLineitemBoxNum(String ivsNumber , String sku) {

        StringBuilder sqlSb = new StringBuilder()
                .append("  select  shpli.box_num as box_num ," +
                        "  shpli.mixed_box_line_seq as mixed_box_line_seq , " +
                        " shpli.carton_number_start , shpli.carton_number_end  "
                        + "  from shipment_line_item shpli "
                        + " inner join shipment shp on shp.id = shpli.shipment_id "
                        + " inner join product_sku ps on ps.id = shpli.sku_id "
                        + " inner join product pps on pps.id = ps.product_id "
                        + " where shp.name = :ivsNum  and ps.code_by_drs = :sku");

     MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("ivsNum", ivsNumber);
        q.addValue("sku", sku);

        List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
        List<String> boxNum = new ArrayList<>();
        for(Object[] items:result){
            boxNum.add( (Integer)items[2] + "-" + (Integer)items[3] +"/"
                    +(Integer)items[0] + "-" + (Integer)items[1]);
        }

        return boxNum;
    }


    @SuppressWarnings("unchecked") @Deprecated // TODO: to be remove
    public ProductSkuStock queryProductSkuStock(String shipmentName, String skuCodeByDrs) {
        String sql ="select  si.id as id, "
                +"          s.name as shipment_name, "
                +"  ps.code_by_drs as sku_code_by_drs, "
                +"     selr.k_code as seller_company_kcode, "
                +"     buyr.k_code as buyer_company_kcode, "
                +"   currency.name as currency_name, "
                +"  si.unit_amount as unit_cost, "
                +"    si.qty_order as qty_order, "
                +"     si.qty_sold as qty_sold, "
                +" si.qty_returned as qty_returned, "
                +"     source.name as parent_shipment_name "
                + "from shipment_line_item si "
                + "inner join product_sku ps on ps.id = si.sku_id "
                + "inner join shipment s on s.id = si.shipment_id "
                + "left  join shipment source on source.id = si.source_shipment_id "
                + "inner join company selr on selr.id = s.seller_company_id "
                + "inner join company buyr on buyr.id = s.buyer_company_id "
                + "inner join currency on currency.id = s.currency_id "
                + "where s.name = :shipmentName "
                + "and ps.code_by_drs = :skuCodeByDrs ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName",shipmentName);
        q.addValue("skuCodeByDrs",skuCodeByDrs);

        List<ProductSkuStockImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ProductSkuStockImpl(
                rs.getInt("id"),rs.getString("shipment_name"),rs.getString("sku_code_by_drs"),
                rs.getString("seller_company_kcode"), rs.getString("buyer_company_kcode"),
                rs.getString("currency_name"),rs.getBigDecimal("unit_cost"),
                rs.getInt("qty_order"),rs.getInt("qty_sold"),rs.getInt("qty_returned"),rs.getString("parent_shipment_name")
        ));



        Assert.isTrue(resultList.size()==1,"Incorrect Rows:"+resultList.size());
        return resultList.get(0);
    }


    public PurchaseOrderInfo queryPurchaseOrderInfo(String shipmentName) {
        String sql = "select sh.id, cpy.name_local, cpy.phone_number, " +
                " cpy.address, sii.po_number, sii.fca_delivery_date, " +
                " sfl.name as fca_shipment_location, sh.subtotal, " +
                " sh.amount_tax, sh.amount_total, cur.name as currency, sh.sales_tax_rate" +
                " from company cpy " +
                " inner join shipment sh on sh.seller_company_id = cpy.id " +
                " inner join shipment_info_ivs sii on sii.shipment_id = sh.id " +
                " inner join shipment_fca_location sfl on sfl.id = sii.shipment_fca_location_id " +
                " inner join currency cur on cur.id = sh.currency_id " +
                " where sh.name = :shipmentName ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);


        return getNamedParameterJdbcTemplate().queryForObject(sql,q , (rs, rowNum) -> new PurchaseOrderInfoImpl(
                rs.getInt("id"),rs.getString("name_local"),rs.getString("phone_number"),
                rs.getString("address"),rs.getString("po_number"),
                rs.getDate("fca_delivery_date"),rs.getString("fca_shipment_location"),
                rs.getBigDecimal("subtotal"),rs.getBigDecimal("amount_tax"),
                rs.getBigDecimal("amount_total"),rs.getString("currency"),rs.getBigDecimal("sales_tax_rate")
        ));
    }

    @SuppressWarnings("unchecked")
    public List<PurchaseOrderSkuInfo> queryPurchaseOrderInfoList(String shipmentName) {
        String sql = "select sh.id, ps.code_by_drs, " +
                " pd.name_by_supplier, sli.unit_amount, " +
                " sum(sli.qty_order) as qty_order, " +
                " (sli.unit_amount * sum(sli.qty_order)) as subtotal " +
                " from shipment sh " +
                " inner join shipment_line_item sli on sli.shipment_id = sh.id " +
                " inner join product_sku ps on ps.id = sli.sku_id " +
                " inner join product pd on pd.id = ps.product_id " +
                " where sh.name = :shipmentName " +
                " group by sh.id, ps.code_by_drs, " +
                " pd.name_by_supplier, sli.unit_amount " +
                " order by ps.code_by_drs";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);

        return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs,rowNum) -> new PurchaseOrderSkuInfoImpl(
                rs.getInt("id"),rs.getString("code_by_drs"),rs.getString("name_by_supplier"),
                rs.getBigDecimal("unit_amount"),rs.getInt("qty_order"),	rs.getBigDecimal("subtotal")
        ));
    }





}
