package edu.ntnu.idatt2106.smartmat.utils;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import java.util.Collection;

public class IngredientTitleMatcher {

  /**
   * Calculates the Levenshtein distance between two strings.
   *
   * @param s1 The first string
   * @param s2 The second string
   * @return The Levenshtein distance between the two strings
   */
  public static double distance(String s1, String s2) {
    int len1 = s1.length();
    int len2 = s2.length();

    int[][] dp = new int[len1 + 1][len2 + 1];

    for (int i = 0; i <= len1; i++) {
      dp[i][0] = i;
    }

    for (int j = 0; j <= len2; j++) {
      dp[0][j] = j;
    }

    for (int i = 1; i <= len1; i++) {
      for (int j = 1; j <= len2; j++) {
        int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
        dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
      }
    }

    return dp[len1][len2];
  }

  /**
   * Calculates the fuzzy match ratio between two strings.
   *
   * @param smallString The first string
   * @param largeString The second string
   * @return The fuzzy match ratio between the two strings
   */
  public static double fuzzyMatch(String smallString, String largeString) {
    smallString = smallString.toLowerCase();
    largeString = largeString.toLowerCase();

    int smallLength = smallString.length();
    int largeLength = largeString.length();

    double bestRatio = Double.MAX_VALUE;

    if (smallLength > largeLength) {
      return bestRatio;
    }

    for (int i = 0; i <= largeLength - smallLength; i++) {
      String subString = largeString.substring(i, i + smallLength);
      double distance = distance(smallString, subString);
      double ratio = distance / smallLength;
      if (ratio <= bestRatio) {
        bestRatio = ratio;
      }
    }

    return bestRatio;
  }

  /**
   * Gets the best match for a given title from a collection of ingredients.
   *
   * @param ingredients The collection of ingredients
   * @param title       The title
   * @return The best match
   */
  public static Ingredient getBestMatch(Collection<Ingredient> ingredients, String title) {
    double bestRatio = Double.MAX_VALUE;
    double minRatio = 0.2;
    Ingredient bestMatch = null;
    for (Ingredient ingredient : ingredients) {
      double ratio = fuzzyMatch(ingredient.getName(), title);
      if (ratio < bestRatio && ratio < minRatio) {
        bestRatio = ratio;
        bestMatch = ingredient;
      }
    }
    return bestMatch;
  }
}
