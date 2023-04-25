package edu.ntnu.idatt2106.smartmat.controller.shoppinglist;

import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.CreateShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.NewItemOnShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.UpdateShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.PermissionDeniedException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.ingredient.IngredientNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.IncorrectItemAmountException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.mapper.shoppinglist.ShoppingListMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.CustomFoodItemService;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListItemService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import edu.ntnu.idatt2106.smartmat.validation.user.AuthValidation;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for public shopping list endpoints.
 * @author Tobias O., Carl G.
 * @version 1.0 - 24.04.2023.
 */
@RestController
@RequestMapping("/api/v1/private/shoppinglists")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class ShoppingListController {

  private final ShoppingListService shoppinglistService;
  private final HouseholdService householdService;
  private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingListController.class);
  private final IngredientService ingredientService;
  private final ShoppingListItemService shoppingListItemService;
  private final CustomFoodItemService customFoodItemService;

  private boolean isAdminOrHouseholdOwner(Auth auth, UUID householdId)
    throws UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    return (
      AuthValidation.hasRole(auth, UserRole.ADMIN) ||
      householdService.isHouseholdOwner(householdId, auth.getUsername())
    );
  }

  /**
   * Method to check whether the user is Admin or member.
   * @param auth authentication for user
   * @param householdId the id of the household.
   * @return admin or member.
   * @throws UserDoesNotExistsException if the user is not found.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws NullPointerException if any value are null.
   */

  private boolean isAdminOrHouseholdMember(Auth auth, UUID householdId)
    throws UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    return (
      AuthValidation.hasRole(auth, UserRole.ADMIN) ||
      householdService.isHouseholdMember(householdId, auth.getUsername())
    );
  }

  /**
   * Method to check whether the user is Admin or privileged member.
   * @param auth authentication for user
   * @param householdId the id of the household.
   * @return admin or privileged member.
   * @throws UserDoesNotExistsException if the user is not found.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws NullPointerException if any value are null.
   */
  private boolean isAdminOrPrivilegedHouseholdMember(Auth auth, UUID householdId)
    throws UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    return (
      isAdminOrHouseholdOwner(auth, householdId) ||
      householdService.isHouseholdMemberWithRole(
        householdId,
        auth.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    );
  }

  /**
   * Method to create a shopping list.
   * @param auth authentication for user
   * @param shoppingListDTO the DTO for the shopping list.
   * @return the created shopping list.
   * @throws ShoppingListAlreadyExistsException if the shopping list already exists.
   * @throws NullPointerException if any values are null.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws PermissionDeniedException if the user does not have permission to create a shopping list.
   */
  @PostMapping(
    value = "",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Creates a new shopping list for the given household",
    description = "Get a household by id and create a shopping list if an open one does not exist. Requires authentication and be owner of the household.",
    tags = { "shopping list" }
  )
  public ResponseEntity<ShoppingListDTO> createShoppingList(
    @AuthenticationPrincipal Auth auth,
    @RequestBody CreateShoppingListDTO shoppingListDTO
  )
    throws ShoppingListAlreadyExistsException, NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException, PermissionDeniedException {
    LOGGER.info("POST request for creating a new shopping list: {}", shoppingListDTO);

    UUID householdId = UUID.fromString(shoppingListDTO.getHousehold());

    boolean hasCreateAccess = isAdminOrHouseholdOwner(auth, householdId);
    LOGGER.info("User has create access: {}", hasCreateAccess);

    if (!hasCreateAccess) {
      throw new PermissionDeniedException(
        "Du har ikke tilgang til å lage en handleliste for denne hustanden"
      );
    }

    Household household = householdService.getHouseholdById(householdId);
    try {
      shoppinglistService.getCurrentShoppingList(householdId);
      throw new ShoppingListAlreadyExistsException("Du har allerede en åpen handleliste");
    } catch (ShoppingListNotFoundException e) {
      LOGGER.info("No open shopping list for household {}.", household);
    }
    ShoppingList shoppingList = ShoppingList
      .builder()
      .id(UUID.randomUUID())
      .household(household)
      .build();
    shoppingList = shoppinglistService.saveShoppingList(shoppingList);
    LOGGER.info("Created a new shopping list with id: {}", shoppingList.getId());

    ShoppingListDTO createdShoppingListDTO = ShoppingListMapper.INSTANCE.shoppingListToDTO(
      shoppingList
    );

    LOGGER.info("Mapped shopping list with id: {} to shoppingListDTO", createdShoppingListDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdShoppingListDTO);
  }

  /**
   * Method to retrieve a shopping list.
   * @param auth authentication for user.
   * @param id id for the shopping list.
   * @return the shopping list.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws NullPointerException if any values are null.
   * @throws PermissionDeniedException if the user does not have permission to create a shopping list.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws UserDoesNotExistsException if the user does not exist.
   */
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Get a shopping list by id",
    description = "Get a shopping list by id. Requires authentication and be part of the household.",
    tags = { "shopping list" }
  )
  ResponseEntity<ShoppingListDTO> getShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id
  )
    throws ShoppingListNotFoundException, NullPointerException, PermissionDeniedException, HouseholdNotFoundException, UserDoesNotExistsException {
    LOGGER.info("GET request for shopping list: {}", id);
    ShoppingList shoppingList = shoppinglistService.getShoppingListById(id);

    boolean hasAccess = isAdminOrHouseholdMember(auth, shoppingList.getHousehold().getId());
    if (!hasAccess) {
      throw new PermissionDeniedException("Du har ikke tilgang til handlelisten");
    }

    ShoppingListDTO shoppingListDTO = ShoppingListMapper.INSTANCE.shoppingListToDTO(shoppingList);
    LOGGER.info("Mapped shopping list to ShoppingListDTO {}", shoppingListDTO);

    return ResponseEntity.ok(shoppingListDTO);
  }

  /**
   * Method to update a shopping list.
   * @param auth authentication for user.
   * @param id id for the shopping list.
   * @param shoppingListDTO the DTO for the shopping list.
   * @return the updated shopping list.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws NullPointerException if any values are null.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws PermissionDeniedException if the user does not have permission to update the shopping list.
   */
  @PutMapping(
    value = "/{id}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Update a shopping list by id",
    description = "Update a shopping list by id. Requires authentication and be owner of the household.",
    tags = { "shopping list" }
  )
  ResponseEntity<ShoppingListDTO> updateShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id,
    @RequestBody UpdateShoppingListDTO shoppingListDTO
  )
    throws ShoppingListNotFoundException, NullPointerException, UserDoesNotExistsException, HouseholdNotFoundException, PermissionDeniedException {
    ShoppingList shoppingList = shoppinglistService.getShoppingListById(id);
    Household currenHousehold = shoppingList.getHousehold();

    boolean hasUpdateAccess = isAdminOrHouseholdOwner(auth, currenHousehold.getId());
    LOGGER.info("User has access to update: {}", hasUpdateAccess);
    if (!hasUpdateAccess) {
      throw new PermissionDeniedException("Du kan ikke oppdatere denne handlelisten");
    }
    shoppingList.setDateCompleted(shoppingListDTO.getDateCompleted());

    Household potentialNewHousehold = householdService.getHouseholdById(
      UUID.fromString(shoppingListDTO.getHousehold())
    );
    if (
      !potentialNewHousehold.equals(currenHousehold) &&
      isAdminOrHouseholdOwner(auth, potentialNewHousehold.getId())
    ) {
      shoppingList.setHousehold(potentialNewHousehold);
    }

    shoppingList = shoppinglistService.updateShoppingList(id, shoppingList);

    ShoppingListDTO updatedShoppingListDTO = ShoppingListMapper.INSTANCE.shoppingListToDTO(
      shoppingList
    );
    LOGGER.info("Updated shopping list: {}", updatedShoppingListDTO);

    return ResponseEntity.ok(updatedShoppingListDTO);
  }

  /**
   * Method to complete a shopping list.
   * @param auth authentication for user.
   * @param id id for the shopping list.
   * @return the completed shopping list.
   * @throws NullPointerException if any values are null.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws PermissionDeniedException if the user does not have permission to complete a shopping list.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @PutMapping(value = "/{id}/complete", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Complete a shopping list by id",
    description = "Complete a shopping list by id. Requires authentication and be owner of the household.",
    tags = { "shopping list" }
  )
  public ResponseEntity<ShoppingListDTO> completeShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id
  )
    throws NullPointerException, ShoppingListNotFoundException, PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException {
    LOGGER.info("Request to complete shopping list with id: {}", id);

    ShoppingList shoppingList = shoppinglistService.getShoppingListById(id);

    boolean hasUpdateAccess = isAdminOrPrivilegedHouseholdMember(
      auth,
      shoppingList.getHousehold().getId()
    );
    if (!hasUpdateAccess) {
      throw new PermissionDeniedException("Du kan ikke fullføre denne handlelisten");
    }

    LocalDate dateCompleted = LocalDate.now();
    shoppingList.setDateCompleted(dateCompleted);
    LOGGER.info("Completed shoppinglist {} at {}", shoppingList, dateCompleted);

    shoppingList = shoppinglistService.updateShoppingList(shoppingList.getId(), shoppingList);

    ShoppingListDTO shoppingListDTO = ShoppingListMapper.INSTANCE.shoppingListToDTO(shoppingList);

    LOGGER.info("Shopping list {} completed", shoppingListDTO);

    return ResponseEntity.ok(shoppingListDTO);
  }

  /**
   * Method to delete a shopping list.
   * @param auth authentication for user.
   * @param id id for the shopping list.
   * @return the deleted shopping list.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws NullPointerException if any values are null.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws PermissionDeniedException if the user does not have permission to delete a shopping list.
   */
  @DeleteMapping(value = "/{id}")
  @Operation(
    summary = "Delete a shopping list by id",
    description = "Delete a shopping list by id. Requires authentication and be owner of the household.",
    tags = { "shopping list" }
  )
  public ResponseEntity<Void> deleteShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id
  )
    throws ShoppingListNotFoundException, NullPointerException, UserDoesNotExistsException, HouseholdNotFoundException, PermissionDeniedException {
    LOGGER.info("Request to delete shopping list with id: {}", id);

    ShoppingList shoppingList = shoppinglistService.getShoppingListById(id);

    boolean hasAccess = isAdminOrHouseholdOwner(auth, shoppingList.getHousehold().getId());
    if (!hasAccess) {
      throw new PermissionDeniedException("Du kan ikke slette denne handlelisten");
    }

    shoppinglistService.deleteShoppingListById(id);

    LOGGER.info("Shopping list {} deleted", id);

    return ResponseEntity.noContent().build();
  }

  /**
   * Method to add an item to a shopping list.
   * @param auth authentication for user.
   * @param id the id of the shopping list.
   * @param item the item that will be added to the shopping list.
   * @return the item to the shopping list.
   * @throws NullPointerException if any values are null.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws PermissionDeniedException if the user does not have permission to add an item to the shopping list.
   * @throws IncorrectItemAmountException if the added item is invalid.
   */
  @PostMapping(
    value = "/{id}/add-item",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Add an item to a shopping list",
    description = "Add an item to a shopping list. Requires authentication and be owner of the household. First checks if an ingredient with the item name exists. If it does, it adds a shopping list item. Otherwise, it adds a custom food item.",
    tags = { "shopping list" }
  )
  public ResponseEntity<ShoppingListDTO> addItemToShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id,
    @RequestBody NewItemOnShoppingListDTO item
  )
    throws NullPointerException, ShoppingListNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, PermissionDeniedException, IncorrectItemAmountException {
    ShoppingList shoppingList = shoppinglistService.getShoppingListById(id);
    boolean hasAccess = isAdminOrHouseholdOwner(auth, shoppingList.getHousehold().getId());
    if (!hasAccess) {
      throw new PermissionDeniedException("Du kan ikke legge til elementer i denne handlelisten");
    }

    try {
      Collection<Ingredient> ingredients = ingredientService.getIngredientsByName(item.getName());
      LOGGER.info("Found ingredients: {}", ingredients);
      Ingredient ingredient = ingredients
        .stream()
        .findFirst()
        .orElseThrow(IngredientNotFoundException::new);
      ShoppingListItem shoppingListItem = ShoppingListItem
        .builder()
        .shoppingList(shoppingList)
        .ingredient(ingredient)
        .amount(item.getAmount())
        .build();
      LOGGER.info("Saving shopping list {}", shoppingListItem);
      shoppingListItem = shoppingListItemService.saveShoppingListItem(shoppingListItem);
      Collection<ShoppingListItem> items = shoppingList.getShoppingListItems();
      LOGGER.info("Adding shopping list item {} to shopping list {}", shoppingListItem, items);
      Set<ShoppingListItem> shoppingListItems = new HashSet<>();
      shoppingListItems.add(shoppingListItem);
      shoppingListItems.addAll(items);
      shoppingList.getShoppingListItems().add(shoppingListItem);
    } catch (IngredientNotFoundException e) {
      int amount = (int) item.getAmount();
      if (item.getAmount() != (double) amount) {
        throw new IncorrectItemAmountException(
          "Klarte ikke å legge til matvaren. Legg til antall i heltall."
        );
      }
      CustomFoodItem customItem = CustomFoodItem
        .builder()
        .name(item.getName())
        .shoppingList(shoppingList)
        .amount(amount)
        .build();
      customItem = customFoodItemService.saveCustomFoodItem(customItem);
      Collection<CustomFoodItem> items = shoppingList.getCustomFoodItems();
      LOGGER.info("Adding item {} to custom food items {}", customItem, items);
      Set<CustomFoodItem> customFoodItems = new HashSet<>();
      customFoodItems.add(customItem);
      customFoodItems.addAll(items);
      shoppingList.setCustomFoodItems(customFoodItems);
    }

    shoppingList = shoppinglistService.updateShoppingList(id, shoppingList);

    ShoppingListDTO shoppingListDTO = ShoppingListMapper.INSTANCE.shoppingListToDTO(shoppingList);

    LOGGER.info("Returning shopping list dto: {}", shoppingListDTO.getCustomFoodItems());

    return ResponseEntity.ok(shoppingListDTO);
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
  @DeleteMapping(value = "/{id}/delete-item/{item-id}")
  @Operation(
    summary = "Delete an item from a shopping list",
    description = "Delete an item from a shopping list. Requires authentication and be owner of the household. First checks if a shopping list item exists. If it is, it deletes the shopping list item. Otherwise, it deletes the custom food item if possible.",
    tags = { "shopping list" }
  )
  public ResponseEntity<ShoppingListDTO> deleteItemFromShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("id") UUID shoppingListId,
    @PathVariable("item-id") UUID itemId
  )
    throws NullPointerException, ShoppingListItemNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, PermissionDeniedException, ShoppingListNotFoundException {
    ShoppingList shoppingList = shoppinglistService.getShoppingListById(shoppingListId);
    boolean hasAccess = isAdminOrHouseholdOwner(auth, shoppingList.getHousehold().getId());
    if (!hasAccess) {
      throw new PermissionDeniedException("Du har ikke tilgang til elementer i denne handlelisten");
    }

    LOGGER.info("Deleting item {} from shopping list {}", itemId, shoppingListId);

    try {
      shoppingListItemService.deleteShoppingListItemInShoppingList(shoppingListId, itemId);
      LOGGER.info("Deleted shopping list item.");
    } catch (ShoppingListItemNotFoundException e) {
      customFoodItemService.deleteCustomFoodItemInShoppingList(shoppingListId, itemId);
      LOGGER.info("Deleted custom food item.");
    }

    return ResponseEntity.noContent().build();
  }

  /**
   * Method to check an item on a shopping list.
   * An item can be checked or unchecked.
   * @param auth authentication for user.
   * @param id the id for the shopping list.
   * @param itemId the id for the item that is being checked.
   * @return the item to be checked or unched.
   * @throws NullPointerException if any values are null.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws PermissionDeniedException if the user does not have permission to check an item on the shopping list.
   * @throws ShoppingListItemNotFoundException if the shopping list item is not found.
   */
  @PutMapping(value = "/{id}/check-item/{item-id}")
  @Operation(
    summary = "Check an item in a shopping list",
    description = "Check an item in a shopping list. Requires authentication and be privileged member of the household. First checks if a shopping list item exists. If it is, it checks the shopping list item. Otherwise, it checks the custom food item if possible.",
    tags = { "shopping list" }
  )
  public ResponseEntity<ShoppingListDTO> checkOrUncheckItemOnShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("id") UUID shoppingListId,
    @PathVariable("item-id") UUID itemId
  )
    throws NullPointerException, ShoppingListItemNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, PermissionDeniedException, ShoppingListNotFoundException {
    ShoppingList shoppingList = shoppinglistService.getShoppingListById(shoppingListId);
    boolean hasAccess = isAdminOrPrivilegedHouseholdMember(
      auth,
      shoppingList.getHousehold().getId()
    );
    if (!hasAccess) {
      throw new PermissionDeniedException("Du har ikke tilgang til elementer i denne handlelisten");
    }

    LOGGER.info("Checking item {} in shopping list {}", itemId, shoppingList);

    if (shoppingListItemService.existsByIdInShoppingList(shoppingListId, itemId)) {
      ShoppingListItem shoppingListItem = shoppingListItemService.getItemById(itemId);

      if (
        !Objects.equals(shoppingListItem.getShoppingList(), shoppingList)
      ) throw new PermissionDeniedException("Du har ikke tilgang til å sjekke av denne matvaren");

      shoppingListItem.setChecked(!shoppingListItem.isChecked());
      LOGGER.info(
        "Checked shopping list item: {} to {}",
        shoppingListItem,
        shoppingListItem.isChecked()
      );
      shoppingListItemService.saveShoppingListItem(shoppingListItem);
    } else if (customFoodItemService.existsByIdInShoppingList(shoppingListId, itemId)) {
      CustomFoodItem customFoodItem = customFoodItemService.getItemById(itemId);

      if (
        !Objects.equals(customFoodItem.getShoppingList(), shoppingList)
      ) throw new PermissionDeniedException("Du har ikke tilgang til å sjekke av denne matvaren");

      customFoodItem.setChecked(!customFoodItem.isChecked());
      LOGGER.info("Checked custom food item: {} to {}", customFoodItem, customFoodItem.isChecked());
      customFoodItemService.saveCustomFoodItem(customFoodItem);
    } else {
      throw new ShoppingListItemNotFoundException();
    }

    ShoppingListDTO shoppingListDTO = ShoppingListMapper.INSTANCE.shoppingListToDTO(shoppingList);

    return ResponseEntity.ok(shoppingListDTO);
  }
}
