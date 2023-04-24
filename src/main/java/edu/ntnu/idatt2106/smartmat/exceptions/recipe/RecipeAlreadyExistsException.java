package edu.ntnu.idatt2106.smartmat.exceptions.recipe;

/**
 * Exception thrown when a recipe already exists.
 * @author Simen G.
 * @version 1.0 - 21.04.2023
 */
public class RecipeAlreadyExistsException extends RuntimeException {

  /**
   * Constructor for RecipeAlreadyExistsException.
   * Has a default message: "Oppskrift finnes allerede".
   */
  public RecipeAlreadyExistsException() {
    super("Oppskrift finnes allerede");
  }

  /**
   * Constructor for RecipeAlreadyExistsException.
   * @param message the message to be displayed.
   */
  public RecipeAlreadyExistsException(String message) {
    super(message);
  }
}
