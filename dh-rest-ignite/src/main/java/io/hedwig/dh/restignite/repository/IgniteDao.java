package io.hedwig.dh.restignite.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.PostConstruct;

/**
 * 1. author: patrick 2. address: github.com/ideafortester
 */
@Component
public class IgniteDao {

  private JdbcTemplate template;

  @Value("${ignite.db.url}")
  private String igniteDBUrl;


  @PostConstruct
  private void init() {

    DriverManagerDataSource ds =
        new DriverManagerDataSource(this.igniteDBUrl);
    ds.setDriverClassName("org.apache.ignite.IgniteJdbcThinDriver");
    template = new JdbcTemplate(ds);
//    try {
//      DriverManagerDataSource dataSource =
//          new DriverManagerDataSource("jdbc:ignite:thin://10.213.128.98:10800");
//      Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
//      this.connection = DriverManager.getConnection(
//          "jdbc:ignite:thin://10.213.128.98:10800");
//    } catch (ClassNotFoundException | SQLException e) {
//      e.printStackTrace();
//      throw new RuntimeException("init db connection failed");
//    }
  }


  public List query(String sql) {
    return template.queryForList(sql);
  }
}
