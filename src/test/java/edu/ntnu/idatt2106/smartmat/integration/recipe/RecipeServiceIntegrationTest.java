package edu.ntnu.idatt2106.smartmat.integration.recipe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.recipe.RecipeAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.recipe.RecipeNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeDifficulty;
import edu.ntnu.idatt2106.smartmat.repository.recipe.RecipeIngredientRepository;
import edu.ntnu.idatt2106.smartmat.repository.recipe.RecipeRepository;
import edu.ntnu.idatt2106.smartmat.service.recipe.RecipeService;
import edu.ntnu.idatt2106.smartmat.service.recipe.RecipeServiceImpl;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test class for the recipe service.
 * @author Callum G.
 * @version 1.0 - 21.04.23
 */
@RunWith(SpringRunner.class)
public class RecipeServiceIntegrationTest {

  @TestConfiguration
  static class RecipeServiceIntegrationTestContextConfiguration {

    @Bean
    public RecipeService recipeService() {
      return new RecipeServiceImpl();
    }
  }

  @Autowired
  private RecipeService recipeService;

  @MockBean
  private RecipeRepository recipeRepository;

  @MockBean
  private RecipeIngredientRepository recipeIngredientRepository;

  Recipe carrotCake;
  Recipe bananaCake;
  Recipe chocolateCake;

  @Before
  public void setUp() {
    final UUID carrotCakeId = UUID.randomUUID();
    final UUID bananaCakeId = null;
    final UUID chocolateCakeId = UUID.randomUUID();

    carrotCake =
      new Recipe(
        carrotCakeId,
        "Carrot Cake",
        "A delicious carrot cake",
        new HashSet<>(),
        "1: do this, 2...",
        30,
        RecipeDifficulty.EASY,
        new HashSet<>()
      );
    bananaCake =
      new Recipe(
        bananaCakeId,
        "Banana Cake",
        "A delicious banana cake",
        new HashSet<>(),
        "1: do this, 2...",
        30,
        RecipeDifficulty.MEDIUM,
        new HashSet<>()
      );
    chocolateCake =
      new Recipe(
        chocolateCakeId,
        "Chocolate Cake",
        "A delicious chocolate cake",
        new HashSet<>(),
        "1: do this, 2...",
        30,
        RecipeDifficulty.ADVANCED,
        new HashSet<>()
      );

    when(recipeRepository.existsById(carrotCakeId)).thenReturn(true);
    when(recipeRepository.existsById(chocolateCakeId)).thenReturn(false);

    when(recipeRepository.findById(carrotCakeId)).thenReturn(Optional.of(carrotCake));
    when(recipeRepository.findById(chocolateCakeId)).thenReturn(Optional.empty());

    when(recipeRepository.findAll()).thenReturn(List.of(carrotCake));

    when(recipeRepository.save(carrotCake)).thenReturn(carrotCake);
    when(recipeRepository.save(bananaCake)).thenReturn(bananaCake);
    when(recipeRepository.save(chocolateCake)).thenReturn(chocolateCake);

    doNothing().when(recipeRepository).deleteById(carrotCakeId);
    doNothing().when(recipeRepository).deleteById(bananaCakeId);
    doNothing().when(recipeRepository).deleteById(chocolateCakeId);
    doNothing().when(recipeIngredientRepository).deleteAllByRecipeId(carrotCakeId);
    doNothing().when(recipeIngredientRepository).deleteAllByRecipeId(bananaCakeId);
    doNothing().when(recipeIngredientRepository).deleteAllByRecipeId(chocolateCakeId);

    when(recipeRepository.findByNameContainingIgnoreCase("c"))
      .thenReturn(Optional.of(List.of(carrotCake)));
    when(recipeRepository.findByNameContainingIgnoreCase("carrot"))
      .thenReturn(Optional.of(List.of(carrotCake)));
    when(recipeRepository.findByNameContainingIgnoreCase("ch")).thenReturn(Optional.of(List.of()));
  }

  @Test
  public void testExistsByIdExistingRecipe() {
    assertTrue(recipeService.existsById(carrotCake.getId()));
  }

  @Test
  public void testExistsByIdNonExistingRecipe() {
    assertFalse(recipeService.existsById(chocolateCake.getId()));
  }

  @Test
  public void testExistsByNullId() {
    assertThrows(NullPointerException.class, () -> recipeService.existsById(bananaCake.getId()));
  }

  @Test
  public void testFindByIdExistingRecipe() {
    assertEquals(recipeService.findRecipeById(carrotCake.getId()), carrotCake);
  }

  @Test
  public void testFindByIdNullId() {
    assertThrows(
      NullPointerException.class,
      () -> recipeService.findRecipeById(bananaCake.getId())
    );
  }

  @Test
  public void testFindByIdNonExistingRecipe() {
    assertThrows(
      RecipeNotFoundException.class,
      () -> recipeService.findRecipeById(chocolateCake.getId())
    );
  }

  @Test
  public void testSaveExistingRecipe() {
    assertThrows(RecipeAlreadyExistsException.class, () -> recipeService.saveRecipe(carrotCake));
  }

  @Test
  public void testSaveNewRecipe() {
    assertEquals(recipeService.saveRecipe(bananaCake), bananaCake);
  }

  @Test
  public void testSaveNullRecipe() {
    assertThrows(NullPointerException.class, () -> recipeService.saveRecipe(null));
  }

  @Test
  public void testUpdateExistingRecipe() {
    assertEquals(recipeService.updateRecipe(carrotCake.getId(), carrotCake), carrotCake);
  }

  @Test
  public void testUpdateNewRecipe() {
    assertThrows(
      NullPointerException.class,
      () -> recipeService.updateRecipe(bananaCake.getId(), bananaCake)
    );
  }

  @Test
  public void testUpdateNonExistingRecipe() {
    assertThrows(
      RecipeNotFoundException.class,
      () -> recipeService.updateRecipe(chocolateCake.getId(), chocolateCake)
    );
  }

  @Test
  public void testUpdateNullRecipe() {
    assertThrows(
      NullPointerException.class,
      () -> recipeService.updateRecipe(carrotCake.getId(), null)
    );
  }

  @Test
  public void testDeleteExistingRecipe() {
    assertDoesNotThrow(() -> recipeService.deleteRecipeById(carrotCake.getId()));
  }

  @Test
  public void testDeleteNonExistingRecipe() {
    assertThrows(
      RecipeNotFoundException.class,
      () -> recipeService.deleteRecipeById(chocolateCake.getId())
    );
  }

  @Test
  public void testDeleteNullRecipe() {
    assertThrows(
      NullPointerException.class,
      () -> recipeService.deleteRecipeById(bananaCake.getId())
    );
  }

  @Test
  public void testFindAllRecipes() {
    assertEquals(recipeService.findAllRecipes(), List.of(carrotCake));
  }

  @Test
  public void testFindAllRecipesByNameLetter() {
    assertEquals(recipeService.findRecipesByName("c"), List.of(carrotCake));
  }

  @Test
  public void testFindAllRecipesByName() {
    assertEquals(recipeService.findRecipesByName("carrot"), List.of(carrotCake));
  }

  @Test
  public void testFindAllRecipesByNameNonExisting() {
    assertEquals(recipeService.findRecipesByName("ch"), List.of());
  }

  @Test
  public void testFindAllRecipesByNameNull() {
    assertThrows(NullPointerException.class, () -> recipeService.findRecipesByName(null));
  }
}
