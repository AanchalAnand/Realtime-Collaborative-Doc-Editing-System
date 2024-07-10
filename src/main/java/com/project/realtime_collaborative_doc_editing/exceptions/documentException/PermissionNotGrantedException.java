package com.project.realtime_collaborative_doc_editing.exceptions.documentException;

import org.springframework.http.HttpStatus;

public class PermissionNotGrantedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final HttpStatus httpStatus;

  public PermissionNotGrantedException(HttpStatus httpStatus) {
    super();
    this.httpStatus = httpStatus;
  }

  public PermissionNotGrantedException(String msg, HttpStatus httpStatus) {
    super(msg);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

}