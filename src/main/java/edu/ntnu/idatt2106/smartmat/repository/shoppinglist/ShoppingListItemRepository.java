package edu.ntnu.idatt2106.smartmat.repository.shoppinglist;

import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for shopping list item operations on the database.
 * @author Carl G.
 * @version 1.0 24.04.2023
 */
@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, UUID> {
  /**
   * Checks if a shopping list item exists in a household.
   * @param id The id of the shopping list item.
   * @param shoppingListId The id of the shopping list.
   * @return True if the shopping list item exists in the household, false otherwise.
   */
  @Query("SELECT sli FROM ShoppingListItem sli WHERE sli.id = ?1 AND sli.shoppingList.id = ?2")
  Optional<Collection<ShoppingListItem>> findByIdInShoppingList(UUID id, UUID shoppingListId);
}
