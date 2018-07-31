package org.attendantsoffice.eventmanager.mvc;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Assign a request id at the point the request is received.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class RequestIdFilter extends OncePerRequestFilter {
    public static final String REQUEST_ID_MDC_KEY = "requestUUID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        MDC.put(REQUEST_ID_MDC_KEY, UUID.randomUUID());

        try {
            filterChain.doFilter(request, response);
        } finally {
            response.addHeader(REQUEST_ID_MDC_KEY, String.valueOf(MDC.get(REQUEST_ID_MDC_KEY)));
            MDC.remove(REQUEST_ID_MDC_KEY);
        }
    }

}
