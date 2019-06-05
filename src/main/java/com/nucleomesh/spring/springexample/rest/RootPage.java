package com.nucleomesh.spring.springexample.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nucleomesh.spring.springexample.services.NucleoMeshService;
import com.nucleomesh.spring.springexample.utils.InitialData;
import com.synload.nucleo.event.NucleoData;
import com.synload.nucleo.event.NucleoResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        result.setResult(ResponseEntity.ok(data));
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
        result.setResult(ResponseEntity.ok(data));
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
  @PostMapping("/req")
  public DeferredResult<ResponseEntity<?>> request(@RequestParam("chains") String chains,
                                                   @RequestParam("objects") String objects,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response){
    TreeMap<String, Object> data = (TreeMap<String, Object>) request.getAttribute("data");
    try {
      data.putAll(new ObjectMapper().readValue(objects, TreeMap.class));
      DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
      meshService.getMesh().call(chains.split(","), data, new NucleoResponder() {
        @Override
        public void run(NucleoData data) {
          result.setResult(ResponseEntity.ok(data));
        }
      });
      return result;
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }
  @ExceptionHandler({Exception.class})
  public Object handleErrors() {
    return new Object[]{"error", "form incomplete"};
  }
}
