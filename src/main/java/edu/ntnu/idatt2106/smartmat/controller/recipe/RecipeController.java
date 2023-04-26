package edu.ntnu.idatt2106.smartmat.controller.recipe;

import edu.ntnu.idatt2106.smartmat.dto.recipe.RecipeCreateDTO;
import edu.ntnu.idatt2106.smartmat.dto.recipe.RecipeDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.PermissionDeniedException;
import edu.ntnu.idatt2106.smartmat.exceptions.ingredient.IngredientNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.recipe.RecipeAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.recipe.RecipeNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.validation.BadInputException;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.mapper.recipe.RecipeMapper;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import edu.ntnu.idatt2106.smartmat.service.recipe.RecipeService;
import edu.ntnu.idatt2106.smartmat.validation.search.SearchRequestValidation;
import edu.ntnu.idatt2106.smartmat.validation.user.AuthValidation;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.zalando.fauxpas.FauxPas;
import org.zalando.fauxpas.ThrowingFunction;

/**
 * Controller for recipe.
 *
 * @author Simen J. G, Nicolai H. Brand., Carl G.
 * @version 1.2 26.03.2020
 */
@RestController
@RequestMapping(value = "/api/v1/private/recipes")
@RequiredArgsConstructor
public class RecipeController {

  private final RecipeService recipeService;
  private final IngredientService ingredientService;
  private static final Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);

  /**
   * Method to get all recipes.
   * @return A collection of recipes.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all recipes", description = "Get all recipes.", tags = { "recipe" })
  public ResponseEntity<Collection<RecipeDTO>> getAllRecipes() {
    LOGGER.info("GET request for all recipes");
    Collection<Recipe> recipes = recipeService.findAllRecipes();
    Collection<RecipeDTO> recipeDTOs = recipes
      .stream()
      .map(RecipeMapper.INSTANCE::recipeToRecipeDTO)
      .collect(Collectors.toList());

    LOGGER.info("Mapped recipes to recipeDTOs: {}", recipeDTOs);

    return ResponseEntity.ok(recipeDTOs);
  }

  /**
   * Method for searching recipes.
   * @return A collection of recipes.
   */
  @GetMapping(value = "/search/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Search for recipes",
    description = "Search for recipes by name.",
    tags = { "recipe" }
  )
  public ResponseEntity<Collection<RecipeDTO>> findRecipesByName(@PathVariable String name) {
    LOGGER.info("GET request for recipes by name: {}", name);
    Collection<Recipe> recipes = recipeService.findRecipesByName(name);
    Collection<RecipeDTO> recipeDTOs = recipes
      .stream()
      .map(RecipeMapper.INSTANCE::recipeToRecipeDTO)
      .collect(Collectors.toList());

    LOGGER.info("Mapped recipes to recipeDTOs: {}", recipeDTOs);

    return ResponseEntity.ok(recipeDTOs);
  }

  /**
   * Method for getting a recipe by id.
   * @param id The id of the recipe.
   * @return A collection of recipes.
   * @throws RecipeNotFoundException If the recipe is not found.
   */
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get recipe", description = "Get recipes by id.", tags = { "recipe" })
  public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable String id)
    throws RecipeNotFoundException {
    LOGGER.info("GET request for recipe by id: {}", id);
    Recipe recipe = recipeService.findRecipeById(UUID.fromString(id));

    if (recipe == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    RecipeDTO recipeDTO = RecipeMapper.INSTANCE.recipeToRecipeDTO(recipe);

    LOGGER.info("Mapped recipe to recipeDTO: {}", recipeDTO);

    return ResponseEntity.ok(recipeDTO);
  }

  /**
   * Creates a recipe.
   * @param recipeDTO The recipe to create.
   * @return The created recipe.
   * @throws PermissionDeniedException If the user does not have permission to create a recipe.
   * @throws RecipeAlreadyExistsException If the recipe already exists.
   */
  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Create a recipe",
    description = "Create a recipe. Requires valid JWT token in header.",
    tags = { "recipe" }
  )
  public ResponseEntity<RecipeDTO> createRecipe(
    @RequestBody RecipeCreateDTO recipeDTO,
    @AuthenticationPrincipal Auth auth
  ) throws PermissionDeniedException, RecipeAlreadyExistsException, IngredientNotFoundException {
    if (!AuthValidation.hasRole(auth, UserRole.ADMIN)) throw new PermissionDeniedException(
      "Du har ikke tilgang til å opprette oppskrifter"
    );

    LOGGER.info("POST request for recipe: {}", recipeDTO);
    final Recipe recipe = RecipeMapper.INSTANCE.recipeCreateDTOToRecipe(recipeDTO);

    Set<RecipeIngredient> ingredients = recipeDTO
      .getIngredients()
      .stream()
      .map(
        FauxPas.throwingFunction(i ->
          new RecipeIngredient(
            recipe,
            ingredientService.getIngredientById(i.getIngredient()),
            i.getAmount()
          )
        )
      )
      .collect(Collectors.toSet());

    LOGGER.info("Using ingredients in recipe: {}", ingredients);
    recipe.setIngredients(ingredients);

    LOGGER.info("Mapped recipeDTO to recipe: {}", recipe);
    Recipe savedRecipe = recipeService.saveRecipe(recipe);

    RecipeDTO createdRecipeDTO = RecipeMapper.INSTANCE.recipeToRecipeDTO(savedRecipe);

    LOGGER.info("Created recipe: {}", createdRecipeDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipeDTO);
  }

  /**
   * Updates a recipe with the given id.
   * @param id The id of the recipe to update.
   * @param recipeDTO The recipe to update.
   * @return The updated recipe.
   * @throws PermissionDeniedException If the user does not have permission to update a recipe.
   * @throws RecipeNotFoundException If the recipe does not exist.
   * @throws NullPointerException If any of the parameters are null.
   */
  @PutMapping(
    value = "/{id}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Update a recipe",
    description = "Update a recipe with the given id." +
    " The id in the path must match the id in the request body.",
    tags = { "recipe" }
  )
  public ResponseEntity<RecipeDTO> updateRecipe(
    @PathVariable String id,
    @RequestBody RecipeCreateDTO recipeDTO,
    @AuthenticationPrincipal Auth auth
  ) throws PermissionDeniedException, RecipeNotFoundException, NullPointerException {
    if (!AuthValidation.hasRole(auth, UserRole.ADMIN)) throw new PermissionDeniedException(
      "Du har ikke tilgang til å oppdatere oppskrifter"
    );

    LOGGER.info("PUT request for recipe id: {} with data: {}", id, recipeDTO);
    final Recipe recipe = RecipeMapper.INSTANCE.recipeCreateDTOToRecipe(recipeDTO);

    ThrowingFunction<Long, Ingredient, IngredientNotFoundException> getIngredient = FauxPas.throwingFunction(i ->
      ingredientService.getIngredientById(i)
    );
    Set<RecipeIngredient> ingredients = recipeDTO
      .getIngredients()
      .stream()
      .map(i -> new RecipeIngredient(recipe, getIngredient.apply(i.getIngredient()), i.getAmount()))
      .collect(Collectors.toSet());
    LOGGER.info("Using ingredients in recipe: {}", ingredients);
    recipe.setIngredients(ingredients);

    Recipe savedRecipe = recipeService.updateRecipe(UUID.fromString(id), recipe);

    RecipeDTO updatedRecipeDTO = RecipeMapper.INSTANCE.recipeToRecipeDTO(savedRecipe);

    LOGGER.info("Updated recipe: {}", updatedRecipeDTO);

    return ResponseEntity.ok(updatedRecipeDTO);
  }

  /**
   * Deletes a recipe with the given id.
   * @param id The id of the recipe to delete.
   * @return A response entity with status code 204.
   * @throws PermissionDeniedException If the user does not have permission to delete a recipe.
   * @throws RecipeNotFoundException If the recipe does not exist.
   */
  @DeleteMapping(value = "/{id}")
  @Operation(
    summary = "Delete a recipe",
    description = "Delete a recipe with the given id.",
    tags = { "recipe" }
  )
  public ResponseEntity<Void> deleteRecipe(
    @PathVariable String id,
    @AuthenticationPrincipal Auth auth
  ) throws PermissionDeniedException, RecipeNotFoundException {
    if (!AuthValidation.hasRole(auth, UserRole.ADMIN)) throw new PermissionDeniedException(
      "Du har ikke tilgang til å slette oppskrifter"
    );
    LOGGER.info("DELETE request for recipe id: {}", id);
    recipeService.deleteRecipeById(UUID.fromString(id));

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * Method for searching recipes.
   * @param searchRequest The search request.
   * @return A collection of recipes.
   */
  @PostMapping(
    value = "/search",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Search recipes",
    description = "Search recipes by name, ingredients and tags, and return a collection of recipes. Returns an empty collection if no recipes are found. Requires valid JWT token in header.",
    tags = { "recipe" }
  )
  public ResponseEntity<Collection<RecipeDTO>> searchRecipes(
    @RequestBody SearchRequest searchRequest
  ) throws BadInputException, NullPointerException {
    if (!SearchRequestValidation.validateSearchRequest(searchRequest)) throw new BadInputException(
      "Ugyldig søkeforespørsel"
    );

    LOGGER.info("POST request for recipes by search request: {}", searchRequest);

    List<RecipeDTO> recipeDTOs = recipeService
      .searchRecipes(searchRequest)
      .stream()
      .map(RecipeMapper.INSTANCE::recipeToRecipeDTO)
      .collect(Collectors.toList());

    LOGGER.info("Mapped recipes to recipeDTOs: {}", recipeDTOs);

    return ResponseEntity.ok(recipeDTOs);
  }
}
