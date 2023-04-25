package edu.ntnu.idatt2106.smartmat.service.foodproduct;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.repository.foodproduct.CustomFoodItemRepository;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the custom food item service.
 * This class is responsible for all business logic related to custom food items.
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */
@Service
@RequiredArgsConstructor
public class CustomFoodItemServiceImpl implements CustomFoodItemService {

  @Autowired
  private CustomFoodItemRepository customFoodItemRepository;

  /**
   * Method to check if a custom food item exists in a shopping list.
   * @param shoppingListId the shopping list id.
   * @param id the custom food item id.
   * @return true if custom food items exists in a shopping list, false otherwise.
   */
  public boolean existsByIdInShoppingList(@NonNull UUID shoppingListId, @NonNull UUID id) {
    Optional<Collection<CustomFoodItem>> customFoodItems = customFoodItemRepository.findByIdInShoppingList(
      id,
      shoppingListId
    );
    return customFoodItems.isPresent() ? customFoodItems.get().size() > 0 : false;
  }

  /**
   * Method for saving custom food items.
   * @param customFoodItem custom food item.
   * @return the saved custom food item.
   * @throws NullPointerException if any values are null.
   * @throws ShoppingListNotFoundException if shopping list does not exist.
   */

  public CustomFoodItem saveCustomFoodItem(@NonNull CustomFoodItem customFoodItem)
    throws NullPointerException, ShoppingListNotFoundException {
    return customFoodItemRepository.save(customFoodItem);
  }

  /**
   * Method for deleting custom food items in a shopping list.
   * @param shoppingListId the shopping list id.
   * @param id the custom food item id.
   * @throws NullPointerException if any values are null.
   * @throws ShoppingListItemNotFoundException if shopping list does not exist.
   */
  @Override
  public void deleteCustomFoodItemInShoppingList(@NonNull UUID shoppingListId, @NonNull UUID id)
    throws NullPointerException, ShoppingListItemNotFoundException {
    if (!existsByIdInShoppingList(shoppingListId, id)) throw new ShoppingListItemNotFoundException(
      "Klarte ikke å slette matvaren fordi den ikke eksiterer."
    );
    customFoodItemRepository.deleteById(id);
  }

  /**
   * Method to get a custom food item by id.
   * @param itemId the id of the item.
   * @return the shopping list item.
   */
  public CustomFoodItem getItemById(@NonNull UUID itemId)
    throws ShoppingListItemNotFoundException, NullPointerException {
    Optional<CustomFoodItem> shoppingListItem = customFoodItemRepository.findById(itemId);
    return shoppingListItem.orElseThrow(() ->
      new ShoppingListItemNotFoundException("Klarte ikke å finne matvaren.")
    );
  }
}
