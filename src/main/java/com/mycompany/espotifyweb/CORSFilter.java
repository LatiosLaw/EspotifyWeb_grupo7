package com.mycompany.espotifyweb;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    httpResponse.setHeader("Access-Control-Allow-Origin", "*");
    httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

    if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) request).getMethod())) {
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return;
    }

    chain.doFilter(request, response);
}

    @Override
    public void destroy() {}
}