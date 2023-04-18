package edu.ntnu.idatt2106.smartmat.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2106.smartmat.validation.user.UserValidation;
import org.junit.Test;

public class UserValidationTest {

  private final String emptyString = "";
  private final String longString =
    "messagemessagemessage" +
    "messagemessagemessage" +
    "messagemessagemessage" +
    "messagemessagemessage" +
    "messagemessagemessage" +
    "messagemessagemessage" +
    "messagemessagemessage" +
    "messagemessagemessage" +
    "messagemessagemessage" +
    "messagemessagemessage" +
    "messagemessagemessage";

  private final String goodEmail = "test@test.com";
  private final String badEmail = "@test.com";
  private final String longEmail = longString + "@test.com";

  private final String goodPassword = "Password123";
  private final String badPassword = "password";
  private final String longPassword = longString + "Messagemessagemessage123";

  private final String goodUserName = "john";
  private final String badUserName = "John Wick";
  private final String longUserName = longString + "John Wick";
  private final String shortUserName = "j";

  private final String goodPersonName = "John";
  private final String badPersonName = "John123";

  @Test
  public void testEmailValidateReturnsTrue() {
    assertTrue(UserValidation.validateEmail(goodEmail));
  }

  @Test
  public void testEmailValidateReturnsFalse() {
    assertFalse(UserValidation.validateEmail(badEmail));
  }

  @Test
  public void testEmailValidateReturnsFalseLongString() {
    assertFalse(UserValidation.validateEmail(longEmail));
  }

  @Test
  public void testEmailValidateReturnsFalseEmptyString() {
    assertFalse(UserValidation.validateEmail(emptyString));
  }

  @Test
  public void testEmailValidateReturnsFalseNull() {
    assertFalse(UserValidation.validateEmail(null));
  }

  @Test
  public void testPasswordValidateReturnsTrue() {
    assertTrue(UserValidation.validatePassword(goodPassword));
  }

  @Test
  public void testPasswordValidateReturnsFalse() {
    assertFalse(UserValidation.validatePassword(badPassword));
  }

  @Test
  public void testPasswordValidateReturnsTrueLongString() {
    assertTrue(UserValidation.validatePassword(longPassword));
  }

  @Test
  public void testPasswordValidateReturnsFalseEmptyString() {
    assertFalse(UserValidation.validatePassword(emptyString));
  }

  @Test
  public void testPasswordValidateReturnsFalseNull() {
    assertFalse(UserValidation.validatePassword(null));
  }

  @Test
  public void testUserNameValidateReturnsTrue() {
    assertTrue(UserValidation.validateUsername(goodUserName));
  }

  @Test
  public void testUserNameValidateReturnsFalse() {
    assertFalse(UserValidation.validateUsername(badUserName));
  }

  @Test
  public void testUserNameValidateReturnsFalseLongString() {
    assertFalse(UserValidation.validateUsername(longUserName));
  }

  @Test
  public void testUserNameValidateReturnsFalseEmptyString() {
    assertFalse(UserValidation.validateUsername(emptyString));
  }

  @Test
  public void testUserNameValidateReturnsFalseNull() {
    assertFalse(UserValidation.validateUsername(null));
  }

  @Test
  public void testUserNameValidateReturnsFalseShortString() {
    assertFalse(UserValidation.validateUsername(shortUserName));
  }

  @Test
  public void testPersonNameValidateReturnsTrue() {
    assertTrue(UserValidation.validateName(goodPersonName));
  }

  @Test
  public void testPersonNameValidateReturnsFalse() {
    assertFalse(UserValidation.validateName(badPersonName));
  }

  @Test
  public void testPersonNameValidateReturnsFalseLongString() {
    assertFalse(UserValidation.validateName(longUserName));
  }

  @Test
  public void testPersonNameValidateReturnsFalseEmptyString() {
    assertFalse(UserValidation.validateName(emptyString));
  }

  @Test
  public void testPersonNameValidateReturnsFalseNull() {
    assertFalse(UserValidation.validateName(null));
  }

  @Test
  public void testValidatePartialUserUpdateReturnsTrueAllFields() {
    assertTrue(
      UserValidation.validatePartialUserUpdate(
        goodEmail,
        goodPersonName,
        goodPersonName,
        goodPassword,
        goodPassword
      )
    );
  }
}
