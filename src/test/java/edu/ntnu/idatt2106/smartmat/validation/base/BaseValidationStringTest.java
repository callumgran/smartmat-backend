package edu.ntnu.idatt2106.smartmat.validation.base;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2106.smartmat.validation.BaseValidation;
import org.junit.Test;

public class BaseValidationStringTest {

  private final String emptyString = "";
  private final String nullString = null;
  private final String goodString = "goodString";
  private final String tooLongString = "longStringThatIsTooLong";
  private final String equalLongString = "longString";
  private final String equalShortString = "short";
  private final String tooShortString = "shor";

  private final int minLen = 5;
  private final int maxLen = 10;

  @Test
  public void testValidateIsNotNullOrEmptyReturnsFalseOnNull() {
    assertFalse(BaseValidation.isNotNullOrEmpty(nullString));
  }

  @Test
  public void testValidateIsNotNullOrEmptyReturnsFalseOnEmptyString() {
    assertFalse(BaseValidation.isNotNullOrEmpty(emptyString));
  }

  @Test
  public void testValidateIsNotNullOrEmptyReturnsTrueOnGoodString() {
    assertTrue(BaseValidation.isNotNullOrEmpty(goodString));
  }

  @Test
  public void testValidateStringSmallerThanReturnsTrueOnSmallerString() {
    assertFalse(BaseValidation.isSmallerThan(goodString, minLen));
  }

  @Test
  public void testValidateStringSmallerThanReturnsTrueOnEmptyString() {
    assertTrue(BaseValidation.isSmallerThan(emptyString, minLen));
  }

  @Test
  public void voidtestValidateStringSmallerThanReturnsFalseOnTooLongString() {
    assertFalse(BaseValidation.isSmallerThan(tooLongString, minLen));
  }

  @Test
  public void testValidateStringSmallerThanEqualReturnsTrueOnEqualString() {
    assertTrue(BaseValidation.isSmallerThanOrEqual(equalShortString, minLen));
  }

  @Test
  public void testValidateStringSmallerThanEqualReturnsTrueOnSmallerString() {
    assertTrue(BaseValidation.isSmallerThanOrEqual(tooShortString, minLen));
  }

  @Test
  public void testValidateStringSmallerThanEqualReturnsFalseOnTooLongString() {
    assertFalse(BaseValidation.isSmallerThanOrEqual(tooLongString, minLen));
  }

  @Test
  public void testValidateStringLargerThanReturnsTrueOnLargerString() {
    assertTrue(BaseValidation.isLargerThan(tooLongString, minLen));
  }

  @Test
  public void testValidateStringLargerThanReturnsFalseOnEmptyString() {
    assertFalse(BaseValidation.isLargerThan(emptyString, minLen));
  }

  @Test
  public void testValidateStringLargerThanReturnsFalseOnTooShortString() {
    assertFalse(BaseValidation.isLargerThan(tooShortString, minLen));
  }

  @Test
  public void testValidateStringLargerThanReturnsFalseOnEqualString() {
    assertFalse(BaseValidation.isLargerThan(equalShortString, minLen));
  }

  @Test
  public void testValidateStringLargerThanEqualReturnsTrueOnEqualString() {
    assertTrue(BaseValidation.isLargerThanOrEqual(equalLongString, minLen));
  }

  @Test
  public void testValidateStringLargerThanEqualReturnsTrueOnLargerString() {
    assertTrue(BaseValidation.isLargerThanOrEqual(tooLongString, minLen));
  }

  @Test
  public void testValidateStringLargerThanEqualReturnsFalseOnTooShortString() {
    assertFalse(BaseValidation.isLargerThanOrEqual(tooShortString, minLen));
  }

  @Test
  public void testValidateStringBetweenReturnsFalseOnEmptyString() {
    assertFalse(BaseValidation.isBetween(emptyString, minLen, maxLen));
  }

  @Test
  public void testValidateStringIsBetweenReturnsTrueOnGoodString() {
    assertTrue(BaseValidation.isBetween(goodString, minLen, maxLen));
  }

  @Test
  public void testValidateStringIsBetweenReturnsFalseOnTooShortString() {
    assertFalse(BaseValidation.isBetween(tooShortString, minLen, maxLen));
  }

  @Test
  public void testValidateStringIsBetweenReturnsFalseOnTooLongString() {
    assertFalse(BaseValidation.isBetween(tooLongString, minLen, maxLen));
  }

  @Test
  public void testValidateStringIsBetweenReturnsTrueOnEqualMinString() {
    assertTrue(BaseValidation.isBetween(equalShortString, minLen, maxLen));
  }

  @Test
  public void testValidateStringIsBetweenReturnsTrueOnEqualMaxString() {
    assertTrue(BaseValidation.isBetween(equalLongString, minLen, maxLen));
  }
}
