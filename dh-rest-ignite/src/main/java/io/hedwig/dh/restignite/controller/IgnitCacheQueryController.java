package io.hedwig.dh.restignite.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.hedwig.dh.restignite.repository.IgniteDao;

/**
 * 1. author: patrick 2. address: github.com/ideafortester
 */

@RestController
public class IgnitCacheQueryController {

  private static Logger logger = LoggerFactory.getLogger(IgnitCacheQueryController.class);
  @Autowired
  private IgniteDao daoService;

  @RequestMapping(value = "/sql/cache/{cache}/{entity}"
      , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
      , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  public ResponseEntity getCacheValue(
      @RequestParam String fromTime,
      @RequestParam String toTime,
      @PathVariable String cache,
      @PathVariable String entity,
      @RequestParam(required = false) String addlCriteria
  ) {

    String sqlTmp = "select _key,* from \"%s\".%s "
                    + "where ignite_update_time >=%s"
                    + " and ignite_update_time <=%s ";
    String sql = String.format(sqlTmp, cache, entity, fromTime, toTime);

    if (!StringUtils.isEmpty(addlCriteria)) {
      sql = sql + "and " + addlCriteria;
    }

    logger.info("input_sql={}", sql);
    return buildResponseEntity(sql);
  }


  @RequestMapping(value = "/sql"
      , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
      , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  public ResponseEntity sqlQuery(@RequestParam String sql) {

    logger.info("input_sql={}", sql);
    return buildResponseEntity(sql);
  }

  private ResponseEntity buildResponseEntity(@RequestParam String sql) {
    List queryResult = daoService.query(sql);
    Map<String, Object> result = new HashMap<>();
    result.put("totalCount", result.size());
    result.put("data", queryResult);
    return ResponseEntity.ok(result);
  }
}
