package org.attendantsoffice.eventmanager.mvc.error;

import java.time.Instant;

import org.attendantsoffice.eventmanager.mvc.RequestIdFilter;
import org.jboss.logging.MDC;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private final HttpStatus status;
    private final Instant timestamp;
    private final String requestUUID;
    private final String code;
    private final String message;
    private final String debugMessage;

    public ErrorResponse(HttpStatus status, Throwable ex) {
        this(status, "Unexpected error", ex);
    }

    public ErrorResponse(HttpStatus status, String message, Throwable ex) {
        this.status = status;
        this.timestamp = Instant.now();
        this.requestUUID = String.valueOf(MDC.get(RequestIdFilter.REQUEST_ID_MDC_KEY));
        this.code = ex.getClass().getSimpleName().replaceAll("Exception$", "");
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getRequestUUID() {
        return requestUUID;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }
}
