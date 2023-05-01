package edu.ntnu.idatt2106.smartmat.service.household;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipe;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipeId;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import edu.ntnu.idatt2106.smartmat.repository.household.WeeklyRecipeRepository;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.HouseholdFoodProductService;
import edu.ntnu.idatt2106.smartmat.service.statistic.FoodProductHistoryService;
import io.micrometer.common.lang.NonNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
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
    weeklyRecipe
      .getRecipe()
      .getIngredients()
      .stream()
      .forEach(ingredient -> {
        FauxPas.throwingRunnable(() -> {
          householdFoodProductService.removeAmountFoodProductFromHouseholdByIngredient(
            weeklyRecipe.getHousehold(),
            ingredient.getIngredient(),
            ingredient.getAmount()
          );
          HouseholdFoodProduct product = householdFoodProductService.getFoodProductFromHouseholdByIngredient(
            weeklyRecipe.getHousehold(),
            ingredient.getIngredient()
          );
          for (int i = 0; i < ingredient.getAmount(); i++) {
            foodProductHistoryService.saveFoodProductHistory(
              FoodProductHistory
                .builder()
                .household(weeklyRecipe.getHousehold())
                .foodProduct(product.getFoodProduct())
                .thrownAmount(0)
                .date(LocalDate.now())
                .build()
            );
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
    throws NullPointerException, HouseholdNotFoundException {
    return weeklyRecipeRepository
      .findAllRecipesByHousehold(household)
      .orElseThrow(() -> new HouseholdNotFoundException("Husholdning ikke funnet"));
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
  ) throws NullPointerException, HouseholdNotFoundException {
    return weeklyRecipeRepository
      .findAllRecipesByHouseholdAndWeek(household, monday, monday.plusDays(6))
      .orElseThrow(() -> new NullPointerException("Ingen oppskrifter funnet for denne uken"));
  }
}
