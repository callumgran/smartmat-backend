package edu.ntnu.idatt2106.smartmat.service.foodproduct;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Service interface for the FoodProduct entity.
 * @author Callum G.
 * @version 1.0 21.04.2023
 */
@Service
public interface HouseholdFoodProductService {
  boolean existsById(@NonNull UUID id) throws NullPointerException;

  HouseholdFoodProduct saveFoodProduct(@NonNull HouseholdFoodProduct foodProduct)
    throws NullPointerException;

  HouseholdFoodProduct getFoodProductById(@NonNull UUID id)
    throws FoodProductNotFoundException, NullPointerException;

  HouseholdFoodProduct updateFoodProduct(@NonNull HouseholdFoodProduct foodProduct)
    throws FoodProductNotFoundException, NullPointerException;

  void deleteFoodProductById(@NonNull UUID id)
    throws FoodProductNotFoundException, NullPointerException;

  Page<HouseholdFoodProduct> searchFoodProducts(@NonNull SearchRequest searchRequest)
    throws NullPointerException;

  HouseholdFoodProduct findHouseholdFoodProductByIdAndEAN(@NonNull UUID id, @NonNull String EAN)
    throws FoodProductNotFoundException, NullPointerException;
}
