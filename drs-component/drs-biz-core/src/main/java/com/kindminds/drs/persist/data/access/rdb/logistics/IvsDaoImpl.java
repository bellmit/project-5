package com.kindminds.drs.persist.data.access.rdb.logistics;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.data.row.logistics.*;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.persist.data.access.rdb.Dao;



import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.data.access.rdb.logistics.IvsDao;

import com.kindminds.drs.util.DateHelper;

import org.apache.commons.lang.StringUtils;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;


@Repository
public class IvsDaoImpl extends Dao implements IvsDao {

  
    private String  dateFormat = "yyyy-MM-dd";



    @Transactional("transactionManager")
    public String insert(String supplierKcode   , Ivs shipment  , int serialId  ,
                         String draftName   , BigDecimal salesTaxRate   , BigDecimal subtotal   ,
                         BigDecimal tax   , BigDecimal total   , Currency handlerCurrency   ,
                         ShipmentStatus status   ) {

        int nextId = this.getNextVal("shipment", "id");

        Timestamp dateC = Timestamp.from(shipment.getDateCreated().toInstant());

        StringBuilder sqlSb = new StringBuilder()
                .append("INSERT INTO shipment ")
                .append("( id, serial_id,  name,  type, invoice_number, seller_company_id,        buyer_company_id,      currency_id, sales_tax_rate,  subtotal, amount_tax, amount_total, destination_country_id, shipping_method_id, date_created,  status ) ")
                .append("select ")
                .append(" :id, :serialId, :name, :type, :invoiceNumber,           " +
                        "splr.id, splr.handler_company_id, splr.currency_id,  :salesTaxRate," +
                        " :subtotal, :amountTax, :amountTotal, ct.id, sm.id," +
                        " :dateCreated , :status ")
                .append("from company splr, country ct, shipping_method sm ")
                .append("where splr.k_code = :supplierKcode ")
                .append("and ct.code = :countryCode ")
                .append("and sm.name = :shippingMethod ");

        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", nextId);
        q.addValue("serialId", serialId);
        q.addValue("name", draftName);
        q.addValue("type", shipment.getType().getValue());
        q.addValue("invoiceNumber", shipment.getInvoiceNumber());
        q.addValue("salesTaxRate", salesTaxRate);
        q.addValue("subtotal", subtotal);
        q.addValue("amountTax", tax);
        q.addValue("amountTotal", total);
        q.addValue("shippingMethod", shipment.getShippingMethod().toString());
        q.addValue("status", status.toString());
        q.addValue("dateCreated", dateC);
        q.addValue("countryCode", shipment.getDestinationCountry());
        q.addValue("supplierKcode", supplierKcode);

        int insertedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);

        Assert.isTrue(insertedRows == 1, "No or more than one shipment inserted.");

        this.insertInfo(draftName, shipment);

        this.insertLineItems(draftName, shipment);

        return draftName;
    }


    private void insertLineItems(String shipmentName , Ivs shipment ) {

        StringBuilder sqlSb = new StringBuilder()
                .append("insert into shipment_line_item ")
                .append("( shipment_id, line_seq, box_num, mixed_box_line_seq, sku_id, " +
                        " qty_order,  unit_amount, ctn_dim_1_cm, ctn_dim_2_cm, ctn_dim_3_cm, " +
                        "gross_weight_per_ctn_kg, units_per_ctn, numbers_of_ctn, require_packaging," +
                        " gui_invoice_number, gui_file_name, gui_uuid , carton_number_start, carton_number_end , " +
                        " line_status, retail_po_number  ) ")
                .append("select ")
                .append("   shp.id, :lineSeq, :box_num, :mixed_box_line_seq, ps.id, " +
                        ":qty_order, :unit_amount,     :ctnDim1,   " +
                        "  :ctnDim2,     :ctnDim3,           :perCtnWeight, " +
                        "  :perCtnUnit,  :ctnCounts, :require_packaging, :gui_invoice_number, " +
                        " :gui_file_name, :gui_uuid,  :carton_number_start, :carton_number_end , :line_status, :retail_po_number ")
                .append("from product_sku ps ")
                .append("inner join shipment shp on shp.name = :shipmentName ")
                .append("where ps.code_by_drs = :codeByDrs ");

        List<IvsLineItem> items = shipment.getLineItems();
        if(items != null){
            for (IvsLineItem lineItem : items) {

                if(lineItem.getBoxNum() != 0 && !lineItem.getSkuCode().isEmpty()) {

                        MapSqlParameterSource q = new MapSqlParameterSource();

                        q.addValue("lineSeq", lineItem.getLineSeq());

                        q.addValue("box_num", lineItem.getBoxNum());

                        q.addValue("mixed_box_line_seq", lineItem.getMixedBoxLineSeq());

                        q.addValue("qty_order",
                              new Integer((StringUtils.isNotEmpty(lineItem.getQuantity()))?
                                    lineItem.getQuantity() : "0"));


                        q.addValue("unit_amount",
                                new BigDecimal((StringUtils.isNotEmpty(lineItem.getUnitAmount())) ?
                                        lineItem.getUnitAmount() : "0"));


                        q.addValue("ctnDim1",
                                (StringUtils.isEmpty(lineItem.getCartonDimensionCm1()))?
                            BigDecimal.ZERO : new BigDecimal(lineItem.getCartonDimensionCm1()));


                        q.addValue("ctnDim2", (StringUtils.isEmpty(lineItem.getCartonDimensionCm2()))
                           ?  BigDecimal.ZERO : new BigDecimal(lineItem.getCartonDimensionCm2()));


                        q.addValue("ctnDim3",
                                (StringUtils.isEmpty(lineItem.getCartonDimensionCm3())) ?
                            BigDecimal.ZERO : new BigDecimal(lineItem.getCartonDimensionCm3()));


                        q.addValue("perCtnWeight",
                                (StringUtils.isEmpty(lineItem.getPerCartonGrossWeightKg()))
                            ? BigDecimal.ZERO : new BigDecimal(lineItem.getPerCartonGrossWeightKg()));

                        q.addValue("perCtnUnit", (StringUtils.isEmpty(lineItem.getPerCartonUnits()))
                            ? BigDecimal.ZERO  : new BigDecimal(lineItem.getPerCartonUnits()));


                        q.addValue("ctnCounts", (StringUtils.isEmpty(lineItem.getCartonCounts() ))
                          ?  BigDecimal.ZERO : new BigDecimal(lineItem.getCartonCounts()));


                        q.addValue("shipmentName", shipmentName);
                        q.addValue("codeByDrs", lineItem.getSkuCode());


                        q.addValue("require_packaging", lineItem.getRequireRepackaging());

                        q.addValue("gui_invoice_number", lineItem.getGuiInvoiceNumber());
                        q.addValue("gui_file_name", lineItem.getGuiFileName());
                        q.addValue("gui_uuid",lineItem.getGuiuuid());

                        q.addValue("carton_number_start", lineItem.getCartonNumberStart());
                        q.addValue("carton_number_end", lineItem.getCartonNumberEnd());

                        q.addValue("line_status" , lineItem.getProductVerificationStatus().getValue());

                        q.addValue("retail_po_number" , lineItem.getRetailPONumber());

                        int insertedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);

                        Assert.isTrue(insertedRows == 1, "No or more than one shipmentLineItem inserted  $insertedRows");



                }

            }
        }

    }


    private void insertInfo(String shipmentName   , Ivs shipment   ) {

        StringBuilder sqlSb = new StringBuilder()
                .append("insert into shipment_info_ivs ")
                .append("( shipment_id, fca_delivery_date, shipment_fca_location_id," +
                        " expected_export_date, special_request, num_of_repackaging, " +
                        "repackaging_fee, required_po, po_number ) select ")
                .append("       shp.id,  :fcaDeliveryDate,   :shipmentFcaLocationId,  " +
                        ":expectedExportDate, :special_request, :num_of_repackaging," +
                        " :repackaging_fee, :required_po, :po_number from shipment shp " +
                        " where shp.name = :shipmentName");

        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("shipmentName", shipmentName);
        q.addValue("expectedExportDate", DateHelper.toDate(shipment.getExpectedExportDate(), this.dateFormat));
        q.addValue("shipmentFcaLocationId",
                java.lang.Short.parseShort(shipment.getFcaDeliveryLocationId()));
        q.addValue("fcaDeliveryDate", DateHelper.toDate(shipment.getFcaDeliveryDate(), this.dateFormat));
        q.addValue("special_request", shipment.getSpecialRequest());
        String  numBoxes = shipment.getBoxesNeedRepackaging();
        q.addValue("num_of_repackaging",  (numBoxes == null) ? 0 : Integer.valueOf(numBoxes));
        q.addValue("repackaging_fee", (shipment.getRepackagingFee() == null) ?
            BigDecimal.ZERO: new BigDecimal(shipment.getRepackagingFee()));

        q.addValue("required_po",  (shipment.getRequiredPO() == null)? false: shipment.getRequiredPO());
        q.addValue("po_number", shipment.getPoNumber());
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sqlSb.toString(),q) == 1, "No or more than one shipment inserted.");
    }

    public  int queryMaxSerialIdOfDraftShipments(String supplierKcode  )   {
        StringBuilder sqlSb = new StringBuilder()
                .append("select max(serial_id) from shipment shp ")
                .append("inner join company selr on selr.id = shp.seller_company_id ")
                .append("where selr.k_code = :supplierKcode ")
                .append("and status = :draftStatusName ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("supplierKcode", supplierKcode);
        q.addValue("draftStatusName", ShipmentStatus.SHPT_DRAFT.toString());

        Integer o = null;
        try{
             o = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
        }catch (EmptyResultDataAccessException ex){

        }

        return o == null ? 0 : o;
    }

    public String queryBuyerKcode(String shipmentName   ) {
        String sql = ("select c.k_code from shipment shp  "
                + "inner join company c on c.id = shp.buyer_company_id "
                + "where shp.name =  :shipmentName ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);
        List resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
        Assert.isTrue(resultList.size() == 1);
        return (String) resultList.get(0);
    }

    @Transactional("transactionManager")
    private String updateData(Ivs shipment  , BigDecimal salesTaxRate   , BigDecimal subtotal   ,
                BigDecimal salesTax   , BigDecimal total  )   {
        StringBuilder sqlSb = new StringBuilder()
                .append("update shipment shp set ")
                .append("        sales_tax_rate = :salesTaxRate, ")
                .append("              subtotal = :subtotal, ")
                .append("            amount_tax = :salesTax, ")
                .append("          amount_total = :total, ")
                .append("destination_country_id = :countryId, ")
                .append("    shipping_method_id = sm.id ,")
                //.append("    shipping_method_id = 2 ,")
                .append("    status = :status ")
                .append(" from shipping_method sm ")
                .append(" where sm.name = :shippingMethod ")
                .append("  and  shp.name = :shipmentName ");


        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("salesTaxRate", salesTaxRate);
        q.addValue("subtotal", subtotal);
        q.addValue("salesTax", salesTax);
        q.addValue("total", total);

        q.addValue("shippingMethod", shipment.getShippingMethod().toString());

        //q.addValue("countryId", Country.valueOf(shipment.getDestinationCountry()).getKey());
        q.addValue("countryId", Country.valueOf(shipment.getDestinationCountry()).getKey());
        q.addValue("status", shipment.getStatus().toString());

        q.addValue("shipmentName", shipment.getName());

        int insertedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);



        Assert.isTrue(insertedRows == 1, "No or more than one shipment updated.");

        this.deleteInfo(shipment.getName());
        this.insertInfo(shipment.getName(), shipment);
        this.deleteLineItems(shipment.getName());
        this.insertLineItems(shipment.getName(), shipment);

        return shipment.getName();
    }

    private void deleteInfo(String shipmentName ) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("delete from shipment_info_ivs sii using shipment shp" +
                " where shp.id = sii.shipment_id and shp.name =  :shipmentName ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sqlSb.toString(),q) == 1);
    }

    private void deleteLineItems(String shipmentName ) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("delete from shipment_line_item shpli using shipment shp " +
                "where shp.id = shpli.shipment_id " +
                " and shp.name = :shipmentName ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);
        getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
    }

    @Transactional("transactionManager")
    private String updatedInternalNote(String shipmentName   , String internalNote   )    {
          String sql =  ("update shipment_info_ivs sii set internal_note = :internalNote "
                  + "from shipment "
                  + "inner join shipment shp on shp.name = :shipmentName "
                  + "where sii.shipment_id = shp.id ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("internalNote", internalNote);
        q.addValue("shipmentName", shipmentName);
        int updatedRows = getNamedParameterJdbcTemplate().update(sql.toString(),q);
        Assert.isTrue(updatedRows == 1);
        return shipmentName;
    }


    @Transactional("transactionManager")
    private void updateGuiInvoiceData(Ivs shipment   ) {
          String sql = ("update shipment_line_item sli "
                  + " SET gui_file_name = :gui_file_name, "
                  + " gui_uuid = :gui_uuid, "
                  + " gui_invoice_number = :gui_invoice_number "
                  + " FROM shipment sh "
                  + " WHERE sh.id = sli.shipment_id "
                  + " AND sh.name = :name "
                  + " AND sli.box_num = :box_num "
                  + " AND sli.mixed_box_line_seq = :mixed_box_line_seq ");


        for (Object o : shipment.getLineItems()) {
            IvsLineItem lineItem = (IvsLineItem)o;
            if(lineItem.getBoxNum() > 0){

                MapSqlParameterSource q = new MapSqlParameterSource();
                q.addValue("gui_file_name", lineItem.getGuiFileName());
                q.addValue("gui_uuid",lineItem.getGuiuuid());
                q.addValue("gui_invoice_number", lineItem.getGuiInvoiceNumber());
                q.addValue("name", shipment.getName());
                q.addValue("box_num", lineItem.getBoxNum());
                q.addValue("mixed_box_line_seq", lineItem.getMixedBoxLineSeq());
                int updated = getNamedParameterJdbcTemplate().update(sql,q);

                //todo arthur need check
                Assert.isTrue(updated == 1, "No or more than one shipmentLineItem inserted  $updated");
            }


        }
    }


    @Transactional("transactionManager")
    public void update(Ivs ivs   ) {

        if (ivs.getStatus() == ShipmentStatus.SHPT_DRAFT
                || ivs.getStatus() == ShipmentStatus.SHPT_AWAIT_PLAN
                || ivs.getStatus() == ShipmentStatus.SHPT_FC_BOOKING_CONFIRM
                || ivs.getStatus() == ShipmentStatus.SHPT_PLANNING
                || ivs.getStatus() == ShipmentStatus.SHPT_INITIAL_VERIFIED) {

            this.updateData(ivs, ivs.getSalesTaxRate(), ivs.getSubtotal(),
                    ivs.getSalesTax(), ivs.getTotal());
            this.updateInvoiceNumber(ivs.getName(), ivs.getInvoiceNumber());
            this.updatedInternalNote(ivs.getName(), ivs.getInternalNote());
            this.updateGuiInvoiceData(ivs);


        }else{

            this.updateStatus(ivs.getName(), ivs.getStatus());
            this.updateInvoiceNumber(ivs.getName(), ivs.getInvoiceNumber());
            this.updatedInternalNote(ivs.getName(), ivs.getInternalNote());

        }
    }

    @Transactional("transactionManager")
    public  String updateStatus(String shipmentName   , ShipmentStatus status   )    {
          String sql = "update shipment shp set status =  :status where name =  :shipmentName ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("status", status.toString());
        q.addValue("shipmentName", shipmentName);
        int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
        Assert.isTrue(updatedRows == 1);
        return shipmentName;
    }


    @Transactional("transactionManager")
    void updateInvoiceNumber(String shipmentName   , String invoiceNumber   ) {
          String sql = "update shipment shp set invoice_number =  :invoiceNumber where name =  :shipmentName ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("invoiceNumber", invoiceNumber);
        q.addValue("shipmentName", shipmentName);
        int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
        Assert.isTrue(updatedRows == 1);
    }

    int getNextVal( String tableName   , String serialName   )   {
        String sql = "select nextval(pg_get_serial_sequence(  :tableName,   :serialName ))";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("tableName", tableName);
        q.addValue("serialName", serialName);

        Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);

        return o == null  ? 0 : o;
    }

   public List<IvsRow> queryByName(String name )    {

       StringBuilder sqlSb = new StringBuilder()
               .append("select                         shp.id as id, ")
               .append("                             shp.name as name, ")
               .append("                             shp.type as type, ")
               .append("to_char(sii.expected_export_date, 'YYYY-MM-DD') as expected_export_date, ")
               .append("         sii.shipment_fca_location_id as shipment_fca_location_id, ")
               .append("   to_char(sii.fca_delivery_date, 'YYYY-MM-DD') as fca_delivery_date, ")
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
               .append("    shp.date_created at time zone 'UTC' as date_created, ")
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
               .append("inner join shipping_method sm on sm.id = shp.shipping_method_id  ")
               .append(" where shp.name = :name  ");

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("name",name);

        /*

           .append("select                         shp.id as id, ")
               .append("                             shp.name as name, ")
               .append("                             shp.type as type, ")
               .append("to_char(sii.expected_export_date, 'YYYY-MM-DD') as expected_export_date, ")
               .append("         sii.shipment_fca_location_id as shipment_fca_location_id, ")
               .append("   to_char(sii.fca_delivery_date, 'YYYY-MM-DD') as fca_delivery_date, ")
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
               .append("    shp.date_created at time zone 'UTC' as date_created, ")
               .append("    				sii.date_purchased as date_purchased, ")
               .append("    				 sii.internal_note as internal_note, ")
               .append("                  sii.special_request as special_request, ")
               .append("               sii.num_of_repackaging as num_of_repackaging, ")
               .append("                  sii.repackaging_fee as repackaging_fee, ")
               .append("                      sii.required_po as required_po, ")
               .append("                        sii.po_number as po_number, ")
               .append("                           shp.status as status ")

         public   IvsRow(int id, String name, String type, String expectedExportDate,
         String shipmentFcaLocationId, String fcaDeliveryDate, String invoiceNumber,
         String sellerCompanyKcode, String buyerCompanyKcode, String currencyName,
         BigDecimal salesTaxRate, BigDecimal subtotal, BigDecimal amountTax,
         BigDecimal amountTotal, String destinationCountryCode, String shippingMethod,
         OffsetDateTime dateCreated, String datePurchased, String internalNote,
         String specialRequest, String numOfRepackaging, String repackagingFee,
          Boolean requiredPo, String poNumber, String status) {

         */

       List dataRow =  getNamedParameterJdbcTemplate().query(sqlSb.toString(), q,
               (rs , rowNum) -> new IvsRow(
               rs.getInt("id"),rs.getString("name"),rs.getString("type"),rs.getString("expected_export_date"),
               rs.getString("shipment_fca_location_id"),rs.getString("fca_delivery_date"),
               rs.getString("invoice_number"),rs.getString("seller_company_kcode"),
               rs.getString("buyer_company_kcode")
               ,rs.getString("currency_name") ,rs.getBigDecimal("sales_tax_rate") ,
               rs.getBigDecimal("subtotal"),rs.getBigDecimal("amount_tax"), rs.getBigDecimal("amount_total") ,
               rs.getString("destination_country_code"),
               rs.getString("shipping_method"),
               (new java.sql.Timestamp(rs.getDate("date_created").getTime())).toInstant().atOffset(ZoneOffset.UTC),
               rs.getString("date_purchased"),
               rs.getString("internal_note"),
               rs.getString("special_request"),rs.getString("num_of_repackaging"),rs.getString("repackaging_fee"),
               rs.getBoolean("required_po"),rs.getString("po_number"),rs.getString("status")
       ));



       // q.addValue("name", name)
       // q.addValue("fm", "YYYY-MM-DD")
       // q.addValue("tz", "UTC")


        //val resultList = q.getResultList() as List<ShipmentIvsImpl>
        //Assert.isTrue(result.size <= 1)

        /*
        if (resultList == null || resultList.size == 0) return null
        val shp = resultList[0]
        shp.setLineItems(this.queryShipmentOfInventoryLineItems(name))
        shp.setPaymentTotal(this.queryShipmentPaymentTotal(name))

        */

       return dataRow != null ? dataRow : new ArrayList<IvsRow>();
    }


    public List<IvsLineitemRow> queryLineItems(String name)  {

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
                .append("       shpli.carton_number_end as carton_number_end , " +
                        " shpli.line_status as  line_status , pb.code_by_drs as product_base_code ")
                .append("from shipment_line_item shpli ")
                .append("inner join shipment shp on shp.id = shpli.shipment_id ")
                .append("left join shipment src_shp on src_shp.id = shpli.source_shipment_id ")
                .append("inner join product_sku ps on ps.id = shpli.sku_id ")
                .append("inner join product pps on pps.id = ps.product_id ")
                .append("inner join product_base pb on pb.id = ps.product_base_id ")
                .append("where shp.name = :name ")
                .append("order by shpli.line_seq ");

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("name",name);


        List dataRow =  getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,(rs,rowNum) -> new IvsLineitemRow(
                rs.getInt("id"),rs.getInt("line_seq"),rs.getInt("box_num"),rs.getInt("mixed_box_line_seq"),
                rs.getString("source_shipment_name"), rs.getString("code_by_drs"), rs.getString("name_by_supplier"),
                rs.getInt("quantity"), rs.getString("unit_amount"),
                rs.getString("ctn_dim_1_cm"), rs.getString("ctn_dim_2_cm"), rs.getString("ctn_dim_3_cm"),
                rs.getString("gross_weight_per_ctn_kg"), rs.getString("units_per_ctn"),
                rs.getString("numbers_of_ctn"), rs.getBoolean("require_packaging"),
                rs.getString("gui_invoice_number"), rs.getString("gui_file_name"),
                rs.getString("gui_uuid"),rs.getInt("carton_number_start"),
                rs.getInt("carton_number_end"), rs.getString("line_status") ,rs.getString("product_base_code")
        ));



        return dataRow != null ? dataRow : new ArrayList<IvsLineitemRow>();
    }

    public IvsShippingCostsRow queryShippingCosts(String shipmentName )  {

          String sql = "SELECT freight_fee_original, freight_fee_nonmerged, freight_fee_rate_id_original,  " +
                "freight_fee_rate_id_nonmerged, truck_cost, inventory_placement_fee, hs_code_fee,  " +
                "other_expense, shipping_cost_original, shipping_cost_nonmerged " +
                "FROM shipment_shipping_cost ssc " +
                "INNER JOIN shipment sh on sh.id = ssc.shipment_id " +
                "WHERE sh.name = :name ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("name",shipmentName);


        List dataRow =  getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new IvsShippingCostsRow(
                rs.getBigDecimal("freight_fee_original"),rs.getBigDecimal("freight_fee_nonmerged"),
                rs.getInt("freight_fee_rate_id_original"),rs.getInt("freight_fee_rate_id_nonmerged"),
                rs.getBigDecimal("truck_cost"),rs.getBigDecimal("inventory_placement_fee"),
                rs.getBigDecimal("hs_code_fee"),rs.getBigDecimal("other_expense"),
                rs.getBigDecimal("shipping_cost_original"),rs.getBigDecimal("shipping_cost_nonmerged")
        ));


        return dataRow != null ? (dataRow.size()> 0 ?(IvsShippingCostsRow)dataRow.get(0) :
                new IvsShippingCostsRow(BigDecimal.ZERO,BigDecimal.ZERO,
                        0,0,
                        BigDecimal.ZERO,BigDecimal.ZERO,
                        BigDecimal.ZERO,BigDecimal.ZERO,
                        BigDecimal.ZERO,BigDecimal.ZERO)): null;
    }

    @Override
    public List queryShippingCostData(String skuCode, String countryCode) {
        return null;
    }

    @Override
    public String queryBatteryType(String skuCode) {
        return null;
    }

    public List<SkuBoxNumRow> queryNewSkuBoxNum(String shipmentName   )    {
          String sql = "SELECT ps.code_by_drs as sku, box_num " +
                " FROM shipment_line_item sli " +
                " INNER JOIN shipment sh on sh.id = sli.shipment_id " +
                " INNER JOIN product_sku ps on ps.id = sli.sku_id " +
                " where sh.name = :name " +
                " and sli.line_status = 'New'";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("name",shipmentName);

        List dataRow =  getNamedParameterJdbcTemplate().query(sql,q,(rs , rowNum) -> new SkuBoxNumRow(
                rs.getString("sku") , rs.getInt("box_num")
        ));

        return dataRow != null ? dataRow : new ArrayList<SkuBoxNumRow>();
    }


    public BigDecimal queryShipmentPaymentTotal(String shipmentName )    {


          String sql =  ("select sum(bsli.statement_amount) "
                  + "from bill_statementlineitem bsli "
                  + "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
                  + "where bsli.reference = :shipmentName "
                  + "and slt.name not in (:sellBackType) ");



        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);
        q.addValue("sellBackType", StatementLineType.getSellbackTypes());
        List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q, BigDecimal.class);

        Assert.isTrue(resultList.size() == 1, "No or more than one result found.");
        return resultList.get(0) == null ? BigDecimal.ZERO : resultList.get(0) ;
    }


    public ShipmentStatus queryStatus(String shipmentName   )  {
          String sql = "select status from shipment shp where shp.name =  :shipmentName ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);
        String  o = getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
        ShipmentStatus status = ShipmentStatus.valueOf(o.toString());
        Assert.notNull(status);
        return status;
    }


    public int queryMaxSerialIdOfNonDraftShipments(String supplierKcode   )   {
        StringBuilder sqlSb = new StringBuilder()
                .append("select max(serial_id) from shipment shp ")
                .append("inner join company supplier on supplier.id = shp.seller_company_id ")
                .append("where supplier.k_code = :supplierKcode ")
                .append("and status !=  :draftStatusName ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("supplierKcode", supplierKcode);
        q.addValue("draftStatusName", ShipmentStatus.SHPT_DRAFT.toString());
        Integer o =  getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q, Integer.class);
        return o!= null ? Integer.parseInt(o.toString()) : 0;
    }




    @Transactional("transactionManager")
    public String updateNameAndSerialId(String origName   ,String newName  , int serialId )    {
          String sql = "update shipment shp set serial_id = :serialId, name = :newName where name = :origName ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("serialId", serialId);
        q.addValue("newName", newName);
        q.addValue("origName", origName);
        int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
        Assert.isTrue(updatedRows == 1);
        return newName;
    }

    @Transactional("transactionManager")
    public void setPickupRequsterForShipment(String shipmentName   ,int userId  ) {
        StringBuilder sqlSb = new StringBuilder()
                .append("update shipment_info_ivs shpii set pick_up_requester_id = :userId ")
                .append("from shipment shp ")
                .append("where shp.id = shpii.shipment_id ")
                .append("and shp.name = :shipmentName ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("userId", userId);
        q.addValue("shipmentName", shipmentName);
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sqlSb.toString(),q) == 1, "No or more than one shipment inserted.");
    }

    @Transactional("transactionManager")
    public void deleteShippingCost(String shipmentName   ) {
          String sql = "DELETE from shipment_shipping_cost ssc " +
                  " USING shipment sh " +
                  " WHERE sh.id = ssc.shipment_id " +
                  " AND sh.name = :shipmentName ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);
        getNamedParameterJdbcTemplate().update(sql.toString(),q);
    }

    @Transactional("transactionManager")
    public void insertShippingCost(String shipmentName   , BigDecimal freightFeeOriginal  ,BigDecimal freightFeeNonMerged  ,
                                    int freightFeeRateIdOriginal  , int freightFeeRateIdNonMerged  , BigDecimal truckCost  ,
                                   BigDecimal inventoryPlacementFee  ,BigDecimal hsCodeFee  , BigDecimal otherExpense  ,
                                   BigDecimal  shippingCostOriginal  ,BigDecimal shippingCostNonMerged  ) {
          String sql ="INSERT INTO shipment_shipping_cost( " +
                  " shipment_id, shipping_cost_original, shipping_cost_nonmerged, freight_fee_original,  " +
                  " freight_fee_nonmerged, freight_fee_rate_id_original, freight_fee_rate_id_nonmerged,  " +
                  " truck_cost, inventory_placement_fee, hs_code_fee, other_expense) " +
                  " SELECT id , :shippingCostOriginal, :shippingCostNonMerged, :freightFeeOriginal,  " +
                  "   :freightFeeNonMerged, :freightFeeRateIdOriginal, :freightFeeRateIdNonMerged,  " +
                  "   :truckCost, :inventoryPlacementFee, :hsCodeFee, :otherExpense " +
                  "     FROM shipment where name = :shipmentName  ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shippingCostOriginal", shippingCostOriginal);
        q.addValue("shippingCostNonMerged", shippingCostNonMerged);
        q.addValue("freightFeeOriginal", freightFeeOriginal);
        q.addValue("freightFeeNonMerged", freightFeeNonMerged);
        q.addValue("freightFeeRateIdOriginal", freightFeeRateIdOriginal);
        q.addValue("freightFeeRateIdNonMerged", freightFeeRateIdNonMerged);
        q.addValue("truckCost", truckCost);
        q.addValue("inventoryPlacementFee", inventoryPlacementFee);
        q.addValue("hsCodeFee", hsCodeFee);
        q.addValue("otherExpense", otherExpense);
        q.addValue("shipmentName", shipmentName);
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)  == 1, "insertShippingCost   update count != 1");
    }


    @Transactional("transactionManager")
    public String updatePurchasedDate(String shipmentName   ,Date  purchasedDate   )    {
          String sql = ("update shipment_info_ivs sii set date_purchased = :purchasedDate "
                  + "from shipment "
                  + "inner join shipment shp on shp.name = :shipmentName "
                  + "where sii.shipment_id = shp.id ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("purchasedDate", purchasedDate);
        q.addValue("shipmentName", shipmentName);
        int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
        Assert.isTrue(updatedRows == 1);
        return shipmentName;
    }

    @Transactional("transactionManager")
    public void updateDateConfirmed(String shipmentName   , Date date   ) {
          String sql = "update shipment s set date_confirmed = :date where s.name = :shipmentName ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("date", date);
        q.addValue("shipmentName", shipmentName);
        int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
        Assert.isTrue(updatedRows == 1);
    }

    @Transactional("transactionManager")
    public void setConfirmor(String shipmentName   , int userId   ) {
        StringBuilder sqlSb = new StringBuilder()
                .append("update shipment_info_ivs shpii set confirmor_id = :userId ")
                .append("from shipment shp ")
                .append("where shp.id = shpii.shipment_id ")
                .append("and shp.name = :shipmentName ");
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("userId", userId);
        q.addValue("shipmentName", shipmentName);
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sqlSb.toString(),q) == 1, "No or more than one shipment inserted.");
    }


    @Transactional("transactionManager")
    public String delete(String shipmentName   )    {
        this.deleteInfo(shipmentName);
        this.deleteLineItems(shipmentName);
          String sql = "delete from shipment where name = :shipmentName and status = :draftStatusName ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);
        q.addValue("draftStatusName", ShipmentStatus.SHPT_DRAFT.toString());
        getNamedParameterJdbcTemplate().update(sql,q);
        return shipmentName;
    }

    public  Map<String, BigDecimal> queryShipmentNameToPaymentTotalMap( List<String> shipmentNameList  )   {
          String sql = ("select bsli.reference, sum(bsli.statement_amount) "
                  + "from bill_statementlineitem bsli "
                  + "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
                  + "where bsli.reference in (:shipmentNameList) "
                  + "and slt.name not in (:sellBackType) "
                  + "group by bsli.reference ");

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentNameList", shipmentNameList);
        q.addValue("sellBackType", StatementLineType.getSellbackTypes());
        Map resultMap =new  HashMap<String, BigDecimal>();
        List<Object []> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        for (Object [] colAry  : columnsList) {

            String  ivsName = colAry[0].toString();
            BigDecimal paymentAmount = new BigDecimal(colAry[1].toString());
            resultMap.put(ivsName, paymentAmount);
        }
        return resultMap;
    }


    public   Map<String, String> queryShipmentNameToUnsNameMap(  List<String> shipmentNameList)   {
        StringBuilder sqlSb = new StringBuilder()
                .append("select distinct ivs.name ivs_name, uns.name uns_name ")
                .append("from shipment ivs ")
                .append("inner join shipment_line_item unsi on unsi.source_shipment_id = ivs.id ")
                .append("inner join shipment uns on uns.id = unsi.shipment_id ")
                .append("where ivs.name in   ivsNames ");

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("ivsNames", shipmentNameList);
        Map resultMap = new HashMap<String, String>();
        List<Object []> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
        for (Object [] colAry  : columnsList) {
            String  ivsName = colAry[0].toString();
            String  unsName = colAry[1].toString();

            resultMap.put(ivsName, unsName);
        }
        return resultMap;
    }

    public String querySellerKcode(   String shipmentName)    {

        StringBuilder sqlSb =new StringBuilder()
                .append("select c.k_code from shipment shp  ")
                .append("inner join company c on c.id = shp.seller_company_id ")
                .append("where shp.name =   shipmentName ");

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);
        List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
        Assert.isTrue(resultList.size() == 1);
        return resultList.get(0);
    }


    @Override
    public FreightFeeRateRow queryFreightFeeRate(String countryCode, ShippingMethod shippingMethod,
                                                 Boolean msFlag , BigDecimal chargeableWeight   )    {

          String sql = " SELECT  id, " + shippingMethod + " as freight_fee_rate " +
                " FROM freight_rate " +
                " WHERE ms_flag = :f " +
                " AND country_code = :c " +
                " AND min_eff_weight  <= :w  " +
                " AND effective_date <= now() " +
                " ORDER BY effective_date DESC, min_eff_weight DESC";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("f", msFlag);
        q.addValue("c", countryCode);
        q.addValue("w", chargeableWeight);


        List<FreightFeeRateRow> dataRow =  getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new FreightFeeRateRow(
               rs.getInt("id") , rs.getBigDecimal("freight_fee_rate")
        ));


        if (dataRow == null || dataRow.isEmpty()) {
            return null;
        }

        return dataRow.get(0);
    }


    public String queryShipmentPickupRequesterEmail(String shipmentName   )    {
        StringBuilder sqlSb = new StringBuilder()
                .append("select ui.user_email from user_info ui ")
                .append("inner join shipment_info_ivs shpii on shpii.pick_up_requester_id = ui.user_id ")
                .append("inner join shipment shp on shp.id = shpii.shipment_id ")
                .append("where shp.name =   shipmentName ");

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName", shipmentName);
        List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
        Assert.isTrue(resultList.size() <= 1);
        return (resultList.size()) == 0 ? null : resultList.get(0);
    }

    public BigDecimal querySalesTaxRate(String ivsName) {
        String sql = "select s.sales_tax_rate from shipment s where s.name = :shipmentName ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName",ivsName);
        BigDecimal rate = getNamedParameterJdbcTemplate().queryForObject(sql,q,BigDecimal.class);
        Assert.notNull(rate,"sales tax rate null");
        return rate;
    }

    @Override
    public BigDecimal[] queryUnitAmtAndSalesTaxRate(String ivsName, String sku) {

        String sql =  " select sli.unit_amount , s2.sales_tax_rate from shipment_line_item sli  " +
                "inner join shipment s2 on s2.id = sli.shipment_id " +
                "inner join product_sku ps on ps.id = sli.sku_id " +
                "where ps.code_by_drs = :sku " +
                "and s2.name = :shipmentName ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("shipmentName",ivsName);
        q.addValue("sku",sku);

        List<BigDecimal[]> rList = getNamedParameterJdbcTemplate().queryForList(sql,q).
                stream().map(row -> row.values().toArray(new BigDecimal[row.size()])).collect(Collectors.toList());;

        Assert.notNull(rList,"sales tax rate null");


        return rList.get(0);
    }




}