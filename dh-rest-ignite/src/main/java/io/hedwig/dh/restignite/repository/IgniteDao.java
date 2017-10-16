package io.hedwig.dh.restignite.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import io.hedwig.dh.restignite.utils.client.IgniteWrapper;

/**
 * 1. author: patrick 2. address: github.com/ideafortester
 */
@Component
public class IgniteDao {

  @Autowired
  private JdbcTemplate template;

//  public IgniteDaoService() {
//    IgniteWrapper.getIgnite().active(true);
//    init();
//  }


  private void init() {
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
