package edu.ntnu.idatt2106.smartmat.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter for validating JWT tokens.
 * If a valid token is found, the user is authenticated.
 * If an invalid token is found, the user is unauthenticated.
 * If no token is found, the user is unauthenticated.
 * Based on the JwtAuthenticationFilter from the IDATT2105 project.
 * @author Thomas S. & Callum G.
 * @version 1.0 - 17.04.2023
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final Logger LOGGER = LogManager.getLogger(JwtAuthenticationFilter.class);

  /**
   * Override function for doing an internal filter on a request.
   * @param request The request to filter.
   * @param response The response to filter.
   * @param filterChain The filter chain to filter.
   * @throws ServletException If an error occurs when filtering.
   * @throws IOException If an error occurs when filtering.
   */
  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    // if Bearer auth header does not exist, continue with unauthenticated user context
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    // if Bearer auth header exists, validate token, and extract userId from token.
    // Note that we have added userId as subject to the token when it is generated
    // Note also that the token comes in this format 'Bearer token'
    String jwtToken = authHeader.substring(7);
    final String[] vals = validateJwtTokenAndGetUsername(jwtToken);
    if (vals == null) {
      // validation failed or token expired
      filterChain.doFilter(request, response);
      return;
    }
    final String username = vals[0];
    final UserRole role = UserRole.valueOf(vals[1]);

    final String grantedRole = role.name();

    LOGGER.info("User {} is authenticated with role {}", username, grantedRole);
    //Add user details to the authentication context
    SecurityContextHolder
      .getContext()
      .setAuthentication(
        new UsernamePasswordAuthenticationToken(
          new Auth(username, role),
          null,
          List.of(new SimpleGrantedAuthority(grantedRole))
        )
      );

    // then, continue with authenticated user context
    filterChain.doFilter(request, response);
  }

  /**
   * Validates a JWT token and returns the username.
   * @param token The token to validate.
   * @return The username of the user.
   */
  public String[] validateJwtTokenAndGetUsername(final String token) {
    try {
      final Algorithm hmac512 = Algorithm.HMAC512(
        JwtTokenSingleton.getInstance().getJwtTokenSecret()
      );
      final JWTVerifier verifier = JWT.require(hmac512).build();
      String[] vals = new String[2];
      vals[0] = verifier.verify(token).getSubject();
      vals[1] = verifier.verify(token).getClaim("role").asString();
      return vals;
    } catch (final JWTVerificationException verificationEx) {
      LOGGER.warn("token is invalid: {}", verificationEx.getMessage());
      return null;
    }
  }
}
