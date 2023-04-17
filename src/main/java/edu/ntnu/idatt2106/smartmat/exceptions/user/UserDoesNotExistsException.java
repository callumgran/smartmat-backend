package edu.ntnu.idatt2106.smartmat.exceptions.user;

/**
 * Exception thrown when a user does not exist.
 * @author Callum G.
 * @version 1.0 - 17.04.2023
 */
public class UserDoesNotExistsException extends Exception {

  /**
   * Constructor for UserDoesNotExistsException.
   * @param message The message to be displayed.
   */
  public UserDoesNotExistsException(String message) {
    super(message);
  }

  /**
   * Constructor for UserDoesNotExistsException.
   * Has a default message.
   */
  public UserDoesNotExistsException() {
    super("User finnes ikke.");
  }
}
