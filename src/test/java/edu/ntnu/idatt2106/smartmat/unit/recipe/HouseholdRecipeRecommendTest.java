package edu.ntnu.idatt2106.smartmat.unit.recipe;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeDifficulty;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.service.recipe.HouseholdRecipeRecommend;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class HouseholdRecipeRecommendTest {

  private Household household;

  List<Recipe> recipes;

  @Before
  public void setUp() throws Exception {
    Unit unit = new Unit("Kilogram", "kg", null, 0.0, null);
    Ingredient carrot = new Ingredient(1L, "Carrot", null, null, unit);
    Ingredient potato = new Ingredient(2L, "Potato", null, null, unit);
    Ingredient onion = new Ingredient(3L, "Onion", null, null, unit);
    Ingredient garlic = new Ingredient(4L, "Garlic", null, null, unit);
    Ingredient tomato = new Ingredient(5L, "Tomato", null, null, unit);
    Ingredient pasta = new Ingredient(6L, "Pasta", null, null, unit);

    // Create pasta recipe
    Recipe pastaRecipe = new Recipe(
      null,
      "Pasta",
      "Pasta with tomato sauce",
      new HashSet<>(),
      "Cook pasta, make sauce",
      50,
      RecipeDifficulty.EASY,
      new HashSet<>()
    );
    RecipeIngredient pastaRecipePasta = new RecipeIngredient(pastaRecipe, pasta, 1.0);
    pastaRecipe.getIngredients().add(pastaRecipePasta);

    // Create tomato sauce recipe
    Recipe tomatoSauceRecipe = new Recipe(
      null,
      "Tomato sauce",
      "Tomato sauce",
      new HashSet<>(),
      "Cook tomato sauce",
      50,
      RecipeDifficulty.EASY,
      new HashSet<>()
    );
    RecipeIngredient tomatoSauceRecipeTomato = new RecipeIngredient(tomatoSauceRecipe, tomato, 2.0);
    RecipeIngredient tomatoSauceRecipeOnion = new RecipeIngredient(tomatoSauceRecipe, onion, 1.0);
    RecipeIngredient tomatoSauceRecipeGarlic = new RecipeIngredient(tomatoSauceRecipe, garlic, 1.0);
    tomatoSauceRecipe.getIngredients().add(tomatoSauceRecipeTomato);
    tomatoSauceRecipe.getIngredients().add(tomatoSauceRecipeOnion);
    tomatoSauceRecipe.getIngredients().add(tomatoSauceRecipeGarlic);

    // Create carrot soup recipe
    Recipe carrotSoupRecipe = new Recipe(
      null,
      "Carrot soup",
      "Carrot soup",
      new HashSet<>(),
      "Cook carrot soup",
      50,
      RecipeDifficulty.EASY,
      new HashSet<>()
    );
    RecipeIngredient carrotSoupRecipeCarrot = new RecipeIngredient(carrotSoupRecipe, carrot, 5.0);
    RecipeIngredient carrotSoupRecipePotato = new RecipeIngredient(carrotSoupRecipe, potato, 1.0);
    RecipeIngredient carrotSoupRecipeOnion = new RecipeIngredient(carrotSoupRecipe, onion, 1.0);
    carrotSoupRecipe.getIngredients().add(carrotSoupRecipeCarrot);
    carrotSoupRecipe.getIngredients().add(carrotSoupRecipePotato);
    carrotSoupRecipe.getIngredients().add(carrotSoupRecipeOnion);

    recipes = List.of(pastaRecipe, tomatoSauceRecipe, carrotSoupRecipe);

    // Create food products
    FoodProduct tomatoFoodProduct = new FoodProduct(
      1L,
      "Bama Tomato",
      "123456789123",
      1.0D,
      false,
      null,
      tomato,
      null
    );
    FoodProduct carrotFoodProduct = new FoodProduct(
      1L,
      "Bama Carrot",
      "123456789123",
      1.0D,
      false,
      null,
      carrot,
      null
    );
    FoodProduct potatoFoodProduct = new FoodProduct(
      2L,
      "Bama Potato",
      "123456789123",
      1.0D,
      false,
      null,
      potato,
      null
    );
    FoodProduct onionFoodProduct = new FoodProduct(
      3L,
      "Bama Onion",
      "123456789123",
      1.0D,
      false,
      null,
      onion,
      null
    );

    // Create household food products
    household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
    HouseholdFoodProduct householdFoodProductTomato = new HouseholdFoodProduct(
      UUID.randomUUID(),
      tomatoFoodProduct,
      household,
      LocalDate.now().plusDays(10),
      2.0
    );
    HouseholdFoodProduct householdFoodProductCarrot = new HouseholdFoodProduct(
      UUID.randomUUID(),
      carrotFoodProduct,
      household,
      LocalDate.now().plusDays(10),
      5.0
    );
    HouseholdFoodProduct householdFoodProductPotato = new HouseholdFoodProduct(
      UUID.randomUUID(),
      potatoFoodProduct,
      household,
      LocalDate.now().plusDays(10),
      2.0
    );
    HouseholdFoodProduct householdFoodProductOnion = new HouseholdFoodProduct(
      UUID.randomUUID(),
      onionFoodProduct,
      household,
      LocalDate.now().plusDays(10),
      2.0
    );
    household.getFoodProducts().add(householdFoodProductTomato);
    household.getFoodProducts().add(householdFoodProductCarrot);
    household.getFoodProducts().add(householdFoodProductPotato);
    household.getFoodProducts().add(householdFoodProductOnion);

    // Create user
    User user = testUserFactory(TestUserEnum.GOOD);
    User user2 = testUserFactory(TestUserEnum.UPDATE);

    HouseholdMember householdMember = new HouseholdMember(household, user, HouseholdRole.OWNER);
    HouseholdMember householdMember2 = new HouseholdMember(household, user2, HouseholdRole.MEMBER);

    // Add user to household
    household.getMembers().add(householdMember);
    household.getMembers().add(householdMember2);

    user.getHouseholds().add(householdMember);
    user2.getHouseholds().add(householdMember2);
  }

  @Test
  public void testHouseHoldRecipeRecommend() {
    List<Recipe> recommendedRecipes = HouseholdRecipeRecommend
      .getRecommendedRecipes(household, recipes, new ArrayList<>())
      .stream()
      .toList();

    assertEquals(2, recommendedRecipes.size());
    assertTrue(recommendedRecipes.contains(recipes.get(1)));
    assertTrue(recommendedRecipes.contains(recipes.get(2)));
    assertFalse(recommendedRecipes.contains(recipes.get(0)));
    assertEquals(recommendedRecipes.get(0), recipes.get(2));
  }

  @Test
  public void testHouseHoldRecipeRecommendNoRecipes() {
    List<Recipe> recommendedRecipes = HouseholdRecipeRecommend
      .getRecommendedRecipes(household, new ArrayList<>(), new ArrayList<>())
      .stream()
      .toList();

    assertEquals(0, recommendedRecipes.size());
  }

  @Test
  public void testHouseholdRecipeRecommendAllRecipesWhenNoFoodInHousehold() {
    List<Recipe> recommendedRecipes = HouseholdRecipeRecommend
      .getRecommendedRecipes(
        testHouseholdFactory(TestHouseholdEnum.NULL_ID),
        recipes,
        new ArrayList<>()
      )
      .stream()
      .toList();

    assertEquals(3, recommendedRecipes.size());
    assertTrue(recommendedRecipes.contains(recipes.get(0)));
    assertTrue(recommendedRecipes.contains(recipes.get(1)));
    assertTrue(recommendedRecipes.contains(recipes.get(2)));
  }

  @Test
  public void testHouseholdRecipeRecommendRecipesCorrectlyWhenRecipesAreUsed() {
    List<Recipe> recommendedRecipes = HouseholdRecipeRecommend
      .getRecommendedRecipes(household, recipes, List.of(recipes.get(2)))
      .stream()
      .toList();

    assertEquals(2, recommendedRecipes.size());
    assertTrue(recommendedRecipes.contains(recipes.get(1)));
    assertTrue(recommendedRecipes.contains(recipes.get(2)));
    assertFalse(recommendedRecipes.contains(recipes.get(0)));
    assertEquals(recommendedRecipes.get(0), recipes.get(2));
  }
}
