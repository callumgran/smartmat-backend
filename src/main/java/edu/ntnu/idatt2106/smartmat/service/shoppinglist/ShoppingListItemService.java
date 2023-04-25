package edu.ntnu.idatt2106.smartmat.service.shoppinglist;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Interface for the shopping list item service.
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */
@Service
public interface ShoppingListItemService {
  public boolean existsByIdInShoppingList(@NonNull UUID shoppingList, @NonNull UUID id);

  public ShoppingListItem saveShoppingListItem(@NonNull ShoppingListItem shoppingListItem)
    throws NullPointerException, ShoppingListNotFoundException;

  public void deleteShoppingListItemInShoppingList(@NonNull UUID shoppingListId, @NonNull UUID id)
    throws NullPointerException, ShoppingListItemNotFoundException;

  public ShoppingListItem getItemById(@NonNull UUID itemId)
    throws ShoppingListItemNotFoundException, NullPointerException;
}
