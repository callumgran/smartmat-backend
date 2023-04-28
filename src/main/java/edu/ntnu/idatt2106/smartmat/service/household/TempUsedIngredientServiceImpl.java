package edu.ntnu.idatt2106.smartmat.service.household;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.household.TempUsedIngredient;
import edu.ntnu.idatt2106.smartmat.model.household.TempUsedIngredientId;
import edu.ntnu.idatt2106.smartmat.repository.household.TempUsedIngredientRepository;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.HouseholdFoodProductService;
import io.micrometer.common.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.fauxpas.FauxPas;

/**
 * Service implementation for the TempUsedIngredient entity
 * @author Callum G.
 * @version 1.0 - 28.04.2020
 */
@Service
public class TempUsedIngredientServiceImpl implements TempUsedIngredientService {

  @Autowired
  private TempUsedIngredientRepository tempUsedIngredientRepository;

  @Autowired
  private HouseholdFoodProductService householdFoodProductService;

  /**
   * Checks if the tempUsedIngredient exists
   * @param tempUsedIngredient the tempUsedIngredient to check
   * @return true if the tempUsedIngredient exists, false otherwise
   * @throws NullPointerException if the tempUsedIngredient is null
   */
  @Override
  public boolean existsById(@NonNull TempUsedIngredient tempUsedIngredient)
    throws NullPointerException {
    return tempUsedIngredientRepository.existsById(
      new TempUsedIngredientId(tempUsedIngredient.getHousehold(), tempUsedIngredient.getDateToUse())
    );
  }

  /**
   * Saves the tempUsedIngredient
   * @param tempUsedIngredient the tempUsedIngredient to save
   * @return the saved tempUsedIngredient
   * @throws NullPointerException if the tempUsedIngredient is null
   */
  @Override
  public TempUsedIngredient saveTempUsedIngredient(@NonNull TempUsedIngredient tempUsedIngredient)
    throws NullPointerException {
    if (tempUsedIngredient.getHousehold() == null || tempUsedIngredient.getDateToUse() == null) {
      throw new NullPointerException("Household and dateToUse cannot be null");
    }
    if (existsById(tempUsedIngredient)) throw new IllegalArgumentException(
      "TempUsedIngredient already exists"
    );
    return tempUsedIngredientRepository.save(tempUsedIngredient);
  }

  /**
   * Deletes the tempUsedIngredient
   * @param tempUsedIngredient the tempUsedIngredient to delete
   * @throws NullPointerException if the tempUsedIngredient is null
   * @throws IllegalArgumentException if the tempUsedIngredient does not exist
   */
  @Override
  public void deleteTempUsedIngredient(@NonNull TempUsedIngredient tempUsedIngredient)
    throws NullPointerException, IllegalArgumentException {
    if (tempUsedIngredient.getHousehold() == null || tempUsedIngredient.getDateToUse() == null) {
      throw new NullPointerException("Household and dateToUse cannot be null");
    }
    if (!existsById(tempUsedIngredient)) throw new IllegalArgumentException(
      "TempUsedIngredient does not exist"
    );
    tempUsedIngredientRepository.delete(tempUsedIngredient);
  }

  /**
   * Uses the recipe for the day, and removes the ingredients from the household
   * @param tempUsedIngredient the tempUsedIngredient to use
   * @throws FoodProductNotFoundException if the foodproduct is not found
   * @throws NullPointerException if the tempUsedIngredient is null
   */
  @Override
  public void useRecipeDay(@NonNull TempUsedIngredient tempUsedIngredient)
    throws FoodProductNotFoundException, NullPointerException {
    tempUsedIngredient
      .getTempUsedIngredientAmount()
      .stream()
      .forEach(tempUsedIngredientAmount -> {
        FauxPas.throwingRunnable(() ->
          householdFoodProductService.removeAmountFoodProductFromHouseholdByIngredient(
            tempUsedIngredient.getHousehold(),
            tempUsedIngredientAmount.getIngredient(),
            tempUsedIngredientAmount.getAmount()
          )
        );
      });

    tempUsedIngredientRepository.delete(tempUsedIngredient);
  }
}
