package edu.ntnu.idatt2106.smartmat.service.foodproduct;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import lombok.NonNull;
import org.springframework.data.domain.Page;

/**
 * Service interface for the FoodProduct entity.
 * @author Callum G.
 * @version 1.0 21.04.2023
 */
public interface FoodProductService {
  boolean existsById(@NonNull Long id) throws NullPointerException;

  FoodProduct saveFoodProduct(@NonNull FoodProduct foodProduct) throws NullPointerException;

  FoodProduct getFoodProductById(@NonNull Long id)
    throws FoodProductNotFoundException, NullPointerException;

  FoodProduct updateFoodProduct(@NonNull FoodProduct foodProduct)
    throws FoodProductNotFoundException, NullPointerException;

  void deleteFoodProductById(@NonNull Long id)
    throws FoodProductNotFoundException, NullPointerException;

  Page<FoodProduct> searchFoodProductsPaginated(@NonNull SearchRequest searchRequest)
    throws NullPointerException;

  FoodProduct getFoodProductByEan(@NonNull String ean)
    throws FoodProductNotFoundException, NullPointerException;
}
