package edu.ntnu.idatt2106.smartmat.exceptions.user;

/**
 * Exception thrown when a user with a given username already exists.
 * @author Callum G.
 * @version 1.0 - 17.04.2023
 */
public class UsernameAlreadyExistsException extends Exception {

  /**
   * Constructor for UsernameAlreadyExistsException.
   * @param message The message to be displayed.
   */
  public UsernameAlreadyExistsException(String message) {
    super(message);
  }

  /**
   * Constructor for UsernameAlreadyExistsException.
   * Has a default message: 'En bruker med dette brukernavnet finnes allerede.'.
   */
  public UsernameAlreadyExistsException() {
    super("En bruker med dette brukernavnet finnes allerede.");
  }
}
