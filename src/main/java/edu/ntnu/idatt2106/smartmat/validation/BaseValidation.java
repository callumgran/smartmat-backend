package edu.ntnu.idatt2106.smartmat.validation;

import java.time.chrono.ChronoLocalDate;

/**
 * Class for validating user input.
 * Base class for validation of user input for different models.
 * @author Callum G.
 * @version 1.0 - 18.04.2023
 */
public abstract class BaseValidation {

  /**
   * Checks if the given string is null or empty.
   */
  public static boolean isNotNullOrEmpty(String string) {
    return !(string == null || string.isEmpty());
  }

  /**
   * Check if the given string is smaller than the given length.
   * @param string The string to check.
   * @param length The length to check against.
   * @return True if the string is smaller than the length, false otherwise.
   */
  public static boolean isSmallerThan(String string, int length) {
    return string.length() < length;
  }

  /**
   * Check if the given string is smaller than or equal to the given length.
   * @param string The string to check.
   * @param length The length to check against.
   * @return True if the string is smaller than or equal to the length, false otherwise.
   */
  public static boolean isSmallerThanOrEqual(String string, int length) {
    return string.length() <= length;
  }

  /**
   * Checks if the given number is smaller than the given number.
   * @param <T>    A subclass of the class number.
   * @param number The number to check.
   * @param max    The maximum number to check against.
   * @return True if the number is smaller than the length, false otherwise.
   */
  public static <T extends Number> boolean isSmallerThan(Number number, Number max) {
    return number.doubleValue() < max.doubleValue();
  }

  /**
   * Check if the given number is smaller than or equal to the given number.
   * @param <T>    A subclass of the class number.
   * @param number The number to check.
   * @return True if the number is smaller than or equal to the length, false otherwise.
   */
  public static <T extends Number> boolean isSmallerThanOrEqual(Number number, Number max) {
    return number.doubleValue() <= max.doubleValue();
  }

  /**
   * Checks if the given number is larger than the given number.
   * @param <T>    The type of the number.
   * @param number The number to check.
   * @param min    The minimum number to check against.
   * @return True if the number is larger than the length, false otherwise.
   */
  public static <T extends Number> boolean isLargerThan(Number number, Number min) {
    return number.doubleValue() > min.doubleValue();
  }

  /**
   * Check if the given number is larger than or equal to the given number.
   * @param <T>    A subclass of the class number.
   * @param number The number to check.
   * @return True if the number is larger than or equal to the length, false otherwise.
   */
  public static <T extends Number> boolean isLargerThanOrEqual(Number number, Number min) {
    return number.doubleValue() >= min.doubleValue();
  }

  /**
   * Check if the given string is larger than the given length.
   * @param string The string to check.
   * @param length The length to check against.
   * @return True if the string is larger than the length, false otherwise.
   */
  public static boolean isLargerThan(String string, int length) {
    return string.length() > length;
  }

  /**
   * Check if the given string is larger than or equal to the given length.
   * @param string The string to check.
   * @param length The length to check against.
   * @return True if the string is larger than or equal to the length, false otherwise.
   */
  public static boolean isLargerThanOrEqual(String string, int length) {
    return string.length() >= length;
  }

  /**
   * Check if the given string is between the given lengths.
   * @param string    The string to check.
   * @param minLength The minimum length to check against.
   * @param maxLength The maximum length to check against.
   * @return True if the string is between the lengths, false otherwise.
   */
  public static boolean isBetween(String string, int minLength, int maxLength) {
    return string.length() >= minLength && string.length() <= maxLength;
  }

  /**
   * Check if the given number is smaller than the given number.
   * @param <T>    A subclass of the class number.
   * @param number The number to check.
   * @param min    The minimum number to check against.
   * @return True if the number is smaller than the minimum, false otherwise.
   */
  public static <T extends Number> boolean isBetween(T number, T min, T max) {
    return number.doubleValue() >= min.doubleValue() && number.doubleValue() <= max.doubleValue();
  }

  /**
   * Check if the given date is after the given date.
   * @param <T>       The subclass of ChronoLocalDate.
   * @param afterDate The date to check.
   * @param beforeDate The date to check against.
   * @return True if the date is after the given date, false otherwise.
   */
  public static <T extends ChronoLocalDate> boolean isAfter(T afterDate, T beforeDate) {
    return afterDate.isAfter(beforeDate);
  }

  /**
   * Check if the given date is before the given date.
   * @param <T>      The subclass of ChronoLocalDate.
   * @param beforeDate The date to check.
   * @param afterDate The date to check against.
   * @return True if the date is before the given date, false otherwise.
   */
  public static <T extends ChronoLocalDate> boolean isBefore(T beforeDate, T afterDate) {
    return beforeDate.isBefore(afterDate);
  }

  /**
   * Check if a generic array is not null and not empty.
   * @param <T> The type of the array.
   * @param array The array to check.
   * @return True if the array is empty, false otherwise.
   */
  public static <T> boolean isNotNullOrEmpty(T[] array) {
    return array != null && array.length != 0;
  }

  public static <T extends Number> boolean validatePositive(T[] array) {
    for (T t : array) if (!isLargerThan(t, 0)) return false;

    return true;
  }
}
