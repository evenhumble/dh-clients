package io.hedwig.dh.jmeter;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;

import java.util.Random;

public class CouponParams {

  private final JavaSamplerContext context;
  private int couponId;
  private String igniteSetting;
  private transient Random random = new Random();
  private String stopIfSellout;
  /**
   * coupon init quantity
   */
  private int couponInitQuantity;
  /**
   * every tx coupon quantity
   */
  private int couponTxQuantity;

  public CouponParams(JavaSamplerContext context) {
    this.context = context;
    this.couponId =
        this.context.getParameter("couponId") == null ? 0
                                                      : this.context.getIntParameter("couponId");
    System.out.println("couponId is "+ this.couponId);
    this.couponInitQuantity =
        this.context.getIntParameter("initQuantity") == 0 ? 1000 : this.context
            .getIntParameter("initQuantity");
    this.couponTxQuantity =
        this.context.getIntParameter("txQuantity") == 0 ? 1 : this.context
            .getIntParameter("txQuantity");
    this.igniteSetting =
        this.context.getParameter("ignite_setting") == null ? "ignite_setting" : this.context
            .getParameter("ignite_setting");
    this.stopIfSellout =
        this.context.getParameter("stopIfSellout") == null ? "ignite_setting" : this.context
            .getParameter("stopIfSellout");
  }


  public int getCouponId() {
    if (this.couponId == 0) {
      return this.random.nextInt(10) + 1;
    } else {
      return this.couponId;
    }
  }

  public String getIgniteSetting() {
    return igniteSetting;
  }

  public int getCouponInitQuantity() {
    return couponInitQuantity;
  }

  public int getCouponTxQuantity() {
    return couponTxQuantity;
  }

  public boolean isStopIfSellout() {
    try {
      return Boolean.parseBoolean(this.stopIfSellout);
    } catch (Exception e) {

    }
    return true; //default is true
  }
}
