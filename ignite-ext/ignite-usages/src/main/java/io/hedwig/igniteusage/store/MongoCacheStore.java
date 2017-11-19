package io.hedwig.igniteusage.store;

import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

/**
 * 1. author: patrick
 */
public class MongoCacheStore extends CacheStoreAdapter<String, String> {

  private static Logger logger = LoggerFactory.getLogger(MongoCacheStore.class);
  private Map<String, String> storeLocal = new ConcurrentHashMap<>();

  @Override
  public String load(String key) throws CacheLoaderException {
    logger.info(">>> Getting Value From Cache Store:" + key);
    return storeLocal.get("key");
  }

  @Override
  public void write(Cache.Entry<? extends String, ? extends String> entry)
      throws CacheWriterException {
    logger.info("put in to local store");
    storeLocal.put(entry.getKey(), entry.getValue());

  }

  @Override
  public void delete(Object key) throws CacheWriterException {
    logger.info("remove key from local store");
    storeLocal.remove(key);
  }
}
