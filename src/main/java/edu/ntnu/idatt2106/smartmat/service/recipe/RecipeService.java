package edu.ntnu.idatt2106.smartmat.service.recipe;

import edu.ntnu.idatt2106.smartmat.exceptions.recipe.RecipeAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.recipe.RecipeNotFoundException;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import java.util.Collection;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Interface for the recipe service.
 * @author Simen G. & Callum G.
 * @version 1.3 - 20.4.2023
 */
@Service
public interface RecipeService {
  boolean existsById(@NonNull UUID id) throws NullPointerException;

  Collection<Recipe> findAllRecipes();

  Recipe findRecipeById(@NonNull UUID id) throws RecipeNotFoundException, NullPointerException;

  Recipe saveRecipe(@NonNull Recipe recipe)
    throws RecipeAlreadyExistsException, NullPointerException;

  Recipe updateRecipe(@NonNull UUID id, @NonNull Recipe recipe)
    throws RecipeNotFoundException, NullPointerException;

  void deleteRecipeById(@NonNull UUID id) throws RecipeNotFoundException, NullPointerException;

  Collection<Recipe> findRecipesByName(@NonNull String name) throws NullPointerException;

  Page<Recipe> searchRecipes(@NonNull SearchRequest searchRequest) throws NullPointerException;
}
