package edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist;

/**
 * Exception for when a shopping list is not found.
 * @author Tobias O.
 * @version 1.0 - 21.04.2023.
 */
public class ShoppingListNotFoundException extends Exception {

  /**
   * Constructor for the exception.
   * @param message the message to be displayed.
   */
  public ShoppingListNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructor for ShoppingListNotFoundException.
   * Has a default message: "Handlelisten ble ikke funnet.".
   */
  public ShoppingListNotFoundException() {
    super("Handlelisten ble ikke funnet.");
  }
}
