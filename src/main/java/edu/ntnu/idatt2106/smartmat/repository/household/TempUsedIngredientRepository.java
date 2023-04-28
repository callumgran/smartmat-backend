package edu.ntnu.idatt2106.smartmat.repository.household;

import edu.ntnu.idatt2106.smartmat.model.household.TempUsedIngredient;
import edu.ntnu.idatt2106.smartmat.model.household.TempUsedIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the TempUsedIngredient entity
 * @author Callum G.
 * @version 1.0 - 28.04.2020
 */
@Repository
public interface TempUsedIngredientRepository
  extends JpaRepository<TempUsedIngredient, TempUsedIngredientId> {}
