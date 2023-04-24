package edu.ntnu.idatt2106.smartmat.exceptions.recipe;

/**
 * Exception thrown when a recipe is not found.
 * @author Simen G.
 * @version 1.0 - 21.04.2023
 */
public class RecipeNotFoundException extends RuntimeException {

  /**
   * Constructor for RecipeNotFoundException.
   * Has a default message: "Oppskrift ble ikke funnet".
   */
  public RecipeNotFoundException() {
    super("Oppskrift ble ikke funnet");
  }

  /**
   * Constructor for RecipeNotFoundException.
   * @param message the message to be displayed.
   */
  public RecipeNotFoundException(String message) {
    super(message);
  }
}
