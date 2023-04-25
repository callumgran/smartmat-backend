package edu.ntnu.idatt2106.smartmat.service.foodproduct;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.filtering.SearchSpecification;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.repository.foodproduct.FoodProductRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * Service interface for the FoodProduct entity.
 * @author Callum G.
 * @version 1.0 21.04.2023
 */
public class FoodProductServiceImpl implements FoodProductService {

  @Autowired
  private FoodProductRepository foodProductRepository;

  /**
   * Checks if a FoodProduct exists in the database by its id.
   * @param id The id of the FoodProduct to be checked.
   * @return True if the FoodProduct exists, false otherwise.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public boolean existsById(@NonNull Long id) throws NullPointerException {
    return foodProductRepository.existsById(id);
  }

  /**
   * Saves a FoodProduct to the database.
   * @param foodProduct The FoodProduct to be saved.
   * @return The saved FoodProduct.
   * @throws NullPointerException If the FoodProduct is null.
   */
  @Override
  public FoodProduct saveFoodProduct(@NonNull FoodProduct foodProduct) throws NullPointerException {
    if (foodProduct.getId() != null && existsById(foodProduct.getId())) {
      throw new IllegalArgumentException("Matproduktet finnes allerede.");
    }
    return foodProductRepository.save(foodProduct);
  }

  /**
   * Gets a FoodProduct from the database by its id.
   * @param id The id of the FoodProduct to be retrieved.
   * @return The retrieved FoodProduct.
   * @throws FoodProductNotFoundException If the FoodProduct is not found.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public FoodProduct getFoodProductById(@NonNull Long id)
    throws FoodProductNotFoundException, NullPointerException {
    return foodProductRepository.findById(id).orElseThrow(FoodProductNotFoundException::new);
  }

  /**
   * Updates a FoodProduct in the database.
   * @param foodProduct The FoodProduct to be updated.
   * @return The updated FoodProduct.
   * @throws FoodProductNotFoundException If the FoodProduct is not found.
   * @throws NullPointerException If the FoodProduct is null.
   */
  @Override
  public FoodProduct updateFoodProduct(@NonNull FoodProduct foodProduct)
    throws FoodProductNotFoundException, NullPointerException {
    if (!existsById(foodProduct.getId())) {
      throw new FoodProductNotFoundException("Matproduktet finnes ikke.");
    }
    return foodProductRepository.save(foodProduct);
  }

  /**
   * Deletes a FoodProduct from the database by its id.
   * @param id The id of the FoodProduct to be deleted.
   * @throws FoodProductNotFoundException If the FoodProduct is not found.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public void deleteFoodProductById(@NonNull Long id)
    throws FoodProductNotFoundException, NullPointerException {
    if (!existsById(id)) {
      throw new FoodProductNotFoundException();
    }
    foodProductRepository.delete(getFoodProductById(id));
  }

  /**
   * Searches for FoodProducts in the database using a SearchRequest.
   * @param searchRequest The SearchRequest to be used for searching.
   * @return A Page of FoodProducts.
   * @throws NullPointerException If the SearchRequest is null.
   */
  @Override
  public Page<FoodProduct> searchFoodProductsPaginated(@NonNull SearchRequest searchRequest)
    throws NullPointerException {
    return foodProductRepository.findAll(
      new SearchSpecification<FoodProduct>(searchRequest),
      SearchSpecification.getPageable(searchRequest)
    );
  }

  /**
   * Gets a FoodProduct from the database by its ean.
   * @param ean The ean of the FoodProduct to be retrieved.
   * @return The retrieved FoodProduct.
   * @throws FoodProductNotFoundException If the FoodProduct is not found.
   * @throws NullPointerException If the ean is null.
   */
  @Override
  public FoodProduct getFoodProductByEan(@NonNull String ean)
    throws FoodProductNotFoundException, NullPointerException {
    return foodProductRepository.findByEAN(ean).orElseThrow(FoodProductNotFoundException::new);
  }
}
