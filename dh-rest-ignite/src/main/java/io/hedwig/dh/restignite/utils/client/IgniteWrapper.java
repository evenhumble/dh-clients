package io.hedwig.dh.restignite.utils.client;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IgniteWrapper {

  private static Ignite ignite ;
  public static Ignite getIgnite() {
//    return Ignition.start("ignite_setting.xml");

    if(ignite==null) {
      ignite = Ignition.start("ignite_setting.xml");
    }
    return ignite;
  }

  public static <K, V> void putAndPrint(IgniteCache<K, V> cache,
                                        K key, V instance) {
    System.out.println("starting put caches");
    cache.put(key, instance);
    System.out.println(cache.get(key));
  }

  public static CacheConfiguration createDefaultTxCacheConfig(String cacheName){

     CacheConfiguration cc = new CacheConfiguration(cacheName);
     cc.setCacheMode(CacheMode.PARTITIONED);
     cc.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
     return cc;
  }

  public static <K, V> void putAndPrintBinary(IgniteCache<K, V> cache, K key, V instance) {
    System.out.println("starting put caches");
    cache.put(key, instance);
    System.out.println(cache.get(key));
    IgniteCache<K, BinaryObject> binaryCache = cache.withKeepBinary();
    BinaryObject bo = binaryCache.get(key);
    System.out.println(bo.type());
    System.out.println(bo.hasField("name"));
    System.out.println(java.util.Optional.ofNullable(bo.field("firstName")));
    System.out.println("same cache?"+(cache.get(key).equals(bo)?"yes":"no"));
  }

  public static <K,V> void getAll(IgniteCache<K,V> cache){
    Set<Integer> keys = new HashSet<>();
    for (int i = 0; i < 100; i++) {
       keys.add(i);
    }
    Map<Integer,V> result = (Map<Integer, V>) cache.getAll((Set<? extends K>) keys);
    for (V v : result.values()) {
      System.out.println(v);
    }
  }

  public static <K,V> void getAllBinary(IgniteCache<K,V> cache){
    Set<Integer> keys = new HashSet<>();
    for (int i = 0; i < 100; i++) {
      keys.add(i);
    }
    IgniteCache<Integer, BinaryObject> binaryCache = cache.withKeepBinary();
    Map<Integer, BinaryObject> result = binaryCache.getAll(keys);
    for (BinaryObject v : result.values()) {
      System.out.println(v);
      System.out.println(v.type());
      System.out.println(v.toBuilder().build());
    }
  }
}
