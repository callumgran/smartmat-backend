package edu.ntnu.idatt2106.smartmat.validation.foodproduct;

import edu.ntnu.idatt2106.smartmat.validation.BaseValidation;
import edu.ntnu.idatt2106.smartmat.validation.RegexPattern;

/**
 * Class for validating food products
 *
 * @author Callum G.
 * @version 1.0 26.04.2020
 */
public class FoodProductValidation extends BaseValidation {

  /**
   * Validates the EAN code of a food product
   * Checks if the EAN code is between the minimum and maximum length, and if it is numeric
   * @param ean the EAN code to validate
   * @return true if the EAN code is valid, false otherwise
   */
  public static boolean validateEan(String ean) {
    return (
      isNotNullOrEmpty(ean) &&
      isBetween(
        ean,
        FoodProductRules.MIN_EAN_LENGTH.getValue(),
        FoodProductRules.MAX_EAN_LENGTH.getValue()
      ) &&
      isNumeric(ean)
    );
  }

  /**
   * Validates the name of a food product
   * Checks if the name is between the minimum and maximum length, and if it is a valid name
   * @param name the name to validate
   * @return true if the name is valid, false otherwise
   */
  public static boolean validateName(String name) {
    return (
      isNotNullOrEmpty(name) &&
      isBetween(
        name,
        FoodProductRules.MIN_NAME_LENGTH.getValue(),
        FoodProductRules.MAX_NAME_LENGTH.getValue()
      ) &&
      name.matches(RegexPattern.NAME.getPattern())
    );
  }

  /**
   * Validates the creation of a food product
   * Checks if the EAN code, name, amount and ingredient ID are valid
   * @param name the name to validate
   * @param ean the EAN code to validate
   * @param amount the amount to validate
   * @param ingredientId the ingredient ID to validate
   * @return true if the EAN code, name, amount and ingredient ID are valid, false otherwise
   */
  public static boolean validateCreateFoodProduct(
    String ean,
    String name,
    double amount,
    boolean looseWeight,
    Long ingredientId
  ) {
    return (
      (validateEan(ean) || looseWeight) &&
      validateName(name) &&
      isLargerThan(amount, 0) &&
      isLargerThan(ingredientId, 0)
    );
  }

  /**
   * Validates the update of a food product
   * Checks if the EAN code, name, amount and ingredient ID are valid
   * @param id the ID of the food product to update
   * @param name the name to validate
   * @param ean the EAN code to validate
   * @param amount the amount to validate
   * @param ingredientId the ingredient ID to validate
   * @return true if the EAN code, name, amount and ingredient ID are valid, false otherwise
   */
  public static boolean validateUpdateFoodProduct(
    Long id,
    String ean,
    String name,
    double amount,
    boolean looseWeight,
    Long ingredientId
  ) {
    return (
      isLargerThan(id, 0) &&
      (validateEan(ean) || looseWeight) &&
      validateName(name) &&
      isLargerThan(amount, 0) &&
      isLargerThan(ingredientId, 0)
    );
  }
}
