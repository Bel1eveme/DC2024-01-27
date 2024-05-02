package com.poluectov.rvproject.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Slf4j
@Component
@Order(HIGHEST_PRECEDENCE)
public class LogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();

        log.info("┌───────────────────────────────────────────");
        log.info("├Request id={}: {} {}", requestId, request.getMethod(), request.getRequestURI());
        log.info("│");

        filterChain.doFilter(request, response);

        log.info("│");
        log.info("├Response id={}: {}", requestId, response.getStatus());
        log.info("└───────────────────────────────────────────");
    }
}
