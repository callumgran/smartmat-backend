package edu.ntnu.idatt2106.smartmat.service.ingredient;

import edu.ntnu.idatt2106.smartmat.exceptions.DatabaseException;
import edu.ntnu.idatt2106.smartmat.exceptions.ingredient.IngredientNotFoundException;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Interface for the ingredient service.
 * @author Tobias. O
 * @version 1.0 - 19.04.2023
 */
@Service
public interface IngredientService {
  boolean ingredientExists(@NonNull long id) throws NullPointerException;

  Ingredient getIngredientById(@NonNull long id) throws IngredientNotFoundException;

  Collection<Ingredient> getIngredientsByName(@NonNull String name) throws NullPointerException;

  Ingredient saveingredient(@NonNull Ingredient ingredient);

  void deleteIngredient(@NonNull Ingredient ingredient) throws IngredientNotFoundException;

  void deleteIngredientById(@NonNull long id) throws IngredientNotFoundException;

  public Collection<Ingredient> getAllIngredients() throws DatabaseException;

  Page<Ingredient> getIngredientsBySearch(@NonNull SearchRequest searchRequest)
    throws NullPointerException;
}
