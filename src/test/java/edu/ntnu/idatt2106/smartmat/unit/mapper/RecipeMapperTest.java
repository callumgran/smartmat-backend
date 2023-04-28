package edu.ntnu.idatt2106.smartmat.unit.mapper;

import static org.junit.Assert.assertEquals;

import edu.ntnu.idatt2106.smartmat.dto.recipe.RecipeDTO;
import edu.ntnu.idatt2106.smartmat.mapper.recipe.RecipeMapper;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeDifficulty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class RecipeMapperTest {

  private final UUID UUID_1 = UUID.randomUUID();

  private final Recipe recipe = new Recipe(
    UUID_1,
    "name",
    "description",
    new HashSet<>(),
    "instructions",
    1,
    RecipeDifficulty.EASY,
    new HashSet<>()
  );
  private final RecipeDTO recipeDTO = new RecipeDTO(
    UUID_1.toString(),
    "name",
    "description",
    new ArrayList<>(),
    "instructions",
    1,
    RecipeDifficulty.EASY
  );

  @Before
  public void setUp() {}

  @Test
  public void testRecipeToRecipeDTO() {
    RecipeDTO dto = RecipeMapper.INSTANCE.recipeToRecipeDTO(recipe);

    assertEquals(dto.getId(), UUID_1.toString());
    assertEquals(dto.getName(), "name");
    assertEquals(dto.getDescription(), "description");
    assertEquals(dto.getIngredients().size(), 0);
    assertEquals(dto.getInstructions(), "instructions");
    assertEquals(dto.getEstimatedMinutes(), 1);
    assertEquals(dto.getRecipeDifficulty(), RecipeDifficulty.EASY);
  }

  @Test
  public void testRecipeDTOToRecipe() {
    Recipe ret = RecipeMapper.INSTANCE.recipeDTOToRecipe(recipeDTO);

    assertEquals(ret.getId(), UUID_1);
    assertEquals(ret.getName(), "name");
    assertEquals(ret.getDescription(), "description");
    assertEquals(ret.getIngredients().size(), 0);
    assertEquals(ret.getInstructions(), "instructions");
    assertEquals(ret.getEstimatedMinutes(), 1);
    assertEquals(ret.getRecipeDifficulty(), RecipeDifficulty.EASY);
  }
}
