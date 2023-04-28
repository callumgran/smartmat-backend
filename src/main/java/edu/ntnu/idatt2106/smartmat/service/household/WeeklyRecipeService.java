package edu.ntnu.idatt2106.smartmat.service.household;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipe;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipeId;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import java.util.Collection;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Service interface for the WeeklyRecipe entity
 * @author Callum G.
 * @version 1.0 - 28.04.2020
 */
@Service
public interface WeeklyRecipeService {
  boolean existsById(@NonNull WeeklyRecipe WeeklyRecipe) throws NullPointerException;

  WeeklyRecipe saveWeeklyRecipe(@NonNull WeeklyRecipe WeeklyRecipe)
    throws NullPointerException, IllegalArgumentException;

  void deleteWeeklyRecipe(@NonNull WeeklyRecipe WeeklyRecipe)
    throws NullPointerException, IllegalArgumentException;

  void deleteWeeklyRecipeById(@NonNull WeeklyRecipeId id)
    throws NullPointerException, IllegalArgumentException;

  void useRecipeDay(@NonNull WeeklyRecipe WeeklyRecipe)
    throws FoodProductNotFoundException, NullPointerException;

  Collection<Recipe> getRecipesForHousehold(@NonNull UUID householdId)
    throws NullPointerException, HouseholdNotFoundException;
}
