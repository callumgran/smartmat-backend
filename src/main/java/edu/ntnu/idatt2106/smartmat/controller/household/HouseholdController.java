package edu.ntnu.idatt2106.smartmat.controller.household;

import edu.ntnu.idatt2106.smartmat.dto.household.CreateHouseholdDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdMemberDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.TmpIngredientUsedDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.UpdateHouseholdDTO;
import edu.ntnu.idatt2106.smartmat.dto.recipe.RecipeDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.PermissionDeniedException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.MemberAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.recipe.RecipeNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.mapper.household.HouseholdMapper;
import edu.ntnu.idatt2106.smartmat.mapper.household.HouseholdMemberMapper;
import edu.ntnu.idatt2106.smartmat.mapper.recipe.RecipeMapper;
import edu.ntnu.idatt2106.smartmat.mapper.shoppinglist.ShoppingListMapper;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipe;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipeId;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.household.WeeklyRecipeService;
import edu.ntnu.idatt2106.smartmat.service.recipe.HouseholdRecipeRecommend;
import edu.ntnu.idatt2106.smartmat.service.recipe.RecipeService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import edu.ntnu.idatt2106.smartmat.validation.user.AuthValidation;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for household endpoints.
 * Used for all household endpoints.
 * All endpoints are private and require authentication.
 * @author Callum G.
 * @version 1.4 - 24.04.2023
 */
@RestController
@RequestMapping(value = "/api/v1/private/households")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class HouseholdController {

  private static final Logger LOGGER = LoggerFactory.getLogger(HouseholdController.class);

  private final HouseholdService householdService;

  private final UserService userService;

  private final ShoppingListService shoppingListService;

  private final RecipeService recipeService;

  private final WeeklyRecipeService weeklyRecipeService;

  private boolean isAdminOrHouseholdOwner(Auth auth, UUID householdId)
    throws UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    return (
      AuthValidation.hasRole(auth, UserRole.ADMIN) ||
      householdService.isHouseholdOwner(householdId, auth.getUsername())
    );
  }

  /**
   * Checks if a authenticated user is a member of a household
   * or if the user is an admin.
   * @param auth The authentication of the user.
   * @param householdId The id of the household.
   * @return True if the user is a member of the household or an admin.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws NullPointerException if auth is null or if the household id is ull
   */
  private boolean isAdminOrHouseholdMember(Auth auth, UUID householdId)
    throws UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    return (
      AuthValidation.hasRole(auth, UserRole.ADMIN) ||
      householdService.isHouseholdMember(householdId, auth.getUsername())
    );
  }

  /**
   * Get a household by id.
   * @param id The id of the household.
   * @return 200 if the household was found and the household.
   * @throws UserDoesNotExistsException
   * @throws NullPointerException
   * @throws HouseholdDoesNotExistsException If the household does not exist.
   */
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Get a household by id",
    description = "Get a household by id, if the household does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<HouseholdDTO> getHousehold(@PathVariable String id)
    throws HouseholdNotFoundException {
    LOGGER.info("Getting household with id: {}", id);
    Household household = householdService.getHouseholdById(UUID.fromString(id));

    LOGGER.info("Got household with id: {}", id);
    HouseholdDTO householdDTO = HouseholdMapper.INSTANCE.householdToHouseholdDTO(household);

    LOGGER.info("Mapped household with id: {} to householdDTO", id);

    return ResponseEntity.ok(householdDTO);
  }

  /**
   * Create a household.
   * @param householdDTO The household to create.
   * @return 201 if the household was created.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Create a household",
    description = "Create a household, if the user does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<HouseholdDTO> createHousehold(
    @AuthenticationPrincipal Auth auth,
    @RequestBody CreateHouseholdDTO householdDTO
  )
    throws UserDoesNotExistsException, HouseholdAlreadyExistsException, ShoppingListAlreadyExistsException {
    LOGGER.info("Creating household with name: {}", householdDTO.getName());
    Household household = Household.builder().name(householdDTO.getName()).build();

    User user = userService.getUserByUsername(auth.getUsername());

    HouseholdMember userHousehold = HouseholdMember
      .builder()
      .household(household)
      .user(user)
      .householdRole(HouseholdRole.OWNER)
      .build();
    household.setMembers(Set.of(userHousehold));

    ShoppingList shoppingList = ShoppingList.builder().household(household).build();
    household.setShoppingLists(Set.of(shoppingList));

    LOGGER.info("Added owner to household with username: {}", auth.getUsername());

    household = householdService.saveHousehold(household);

    LOGGER.info("Created household with name: {}", householdDTO.getName());
    HouseholdDTO householdDTORet = HouseholdMapper.INSTANCE.householdToHouseholdDTO(household);

    LOGGER.info("Mapped household with name: {} to householdDTO", householdDTO.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(householdDTORet);
  }

  /**
   * Method to update a households name.
   * @param householdId The id of the household.
   * @param updateDTO The new name of the household.
   * @return 200 OK if the household was updated and the updated household.
   * @throws NullPointerException If the household does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Update a households name",
    description = "Update a households name, if the household does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<HouseholdDTO> updateHouseholdName(
    @AuthenticationPrincipal Auth auth,
    @RequestBody UpdateHouseholdDTO updateDTO
  )
    throws NullPointerException, PermissionDeniedException, HouseholdNotFoundException, UserDoesNotExistsException {
    boolean ownerOrAdmin = isAdminOrHouseholdOwner(auth, UUID.fromString(updateDTO.getId()));

    if (!ownerOrAdmin) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å endre navnet på denne husholdningen."
      );
    }

    LOGGER.info("Updating name of household with id: {}", updateDTO.getId());

    Household updated = householdService.updateHouseholdName(
      UUID.fromString(updateDTO.getId()),
      updateDTO.getName()
    );

    LOGGER.info("Updated name of household with id: {}", updateDTO.getId());
    HouseholdDTO retDto = HouseholdMapper.INSTANCE.householdToHouseholdDTO(updated);

    return ResponseEntity.ok(retDto);
  }

  /**
   * Method to delete a household.
   * @param householdId The id of the household.
   * @return 204 No Content.
   * @throws NullPointerException If the household does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Delete a household",
    description = "Delete a household, if the household does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<String> deleteHousehold(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id
  )
    throws NullPointerException, PermissionDeniedException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!isAdminOrHouseholdOwner(auth, id)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å slette denne husholdningen."
      );
    }
    LOGGER.info("Deleting household with id: {}", id);

    householdService.deleteHouseholdById(id);

    LOGGER.info("Deleted household with id: {}", id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Method to get all households for a user.
   * @param username The username of the user.
   * @param auth The authentication object.
   * @return 200 OK if the user was found and the households.
   * @throws NullPointerException If any value are null.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @GetMapping(value = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Get all households for a user",
    description = "Get all households for a user, if the user does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<List<HouseholdDTO>> getHouseholdsForUser(
    @PathVariable String username,
    @AuthenticationPrincipal Auth auth
  ) throws PermissionDeniedException, NullPointerException, UserDoesNotExistsException {
    if (
      !AuthValidation.hasRoleOrIsUser(auth, UserRole.ADMIN, username)
    ) throw new PermissionDeniedException("Brukeren har ikke tilgang til å se denne brukeren.");

    LOGGER.info("Getting households for user with username: {}", username);
    List<HouseholdDTO> householdDTOS = householdService
      .getHouseholdsByUser(username)
      .stream()
      .map(HouseholdMapper.INSTANCE::householdToHouseholdDTO)
      .collect(Collectors.toList());

    LOGGER.info("Mapped households for user with username: {} to householdDTOs", username);
    return ResponseEntity.ok(householdDTOS);
  }

  /**
   * Method to add a user to a household.
   * @param householdId The id of the household.
   * @param username The username of the user to add.
   * @return 200 OK if the user was added to the household.
   * @throws NullPointerException If any value are null.
   * @throws PermissionDeniedException If the user does not have permission to add a user to the household.
   * @throws MemberAlreadyExistsException If the user is already a member of the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   */
  @PostMapping(value = "/{id}/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Add a user to a household",
    description = "Add a user to a household, if the user or household does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<HouseholdMemberDTO> addUserToHousehold(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id,
    @PathVariable String username
  )
    throws NullPointerException, PermissionDeniedException, MemberAlreadyExistsException, UserDoesNotExistsException, HouseholdNotFoundException {
    if (!isAdminOrHouseholdOwner(auth, id)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å legge til brukere i denne husholdningen."
      );
    }
    if (householdService.isHouseholdMember(id, username)) throw new MemberAlreadyExistsException();

    LOGGER.info("Adding user with username: {} to household with id: {}", username, id);
    HouseholdMember householdMember = householdService.addHouseholdMember(
      id,
      username,
      HouseholdRole.MEMBER
    );

    LOGGER.info("Added user with username: {} to household with id: {}", username, id);
    HouseholdMemberDTO householdMemberDTORet = HouseholdMemberMapper.INSTANCE.householdMemberToHouseholdMemberDTO(
      householdMember
    );

    LOGGER.info("Mapped householdMember to householdMemberDTO: {}", householdMemberDTORet);
    return ResponseEntity.ok(householdMemberDTORet);
  }

  /**
   * Method to remove a user from a household.
   * @param householdId The id of the household.
   * @param username The username of the user to remove.
   * @return 204 No Content.
   * @throws NullPointerException If any value are null.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   */
  @DeleteMapping(value = "/{id}/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Remove a user from a household",
    description = "Remove a user from a household, if the user or household does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<String> removeUserFromHousehold(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id,
    @PathVariable String username
  )
    throws NullPointerException, PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException {
    if (!isAdminOrHouseholdOwner(auth, id)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å fjerne brukere fra denne husholdningen."
      );
    }
    if (!householdService.isHouseholdMember(id, username)) {
      throw new UserDoesNotExistsException("Brukeren er ikke medlem av denne husholdningen.");
    }
    if (householdService.isHouseholdOwner(id, username)) {
      throw new PermissionDeniedException(
        "Brukeren er eier av denne husholdningen og kan ikke fjernes."
      );
    }

    LOGGER.info("Removing user with username: {} from household with id: {}", username, id);
    householdService.deleteHouseholdMember(id, username);

    LOGGER.info("Removed user with username: {} from household with id: {}", username, id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Method to update the role of a user in a household.
   * @param householdId The id of the household.
   * @param username The username of the user to update.
   * @param householdRole The new role of the user.
   * @return 200 OK if the user was updated.
   * @throws NullPointerException If any value are null.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   */
  @PutMapping(value = "/{id}/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Update the role of a user in a household",
    description = "Update the role of a user in a household, if the user or household does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<HouseholdMemberDTO> updateUserInHousehold(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id,
    @PathVariable String username,
    @RequestBody HouseholdRole householdRole
  )
    throws NullPointerException, PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException {
    if (!isAdminOrHouseholdOwner(auth, id)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å oppdatere brukere i denne husholdningen."
      );
    }

    LOGGER.info("Updating user with username: {} in household with id: {}", username, id);
    HouseholdMember householdMember = householdService.updateHouseholdMember(
      id,
      username,
      householdRole
    );

    LOGGER.info("Updated user with username: {} in household with id: {}", username, id);
    HouseholdMemberDTO householdMemberDTORet = HouseholdMemberMapper.INSTANCE.householdMemberToHouseholdMemberDTO(
      householdMember
    );

    LOGGER.info("Mapped householdMember to householdMemberDTO: {}", householdMemberDTORet);
    return ResponseEntity.ok(householdMemberDTORet);
  }

  /**
   * Gets the current shopping list for a household.
   * A shopping list is current if it has not completion date.
   * The first current shopping list that is found is returned.
   * @param auth The authentication of the user.
   * @param id The id of the household.
   * @return The current shopping list
   * @throws NullPointerException If any values are null.
   * @throws PermissionDeniedException If the user does not have access to the household.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @GetMapping(value = "/{id}/current", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Get the current shopping list by a household",
    description = "Get a household by id and find its current shopping list. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<ShoppingListDTO> getCurrentShoppingList(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id
  )
    throws NullPointerException, PermissionDeniedException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!isAdminOrHouseholdMember(auth, id)) {
      throw new PermissionDeniedException(
        "Du har ikke tilgang til å hente handlelisten til denne husholdningen."
      );
    }

    ShoppingList shoppingList;
    try {
      shoppingList = shoppingListService.getCurrentShoppingList(id);
      LOGGER.info("Found shopping list: {}", shoppingList);
    } catch (ShoppingListNotFoundException e) {
      LOGGER.error("No shopping list found for household with id: {}", id);
      return ResponseEntity.notFound().build();
    }

    ShoppingListDTO shoppingListDTO = ShoppingListMapper.INSTANCE.shoppingListToDTO(shoppingList);

    return ResponseEntity.ok(shoppingListDTO);
  }

  /**
   * Method to get recommended recipes for a household.
   * @param auth The authentication of the user.
   * @param id The id of the household.
   * @return 200 OK if the recipes were found.
   * @throws NullPointerException If any values are null.
   * @throws PermissionDeniedException If the user does not have access to the household.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @GetMapping(value = "/{id}/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Get recommended recipes for a household",
    description = "Get recommended recipes for a household. Recipes are recommended based on the household's ingredients. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<List<RecipeDTO>> getRecommendedRecipes(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id
  )
    throws NullPointerException, PermissionDeniedException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!isAdminOrHouseholdMember(auth, id)) {
      throw new PermissionDeniedException(
        "Du har ikke tilgang til å hente anbefalte oppskrifter for denne husholdningen."
      );
    }

    Collection<Recipe> recipes = HouseholdRecipeRecommend.getRecommendedRecipes(
      householdService.getHouseholdById(id),
      recipeService.findAllRecipes(),
      weeklyRecipeService.getRecipesForHousehold(id)
    );

    LOGGER.info("Found {} recommended recipes for household with id: {}", recipes.size(), id);

    List<RecipeDTO> recipeDTOS = recipes
      .stream()
      .map(RecipeMapper.INSTANCE::recipeToRecipeDTO)
      .collect(Collectors.toList());

    return ResponseEntity.ok(recipeDTOS);
  }

  /**
   * Method to add a weekly recipe to a household.
   * @param auth The authentication of the user.
   * @param id The id of the household.
   * @param recipeId The id of the recipe.
   * @return 201 CREATED if the recipe was added.
   * @throws NullPointerException If any values are null.
   * @throws PermissionDeniedException If the user does not have access to the household.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws RecipeNotFoundException If the recipe does not exist.
   */
  @PostMapping(value = "/{id}/recipes/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Add a weekly recipe to a household",
    description = "Add a weekly recipe to a household. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<Void> addWeeklyRecipe(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id,
    @PathVariable UUID recipeId,
    @RequestBody TmpIngredientUsedDTO tmpIngredientUsedDTO
  )
    throws NullPointerException, PermissionDeniedException, HouseholdNotFoundException, UserDoesNotExistsException, RecipeNotFoundException {
    if (!isAdminOrHouseholdMember(auth, id)) {
      throw new PermissionDeniedException(
        "Du har ikke tilgang til å legge til en ukentlig oppskrift for denne husholdningen."
      );
    }

    Recipe recipe = recipeService.findRecipeById(recipeId);
    LOGGER.info("Found recipe with id: {}", recipeId);

    Household household = householdService.getHouseholdById(id);
    LOGGER.info("Found household with id: {}", id);

    final int householdSize = household.getMembers().size();

    recipe.getIngredients().forEach(i -> i.setAmount(i.getAmount() * householdSize));

    final WeeklyRecipe tempUsedDay = new WeeklyRecipe(
      household,
      tmpIngredientUsedDTO.getDate(),
      recipe
    );

    weeklyRecipeService.saveWeeklyRecipe(tempUsedDay);

    LOGGER.info("Added weekly recipe with id: {} to household with id: {}", recipeId, id);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Method to delete temporary used ingredients for a household on a specific date.
   * @param auth The authentication of the user.
   * @param id The id of the household.
   * @param date The date of the temporary used ingredients.
   * @return 204 NO CONTENT if the temporary used ingredients were deleted.
   * @throws NullPointerException If any values are null.
   * @throws PermissionDeniedException If the user does not have access to the household.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @DeleteMapping(value = "/{id}/tempused/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Delete temporary used ingredients for a household on a specific date",
    description = "Delete temporary used ingredients for a household on a specific date. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<Void> deleteWeeklyRecipe(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id,
    @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
  )
    throws NullPointerException, PermissionDeniedException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!isAdminOrHouseholdMember(auth, id)) {
      throw new PermissionDeniedException(
        "Du har ikke tilgang til å slette midlertidig brukte ingredienser for denne husholdningen."
      );
    }

    weeklyRecipeService.deleteWeeklyRecipeById(
      new WeeklyRecipeId(householdService.getHouseholdById(id), date)
    );

    LOGGER.info(
      "Deleted temporary used ingredients for household with id: {} on date: {}",
      id,
      date
    );

    return ResponseEntity.noContent().build();
  }
}
