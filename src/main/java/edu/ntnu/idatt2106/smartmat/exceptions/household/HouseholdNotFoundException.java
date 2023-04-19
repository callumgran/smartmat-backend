package edu.ntnu.idatt2106.smartmat.exceptions.household;

/**
 * Exception for when a household is not found.
 * @author Callum G.
 * @version 1.0 - 19.4.2023
 */
public class HouseholdNotFoundException extends Exception {

  /**
   * Constructor for HouseholdNotFoundException.
   * @param message The message to be displayed.
   */
  public HouseholdNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructor for HouseholdNotFoundException.
   * Has a default message: "Husstanden ble ikke funnet."
   */
  public HouseholdNotFoundException() {
    super("Husstanden ble ikke funnet.");
  }
}
