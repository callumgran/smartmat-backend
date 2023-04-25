package edu.ntnu.idatt2106.smartmat.service.shoppinglist;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.repository.shoppinglist.ShoppingListItemRepository;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the shopping list item service.
 * The class is responsible for all business logic related to shopping list items.
 * @author Carl G.
 * @version 1.1 - 24.04.2023.
 */
@Service
@RequiredArgsConstructor
public class ShoppingListItemServiceImpl implements ShoppingListItemService {

  @Autowired
  private ShoppingListItemRepository shoppingListItemRepository;

  public boolean existsByIdInShoppingList(@NonNull UUID shoppingListId, @NonNull UUID id) {
    Optional<Collection<ShoppingListItem>> shoppingListItems = shoppingListItemRepository.findByIdInShoppingList(
      id,
      shoppingListId
    );
    return shoppingListItems.isPresent() ? shoppingListItems.get().size() > 0 : false;
  }

  /**
   * Method to save shopping list items.
   * @param shoppingListItem the shopping list item.
   * @return the saved shopping list item.
   * @throws NullPointerException if the shopping list is null.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   */
  public ShoppingListItem saveShoppingListItem(@NonNull ShoppingListItem shoppingListItem)
    throws NullPointerException, ShoppingListNotFoundException {
    return shoppingListItemRepository.save(shoppingListItem);
  }

  /**
   * Method to delete a shopping list item in shopping list.
   * @param shoppingListId the shopping list id.
   * @param id the id of the item being deleted.
   * @throws NullPointerException if any values are null.
   * @throws ShoppingListItemNotFoundException if shopping list does not exist.
   */

  public void deleteShoppingListItemInShoppingList(@NonNull UUID shoppingListId, @NonNull UUID id)
    throws NullPointerException, ShoppingListItemNotFoundException {
    if (!existsByIdInShoppingList(shoppingListId, id)) throw new ShoppingListItemNotFoundException(
      "Matvaren ble ikke funnet i din beholdning."
    );
    shoppingListItemRepository.deleteById(id);
  }

  /**
   * Method to get a shopping list item by id.
   * @param itemId the id of the item.
   * @return the shopping list item.
   */
  public ShoppingListItem getItemById(@NonNull UUID itemId)
    throws ShoppingListItemNotFoundException, NullPointerException {
    Optional<ShoppingListItem> shoppingListItem = shoppingListItemRepository.findById(itemId);
    return shoppingListItem.orElseThrow(ShoppingListItemNotFoundException::new);
  }
}
