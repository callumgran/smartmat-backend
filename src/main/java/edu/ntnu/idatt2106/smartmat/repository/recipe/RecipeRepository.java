package edu.ntnu.idatt2106.smartmat.repository.recipe;

import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for recipe operations on the database.
 * @author Simen G.
 * @version 1.0 - 20.04.23
 */
@Repository
public interface RecipeRepository
  extends JpaRepository<Recipe, UUID>, JpaSpecificationExecutor<Recipe> {
  /**
   * Method to search for recipes with a name containing the specified string.
   * @param name the string to search for in recipe names.
   * @return a collection of recipes with names containing the specified string.
   */
  Optional<Collection<Recipe>> findByNameContainingIgnoreCase(String name);
}
