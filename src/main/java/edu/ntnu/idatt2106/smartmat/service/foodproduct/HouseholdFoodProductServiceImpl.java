package edu.ntnu.idatt2106.smartmat.service.foodproduct;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.filtering.SearchSpecification;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.repository.foodproduct.HouseholdFoodProductRepository;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * Service interface for the FoodProduct entity.
 * @author Callum G.
 * @version 1.0 21.04.2023
 */
public class HouseholdFoodProductServiceImpl implements HouseholdFoodProductService {

  @Autowired
  private HouseholdFoodProductRepository householdFoodProductRepository;

  /**
   * Checks if a FoodProduct exists in the database.
   * @param id The id of the FoodProduct to be checked.
   * @return True if the FoodProduct exists, false otherwise.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public boolean existsById(@NonNull UUID id) throws NullPointerException {
    return householdFoodProductRepository.existsById(id);
  }

  /**
   * Saves a FoodProduct to the database.
   * @param foodProduct The FoodProduct to be saved.
   * @return The saved FoodProduct.
   * @throws NullPointerException If the FoodProduct is null.
   */
  @Override
  public HouseholdFoodProduct saveFoodProduct(@NonNull HouseholdFoodProduct foodProduct)
    throws NullPointerException {
    if (foodProduct.getId() != null && existsById(foodProduct.getId())) {
      throw new IllegalArgumentException("Husholdnings matproduktet finnes allerede.");
    }
    return householdFoodProductRepository.save(foodProduct);
  }

  /**
   * Gets a FoodProduct from the database by its id.
   * @param id The id of the FoodProduct to be retrieved.
   * @return The retrieved FoodProduct.
   * @throws FoodProductNotFoundException If the FoodProduct is not found.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public HouseholdFoodProduct getFoodProductById(@NonNull UUID id)
    throws FoodProductNotFoundException, NullPointerException {
    return householdFoodProductRepository
      .findById(id)
      .orElseThrow(FoodProductNotFoundException::new);
  }

  /**
   * Updates a FoodProduct in the database.
   * @param foodProduct The FoodProduct to be updated.
   * @return The updated FoodProduct.
   * @throws FoodProductNotFoundException If the FoodProduct is not found.
   * @throws NullPointerException If the FoodProduct is null.
   */
  @Override
  public HouseholdFoodProduct updateFoodProduct(@NonNull HouseholdFoodProduct foodProduct)
    throws FoodProductNotFoundException, NullPointerException {
    if (!existsById(foodProduct.getId())) {
      throw new FoodProductNotFoundException();
    }
    return householdFoodProductRepository.save(foodProduct);
  }

  /**
   * Deletes a FoodProduct from the database.
   * @param id The id of the FoodProduct to be deleted.
   * @throws FoodProductNotFoundException If the FoodProduct is not found.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public void deleteFoodProductById(@NonNull UUID id)
    throws FoodProductNotFoundException, NullPointerException {
    if (!existsById(id)) {
      throw new FoodProductNotFoundException();
    }
    householdFoodProductRepository.deleteById(id);
  }

  /**
   * Searches for FoodProducts in the database using a SearchRequest.
   * @param searchRequest The SearchRequest to be used for searching.
   * @return A Page of FoodProducts.
   * @throws NullPointerException If the SearchRequest is null.
   */
  @Override
  public Page<HouseholdFoodProduct> searchFoodProducts(@NonNull SearchRequest searchRequest)
    throws NullPointerException {
    return householdFoodProductRepository.findAll(
      new SearchSpecification<HouseholdFoodProduct>(searchRequest),
      SearchSpecification.getPageable(searchRequest)
    );
  }

  /**
   * Finds a FoodProduct by its id and EAN.
   * @param id The id of the FoodProduct to be found.
   * @param EAN The EAN of the FoodProduct to be found.
   * @return The found FoodProduct.
   * @throws FoodProductNotFoundException If the FoodProduct is not found.
   * @throws NullPointerException If the id or EAN is null.
   */
  @Override
  public HouseholdFoodProduct findHouseholdFoodProductByIdAndEAN(
    @NonNull UUID id,
    @NonNull String EAN
  ) throws FoodProductNotFoundException, NullPointerException {
    return householdFoodProductRepository
      .findHouseholdFoodProductByHouseholdAndEAN(id, EAN)
      .orElseThrow(FoodProductNotFoundException::new);
  }
}
