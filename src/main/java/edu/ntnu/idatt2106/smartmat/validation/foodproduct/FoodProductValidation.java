package edu.ntnu.idatt2106.smartmat.validation.foodproduct;

import edu.ntnu.idatt2106.smartmat.validation.BaseValidation;
import edu.ntnu.idatt2106.smartmat.validation.ValidationRules;
import java.time.LocalDate;

/**
 * Class for validating food products
 *
 * @author Callum G.
 * @version 1.3 26.04.2020
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
      )
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
    Double amount,
    boolean looseWeight,
    Long ingredientId
  ) {
    return (
      (validateEan(ean) || looseWeight) &&
      validateName(name) &&
      isLargerThan(amount, 0.0D) &&
      isLargerThanOrEqual(ingredientId, ValidationRules.DATABASE_MIN_INDEX.getValue())
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
    Double amount,
    boolean looseWeight,
    Long ingredientId
  ) {
    return (
      isLargerThanOrEqual(id, ValidationRules.DATABASE_MIN_INDEX.getValue()) &&
      (validateEan(ean) || looseWeight) &&
      validateName(name) &&
      isLargerThan(amount, 0.0D) &&
      isLargerThanOrEqual(ingredientId, ValidationRules.DATABASE_MIN_INDEX.getValue())
    );
  }

  /**
   * Validates the creation of a custom food product
   * Checks if the name and amount are valid
   * @param name the name to validate
   * @param amount the amount to validate
   * @return true if the name and amount are valid, false otherwise
   */
  public static boolean validateCreateCustomFoodProduct(String name, double amount) {
    return (validateName(name) && isLargerThan(amount, 0.0D));
  }

  /**
   * Validates the creation of a shopping list item.
   * Checks if the name, amount, ingredient ID are valid
   * @param name the name to validate
   * @param amount the amount to validate
   * @param ingredientId the ingredient ID to validate
   * @return true if the name, amount, ingredient ID are valid, false otherwise
   */
  public static boolean validateCreateShoppingListItem(
    String name,
    Double amount,
    Long ingredientId
  ) {
    return (
      validateName(name) &&
      isLargerThan(amount, 0.0D) &&
      isLargerThanOrEqual(ingredientId, ValidationRules.DATABASE_MIN_INDEX.getValue())
    );
  }

  /**
   * Validates the creation of a household food product
   * Checks if the food product id, expiration date and amount left are valid.
   * @param foodProductId the food product id to validate
   * @param expirationDate the expiration date to validate
   * @param amountLeft the amount left to validate
   * @return true if the food product id, expiration date and amount left are valid, false otherwise
   */
  public static boolean validateCreateHouseholdFoodProduct(
    Long foodProductId,
    String expirationDate,
    Double amountLeft
  ) {
    return (
      isLargerThanOrEqual(foodProductId, ValidationRules.DATABASE_MIN_INDEX.getValue()) &&
      isAfter(LocalDate.parse(expirationDate), LocalDate.now().minusDays(7)) &&
      isLargerThan(amountLeft, 0.0D)
    );
  }

  /**
   * Validates the update of a household food product
   * Checks if the food product id, expiration date and amount left are valid.
   * @param foodProductId the food product id to validate
   * @param expirationDate the expiration date to validate
   * @param amountLeft the amount left to validate
   */
  public static boolean validateUpdateHouseholdFoodProduct(
    Long foodProductId,
    String expirationDate,
    Double amountLeft
  ) {
    return (
      isLargerThanOrEqual(foodProductId, ValidationRules.DATABASE_MIN_INDEX.getValue()) &&
      isAfter(LocalDate.parse(expirationDate), LocalDate.now().minusDays(7)) &&
      isLargerThan(amountLeft, 0.0D)
    );
  }
}
