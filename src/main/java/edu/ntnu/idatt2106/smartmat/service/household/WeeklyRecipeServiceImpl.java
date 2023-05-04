package edu.ntnu.idatt2106.smartmat.service.household;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipe;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipeId;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import edu.ntnu.idatt2106.smartmat.repository.household.WeeklyRecipeRepository;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.HouseholdFoodProductService;
import edu.ntnu.idatt2106.smartmat.service.statistic.FoodProductHistoryService;
import edu.ntnu.idatt2106.smartmat.utils.UnitUtils;
import io.micrometer.common.lang.NonNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.fauxpas.FauxPas;

/**
 * Service implementation for the WeeklyRecipe entity
 * @author Callum G.
 * @version 1.0 - 28.04.2020
 */
@Service
public class WeeklyRecipeServiceImpl implements WeeklyRecipeService {

  @Autowired
  private WeeklyRecipeRepository weeklyRecipeRepository;

  @Autowired
  private FoodProductHistoryService foodProductHistoryService;

  @Autowired
  private HouseholdFoodProductService householdFoodProductService;

  /**
   * Checks if the WeeklyRecipe exists
   * @param weeklyRecipe the WeeklyRecipe to check
   * @return true if the WeeklyRecipe exists, false otherwise
   * @throws NullPointerException if the WeeklyRecipe is null
   */
  @Override
  public boolean existsById(@NonNull WeeklyRecipe weeklyRecipe) throws NullPointerException {
    return weeklyRecipeRepository.existsById(
      new WeeklyRecipeId(weeklyRecipe.getHousehold(), weeklyRecipe.getDateToUse())
    );
  }

  /**
   * Gets the WeeklyRecipe by id
   * @param id the id of the WeeklyRecipe to get
   * @return the WeeklyRecipe
   * @throws NullPointerException if the id is null or the WeeklyRecipe does not exist
   */
  @Override
  public WeeklyRecipe getWeeklyRecipeById(@NonNull WeeklyRecipeId id) throws NullPointerException {
    return weeklyRecipeRepository
      .findById(id)
      .orElseThrow(() -> new NullPointerException("Ukentlig oppskrift finnes ikke"));
  }

  /**
   * Saves the WeeklyRecipe
   * @param weeklyRecipe the WeeklyRecipe to save
   * @return the saved WeeklyRecipe
   * @throws NullPointerException if the WeeklyRecipe is null
   */
  @Override
  public WeeklyRecipe saveWeeklyRecipe(@NonNull WeeklyRecipe weeklyRecipe)
    throws NullPointerException {
    if (weeklyRecipe.getHousehold() == null || weeklyRecipe.getDateToUse() == null) {
      throw new NullPointerException("Household and dateToUse cannot be null");
    }
    if (existsById(weeklyRecipe)) throw new IllegalArgumentException("WeeklyRecipe already exists");
    return weeklyRecipeRepository.save(weeklyRecipe);
  }

  /**
   * Deletes the WeeklyRecipe
   * @param weeklyRecipe the WeeklyRecipe to delete
   * @throws NullPointerException if the WeeklyRecipe is null
   * @throws IllegalArgumentException if the WeeklyRecipe does not exist
   */
  @Override
  public void deleteWeeklyRecipe(@NonNull WeeklyRecipe weeklyRecipe)
    throws NullPointerException, IllegalArgumentException {
    if (weeklyRecipe.getHousehold() == null || weeklyRecipe.getDateToUse() == null) {
      throw new NullPointerException("Household and dateToUse cannot be null");
    }
    if (!existsById(weeklyRecipe)) throw new IllegalArgumentException(
      "WeeklyRecipe does not exist"
    );
    weeklyRecipeRepository.delete(weeklyRecipe);
  }

  /**
   * Deletes the WeeklyRecipe by id
   * @param id the id of the WeeklyRecipe to delete
   * @throws NullPointerException if the id is null
   * @throws IllegalArgumentException if the WeeklyRecipe does not exist
   */
  @Override
  public void deleteWeeklyRecipeById(@NonNull WeeklyRecipeId id)
    throws NullPointerException, IllegalArgumentException {
    if (!weeklyRecipeRepository.existsById(id)) throw new IllegalArgumentException(
      "WeeklyRecipe does not exist"
    );
    weeklyRecipeRepository.deleteById(id);
  }

  /**
   * Uses the recipe for the day, and removes the ingredients from the household
   * @param weeklyRecipe the WeeklyRecipe to use
   * @throws FoodProductNotFoundException if the foodproduct is not found
   * @throws NullPointerException if the WeeklyRecipe is null
   */
  @Override
  public void useRecipeDay(@NonNull WeeklyRecipe weeklyRecipe)
    throws FoodProductNotFoundException, NullPointerException {
    Collection<RecipeIngredient> ingredients = weeklyRecipe
      .getRecipe()
      .getIngredients()
      .stream()
      .map(ingredient -> {
        ingredient.setAmount(ingredient.getAmount() * weeklyRecipe.getPortions());
        return ingredient;
      })
      .toList();

    ingredients
      .stream()
      .forEach(i -> {
        FauxPas.throwingRunnable(() -> {
          double ingredientAmount = UnitUtils.getNormalizedUnit(i);

          for (HouseholdFoodProduct hfp : weeklyRecipe.getHousehold().getFoodProducts()) {
            if (
              hfp.getFoodProduct().getIngredient().equals(i.getIngredient()) && ingredientAmount > 0
            ) {
              double amount = UnitUtils.getNormalizedUnit(hfp);
              if (amount >= ingredientAmount) {
                hfp.setAmountLeft(UnitUtils.getOriginalUnit(amount - ingredientAmount, hfp));
                householdFoodProductService.saveFoodProduct(hfp);
                foodProductHistoryService.saveFoodProductHistory(
                  FoodProductHistory
                    .builder()
                    .household(weeklyRecipe.getHousehold())
                    .foodProduct(hfp.getFoodProduct())
                    .thrownAmount(0)
                    .amount(ingredientAmount)
                    .date(LocalDate.now())
                    .build()
                );
                return;
              } else {
                foodProductHistoryService.saveFoodProductHistory(
                  FoodProductHistory
                    .builder()
                    .household(weeklyRecipe.getHousehold())
                    .foodProduct(hfp.getFoodProduct())
                    .thrownAmount(0)
                    .amount(amount)
                    .date(LocalDate.now())
                    .build()
                );
                ingredientAmount -= amount;
                hfp.setAmountLeft(0);
                householdFoodProductService.deleteFoodProductById(hfp.getId());
              }
            }
          }
        });
      });
    weeklyRecipe.setUsed(true);
    weeklyRecipeRepository.save(weeklyRecipe);
  }

  /**
   * Gets the recipes chosen for the household
   * @param household the household to get the recipes for
   * @return the recipes chosen for the household
   * @throws NullPointerException if the household is null
   * @throws IllegalArgumentException if the household does not exist
   */
  @Override
  public Collection<WeeklyRecipe> getRecipesForHousehold(@NonNull UUID household)
    throws NullPointerException {
    return weeklyRecipeRepository
      .findAllRecipesByHousehold(household)
      .orElseThrow(() -> new NullPointerException("Husholdning ikke funnet"));
  }

  /**
   * Gets the recipes for the household and week
   * @param household the household to get the recipes for
   * @param monday the monday of the week to get the recipes for
   * @return the recipes for the household and week
   * @throws NullPointerException if the household or monday is null
   * @throws IllegalArgumentException if the household does not exist
   */
  @Override
  public Collection<WeeklyRecipe> getRecipesForHouseholdWeek(
    @NonNull UUID household,
    @NonNull LocalDate monday
  ) throws NullPointerException {
    return weeklyRecipeRepository
      .findAllRecipesByHouseholdAndWeek(household, monday, monday.plusDays(6))
      .orElseThrow(() -> new NullPointerException("Ingen oppskrifter funnet for denne uken"));
  }

  /**
   * Gets the shopping list items needed for the household and week
   * @param householdId the household to get the shopping list items for
   * @param monday the monday of the week to get the shopping list items for
   * @return the shopping list items needed for the household and week
   * @throws NullPointerException if the household or monday is null
   * @throws IllegalArgumentException if the household does not exist
   */
  @Override
  public Collection<ShoppingListItem> getShoppingListItemsForHouseholdWeek(
    @NonNull UUID householdId,
    @NonNull LocalDate monday
  ) throws NullPointerException {
    final Collection<WeeklyRecipe> recipes = getRecipesForHouseholdWeek(householdId, monday);
    final Household household = recipes.stream().toList().get(0).getHousehold();
    final int portions = recipes.stream().toList().get(0).getPortions();
    final Collection<HouseholdFoodProduct> householdFoodProducts = household
      .getFoodProducts()
      .stream()
      .collect(Collectors.toUnmodifiableList());

    final Collection<RecipeIngredient> usedIngredients = recipes
      .stream()
      .flatMap(r -> r.getRecipe().getIngredients().stream())
      .map(ri -> {
        ri.setAmount(ri.getAmount() * portions);
        return ri;
      })
      .toList();

    return usedIngredients
      .stream()
      .filter(ri -> {
        householdFoodProducts.forEach(hfp -> {
          if (ri.getIngredient().getId() == hfp.getFoodProduct().getIngredient().getId()) {
            if (hfp.getAmountLeft() > 0) {
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

        return ri.getAmount() > 0;
      })
      .map(ri -> {
        return ShoppingListItem
          .builder()
          .ingredient(ri.getIngredient())
          .amount(ri.getAmount())
          .checked(false)
          .build();
      })
      .toList();
  }
}
