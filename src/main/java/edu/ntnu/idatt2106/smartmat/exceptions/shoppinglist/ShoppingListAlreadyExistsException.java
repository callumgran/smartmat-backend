package edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist;

/**
 * Exception for when a shopping list already exists.
 * @author Tobias O.
 * @version 1.0 - 21.04.2023.
 */
public class ShoppingListAlreadyExistsException extends Exception {

  /**
   * Constructor for the exception.
   * @param message the message to be displayed.
   */
  public ShoppingListAlreadyExistsException(String message) {
    super(message);
  }

  /**
   * Constructor for ShoppingListAlreadyExistsException.
   * Has a default message: "Handlelisten eksisterer allerede!".
   */
  public ShoppingListAlreadyExistsException() {
    super("Handelisten eksisterer allerede!");
  }
}
