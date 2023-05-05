package edu.ntnu.idatt2106.smartmat.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.ntnu.idatt2106.smartmat.validation.househould.HouseholdValidation;
import org.junit.Test;

public class HouseholdValidationTest {

  private final String nullString = null;
  private final String emptyString = "";
  private final String shortName = "a";
  private final String longName = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
  private final String veryLongName =
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

  @Test
  public void testValidateHouseholdNameValid() {
    assertTrue(HouseholdValidation.isValidName(longName));
  }

  @Test
  public void testValidateHouseholdNameInvalidNull() {
    assertFalse(HouseholdValidation.isValidName(nullString));
  }

  @Test
  public void testValidateHouseholdNameInvalidEmpty() {
    assertFalse(HouseholdValidation.isValidName(emptyString));
  }

  @Test
  public void testValidateHouseholdNameInvalidShort() {
    assertTrue(HouseholdValidation.isValidName(shortName));
  }

  @Test
  public void testValidateHouseholdNameInvalidLong() {
    assertFalse(HouseholdValidation.isValidName(veryLongName));
  }
}
