package edu.ntnu.idatt2106.smartmat.validation.househould;

import edu.ntnu.idatt2106.smartmat.validation.BaseValidation;

/**
 * Class for validating user input for household.
 * @author Callum G.
 * @version 1.0 - 19.04.2023
 */
public class HouseholdValidation extends BaseValidation {

  /**
   * Check if the given string is a valid household name.
   * @param name The name to check.
   * @return True if the name is valid, false otherwise.
   */
  public static boolean isValidName(String name) {
    return (
      isNotNullOrEmpty(name) &&
      isBetween(
        name,
        HouseholdValidationRules.NAME_MIN_LENGTH.getValue(),
        HouseholdValidationRules.NAME_MAX_LENGTH.getValue()
      )
    );
  }
}
