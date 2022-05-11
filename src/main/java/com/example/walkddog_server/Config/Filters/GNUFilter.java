package com.example.walkddog_server.Config.Filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class GNUFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String headerName = "X-Clacks-Overhead";
        final String headerContent = "GNU Terry Pratchett";
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader(headerName, headerContent);
        chain.doFilter(request, response);
    }
}
