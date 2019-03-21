package com.nucleomesh.spring.springexample.rest;

import com.nucleomesh.spring.springexample.services.NucleoMeshService;
import com.synload.nucleo.event.NucleoData;
import com.synload.nucleo.event.NucleoResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.TreeMap;

@RestController
public class RootPage {
  @Autowired
  NucleoMeshService meshService;

  Logger logger = LoggerFactory.getLogger(RootPage.class);

  @GetMapping("/")
  public String get(){
    TreeMap<String, Object> data = new TreeMap<String, Object>();
    data.put("wow", "works?");
    meshService.getMesh().call("info.hits", data, new NucleoResponder(){
      @Override
      public void run(NucleoData data) {
        logger.info((String)data.getObjects().get("wow"));
      }
    });
    return "test";
  }
}
