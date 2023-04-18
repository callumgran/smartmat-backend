package edu.ntnu.idatt2106.smartmat.validation.base;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2106.smartmat.validation.BaseValidation;
import org.junit.Test;

public class BaseValidationNumberTest {

  private final Integer positive = 1;
  private final Integer negative = -1;
  private final Integer zero = 0;

  @Test
  public void testValidatePositiveNumberReturnsTrueOnPositiveNumber() {
    assertTrue(BaseValidation.isLargerThan(positive, 0));
  }

  @Test
  public void testValidatePositiveNumberReturnsFalseOnNegativeNumber() {
    assertFalse(BaseValidation.isLargerThan(negative, 0));
  }

  @Test
  public void testValidatePositiveNumberReturnsFalseOnZero() {
    assertFalse(BaseValidation.isLargerThan(zero, 0));
  }

  @Test
  public void testValidateNegativeNumberReturnsTrueOnNegativeNumber() {
    assertTrue(BaseValidation.isSmallerThan(negative, 0));
  }

  @Test
  public void testValidateNegativeNumberReturnsFalseOnPositiveNumber() {
    assertFalse(BaseValidation.isSmallerThan(positive, 0));
  }

  @Test
  public void testValidateNegativeNumberReturnsFalseOnZero() {
    assertFalse(BaseValidation.isSmallerThan(zero, 0));
  }

  @Test
  public void testValidateZeroNumberLargerThanEqualsReturnsTrueOnZero() {
    assertTrue(BaseValidation.isLargerThanOrEqual(zero, 0));
  }

  @Test
  public void testValidateZeroNumberLargerThanEqualsReturnsTrueOnPositiveNumber() {
    assertTrue(BaseValidation.isLargerThanOrEqual(positive, 0));
  }

  @Test
  public void testValidateZeroNumberLargerThanEqualsReturnsFalseOnNegativeNumber() {
    assertFalse(BaseValidation.isLargerThanOrEqual(negative, 0));
  }

  @Test
  public void testValidateZeroNumberSmallerThanEqualsReturnsTrueOnZero() {
    assertTrue(BaseValidation.isSmallerThanOrEqual(zero, 0));
  }

  @Test
  public void testValidateZeroNumberSmallerThanEqualsReturnsTrueOnNegativeNumber() {
    assertTrue(BaseValidation.isSmallerThanOrEqual(negative, 0));
  }

  @Test
  public void testValidateZeroNumberSmallerThanEqualsReturnsFalseOnPositiveNumber() {
    assertFalse(BaseValidation.isSmallerThanOrEqual(positive, 0));
  }

  @Test
  public void testBetweenReturnsTrueOnNumberBetween() {
    assertTrue(BaseValidation.isBetween(positive, 0, 2));
  }

  @Test
  public void testBetweenReturnsFalseOnNumberNotBetween() {
    assertFalse(BaseValidation.isBetween(positive, -1, 0));
  }

  @Test
  public void testBetweenReturnsTrueOnNumberEqualToMin() {
    assertTrue(BaseValidation.isBetween(positive, 1, 2));
  }

  @Test
  public void testBetweenReturnsTrueOnNumberEqualToMax() {
    assertTrue(BaseValidation.isBetween(positive, 0, 1));
  }
}
