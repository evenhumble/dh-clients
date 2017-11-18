package org.apache.ignite.yardstick.scenario.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;


public class Order {
  @QuerySqlField(index = true)
  private Long orderId ;
  private String orderDesc;
  @QuerySqlField(index = true)
  private Integer productId;
  private Integer quantity;



  public String getOrderDesc() {
    return orderDesc;
  }

  public void setOrderDesc(String orderDesc) {
    this.orderDesc = orderDesc;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
}
