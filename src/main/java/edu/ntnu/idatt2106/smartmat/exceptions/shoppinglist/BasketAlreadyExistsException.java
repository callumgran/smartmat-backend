package edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist;

/**
 * Exception for when a shopping list already exists.
 * @author Tobias O.
 * @version 1.0 - 21.04.2023.
 */
public class BasketAlreadyExistsException extends Exception {

  /**
   * Constructor for the exception.
   * @param message the message to be displayed.
   */
  public BasketAlreadyExistsException(String message) {
    super(message);
  }

  /**
   * Constructor for BasketAlreadyExistsException.
   * Has a default message: "Handekurv eksisterer allerede!".
   */
  public BasketAlreadyExistsException() {
    super("Handekurv eksisterer allerede!");
  }
}
