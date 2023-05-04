package edu.ntnu.idatt2106.smartmat.exceptions.user;

/**
 * Exception thrown when a bad password is given.
 * @author Callum G.
 * @version 1.0 - 04.05.2023
 */
public class WrongPasswordException extends Exception {

  /**
   * Constructor for WrongPasswordException.
   * @param message The message to be displayed.
   */
  public WrongPasswordException(String message) {
    super(message);
  }

  /**
   * Constructor for UserDoesNotExistsException.
   * Has a default message: 'Feil passord.'.
   */
  public WrongPasswordException() {
    super("Feil passord.");
  }
}
