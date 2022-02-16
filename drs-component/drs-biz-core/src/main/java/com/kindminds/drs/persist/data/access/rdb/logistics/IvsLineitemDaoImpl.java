package com.kindminds.drs.persist.data.access.rdb.logistics;

import com.kindminds.drs.api.data.row.logistics.IvsLineitemRow;
import com.kindminds.drs.api.data.access.rdb.logistics.IvsLineitemDao;


import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;



import java.util.ArrayList;
import java.util.List;

@Repository
public class IvsLineitemDaoImpl extends Dao implements IvsLineitemDao {




   public IvsLineitemRow query(  String name , int boxNum  , int mixedBoxLineSeq ) {

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
                .append("       shpli.carton_number_end as carton_number_end " +
                    " , shpli.line_status as  line_status , pb.code_by_drs as product_base_code ")
                .append("from shipment_line_item shpli ")
                .append("inner join shipment shp on shp.id = shpli.shipment_id ")
                .append("left join shipment src_shp on src_shp.id = shpli.source_shipment_id ")
                .append("inner join product_sku ps on ps.id = shpli.sku_id ")
                .append("inner join product pps on pps.id = ps.product_id ")
                .append("inner join product_base pb on pb.id = ps.product_base_id ")
                .append("where shp.name = :name and box_num = :num and mixed_box_line_seq = :seq ")
                .append("order by shpli.line_seq ");


       MapSqlParameterSource q = new MapSqlParameterSource();
       q.addValue("name", name);
       q.addValue("num", boxNum);
       q.addValue("seq", mixedBoxLineSeq);

       List<IvsLineitemRow> dataRowList =  getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,(rs,rowNum) -> new IvsLineitemRow(
               rs.getInt("id"),rs.getInt("line_seq"),rs.getInt("box_num"),rs.getInt("mixed_box_line_seq"),
               rs.getString("source_shipment_name"), rs.getString("code_by_drs"), rs.getString("name_by_supplier"),
               rs.getInt("quantity"), rs.getString("unit_amount"),
               rs.getString("ctn_dim_1_cm"), rs.getString("ctn_dim_2_cm"), rs.getString("ctn_dim_3_cm"),
               rs.getString("gross_weight_per_ctn_kg"), rs.getString("units_per_ctn"),
               rs.getString("numbers_of_ctn"), rs.getBoolean("require_packaging"),
               rs.getString("gui_invoice_number"), rs.getString("gui_file_name"),
               rs.getString("gui_uuid"), rs.getInt("carton_number_start"),
               rs.getInt("carton_number_end"), rs.getString("line_status") ,rs.getString("product_base_code")
       ));


        return (dataRowList != null  && dataRowList.size() >0) ? dataRowList.get(0) : null;



       
    }


    @Transactional
    public void updateLineItemStatus(int id,String status ) {

        String  sql = "UPDATE shipment_line_item sli " +
                " SET line_status = :status " +
                " WHERE sli.id = :id " ;


        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id", id);
        q.addValue("status", status);


        int  updateCount = getNamedParameterJdbcTemplate().update(sql,q);

        Assert.isTrue(updateCount == 1, "update count != 1 $updateCount");
    }


    public List<IvsLineitemRow>  queryByName(String name) {

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
                .append("       shpli.carton_number_end as carton_number_end  ,  " +
                        " shpli.line_status as  line_status , pb.code_by_drs as product_base_code ")
                .append("from shipment_line_item shpli ")
                .append("inner join shipment shp on shp.id = shpli.shipment_id ")
                .append("left join shipment src_shp on src_shp.id = shpli.source_shipment_id ")
                .append("inner join product_sku ps on ps.id = shpli.sku_id ")
                .append("inner join product pps on pps.id = ps.product_id ")
                .append("inner join product_base pb on pb.id = ps.product_base_id ")
                .append("where shp.name = :name  ")
                .append("order by shpli.line_seq ");

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("name", name);

        List<IvsLineitemRow> dataRow =  getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,(rs,rowNum) -> new IvsLineitemRow(
                rs.getInt("id"),rs.getInt("line_seq"),rs.getInt("box_num"),rs.getInt("mixed_box_line_seq"),
                rs.getString("source_shipment_name"), rs.getString("code_by_drs"), rs.getString("name_by_supplier"),
                rs.getInt("quantity"), rs.getString("unit_amount"),
                rs.getString("ctn_dim_1_cm"), rs.getString("ctn_dim_2_cm"), rs.getString("ctn_dim_3_cm"),
                rs.getString("gross_weight_per_ctn_kg"), rs.getString("units_per_ctn"),
                rs.getString("numbers_of_ctn"), rs.getBoolean("require_packaging"),
                rs.getString("gui_invoice_number"), rs.getString("gui_file_name"),
                rs.getString("gui_uuid"), rs.getInt("carton_number_start"),
                rs.getInt("carton_number_end"), rs.getString("line_status") ,rs.getString("product_base_code")
        ));



        return dataRow != null ? dataRow : new ArrayList<IvsLineitemRow>();
    }


}