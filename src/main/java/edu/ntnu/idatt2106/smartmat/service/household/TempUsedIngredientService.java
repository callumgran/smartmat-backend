package edu.ntnu.idatt2106.smartmat.service.household;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.household.TempUsedIngredient;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Service interface for the TempUsedIngredient entity
 * @author Callum G.
 * @version 1.0 - 28.04.2020
 */
@Service
public interface TempUsedIngredientService {
  boolean existsById(@NonNull TempUsedIngredient tempUsedIngredient) throws NullPointerException;

  TempUsedIngredient saveTempUsedIngredient(@NonNull TempUsedIngredient tempUsedIngredient)
    throws NullPointerException, IllegalArgumentException;

  void deleteTempUsedIngredient(@NonNull TempUsedIngredient tempUsedIngredient)
    throws NullPointerException, IllegalArgumentException;

  void useRecipeDay(@NonNull TempUsedIngredient tempUsedIngredient)
    throws FoodProductNotFoundException, NullPointerException;
}
