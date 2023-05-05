package edu.ntnu.idatt2106.smartmat.repository.shoppinglist;

import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for shopping list.
 * @author Tobias O., Carl G.
 * @version 1.0 - 23.04.2023
 */
@Transactional
@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, UUID> {
  /**
   * Get the current shopping list by household.
   * Only returns one shopping list.
   * @param household The household to get the shopping list from.
   * @return The current shopping list which has not been completed.
   */
  @Query(
    value = "SELECT sl FROM ShoppingList sl WHERE sl.household.id = ?1 AND sl.dateCompleted IS NULL"
  )
  Optional<Collection<ShoppingList>> getCurrentShoppingListByHousehold(@NonNull UUID household);

  @Modifying
  @Query(value = "DELETE FROM ShoppingListItem sli WHERE sli.shoppingList.id = ?1")
  void deleteShoppingListItemsByShoppingList(@NonNull UUID shoppingListId);
}
