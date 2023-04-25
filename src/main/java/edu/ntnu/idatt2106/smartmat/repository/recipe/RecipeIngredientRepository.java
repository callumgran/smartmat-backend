package edu.ntnu.idatt2106.smartmat.repository.recipe;

import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredientId;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for recipe ingredients.
 * This class is responsible for all database operations related to recipe ingredients.
 * @author Callum G.
 * @version 1.0 - 25.04.2023
 */
@Transactional
@Repository
public interface RecipeIngredientRepository
  extends JpaRepository<RecipeIngredient, RecipeIngredientId> {
  /**
   * Method to delete all recipe ingredients with the specified recipe id.
   * @param id the id of the recipe to delete all ingredients for.
   */
  @Modifying
  @Query("DELETE FROM RecipeIngredient ri WHERE ri.id.recipe.id = ?1")
  void deleteAllByRecipeId(@NonNull UUID id) throws NullPointerException;
}
