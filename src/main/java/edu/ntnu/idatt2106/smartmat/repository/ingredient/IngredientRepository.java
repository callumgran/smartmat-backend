package edu.ntnu.idatt2106.smartmat.repository.ingredient;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * Repository for ingredient operations on the database.
 * @author Tobias. O, Carl G.
 * @version 1.1 - 25.04.2023
 */
@Repository
public interface IngredientRepository
  extends JpaRepository<Ingredient, Long>, JpaSpecificationExecutor<Ingredient> {
  /**
   * Method to search for ingredients with a name containing the specified string.
   * @param name the string to search for in ingredient names.
   * @return a collection of ingredients with the specified name.
   */
  Optional<Collection<Ingredient>> findByName(@NonNull String name);
}
