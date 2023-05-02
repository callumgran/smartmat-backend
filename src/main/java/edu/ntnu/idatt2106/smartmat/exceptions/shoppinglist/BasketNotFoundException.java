package edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist;

/**
 * Exception for when a shopping list item is not found.
 * @author Callum G.
 * @version 1.0 - 02.05.2023.
 */
public class BasketNotFoundException extends Exception {

  /**
   * Constructor for the exception.
   * @param message the message being displayed.
   */

  public BasketNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructor for BasketNotFoundException.
   * Has a default message: "Handlekurven ble ikke funnet.".
   */

  public BasketNotFoundException() {
    super("Handlekurven ble ikke funnet.");
  }
}
