package edu.ntnu.idatt2106.smartmat.validation.househould;

/**
 * Enum for validation rules.
 * Used for validation of household input.
 * Describes the minimum and maximum values for certain fields.
 * @author Callum G.
 * @version 1.0 - 18.04.2023
 */
public enum HouseholdValidationRules {
  /**
   * The minimum length of a household name.
   */
  NAME_MIN_LENGTH(1),
  /**
   * The maximum length of a household name.
   */
  NAME_MAX_LENGTH(64);

  private final int value;

  HouseholdValidationRules(int value) {
    this.value = value;
  }

  /**
   * Get the value of the enum.
   * @return The value of the enum.
   */
  public int getValue() {
    return value;
  }
}
