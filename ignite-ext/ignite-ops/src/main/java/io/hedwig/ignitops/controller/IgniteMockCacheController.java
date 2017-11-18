package io.hedwig.ignitops.controller;


import com.wanda.drcard.entity.tasks.TaskProcessor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 1. author: patrick 2. address: github.com/ideafortester
 */
@Controller
public class IgniteMockCacheController {

  @RequestMapping(value = "/mock"
      , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
      , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  public ResponseEntity mockCacheData(
  ) {

    try {
      TaskProcessor.execute(null);
      return ResponseEntity.ok("ok");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return ResponseEntity.ok("failed");
  }
}
