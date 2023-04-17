package edu.ntnu.idatt2106.smartmat.security;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configures spring security
 * Sets up the authentication filter and the exception handler
 * Based on the SecurityConfig from the IDATT2105 project.
 *
 * @author Carl G, Thomas S., Callum G.
 * @version 1.0 - 17.04.2023
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

  /**
   * Permits all requests to `swagger/**` and `api/v1/public/**`.
   *
   * @param http HttpSecurity - http object to build configurations on
   * @throws Exception thrown if an error occurs when permitting requests
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .cors()
      .and()
      .csrf()
      .disable()
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers("/swagger/**", "/api/v1/public/**")
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
      .exceptionHandling()
      .authenticationEntryPoint((request, response, exception) ->
        setAccessHandlerResponse(
          response,
          "AuthenticationException",
          HttpServletResponse.SC_UNAUTHORIZED
        )
      );

    return http.build();
  }

  /**
   * Sets the response for the AuthenticationEntryPoint
   * Sets the message to be json object with the key "detail" and the value of the message
   * @param response HttpServletResponse - exception response to be sent to the client
   * @param message String - message in json to be sent to the client
   * @param status int - status code to be sent with the response
   * @throws IOException thrown if an error occurs when writing to the response
   */
  public void setAccessHandlerResponse(HttpServletResponse response, String message, int status)
    throws IOException {
    log.error("Sending error for invalid access and sending message: {}", message);
    response.setStatus(status);
    response.setHeader("Content-Type", "application/json");
    PrintWriter writer = response.getWriter();
    writer.print("{\"detail\": \"" + message + "\"}");
  }

  /**
   * Configures CORS
   * @return CorsConfigurationSource - cors configuration
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(List.of("http://localhost:5173"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
    config.setAllowCredentials(true);
    config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }
}
