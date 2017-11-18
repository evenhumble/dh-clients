package io.hedwig.dh.jmeter;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.ignite.yardstick.scenario.model.Inventory;
import org.apache.ignite.yardstick.scenario.model.Order;


public class IgniteCouponTxSample extends AbstractJavaSamplerClient {

  private static final Logger log = LoggerFactory.getLogger(AbstractJavaSamplerClient.class);
  private Ignite ignite;
  private static final int MAX = 100_000_000;
  private static AtomicLong ORDER_GENERATOR = new AtomicLong(0);
  private IgniteCache<Integer, Inventory> icI;
  private IgniteCache<Long, Order> icO;
  private CouponParams params;


  /**
   * Runnning before the test
   */
  @Override
  public void setupTest(JavaSamplerContext context) {

    params = new CouponParams(context);
    CacheConfiguration inventoryCC = IgniteWrapper.createDefaultTxCacheConfig("inventory");
    CacheConfiguration orderCC = IgniteWrapper.createDefaultTxCacheConfig("order");
    ignite = IgniteWrapper.getIgnite();
    ignite.addCacheConfiguration(inventoryCC);
    ignite.addCacheConfiguration(orderCC);
    icI = ignite.getOrCreateCache("inventory");
    icO = ignite.getOrCreateCache("order");
    System.out.println("setup transaction");


  }

  @Override
  public void teardownTest(JavaSamplerContext context) {
    System.out.println("only run once");
    this.ignite.close();

  }

  private Callable<Void> couponTX(IgniteCache<Integer, Inventory> inventoryCache,IgniteCache<Long, Order> orderCache,
                                  SampleResult result) {
    return () -> {
      Inventory inventory = icI.get(this.params.getCouponId());
      Order order = new Order();
      order.setOrderId(ORDER_GENERATOR.incrementAndGet());
      order.setQuantity(params.getCouponTxQuantity());
      order.setOrderDesc("Order Test");
      order.setProductId(this.params.getCouponId());
      int quantityRemain = inventory.getQuantity() - order.getQuantity();
      if (inventory.getQuantity() > 0 && quantityRemain > 0) {
        inventory.setQuantity(quantityRemain);
        try {
          inventoryCache.put(inventory.getProductId(), inventory);
          orderCache.put(order.getOrderId(), order);
          result.setResponseCode("200");
          result.setResponseMessageOK();
          result.setSuccessful(true);
        } catch (Exception e) {
          e.printStackTrace();
          result.setResponseMessage(e.getMessage());
          result.setSuccessful(false);
        }
      } else {
        if (inventory.getQuantity() <= 0) {
          if (this.params.isStopIfSellout()) {
            throw new RuntimeException("stop test as it is overbooked!");
          } else {
            //reset the quantity
            inventory.setQuantity(1000);
            icI.put(inventory.getProductId(), inventory);
          }

        }
      }

      return null;
    };
  }

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    log.info("start ignite Testing");
    SampleResult result = new SampleResult();
    result.setSampleLabel("IgniteCouponRush");
    result.sampleStart();
    IgniteTransactions txs = ignite.transactions();
    try {
      IgniteBenchmarkUtils
            .doInTransaction(txs,TransactionConcurrency.OPTIMISTIC,TransactionIsolation.SERIALIZABLE,couponTX(icI,icO,result));
    } catch (Exception e) {
      e.printStackTrace();
    }

    result.sampleEnd();
    return result;
  }


  public Arguments getDefaultParameters() {
    Arguments params = new Arguments();
    params.addArgument("ignite_setting", "ignite_setting");
    params.addArgument("couponId", "2");
    params.addArgument("initQuantity", "1000");
    params.addArgument("txQuantity", "1");
    params.addArgument("stopIfSellout", "true");
    return params;
  }

//  public static void main(String[] args) {
//    IgniteCouponTxSample sample = new IgniteCouponTxSample();
//    Arguments params = sample.getDefaultParameters();
//    JavaSamplerContext context = new JavaSamplerContext(params);
//
//    sample.setupTest(context);
//    SampleResult result = sample.runTest(context);
//    sample.teardownTest(context);
//    System.out.println(result);
//  }
}

