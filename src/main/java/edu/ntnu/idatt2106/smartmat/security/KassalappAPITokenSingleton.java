package edu.ntnu.idatt2106.smartmat.security;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Singleton class for the Kassalapp API token.
 * @author Callum G.
 * @version 1.0 - 01.05.2023
 */
public class KassalappAPITokenSingleton {

  private static KassalappAPITokenSingleton instance = null;

  private final String apiToken;

  private KassalappAPITokenSingleton() {
    apiToken = Dotenv.load().get("KASSAL_API_KEY");
  }

  /**
   * Get the instance of the singleton.
   * @return The instance of the singleton.
   */
  public static synchronized KassalappAPITokenSingleton getInstance() {
    if (instance == null) instance = new KassalappAPITokenSingleton();

    return instance;
  }

  /**
   * Get the Kassalapp token.
   * @return The Kassalapp token.
   */
  public String getKassalappAPIToken() {
    return apiToken;
  }
}
