package edu.ntnu.idatt2106.smartmat.repository.shoppinglist;

import edu.ntnu.idatt2106.smartmat.model.shoppinglist.BasketItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for basket item operations on the database.
 * @author Callum G.
 * @version 1.0 03.05.2023
 */
@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, UUID> {}
