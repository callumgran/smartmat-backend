package edu.ntnu.idatt2106.smartmat.service.shoppinglist;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Interface for the shopping list item service.
 * @author Carl G. & Callum G.
 * @version 1.1 - 26.04.2023.
 */
@Service
public interface ShoppingListItemService {
  public boolean existsById(@NonNull UUID id) throws NullPointerException;

  public ShoppingListItem saveShoppingListItem(@NonNull ShoppingListItem shoppingListItem)
    throws NullPointerException, IllegalArgumentException;

  public ShoppingListItem updateShoppingListItem(@NonNull ShoppingListItem shoppingListItem)
    throws NullPointerException, ShoppingListItemNotFoundException;

  public void deleteShoppingListItem(@NonNull UUID id)
    throws NullPointerException, ShoppingListItemNotFoundException;

  public ShoppingListItem getItemById(@NonNull UUID itemId)
    throws ShoppingListItemNotFoundException, NullPointerException;
}
