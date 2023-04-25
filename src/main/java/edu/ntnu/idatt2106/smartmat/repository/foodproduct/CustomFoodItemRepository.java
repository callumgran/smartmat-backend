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
   * Checks if a custom food item item exists in a shopping list in a which is connected to a household.
   * @param id The id of the shopping list item.
   * @param householdId The id of the household.
   * @return True if the shopping list item exists in the household, false otherwise.
   */
  @Query("SELECT cfi FROM CustomFoodItem cfi WHERE cfi.id = ?1 AND cfi.shoppingList.id = ?2")
  Optional<Collection<CustomFoodItem>> findByIdInShoppingList(UUID id, UUID shoppingListId);
}
