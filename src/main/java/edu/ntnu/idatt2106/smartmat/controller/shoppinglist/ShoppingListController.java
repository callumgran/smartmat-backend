package edu.ntnu.idatt2106.smartmat.controller.shoppinglist;

import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.CreateShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.UpdateShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.PermissionDeniedException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.BasketNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.mapper.shoppinglist.ShoppingListMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.Basket;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.HouseholdFoodProductService;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import edu.ntnu.idatt2106.smartmat.utils.PrivilegeUtil;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.List;
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
 * @author Tobias O., Carl G., Callum G.
 * @version 1.3 - 05.05.2023.
 */
@RestController
@RequestMapping("/api/v1/private/shoppinglists")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class ShoppingListController {

  private final ShoppingListService shoppinglistService;

  private final HouseholdService householdService;

  private final HouseholdFoodProductService householdFoodProductService;

  private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingListController.class);

  /**
   * Method to create a shopping list.
   * @param auth authentication for user
   * @param shoppingListDTO the DTO for the shopping list.
   * @return 201 if the shopping list is created.
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
    description = "Get a household by id and create a shopping list if an open one does not exist. Requires authentication and be a privileged user for the household.",
    tags = { "shopping list" }
  )
  public ResponseEntity<ShoppingListDTO> createShoppingList(
    @AuthenticationPrincipal Auth auth,
    @RequestBody CreateShoppingListDTO shoppingListDTO
  )
    throws ShoppingListAlreadyExistsException, NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException, PermissionDeniedException {
    LOGGER.info("POST request for creating a new shopping list: {}", shoppingListDTO);

    UUID householdId = shoppingListDTO.getHousehold();

    if (!PrivilegeUtil.isAdminOrHouseholdPrivileged(auth, householdId, householdService)) {
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
   * @return 200 if the shopping list is found.
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

    if (
      !PrivilegeUtil.isAdminOrHouseholdMember(
        auth,
        shoppingList.getHousehold().getId(),
        householdService
      )
    ) {
      throw new PermissionDeniedException("Du har ikke tilgang til handlelisten");
    }

    ShoppingListDTO shoppingListDTO = ShoppingListMapper.INSTANCE.shoppingListToDTO(shoppingList);
    LOGGER.info("Mapped shopping list to ShoppingListDTO {}", shoppingListDTO);

    return ResponseEntity.ok(shoppingListDTO);
  }

  /**
   * Method to retrieve a shopping list with difference
   * to what is in the basket.
   * @param auth authentication for user.
   * @param id id for the shopping list.
   * @return 200 OK and the shopping list with the difference.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   * @throws BasketNotFoundException if the basket is not found.
   * @throws NullPointerException if any values are null.
   * @throws PermissionDeniedException if the user does not have permission to create a shopping list.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws UserDoesNotExistsException if the user does not exist.
   */
  @GetMapping(value = "/{id}/diff", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Get a shopping list by id",
    description = "Get a shopping list by id. Requires authentication and be part of the household.",
    tags = { "shopping list" }
  )
  ResponseEntity<ShoppingListDTO> getShoppingListDiff(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id
  )
    throws ShoppingListNotFoundException, BasketNotFoundException, NullPointerException, PermissionDeniedException, HouseholdNotFoundException, UserDoesNotExistsException {
    LOGGER.info("GET request for shopping list: {}", id);

    ShoppingList shoppingList = shoppinglistService.getShoppingListById(id);

    if (
      !PrivilegeUtil.isAdminOrHouseholdMember(
        auth,
        shoppingList.getHousehold().getId(),
        householdService
      )
    ) {
      throw new PermissionDeniedException("Du har ikke tilgang til handlelisten");
    }

    shoppingList = shoppinglistService.getShoppingListWithDiff(id);

    ShoppingListDTO shoppingListDTO = ShoppingListMapper.INSTANCE.shoppingListToDTO(shoppingList);
    LOGGER.info("Mapped shopping list to ShoppingListDTO {}", shoppingListDTO);

    return ResponseEntity.ok(shoppingListDTO);
  }

  /**
   * Method to update a shopping list.
   * @param auth authentication for user.
   * @param id id for the shopping list.
   * @param shoppingListDTO the DTO for the shopping list.
   * @return 200 OK if the shopping list is updated.
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

    if (
      !PrivilegeUtil.isAdminOrHouseholdOwner(
        auth,
        shoppingList.getHousehold().getId(),
        householdService
      )
    ) {
      throw new PermissionDeniedException("Du kan ikke oppdatere denne handlelisten");
    }

    LOGGER.info("User has access to update");

    shoppingList.setDateCompleted(shoppingListDTO.getDateCompleted());

    Household potentialNewHousehold = householdService.getHouseholdById(
      shoppingListDTO.getHousehold()
    );
    if (
      !potentialNewHousehold.equals(currenHousehold) &&
      !PrivilegeUtil.isAdminOrHouseholdOwner(auth, potentialNewHousehold.getId(), householdService)
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
   * @return 200 OK if the shopping list is completed.
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

    final ShoppingList shoppingList = shoppinglistService.getShoppingListById(id);

    if (
      !PrivilegeUtil.isAdminOrHouseholdPrivileged(
        auth,
        shoppingList.getHousehold().getId(),
        householdService
      )
    ) {
      throw new PermissionDeniedException("Du kan ikke fullføre denne handlelisten");
    }

    LocalDate dateCompleted = LocalDate.now();
    shoppingList.setDateCompleted(dateCompleted);
    LOGGER.info("Completed shoppinglist {} at {}", shoppingList, dateCompleted);

    Basket basket = shoppingList.getBasket();

    List<HouseholdFoodProduct> householdFoodProducts = basket
      .getBasketItems()
      .stream()
      .map(bi -> {
        return HouseholdFoodProduct
          .builder()
          .foodProduct(bi.getFoodProduct())
          .household(shoppingList.getHousehold())
          .amountLeft(bi.getAmount())
          .expirationDate(LocalDate.now().plusDays(14))
          .build();
      })
      .toList();

    for (HouseholdFoodProduct householdFoodProduct : householdFoodProducts) {
      householdFoodProductService.saveFoodProduct(householdFoodProduct);
    }

    final ShoppingList ret = shoppinglistService.updateShoppingList(
      shoppingList.getId(),
      shoppingList
    );

    ShoppingListDTO shoppingListDTO = ShoppingListMapper.INSTANCE.shoppingListToDTO(ret);

    LOGGER.info("Shopping list {} completed", shoppingListDTO);

    return ResponseEntity.ok(shoppingListDTO);
  }

  /**
   * Method to delete a shopping list.
   * @param auth authentication for user.
   * @param id id for the shopping list.
   * @return 204 NO CONTENT if the shopping list is deleted.
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

    if (
      !PrivilegeUtil.isAdminOrHouseholdOwner(
        auth,
        shoppingList.getHousehold().getId(),
        householdService
      )
    ) {
      throw new PermissionDeniedException("Du kan ikke slette denne handlelisten");
    }

    shoppinglistService.deleteAllShoppingListItems(id);

    shoppinglistService.deleteShoppingListById(id);

    LOGGER.info("Shopping list {} deleted", id);

    return ResponseEntity.noContent().build();
  }
}
