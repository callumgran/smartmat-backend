package edu.ntnu.idatt2106.smartmat.controller.foodproduct;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CreateCustomFoodItemDTO;
import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CustomFoodItemDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.PermissionDeniedException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.validation.BadInputException;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.CustomFoodItemMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.CustomFoodItemService;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
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
 * Rest controller for custom food items.
 * Handles requests from the client, and sends the response back to the client.
 * Handles all requests related to custom food items.
 * @auther Callum Gran
 * @version 1.2 - 26.04.2023
 */
@RestController
@RequestMapping("/api/v1/private/customfooditems")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class CustomFoodProductController {

  private final CustomFoodItemService customFoodItemService;

  private final ShoppingListService shoppingListService;

  private final HouseholdService householdService;

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomFoodProductController.class);

  /**
   * Method to add an item to a shopping list.
   * @param auth authentication for user.
   * @param item the item to add.
   * @return the item added.
   * @throws NullPointerException if any value are null.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws UserDoesNotExistsException if the user is not found.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws PermissionDeniedException if the user does not have permission to add an item to the shopping list.
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
    tags = { "customfooditem" }
  )
  public ResponseEntity<CustomFoodItemDTO> addItemToShoppingList(
    @AuthenticationPrincipal Auth auth,
    @RequestBody CreateCustomFoodItemDTO item
  )
    throws NullPointerException, ShoppingListNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, PermissionDeniedException, ShoppingListItemNotFoundException, BadInputException {
    ShoppingList shoppingList = shoppingListService.getShoppingListById(item.getShoppingListId());

    LOGGER.info("Adding item to shopping list with id: " + item.getShoppingListId());

    if (
      !PrivilegeUtil.isAdminOrHouseholdPrivileged(
        auth,
        shoppingList.getHousehold().getId(),
        householdService
      )
    ) {
      throw new PermissionDeniedException(
        "Du har ikke tilgang til å legge til en vare i denne handlelisten"
      );
    }

    if (
      !FoodProductValidation.validateCreateCustomFoodProduct(item.getName(), item.getAmount())
    ) throw new BadInputException("Ugyldig input for custom food item");

    CustomFoodItem foodItem;

    if (
      shoppingList.getCustomFoodItems().stream().anyMatch(s -> s.getName().equals(item.getName()))
    ) {
      CustomFoodItem current = shoppingList
        .getCustomFoodItems()
        .stream()
        .filter(s -> s.getName().equals(item.getName()))
        .findFirst()
        .get();
      current.setAmount(current.getAmount() + item.getAmount());
      current.setChecked(false);
      foodItem = customFoodItemService.updateCustomFoodItem(current);
      LOGGER.info("Updated item in shopping list with id: " + item.getShoppingListId());
    } else {
      foodItem =
        customFoodItemService.saveCustomFoodItem(
          CustomFoodItem
            .builder()
            .name(item.getName())
            .amount(item.getAmount())
            .checked(false)
            .shoppingList(shoppingList)
            .build()
        );
      LOGGER.info("Added item to shopping list with id: " + item.getShoppingListId());
    }

    CustomFoodItemDTO customFoodItemDTO = CustomFoodItemMapper.INSTANCE.customFoodItemsToCustomFoodItemDTO(
      foodItem
    );

    LOGGER.info("Returning item with id: " + customFoodItemDTO.getId());

    return ResponseEntity.ok(customFoodItemDTO);
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
    tags = { "customfooditem" }
  )
  public ResponseEntity<Void> deleteItemFromShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("householdId") UUID householdId,
    @PathVariable("id") UUID id
  )
    throws NullPointerException, ShoppingListItemNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, PermissionDeniedException {
    if (!PrivilegeUtil.isAdminOrHouseholdPrivileged(auth, householdId, householdService)) {
      throw new PermissionDeniedException(
        "Du har ikke tilgang til å slette en vare i denne handlelisten"
      );
    }

    LOGGER.info("Deleting item {} from shopping list", id);

    customFoodItemService.deleteCustomFoodItem(id);

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
    tags = { "customfooditem" }
  )
  public ResponseEntity<CustomFoodItemDTO> checkOrUncheckItemOnShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("id") UUID id
  )
    throws NullPointerException, ShoppingListItemNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, PermissionDeniedException {
    CustomFoodItem shoppingListItem = customFoodItemService.getItemById(id);

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
    customFoodItemService.updateCustomFoodItem(shoppingListItem);

    return ResponseEntity.ok(
      CustomFoodItemMapper.INSTANCE.customFoodItemsToCustomFoodItemDTO(shoppingListItem)
    );
  }
}
