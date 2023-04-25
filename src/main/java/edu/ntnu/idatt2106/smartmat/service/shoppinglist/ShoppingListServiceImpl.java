package edu.ntnu.idatt2106.smartmat.service.shoppinglist;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.repository.shoppinglist.ShoppingListRepository;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the shopping list service.
 * The class is responsible for all business logic related to shopping list.
 * @author Tobias O., Carl G.
 * @version 1.1 - 24.04.2023.
 */
@Service
@RequiredArgsConstructor
public class ShoppingListServiceImpl implements ShoppingListService {

  @Autowired
  private ShoppingListRepository shoppinglistRepository;

  /**
   * Method to check whether the shopping list exists or not.
   * @param id the id for the shopping list.
   * @return the shopping list if it exists, an exception if it does not.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws NullPointerException if the shopping list id is null.
   */
  @Override
  public boolean shoppingListExists(@NonNull UUID id)
    throws ShoppingListNotFoundException, NullPointerException {
    if (!shoppinglistRepository.existsById(id)) {
      throw new ShoppingListNotFoundException();
    }
    return shoppinglistRepository.existsById(id);
  }

  /**
   * Method to retrieve a shopping list.
   * @param id the id for the shopping list.
   * @return the shopping list if it is found, an exception otherwise.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws NullPointerException if the shopping list id is null.
   */

  @Override
  public ShoppingList getShoppingListById(@NonNull UUID id)
    throws ShoppingListNotFoundException, NullPointerException {
    return shoppinglistRepository.findById(id).orElseThrow(ShoppingListNotFoundException::new);
  }

  /**
   * Method to update a shopping list.
   * @param id the id for the shopping list.
   * @param shoppingList the shopping list.
   * @return the updated shopping list.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws NullPointerException if any values are null.
   */

  public ShoppingList updateShoppingList(@NonNull UUID id, ShoppingList shoppingList)
    throws ShoppingListNotFoundException, NullPointerException {
    if (!shoppinglistRepository.existsById(id)) {
      throw new ShoppingListNotFoundException();
    }
    shoppingList.setId(id);
    return shoppinglistRepository.save(shoppingList);
  }

  /**
   * Method to save a shopping list.
   * @param shoppingList the shopping list.
   * @return the saved shopping list.
   * @throws ShoppingListAlreadyExistsException if the shopping list already exists.
   * @throws NullPointerException if the shopping list is null.
   */
  @Override
  public ShoppingList saveShoppingList(@NonNull ShoppingList shoppingList)
    throws ShoppingListAlreadyExistsException, NullPointerException {
    if (shoppinglistRepository.existsById(shoppingList.getId())) {
      throw new ShoppingListAlreadyExistsException();
    }
    return shoppinglistRepository.save(shoppingList);
  }

  /**
   * Method to delete a shopping list from the repository.
   * @param shoppingList shopping list to delete.
   * @throws NullPointerException if the shopping list is null.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   */
  @Override
  public void deleteShoppingList(@NonNull ShoppingList shoppingList)
    throws ShoppingListNotFoundException, NullPointerException {
    if (!shoppinglistRepository.existsById(shoppingList.getId())) {
      throw new ShoppingListNotFoundException();
    }
    shoppinglistRepository.delete(shoppingList);
  }

  /**
   * Method to delete a shopping list by id.
   * @param id the id for the shopping list.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws NullPointerException if the shopping list id is null.
   */

  public void deleteShoppingListById(@NonNull UUID id)
    throws ShoppingListNotFoundException, NullPointerException {
    if (!shoppinglistRepository.existsById(id)) {
      throw new ShoppingListNotFoundException();
    }
    shoppinglistRepository.deleteById(id);
  }

  /**
   * Method to get the current shopping list for a household.
   * Returns the first shopping list that has not been completed.
   * @param householdId The household to get the shopping list from.
   * @return The current shopping list.
   * @throws ShoppingListNotFoundException if no such shopping list exists.
   * @throws NullPointerException if the household id is null.
   */
  public ShoppingList getCurrentShoppingList(@NonNull UUID householdId)
    throws ShoppingListNotFoundException, NullPointerException {
    return shoppinglistRepository
      .getCurrentShoppingListByHousehold(householdId)
      .orElseThrow(ShoppingListNotFoundException::new)
      .stream()
      .findFirst()
      .orElseThrow(ShoppingListNotFoundException::new);
  }
}
