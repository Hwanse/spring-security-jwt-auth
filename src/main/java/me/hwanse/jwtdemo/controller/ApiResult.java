package me.hwanse.jwtdemo.controller;

import org.springframework.core.style.DefaultToStringStyler;
import org.springframework.core.style.ToStringCreator;
import org.springframework.core.style.ToStringStyler;
import org.springframework.http.HttpStatus;

public class ApiResult<T> {

  private final boolean success;

  private final T response;

  private final ApiError error;

  ApiResult(boolean success, T response, ApiError apiError) {
    this.success = success;
    this.response = response;
    this.error = apiError;
  }

  public static <T> ApiResult<T> OK (T response) {
    return new ApiResult<T>(true, response, null);
  }

  public static ApiResult<?> ERROR(HttpStatus httpStatus, Throwable throwable) {
    return new ApiResult<>(false, null, new ApiError(httpStatus, throwable));
  }

  public static ApiResult<?> ERROR(HttpStatus httpStatus, String errorMessage) {
    return new ApiResult<>(false, null, new ApiError(httpStatus, errorMessage));
  }

  public boolean isSuccess() {
    return success;
  }

  public T getResponse() {
    return response;
  }

  public ApiError getError() {
    return error;
  }

  @Override
  public String toString() {
    return new ToStringCreator(this)
      .append("success", success)
      .append("response", response)
      .toString();
  }
}
