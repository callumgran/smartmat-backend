package edu.ntnu.idatt2106.smartmat.service.user;

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
   */
  public static String hashPassword(String password) {
    String salt = BCrypt.gensalt(); // generate a random salt value
    return salt + ":" + BCrypt.hashpw(password, salt);
  }

  /**
   * Method to check a password.
   * @param password the password to check.
   * @param hashedPassword the hashed password to check against.
   * @return true if the password is correct.
   */
  public static boolean checkPassword(String password, String hashedPassword) {
    LOGGER.info(password + " -> " + hashedPassword);
    String[] parts = hashedPassword.split(":");
    String salt = parts[0];
    String hashedPasswordFromDB = parts[1];
    String hashedPasswordToCheck = BCrypt.hashpw(password, salt);
    LOGGER.info(hashedPasswordToCheck + " == " + hashedPasswordFromDB);
    return hashedPasswordFromDB.equals(hashedPasswordToCheck);
  }
}
