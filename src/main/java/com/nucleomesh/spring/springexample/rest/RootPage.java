package com.nucleomesh.spring.springexample.rest;

import com.nucleomesh.spring.springexample.services.NucleoMeshService;
import com.synload.nucleo.event.NucleoData;
import com.synload.nucleo.event.NucleoResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.TreeMap;

@RestController
public class RootPage {
  @Autowired
  NucleoMeshService meshService;

  Logger logger = LoggerFactory.getLogger(RootPage.class);

  @GetMapping("/")
  public DeferredResult<ResponseEntity<?>> getRoot(){
    TreeMap<String, Object> data = new TreeMap<String, Object>();
    data.put("wow", "works?");
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
    meshService.getMesh().call("information.hits", data, new NucleoResponder(){
      @Override
      public void run(NucleoData data) {
        result.setResult(ResponseEntity.ok(data.getObjects()));
      }
    });
    return result;
  }
  @GetMapping("/c")
  public DeferredResult<ResponseEntity<?>> getChange(){
    TreeMap<String, Object> data = new TreeMap<String, Object>();
    data.put("wow", "works?");
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
    meshService.getMesh().call("information.changeme", data, new NucleoResponder(){
      @Override
      public void run(NucleoData data) {
        result.setResult(ResponseEntity.ok((String)data.getObjects().get("wow")));
      }
    });
    return result;
  }
  @GetMapping("/chain")
  public DeferredResult<ResponseEntity<?>> chainBreak(){
    TreeMap<String, Object> data = new TreeMap<String, Object>();
    data.put("stop", "chain");
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
    meshService.getMesh().call("information.changeme", data, new NucleoResponder(){
      @Override
      public void run(NucleoData data) {
        result.setResult(ResponseEntity.ok(data));
      }
    });
    return result;
  }
}
