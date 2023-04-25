package edu.ntnu.idatt2106.smartmat.service.ingredient;

import edu.ntnu.idatt2106.smartmat.exceptions.DatabaseException;
import edu.ntnu.idatt2106.smartmat.exceptions.ingredient.IngredientNotFoundException;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.filtering.SearchSpecification;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.repository.ingredient.IngredientRepository;
import java.util.Collection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Implementation of the ingredient service.
 * This class is responsible for all business logic related to ingredients.
 * @author Tobias. O
 * @version 1.0 - 19.04.2023
 */
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

  @Autowired
  IngredientRepository ingredientRepository;

  /**
   * Method to check if an ingredient exists.
   * @param id long ID of the ingredient.
   * @return true if the ingredient exits, false otherwise.
   * @throws NullPointerException if the ID is null.
   */
  @Override
  public boolean ingredientExists(long id) throws NullPointerException {
    return ingredientRepository.existsById(id);
  }

  /**
   * Method to get an ingredient by its ID.
   * @param id long ID of the ingredient.
   * @return The ingredient with the ID.
   * @throws NullPointerException if the ID is null.
   */
  @Override
  public Ingredient getIngredientById(long id)
    throws IngredientNotFoundException, NullPointerException {
    return ingredientRepository.findById(id).orElseThrow(IngredientNotFoundException::new);
  }

  /**
   * Method to get ingredients by name.
   * @param name name of the ingredient.
   * @return A list of ingredients with the name.
   * @throws NullPointerException If the name does not exist.
   */
  @Override
  public Collection<Ingredient> getIngredientsByName(@NonNull String name)
    throws NullPointerException {
    return ingredientRepository.findByName(name).orElseThrow(NullPointerException::new);
  }

  /**
   * Method to save an ingredient.
   * @param ingredient ingredient to save.
   * @return the saved ingredient.
   * @throws NullPointerException if the ingredient is not found.
   */

  @Override
  public Ingredient saveingredient(@NonNull Ingredient ingredient) throws NullPointerException {
    return ingredientRepository.save(ingredient);
  }

  /**
   * Method to delete an ingredient.
   * @param ingredient ingredient to delete.
   * @throws NullPointerException if the ingredient is not found.
   */

  @Override
  public void deleteIngredient(@NonNull Ingredient ingredient)
    throws IngredientNotFoundException, NullPointerException {
    if (!ingredientExists(ingredient.getId())) throw new IngredientNotFoundException();
    ingredientRepository.delete(ingredient);
  }

  /**
   * Method to delete an ingredient by its ID.
   * @param id the ID of the ingredient.
   * @throws NullPointerException if the ID is not found.
   */
  @Override
  public void deleteIngredientById(long id)
    throws IngredientNotFoundException, NullPointerException {
    if (!ingredientExists(id)) throw new IngredientNotFoundException();
    ingredientRepository.deleteById(id);
  }

  /**
   * Method to get all ingredients from the database.
   * @return a list of all ingredients.
   * @throws DatabaseException if an error occurred in the database.
   */
  @Override
  public Collection<Ingredient> getAllIngredients() throws DatabaseException {
    Collection<Ingredient> ingredients = ingredientRepository.findAll();

    if (ingredients.isEmpty()) throw new DatabaseException(
      "Ingen ingredienser ble funnet i databasen!"
    );

    return ingredients;
  }

  /**
   * Method to get ingredients by search request.
   * @param searchRequest the search request. With sort, pagesize, page and filters.
   * @return a page of ingredients.
   */
  @Override
  public Page<Ingredient> getIngredientsBySearch(SearchRequest searchRequest)
    throws NullPointerException {
    return ingredientRepository.findAll(
      new SearchSpecification<Ingredient>(searchRequest),
      SearchSpecification.getPageable(searchRequest)
    );
  }
}
