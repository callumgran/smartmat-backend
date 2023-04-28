package edu.ntnu.idatt2106.smartmat.repository.household;

import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipe;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipeId;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for the WeeklyRecipe entity
 * @author Callum G.
 * @version 1.0 - 28.04.2020
 */
@Repository
public interface WeeklyRecipeRepository extends JpaRepository<WeeklyRecipe, WeeklyRecipeId> {
  /**
   * Finds all WeeklyRecipe recipes by the household id.
   * @param householdId The id of the household.
   * @return A collection of recipes.
   */
  @Query("SELECT w.recipe FROM WeeklyRecipe w WHERE w.id.household.id = ?1")
  Optional<Collection<Recipe>> findAllRecipesByHousehold(UUID houseHoldId);
}
