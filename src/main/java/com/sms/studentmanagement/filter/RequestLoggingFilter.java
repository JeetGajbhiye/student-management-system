package com.sms.studentmanagement.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter that:
 * <ol>
 *   <li>Assigns a unique {@code X-Request-Id} to every incoming request.</li>
 *   <li>Puts the id into the SLF4J MDC so every log line carries the correlation id.</li>
 *   <li>Records basic request/response info for audit purposes.</li>
 * </ol>
 */
@Component
@Order(1)
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String MDC_KEY           = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        }

        MDC.put(MDC_KEY, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        long start = System.currentTimeMillis();
        try {
            log.info("--> {} {} [requestId={}]", request.getMethod(), request.getRequestURI(), requestId);
            chain.doFilter(request, response);
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            log.info("<-- {} {} {} {}ms [requestId={}]",
                    request.getMethod(), request.getRequestURI(),
                    response.getStatus(), elapsed, requestId);
            MDC.remove(MDC_KEY);
        }
    }
}
