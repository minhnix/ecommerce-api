package com.nix.ecommerceapi.exception;

import com.nix.ecommerceapi.model.response.ExceptionMessage;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ExceptionMessage handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, WebRequest request) {
        return new ExceptionMessage(e.getMessage(), getRequestUri(request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> resp = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            resp.put(fieldName, message);
        });
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        return new ExceptionMessage(e.getMessage(), getRequestUri(request));
    }

    @ExceptionHandler(AuthFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionMessage handleAuthenticationException(AuthFailureException e, WebRequest request) {
        return new ExceptionMessage(e.getMessage(), getRequestUri(request));
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessage handleForbiddenException(ForbiddenException e, WebRequest request) {
        return new ExceptionMessage(e.getMessage(), getRequestUri(request));
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleDuplicateEntityException(DuplicateEntityException e, WebRequest request) {
        return new ExceptionMessage(e.getMessage(), getRequestUri(request));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleResourceNotFoundException(NotFoundException e, WebRequest request) {
        return new ExceptionMessage(e.getMessage(), getRequestUri(request));
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleBadRequestException(BadRequestException e, WebRequest request) {
        return new ExceptionMessage(e.getMessage(), getRequestUri(request));
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleAppException(AppException e, WebRequest request) {
        return new ExceptionMessage(e.getMessage(), getRequestUri(request));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e, WebRequest request) {
        return new ExceptionMessage(e.getMessage(), getRequestUri(request));
    }


    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionMessage handleExpiredJwtException(ExpiredJwtException e, WebRequest request) {
        return new ExceptionMessage(e.getMessage(), getRequestUri(request));
    }

    private String getRequestUri(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}
