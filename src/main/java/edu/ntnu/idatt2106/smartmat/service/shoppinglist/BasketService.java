package edu.ntnu.idatt2106.smartmat.service.shoppinglist;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.BasketAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.BasketNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.Basket;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Service for basket operations on the database.
 *
 * @author Callum G.
 * @version 1.0 02.05.2023
 */
@Service
public interface BasketService {
  boolean basketExists(@NonNull UUID id) throws NullPointerException;

  Basket getBasketById(@NonNull UUID id) throws BasketNotFoundException, NullPointerException;

  Basket createBasket(@NonNull Basket basket)
    throws BasketAlreadyExistsException, NullPointerException;

  Basket updateBasket(@NonNull Basket basket) throws BasketNotFoundException, NullPointerException;

  void deleteBasket(@NonNull UUID id) throws BasketNotFoundException, NullPointerException;

  void deleteBasket(@NonNull Basket basket) throws BasketNotFoundException, NullPointerException;

  Basket getBasketByShoppingListId(@NonNull UUID id)
    throws BasketNotFoundException, NullPointerException;

  void deleteBasketItem(@NonNull UUID itemId) throws BasketNotFoundException, NullPointerException;
}
