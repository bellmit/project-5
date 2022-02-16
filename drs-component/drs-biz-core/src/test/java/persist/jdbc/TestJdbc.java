package persist.jdbc;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestJdbc {


    @Value("#{dataSource}")
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;


    @Test
    public void testDs() {

        jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("AAAAAAAAAAAAAA");
        int result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) from user_info ", Integer.class);
        System.out.println("AAAAAAAAAAAAAA");
        System.out.println(result);
        System.out.println("AAAAAAAAAAAAAA");

    }

    @Test
    public void testDs2(){

        String sql = "SELECT  cpy.k_code, sum(amount) as amount  " +
                "\tFROM remittance rm  " +
                "\tINNER JOIN company cpy ON cpy.id = rm.sender_company_id  " +
                //"\tWHERE date_sent > :latestEnd  " +
               // "\t AND date_sent < :reportDate " +
                "\tgroup by cpy.k_code ";

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Object []> resultList = jdbcTemplate.query(sql,(rs, rowNum)-> {
            int cols = rs.getMetaData().getColumnCount();
            Object[] arr = new Object[cols];
            for(int i=0; i<cols; i++){
                arr[i] = rs.getObject(i+1);
            }
            return arr;
        }
    );


        Map<String, BigDecimal> supplierToRemittanceMap = new HashMap<>();

        for (Object[] result : resultList) {
            System.out.println(result[0]);
            System.out.println(result[1]);
            supplierToRemittanceMap.put((String) result[0], (BigDecimal) result[1]);
        }



    }


}
