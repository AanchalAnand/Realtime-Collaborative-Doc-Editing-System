package com.project.realtime_collaborative_doc_editing.config;

import com.project.realtime_collaborative_doc_editing.exceptions.documentException.RequestFilterException;
import com.project.realtime_collaborative_doc_editing.repository.UserRepository;
import com.project.realtime_collaborative_doc_editing.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {


    //Verify whether request has Authorization header and it has Bearer in it
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String email;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    //Extract jwt from the Authorization
    jwt = authHeader.substring(7);
    //Verify whether user is present in db
    //Verify whether token is valid
    email = jwtService.extractUsername(jwt);
    //If user is present and no authentication object in securityContext
    if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
      //If valid set to security context holder
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
      );
      authToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
      );
      SecurityContextHolder.getContext().setAuthentication(authToken);
    }
    filterChain.doFilter(request, response);
  }

  private HttpServletResponse getHttpServletResponse(ServletResponse response) {
    if (!(response instanceof HttpServletResponse)) {
      throw new RequestFilterException("Expecting an HTTP request", HttpStatus.FORBIDDEN);
    }
    return (HttpServletResponse) response;
  }

  private HttpServletRequest getHttpServletRequest(ServletRequest request) {
    if (!(request instanceof HttpServletRequest)) {
      throw new RequestFilterException("Expecting an HTTP request", HttpStatus.FORBIDDEN);
    }
    return (HttpServletRequest) request;
  }

  //Verify if it is whitelisted path and if yes dont do anything
  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
    return request.getServletPath().contains("/user/v1/auth");
  }

}