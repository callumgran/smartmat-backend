package edu.ntnu.idatt2106.smartmat.utils;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.BasketItem;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;

/**
 * Util functions for comparing units.
 * Mostly used for comparing ingredients and food products.
 *
 * @author Callum G.
 * @version 1.0 01.05.2023
 */
public class UnitUtils {

  /**
   * Method to get the normalized unit of a household food product
   * @param amount The amount of the household food product
   * @param conversionFactor The conversion factor of the household food product
   * @param unitType The unit type of the household food product
   * @return The normalized unit of the household food product
   */
  public static double getNormalizedUnit(
    double amount,
    double conversionFactor,
    UnitTypeEnum unitType
  ) {
    return amount * unitType.toKg(conversionFactor);
  }

  /**
   * Method to get the normalized unit of a household food product
   * @param foodProduct The household food product to get the normalized unit of
   * @return The normalized unit of the household food product
   */
  public static double getNormalizedUnit(HouseholdFoodProduct foodProduct) {
    return getNormalizedUnit(
      foodProduct.getAmountLeft() * foodProduct.getFoodProduct().getAmount(),
      foodProduct.getFoodProduct().getUnit().getToNormalFormConversionFactor(),
      foodProduct.getFoodProduct().getUnit().getUnitType()
    );
  }

  /**
   * Method to get the normalized unit of a recipe ingredient
   * @param ingredient The recipe ingredient to get the normalized unit of
   * @return The normalized unit of the recipe ingredient
   */
  public static double getNormalizedUnit(RecipeIngredient ingredient) {
    return getNormalizedUnit(
      ingredient.getAmount(),
      ingredient.getIngredient().getUnit().getToNormalFormConversionFactor(),
      ingredient.getIngredient().getUnit().getUnitType()
    );
  }

  /**
   * Method to get the normalized unit of a shopping list item
   * @param shoppingListItem The shopping list item to get the normalized unit of
   * @return The normalized unit of the shopping list item
   */
  public static double getNormalizedUnit(ShoppingListItem shoppingListItem) {
    return getNormalizedUnit(
      shoppingListItem.getAmount(),
      shoppingListItem.getIngredient().getUnit().getToNormalFormConversionFactor(),
      shoppingListItem.getIngredient().getUnit().getUnitType()
    );
  }

  /**
   * Method to get the normalized unit of a shopping list item
   * @param basketItem The basket item to get the normalized unit of
   * @return The normalized unit of the basket item
   */
  public static double getNormalizedUnit(BasketItem basketItem) {
    return getNormalizedUnit(
      basketItem.getAmount() * basketItem.getFoodProduct().getAmount(),
      basketItem.getFoodProduct().getUnit().getToNormalFormConversionFactor(),
      basketItem.getFoodProduct().getUnit().getUnitType()
    );
  }

  /**
   * Method to get the original unit of a household food product
   * @param amount The amount of the household food product
   * @param conversionFactor The conversion factor of the household food product
   * @param unitType The unit type of the household food product
   * @return The original unit of the household food product
   */
  public static double getOriginalUnit(
    double amount,
    double conversionFactor,
    UnitTypeEnum unitType
  ) {
    return amount / unitType.toKg(conversionFactor);
  }

  /**
   * Method to get the original unit of a household food product
   * @param amount The amount of the household food product
   * @param foodProduct The household food product to get the original unit of
   * @return The original unit of the household food product
   */
  public static double getOriginalUnit(double amount, HouseholdFoodProduct foodProduct) {
    return getOriginalUnit(
      amount / foodProduct.getFoodProduct().getAmount(),
      foodProduct.getFoodProduct().getUnit().getToNormalFormConversionFactor(),
      foodProduct.getFoodProduct().getUnit().getUnitType()
    );
  }

  /**
   * Method to get the original unit of a recipe ingredient
   * @param amount The amount of the recipe ingredient
   * @param ingredient The recipe ingredient to get the original unit of
   * @return The original unit of the recipe ingredient
   */
  public static double getOriginalUnit(double amount, RecipeIngredient ingredient) {
    return getOriginalUnit(
      amount,
      ingredient.getIngredient().getUnit().getToNormalFormConversionFactor(),
      ingredient.getIngredient().getUnit().getUnitType()
    );
  }

  /**
   * Method to get the original unit of a shopping list item
   * @param amount The amount of the shopping list item
   * @param shoppingListItem The shopping list item to get the original unit of
   * @return The original unit of the shopping list item
   */
  public static double getOriginalUnit(double amount, ShoppingListItem shoppingListItem) {
    return getOriginalUnit(
      amount,
      shoppingListItem.getIngredient().getUnit().getToNormalFormConversionFactor(),
      shoppingListItem.getIngredient().getUnit().getUnitType()
    );
  }

  /**
   * Method to get the original unit of a shopping list item
   * @param amount The amount of the shopping list item
   * @param basketItem The shopping list item to get the original unit of
   * @return The original unit of the shopping list item
   */
  public static double getOriginalUnit(double amount, BasketItem basketItem) {
    return getOriginalUnit(
      amount,
      basketItem.getFoodProduct().getUnit().getToNormalFormConversionFactor(),
      basketItem.getFoodProduct().getUnit().getUnitType()
    );
  }

  /**
   * Method to check if two unit types are the same
   * @param abbreviation1 The first unit type
   * @param abbreviation2 The second unit type
   * @return True if the unit types are the same, false otherwise
   */
  public static boolean isSameUnitType(String abbreviation1, String abbreviation2) {
    return abbreviation1.equals(abbreviation2);
  }

  /**
   * Method to check if two household food products are the same unit type
   * @param foodProduct1 The first household food product
   * @param foodProduct2 The second household food product
   * @return True if the household food products are the same unit type, false otherwise
   */
  public static boolean isSameUnitType(
    HouseholdFoodProduct foodProduct,
    RecipeIngredient ingredient
  ) {
    return isSameUnitType(
      foodProduct.getFoodProduct().getUnit().getAbbreviation(),
      ingredient.getIngredient().getUnit().getAbbreviation()
    );
  }

  /**
   * Method to remove the amount of a recipe ingredient from a household food product
   * @param ingredient The recipe ingredient to remove
   * @param foodProduct The household food product to remove the recipe ingredient from
   * @return The new amount of the household food product
   */
  public static double removeRecipeIngredientAmountFromHouseholdFoodProductAmount(
    RecipeIngredient ingredient,
    HouseholdFoodProduct foodProduct
  ) {
    if (isSameUnitType(foodProduct, ingredient)) {
      foodProduct.setAmountLeft(foodProduct.getAmountLeft() - ingredient.getAmount());
      return foodProduct.getAmountLeft() - ingredient.getAmount();
    }

    double normalizedIngredientAmount = getNormalizedUnit(ingredient);

    double normalizedFoodProductAmount = getNormalizedUnit(foodProduct);

    double newAmount = normalizedFoodProductAmount - normalizedIngredientAmount;
    if (newAmount < 0) {
      foodProduct.setAmountLeft(0);
    } else {
      foodProduct.setAmountLeft(getOriginalUnit(newAmount, foodProduct));
    }

    return newAmount;
  }

  /**
   * Method to remove the amount of a household food product from a recipe ingredient
   * @param foodProduct The household food product to remove
   * @param ingredient The recipe ingredient to remove the household food product from
   * @return The new amount of the recipe ingredient
   */
  public static double removeRecipeHouseholdFoodProductAmountFromIngredientAmount(
    HouseholdFoodProduct foodProduct,
    RecipeIngredient ingredient
  ) {
    if (isSameUnitType(foodProduct, ingredient)) {
      ingredient.setAmount(ingredient.getAmount() - foodProduct.getAmountLeft());
      return ingredient.getAmount() - foodProduct.getAmountLeft();
    }

    double normalizedIngredientAmount = getNormalizedUnit(ingredient);

    double normalizedFoodProductAmount = getNormalizedUnit(foodProduct);

    double newAmount = normalizedIngredientAmount - normalizedFoodProductAmount;
    if (newAmount < 0) {
      ingredient.setAmount(0.0D);
    } else {
      ingredient.setAmount(getOriginalUnit(newAmount, ingredient));
    }

    return newAmount;
  }
}
