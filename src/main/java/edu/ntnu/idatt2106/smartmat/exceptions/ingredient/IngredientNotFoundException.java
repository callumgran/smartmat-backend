package edu.ntnu.idatt2106.smartmat.exceptions.ingredient;

/**
 * Exception for when an ingredient is not found.
 * @author Tobias O.
 * @version 1.0 - 20.04.2023
 */
public class IngredientNotFoundException extends Exception {

  /**
   * Constructor for IngredientNotFoundException.
   * @param message String message to be displayed.
   */
  public IngredientNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructor for IngredientNotFoundException.
   * Has a default message: "Ingrediensen ble ikke funnet."
   */
  public IngredientNotFoundException() {
    super("Ingrediensen ble ikke funnet.");
  }
}
