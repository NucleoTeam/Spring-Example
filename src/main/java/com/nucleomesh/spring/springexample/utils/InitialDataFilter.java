package com.nucleomesh.spring.springexample.utils;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.TreeMap;

@Component
@Order(0)
public class InitialDataFilter implements Filter {
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        TreeMap<String, Object> data = InitialData.exec(req);
        request.setAttribute("data", data);
        chain.doFilter(request, response);
    }
}
