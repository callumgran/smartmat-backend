package edu.ntnu.idatt2106.smartmat.repository.foodproduct;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for database operations for custom food items.
 * @author Carl G.
 * @version 1.0 24.04.2023
 */
@Repository
public interface CustomFoodItemRepository extends JpaRepository<CustomFoodItem, UUID> {
  /**
   * Checks if a collection of custom food items have the specified id and shopping list id
   * @param id The id of the custom food item.
   * @param shoppingListId The id of the shopping list.
   * @return A collection of custom food items.
   */
  @Query("SELECT cfi FROM CustomFoodItem cfi WHERE cfi.id = ?1 AND cfi.shoppingList.id = ?2")
  Optional<Collection<CustomFoodItem>> findByIdInShoppingList(UUID id, UUID shoppingListId);
}
