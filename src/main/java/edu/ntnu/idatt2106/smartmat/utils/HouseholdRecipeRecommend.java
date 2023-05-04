package edu.ntnu.idatt2106.smartmat.utils;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeRecommendation;
import java.util.ArrayList;
import java.util.Collection;
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
 * @version 1.2 04.05.2023
 */
public class HouseholdRecipeRecommend {

  /**
   * Returns a collection of recommended recipes for a household.
   * @param household The household to recommend recipes for.
   * @param recipes The recipes to recommend.
   * @return A collection of recommended recipes for a household.
   */
  private static double getRecipeScore(
    Recipe recipeIngredients,
    Collection<HouseholdFoodProduct> householdIngredients
  ) {
    double score = 0;
    for (RecipeIngredient ingredient : recipeIngredients.getIngredients()) {
      System.out.println("Ingredient: " + ingredient.getIngredient().getName());
      System.out.println("Amount: " + ingredient.getAmount());
      if (
        householdIngredients
          .stream()
          .map(HouseholdFoodProduct::getFoodProduct)
          .map(FoodProduct::getIngredient)
          .anyMatch(i -> i.getId() == ingredient.getIngredient().getId())
      ) {
        double householdAmount = householdIngredients
          .stream()
          .filter(hfp ->
            hfp.getFoodProduct().getIngredient().getId() == ingredient.getIngredient().getId()
          )
          .map(hfp -> UnitUtils.getNormalizedUnit(hfp))
          .reduce((a, b) -> a + b)
          .orElse(0.0D);

        System.out.println("Household amount: " + householdAmount);

        double ingredientAmount = UnitUtils.getNormalizedUnit(ingredient);

        double used = householdAmount - ingredientAmount;

        if (used < 0) {
          double tmp = 100 + ((used / ingredientAmount) * 100);
          System.out.println("tmp: " + tmp);
          score += tmp > 10 ? tmp : 10;
          System.out.println("Score: " + score);
        } else {
          double tmp = 100 - ((used / ingredientAmount) * 100);
          score += tmp > 50 ? tmp : 50;
          System.out.println("Score: " + score);
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
   * Returns a collection of household food products that are going to be used.
   * Starts by iterating through the recipes and multiplying the amount of ingredients by the number of members in the household.
   * Then iterates through the household food products and removes the amount of ingredients from the household food products.
   * If the amount of ingredients is greater than the amount of household food products, the amount of ingredients is set to 0.
   * @param household The household to get household food products from.
   * @param usedRecipes The recipes that are going to be used.
   * @return A collection of household food products that are going to be used.
   */
  private static Collection<HouseholdFoodProduct> getRealHouseholdFoodProducts(
    final int householdSize,
    final Collection<HouseholdFoodProduct> householdFoodProducts,
    final Collection<Recipe> usedRecipes
  ) {
    final Collection<RecipeIngredient> usedIngredients = usedRecipes
      .stream()
      .flatMap(r -> r.getIngredients().stream())
      .map(ri -> {
        return new RecipeIngredient(
          ri.getRecipe(),
          ri.getIngredient(),
          ri.getAmount() * householdSize,
          ri.getUnit()
        );
      })
      .toList();

    return householdFoodProducts
      .stream()
      .filter(hfp -> {
        usedIngredients
          .stream()
          .forEach(ri -> {
            if (ri.getIngredient().getId() == hfp.getFoodProduct().getIngredient().getId()) {
              if (ri.getAmount() > 0) {
                double amount = UnitUtils.removeRecipeIngredientAmountFromHouseholdFoodProductAmount(
                  ri,
                  hfp
                );

                if (amount > 0) {
                  ri.setAmount(0.0D);
                } else {
                  ri.setAmount(UnitUtils.getOriginalUnit((-1.0 * amount), ri));
                }
              }
            }
          });
        return hfp.getAmountLeft() > 0;
      })
      .toList();
  }

  /**
   * Returns a map of recipes and their ingredients that are going to be used.
   * Starts by filtering the recipes to only include recipes that have ingredients that the household has.
   * Then iterates through the recipes and multiplies the amount of ingredients by the number of members in the household.
   * @param householdSize The size of the household.
   * @param recipes The recipes to get ingredients from.
   * @param householdFoodProducts The household food products to get ingredients from.
   * @return A map of recipes and their ingredients that are going to be used.
   */
  private static Collection<Recipe> filterRecipesToMap(
    final int householdSize,
    final Collection<Recipe> recipes,
    final Collection<HouseholdFoodProduct> householdFoodProducts
  ) {
    return recipes
      .stream()
      .filter(r ->
        r
          .getIngredients()
          .stream()
          .map(RecipeIngredient::getIngredient)
          .anyMatch(i ->
            householdFoodProducts
              .stream()
              .anyMatch(hfp -> hfp.getFoodProduct().getIngredient().getId() == i.getId())
          )
      )
      .map(r -> {
        r.setIngredients(
          r
            .getIngredients()
            .stream()
            .map(ri -> {
              System.out.println(ri.getIngredient().getName() + " : " + ri.getAmount());
              return new RecipeIngredient(
                ri.getRecipe(),
                ri.getIngredient(),
                ri.getAmount() * householdSize,
                ri.getUnit()
              );
            })
            .collect(Collectors.toSet())
        );
        return r;
      })
      .toList();
  }

  /**
   * Returns a collection of all recipes and gives them a recommendation of 0.
   * This is used to add recipes that are not recommended if there are not enough recipes that are recommended.
   * @param recipes The recipes to add to the collection.
   * @return A collection of all recipes and gives them a recommendation of 0.
   */
  private static Collection<RecipeRecommendation> getAllList(Collection<Recipe> recipes) {
    return recipes.stream().map(r -> new RecipeRecommendation(r, 0.0D)).toList();
  }

  private static Collection<RecipeRecommendation> sortRecommendations(
    final Collection<Recipe> recipes,
    final Collection<HouseholdFoodProduct> householdIngredients
  ) {
    return recipes
      .stream()
      .map(e -> {
        double score = getRecipeScore(e, householdIngredients);
        System.out.println(e.getName() + ": " + score);
        return new RecipeRecommendation(e, score);
      })
      .sorted((a, b) -> compare(a.getScore(), b.getScore()))
      .toList();
  }

  /**
   * Returns a collection of recipes that are sorted by recommendation for the household.
   * @param household The household to recommend recipes for.
   * @param recipes The recipes to recommend from.
   * @param usedRecipes The recipes that are going to be used.
   * @return A collection of recipes that are sorted by recommendation for the household.
   */
  public static Collection<RecipeRecommendation> getRecommendedRecipes(
    final Household household,
    final Collection<Recipe> recipes,
    final Collection<Recipe> usedRecipes
  ) {
    final Collection<HouseholdFoodProduct> householdFoodProducts = household.getFoodProducts();

    final int householdSize = household.getMembers().size();

    final Collection<HouseholdFoodProduct> householdIngredients = getRealHouseholdFoodProducts(
      householdSize,
      householdFoodProducts,
      usedRecipes
    );

    final Collection<Recipe> recipeIngredients = filterRecipesToMap(
      householdSize,
      recipes,
      householdIngredients
    );

    if (recipeIngredients.isEmpty()) {
      return new ArrayList<>();
    }

    return sortRecommendations(recipeIngredients, householdIngredients);
  }
}
