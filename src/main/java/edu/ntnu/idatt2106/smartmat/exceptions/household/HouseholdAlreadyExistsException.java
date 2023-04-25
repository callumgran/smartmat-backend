package edu.ntnu.idatt2106.smartmat.exceptions.household;

/**
 * Exception for when a household already exists.
 * @author Callum G.
 * @version 1.0 - 19.4.2023
 */
public class HouseholdAlreadyExistsException extends Exception {

  /**
   * Constructor for the exception.
   * @param message The message to be displayed.
   */
  public HouseholdAlreadyExistsException(String message) {
    super(message);
  }

  /**
   * Constructor for HouseholdAlreadyExistsException.
   * Has a default message: "Husstanden eksisterer allerede."
   */
  public HouseholdAlreadyExistsException() {
    super("Husstanden eksisterer allerede.");
  }
}
