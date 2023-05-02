package edu.ntnu.idatt2106.smartmat.repository.shoppinglist;

import edu.ntnu.idatt2106.smartmat.model.shoppinglist.Basket;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for basket operations on the database.
 *
 * @author Callum G.
 * @version 1.0 02.05.2023
 */
@Repository
public interface BasketRepository extends JpaRepository<Basket, UUID> {
  @Query(value = "SELECT b FROM Basket b WHERE b.shoppingList.id = ?1")
  Optional<Basket> findByShoppingListId(UUID id);
}
