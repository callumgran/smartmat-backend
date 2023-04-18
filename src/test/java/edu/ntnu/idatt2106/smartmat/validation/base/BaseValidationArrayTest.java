package edu.ntnu.idatt2106.smartmat.validation.base;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2106.smartmat.validation.BaseValidation;
import org.junit.Test;

public class BaseValidationArrayTest {

  private final String[] emptyArray = {};
  private final String[] nullArray = null;
  private final String[] goodArray = { "goodString", "goodString2" };

  private final Integer[] numArr = { 1, 2, 3 };
  private final Integer[] numArr2 = { -1, -2, 2 };

  @Test
  public void testValidateIsNotNullOrEmptyReturnsFalseOnNull() {
    assertFalse(BaseValidation.isNotNullOrEmpty(nullArray));
  }

  @Test
  public void testValidateIsNotNullOrEmptyReturnsFalseOnEmptyArray() {
    assertFalse(BaseValidation.isNotNullOrEmpty(emptyArray));
  }

  @Test
  public void testValidateIsNotNullOrEmptyReturnsTrueOnGoodArray() {
    assertTrue(BaseValidation.isNotNullOrEmpty(goodArray));
  }

  @Test
  public void testValidatePositiveNumberReturnsTrueOnPositiveNumber() {
    assertTrue(BaseValidation.validatePositive(numArr));
  }

  @Test
  public void testValidatePositiveNumberReturnsFalseOnNegativeNumber() {
    assertFalse(BaseValidation.validatePositive(numArr2));
  }
}
