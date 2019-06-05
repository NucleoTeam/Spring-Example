package com.nucleomesh.spring.springexample.rest;

import com.nucleomesh.spring.springexample.services.NucleoMeshService;
import com.nucleomesh.spring.springexample.utils.InitialData;
import com.synload.nucleo.event.NucleoData;
import com.synload.nucleo.event.NucleoResponder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TreeMap;

@RestController
public class SessionHandling {

  @Autowired
  NucleoMeshService meshService;

  @PostMapping("/login")
  public DeferredResult<ResponseEntity<?>> login(@RequestParam("username") String username,
                                                 @RequestParam("password") String password,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
    TreeMap<String, Object> data = (TreeMap<String, Object>) request.getAttribute("data");
    data.put("username", username);
    data.put("password", password);
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
    meshService.getMesh().call(new String[]{"user.login", "session.create"}, data, new NucleoResponder() {
      @Override
      public void run(NucleoData data) {
        if (data.getObjects().containsKey("session")) {
          response.addCookie(new Cookie("session", (String) data.getObjects().get("session")));
        }
        result.setResult(ResponseEntity.ok(data));
      }
    });
    return result;
  }

  @GetMapping("/logout")
  public DeferredResult<ResponseEntity<?>> logout(HttpServletRequest request,
                                                  HttpServletResponse response) {
    TreeMap<String, Object> data = (TreeMap<String, Object>) request.getAttribute("data");
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
    meshService.getMesh().call(new String[]{"session.verify","session.destroy"}, data, new NucleoResponder() {
      @Override
      public void run(NucleoData data) {
        if (!data.getObjects().containsKey("session")) {
          Cookie cookie = new Cookie("session", "");
          cookie.setMaxAge(0);
          response.addCookie(cookie);
        }
        result.setResult(ResponseEntity.ok(data));
      }
    });
    return result;
  }
}
