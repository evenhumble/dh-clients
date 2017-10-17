package io.hedwig.dh.restignite.repository;

import static org.junit.Assert.*;

import org.apache.ignite.Ignite;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 1. author: patrick 2. address: github.com/ideafortester
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IgniteDaoTest {

  @Autowired
  IgniteDao dao;

  @Test
  public void test_queryForList(){
    String sql = String.format("select count(*) from %s",
                               "\"drcard_cardmwallet\".cardmwallet" );
    List result = dao.query(sql);
    Assert.assertNotNull(result);
  }

}