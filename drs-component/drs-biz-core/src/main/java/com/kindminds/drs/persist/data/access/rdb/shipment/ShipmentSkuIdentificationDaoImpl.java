package com.kindminds.drs.persist.data.access.rdb.shipment;

import com.kindminds.drs.Country;


import com.kindminds.drs.api.data.access.rdb.shipment.ShipmentSkuIdentificationDao;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.logistics.ShipmentSkuIdentification;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ShipmentSkuIdentificationDaoImpl extends Dao implements ShipmentSkuIdentificationDao {


    @Override
    public List<Object[]> query(int drsTranId, String status) {

        String sql = "select ssi.id , " +
                " ssi.drs_transaction_id , ssi.status , ssi.remark , ssi.ivs_sku_serial_no " +
                " from shipment_sku_identification ssi " +
                " where ssi.drs_transaction_id = :drsTranId and ssi.status = :status " +
                " order by ssi.id ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("drsTranId", drsTranId);
        q.addValue("status", status);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql, q, objArrayMapper);

        return columnsList;

    }

    @Override
    public List<Object[]> query(int drsTranId) {


        StringBuilder sqlSb = new StringBuilder()
                .append("select   ssi.id , " +
                        " ssi.drs_transaction_id , ssi.status , ssi.remark , ivs.name as ivs_name ," +
                        "  uns.name as uns_name  " +
                        " from shipment_sku_identification ssi " +
                        " inner join shipment_line_item sli on ssi.ivs_shipment_line_item_id  = sli.id " +
                        " inner join product_sku ps on ps.id = ssi.sku_id  " +
                        " inner join shipment ivs on ivs.id = ssi.ivs_shipment_id  " +
                        " inner join company c on c.id = ivs.seller_company_id " +
                        " inner join shipment uns on uns.id = ssi.uns_shipment_id " +
                        " inner join shipment_uns_info sui on sui.shipment_id = uns.id " +
                        " where ssi.drs_transaction_id = :drsTranId " +
                        " order by sui.arrival_date , ssi.id ");

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("drsTranId", drsTranId);
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(), q, objArrayMapper);

        return columnsList;

    }

    @Override
    public List<Object[]> queryCanBeRefund(Country unsDestinationCountry, String drsSku) {

        StringBuilder sqlSb = new StringBuilder()
                .append("select   ssi.id , " +
                        " ssi.drs_transaction_id , ssi.status , ssi.remark , ssi.ivs_sku_serial_no  " +
                        " from shipment_sku_identification ssi " +
                        " inner join shipment_line_item sli on ssi.ivs_shipment_line_item_id  = sli.id " +
                        " inner join product_sku ps on ps.id = ssi.sku_id  " +
                        " inner join shipment ivs on ivs.id = ssi.ivs_shipment_id  " +
                        " inner join company c on c.id = ivs.seller_company_id " +
                        " inner join shipment uns on uns.id = ssi.uns_shipment_id " +
                        " inner join shipment_uns_info sui on sui.shipment_id = uns.id " +
                        " where " +
                        "  ( ssi.status <> 'Refund' or  ssi.status <> '' or ssi.status <> 'FBA Returns Recovery' ) and ps.code_by_drs = :drsSku  " +
                        " and uns.destination_country_id = :countryKey  " +
                        " and (uns.status = 'SHPT_RECEIVING' or uns.status = 'SHPT_RECEIVED' ) " +
                        " order by sui.arrival_date , ssi.id ");


        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("drsSku", drsSku);
        q.addValue("countryKey", unsDestinationCountry.getKey());


        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(), q, objArrayMapper);

        return columnsList;

    }

    @Override
    public List<Object[]> queryCanBeSold(Country unsDestinationCountry, String drsSku) {

        StringBuilder sqlSb = new StringBuilder()
                .append("select   ssi.id , " +
                        " ssi.drs_transaction_id , ssi.status , ssi.remark , ivs.name as ivs_name ," +
                        "  uns.name as uns_name  " +
                        " from shipment_sku_identification ssi " +
                        " inner join shipment_line_item sli on ssi.ivs_shipment_line_item_id  = sli.id " +
                        " inner join product_sku ps on ps.id = ssi.sku_id  " +
                        " inner join shipment ivs on ivs.id = ssi.ivs_shipment_id  " +
                        " inner join company c on c.id = ivs.seller_company_id " +
                        " inner join shipment uns on uns.id = ssi.uns_shipment_id " +
                        " inner join shipment_uns_info sui on sui.shipment_id = uns.id " +
                        " where " +
                        "  ( ssi.status = 'Refund' or  ssi.status = '' or  ssi.status = 'FBA Returns Recovery' or ssi.status is null ) and ps.code_by_drs = :drsSku  " +
                        " and uns.destination_country_id = :countryKey  " +
                        " and (uns.status = 'SHPT_RECEIVING' or uns.status = 'SHPT_RECEIVED' ) " +
                        " order by sui.arrival_date , ssi.id ");


        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("drsSku", drsSku);
        q.addValue("countryKey", unsDestinationCountry.getKey());

        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(), q, objArrayMapper);

        return columnsList;

    }

    @Override
    public List<Object[]> queryCanBeSoldBackRecovery(Country unsDestinationCountry, String drsSku) {

        StringBuilder sqlSb = new StringBuilder()
                .append("select   ssi.id , " +
                        " ssi.drs_transaction_id , ssi.status , ssi.remark , ivs.name as ivs_name ," +
                        "  uns.name as uns_name  " +
                        " from shipment_sku_identification ssi " +
                        " inner join shipment_line_item sli on ssi.ivs_shipment_line_item_id  = sli.id " +
                        " inner join product_sku ps on ps.id = ssi.sku_id  " +
                        " inner join shipment ivs on ivs.id = ssi.ivs_shipment_id  " +
                        " inner join company c on c.id = ivs.seller_company_id " +
                        " inner join shipment uns on uns.id = ssi.uns_shipment_id " +
                        " inner join shipment_uns_info sui on sui.shipment_id = uns.id " +
                        " where " +
                        "  ( ssi.status <> 'Refund' or  ssi.status <> '' or ssi.status <> 'FBA Returns Recovery') and ps.code_by_drs = :drsSku  " +
                        " and uns.destination_country_id = :countryKey  " +
                        " and (uns.status = 'SHPT_RECEIVING' or uns.status = 'SHPT_RECEIVED' ) " +
                        " order by sui.arrival_date , ssi.id ");


        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("drsSku", drsSku);
        q.addValue("countryKey", unsDestinationCountry.getKey());


        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(), q, objArrayMapper);

        return columnsList;

    }


    @Override
    public void update(SkuShipmentAllocationInfo skuShipmentAllocationInfo, int drsTransactionId) {

        String sql = "update shipment_sku_identification  set "
                + " drs_transaction_id = :dtId , status  = :status  , remark= :remark "
                + "where id = :id ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id", skuShipmentAllocationInfo.getIvsSkuIdentificationId());
        q.addValue("dtId", drsTransactionId);
        q.addValue("status", skuShipmentAllocationInfo.getIvsSkuIdentificationStatus());
        q.addValue("remark", skuShipmentAllocationInfo.getIvsSkuIdentificationRemark());

        int affectedRows = getNamedParameterJdbcTemplate().update(sql, q);


    }

    @Override
    public void update(int id, int drsTransactionId, String status, String remark) {


        String sql = "update shipment_sku_identification  set "
                + " drs_transaction_id = :dtId , status  = :status  , remark= :remark "
                + "where id = :id ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id", id);
        q.addValue("dtId", drsTransactionId);
        q.addValue("status", status);
        q.addValue("remark", remark);

        int affectedRows = getNamedParameterJdbcTemplate().update(sql, q);
        System.out.println("update sku idenity");
        System.out.println(affectedRows);
        System.out.println("update sku idenity");

    }


    @Override
    public List<ShipmentSkuIdentification> doShipmentSkuIdentification(String unsName) {
        List<ShipmentSkuIdentification> skuIdentifications =new ArrayList<>();

        String sql ="select         s.id as uns_id, "
                +"                 sh.id as ivs_id, "
                +"                sli.id as shipment_line_item_id, "
                +"            sli.sku_id as sku_id, "
                +"                c.code as destination_country, "
                +"               sh.name as ivs_name, "
                +"        ps.code_by_drs as code_by_drs, "
                +"         sli.qty_order as quantity, "
                +"           sli.box_num as box_num, "
                +"sli.mixed_box_line_seq as mixed_box_line_seq "
                + "from public.shipment_line_item sli "
                + "inner join shipment s on s.id = sli.shipment_id "
                + "inner join shipment sh on sh.id = sli.source_shipment_id "
                + "inner join country c on c.id =s.destination_country_id "
                + "inner join product_sku ps on ps.id =sli.sku_id "
                + "where s.name= :unsName ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("unsName",unsName);

        List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql, q, objArrayMapper);

        for(Object[] result : resultList){
           int qty=(Integer) result[7];
           for(int seq=1;seq<=qty;seq++){
           int unsId=((Long)result[0]).intValue();
           int ivsId=((Long)result[1]).intValue();
           ShipmentSkuIdentification ssi= new ShipmentSkuIdentification(
                  unsId,ivsId,(Integer)result[2],seq,(Integer)result[3],(String)result[4],
                   (String)result[5],(String)result[6],((Integer) result[8])
                   ,((Integer) result[9]) ,seq);
           skuIdentifications.add(ssi);
           }
        }

        return skuIdentifications;
    }

    @Override
    @Transactional("transactionManager")
    public void add(List<ShipmentSkuIdentification> shipmentSkuIdentification) {
        shipmentSkuIdentification.forEach(x ->{

            String sql ="INSERT INTO public.shipment_sku_identification "
                    +" (uns_shipment_id, ivs_shipment_id, ivs_shipment_line_item_id, "
                    +" seq, sku_id, destination_country, ivs_sku_serial_no , remark , status) "
                    +" VALUES ( :unsShipmentId, :ivsShipmentId, :ivsShipmentLineItemId, "
                    +" :seq, :skuId, :destinationCountry, :ivsSkuSerialNo , '' ,'')";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("unsShipmentId",x.getUnsShipmentId());
            q.addValue("ivsShipmentId",x.getIvsShipmentId());
            q.addValue("ivsShipmentLineItemId",x.getIvsShipmentLineItemId());
            q.addValue("seq",x.getSeq());
            q.addValue("skuId",x.getSkuId());
            q.addValue("destinationCountry",x.getDestinationCountry());
            q.addValue("ivsSkuSerialNo",x.getIvsSkuSerialNo());

            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });

    }

    @Override
    public List<Object[]> queryByUns(String unsName) {
        String sql = "SELECT ssi.* "
                + " FROM public.shipment_sku_identification ssi "
                + " inner join shipment s on s.id = ssi.uns_shipment_id "
                + " where s.name = :unsName ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("unsName",unsName);
        List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql, q, objArrayMapper);

        return resultList;
    }


}

