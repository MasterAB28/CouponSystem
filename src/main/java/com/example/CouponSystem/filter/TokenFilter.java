package com.example.CouponSystem.filter;

import com.example.CouponSystem.CouponSystemApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Order(2)
public class TokenFilter extends OncePerRequestFilter {
    @Autowired
    private ApplicationContext ctx;




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        CouponSystemApplication spa=ctx.getBean(CouponSystemApplication.class);
        try {
            String token=request.getHeader("authorization");
            if (spa.sessions().get(token.replace("Bearer ","")) != null) {
                filterChain.doFilter(request, response);
            }
            else {
                response.setStatus(401);
                response.getWriter().write("Your connection has been disconnected, please log in!");
            }
        }catch (Exception e){
            response.setStatus(401);
            response.getWriter().write("invalid,please log in!");
        }
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)  {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        List<String> excludeUrlPatterns = List.of(
                "/auth/login",
                "/"
        );
        return excludeUrlPatterns
                .stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }


}