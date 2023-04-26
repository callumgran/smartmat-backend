package edu.ntnu.idatt2106.smartmat.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.ntnu.idatt2106.smartmat.validation.foodproduct.FoodProductValidation;
import org.junit.Test;

public class FoodProductValidationTest {

  private final String emptyString = "";
  private final String shortEan = "1234567";
  private final String eightEan = "12345678";
  private final String thirteenEan = "1234567890123";
  private final String longEan = "12345678901234";

  private final String shortName = "a";
  private final String longName = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
  private final String veryLongName =
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

  private final Long badId = -1L;
  private final Long goodId = 1L;

  private final double badAmount = -1.0;
  private final double goodAmount = 1.0;

  private final boolean notLoose = false;
  private final boolean loose = true;

  @Test
  public void testValidateEanValidEightDigitEan() {
    assertTrue(FoodProductValidation.validateEan(eightEan));
  }

  @Test
  public void testValidateEanValidThirteenDigitEan() {
    assertTrue(FoodProductValidation.validateEan(thirteenEan));
  }

  @Test
  public void testValidateEanInvalidShortEan() {
    assertFalse(FoodProductValidation.validateEan(shortEan));
  }

  @Test
  public void testValidateEanInvalidLongEan() {
    assertFalse(FoodProductValidation.validateEan(longEan));
  }

  @Test
  public void testValidateEanInvalidEmptyEan() {
    assertFalse(FoodProductValidation.validateEan(emptyString));
  }

  @Test
  public void testValidateEanInvalidNullEan() {
    assertFalse(FoodProductValidation.validateEan(null));
  }

  @Test
  public void testValidateNameValidShortName() {
    assertTrue(FoodProductValidation.validateName(shortName));
  }

  @Test
  public void testValidateNameValidLongName() {
    assertTrue(FoodProductValidation.validateName(longName));
  }

  @Test
  public void testValidateNameInvalidVeryLongName() {
    assertFalse(FoodProductValidation.validateName(veryLongName));
  }

  @Test
  public void testValidateNameInvalidEmptyName() {
    assertFalse(FoodProductValidation.validateName(emptyString));
  }

  @Test
  public void testValidateNameInvalidNullName() {
    assertFalse(FoodProductValidation.validateName(null));
  }

  @Test
  public void testValidateCreateFoodProductValid() {
    assertTrue(
      FoodProductValidation.validateCreateFoodProduct(
        eightEan,
        shortName,
        goodAmount,
        loose,
        goodId
      )
    );
  }

  @Test
  public void testValidateCreateFoodProductInvalidName() {
    assertFalse(
      FoodProductValidation.validateCreateFoodProduct(
        eightEan,
        emptyString,
        goodAmount,
        loose,
        goodId
      )
    );
  }

  @Test
  public void testValidateCreateFoodProductInvalidAmount() {
    assertFalse(
      FoodProductValidation.validateCreateFoodProduct(eightEan, shortName, badAmount, loose, goodId)
    );
  }

  @Test
  public void testValidateCreateFoodProductInvalidId() {
    assertFalse(
      FoodProductValidation.validateCreateFoodProduct(eightEan, shortName, goodAmount, loose, badId)
    );
  }

  @Test
  public void testValidateCreateFoodProductInvalidEanAndLoose() {
    assertTrue(
      FoodProductValidation.validateCreateFoodProduct(
        shortEan,
        shortName,
        goodAmount,
        loose,
        goodId
      )
    );
  }

  @Test
  public void testValidateCreateFoodProductInvalidEanAndNotLoose() {
    assertFalse(
      FoodProductValidation.validateCreateFoodProduct(
        shortEan,
        shortName,
        goodAmount,
        notLoose,
        goodId
      )
    );
  }

  @Test
  public void testValidateUpdateFoodProductValid() {
    assertTrue(
      FoodProductValidation.validateUpdateFoodProduct(
        goodId,
        eightEan,
        shortName,
        goodAmount,
        loose,
        goodId
      )
    );
  }

  @Test
  public void testValidateUpdateFoodProductInvalidId() {
    assertFalse(
      FoodProductValidation.validateUpdateFoodProduct(
        badId,
        eightEan,
        shortName,
        goodAmount,
        loose,
        goodId
      )
    );
  }

  @Test
  public void testValidateUpdateFoodProductInvalidName() {
    assertFalse(
      FoodProductValidation.validateUpdateFoodProduct(
        goodId,
        eightEan,
        emptyString,
        goodAmount,
        loose,
        goodId
      )
    );
  }

  @Test
  public void testValidateUpdateFoodProductInvalidAmount() {
    assertFalse(
      FoodProductValidation.validateUpdateFoodProduct(
        goodId,
        eightEan,
        shortName,
        badAmount,
        loose,
        goodId
      )
    );
  }

  @Test
  public void testValidateUpdateFoodProductInvalidIngredientId() {
    assertFalse(
      FoodProductValidation.validateUpdateFoodProduct(
        goodId,
        eightEan,
        shortName,
        goodAmount,
        loose,
        badId
      )
    );
  }

  @Test
  public void testValidateUpdateFoodProductInvalidEanAndLoose() {
    assertTrue(
      FoodProductValidation.validateUpdateFoodProduct(
        goodId,
        shortEan,
        shortName,
        goodAmount,
        loose,
        goodId
      )
    );
  }

  @Test
  public void testValidateUpdateFoodProductInvalidEanAndNotLoose() {
    assertFalse(
      FoodProductValidation.validateUpdateFoodProduct(
        goodId,
        shortEan,
        shortName,
        goodAmount,
        notLoose,
        goodId
      )
    );
  }
}
