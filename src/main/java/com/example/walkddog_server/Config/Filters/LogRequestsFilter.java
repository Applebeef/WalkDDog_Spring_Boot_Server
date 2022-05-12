package com.example.walkddog_server.Config.Filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
public class LogRequestsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Request: " + servletRequest.getRemoteAddr() + "\n" + servletRequest.getRemoteHost());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
