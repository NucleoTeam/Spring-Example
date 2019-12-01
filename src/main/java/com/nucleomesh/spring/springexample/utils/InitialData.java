package com.nucleomesh.spring.springexample.utils;

import com.synload.nucleo.data.NucleoObject;
import org.springframework.web.util.WebUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.TreeMap;

public class InitialData {
  public static NucleoObject exec(HttpServletRequest request){
    NucleoObject data = new NucleoObject();
    Cookie c = WebUtils.getCookie(request, "session");
    if(c!=null){
      data.set("session", c.getValue());
    }
    data.set("ip", request.getRemoteAddr());
    data.set("browser", request.getHeader("user-agent"));
    return data;
  }
}
