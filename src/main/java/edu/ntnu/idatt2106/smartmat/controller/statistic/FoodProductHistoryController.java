package edu.ntnu.idatt2106.smartmat.controller.statistic;

import edu.ntnu.idatt2106.smartmat.dto.statistic.FoodProductHistoryDTO;
import edu.ntnu.idatt2106.smartmat.dto.statistic.UpdateFoodProductHistoryDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.PermissionDeniedException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.statistic.FoodProductHistoryNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.mapper.statistic.FoodProductHistoryMapper;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.statistic.FoodProductHistoryService;
import edu.ntnu.idatt2106.smartmat.validation.user.AuthValidation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/private/stats")
@RequiredArgsConstructor
public class FoodProductHistoryController {

  private final FoodProductHistoryService foodProductHistoryService;

  private final HouseholdService householdService;

  private boolean isAdminOrHouseholdMember(Auth auth, UUID householdId)
    throws UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    return (
      AuthValidation.hasRole(auth, UserRole.ADMIN) ||
      householdService.isHouseholdMember(householdId, auth.getUsername())
    );
  }

  /**
   * Method for getting a single stat entry for a food product.
   * @param auth The auth object of the user.
   * @param foodProductHistoryId The id of the food product history.
   * @return The food product stats.
   * @throws PermissionDeniedException If the user is not an admin or a member of the household.
   * @throws FoodProductHistoryNotFoundException If the food product history does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws NullPointerException If the household is null.
   */
  @GetMapping("/{foodProductHistoryId}")
  @Operation(
    summary = "Get a single stat entry for a food product.",
    description = "Returns a single stat entry for a food product. Requires admin access or household member access.",
    tags = { "stats" }
  )
  public ResponseEntity<FoodProductHistoryDTO> getFoodProductHistoryById(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID foodProductHistoryId
  )
    throws PermissionDeniedException, FoodProductHistoryNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    FoodProductHistory foodProductHistory = foodProductHistoryService.getFoodProductHistoryById(
      foodProductHistoryId
    );
    if (
      !isAdminOrHouseholdMember(auth, foodProductHistory.getHousehold().getId())
    ) throw new PermissionDeniedException(
      "Du har ikke tilgang til å se statistikken for denne matvaren."
    );

    return ResponseEntity.ok(
      FoodProductHistoryMapper.INSTANCE.foodProductHistoryToFoodProductHistoryDTO(
        foodProductHistory
      )
    );
  }

  /**
   * Method for getting all stat entries for a food product.
   * @param auth The auth object of the user.
   * @param foodProductId The id of the food product.
   * @return A list of food product stats.
   * @throws PermissionDeniedException If the user is not an admin or a member of the household.
   * @throws FoodProductHistoryNotFoundException If the food product history does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws NullPointerException If the household is null.
   */
  @PutMapping("/{foodProductHistoryId}")
  @Operation(
    summary = "Update a single stat entry for a food product.",
    description = "Updates a single stat entry for a food product. Requires admin access or household member access.",
    tags = { "stats" }
  )
  public ResponseEntity<FoodProductHistoryDTO> updateFoodProductHistory(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID foodProductHistoryId,
    @RequestBody UpdateFoodProductHistoryDTO updateFoodHistoryDTO
  )
    throws PermissionDeniedException, FoodProductHistoryNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    FoodProductHistory foodProductHistory = foodProductHistoryService.getFoodProductHistoryById(
      foodProductHistoryId
    );
    if (
      !isAdminOrHouseholdMember(auth, foodProductHistory.getHousehold().getId())
    ) throw new PermissionDeniedException(
      "Du har ikke tilgang til å endre statistikken for denne matvaren."
    );

    FoodProductHistory newFoodProductHistory = FoodProductHistory
      .builder()
      .household(foodProductHistory.getHousehold())
      .foodProduct(foodProductHistory.getFoodProduct())
      .date(updateFoodHistoryDTO.getDate())
      .thrownAmount(updateFoodHistoryDTO.getThrownAmount())
      .build();

    return ResponseEntity.ok(
      FoodProductHistoryMapper.INSTANCE.foodProductHistoryToFoodProductHistoryDTO(
        foodProductHistoryService.updateFoodProductHistory(
          foodProductHistoryId,
          newFoodProductHistory
        )
      )
    );
  }

  /**
   * Method for deleting a single stat entry for a food product.
   * @param auth The auth object of the user.
   * @param foodProductHistoryId The id of the food product history.
   * @return A response entity with 204 no content.
   * @throws PermissionDeniedException If the user is not an admin or a member of the household.
   * @throws FoodProductHistoryNotFoundException If the food product history does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws NullPointerException If the household is null.
   */
  @DeleteMapping("/{foodProductHistoryId}")
  @Operation(
    summary = "Delete a single stat entry for a food product.",
    description = "Deletes a single stat entry for a food product. Requires admin access or household member access.",
    tags = { "stats" }
  )
  public ResponseEntity<Void> deleteFoodProductHistory(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID foodProductHistoryId
  )
    throws PermissionDeniedException, FoodProductHistoryNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    FoodProductHistory foodProductHistory = foodProductHistoryService.getFoodProductHistoryById(
      foodProductHistoryId
    );
    if (
      !isAdminOrHouseholdMember(auth, foodProductHistory.getHousehold().getId())
    ) throw new PermissionDeniedException(
      "Du har ikke tilgang til å slette statistikken for denne matvaren."
    );

    foodProductHistoryService.deleteFoodProductHistoryById(foodProductHistoryId);

    return ResponseEntity.noContent().build();
  }

  /**
   * Method for getting the stats and history of a food product.
   * @param foodProductId The id of the food product.
   * @return The history of the food product.
   * @throws PermissionDeniedException If the user is not an admin.
   * @throws FoodProductHistoryNotFoundException If the food product history does not exist.
   */
  @GetMapping("/foodproduct/{foodProductId}")
  @Operation(
    summary = "Get the history of a food product.",
    description = "Returns the history of a food product for all households of all time. Requires admin access.",
    tags = { "stats" }
  )
  public ResponseEntity<Collection<FoodProductHistoryDTO>> getFoodProductHistoryForProduct(
    @AuthenticationPrincipal Auth auth,
    @PathVariable Long foodProductId
  ) throws PermissionDeniedException, FoodProductHistoryNotFoundException {
    if (!AuthValidation.hasRole(auth, UserRole.ADMIN)) throw new PermissionDeniedException(
      "Du har ikke tilgang til å se statistikk for matvarer."
    );
    return ResponseEntity.ok(
      foodProductHistoryService
        .getAllFoodProductHistoryByFoodProductId(foodProductId)
        .stream()
        .map(fph -> FoodProductHistoryMapper.INSTANCE.foodProductHistoryToFoodProductHistoryDTO(fph)
        )
        .toList()
    );
  }

  /**
   * Method for getting the stats and history of a food product.
   * @param foodProductId The id of the food product.
   * @param householdId The id of the household.
   * @return The history of the food product.
   * @throws PermissionDeniedException If the user is not an admin.
   * @throws FoodProductHistoryNotFoundException If the food product history does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws NullPointerException If the household id is null.
   */
  @GetMapping("/foodproduct/{foodProductId}/household/{householdId}")
  @Operation(
    summary = "Get the history of a food product.",
    description = "Returns the history of a food product for a household of all time. If the user is not an admin, the householdId must be the id of the household the user is a member of.",
    tags = { "stats" }
  )
  public ResponseEntity<Collection<FoodProductHistoryDTO>> getFoodProductHistoryForProductAndHousehold(
    @AuthenticationPrincipal Auth auth,
    @PathVariable Long foodProductId,
    @PathVariable UUID householdId
  )
    throws PermissionDeniedException, FoodProductHistoryNotFoundException, UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    if (!isAdminOrHouseholdMember(auth, householdId)) throw new PermissionDeniedException(
      "Du har ikke tilgang til å se statistikk for matvarer."
    );
    return ResponseEntity.ok(
      foodProductHistoryService
        .getAllFoodProductHistoryByHouseholdIdAndFoodProductId(householdId, foodProductId)
        .stream()
        .map(fph -> FoodProductHistoryMapper.INSTANCE.foodProductHistoryToFoodProductHistoryDTO(fph)
        )
        .toList()
    );
  }

  /**
   * Method for getting the stats and history of all food products for a household.
   * @param householdId The id of the household.
   * @return The history of food products.
   * @throws PermissionDeniedException If the user is not an admin.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws NullPointerException If the household id is null.
   */
  @GetMapping("/household/{householdId}")
  @Operation(
    summary = "Get the history of all food products for a household.",
    description = "Returns the history of all food products for a household of all time. If the user is not an admin, the householdId must be the id of the household the user is a member of.",
    tags = { "stats" }
  )
  public ResponseEntity<Collection<FoodProductHistoryDTO>> getFoodProductHistoryForHousehold(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID householdId
  )
    throws PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    if (!isAdminOrHouseholdMember(auth, householdId)) throw new PermissionDeniedException(
      "Du har ikke tilgang til å se statistikk for matvarer."
    );
    return ResponseEntity.ok(
      foodProductHistoryService
        .getAllFoodProductHistoryByHouseholdId(householdId)
        .stream()
        .map(fph -> FoodProductHistoryMapper.INSTANCE.foodProductHistoryToFoodProductHistoryDTO(fph)
        )
        .toList()
    );
  }

  /**
   * Method for getting the total waste for a household.
   * @param householdId The id of the household.
   * @return The total waste for a household.
   * @throws PermissionDeniedException If the user is not an admin or a member of the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws NullPointerException If the household id is null.
   */
  @GetMapping("/household/{householdId}/total")
  @Operation(
    summary = "Get the total waste for a household.",
    description = "Returns the total waste for a household of all time. If the user is not an admin, the householdId must be the id of the household the user is a member of.",
    tags = { "stats" }
  )
  public ResponseEntity<Double> getTotalWasteForHousehold(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID householdId
  )
    throws PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    if (!isAdminOrHouseholdMember(auth, householdId)) throw new PermissionDeniedException(
      "Du har ikke tilgang til å se statistikk for matvarer."
    );
    return ResponseEntity.ok(foodProductHistoryService.getTotalWaste(householdId));
  }

  /**
   * Method for getting the total waste for a household.
   * @param householdId The id of the household.
   * @param year The year to get the total waste for.
   * @return The total waste for a household.
   * @throws PermissionDeniedException If the user is not an admin or a member of the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws NullPointerException If the household id is null.
   */
  @GetMapping("/household/{householdId}/total/{year}")
  @Operation(
    summary = "Get the total waste for a household.",
    description = "Returns the total waste for a household for a year. If the user is not an admin, the householdId must be the id of the household the user is a member of.",
    tags = { "stats" }
  )
  public ResponseEntity<Double> getTotalWasteForHouseholdAndYear(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID householdId,
    @PathVariable @Min(2000) Integer year
  )
    throws PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    if (!isAdminOrHouseholdMember(auth, householdId)) throw new PermissionDeniedException(
      "Du har ikke tilgang til å se statistikk for matvarer."
    );
    return ResponseEntity.ok(
      foodProductHistoryService.getWasteByInPeriod(
        householdId,
        LocalDate.of(year, 1, 1),
        LocalDate.of(year, 12, 31)
      )
    );
  }

  /**
   * Method for getting the total waste for a household.
   * @param householdId The id of the household.
   * @param year The year to get the total waste for.
   * @param month The month to get the total waste for.
   * @return The total waste for a household.
   * @throws PermissionDeniedException If the user is not an admin or a member of the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws NullPointerException If the household id is null.
   */
  @GetMapping("/household/{householdId}/total/{year}/{month}")
  @Operation(
    summary = "Get the total waste for a household.",
    description = "Returns the total waste for a household for a month. If the user is not an admin, the householdId must be the id of the household the user is a member of.",
    tags = { "stats" }
  )
  public ResponseEntity<Double> getTotalWasteForHouseholdAndYearAndMonth(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID householdId,
    @PathVariable @Min(2000) Integer year,
    @PathVariable @Min(1) @Max(12) Integer month
  )
    throws PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    if (!isAdminOrHouseholdMember(auth, householdId)) throw new PermissionDeniedException(
      "Du har ikke tilgang til å se statistikk for matvarer."
    );
    return ResponseEntity.ok(
      foodProductHistoryService.getWasteByInPeriod(
        householdId,
        LocalDate.of(year, month, 1),
        LocalDate.of(year, month, 1).withDayOfMonth(LocalDate.of(year, month, 1).lengthOfMonth())
      )
    );
  }
}
