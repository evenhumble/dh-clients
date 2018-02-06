package io.hedwig.ignitops.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.hedwig.ignitops.utils.client.IgniteWrapper;


public class IgniteDaoService {

  private Connection connection;
  private JdbcTemplate jdbcTemplate;

  public IgniteDaoService() {
    IgniteWrapper.getIgnite().active(true);
    init();

  }


  private void init() {
    try {
//      Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
//      this.connection = DriverManager.getConnection(
//          "jdbc:ignite:thin://localhost:10800");
      DriverManagerDataSource ds = new DriverManagerDataSource("jdbc:ignite:thin://localhost:10800");
      ds.setDriverClassName("org.apache.ignite.IgniteJdbcThinDriver");
      jdbcTemplate = new JdbcTemplate(ds);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("init db connection failed");
    }
  }

  public void getCacheCount(String fqn) throws SQLException {
    init();
    //String sql = String.format("select count(distinct ignite_update_time) as total from %s", fqn);
    //String statement="select count(*) as total from %s";
    //String statement="merge into \"drcard_cardwallet\".cardwallet(_KEY,WALLETID,USERNAME,EXCODE,ASSETTYPE,EXTERNALUID,TOTALBALANCE,AVAILABLEBALANCE,RESERVEDBALANCE,BINDTIME,ISDISPALY,OFFLINE,IGNITE_UPDATE_TIME,RESERVED_JSON) values('417', '993060974686566830683569573168', 'username-993060974686566830683569573168', 'exc-993060974686566830683569573168', null, 'null', 120, 120, 230, '2017-09-14 14:10:47.56', 0, 'false', 1505369447560, \"null\")";

//    String statement = "SELECT count(*) as total,ignite_update_time FROM %s group by ignite_update_time having count(*)>1";
//    String statement = "select count(*) as total from \"inventory\".inventory";
    String statement="select count(*) as total from %s where ignite_update_time >=1405872528903";
    String sql = String.format(statement,fqn);
    List result =jdbcTemplate.queryForList(sql);
    for (Object o : result) {
      System.out.println(o);
    }
//    System.out.println(String.format("query sql is %s", sql));
//    Statement stmt = this.connection.createStatement();
////    try{
////    stmt.execute(statement);
//    try (ResultSet rs =
//             stmt.executeQuery(sql)) {
//      while (rs.next()) {
//        System.out.println(">>> " + fqn + ":" + rs.getString("total")
////                           +"ignite_update_time:" +rs.getString("ignite_update_time")
//        );
//      }
//
//    } catch (SQLException e) {
////      e.printStackTrace();
//      e.printStackTrace();
//
//    }finally {
//      close();
//    }
  }

  private void close() throws SQLException {
    this.connection.close();
  }


  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    IgniteDaoService service = new IgniteDaoService();
    List<String> fullQualifierNames = new ArrayList<>();
    fullQualifierNames.add("\"drcard_cardwallet\".cardwallet");
    fullQualifierNames.add("\"drcard_cardlist\".cardlist");
    fullQualifierNames.add("\"drcard_cardmwallet\".cardmwallet");
    fullQualifierNames.add("\"drcard_cardtrans\".cardtrans");
    fullQualifierNames.add("\"drcard_cardmlist\".cardmlist");

    for (String fullQualifierName : fullQualifierNames) {
     try {
       service.getCacheCount(fullQualifierName);
     }catch (Exception e){
//       e.printStackTrace();
     }
    }
    service.close();
  }
}
