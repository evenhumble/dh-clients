package io.hedwig.ignitops.entity;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class IgniteAuditEntity implements Serializable {

  @Column(name = "ignite_update_time")
  @QuerySqlField(name="ignite_update_time")
  private Long igniteUpdateTime = new Date().getTime();
  /**
   * 预留字段（for ignite）
   */
  @Column(name = "reserved_json")
  @QuerySqlField(name = "reserved_json")
  private String reservedJson;

}
