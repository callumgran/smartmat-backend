package edu.ntnu.idatt2106.smartmat.validation.foodproduct;

/**
 * Enum for the rules of a food product
 * @author Callum G.
 * @version 1.0 - 26.04.2023
 */
public enum FoodProductRules {
  /**
   * Minimum length of an EAN code
   */
  MIN_EAN_LENGTH(8),
  /**
   * Maximum length of an EAN code
   */
  MAX_EAN_LENGTH(13),
  /**
   * Minimum length of a name
   */
  MIN_NAME_LENGTH(1),
  /**
   * Maximum length of a name
   */
  MAX_NAME_LENGTH(64);

  private final int value;

  /**
   * Constructor for the enum
   * @param value the value of the rule
   */
  FoodProductRules(int value) {
    this.value = value;
  }

  /**
   * Getter for the value of the rule
   * @return the value of the rule
   */
  public int getValue() {
    return value;
  }
}
