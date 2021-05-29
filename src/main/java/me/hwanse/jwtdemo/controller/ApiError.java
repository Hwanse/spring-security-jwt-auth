package me.hwanse.jwtdemo.controller;

import org.springframework.core.style.ToStringCreator;
import org.springframework.http.HttpStatus;

public class ApiError {

  private final int statusCode;

  private final String message;

  ApiError(HttpStatus httpStatus, Throwable throwable) {
    this(httpStatus, throwable.getMessage());
  }

  ApiError(HttpStatus httpStatus, String message) {
    this.statusCode = httpStatus.value();
    this.message = message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return new ToStringCreator(this)
      .append("statusCode", statusCode)
      .append("message", message)
      .toString();
  }
}
