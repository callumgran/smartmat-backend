package edu.ntnu.idatt2106.smartmat.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

public class RegexTest {

  private final String emailGood = "test@test.com";
  private final String emailBadChar = "test@test&%";
  private final String emailBadFormat = "testtest.com";
  private final String emailStartWithAt = "@test.com";
  private final String emailEndWithAt = "test@.com";
  private final String emailWithWhiteSpace = "test @test.com";

  private final String passwordGood = "Passworddddd1";
  private final String passwordMissingNumber = "Password";
  private final String passwordMissingCapital = "password1";
  private final String passwordMissingLowercase = "PASSWORD1";

  private final String usernameGood = "username";
  private final String usernameUpperCase = "Username";
  private final String usernameBadChar = "user name";
  private final String usernameConsecutive = "user__name";

  private final String nameGood = "Good";
  private final String nameWithNumber = "Bad1";
  private final String nameWithSpecialChar = "bad!";
  private final String nameWithSpace = "Good Name";
  private final String nameWithHyphen = "Good-Name";
  private final String nameWithApostrophe = "Good's Name";
  private final String nameWithDot = "Good. Name";

  private final String javaVariableGood = "goodName";
  private final String javaVariableSpace = "bad name";
  private final String javaVariableDollarSign = "good$name";
  private final String javaVariableHyphen = "bad-name";
  private final String javaVariableUpperCase = "BadName";
  private final String javaVariableNumber = "goodName1";

  @Test
  public void testEmailRegexWorksOnNormalEmail() {
    assertTrue(emailGood.matches(RegexPattern.EMAIL.getPattern()));
  }

  @Test
  public void testEmailRegexFailsOnBadChar() {
    assertFalse(emailBadChar.matches(RegexPattern.EMAIL.getPattern()));
  }

  @Test
  public void testEmailRegexFailsOnBadFormat() {
    assertFalse(emailBadFormat.matches(RegexPattern.EMAIL.getPattern()));
  }

  @Test
  public void testEmailRegexFailsOnStartWithAt() {
    assertFalse(emailStartWithAt.matches(RegexPattern.EMAIL.getPattern()));
  }

  @Test
  public void testEmailRegexFailsOnEndWithAt() {
    assertFalse(emailEndWithAt.matches(RegexPattern.EMAIL.getPattern()));
  }

  @Test
  public void testEmailRegexFailsOnWhiteSpace() {
    assertFalse(emailWithWhiteSpace.matches(RegexPattern.EMAIL.getPattern()));
  }

  @Test
  public void testPasswordRegexWorksOnNormalPassword() {
    assertTrue(passwordGood.matches(RegexPattern.PASSWORD.getPattern()));
  }

  @Test
  public void testPasswordRegexFailsOnMissingNumber() {
    assertFalse(passwordMissingNumber.matches(RegexPattern.PASSWORD.getPattern()));
  }

  @Test
  public void testPasswordRegexFailsOnMissingCapital() {
    assertFalse(passwordMissingCapital.matches(RegexPattern.PASSWORD.getPattern()));
  }

  @Test
  public void testPasswordRegexFailsOnMissingLowercase() {
    assertFalse(passwordMissingLowercase.matches(RegexPattern.PASSWORD.getPattern()));
  }

  @Test
  public void testUsernameRegexWorksOnNormalUsername() {
    assertTrue(usernameGood.matches(RegexPattern.USERNAME.getPattern()));
  }

  @Test
  public void testUsernameRegexWorksOnUpperCase() {
    assertTrue(usernameUpperCase.matches(RegexPattern.USERNAME.getPattern()));
  }

  @Test
  public void testUsernameRegexFailsOnBadChar() {
    assertFalse(usernameBadChar.matches(RegexPattern.USERNAME.getPattern()));
  }

  @Test
  public void testUsernameRegexFailsOnConsecutive() {
    assertFalse(usernameConsecutive.matches(RegexPattern.USERNAME.getPattern()));
  }

  @Test
  public void testNameRegexWorksOnNormalName() {
    assertTrue(nameGood.matches(RegexPattern.NAME.getPattern()));
  }

  @Test
  public void testNameRegexFailsOnNumber() {
    assertFalse(nameWithNumber.matches(RegexPattern.NAME.getPattern()));
  }

  @Test
  public void testNameRegexFailsOnSpecialChar() {
    assertFalse(nameWithSpecialChar.matches(RegexPattern.NAME.getPattern()));
  }

  @Test
  public void testNameRegexWorksOnSpace() {
    assertTrue(nameWithSpace.matches(RegexPattern.NAME.getPattern()));
  }

  @Test
  public void testNameRegexWorksOnHyphen() {
    assertTrue(nameWithHyphen.matches(RegexPattern.NAME.getPattern()));
  }

  @Test
  public void testNameRegexWorksOnApostrophe() {
    assertTrue(nameWithApostrophe.matches(RegexPattern.NAME.getPattern()));
  }

  @Test
  public void testNameRegexWorksOnDot() {
    assertTrue(nameWithDot.matches(RegexPattern.NAME.getPattern()));
  }

  @Test
  public void testJavaVariableRegexWorksOnNormalVariable() {
    assertTrue(javaVariableGood.matches(RegexPattern.JAVA_VARIABLE.getPattern()));
  }

  @Test
  public void testJavaVariableRegexFailsOnSpace() {
    assertFalse(javaVariableSpace.matches(RegexPattern.JAVA_VARIABLE.getPattern()));
  }

  @Test
  public void testJavaVariableRegexWorksOnDollarSign() {
    assertTrue(javaVariableDollarSign.matches(RegexPattern.JAVA_VARIABLE.getPattern()));
  }

  @Test
  public void testJavaVariableRegexFailsOnHyphen() {
    assertFalse(javaVariableHyphen.matches(RegexPattern.JAVA_VARIABLE.getPattern()));
  }

  @Test
  public void testJavaVariableRegexFailsOnUpperCase() {
    assertFalse(javaVariableUpperCase.matches(RegexPattern.JAVA_VARIABLE.getPattern()));
  }

  @Test
  public void testJavaVariableRegexWorksOnNumber() {
    assertTrue(javaVariableNumber.matches(RegexPattern.JAVA_VARIABLE.getPattern()));
  }
}
