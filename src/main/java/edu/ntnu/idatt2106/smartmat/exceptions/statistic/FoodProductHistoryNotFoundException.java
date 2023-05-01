package edu.ntnu.idatt2106.smartmat.exceptions.statistic;

/**
 * Exception thrown when a food product history is not found.
 * @author Callum G.
 * @version 1.0 - 29.04.2023
 */
public class FoodProductHistoryNotFoundException extends Exception {

  /**
   * Constructor for the exception.
   * @param message The message to be displayed.
   */
  public FoodProductHistoryNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructor for the exception.
   * Has a default message: "Mat produkt historikk ble ikke funnet."
   */
  public FoodProductHistoryNotFoundException() {
    super("Mat produkt historikk ble ikke funnet.");
  }
}
