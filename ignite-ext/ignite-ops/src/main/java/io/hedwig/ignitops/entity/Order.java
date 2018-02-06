package io.hedwig.ignitops.entity;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Order {
  @QuerySqlField(index = true)
  private String orderId ;
  private String orderDesc;
  @QuerySqlField(index = true)
  private Integer productId;
  private Integer quantity;

}
