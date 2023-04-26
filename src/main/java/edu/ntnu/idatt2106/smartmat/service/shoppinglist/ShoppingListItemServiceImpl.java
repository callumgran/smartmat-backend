package edu.ntnu.idatt2106.smartmat.service.shoppinglist;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.repository.shoppinglist.ShoppingListItemRepository;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the shopping list item service.
 * The class is responsible for all business logic related to shopping list items.
 * @author Carl G. & Callum G.
 * @version 1.2 - 26.04.2023.
 */
@Service
@RequiredArgsConstructor
public class ShoppingListItemServiceImpl implements ShoppingListItemService {

  @Autowired
  private ShoppingListItemRepository shoppingListItemRepository;

  /**
   * Method to check if a shopping list item exists in a shopping list.
   * @param shoppingListId the shopping list id.
   * @param id the id of the item.
   * @return true if the item exists in the shopping list.
   * @throws NullPointerException if any values are null.
   */
  public boolean existsById(@NonNull UUID id) throws NullPointerException {
    return shoppingListItemRepository.existsById(id);
  }

  /**
   * Method to save shopping list items.
   * @param shoppingListItem the shopping list item.
   * @return the saved shopping list item.
   * @throws NullPointerException if the shopping list is null.
   * @throws IllegalArgumentException if the shopping list item already exists.
   */
  public ShoppingListItem saveShoppingListItem(@NonNull ShoppingListItem shoppingListItem)
    throws NullPointerException, IllegalArgumentException {
    if (shoppingListItem.getId() != null && existsById(shoppingListItem.getId())) {
      throw new IllegalArgumentException("Matvare finnes allerede i handlelisten.");
    }
    return shoppingListItemRepository.save(shoppingListItem);
  }

  /**
   * Method to update a shopping list item.
   * @param shoppingListItem the shopping list item.
   * @return the updated shopping list item.
   * @throws NullPointerException if the shopping list item is null.
   * @throws ShoppingListItemNotFoundException if the shopping list item does not exist.
   */
  public ShoppingListItem updateShoppingListItem(@NonNull ShoppingListItem shoppingListItem)
    throws NullPointerException, ShoppingListItemNotFoundException {
    if (!existsById(shoppingListItem.getId())) {
      throw new ShoppingListItemNotFoundException();
    }
    return shoppingListItemRepository.save(shoppingListItem);
  }

  /**
   * Method to delete a shopping list item in shopping list.
   * @param shoppingListId the shopping list id.
   * @param id the id of the item being deleted.
   * @throws NullPointerException if any values are null.
   * @throws ShoppingListItemNotFoundException if shopping list does not exist.
   */
  public void deleteShoppingListItem(@NonNull UUID id)
    throws NullPointerException, ShoppingListItemNotFoundException {
    if (!existsById(id)) {
      throw new ShoppingListItemNotFoundException();
    }
    shoppingListItemRepository.deleteById(id);
  }

  /**
   * Method to get a shopping list item by id.
   * @param itemId the id of the item.
   * @return the shopping list item.
   * @throws ShoppingListItemNotFoundException if the item is not found.
   * @throws NullPointerException if the id is null.
   */
  public ShoppingListItem getItemById(@NonNull UUID itemId)
    throws ShoppingListItemNotFoundException, NullPointerException {
    return shoppingListItemRepository
      .findById(itemId)
      .orElseThrow(ShoppingListItemNotFoundException::new);
  }
}
