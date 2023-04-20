package edu.ntnu.idatt2106.smartmat.repository.ingredient;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * Repository for ingredient operations on the database.
 * @author Tobias. O
 * @version 1.0 - 19.04.2023
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
  Optional<Collection<Ingredient>> findByName(@NonNull String name);
}
