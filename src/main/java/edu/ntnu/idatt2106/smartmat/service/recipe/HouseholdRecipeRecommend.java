package edu.ntnu.idatt2106.smartmat.service.recipe;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeRecommendation;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class that recommends recipes for a household.
 * It uses the household's food products to recommend recipes.
 * The more food products the household has, the more recipes it will recommend.
 * The recipes are sorted by recommendation, so the first recipe is the most recommended.
 * The recommendation is based on how many ingredients the household has for the recipe.
 * The more ingredients the household has for the recipe, the higher the recommendation.
 * The recommendation is also based on how many of the ingredients the household has.
 * The recommendation also takes into account how much of the ingredients the household has planned to use.
 * @author Callum G.
 * @version 1.0 28.04.2023
 */
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
      } else {
        score -= 10;
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
   * @param usedRecipes The recipes that are going to be used.
   * @return A collection of recipes that are sorted by recommendation for the household.
   */
  public static Collection<RecipeRecommendation> getRecommendedRecipes(
    Household household,
    Collection<Recipe> recipes,
    Collection<Recipe> usedRecipes
  ) {
    final Map<Ingredient, Double> householdIngredients = household
      .getFoodProducts()
      .stream()
      .filter(i -> {
        usedRecipes
          .stream()
          .flatMap(r -> r.getIngredients().stream())
          .forEach(i2 -> {
            if (i2.getIngredient().getId() == i.getFoodProduct().getIngredient().getId()) {
              i.setAmountLeft(i.getAmountLeft() - i2.getAmount());
            }
          });
        return i.getAmountLeft() > 0;
      })
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
      return recipes
        .stream()
        .map(r -> RecipeRecommendation.builder().recipe(r).score(0).build())
        .toList();
    }

    return recipeIngredients
      .entrySet()
      .stream()
      .map(r ->
        RecipeRecommendation
          .builder()
          .recipe(r.getKey())
          .score(getRecipeScore(r.getValue(), householdIngredients))
          .build()
      )
      .sorted((r1, r2) -> compare(r1.getScore(), r2.getScore()))
      .toList();
  }
}
