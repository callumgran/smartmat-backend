package edu.ntnu.idatt2106.smartmat.validation.user;

import edu.ntnu.idatt2106.smartmat.validation.BaseValidation;
import edu.ntnu.idatt2106.smartmat.validation.RegexPattern;

/**
 * Class for validating user input.
 * @author Callum G.
 * @version 1.0 - 18.04.2023
 */
public class UserValidation extends BaseValidation {

  /**
   * Checks if the given string is a valid email.
   * @param email The email to check.
   * @return True if the email is valid, false otherwise.
   */
  public static boolean validateEmail(String email) {
    return (
      isNotNullOrEmpty(email) &&
      email.matches(RegexPattern.EMAIL.getPattern()) &&
      isBetween(
        email,
        UserValidationRules.EMAIL_MIN_LENGTH.getValue(),
        UserValidationRules.EMAIL_MAX_LENGTH.getValue()
      )
    );
  }

  /**
   * Checks if the given string is a valid password.
   * Must contain 1 uppercase letter, 1 lowercase letter, 1 number and be at least
   * 8 characters long.
   * @param password The password to check.
   * @return True if the password is valid, false otherwise.
   */
  public static boolean validatePassword(String password) {
    return (
      isNotNullOrEmpty(password) &&
      password.matches(RegexPattern.PASSWORD.getPattern()) &&
      isLargerThanOrEqual(password, UserValidationRules.PASSWORD_MIN_LENGTH.getValue())
    );
  }

  /**
   * Check if the given string is a valid username.
   * Must be between 3 and 32 characters long and only contain letters, numbers
   * and underscores.
   * @param username The username to check.
   * @return True if the username is valid, false otherwise.
   */
  public static boolean validateUsername(String username) {
    return (
      isNotNullOrEmpty(username) &&
      username.matches(RegexPattern.USERNAME.getPattern()) &&
      isBetween(
        username,
        UserValidationRules.USERNAME_MIN_LENGTH.getValue(),
        UserValidationRules.USERNAME_MAX_LENGTH.getValue()
      )
    );
  }

  /**
   * Check if the given string is a valid name.
   * Must be smaller than 64 characters long and only contain letters.
   * @param firstName The name to check.
   * @return True if the name is valid, false otherwise.
   */
  public static boolean validateName(String firstName) {
    return (
      isNotNullOrEmpty(firstName) &&
      firstName.matches(RegexPattern.NAME.getPattern()) &&
      isSmallerThan(firstName, UserValidationRules.NAME_MAX_LENGTH.getValue())
    );
  }

  /**
   * Validate registration form.
   * @param username  The username to validate.
   * @param email     The email to validate.
   * @param firstName The first name to validate.
   * @param lastName  The last name to validate.
   * @param password  The password to validate.
   * @return True if the form is valid, false otherwise.
   */
  public static boolean validateRegistrationForm(
    String username,
    String email,
    String firstName,
    String lastName,
    String password
  ) {
    boolean valid = true;
    valid &= validateUsername(username);
    valid &= validateEmail(email);
    valid &= validatePassword(password);
    valid &= validateName(firstName);
    valid &= validateName(lastName);

    return valid;
  }

  /**
   * Validate password change.
   * @param oldPassword The old password to validate.
   * @param newPassword The new password to validate.
   * @return True if the form is valid, false otherwise.
   */
  public static boolean validatePasswordChange(String oldPassword, String newPassword) {
    boolean valid = true;
    valid &= validatePassword(oldPassword);
    valid &= validatePassword(newPassword);

    return valid;
  }

  /**
   * Validate partial user update.
   * @param email       The email to validate.
   * @param firstName   The first name to validate.
   * @param lastName    The last name to validate.
   * @param password    The password to validate.
   * @param newPassword The new password to validate.
   * @return True if the form is valid, false otherwise.
   */
  public static boolean validatePartialUserUpdate(
    String email,
    String firstName,
    String lastName,
    String password,
    String newPassword
  ) {
    boolean valid = true;
    if (isNotNullOrEmpty(email)) {
      valid &= validateEmail(email);
    }
    if (isNotNullOrEmpty(firstName)) {
      valid &= validateName(firstName);
    }
    if (isNotNullOrEmpty(lastName)) {
      valid &= validateName(lastName);
    }
    if (isNotNullOrEmpty(newPassword)) {
      valid &= validatePasswordChange(password, newPassword);
    }

    return valid;
  }
}
