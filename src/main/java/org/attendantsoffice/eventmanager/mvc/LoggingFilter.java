package org.attendantsoffice.eventmanager.mvc;

import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.io.IOException;
import java.time.Clock;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Login the incoming requests. This should be ordered immediately after the {@code RequestIdFilter}, which assigns the
 * requestId
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingFilter.class);
    private final Clock clock;

    public LoggingFilter(Clock clock) {
        this.clock = clock;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logRequestStart(request);

        long startTime = clock.instant().toEpochMilli();

        try {
            filterChain.doFilter(request, response);
        } finally {
            Long endTime = clock.instant().toEpochMilli();
            logRequestEnd(request, response, startTime, endTime);
        }
    }

    private void logRequestStart(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(generateRequestBasicInformation(request));
        builder.append(" started");

        LOG.info(builder.toString());
    }

    private void logRequestEnd(HttpServletRequest request,
            HttpServletResponse response,
            long startTime,
            long endTime) {
        int responseStatus = response.getStatus();

        StringBuilder builder = new StringBuilder();

        builder.append(generateRequestBasicInformation(request)).append(" ");
        builder.append(responseStatus);
        builder.append(" (").append(endTime - startTime).append("ms)");

        String message = builder.toString();

        if (responseStatus == HttpStatus.UNAUTHORIZED.value() || responseStatus == HttpStatus.FORBIDDEN.value()) {
            LOG.warn(message);
        } else if (responseStatus >= HttpStatus.BAD_REQUEST.value()) {
            LOG.error(message);
        } else {
            LOG.info(message);
        }
    }

    private String generateRequestBasicInformation(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getMethod()).append(" ");
        builder.append(ObjectUtils.firstNonNull(
                trimToNull(request.getServletPath()), trimToNull(request.getPathInfo())));
        if (request.getQueryString() != null) {
            builder.append('?').append(request.getQueryString());
        }
        return builder.toString();
    }
}
