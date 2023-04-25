package edu.ntnu.idatt2106.smartmat.repository.foodproduct;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
}
