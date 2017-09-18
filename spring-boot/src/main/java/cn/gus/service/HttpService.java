package cn.gus.service;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
public class HttpService {



    @Autowired
    @Qualifier("mysqlOneJdbcTemplate")
    private JdbcTemplate jdbcTemplate1;

    @Autowired
    @Qualifier("msysqlOneDataSource")
    private DataSource ds;


    @RequestMapping("/home")
    @ResponseBody
    String home() throws SQLException {

        new Thread(new Runnable() {
        			public void run() {
                        for(;;) {
                            System.out.println(ds.getActive());
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
        		}).start();





        List<Map<String, Object>> res = jdbcTemplate1.queryForList("select * from t1");

        System.out.println(res);

        System.out.println(ds.getActive());
        return "Hello World!";
    }
}
