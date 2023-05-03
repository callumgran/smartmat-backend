package edu.ntnu.idatt2106.smartmat.controller.shoppinglist;

import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.BasketDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.BasketItemDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.CreateBasketDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.CreateBasketItemDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.PermissionDeniedException;
import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.BasketAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.BasketNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.CustomFoodItemMapper;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.FoodProductMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.Basket;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.BasketItem;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.CustomFoodItemService;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.FoodProductService;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.BasketService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import edu.ntnu.idatt2106.smartmat.validation.user.AuthValidation;
import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for shopping list items.
 * Handles requests from the client, and sends the response back to the client.
 * Handles all requests related to shopping list items.
 * @auther Callum G.
 * @version 1.0 - 02.05.2023
 */
@RestController
@RequestMapping("/api/v1/private/basket")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class BasketController {

  private final BasketService basketService;

  private final HouseholdService householdService;

  private final FoodProductService foodProductService;

  private final CustomFoodItemService customFoodItemService;

  private final ShoppingListService shoppingListService;

  private static final Logger LOGGER = LoggerFactory.getLogger(BasketController.class);

  private boolean isAdminOrHouseholdOwner(Auth auth, UUID householdId)
    throws UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    return (
      AuthValidation.hasRole(auth, UserRole.ADMIN) ||
      householdService.isHouseholdOwner(householdId, auth.getUsername())
    );
  }

  private boolean isAdminOrHouseholdPrivileged(Auth auth, UUID householdId)
    throws UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    return (
      householdService.isHouseholdMemberWithRole(
        householdId,
        auth.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      ) ||
      isAdminOrHouseholdOwner(auth, householdId)
    );
  }

  /**
   * Creates a new shopping list basket.
   * @param auth The auth object of the user.
   * @param shoppingListId The id of the shopping list to create the basket for.
   * @return The created shopping list basket.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws ShoppingListNotFoundException If the shopping list does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws BasketAlreadyExistsException If the basket already exists.
   * @throws PermissionDeniedException If the user does not have permission to create a basket.
   * @throws NullPointerException If the shoppingListId is null.
   */
  @PostMapping(
    value = "/",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Creates a new shopping list basket.",
    description = "Creates a new shopping list basket, can only be done by a privileged user or an admin.",
    tags = { "basket" }
  )
  public ResponseEntity<BasketDTO> createBasket(
    @AuthenticationPrincipal Auth auth,
    @RequestBody CreateBasketDTO basketDTO
  )
    throws UserDoesNotExistsException, ShoppingListNotFoundException, HouseholdNotFoundException, BasketAlreadyExistsException, PermissionDeniedException, NullPointerException {
    LOGGER.info("POST /api/v1/private/basket/" + basketDTO.getShoppingListId());
    ShoppingList shoppingList = shoppingListService.getShoppingListById(
      basketDTO.getShoppingListId()
    );

    LOGGER.info("Checking if user has permission to create a basket.");
    if (!isAdminOrHouseholdPrivileged(auth, shoppingList.getHousehold().getId())) {
      throw new PermissionDeniedException(
        "Du har ikke tilgang til å opprette en handleliste for denne husstanden."
      );
    }

    LOGGER.info("Creating basket.");
    Basket basket = basketService.createBasket(
      Basket
        .builder()
        .shoppingList(shoppingList)
        .basketItems(new ArrayList<>())
        .customFoodItems(new HashSet<>())
        .build()
    );

    shoppingList.setBasket(basket);
    shoppingListService.updateShoppingList(shoppingList.getId(), shoppingList);

    LOGGER.info("Returning created basket.");
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(
        BasketDTO
          .builder()
          .id(basket.getId())
          .shoppingListId(basket.getShoppingList().getId())
          .customFoodItems(new ArrayList<>())
          .basketItems(new ArrayList<>())
          .build()
      );
  }

  /**
   * Adds an item to the shopping list basket.
   * @param auth The auth object of the user.
   * @param basketId The id of the basket to add the item to.
   * @return The item that was added to the basket.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws BasketNotFoundException If the basket does not exist.
   * @throws FoodProductNotFoundException If the food product does not exist.
   * @throws PermissionDeniedException If the user does not have permission to add an item to the basket.
   * @throws NullPointerException If the householdId is null.
   */
  @PostMapping("/{basketId}/add-item")
  @Operation(
    summary = "Adds an item to the shopping list basket.",
    description = "Adds an item to the shopping list basket, can only be done by a privileged user or an admin.",
    tags = { "basket" }
  )
  public ResponseEntity<Void> addItemToBasket(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("basketId") UUID basketId,
    @RequestBody CreateBasketItemDTO createBasketItemDTO
  )
    throws UserDoesNotExistsException, HouseholdNotFoundException, BasketNotFoundException, FoodProductNotFoundException, PermissionDeniedException, NullPointerException {
    LOGGER.info("Received request to add item to basket with id: {}", basketId);

    Basket basket = basketService.getBasketById(basketId);
    if (!isAdminOrHouseholdPrivileged(auth, basket.getShoppingList().getHousehold().getId())) {
      throw new PermissionDeniedException("User does not exist.");
    }

    LOGGER.info("User has permission to add item to basket with id: {}", basketId);

    BasketItem basketItem = BasketItem
      .builder()
      .foodProduct(foodProductService.getFoodProductById(createBasketItemDTO.getFoodProductId()))
      .amount(createBasketItemDTO.getAmount())
      .basket(basket)
      .build();
    basket.getBasketItems().add(basketItem);

    basketService.updateBasket(basket);

    LOGGER.info("Added item to basket with id: {}", basketId);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Adds a custom item to the shopping list basket.
   * @param auth The auth object of the user.
   * @param basketId The id of the basket to add the item to.
   * @param customFoodProductId The custom item to add to the basket.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws BasketNotFoundException If the basket does not exist.
   * @throws ShoppingListItemNotFoundException If the shopping list item does not exist.
   * @throws PermissionDeniedException If the user does not have permission to add an item to the basket.
   * @throws NullPointerException If the householdId is null.
   */
  @PostMapping("/{basketId}/add-custom")
  @Operation(
    summary = "Adds a custom item to the shopping list basket.",
    description = "Adds a custom item to the shopping list basket, can only be done by a privileged user or an admin.",
    tags = { "basket" }
  )
  public ResponseEntity<Void> addItemToBasket(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("basketId") UUID basketId,
    @RequestBody UUID customFoodProductId
  )
    throws UserDoesNotExistsException, HouseholdNotFoundException, BasketNotFoundException, ShoppingListItemNotFoundException, PermissionDeniedException, NullPointerException {
    LOGGER.info("Received request to add item to basket with id: {}", basketId);

    Basket basket = basketService.getBasketById(basketId);
    if (!isAdminOrHouseholdPrivileged(auth, basket.getShoppingList().getHousehold().getId())) {
      throw new PermissionDeniedException("User does not exist.");
    }

    LOGGER.info("User has permission to add item to basket with id: {}", basketId);

    CustomFoodItem custom = customFoodItemService.getItemById(customFoodProductId);
    custom.setBasket(basket);
    basket.getCustomFoodItems().add(custom);

    basketService.updateBasket(basket);

    LOGGER.info("Added item to basket with id: {}", basketId);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Removes an item from the shopping list basket.
   * @param auth The auth object of the user.
   * @param basketId The id of the basket to remove the item from.
   * @param basketItemId The id of the item to remove from the basket.
   * @return The item that was removed from the basket.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws BasketNotFoundException If the basket does not exist.
   * @throws PermissionDeniedException If the user does not have permission to remove an item from the basket.
   * @throws NullPointerException If the householdId is null.
   */
  @DeleteMapping("/{basketId}/item/{basketItemId}")
  @Operation(
    summary = "Removes an item from the shopping list basket.",
    description = "Removes an item from the shopping list basket, can only be done by a privileged user or an admin.",
    tags = { "basket" }
  )
  public ResponseEntity<Void> removeItemFromBasket(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("basketId") UUID basketId,
    @PathVariable("basketItemId") UUID basketItemId
  )
    throws UserDoesNotExistsException, HouseholdNotFoundException, BasketNotFoundException, PermissionDeniedException, NullPointerException {
    LOGGER.info("Received request to remove item from basket with id: {}", basketId);

    Basket basket = basketService.getBasketById(basketId);
    if (!isAdminOrHouseholdPrivileged(auth, basket.getShoppingList().getHousehold().getId())) {
      throw new PermissionDeniedException("User does not exist.");
    }

    LOGGER.info("User has permission to remove item from basket with id: {}", basketId);

    basket.getBasketItems().removeIf(basketItem -> basketItem.getId().equals(basketItemId));
    basketService.updateBasket(basket);

    LOGGER.info("Removed item from basket with id: {}", basketId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * Removes a custom item from the shopping list basket.
   * @param auth The auth object of the user.
   * @param basketId The id of the basket to remove the item from.
   * @param basketItemId The id of the custom item to remove from the basket.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws BasketNotFoundException If the basket does not exist.
   * @throws PermissionDeniedException If the user does not have permission to remove an item from the basket.
   * @throws NullPointerException If the householdId is null.
   */
  @DeleteMapping("/{basketId}/custom-item/{basketItemId}")
  @Operation(
    summary = "Removes a custom item from the shopping list basket.",
    description = "Removes a custom item from the shopping list basket, can only be done by a privileged user or an admin.",
    tags = { "basket" }
  )
  public ResponseEntity<Void> removeCustomItemFromBasket(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("basketId") UUID basketId,
    @PathVariable("basketItemId") UUID basketItemId
  )
    throws UserDoesNotExistsException, HouseholdNotFoundException, BasketNotFoundException, ShoppingListItemNotFoundException, PermissionDeniedException, NullPointerException {
    LOGGER.info("Received request to remove item from basket with id: {}", basketId);

    Basket basket = basketService.getBasketById(basketId);
    if (!isAdminOrHouseholdPrivileged(auth, basket.getShoppingList().getHousehold().getId())) {
      throw new PermissionDeniedException("User does not exist.");
    }

    basket.getCustomFoodItems().removeIf(item -> item.getId().equals(basketItemId));

    CustomFoodItem custom = customFoodItemService.getItemById(basketItemId);
    custom.setBasket(null);
    customFoodItemService.updateCustomFoodItem(custom);

    LOGGER.info("User has permission to remove item from basket with id: {}", basketId);

    basketService.updateBasket(basket);

    LOGGER.info("Removed item from basket with id: {}", basketId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * Gets a shopping list basket.
   * @param auth The auth object of the user.
   * @param basketId The id of the basket to get.
   * @return The basket that was requested.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws BasketNotFoundException If the basket does not exist.
   * @throws PermissionDeniedException If the user does not have permission to remove an item from the basket.
   * @throws NullPointerException If the householdId is null.
   */
  @GetMapping("/{basketId}")
  @Operation(
    summary = "Gets a shopping list basket.",
    description = "Gets a shopping list basket, can only be done by a privileged user or an admin.",
    tags = { "basket" }
  )
  public ResponseEntity<BasketDTO> getBasket(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("basketId") UUID basketId
  )
    throws UserDoesNotExistsException, HouseholdNotFoundException, BasketNotFoundException, PermissionDeniedException, NullPointerException {
    LOGGER.info("Received request to get basket with id: {}", basketId);

    Basket basket = basketService.getBasketById(basketId);
    if (!isAdminOrHouseholdPrivileged(auth, basket.getShoppingList().getHousehold().getId())) {
      throw new PermissionDeniedException("User does not exist.");
    }

    LOGGER.info("User has permission to get basket with id: {}", basketId);

    BasketDTO basketDTO = BasketDTO
      .builder()
      .id(basket.getId())
      .basketItems(
        basket
          .getBasketItems()
          .stream()
          .map(basketItem ->
            BasketItemDTO
              .builder()
              .basketId(basketId)
              .id(basketItem.getId())
              .foodProduct(
                FoodProductMapper.INSTANCE.foodProductToFoodProductDTO(basketItem.getFoodProduct())
              )
              .amount(basketItem.getAmount())
              .build()
          )
          .toList()
      )
      .shoppingListId(basket.getShoppingList().getId())
      .customFoodItems(
        basket
          .getCustomFoodItems()
          .stream()
          .map(CustomFoodItemMapper.INSTANCE::customFoodItemsToCustomFoodItemDTO)
          .toList()
      )
      .build();

    return ResponseEntity.ok(basketDTO);
  }
}
