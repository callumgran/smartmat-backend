package edu.ntnu.idatt2106.smartmat.repository.recipe;

import static org.junit.Assert.assertEquals;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeDifficulty;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import java.util.HashSet;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test class for the recipe repository.
 * @author Callum G.
 * @version 1.0 - 21.04.23
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class RecipeRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private RecipeRepository recipeRepository;

  @Test
  public void testFindByNameContainingIgnoreCase() {
    Ingredient ingredient = new Ingredient(
      null,
      "Carrot",
      new HashSet<>(),
      new HashSet<>(),
      null,
      null
    );
    Recipe recipe = new Recipe(
      null,
      "Carrot Cake",
      "A delicious carrot cake",
      new HashSet<>(),
      "1: do this, 2...",
      30,
      RecipeDifficulty.EASY
    );
    RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, ingredient, 3D);
    recipe.getIngredients().add(recipeIngredient);
    ingredient.getRecipes().add(recipeIngredient);

    entityManager.persist(ingredient);
    entityManager.persist(recipe);
    entityManager.persist(recipeIngredient);
    entityManager.flush();

    List<Recipe> recipes = recipeRepository
      .findByNameContainingIgnoreCase("carrot")
      .get()
      .stream()
      .toList();
    assertEquals(recipes.size(), 1);
    assertEquals(recipes.get(0).getName(), "Carrot Cake");
    assertEquals(recipes.get(0).getIngredients().size(), 1);
  }
}
