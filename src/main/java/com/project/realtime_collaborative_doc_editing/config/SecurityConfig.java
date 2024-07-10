package com.project.realtime_collaborative_doc_editing.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.project.realtime_collaborative_doc_editing.model.Permission.*;
import static com.project.realtime_collaborative_doc_editing.model.Role.ADMIN;
import static com.project.realtime_collaborative_doc_editing.model.Role.MEMBER;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {


  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(req ->
            req.requestMatchers(POST, "/user/v1/auth/register").permitAll() // Allow everyone to access the register endpoint
                .requestMatchers(POST, "/user/v1/auth/authenticate").permitAll() // // Allow everyone to access the login endpoint
                .requestMatchers(GET, "/test/**").permitAll() // to allow all the free test APIs
                .requestMatchers("/user/v1/management/**").hasAnyRole(ADMIN.name(), MEMBER.name())
                .requestMatchers(GET, "/user/v1/management/**")
                .hasAnyAuthority(ADMIN_READ.name(), MEMBER_READ.name())
                .anyRequest()
                .authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}

// bin/kafka-topics.sh --create --topic001 --bootstrap-server localhost:9092