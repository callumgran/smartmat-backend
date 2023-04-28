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
// import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
// import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.TempUsedIngredient;
import edu.ntnu.idatt2106.smartmat.model.household.TempUsedIngredientAmount;
import edu.ntnu.idatt2106.smartmat.model.household.TempUsedIngredientId;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.repository.household.TempUsedIngredientRepository;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.HouseholdFoodProductService;
import edu.ntnu.idatt2106.smartmat.service.household.TempUsedIngredientService;
import edu.ntnu.idatt2106.smartmat.service.household.TempUsedIngredientServiceImpl;
import java.time.LocalDate;
import java.util.HashSet;
// import java.util.UUID;
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
 * @version 1.0 - 28.4.2023
 */
@RunWith(SpringRunner.class)
public class TempUsedIngredientServiceIntegrationTest {

  @TestConfiguration
  static class TempUsedIngredientServiceIntegrationTestConfiguration {

    @Bean
    public TempUsedIngredientService householdService() {
      return new TempUsedIngredientServiceImpl();
    }
  }

  @Autowired
  private TempUsedIngredientService tempUsedIngredientService;

  @MockBean
  private HouseholdFoodProductService householdFoodProductService;

  @MockBean
  private TempUsedIngredientRepository tempUsedIngredientRepository;

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

  private TempUsedIngredient tmpIngredient;

  private TempUsedIngredient noneTmpIngredient;

  private TempUsedIngredient nullTmpIngredient;

  @Before
  public void setUp() {
    carrot = Ingredient.builder().id(1L).name("Carrot").build();
    tomato = Ingredient.builder().id(2L).name("Tomato").build();
    // nullIngredient = null;

    household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
    noneHousehold = testHouseholdFactory(TestHouseholdEnum.BAD_HOUSEHOLD);
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

    tmpIngredient = new TempUsedIngredient(LocalDate.of(2023, 2, 1));
    tmpIngredient.setHousehold(household);
    TempUsedIngredientAmount tmpIngredientAmount = new TempUsedIngredientAmount(
      tmpIngredient,
      carrot,
      1.0D
    );
    TempUsedIngredientAmount tmpIngredientAmount2 = new TempUsedIngredientAmount(
      noneTmpIngredient,
      tomato,
      1.0D
    );
    tmpIngredient.setTempUsedIngredientAmount(new HashSet<>());
    tmpIngredient.getTempUsedIngredientAmount().add(tmpIngredientAmount);
    tmpIngredient.getTempUsedIngredientAmount().add(tmpIngredientAmount2);

    noneTmpIngredient = new TempUsedIngredient(LocalDate.of(2023, 2, 1));
    noneTmpIngredient.setHousehold(noneHousehold);
    noneTmpIngredient.setTempUsedIngredientAmount(new HashSet<>());

    nullTmpIngredient = null;
    when(
      tempUsedIngredientRepository.existsById(
        new TempUsedIngredientId(household, LocalDate.of(2023, 2, 1))
      )
    )
      .thenReturn(true);
    when(
      tempUsedIngredientRepository.existsById(
        new TempUsedIngredientId(noneHousehold, LocalDate.of(2023, 2, 1))
      )
    )
      .thenReturn(false);
  }

  @Test
  public void testExistsByIdExisting() {
    when(
      tempUsedIngredientRepository.existsById(
        new TempUsedIngredientId(household, LocalDate.of(2023, 2, 1))
      )
    )
      .thenReturn(true);
    assertTrue(tempUsedIngredientService.existsById(tmpIngredient));
  }

  @Test
  public void testExistsByIdNonExisting() {
    when(
      tempUsedIngredientRepository.existsById(
        new TempUsedIngredientId(noneHousehold, LocalDate.of(2023, 2, 1))
      )
    )
      .thenReturn(false);
    assertFalse(tempUsedIngredientService.existsById(noneTmpIngredient));
  }

  @Test
  public void testExistsByIdNull() {
    assertThrows(
      NullPointerException.class,
      () -> tempUsedIngredientService.existsById(nullTmpIngredient)
    );
  }

  @Test
  public void testSaveExisting() {
    when(tempUsedIngredientRepository.save(tmpIngredient)).thenReturn(tmpIngredient);
    assertThrows(
      IllegalArgumentException.class,
      () -> tempUsedIngredientService.saveTempUsedIngredient(tmpIngredient)
    );
  }

  @Test
  public void testSaveNonExisting() {
    when(tempUsedIngredientRepository.save(noneTmpIngredient)).thenReturn(noneTmpIngredient);
    assertEquals(
      tempUsedIngredientService.saveTempUsedIngredient(noneTmpIngredient),
      noneTmpIngredient
    );
  }

  @Test
  public void testSaveNull() {
    assertThrows(
      NullPointerException.class,
      () -> tempUsedIngredientService.saveTempUsedIngredient(nullTmpIngredient)
    );
  }

  @Test
  public void testSaveWithNullHousehold() {
    tmpIngredient.setHousehold(null);
    assertThrows(
      NullPointerException.class,
      () -> tempUsedIngredientService.saveTempUsedIngredient(tmpIngredient)
    );
  }

  @Test
  public void deleteByIdExisting() {
    when(
      tempUsedIngredientRepository.existsById(
        new TempUsedIngredientId(household, LocalDate.of(2023, 2, 1))
      )
    )
      .thenReturn(true);
    doNothing()
      .when(tempUsedIngredientRepository)
      .deleteById(new TempUsedIngredientId(household, LocalDate.of(2023, 2, 1)));
    assertDoesNotThrow(() -> tempUsedIngredientService.deleteTempUsedIngredient(tmpIngredient));
  }

  @Test
  public void deleteByIdNonExisting() {
    when(
      tempUsedIngredientRepository.existsById(
        new TempUsedIngredientId(noneHousehold, LocalDate.of(2023, 2, 1))
      )
    )
      .thenReturn(false);
    doNothing()
      .when(tempUsedIngredientRepository)
      .deleteById(new TempUsedIngredientId(noneHousehold, LocalDate.of(2023, 2, 1)));
    assertThrows(
      IllegalArgumentException.class,
      () -> tempUsedIngredientService.deleteTempUsedIngredient(noneTmpIngredient)
    );
  }

  @Test
  public void deleteByIdNull() {
    assertThrows(
      NullPointerException.class,
      () -> tempUsedIngredientService.deleteTempUsedIngredient(nullTmpIngredient)
    );
  }

  @Test
  public void deleteByIdNullHousehold() {
    tmpIngredient.setHousehold(null);
    assertThrows(
      NullPointerException.class,
      () -> tempUsedIngredientService.deleteTempUsedIngredient(tmpIngredient)
    );
  }
}
