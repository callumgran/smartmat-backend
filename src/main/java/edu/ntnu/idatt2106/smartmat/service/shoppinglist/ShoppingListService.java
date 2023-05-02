package edu.ntnu.idatt2106.smartmat.service.shoppinglist;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.BasketNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Interface for the shopping list.
 * @author Tobias O., Carl G.
 * @version 1.1 - 24.04.2023.
 */
@Service
public interface ShoppingListService {
  boolean shoppingListExists(@NonNull UUID id)
    throws ShoppingListNotFoundException, NullPointerException;

  ShoppingList getShoppingListById(@NonNull UUID id)
    throws ShoppingListNotFoundException, NullPointerException;

  ShoppingList updateShoppingList(@NonNull UUID id, ShoppingList shoppinglist)
    throws ShoppingListNotFoundException, NullPointerException;

  ShoppingList saveShoppingList(@NonNull ShoppingList shoppinglist)
    throws ShoppingListAlreadyExistsException, NullPointerException;

  void deleteShoppingList(@NonNull ShoppingList shoppinglist)
    throws ShoppingListNotFoundException, NullPointerException;

  void deleteShoppingListById(@NonNull UUID id)
    throws ShoppingListNotFoundException, NullPointerException;

  ShoppingList getCurrentShoppingList(@NonNull UUID householdId)
    throws ShoppingListNotFoundException, NullPointerException;

  ShoppingList getShoppingListWithDiff(@NonNull UUID id)
    throws ShoppingListNotFoundException, BasketNotFoundException, NullPointerException;
}
