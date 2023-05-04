package edu.ntnu.idatt2106.smartmat.controller.shoppinglist;

import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.CreateShoppingListItemDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ShoppingListItemDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.PermissionDeniedException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.ingredient.IngredientNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.validation.BadInputException;
import edu.ntnu.idatt2106.smartmat.mapper.shoppinglist.ShoppingListItemMapper;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListItemService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import edu.ntnu.idatt2106.smartmat.utils.PrivilegeUtil;
import edu.ntnu.idatt2106.smartmat.validation.foodproduct.FoodProductValidation;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for shopping list items.
 * Handles requests from the client, and sends the response back to the client.
 * Handles all requests related to shopping list items.
 * @auther Callum Gran
 * @version 1.1 - 26.04.2023
 */
@RestController
@RequestMapping("/api/v1/private/shoppinglistitems")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class ShoppingListItemController {

  private final ShoppingListItemService shoppingListItemService;

  private final ShoppingListService shoppingListService;

  private final IngredientService ingredientService;

  private final HouseholdService householdService;

  private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingListItemController.class);

  /**
   * Method to add an an item to a shopping list.
   * @param auth authentication for user.
   * @param item the item to add.
   * @return the item added.
   * @throws NullPointerException if any value are null.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws IngredientNotFoundException if the ingredient is not found.
   * @throws UserDoesNotExistsException if the user is not found.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws PermissionDeniedException if the user does not have permission.
   * @throws ShoppingListItemNotFoundException if the shopping list item is not found.
   * @throws BadInputException if the input is bad.
   */
  @PostMapping(
    value = "",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Add an item to a shopping list",
    description = "Add an item to a shopping list. Requires authentication and be owner of the household. First checks if an ingredient with the item name exists. If it does, it adds a shopping list item. Otherwise, it adds a custom food item.",
    tags = { "shoppinglistitem" }
  )
  public ResponseEntity<ShoppingListItemDTO> addItemToShoppingList(
    @AuthenticationPrincipal Auth auth,
    @RequestBody CreateShoppingListItemDTO item
  )
    throws NullPointerException, ShoppingListNotFoundException, IngredientNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, PermissionDeniedException, ShoppingListItemNotFoundException, BadInputException {
    ShoppingList shoppingList = shoppingListService.getShoppingListById(item.getShoppingListId());

    LOGGER.info("Adding item to shopping list");

    if (
      !PrivilegeUtil.isAdminOrHouseholdPrivileged(
        auth,
        shoppingList.getHousehold().getId(),
        householdService
      )
    ) {
      throw new PermissionDeniedException(
        "You do not have permission to add an item to this shopping list."
      );
    }

    if (
      !FoodProductValidation.validateCreateShoppingListItem(
        item.getName(),
        item.getAmount(),
        item.getIngredientId()
      )
    ) throw new BadInputException("Ugyldig input for å legge til ingrediens i handleliste");

    Ingredient ingredient = ingredientService.getIngredientById(item.getIngredientId());

    ShoppingListItem shoppingListItem;

    if (
      shoppingList
        .getShoppingListItems()
        .stream()
        .anyMatch(s -> s.getIngredient().getId().equals(ingredient.getId()))
    ) {
      ShoppingListItem current = shoppingList
        .getShoppingListItems()
        .stream()
        .filter(s -> s.getIngredient().getId().equals(ingredient.getId()))
        .findFirst()
        .get();
      current.setChecked(false);
      current.setAmount(current.getAmount() + item.getAmount());
      shoppingListItem = shoppingListItemService.updateShoppingListItem(current);
      LOGGER.info("Updated item in shopping list");
    } else {
      shoppingListItem =
        shoppingListItemService.saveShoppingListItem(
          ShoppingListItem
            .builder()
            .ingredient(ingredient)
            .shoppingList(shoppingList)
            .amount(item.getAmount())
            .checked(false)
            .build()
        );
      LOGGER.info("Added item to shopping list");
    }

    ShoppingListItemDTO shoppingListItemDTO = ShoppingListItemMapper.INSTANCE.shoppingListItemsToShoppingListItemDTO(
      shoppingListItem
    );

    return ResponseEntity.ok(shoppingListItemDTO);
  }

  /**
   * Method to delete an item from a shopping list.
   * @param auth authentication for user.
   * @param id the id for the shopping list.
   * @param itemId the id for the item that is being deleted.
   * @return the deleted item.
   * @throws NullPointerException if any values are null.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws PermissionDeniedException if the user does not have permission to delete an item from the shopping list.
   */
  @DeleteMapping(value = "/household/{householdId}/item/{id}")
  @Operation(
    summary = "Delete an item from a shopping list",
    description = "Delete an item from a shopping list. Requires authentication and be privileged member of the household or admin. First checks if a shopping list item exists. If it is, it deletes the shopping list item. Otherwise, it deletes the custom food item if possible.",
    tags = { "shoppinglistitem" }
  )
  public ResponseEntity<Void> deleteItemFromShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("householdId") UUID householdId,
    @PathVariable("id") UUID id
  )
    throws NullPointerException, ShoppingListItemNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, PermissionDeniedException {
    if (!PrivilegeUtil.isAdminOrHouseholdPrivileged(auth, householdId, householdService)) {
      throw new PermissionDeniedException(
        "You do not have permission to delete an item from this shopping list."
      );
    }

    LOGGER.info("Deleting item {} from shopping list", id);

    shoppingListItemService.deleteShoppingListItem(id);

    return ResponseEntity.noContent().build();
  }

  /**
   * Method to check an item in a shopping list.
   * An item can be checked or unchecked.
   * @param auth authentication for user.
   * @param itemId the id for the item that is being checked.
   * @return the item to be checked or unched.
   * @throws NullPointerException if any values are null.
   * @throws ShoppingListNotFoundException if the shopping list the items is in is not found.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws PermissionDeniedException if the user does not have permission to check an item on the shopping list.
   * @throws ShoppingListItemNotFoundException if the shopping list item is not found.
   */
  @PutMapping(value = "/check-item/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Check an item in a shopping list",
    description = "Checks or unchecks an item in a shopping list. Requires authentication and be privileged member of the household.",
    tags = { "shoppinglistitem" }
  )
  public ResponseEntity<ShoppingListItemDTO> checkOrUncheckItemOnShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("id") UUID id
  )
    throws NullPointerException, ShoppingListItemNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, PermissionDeniedException {
    ShoppingListItem shoppingListItem = shoppingListItemService.getItemById(id);

    if (
      !PrivilegeUtil.isAdminOrHouseholdPrivileged(
        auth,
        shoppingListItem.getShoppingList().getHousehold().getId(),
        householdService
      )
    ) throw new PermissionDeniedException("Du har ikke tilgang til å sjekke av denne matvaren");

    shoppingListItem.setChecked(!shoppingListItem.isChecked());

    LOGGER.info(
      "Checked shopping list item: {} to {}",
      shoppingListItem,
      shoppingListItem.isChecked()
    );
    shoppingListItemService.updateShoppingListItem(shoppingListItem);

    return ResponseEntity.ok(
      ShoppingListItemMapper.INSTANCE.shoppingListItemsToShoppingListItemDTO(shoppingListItem)
    );
  }
}
