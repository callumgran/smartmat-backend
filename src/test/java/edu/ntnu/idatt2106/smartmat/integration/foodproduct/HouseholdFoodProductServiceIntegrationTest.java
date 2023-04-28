package edu.ntnu.idatt2106.smartmat.integration.foodproduct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.repository.foodproduct.HouseholdFoodProductRepository;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.HouseholdFoodProductService;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.HouseholdFoodProductServiceImpl;
import java.time.LocalDate;
import java.util.HashSet;
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
 * Integration test for the ingredient service.
 */
@RunWith(SpringRunner.class)
public class HouseholdFoodProductServiceIntegrationTest {

  @TestConfiguration
  static class HouseholdFoodProductServiceIntegrationTestConfiguration {

    @Bean
    public HouseholdFoodProductService householdFoodProductService() {
      return new HouseholdFoodProductServiceImpl();
    }
  }

  @Autowired
  private HouseholdFoodProductService foodProductService;

  @MockBean
  private HouseholdFoodProductRepository foodProductRepository;

  private FoodProduct existingFoodProduct;

  private HouseholdFoodProduct existingHouseholdFoodProduct;

  private HouseholdFoodProduct newHouseholdFoodProduct;

  private HouseholdFoodProduct corruptedHouseholdFoodProduct;

  private static final UUID EXISTING_UUID = UUID.randomUUID();

  private static final UUID NULL_UUID = null;

  private static final UUID CORRUPTED_UUID = UUID.randomUUID();

  @Before
  public void setUp() throws FoodProductNotFoundException {
    Ingredient carrot = new Ingredient(1L, "Carrot", new HashSet<>(), new HashSet<>(), null);
    existingFoodProduct =
      new FoodProduct(1L, "Carrot", "1234567890123", 4.0D, true, new HashSet<>(), carrot, null);

    Household household = new Household(
      UUID.randomUUID(),
      "Household",
      new HashSet<>(),
      new HashSet<>(),
      new HashSet<>(),
      new HashSet<>(),
      null,
      null
    );

    existingHouseholdFoodProduct =
      new HouseholdFoodProduct(
        EXISTING_UUID,
        existingFoodProduct,
        household,
        LocalDate.now(),
        1.0D
      );
    newHouseholdFoodProduct =
      new HouseholdFoodProduct(NULL_UUID, existingFoodProduct, household, LocalDate.now(), 1.0D);
    corruptedHouseholdFoodProduct =
      new HouseholdFoodProduct(
        CORRUPTED_UUID,
        existingFoodProduct,
        household,
        LocalDate.now(),
        1.0D
      );

    when(foodProductRepository.existsById(existingHouseholdFoodProduct.getId())).thenReturn(true);
    when(foodProductRepository.existsById(newHouseholdFoodProduct.getId())).thenReturn(false);
    when(foodProductRepository.existsById(corruptedHouseholdFoodProduct.getId())).thenReturn(false);

    when(foodProductRepository.save(existingHouseholdFoodProduct))
      .thenReturn(existingHouseholdFoodProduct);
    when(foodProductRepository.save(newHouseholdFoodProduct)).thenReturn(newHouseholdFoodProduct);
    when(foodProductRepository.save(corruptedHouseholdFoodProduct))
      .thenReturn(corruptedHouseholdFoodProduct);

    when(foodProductRepository.findById(existingHouseholdFoodProduct.getId()))
      .thenReturn(Optional.of(existingHouseholdFoodProduct));
    when(foodProductRepository.findById(newHouseholdFoodProduct.getId()))
      .thenReturn(Optional.empty());
    when(foodProductRepository.findById(corruptedHouseholdFoodProduct.getId()))
      .thenReturn(Optional.empty());

    doNothing().when(foodProductRepository).deleteById(existingHouseholdFoodProduct.getId());
    doNothing().when(foodProductRepository).deleteById(newHouseholdFoodProduct.getId());
    doNothing().when(foodProductRepository).deleteById(corruptedHouseholdFoodProduct.getId());

    when(
      foodProductRepository.findHouseholdFoodProductByHouseholdAndEAN(
        household.getId(),
        "1234567890123"
      )
    )
      .thenReturn(Optional.of(existingHouseholdFoodProduct));
  }

  @Test
  public void testExistsByIdExistingHouseholdFoodProduct() {
    assertTrue(foodProductService.existsById(existingHouseholdFoodProduct.getId()));
  }

  @Test
  public void testExistsByIdNewHouseholdFoodProduct() {
    assertThrows(
      NullPointerException.class,
      () -> foodProductService.existsById(newHouseholdFoodProduct.getId())
    );
  }

  @Test
  public void testExistsByIdCorruptedHouseholdFoodProduct() {
    assertFalse(foodProductService.existsById(corruptedHouseholdFoodProduct.getId()));
  }

  @Test
  public void testSaveFoodProductExistingHouseholdFoodProduct() {
    assertThrows(
      IllegalArgumentException.class,
      () -> foodProductService.saveFoodProduct(existingHouseholdFoodProduct)
    );
  }

  @Test
  public void testSaveFoodProductNewHouseholdFoodProduct() {
    assertEquals(
      foodProductService.saveFoodProduct(newHouseholdFoodProduct),
      newHouseholdFoodProduct
    );
  }

  @Test
  public void testGetFoodProductByIdExistingHouseholdFoodProduct()
    throws FoodProductNotFoundException {
    assertEquals(
      foodProductService.getFoodProductById(existingHouseholdFoodProduct.getId()),
      existingHouseholdFoodProduct
    );
  }

  @Test
  public void testGetFoodProductByIdNewHouseholdFoodProduct() {
    assertThrows(
      NullPointerException.class,
      () -> foodProductService.getFoodProductById(newHouseholdFoodProduct.getId())
    );
  }

  @Test
  public void testGetFoodProductByIdCorruptedHouseholdFoodProduct() {
    assertThrows(
      FoodProductNotFoundException.class,
      () -> foodProductService.getFoodProductById(corruptedHouseholdFoodProduct.getId())
    );
  }

  @Test
  public void testUpdateFoodProductExistingHouseholdFoodProduct()
    throws FoodProductNotFoundException {
    assertEquals(
      foodProductService.updateFoodProduct(existingHouseholdFoodProduct),
      existingHouseholdFoodProduct
    );
  }

  @Test
  public void testUpdateFoodProductNewHouseholdFoodProduct() {
    assertThrows(
      NullPointerException.class,
      () -> foodProductService.updateFoodProduct(newHouseholdFoodProduct)
    );
  }

  @Test
  public void testUpdateFoodProductCorruptedHouseholdFoodProduct() {
    assertThrows(
      FoodProductNotFoundException.class,
      () -> foodProductService.updateFoodProduct(corruptedHouseholdFoodProduct)
    );
  }

  @Test
  public void testDeleteFoodProductByIdExistingHouseholdFoodProduct()
    throws FoodProductNotFoundException {
    foodProductService.deleteFoodProductById(existingHouseholdFoodProduct.getId());
  }

  @Test
  public void testDeleteFoodProductByIdNewHouseholdFoodProduct() {
    assertThrows(
      NullPointerException.class,
      () -> foodProductService.deleteFoodProductById(newHouseholdFoodProduct.getId())
    );
  }

  @Test
  public void testDeleteFoodProductByIdCorruptedHouseholdFoodProduct() {
    assertThrows(
      FoodProductNotFoundException.class,
      () -> foodProductService.deleteFoodProductById(corruptedHouseholdFoodProduct.getId())
    );
  }

  @Test
  public void testGetFoodProductByEANExistingHouseholdFoodProduct()
    throws FoodProductNotFoundException {
    assertEquals(
      foodProductService.findHouseholdFoodProductByIdAndEAN(
        existingHouseholdFoodProduct.getHousehold().getId(),
        existingHouseholdFoodProduct.getFoodProduct().getEAN()
      ),
      existingHouseholdFoodProduct
    );
  }
}
