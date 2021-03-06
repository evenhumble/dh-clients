package io.hedwig.ignitops.controller;

import com.wanda.drcard.entity.Cardlist;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import io.hedwig.ignitops.utils.client.IgniteWrapper;

/**
 * 1. author: patrick
 * 2. address: github.com/ideafortester
 */

@RestController
public class IgniteCacheController {

  @RequestMapping(value = "/cache/{cache}/key/{key}"
      , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
      , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  public ResponseEntity getCacheValue(@PathVariable String cache,
                                      @PathVariable String key
  ) {

    Ignite ignite = IgniteWrapper.getIgnite();
    IgniteCache<String, Cardlist> cacheValue =
        ignite.getOrCreateCache(cache);

    return ResponseEntity.ok(cacheValue.get(key));

  }

  @RequestMapping(value = "/cache/{cache}"
      , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
      , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      method = RequestMethod.DELETE
  )
  public ResponseEntity destroyCacheValue(@PathVariable String cache
  ) {

    Ignite ignite = IgniteWrapper.getIgnite();
    ignite.destroyCache(cache);
    return ResponseEntity.ok(String.format("destroy cache %s", cache));

  }

  @RequestMapping(value = "/cache/{cache}/key/{key}"
      , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
      , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
      , method = RequestMethod.PUT
  )
  public ResponseEntity updateCacheValue(@PathVariable String cache,
                                         @PathVariable String key
  ) {

    Ignite ignite = IgniteWrapper.getIgnite();
    IgniteCache<String, Cardlist> cacheValue = ignite.getOrCreateCache(cache);
    Cardlist cardlist = cacheValue.get(key);
    if(cardlist.getUsagestatus()==1){
      cardlist.setUsagestatus(0);
    }else{
      cardlist.setUsagestatus(1);
    }
    if(cardlist.getCardstatus()==1){
      cardlist.setCardstatus(0);
    }else {
      cardlist.setCardstatus(1);
    }
    cardlist.setIgniteUpdateTime(new Date().getTime());
    cacheValue.put(key, cardlist);
    return ResponseEntity.ok(cardlist);

  }



}
