package edu.ntnu.idatt2106.smartmat.integration.household;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipe;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipeId;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeDifficulty;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import edu.ntnu.idatt2106.smartmat.repository.household.WeeklyRecipeRepository;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.HouseholdFoodProductService;
import edu.ntnu.idatt2106.smartmat.service.household.WeeklyRecipeService;
import edu.ntnu.idatt2106.smartmat.service.household.WeeklyRecipeServiceImpl;
import edu.ntnu.idatt2106.smartmat.service.statistic.FoodProductHistoryService;
import java.time.LocalDate;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test for the temperary used ingredient service.
 * @author Callum G.
 * @version 1.1 - 1.5.2023
 */
@RunWith(SpringRunner.class)
public class WeeklyRecipeServiceIntegrationTest {

  @TestConfiguration
  static class WeeklyRecipeServiceIntegrationTestConfiguration {

    @Bean
    public WeeklyRecipeService householdService() {
      return new WeeklyRecipeServiceImpl();
    }
  }

  @Autowired
  private WeeklyRecipeService weeklyRecipeService;

  @MockBean
  private HouseholdFoodProductService householdFoodProductService;

  @MockBean
  private WeeklyRecipeRepository weeklyRecipeRepository;

  @MockBean
  private FoodProductHistoryService foodProductHistoryService;

  // private final UUID EXISTING_UUID = UUID.randomUUID();

  // private final UUID NON_EXISTING_UUID = UUID.randomUUID();

  // private final UUID NULL_UUID = null;

  private Ingredient carrot;

  private Ingredient tomato;

  // private Ingredient nullIngredient;

  private Household household;

  private Household noneHousehold;

  // private Household nullHousehold;

  // private FoodProduct foodProduct;

  // private FoodProduct foodProduct2;

  // private FoodProduct nullFoodProduct;

  // private HouseholdFoodProduct hfp;

  // private HouseholdFoodProduct noneHfp;

  // private HouseholdFoodProduct nullHfp;

  private WeeklyRecipe weeklyRecipe;

  private WeeklyRecipe noneWeeklyRecipe;

  private WeeklyRecipe nullWeeklyRecipe;

  @Before
  public void setUp() {
    carrot = Ingredient.builder().id(1L).name("Carrot").build();
    tomato = Ingredient.builder().id(2L).name("Tomato").build();
    // nullIngredient = null;

    household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
    noneHousehold = testHouseholdFactory(TestHouseholdEnum.BAD_HOUSEHOLD);
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
    carrotSoupRecipe.getIngredients().add(carrotSoupRecipeCarrot);

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
    tomatoSauceRecipe.getIngredients().add(tomatoSauceRecipeTomato);

    // nullHousehold = null;

    // foodProduct =
    //   new FoodProduct(1L, "Carrot", "1234567890123", 4.0D, true, new HashSet<>(), carrot);
    // foodProduct2 =
    //   new FoodProduct(2L, "Tomato", "1234567890124", 4.0D, true, new HashSet<>(), tomato);
    // nullFoodProduct = null;

    // hfp = new HouseholdFoodProduct(NULL_UUID, foodProduct, household, LocalDate.now(), 1.0D);
    // noneHfp =
    //   new HouseholdFoodProduct(
    //     NON_EXISTING_UUID,
    //     foodProduct2,
    //     noneHousehold,
    //     LocalDate.now(),
    //     1.0D
    //   );
    // nullHfp = null;

    weeklyRecipe = new WeeklyRecipe(LocalDate.of(2023, 2, 1));
    weeklyRecipe.setHousehold(household);
    weeklyRecipe.setRecipe(tomatoSauceRecipe);

    noneWeeklyRecipe = new WeeklyRecipe(LocalDate.of(2023, 2, 1));
    noneWeeklyRecipe.setHousehold(noneHousehold);
    noneWeeklyRecipe.setRecipe(carrotSoupRecipe);

    nullWeeklyRecipe = null;
    when(weeklyRecipeRepository.existsById(new WeeklyRecipeId(household, LocalDate.of(2023, 2, 1))))
      .thenReturn(true);
    when(
      weeklyRecipeRepository.existsById(new WeeklyRecipeId(noneHousehold, LocalDate.of(2023, 2, 1)))
    )
      .thenReturn(false);
  }

  @Test
  public void testExistsByIdExisting() {
    when(weeklyRecipeRepository.existsById(new WeeklyRecipeId(household, LocalDate.of(2023, 2, 1))))
      .thenReturn(true);
    assertTrue(weeklyRecipeService.existsById(weeklyRecipe));
  }

  @Test
  public void testExistsByIdNonExisting() {
    when(
      weeklyRecipeRepository.existsById(new WeeklyRecipeId(noneHousehold, LocalDate.of(2023, 2, 1)))
    )
      .thenReturn(false);
    assertFalse(weeklyRecipeService.existsById(noneWeeklyRecipe));
  }

  @Test
  public void testExistsByIdNull() {
    assertThrows(
      NullPointerException.class,
      () -> weeklyRecipeService.existsById(nullWeeklyRecipe)
    );
  }

  @Test
  public void testSaveExisting() {
    when(weeklyRecipeRepository.save(weeklyRecipe)).thenReturn(weeklyRecipe);
    assertThrows(
      IllegalArgumentException.class,
      () -> weeklyRecipeService.saveWeeklyRecipe(weeklyRecipe)
    );
  }

  @Test
  public void testSaveNonExisting() {
    when(weeklyRecipeRepository.save(noneWeeklyRecipe)).thenReturn(noneWeeklyRecipe);
    assertEquals(weeklyRecipeService.saveWeeklyRecipe(noneWeeklyRecipe), noneWeeklyRecipe);
  }

  @Test
  public void testSaveNull() {
    assertThrows(
      NullPointerException.class,
      () -> weeklyRecipeService.saveWeeklyRecipe(nullWeeklyRecipe)
    );
  }

  @Test
  public void testSaveWithNullHousehold() {
    weeklyRecipe.setHousehold(null);
    assertThrows(
      NullPointerException.class,
      () -> weeklyRecipeService.saveWeeklyRecipe(weeklyRecipe)
    );
  }

  @Test
  public void deleteByIdExisting() {
    when(weeklyRecipeRepository.existsById(new WeeklyRecipeId(household, LocalDate.of(2023, 2, 1))))
      .thenReturn(true);
    doNothing()
      .when(weeklyRecipeRepository)
      .deleteById(new WeeklyRecipeId(household, LocalDate.of(2023, 2, 1)));
    assertDoesNotThrow(() -> weeklyRecipeService.deleteWeeklyRecipe(weeklyRecipe));
  }

  @Test
  public void deleteByIdNonExisting() {
    when(
      weeklyRecipeRepository.existsById(new WeeklyRecipeId(noneHousehold, LocalDate.of(2023, 2, 1)))
    )
      .thenReturn(false);
    doNothing()
      .when(weeklyRecipeRepository)
      .deleteById(new WeeklyRecipeId(noneHousehold, LocalDate.of(2023, 2, 1)));
    assertThrows(
      IllegalArgumentException.class,
      () -> weeklyRecipeService.deleteWeeklyRecipe(noneWeeklyRecipe)
    );
  }

  @Test
  public void deleteByIdNull() {
    assertThrows(
      NullPointerException.class,
      () -> weeklyRecipeService.deleteWeeklyRecipe(nullWeeklyRecipe)
    );
  }

  @Test
  public void deleteByIdNullHousehold() {
    weeklyRecipe.setHousehold(null);
    assertThrows(
      NullPointerException.class,
      () -> weeklyRecipeService.deleteWeeklyRecipe(weeklyRecipe)
    );
  }
}
