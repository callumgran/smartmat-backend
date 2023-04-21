package edu.ntnu.idatt2106.smartmat.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2106.smartmat.validation.search.SearchRequestValidation;
import org.junit.Test;

public class SearchRequestValidationTest {

  private final String emptyString = "";

  private final String goodJavaName = "categoryName";
  private final String badJavaName = ".categoryname";

  private final int goodPageSize = 10;
  private final int badPageSize = 1000;

  private final int goodPageNumber = 1;
  private final int badPageNumber = -1;

  @Test
  public void testJavaNameValidateReturnsTrue() {
    assertTrue(SearchRequestValidation.validateJavaVariableName(goodJavaName));
  }

  @Test
  public void testJavaNameValidateReturnsFalse() {
    assertFalse(SearchRequestValidation.validateJavaVariableName(badJavaName));
  }

  @Test
  public void testJavaNameValidateReturnsTrueEmptyString() {
    assertTrue(SearchRequestValidation.validateJavaVariableName(emptyString));
  }

  @Test
  public void testPageSizeValidateReturnsTrue() {
    assertTrue(SearchRequestValidation.validatePageSize(goodPageSize));
  }

  @Test
  public void testPageSizeValidateReturnsFalse() {
    assertFalse(SearchRequestValidation.validatePageSize(badPageSize));
  }

  @Test
  public void testPageNumberValidateReturnsTrue() {
    assertTrue(SearchRequestValidation.validatePageNumber(goodPageNumber));
  }

  @Test
  public void testPageNumberValidateReturnsFalse() {
    assertFalse(SearchRequestValidation.validatePageNumber(badPageNumber));
  }
}
