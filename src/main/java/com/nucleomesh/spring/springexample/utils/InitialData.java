package com.nucleomesh.spring.springexample.utils;

import com.synload.nucleo.event.NucleoData;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.TreeMap;

public class InitialData {
  public static TreeMap<String, Object> exec(HttpServletRequest request){
    TreeMap<String, Object> data = new TreeMap<>();
    Cookie c = WebUtils.getCookie(request, "session");
    if(c!=null){
      data.put("session", c.getValue());
    }
    data.put("ip", request.getRemoteAddr());
    data.put("browser", request.getHeader("user-agent"));
    return data;
  }
}
