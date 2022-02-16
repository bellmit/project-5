package com.kindminds.drs.persist.data.access.rdb.customercare;

import com.kindminds.drs.Criteria;
import com.kindminds.drs.CustomerCareCaseQueryField;

import com.kindminds.drs.Filter;
import com.kindminds.drs.api.data.row.customercare.CustomerCareCaseRow;
import com.kindminds.drs.api.data.access.rdb.customercare.CustomerCareCaseDao;

import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Repository
public class CustomerCareCaseDaoImpl extends Dao implements CustomerCareCaseDao {

   // @PersistenceContext(unitName = "default")
   // private val entityManager EntityManager = null

    //@Autowired
    //private DSLContext dsl  = null;

    //@Autowired
    //private ModelMapper mapper  = null;

    /*

    override fun getCustomerCareCaseByFilter(filter Filter) MutableList<CustomerCareCaseRow> {

        //SELECT id, type_id, channel_id, drs_company_id, ,
        // issue_type_category_id, marketplace_id, marketplace_order_id, marketplace_order_date, customer_name, customer_email, date_create, status, last_update_time
        //FROM public.customercarecase;


        //use union to get data

        val sql = "select ccc.id , ccc.supplier_company_id , ccc.date_create " +
                " from customercarecase ccc  " +
                 " inner join company c on ccc.supplier_company_id = c.id " +
                " where  c.k_code =  kcode " +
                "  and ccc.date_create >= sd and ccc.date_create <= ed "

        val q = this.entityManager.createNativeQuery(sql , CustomerCareCaseRowImplclass.java )

        filter.getCriteriaList().forEach {
            if(it.field == CustomerCareCaseQueryField.KCode){
                q.setParameter("kcode", it.value)
            }

            if(it.field == CustomerCareCaseQueryField.StartDate){
                q.setParameter("sd",  Timestamp.valueOf( LocalDateTime.parse(it.value,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss"))) )
            }

            if(it.field == CustomerCareCaseQueryField.EndDate){
                q.setParameter("ed",  Timestamp.valueOf( LocalDateTime.parse(it.value,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss"))) )
            }
        }




        val columnsList =
                q.resultList as MutableList<CustomerCareCaseRow>

        return columnsList
    }

    */


    public List<CustomerCareCaseRow> getCustomerCareCaseByFilter(Filter filter )  {


        String sql = "select ccc.id , ccc.supplier_company_id , ccc.date_create " +
                " from customercarecase ccc  " +
                " inner join company c on ccc.supplier_company_id = c.id " +
                " where  c.k_code = :kcode  and ccc.date_create >=  :st and ccc.date_create <=  :et ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        //List bindValues = new ArrayList<Object>();
        for (Criteria it: filter.getCriteriaList()) {
            if(it.field == CustomerCareCaseQueryField.KCode){
                q.addValue("kcode", it.value);
            }

            if(it.field == CustomerCareCaseQueryField.StartDate){
                q.addValue("st", Timestamp.valueOf( LocalDateTime.parse(it.value,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss"))));

            }

            if(it.field == CustomerCareCaseQueryField.EndDate){
                q.addValue("et", Timestamp.valueOf( LocalDateTime.parse(it.value,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss"))));
            }
        }


        List<CustomerCareCaseRow>  result = getNamedParameterJdbcTemplate().query(sql,q,(rs , rowNum) ->
                   new  CustomerCareCaseRow(rs.getInt("id"),rs.getInt("supplier_company_id"),
                           rs.getString("date_create"))

             );


        return result != null ? result : new ArrayList<CustomerCareCaseRow>();



    }


}