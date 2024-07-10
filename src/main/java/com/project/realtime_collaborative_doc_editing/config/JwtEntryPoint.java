package com.project.realtime_collaborative_doc_editing.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import java.io.IOException;

public class JwtEntryPoint extends Http403ForbiddenEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException {
    super.commence(request, response, arg2);
    response.sendError(HttpServletResponse.SC_FORBIDDEN,"Access denied");
  }
}