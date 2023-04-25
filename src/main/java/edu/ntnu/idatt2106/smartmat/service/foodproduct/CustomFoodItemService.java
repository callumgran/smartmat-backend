package edu.ntnu.idatt2106.smartmat.service.foodproduct;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Interface for the custom food item.
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */
@Service
public interface CustomFoodItemService {
  public boolean existsByIdInShoppingList(@NonNull UUID shoppingList, @NonNull UUID id);

  public CustomFoodItem saveCustomFoodItem(@NonNull CustomFoodItem customFoodItem)
    throws NullPointerException, ShoppingListNotFoundException;

  public void deleteCustomFoodItemInShoppingList(@NonNull UUID shoppingListId, @NonNull UUID id)
    throws NullPointerException, ShoppingListItemNotFoundException;

  public CustomFoodItem getItemById(@NonNull UUID itemId)
    throws ShoppingListItemNotFoundException, NullPointerException;
}
