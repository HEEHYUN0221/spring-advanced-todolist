package com.example.todolist_advanced.common.config.filter;

import com.example.todolist_advanced.common.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j(topic = "JwtFilter")
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    private static final String[] WHITE_LIST = {"/", "/users/signup", "/login", "/logout"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpRequest.getRequestURI();
        String username = null;
        String jwt = null;

        String authorizationHeader = httpRequest.getHeader("Authorization");

        if(isWhiteList(requestURI)) {
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"JWT token required.\"}");
            return;
        }

        jwt = authorizationHeader.substring(7);

        Long userId = jwtUtil.extractUserId(jwt);

        if(!jwtUtil.validateToken(jwt)) {
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Invalid JWT token.\"}");
        }

        username = jwtUtil.extractUsername(jwt);

        if(requestURI.startsWith("/api/admin")) {
            if(jwtUtil.hasRole(jwt,"ADMIN")) {
                filterChain.doFilter(servletRequest,servletResponse);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Do not have access.");
            }
            return;
        }

        servletRequest.setAttribute("userName",username);
        servletRequest.setAttribute("userId",userId);

        filterChain.doFilter(servletRequest,servletResponse);

    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

}
