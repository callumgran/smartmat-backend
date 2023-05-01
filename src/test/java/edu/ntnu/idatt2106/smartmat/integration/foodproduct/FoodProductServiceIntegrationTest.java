package edu.ntnu.idatt2106.smartmat.integration.foodproduct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.repository.foodproduct.FoodProductRepository;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.FoodProductService;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.FoodProductServiceImpl;
import java.util.HashSet;
import java.util.Optional;
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
public class FoodProductServiceIntegrationTest {

  @TestConfiguration
  static class FoodProductServiceIntegrationTestConfiguration {

    @Bean
    public FoodProductService foodProductService() {
      return new FoodProductServiceImpl();
    }
  }

  @Autowired
  private FoodProductService foodProductService;

  @MockBean
  private FoodProductRepository foodProductRepository;

  private FoodProduct existingFoodProduct;

  private FoodProduct newFoodProduct;

  private FoodProduct corruptedFoodProduct;

  @Before
  public void setUp() throws FoodProductNotFoundException {
    Ingredient carrot = new Ingredient(1L, "Carrot", new HashSet<>(), new HashSet<>(), null);
    existingFoodProduct =
      new FoodProduct(
        1L,
        "Carrot",
        "1234567890123",
        4.0D,
        true,
        null,
        new HashSet<>(),
        carrot,
        null,
        false
      );
    newFoodProduct =
      new FoodProduct(
        null,
        "Raisin",
        "1234567890124",
        2.0D,
        true,
        null,
        new HashSet<>(),
        carrot,
        null,
        false
      );
    corruptedFoodProduct =
      new FoodProduct(
        2L,
        "Raisin",
        "1234567890124",
        2.0D,
        true,
        null,
        new HashSet<>(),
        carrot,
        null,
        false
      );

    when(foodProductRepository.existsById(existingFoodProduct.getId())).thenReturn(true);
    when(foodProductRepository.existsById(newFoodProduct.getId())).thenReturn(false);
    when(foodProductRepository.existsById(corruptedFoodProduct.getId())).thenReturn(false);

    when(foodProductRepository.save(existingFoodProduct)).thenReturn(existingFoodProduct);
    when(foodProductRepository.save(newFoodProduct)).thenReturn(newFoodProduct);
    when(foodProductRepository.save(corruptedFoodProduct)).thenReturn(corruptedFoodProduct);

    when(foodProductRepository.findById(existingFoodProduct.getId()))
      .thenReturn(Optional.of(existingFoodProduct));
    when(foodProductRepository.findById(newFoodProduct.getId())).thenReturn(Optional.empty());
    when(foodProductRepository.findById(corruptedFoodProduct.getId())).thenReturn(Optional.empty());

    doNothing().when(foodProductRepository).deleteById(existingFoodProduct.getId());
    doNothing().when(foodProductRepository).deleteById(newFoodProduct.getId());
    doNothing().when(foodProductRepository).deleteById(corruptedFoodProduct.getId());

    when(foodProductRepository.findByEAN("1234567890123"))
      .thenReturn(Optional.of(existingFoodProduct));
    when(foodProductRepository.findByEAN("1234567890124")).thenReturn(Optional.empty());
  }

  @Test
  public void testExistsByIdExistingFoodProduct() {
    assertTrue(foodProductService.existsById(existingFoodProduct.getId()));
  }

  @Test
  public void testExistsByIdNewFoodProduct() {
    assertThrows(
      NullPointerException.class,
      () -> foodProductService.existsById(newFoodProduct.getId())
    );
  }

  @Test
  public void testExistsByIdCorruptedFoodProduct() {
    assertFalse(foodProductService.existsById(corruptedFoodProduct.getId()));
  }

  @Test
  public void testSaveFoodProductExistingFoodProduct() {
    assertThrows(
      IllegalArgumentException.class,
      () -> foodProductService.saveFoodProduct(existingFoodProduct)
    );
  }

  @Test
  public void testSaveFoodProductNewFoodProduct() {
    assertEquals(foodProductService.saveFoodProduct(newFoodProduct), newFoodProduct);
  }

  @Test
  public void testGetFoodProductByIdExistingFoodProduct() throws FoodProductNotFoundException {
    assertEquals(
      foodProductService.getFoodProductById(existingFoodProduct.getId()),
      existingFoodProduct
    );
  }

  @Test
  public void testGetFoodProductByIdNewFoodProduct() {
    assertThrows(
      NullPointerException.class,
      () -> foodProductService.getFoodProductById(newFoodProduct.getId())
    );
  }

  @Test
  public void testGetFoodProductByIdCorruptedFoodProduct() {
    assertThrows(
      FoodProductNotFoundException.class,
      () -> foodProductService.getFoodProductById(corruptedFoodProduct.getId())
    );
  }

  @Test
  public void testUpdateFoodProductExistingFoodProduct() throws FoodProductNotFoundException {
    assertEquals(foodProductService.updateFoodProduct(existingFoodProduct), existingFoodProduct);
  }

  @Test
  public void testUpdateFoodProductNewFoodProduct() {
    assertThrows(
      NullPointerException.class,
      () -> foodProductService.updateFoodProduct(newFoodProduct)
    );
  }

  @Test
  public void testUpdateFoodProductCorruptedFoodProduct() {
    assertThrows(
      FoodProductNotFoundException.class,
      () -> foodProductService.updateFoodProduct(corruptedFoodProduct)
    );
  }

  @Test
  public void testDeleteFoodProductByIdExistingFoodProduct() throws FoodProductNotFoundException {
    foodProductService.deleteFoodProductById(existingFoodProduct.getId());
  }

  @Test
  public void testDeleteFoodProductByIdNewFoodProduct() {
    assertThrows(
      NullPointerException.class,
      () -> foodProductService.deleteFoodProductById(newFoodProduct.getId())
    );
  }

  @Test
  public void testDeleteFoodProductByIdCorruptedFoodProduct() {
    assertThrows(
      FoodProductNotFoundException.class,
      () -> foodProductService.deleteFoodProductById(corruptedFoodProduct.getId())
    );
  }

  @Test
  public void testGetFoodProductByEANExistingFoodProduct() throws FoodProductNotFoundException {
    assertEquals(
      foodProductService.getFoodProductByEan(existingFoodProduct.getEAN()),
      existingFoodProduct
    );
  }

  @Test
  public void testGetFoodProductByEANNewFoodProduct() {
    assertThrows(
      FoodProductNotFoundException.class,
      () -> foodProductService.getFoodProductByEan(newFoodProduct.getEAN())
    );
  }

  @Test
  public void testGetFoodProductByEANCorruptedFoodProduct() {
    assertThrows(
      FoodProductNotFoundException.class,
      () -> foodProductService.getFoodProductByEan(corruptedFoodProduct.getEAN())
    );
  }
}
