package edu.ntnu.idatt2106.smartmat.service.recipe;

import edu.ntnu.idatt2106.smartmat.exceptions.recipe.RecipeAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.recipe.RecipeNotFoundException;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.filtering.SearchSpecification;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.repository.recipe.RecipeIngredientRepository;
import edu.ntnu.idatt2106.smartmat.repository.recipe.RecipeRepository;
import edu.ntnu.idatt2106.smartmat.utils.UnitUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Implementation of the recipe service.
 * This class is responsible for all logic related to recipes.
 * @author Simen G. Callum G.
 * @version 1.2 - 25.04.2023
 */
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

  @Autowired
  private RecipeRepository recipeRepository;

  @Autowired
  private RecipeIngredientRepository recipeIngredientRepository;

  /**
   * Method to check if a recipe with the specified id exists in the repository.
   * @param id the id of the recipe to check.
   * @return true if the recipe exists, false otherwise.
   */
  @Override
  public boolean existsById(@NonNull UUID id) throws NullPointerException {
    return recipeRepository.existsById(id);
  }

  /**
   * Method to retrieve all recipes from the repository.
   * @return a collection of all recipes.
   */
  @Override
  public Collection<Recipe> findAllRecipes() {
    return recipeRepository.findAll();
  }

  /**
   * Method to search for recipes with a name containing the specified string.
   * @param name the string to search for in recipe names.
   * @return a collection of recipes with names containing the specified string.
   */
  @Override
  public Collection<Recipe> findRecipesByName(@NonNull String name) throws NullPointerException {
    return recipeRepository
      .findByNameContainingIgnoreCase(name)
      .orElseThrow(NullPointerException::new);
  }

  /**
   * Method to retrieve a recipe by its id.
   * @param id the id of the recipe to find.
   * @return the recipe with the specified id, or null if not found.
   */
  @Override
  public Recipe findRecipeById(@NonNull UUID id)
    throws RecipeNotFoundException, NullPointerException {
    return recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
  }

  /**
   * Method to save a recipe to the repository.
   * @param recipe the recipe to save.
   * @return the saved recipe.
   */
  @Override
  public Recipe saveRecipe(@NonNull Recipe recipe)
    throws RecipeAlreadyExistsException, NullPointerException {
    if (
      recipe.getId() != null && existsById(recipe.getId())
    ) throw new RecipeAlreadyExistsException();

    Recipe retRecipe = recipeRepository.save(recipe);

    recipe
      .getIngredients()
      .forEach(recipeIngredient -> {
        if (recipeIngredient.getIngredient().getRecipes() == null) recipeIngredient
          .getIngredient()
          .setRecipes(new HashSet<>());
        recipeIngredient.getIngredient().getRecipes().add(recipeIngredient);
        recipeIngredient.setRecipe(recipe);
        recipeIngredientRepository.save(recipeIngredient);
      });

    return retRecipe;
  }

  /**
   * Method to update a recipe with the specified id in the repository.
   * @param id the id of the recipe to update.
   * @param recipe the updated recipe.
   * @return the updated recipe, or null if not found.
   */
  @Override
  public Recipe updateRecipe(@NonNull UUID id, @NonNull Recipe recipe)
    throws RecipeNotFoundException, NullPointerException {
    if (!existsById(id)) throw new RecipeNotFoundException();

    recipe.setId(id);
    return recipeRepository.save(recipe);
  }

  /**
   * Method to delete a recipe with the specified id from the repository.
   * @param id the id of the recipe to delete.
   */
  @Override
  public void deleteRecipeById(@NonNull UUID id)
    throws RecipeNotFoundException, NullPointerException {
    if (!existsById(id)) throw new RecipeNotFoundException();

    recipeIngredientRepository.deleteAllByRecipeId(id);
    recipeRepository.deleteById(id);
  }

  /**
   * Method to search for recipes based on the specified search request.
   * @param searchRequest the search request to use.
   * @return a page of recipes matching the search request.
   * @throws NullPointerException if the search request is null.
   */
  @Override
  public Page<Recipe> searchRecipes(@NonNull SearchRequest searchRequest)
    throws NullPointerException {
    return recipeRepository.findAll(
      new SearchSpecification<Recipe>(searchRequest),
      SearchSpecification.getPageable(searchRequest)
    );
  }

  /**
   * Method to get the shopping list items for a recipe.
   * @param recipeId the id of the recipe to get the shopping list items for.
   * @param household the household to get the shopping list items for.
   * @param portions the number of portions to get the shopping list items for.
   * @return a collection of shopping list items for the recipe.
   * @throws RecipeNotFoundException if the recipe with the specified id does not exist.
   * @throws NullPointerException if the recipe id or household is null.
   */
  @Override
  public Collection<ShoppingListItem> getShoppingListItems(
    @NonNull UUID recipeId,
    @NonNull Household household,
    int portions
  ) throws NullPointerException, RecipeNotFoundException {
    if (!existsById(recipeId)) {
      throw new RecipeNotFoundException();
    }

    Recipe recipe = findRecipeById(recipeId);

    final Collection<HouseholdFoodProduct> householdFoodProducts = household
      .getFoodProducts()
      .stream()
      .collect(Collectors.toUnmodifiableList());

    final Collection<RecipeIngredient> usedIngredients = recipe
      .getIngredients()
      .stream()
      .map(ri -> {
        ri.setAmount(ri.getAmount() * portions);
        return ri;
      })
      .toList();

    return usedIngredients
      .stream()
      .filter(ri -> {
        householdFoodProducts.forEach(hfp -> {
          if (
            hfp.getFoodProduct().getIngredient() != null &&
            hfp.getFoodProduct().getIngredient().getId() == ri.getIngredient().getId()
          ) {
            if (hfp.getAmountLeft() > 0) {
              double amount = UnitUtils.removeRecipeIngredientAmountFromHouseholdFoodProductAmount(
                ri,
                hfp
              );

              if (amount > 0) {
                ri.setAmount(0.0D);
              } else {
                ri.setAmount(UnitUtils.getOriginalUnit((-1.0 * amount), ri));
              }
            }
          }
        });

        return ri.getAmount() > 0;
      })
      .map(ri -> {
        return ShoppingListItem
          .builder()
          .ingredient(ri.getIngredient())
          .amount(UnitUtils.getIngredientAmount(ri))
          .checked(false)
          .build();
      })
      .toList();
  }
}
