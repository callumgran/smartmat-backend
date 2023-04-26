package edu.ntnu.idatt2106.smartmat.unit.foodproduct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.Test;

/**
 * Unit test for the household food product model.
 */
public class HouseholdFoodProductTest {

  HouseholdFoodProduct householdFoodProduct;

  @Test
  public void testConstructor() {
    LocalDate localDate = LocalDate.now();
    double amount_left = 5.1;
    householdFoodProduct =
      new HouseholdFoodProduct(
        UUID.randomUUID(),
        new FoodProduct(),
        new Household(),
        localDate,
        amount_left
      );

    // Not checking the household and food product, since they are tested in their own tests and don't have equals methods.
    assertEquals(localDate, householdFoodProduct.getExpirationDate());
    assertTrue(amount_left == householdFoodProduct.getAmountLeft());
  }

  @Test
  public void testSetters() {
    householdFoodProduct = new HouseholdFoodProduct();
    final Household household = new Household();
    final FoodProduct foodProduct = new FoodProduct();
    final LocalDate expirationDate = LocalDate.now();
    final double amountLeft = 0.1;
    householdFoodProduct.setHousehold(household);
    householdFoodProduct.setFoodProduct(foodProduct);
    householdFoodProduct.setExpirationDate(expirationDate);
    householdFoodProduct.setAmountLeft(amountLeft);

    assertEquals(household, householdFoodProduct.getHousehold());
    assertEquals(foodProduct, householdFoodProduct.getFoodProduct());
    assertTrue(amountLeft == householdFoodProduct.getAmountLeft());
    assertEquals(expirationDate, householdFoodProduct.getExpirationDate());
  }

  @Test
  public void testSetExpirationDateWithNull() {
    householdFoodProduct = new HouseholdFoodProduct();
    assertDoesNotThrow(() -> householdFoodProduct.setExpirationDate(null));
  }
}
