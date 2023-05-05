package edu.ntnu.idatt2106.smartmat.service.foodproduct;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.repository.foodproduct.CustomFoodItemRepository;
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
   * Method to check if a custom food item exists.
   * @param id the id of the custom food item.
   * @return true if it exists, false if not.
   * @throws NullPointerException if any values are null.
   */
  public boolean existsById(@NonNull UUID id) throws NullPointerException {
    return customFoodItemRepository.existsById(id);
  }

  /**
   * Method for saving custom food items.
   * @param customFoodItem custom food item.
   * @return the saved custom food item.
   * @throws NullPointerException if any values are null.
   * @throws IllegalArgumentException if the custom food item already exists.
   */
  public CustomFoodItem saveCustomFoodItem(@NonNull CustomFoodItem customFoodItem)
    throws NullPointerException, IllegalArgumentException {
    if (customFoodItem.getId() != null && existsById(customFoodItem.getId())) {
      throw new IllegalArgumentException("Matvare finnes allerede i handlelisten.");
    }
    return customFoodItemRepository.save(customFoodItem);
  }

  /**
   * Method for updating custom food items.
   * @param customFoodItem custom food item.
   * @return the updated custom food item.
   * @throws NullPointerException if any values are null.
   * @throws ShoppingListItemNotFoundException if shopping list item does not exist.
   */
  public CustomFoodItem updateCustomFoodItem(@NonNull CustomFoodItem customFoodItem)
    throws NullPointerException, ShoppingListItemNotFoundException {
    if (!existsById(customFoodItem.getId())) {
      throw new ShoppingListItemNotFoundException("Klarte ikke å finne varen.");
    }
    return customFoodItemRepository.save(customFoodItem);
  }

  /**
   * Method for deleting custom food items in a shopping list.
   * @param id the custom food item id.
   * @throws NullPointerException if any values are null.
   * @throws ShoppingListItemNotFoundException if shopping list does not exist.
   */
  @Override
  public void deleteCustomFoodItem(@NonNull UUID id)
    throws NullPointerException, ShoppingListItemNotFoundException {
    if (!existsById(id)) {
      throw new ShoppingListItemNotFoundException("Klarte ikke å finne matvaren.");
    }
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
