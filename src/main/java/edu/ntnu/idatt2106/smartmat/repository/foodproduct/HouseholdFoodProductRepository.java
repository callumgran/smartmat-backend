package edu.ntnu.idatt2106.smartmat.repository.foodproduct;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
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
public interface HouseholdFoodProductRepository
  extends
    JpaRepository<HouseholdFoodProduct, UUID>, JpaSpecificationExecutor<HouseholdFoodProduct> {
  /**
   * Finds a HouseholdFoodProduct by the household id and the EAN of the food product.
   * @param id The id of the household.
   * @param EAN The EAN of the food product.
   * @return The HouseholdFoodProduct.
   */
  @Query(
    "SELECT hfp FROM HouseholdFoodProduct hfp WHERE hfp.household.id = ?1 AND hfp.foodProduct.EAN = ?2"
  )
  Optional<HouseholdFoodProduct> findHouseholdFoodProductByHouseholdAndEAN(UUID id, String EAN);

  /**
   * Finds a HouseholdFoodProduct by the household and ingredient.
   * @param household The household.
   * @param ingredient The ingredient.
   * @return The HouseholdFoodProduct.
   */
  @Query(
    "SELECT hfp FROM HouseholdFoodProduct hfp WHERE hfp.household = ?1 AND hfp.foodProduct.ingredient = ?2 ORDER BY hfp.expirationDate ASC"
  )
  Optional<Collection<HouseholdFoodProduct>> findHouseholdFoodProductByHouseholdAndIngredient(
    Household household,
    Ingredient ingredient
  );
}
