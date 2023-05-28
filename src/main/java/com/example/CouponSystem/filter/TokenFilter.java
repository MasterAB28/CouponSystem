package com.example.CouponSystem.filter;


import com.example.CouponSystem.login.LoginParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Component
@Order(2)
public class TokenFilter extends OncePerRequestFilter {
    @Autowired
    private HashMap<String, LoginParameters> sessions;




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            String token=request.getHeader("authorization");
            if (sessions.get(token.replace("Bearer ","")) != null) {
                sessions.get(token.replace("Bearer ","")).setCalendar(Calendar.getInstance());
                filterChain.doFilter(request, response);
            }
            else {
                response.setStatus(401);
                response.getWriter().write("Your connection has been disconnected, please log in!");
            }
        }catch (Exception e){
            response.setStatus(403);
            response.getWriter().write(e.getMessage());
        }
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)  {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        List<String> excludeUrlPatterns = List.of(
                "/auth/login",
                "/auth/logout"
        );
        return excludeUrlPatterns
                .stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }


}