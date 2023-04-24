package edu.ntnu.idatt2106.smartmat.unit.recipe;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeDifficulty;
import java.util.HashSet;
import org.junit.Test;

public class RecipeTest {

  @Test
  public void testRecipeConstructor() {
    assertDoesNotThrow(() ->
      new Recipe(
        null,
        "name",
        "description",
        new HashSet<>(),
        "instructions",
        1,
        RecipeDifficulty.EASY
      )
    );
  }

  @Test
  public void testRecipeConstructorWithNulls() {
    assertThrows(
      NullPointerException.class,
      () -> new Recipe(null, null, null, null, null, 0, RecipeDifficulty.ADVANCED)
    );
  }

  @Test
  public void testRecipeSetters() {
    Recipe recipe = new Recipe();
    assertDoesNotThrow(() -> recipe.setName("name"));
    assertDoesNotThrow(() -> recipe.setDescription("description"));
    assertDoesNotThrow(() -> recipe.setIngredients(new HashSet<>()));
    assertDoesNotThrow(() -> recipe.setInstructions("instructions"));
    assertDoesNotThrow(() -> recipe.setEstimatedMinutes(1));
    assertDoesNotThrow(() -> recipe.setRecipeDifficulty(RecipeDifficulty.EASY));
  }

  @Test
  public void testSetNameWithNull() {
    Recipe recipe = new Recipe();
    assertThrows(NullPointerException.class, () -> recipe.setName(null));
  }

  @Test
  public void testSetDescriptionWithNull() {
    Recipe recipe = new Recipe();
    assertThrows(NullPointerException.class, () -> recipe.setDescription(null));
  }

  @Test
  public void testSetInstructionsWithNull() {
    Recipe recipe = new Recipe();
    assertThrows(NullPointerException.class, () -> recipe.setInstructions(null));
  }

  @Test
  public void testSetRecipeDifficultyWithNull() {
    Recipe recipe = new Recipe();
    assertThrows(NullPointerException.class, () -> recipe.setRecipeDifficulty(null));
  }
}
