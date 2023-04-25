package edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist;

/**
 * Exception for when a shopping list item is not found.
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */
public class ShoppingListItemNotFoundException extends Exception {

  /**
   * Constructor for the exception.
   * @param message the message being displayed.
   */

  public ShoppingListItemNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructor for ShoppingListItemNotFoundException.
   * Has a default message: "Matvaren på handlelisten ble ikke funnet.".
   */

  public ShoppingListItemNotFoundException() {
    super("Matvaren på handlelisten ble ikke funnet.");
  }
}
