package edu.ntnu.idatt2106.smartmat.integration.household;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipe;
import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipeId;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeDifficulty;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;
import edu.ntnu.idatt2106.smartmat.repository.household.WeeklyRecipeRepository;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.HouseholdFoodProductService;
import edu.ntnu.idatt2106.smartmat.service.household.WeeklyRecipeService;
import edu.ntnu.idatt2106.smartmat.service.household.WeeklyRecipeServiceImpl;
import edu.ntnu.idatt2106.smartmat.service.statistic.FoodProductHistoryService;
import java.time.LocalDate;
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

  private Ingredient carrot;

  private Ingredient tomato;

  private Household household;

  private Household noneHousehold;

  private WeeklyRecipe weeklyRecipe;

  private WeeklyRecipe noneWeeklyRecipe;

  private WeeklyRecipe nullWeeklyRecipe;

  @Before
  public void setUp() {
    Unit unit = new Unit("kilogram", "kg", new HashSet<>(), 1, UnitTypeEnum.SOLID, new HashSet<>());
    Unit gram = new Unit("gram", "g", new HashSet<>(), 0.001, UnitTypeEnum.SOLID, new HashSet<>());
    carrot = new Ingredient(1L, "Carrot", null, null, unit);
    tomato = new Ingredient(5L, "Tomato", null, null, unit);

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
      new HashSet<>(),
      null
    );
    RecipeIngredient carrotSoupRecipeCarrot = new RecipeIngredient(
      carrotSoupRecipe,
      carrot,
      5.0,
      unit
    );
    carrotSoupRecipe.getIngredients().add(carrotSoupRecipeCarrot);

    Recipe tomatoSauceRecipe = new Recipe(
      null,
      "Tomato sauce",
      "Tomato sauce",
      new HashSet<>(),
      "Cook tomato sauce",
      50,
      RecipeDifficulty.EASY,
      new HashSet<>(),
      null
    );
    RecipeIngredient tomatoSauceRecipeTomato = new RecipeIngredient(
      tomatoSauceRecipe,
      tomato,
      2.0,
      unit
    );
    tomatoSauceRecipe.getIngredients().add(tomatoSauceRecipeTomato);

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

    FoodProduct tomatoFoodProduct = new FoodProduct(
      1L,
      "Bama Tomato",
      "123456789123",
      1000.0D,
      false,
      null,
      null,
      tomato,
      null,
      false,
      gram,
      null
    );
    FoodProduct carrotFoodProduct = new FoodProduct(
      1L,
      "Bama Carrot",
      "123456789123",
      1000.0D,
      false,
      null,
      null,
      carrot,
      null,
      false,
      gram,
      null
    );
    HouseholdFoodProduct householdFoodProductTomato = new HouseholdFoodProduct(
      UUID.randomUUID(),
      tomatoFoodProduct,
      household,
      LocalDate.now().plusDays(10),
      4.0
    );
    HouseholdFoodProduct householdFoodProductCarrot = new HouseholdFoodProduct(
      UUID.randomUUID(),
      carrotFoodProduct,
      household,
      LocalDate.now().plusDays(10),
      10.0
    );
    household.getFoodProducts().add(householdFoodProductTomato);
    household.getFoodProducts().add(householdFoodProductCarrot);
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
  public void deleteExisting() {
    when(weeklyRecipeRepository.existsById(new WeeklyRecipeId(household, LocalDate.of(2023, 2, 1))))
      .thenReturn(true);
    doNothing()
      .when(weeklyRecipeRepository)
      .deleteById(new WeeklyRecipeId(household, LocalDate.of(2023, 2, 1)));
    assertDoesNotThrow(() -> weeklyRecipeService.deleteWeeklyRecipe(weeklyRecipe));
  }

  @Test
  public void deleteNonExisting() {
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
  public void deleteNull() {
    assertThrows(
      NullPointerException.class,
      () -> weeklyRecipeService.deleteWeeklyRecipe(nullWeeklyRecipe)
    );
  }

  @Test
  public void deleteNullHousehold() {
    weeklyRecipe.setHousehold(null);
    assertThrows(
      NullPointerException.class,
      () -> weeklyRecipeService.deleteWeeklyRecipe(weeklyRecipe)
    );
  }

  @Test
  public void deleteByIdExisting() {
    when(weeklyRecipeRepository.existsById(new WeeklyRecipeId(household, LocalDate.of(2023, 2, 1))))
      .thenReturn(true);
    doNothing()
      .when(weeklyRecipeRepository)
      .deleteById(new WeeklyRecipeId(household, LocalDate.of(2023, 2, 1)));
    assertDoesNotThrow(() ->
      weeklyRecipeService.deleteWeeklyRecipeById(
        new WeeklyRecipeId(household, LocalDate.of(2023, 2, 1))
      )
    );
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
      () ->
        weeklyRecipeService.deleteWeeklyRecipeById(
          new WeeklyRecipeId(noneHousehold, LocalDate.of(2023, 2, 1))
        )
    );
  }

  @Test
  public void testGetRecipesForHouseholdWeek() throws Exception {
    when(
      weeklyRecipeRepository.findAllRecipesByHouseholdAndWeek(
        household.getId(),
        LocalDate.of(2023, 2, 1),
        LocalDate.of(2023, 2, 1).plusDays(6)
      )
    )
      .thenReturn(Optional.of(List.of(weeklyRecipe)));
    assertEquals(
      weeklyRecipeService
        .getRecipesForHouseholdWeek(household.getId(), LocalDate.of(2023, 2, 1))
        .stream()
        .toList()
        .get(0),
      weeklyRecipe
    );
  }

  @Test
  public void testGetRecipesForHousehold() throws Exception {
    when(weeklyRecipeRepository.findAllRecipesByHousehold(household.getId()))
      .thenReturn(Optional.of(List.of(weeklyRecipe)));
    assertEquals(
      weeklyRecipeService.getRecipesForHousehold(household.getId()).stream().toList().get(0),
      weeklyRecipe
    );
  }

  @Test
  public void testUseRecipe() throws Exception {
    doNothing().when(householdFoodProductService).deleteFoodProductById(any(UUID.class));
    when(householdFoodProductService.saveFoodProduct(any(HouseholdFoodProduct.class)))
      .thenReturn(new HouseholdFoodProduct());
    when(weeklyRecipeRepository.findById(new WeeklyRecipeId(household, LocalDate.of(2023, 2, 1))))
      .thenReturn(Optional.of(weeklyRecipe));
    when(foodProductHistoryService.saveFoodProductHistory(any(FoodProductHistory.class)))
      .thenReturn(new FoodProductHistory());
    when(weeklyRecipeRepository.save(any(WeeklyRecipe.class))).thenReturn(weeklyRecipe);
    assertDoesNotThrow(() -> weeklyRecipeService.useRecipeDay(weeklyRecipe));
    assertEquals(weeklyRecipe.isUsed(), true);
  }
}
