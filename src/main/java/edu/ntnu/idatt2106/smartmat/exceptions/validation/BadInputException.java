package edu.ntnu.idatt2106.smartmat.exceptions.validation;

/**
 * Exception thrown when the user inputs invalid data.
 * @author Callum G.
 * @version 1.0 - 18.04.2023
 */
public class BadInputException extends Exception {

  /**
   * Constructor for BadInputException.
   * @param message The message to be displayed.
   */
  public BadInputException(String message) {
    super(message);
  }

  /**
   * The message to be displayed when the exception is thrown.
   * Has a default message: 'Dårlig input data.'.
   */
  public BadInputException() {
    super("Dårlig input data.");
  }
}
