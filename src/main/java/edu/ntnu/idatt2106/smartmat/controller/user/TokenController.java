package edu.ntnu.idatt2106.smartmat.controller.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import edu.ntnu.idatt2106.smartmat.dto.user.AuthenticateDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.WrongPasswordException;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.JwtTokenSingleton;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.annotations.Operation;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for generating JWT tokens.
 * Based on the TokenController from the IDATT2105 project.
 * @author Thomas S., Callum G.
 * @version 1.1 - 17.04.2023
 */
@RestController
@RequestMapping(value = "/api/v1/public/token")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class TokenController {

  private final UserService userService;

  private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);

  private static final Duration JWT_TOKEN_VALIDITY = Duration.ofMinutes(
    Dotenv.load().get("JWT_TOKEN_VALIDITY") != null
      ? Long.parseLong(Dotenv.load().get("JWT_TOKEN_VALIDITY"))
      : 240
  );

  /**
   * Generate a JWT token for the given user.
   * @param authenticate The user to generate a token for.
   * @return The generated token.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws WrongPasswordException if the user credentials are wrong.
   */
  @PostMapping(value = "")
  @Operation(
    summary = "Generate a JWT token for the given user.",
    description = "Generate a JWT token for the given user. The token is valid for 30 minutes or the amount of time specified in the JWT_TOKEN_VALIDITY environment variable."
  )
  @ResponseStatus(value = HttpStatus.CREATED)
  public String generateToken(@RequestBody AuthenticateDTO authenticate)
    throws UserDoesNotExistsException, WrongPasswordException, ResponseStatusException {
    LOGGER.info("Authenticating user: {}", authenticate.getUsername());

    if (userService.authenticateUser(authenticate.getUsername(), authenticate.getPassword())) {
      LOGGER.info("User authenticated: {}", authenticate.getUsername());
      User user = userService.getUserByUsername(authenticate.getUsername());
      return generateToken(user);
    }

    LOGGER.info("Wrong credentials: {}", authenticate.getUsername());
    throw new WrongPasswordException("Feil brukernavn eller passord...");
  }

  /**
   * Generate a JWT token for the given user.
   * @param userId The user to generate a token for.
   * @return The generated token.
   */
  public String generateToken(final User user) {
    final Instant now = Instant.now();
    final Algorithm hmac512 = Algorithm.HMAC512(
      JwtTokenSingleton.getInstance().getJwtTokenSecret()
    );

    return JWT
      .create()
      .withSubject(user.getUsername())
      .withIssuer("idatt2106_project_smartmat")
      .withClaim("role", user.getRole().name())
      .withIssuedAt(now)
      .withExpiresAt(now.plusMillis(JWT_TOKEN_VALIDITY.toMillis()))
      .sign(hmac512);
  }
}
