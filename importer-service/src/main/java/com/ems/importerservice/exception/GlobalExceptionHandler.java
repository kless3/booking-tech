package com.ems.importerservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ImportedEventNotFoundException.class)
    public ProblemDetail handleImportedEventNotFound(ImportedEventNotFoundException exception, HttpServletRequest request) {
        return problemDetail(HttpStatus.NOT_FOUND, "Imported event not found", exception.getMessage(), request);
    }

    @ExceptionHandler(UnsupportedEventSourceException.class)
    public ProblemDetail handleUnsupportedSource(UnsupportedEventSourceException exception, HttpServletRequest request) {
        return problemDetail(HttpStatus.BAD_REQUEST, "Unsupported event source", exception.getMessage(), request);
    }

    @ExceptionHandler(EventServiceUnavailableException.class)
    public ProblemDetail handleEventServiceUnavailable(EventServiceUnavailableException exception, HttpServletRequest request) {
        log.warn("Event Service interaction failed", exception);
        return problemDetail(HttpStatus.BAD_GATEWAY, "Event Service unavailable", "Unable to create imported event", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> errors = exception.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, this::safeMessage, (left, right) -> left));
        ProblemDetail problemDetail = problemDetail(HttpStatus.BAD_REQUEST, "Validation failed", "Request validation failed", request);
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadableMessage(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return problemDetail(HttpStatus.BAD_REQUEST, "Malformed request", "Request body is missing or malformed", request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException exception, HttpServletRequest request) {
        return problemDetail(HttpStatus.BAD_REQUEST, "Invalid request parameter", exception.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception exception, HttpServletRequest request) {
        log.error("Unhandled exception while processing {} {}", request.getMethod(), request.getRequestURI(), exception);
        return problemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "Unexpected error occurred", request);
    }

    private ProblemDetail problemDetail(HttpStatus status, String title, String detail, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return problemDetail;
    }

    private String safeMessage(FieldError fieldError) {
        return fieldError.getDefaultMessage() == null ? "Invalid value" : fieldError.getDefaultMessage();
    }
}
