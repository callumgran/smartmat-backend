package edu.ntnu.idatt2106.smartmat.exceptions;

/**
 * Exception thrown when an error occurs while communicating with the database.
 * @author Callum G.
 * @version 1.0 - 17.04.2023
 */
public class DatabaseException extends Exception {

  /**
   * Constructor for DatabaseException.
   * @param message The message to be displayed.
   */
  public DatabaseException(String message) {
    super(message);
  }

  /**
   * Constructor for DatabaseException.
   * @param cause The cause of the exception.
   */
  public DatabaseException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructor for DatabaseException.
   * Has a default message.
   */
  public DatabaseException() {
    super("An error occurred while communicating with the database.");
  }
}
