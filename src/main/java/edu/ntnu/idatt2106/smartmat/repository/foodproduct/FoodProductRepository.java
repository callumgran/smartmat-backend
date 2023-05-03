package edu.ntnu.idatt2106.smartmat.repository.foodproduct;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for the FoodProduct entity.
 * @author Callum G, Thomas S.
 * @version 1.1 24.04.2023
 */
@Repository
public interface FoodProductRepository
  extends JpaRepository<FoodProduct, Long>, JpaSpecificationExecutor<FoodProduct> {
  /**
   * Finds a FoodProduct by the EAN.
   * @param EAN The EAN of the food product.
   * @return The FoodProduct.
   */
  Optional<FoodProduct> findByEAN(String EAN);

  /**
   * Finds a FoodProduct by the ingredient id.
   * @param id The id of the ingredient.
   * @param isLoose Whether the food product is loose or not.
   * @return The FoodProduct.
   */
  @Query("SELECT fp FROM FoodProduct fp WHERE fp.ingredient.id = ?1 AND fp.looseWeight = ?2")
  Optional<FoodProduct> findByIngredientId(Long id, boolean isLoose);
}
