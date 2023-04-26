package edu.ntnu.idatt2106.smartmat.endpoint.recipe;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.createAuthenticationToken;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.recipe.RecipeController;
import edu.ntnu.idatt2106.smartmat.exceptions.ingredient.IngredientNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.recipe.RecipeNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeDifficulty;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import edu.ntnu.idatt2106.smartmat.service.recipe.RecipeService;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest({ RecipeController.class, SecurityConfig.class })
public class RecipeControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private RecipeService recipeService;

  @MockBean
  private IngredientService ingredientService;

  private static final String BASE_URL = "/api/v1/private/recipes";

  private static final UUID badUUID = UUID.randomUUID();

  private Recipe recipe;

  private Ingredient ingredient;

  @Before
  public void setUp() throws IngredientNotFoundException {
    recipe =
      new Recipe(
        UUID.randomUUID(),
        "name",
        "description",
        new HashSet<>(),
        "instructions",
        1,
        RecipeDifficulty.EASY
      );

    ingredient = Ingredient.builder().id(1L).name("Ingredient").build();

    when(recipeService.findAllRecipes()).thenReturn(Set.of(recipe));
    when(ingredientService.getIngredientById(ingredient.getId())).thenReturn(ingredient);

    when(recipeService.findRecipeById(recipe.getId())).thenReturn(recipe);
    when(recipeService.findRecipeById(badUUID)).thenThrow(new RecipeNotFoundException());
    when(recipeService.findRecipeById(null)).thenThrow(new NullPointerException());

    when(recipeService.saveRecipe(any(Recipe.class))).thenReturn(recipe);

    when(recipeService.updateRecipe(eq(recipe.getId()), any(Recipe.class))).thenReturn(recipe);

    doNothing().when(recipeService).deleteRecipeById(recipe.getId());
  }

  @Test
  public void testGetExistingRecipe() {
    try {
      mvc
        .perform(
          get(BASE_URL + "/" + recipe.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.GOOD))))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testGetNonExistingRecipe() {
    try {
      mvc
        .perform(
          get(BASE_URL + "/" + badUUID.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.GOOD))))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testGetAllRecipes() {
    try {
      mvc
        .perform(
          get(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.GOOD))))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCreateRecipe() {
    try {
      mvc
        .perform(
          post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\n" +
              "  \"name\": \"name\",\n" +
              "  \"description\": \"description\",\n" +
              "  \"ingredients\": [],\n" +
              "  \"instructions\": \"instructions\",\n" +
              "  \"estimatedMinutes\": 1,\n" +
              "  \"recipeDifficulty\": \"EASY\"\n" +
              "}"
            )
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testUpdateRecipe() {
    try {
      mvc
        .perform(
          put(BASE_URL + "/" + recipe.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\n" +
              "  \"name\": \"name\",\n" +
              "  \"description\": \"description\",\n" +
              "  \"ingredients\": [],\n" +
              "  \"instructions\": \"instructions\",\n" +
              "  \"estimatedMinutes\": 50,\n" +
              "  \"recipeDifficulty\": \"EASY\"\n" +
              "}"
            )
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCreateRecipeWithIngredients() {
    try {
      mvc
        .perform(
          post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\n" +
              "  \"name\": \"name\",\n" +
              "  \"description\": \"description\",\n" +
              "  \"ingredients\": [{" +
              "    \"ingredient\": " +
              ingredient.getId() +
              "," +
              "    \"amount\": 1" +
              "}],\n" +
              "  \"instructions\": \"instructions\",\n" +
              "  \"estimatedMinutes\": 1,\n" +
              "  \"recipeDifficulty\": \"EASY\"\n" +
              "}"
            )
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCreateRecipeWithUnknownIngredient() {
    long ingredientId = 9999;
    try {
      when(ingredientService.getIngredientById(ingredientId))
        .thenThrow(new IngredientNotFoundException());
      mvc
        .perform(
          post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\n" +
              "  \"name\": \"name\",\n" +
              "  \"description\": \"description\",\n" +
              "  \"ingredients\": [{" +
              "    \"ingredient\": 9999," +
              "    \"amount\": 1" +
              "}],\n" +
              "  \"instructions\": \"instructions\",\n" +
              "  \"estimatedMinutes\": 1,\n" +
              "  \"recipeDifficulty\": \"EASY\"\n" +
              "}"
            )
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testUpdateRecipeWithIngredients() {
    try {
      mvc
        .perform(
          put(BASE_URL + "/" + recipe.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\n" +
              "  \"name\": \"name\",\n" +
              "  \"description\": \"description\",\n" +
              "  \"ingredients\": [{" +
              "    \"ingredient\": " +
              ingredient.getId() +
              "," +
              "    \"amount\": 1" +
              "}],\n" +
              "  \"instructions\": \"instructions\",\n" +
              "  \"estimatedMinutes\": 50,\n" +
              "  \"recipeDifficulty\": \"EASY\"\n" +
              "}"
            )
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testDeleteRecipe() {
    try {
      mvc
        .perform(
          delete(BASE_URL + "/" + recipe.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
        )
        .andExpect(status().isNoContent());
    } catch (Exception e) {
      fail();
    }
  }
}
