package edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist;

/**
 * Exception for when item is invalid.
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */
public class IncorrectItemAmountException extends Exception {

  /**
   * Constructor for the exception.
   * @param message the message to be displayed.
   */
  public IncorrectItemAmountException(String message) {
    super(message);
  }

  /**
   * Constructor for IncorrectItemAmountException.
   * Has a default message: "Verdien til matvaren er ikke gyldig.".
   */
  public IncorrectItemAmountException() {
    super("Verdien til matvaren er ikke gyldig.");
  }
}
