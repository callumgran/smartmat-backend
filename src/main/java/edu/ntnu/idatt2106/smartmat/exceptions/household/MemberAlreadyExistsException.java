package edu.ntnu.idatt2106.smartmat.exceptions.household;

/**
 * Exception thrown when a member already exists in a household
 * @author Callum G.
 * @version 1.0 - 24.04.2023
 */
public class MemberAlreadyExistsException extends Exception {

  /**
   * Constructor for member already exists exception
   * @param message The message to be displayed
   */
  public MemberAlreadyExistsException(String message) {
    super(message);
  }

  /**
   * Constructor for member already exists exception
   * Has a default message: "Medlemmet eksisterer allerede i husstanden."
   */
  public MemberAlreadyExistsException() {
    super("Medlemmet eksisterer allerede i husstanden.");
  }
}
