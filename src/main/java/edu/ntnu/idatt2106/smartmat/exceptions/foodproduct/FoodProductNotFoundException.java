package edu.ntnu.idatt2106.smartmat.exceptions.foodproduct;

/**
 * Exception thrown when a food product is not found.
 * @author Callum G.
 * @version 1.0 21.04.2023
 */
public class FoodProductNotFoundException extends Exception {

  /**
   * Constructor for the exception.
   * @param message The message to be displayed.
   */
  public FoodProductNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructor for the exception.
   * Has a default message: "Matvaren ble ikke funnet."
   */
  public FoodProductNotFoundException() {
    super("Matvaren ble ikke funnet.");
  }
}
