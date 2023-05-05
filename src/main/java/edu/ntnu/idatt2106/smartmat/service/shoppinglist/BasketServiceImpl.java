package edu.ntnu.idatt2106.smartmat.service.shoppinglist;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.BasketAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.BasketNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.Basket;
import edu.ntnu.idatt2106.smartmat.repository.shoppinglist.BasketItemRepository;
import edu.ntnu.idatt2106.smartmat.repository.shoppinglist.BasketRepository;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for basket operations on the database.
 *
 * @author Callum G.
 * @version 1.0 02.05.2023
 */
@Service
public class BasketServiceImpl implements BasketService {

  @Autowired
  private BasketRepository basketRepository;

  @Autowired
  private BasketItemRepository basketItemRepository;

  /**
   * Checks if a basket exists in the database.
   * @param id The id of the basket to check.
   * @return True if the basket exists, false otherwise.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public boolean basketExists(@NonNull UUID id) throws NullPointerException {
    return basketRepository.existsById(id);
  }

  /**
   * Gets a basket from the database by its id.
   * @param id The id of the basket to get.
   * @return The basket with the given id.
   * @throws BasketNotFoundException If the basket does not exist.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public Basket getBasketById(@NonNull UUID id)
    throws BasketNotFoundException, NullPointerException {
    return basketRepository.findById(id).orElseThrow(() -> new BasketNotFoundException());
  }

  /**
   * Creates a basket in the database.
   * @param basket The basket to create.
   * @return The created basket.
   * @throws BasketAlreadyExistsException If the basket already exists.
   * @throws NullPointerException If the basket is null.
   */
  @Override
  public Basket createBasket(@NonNull Basket basket)
    throws BasketAlreadyExistsException, NullPointerException {
    if (basket.getId() != null && basketExists(basket.getId())) {
      throw new BasketAlreadyExistsException();
    }
    return basketRepository.save(basket);
  }

  /**
   * Updates a basket in the database.
   * @param basket The basket to update.
   * @return The updated basket.
   * @throws BasketNotFoundException If the basket does not exist.
   * @throws NullPointerException If the basket is null.
   */
  @Override
  public Basket updateBasket(@NonNull Basket basket)
    throws BasketNotFoundException, NullPointerException {
    if (basket.getId() == null || !basketExists(basket.getId())) {
      throw new BasketNotFoundException();
    }
    return basketRepository.save(basket);
  }

  /**
   * Deletes a basket from the database by its id.
   * @param id The id of the basket to delete.
   * @throws BasketNotFoundException If the basket does not exist.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public void deleteBasket(@NonNull UUID id) throws BasketNotFoundException, NullPointerException {
    if (!basketExists(id)) {
      throw new BasketNotFoundException();
    }
    basketRepository.deleteById(id);
  }

  /**
   * Deletes a basket from the database.
   * @param basket The basket to delete.
   * @throws BasketNotFoundException If the basket does not exist.
   * @throws NullPointerException If the basket is null.
   */
  @Override
  public void deleteBasket(@NonNull Basket basket)
    throws BasketNotFoundException, NullPointerException {
    if (basket.getId() == null || !basketExists(basket.getId())) {
      throw new BasketNotFoundException();
    }
    basketRepository.delete(basket);
  }

  /**
   * Gets a basket from the database by its shopping list id.
   * @param id The id of the shopping list to get the basket of.
   * @return The basket with the given shopping list id.
   * @throws BasketNotFoundException If the basket does not exist.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public Basket getBasketByShoppingListId(@NonNull UUID id)
    throws BasketNotFoundException, NullPointerException {
    return basketRepository
      .findByShoppingListId(id)
      .orElseThrow(() -> new BasketNotFoundException());
  }

  /**
   * Deletes a basket item from the database by its id.
   * @param itemId The id of the basket item to delete.
   * @throws BasketNotFoundException If the basket does not exist.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public void deleteBasketItem(@NonNull UUID itemId)
    throws BasketNotFoundException, NullPointerException {
    if (!basketItemRepository.existsById(itemId)) {
      throw new BasketNotFoundException("Fant ikke handlekurv vare");
    }
    basketItemRepository.deleteById(itemId);
  }
}
