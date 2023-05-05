package edu.ntnu.idatt2106.smartmat.service.user;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Class for hashing and checking passwords.
 * @author Thomas S.
 * @version 1.0 - 17.4.2023
 */
public class PasswordService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PasswordService.class);

  /**
   * Method to hash a password.
   * @param password the password to hash.
   * @return the hashed password.
   * @throws NullPointerException if the password is null.
   * @throws IllegalArgumentException if the password is empty.
   */
  public static String hashPassword(@NonNull String password) {
    if (password.isBlank()) {
      throw new IllegalArgumentException("Passord kan ikke være blankt.");
    }
    String salt = BCrypt.gensalt(); // generate a random salt value
    return salt + ":" + BCrypt.hashpw(password, salt);
  }

  /**
   * Method to check a password.
   * @param password the password to check.
   * @param hashedPassword the hashed password to check against.
   * @return true if the password is correct.
   * @throws NullPointerException if the password or hashed password is null.
   * @throws IllegalArgumentException if the password or hashed password is empty.
   */
  public static boolean checkPassword(@NonNull String password, @NonNull String hashedPassword) {
    if (password.isBlank() || hashedPassword.isBlank()) {
      throw new IllegalArgumentException("Kan ikke sjekke blankt passord.");
    }
    LOGGER.info(password + " -> " + hashedPassword);
    String[] parts = hashedPassword.split(":");
    String salt = parts[0];
    String hashedPasswordFromDB = parts[1];
    String hashedPasswordToCheck = BCrypt.hashpw(password, salt);
    LOGGER.info(hashedPasswordToCheck + " == " + hashedPasswordFromDB);
    return hashedPasswordFromDB.equals(hashedPasswordToCheck);
  }
}
