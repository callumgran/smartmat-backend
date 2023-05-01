package edu.ntnu.idatt2106.smartmat.unit.foodproduct;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit test for the food product model.
 */
public class FoodProductTest {

  @Nested
  public class FoodProductConstructorTest {

    FoodProduct foodProduct;

    @Test
    public void testFoodProductConstructor() {
      String foodProductName = "foodProductName";
      foodProduct = new FoodProduct(foodProductName);

      assertEquals(foodProductName, foodProduct.getName());
    }

    @Test
    public void testFoodProductConstructorWithNulls() {
      try {
        foodProduct = new FoodProduct(1L, null, null, null, false, null, null, null, null, false);
        fail();
      } catch (NullPointerException e) {
        assertEquals(NullPointerException.class, e.getClass());
      }
    }
  }

  @Nested
  public class FoodProductSettersTest {

    FoodProduct foodProduct;

    @Test
    public void testFoodProductSetters() {
      foodProduct = new FoodProduct();
      final long tmp = 1;
      final String foodProductName = "foodProductName";
      final boolean looseWeight = false;
      final String EAN = "EAN";
      final double amount = 10.1;
      final Set<HouseholdFoodProduct> households = new HashSet<>();
      final Ingredient ingredient = new Ingredient(1L, "ingredient", null, null, null);

      foodProduct.setId(tmp);
      foodProduct.setName(foodProductName);
      foodProduct.setLooseWeight(looseWeight);
      foodProduct.setEAN(EAN);
      foodProduct.setAmount(amount);
      foodProduct.setHouseholds(households);
      foodProduct.setIngredient(ingredient);

      assertEquals(tmp, foodProduct.getId());
      assertEquals(foodProductName, foodProduct.getName());
      assertEquals(households, foodProduct.getHouseholds());
      assertEquals(looseWeight, foodProduct.isLooseWeight());
      assertEquals(EAN, foodProduct.getEAN());
      assertEquals(amount, foodProduct.getAmount());
      assertEquals(ingredient, foodProduct.getIngredient());
    }

    @Test
    public void testFoodProductSetNameWithNull() {
      foodProduct = new FoodProduct();
      assertThrows(NullPointerException.class, () -> foodProduct.setName(null));
    }

    @Test
    public void testFoodProductSetAmountWithNull() {
      foodProduct = new FoodProduct();

      assertDoesNotThrow(() -> foodProduct.setAmount(null));
    }

    @Test
    public void testFoodProductSetHouseholdsWithNull() {
      foodProduct = new FoodProduct();

      assertDoesNotThrow(() -> foodProduct.setHouseholds(null));
    }

    @Test
    public void testFoodProductSetIngredientWithNull() {
      foodProduct = new FoodProduct();

      assertDoesNotThrow(() -> foodProduct.setIngredient(null));
    }

    @Test
    public void testFoodProductSetEANWithNull() {
      foodProduct = new FoodProduct();

      assertDoesNotThrow(() -> foodProduct.setEAN(null));
    }
  }
}
