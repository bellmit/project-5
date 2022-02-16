package com.kindminds.drs.persist.data.access.rdb.inventory;




import com.kindminds.drs.api.data.access.rdb.inventory.FbaReturnToSupplierDao;
import com.kindminds.drs.api.data.transfer.fba.FbaReturnToSupplierItem;
import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;

@Repository
public class FbaReturnToSupplierDaoImpl extends Dao implements FbaReturnToSupplierDao {

    

    @Override
    public Boolean queryFbaDataExists(OffsetDateTime dateTime) {
        String sql = " select exists(" +
                " select 1 from fba_return_to_supplier \n" +
                " where drs_process_date = :drsProcessDate) ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        Timestamp drsProcessDate = Timestamp.valueOf(dateTime
                .atZoneSameInstant(ZoneId.of("Z")).toLocalDateTime());
        q.addValue("drsProcessDate", drsProcessDate);
        return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
    }

    @Override
    public Timestamp queryLastPeriodEnd() {
        String sql = " SELECT MAX(period_end) \n" +
                " from settlement_period ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        return getNamedParameterJdbcTemplate().queryForObject(sql,q,Timestamp.class);
    }

    @Override @SuppressWarnings("unchecked")
    public Map<String, List<String>> queryIvsToUnsMarketplace(List<String> ivsList) {
        String sql = "select distinct ivs.name ivs_name, uns.name uns_name, cty.code country \n" +
                " from shipment ivs \n" +
                " inner join shipment_line_item unsi on unsi.source_shipment_id = ivs.id \n" +
                " inner join shipment uns on uns.id = unsi.shipment_id \n" +
                " INNER JOIN country cty ON cty.id = uns.actual_destination_id\n" +
                " where ivs.name in (:ivsList) ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("ivsList", ivsList);
        List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        Map<String, List<String>> resultMap = new HashMap<>();
        for (Object[] columns : resultList) {
            String ivsName = (String)columns[0];
            String unsName = (String)columns[1];
            String country = (String)columns[2];
            List<String> unsMarketplaceList = new ArrayList<>();
            unsMarketplaceList.add(unsName);
            unsMarketplaceList.add(country);
            resultMap.put(ivsName,unsMarketplaceList);
        }
        return resultMap;
    }

    @Override @Transactional("transactionManager")
    public Integer insertSellbackRecords(List<FbaReturnToSupplierItem> sellbackRecords) {
        String insertSql = "INSERT INTO fba_return_to_supplier( \n" +
                "\tdrs_process_date, drs_sku, source_fba_marketplace," +
                " quantity, ivs_name, uns_name, sellback_type) \n" +
                "\tVALUES (?, ?, ?, ?, ?, ?, ?) ";

        int[][] updateCounts = this.getJdbcTemplate().batchUpdate(insertSql, sellbackRecords, 50 ,
                new ParameterizedPreparedStatementSetter<FbaReturnToSupplierItem>() {
                    @Override
                    public void setValues(PreparedStatement pstmt, FbaReturnToSupplierItem sellback) throws SQLException {

                        Timestamp drsProcessDate = Timestamp.valueOf(sellback.getDrsProcessDate()
                                .atZoneSameInstant(ZoneId.of("Z")).toLocalDateTime());
                        pstmt.setTimestamp(1, drsProcessDate);
                        pstmt.setString(2, sellback.getDrsSku());
                        pstmt.setString(3, sellback.getSourceMarketplace());
                        pstmt.setInt(4, sellback.getQuantity());
                        pstmt.setString(5, sellback.getIvsName());
                        pstmt.setString(6, sellback.getUnsName());
                        pstmt.setString(7, sellback.getSellbackType());

                    }

                });


       return sellbackRecords.size();
    }
}
