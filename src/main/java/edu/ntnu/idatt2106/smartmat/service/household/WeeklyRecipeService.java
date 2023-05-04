package edu.ntnu.idatt2106.smartmat.service.household;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipe;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipeId;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Service interface for the WeeklyRecipe entity
 * @author Callum G.
 * @version 1.0 - 28.04.2023
 */
@Service
public interface WeeklyRecipeService {
  boolean existsById(@NonNull WeeklyRecipe WeeklyRecipe) throws NullPointerException;

  WeeklyRecipe getWeeklyRecipeById(@NonNull WeeklyRecipeId id) throws NullPointerException;

  WeeklyRecipe saveWeeklyRecipe(@NonNull WeeklyRecipe WeeklyRecipe)
    throws NullPointerException, IllegalArgumentException;

  void deleteWeeklyRecipe(@NonNull WeeklyRecipe WeeklyRecipe)
    throws NullPointerException, IllegalArgumentException;

  void deleteWeeklyRecipeById(@NonNull WeeklyRecipeId id)
    throws NullPointerException, IllegalArgumentException;

  void useRecipeDay(@NonNull WeeklyRecipe WeeklyRecipe)
    throws FoodProductNotFoundException, NullPointerException;

  Collection<WeeklyRecipe> getRecipesForHousehold(@NonNull UUID householdId)
    throws NullPointerException;

  Collection<WeeklyRecipe> getRecipesForHouseholdWeek(
    @NonNull UUID householdId,
    @NonNull LocalDate monday
  ) throws NullPointerException;

  Collection<ShoppingListItem> getShoppingListItemsForHouseholdWeek(
    @NonNull UUID householdId,
    @NonNull LocalDate monday
  ) throws NullPointerException;
}
