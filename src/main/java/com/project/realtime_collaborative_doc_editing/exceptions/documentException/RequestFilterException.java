package com.project.realtime_collaborative_doc_editing.exceptions.documentException;

import org.springframework.http.HttpStatus;

public class RequestFilterException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private final HttpStatus httpStatus;

  public RequestFilterException(HttpStatus httpStatus) {
    super();
    this.httpStatus = httpStatus;
  }
  public RequestFilterException(String msg, HttpStatus httpStatus) {
    super(msg);
    this.httpStatus = httpStatus;
  }

  public RequestFilterException(String msg, Throwable cause, HttpStatus httpStatus) {
    super(msg, cause);
    this.httpStatus = httpStatus;
  }

  public RequestFilterException(Throwable cause, HttpStatus httpStatus) {
    super(cause);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}