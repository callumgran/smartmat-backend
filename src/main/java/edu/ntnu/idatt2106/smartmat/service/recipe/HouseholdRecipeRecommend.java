package edu.ntnu.idatt2106.smartmat.service.recipe;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class HouseholdRecipeRecommend {

  private static double getRecipeScore(
    Map<Ingredient, Double> recipeIngredients,
    Map<Ingredient, Double> householdIngredients
  ) {
    double score = 0;
    for (Map.Entry<Ingredient, Double> ingredientEntry : recipeIngredients.entrySet()) {
      if (householdIngredients.containsKey(ingredientEntry.getKey())) {
        double overused =
          ingredientEntry.getValue() - householdIngredients.get(ingredientEntry.getKey());
        if (overused > 0) {
          double add = 10 * (10 - overused);
          score += add > 50 ? add : 50;
        } else {
          score +=
            ingredientEntry.getValue() / householdIngredients.get(ingredientEntry.getKey()) * 100;
        }
      }
    }

    return score;
  }

  private static int compare(Double a, Double b) {
    return Double.compare(b, a);
  }

  /**
   * Returns a collection of recipes that are sorted by recommendation for the household.
   * @param household The household to recommend recipes for.
   * @param recipes The recipes to recommend from.
   * @return A collection of recipes that are sorted by recommendation for the household.
   */
  public static Collection<Recipe> getRecommendedRecipes(
    Household household,
    Collection<Recipe> recipes
  ) {
    final Map<Ingredient, Double> householdIngredients = household
      .getFoodProducts()
      .stream()
      .collect(
        Collectors.toMap(
          i -> i.getFoodProduct().getIngredient(),
          HouseholdFoodProduct::getAmountLeft
        )
      );
    final Map<Recipe, Map<Ingredient, Double>> recipeIngredients = recipes
      .stream()
      .filter(r ->
        r
          .getIngredients()
          .stream()
          .map(RecipeIngredient::getIngredient)
          .anyMatch(householdIngredients::containsKey)
      )
      .collect(
        Collectors.toMap(
          i -> i,
          r ->
            r
              .getIngredients()
              .stream()
              .collect(
                Collectors.toMap(
                  i -> i.getIngredient(),
                  i -> i.getAmount() * household.getMembers().size()
                )
              )
        )
      );

    if (recipeIngredients.isEmpty()) {
      return recipes;
    }

    return recipeIngredients
      .entrySet()
      .stream()
      .collect(
        Collectors.toMap(Map.Entry::getKey, e -> getRecipeScore(e.getValue(), householdIngredients))
      )
      .entrySet()
      .stream()
      .sorted((e1, e2) -> compare(e1.getValue(), e2.getValue()))
      .map(Map.Entry::getKey)
      .collect(Collectors.toList());
  }
}
