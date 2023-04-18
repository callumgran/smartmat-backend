package edu.ntnu.idatt2106.smartmat.exceptions;

/**
 * Exception thrown when a user does not have permission to perform an action.
 * @author Callum G.
 * @version 1.0 - 18.04.2023
 */
public class PermissionDeniedException extends Exception {

  /**
   * Constructor for PermissionDeniedException.
   * @param message The message to be displayed.
   */
  public PermissionDeniedException(String message) {
    super(message);
  }

  /**
   * Constructor for PermissionDeniedException.
   * Has a default message: 'Ingen tilgang'.
   */
  public PermissionDeniedException() {
    super("Ingen tilgang.");
  }
}
