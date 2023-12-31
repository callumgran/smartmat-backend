package edu.ntnu.idatt2106.smartmat.validation;

/**
 * Enum for validation rules.
 * Describes the minimum and maximum values for certain fields.
 * @author Callum G.
 * @version 1.1 - 26.04.2023
 */
public enum ValidationRules {
  /**
   * Minimum index of a database entry.
   */
  DATABASE_MIN_INDEX(1),
  /**
   * Size of a UUID.
   * 36 characters.
   */
  UUID_SIZE(36);

  /**
   * The value of the rule.
   */
  private final int value;

  /**
   * Constructor for ValidationRules.
   * @param value The value of the rule.
   */
  ValidationRules(int value) {
    this.value = value;
  }

  /**
   * Getter for value.
   * @return The value of the rule.
   */
  public int getValue() {
    return value;
  }
}
