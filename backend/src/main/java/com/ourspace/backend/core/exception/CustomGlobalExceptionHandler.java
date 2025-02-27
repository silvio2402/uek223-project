package com.ourspace.backend.core.exception;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

/**
 * Global exception handler for the application.
 */
@RestControllerAdvice
@AllArgsConstructor
public class CustomGlobalExceptionHandler {

  /**
   * Handles MethodArgumentNotValidException.
   * This exception is thrown when argument annotated with @Valid failed
   * validation.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseError handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
    return new ResponseError().setTimeStamp(LocalDate.now())
        .setErrors(ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(FieldError::getField,
                DefaultMessageSourceResolvable::getDefaultMessage)))
        .build();
  }

  /**
   * Handles NoSuchElementException.
   * This exception is thrown when an element is not found.
   */
  @ExceptionHandler({ NoSuchElementException.class })
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseError handleNoSuchElement() {
    Map<String, String> errors = new HashMap<>();
    errors.put("element", "Element was not found");
    return new ResponseError().setTimeStamp(LocalDate.now())
        .setErrors(errors)
        .build();
  }

  /**
   * Handles UsernameNotFoundException.
   * This exception is thrown when a username is not found.
   */
  @ExceptionHandler({ UsernameNotFoundException.class })
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseError handleUsernameNotFound(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("username", String.format("Email %s was not found", e.getMessage()));
    return new ResponseError().setTimeStamp(LocalDate.now())
        .setErrors(errors)
        .build();
  }

  /**
   * Handles HttpMessageNotReadableException.
   * This exception is thrown when the HttpMessage is not readable.
   */
  @ExceptionHandler({ HttpMessageNotReadableException.class })
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseError handleHttp(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("status", e.getMessage());
    return new ResponseError().setTimeStamp(LocalDate.now())
        .setErrors(errors)
        .build();
  }

  /**
   * Handles IOException.
   * This exception is thrown when an I/O exception occurs.
   */
  @ExceptionHandler({ IOException.class })
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseError handleIOException(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("ioException", e.getMessage());
    return new ResponseError().setTimeStamp(LocalDate.now())
        .setErrors(errors)
        .build();
  }

  /**
   * Handles AccessDeniedException.
   * This exception is thrown when access to a resource is denied.
   */
  @ExceptionHandler({ AccessDeniedException.class })
  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  public ResponseError handleAccessDeneidException(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("accessDeneidException", e.getMessage());
    return new ResponseError().setTimeStamp(LocalDate.now())
        .setErrors(errors)
        .build();
  }

  /**
   * Handles AuthenticationException.
   * This exception is thrown when authentication fails.
   */
  @ExceptionHandler({ AuthenticationException.class })
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public ResponseError handleAuthenticationException(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("authenticationException", e.getMessage());
    return new ResponseError().setTimeStamp(LocalDate.now())
        .setErrors(errors)
        .build();
  }

  /**
   * Handles RuntimeException.
   * This exception is thrown when a runtime exception occurs.
   */
  @ExceptionHandler({ RuntimeException.class })
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseError handleRuntimeException(Throwable e) {
    Map<String, String> errors = new HashMap<>();
    errors.put("runtimeException", e.getMessage());
    return new ResponseError().setTimeStamp(LocalDate.now())
        .setErrors(errors)
        .build();
  }

}
