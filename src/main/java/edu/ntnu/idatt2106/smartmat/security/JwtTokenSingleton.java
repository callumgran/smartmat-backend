package edu.ntnu.idatt2106.smartmat.security;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Singleton class for the JWT token secret.
 * @author Callum G.
 * @version 1.0 - 17.04.2023
 */
public class JwtTokenSingleton {

  private static JwtTokenSingleton instance = null;

  private final String jwtTokenSecret;

  private JwtTokenSingleton() {
    jwtTokenSecret = Dotenv.load().get("JWT_TOKEN_SECRET");
  }

  /**
   * Get the instance of the singleton.
   * @return The instance of the singleton.
   */
  public static synchronized JwtTokenSingleton getInstance() {
    if (instance == null) instance = new JwtTokenSingleton();

    return instance;
  }

  /**
   * Get the JWT token secret.
   * @return The JWT token secret.
   */
  public String getJwtTokenSecret() {
    return jwtTokenSecret;
  }
}
