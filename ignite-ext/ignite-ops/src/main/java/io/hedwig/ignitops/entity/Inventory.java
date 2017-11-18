package io.hedwig.ignitops.entity;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Inventory {

  @QuerySqlField(index = true)
  private String name;
  @QuerySqlField(index = true)
  private Integer productId;
  private int quantity=1000000;


}
