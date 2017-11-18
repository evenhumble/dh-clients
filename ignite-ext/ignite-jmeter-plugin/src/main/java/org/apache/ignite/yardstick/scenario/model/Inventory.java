package org.apache.ignite.yardstick.scenario.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;



public class Inventory {

  @QuerySqlField(index = true)
  private String name;
  @QuerySqlField(index = true)
  private Integer productId;
  private int quantity=1000000;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
