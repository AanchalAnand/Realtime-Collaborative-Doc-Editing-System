package com.project.realtime_collaborative_doc_editing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {

  private String message;

  private Boolean success;

  private Object payload;

  private String statusCode;

}