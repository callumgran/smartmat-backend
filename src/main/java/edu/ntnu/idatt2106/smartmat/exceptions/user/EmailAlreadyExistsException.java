package edu.ntnu.idatt2106.smartmat.exceptions.user;

/**
 * Exception thrown when a user with a given email already exists.
 * @author Callum G.
 * @version 1.0 - 17.04.2023
 */
public class EmailAlreadyExistsException extends Exception {

  /**
   * Constructor for EmailAlreadyExistsException.
   * @param message The message to be displayed.
   */
  public EmailAlreadyExistsException(String message) {
    super(message);
  }

  /**
   * Constructor for EmailAlreadyExistsException.
   * Has a default message.
   */
  public EmailAlreadyExistsException() {
    super("Email already exists");
  }
}
