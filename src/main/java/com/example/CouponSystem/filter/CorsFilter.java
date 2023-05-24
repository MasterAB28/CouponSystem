package com.example.CouponSystem.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class CorsFilter extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*"); // allow access to all controller paths ("/auth",  ...)
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, DELETE, POST, PUT, HEAD"); // allow methods
        response.setHeader("Access-Control-Allow-Headers", "authorization, Origin, Accept, content-type, Access-Control-Request-Method, Access-Control-Request-Headers");
        if(request.getMethod().equals("OPTIONS"))
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        else
            filterChain.doFilter(request, response);
    }
}
