package edu.ntnu.idatt2106.smartmat.unit.ingredient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import org.junit.Test;

/**
 * Unit test for the ingredient model.
 */
public class IngredientTest {

  private final Long id = 1L;
  private final String name = "Carrot";

  @Test
  public void testIngredientConstructor() {
    Ingredient ingredient = new Ingredient(id, name, null, null, null, null);

    assertEquals(id, ingredient.getId());
    assertEquals(name, ingredient.getName());
  }

  @Test
  public void testIngredientConstructorWithNulls() {
    try {
      new Ingredient(1L, null, null, null, null, null);
      fail();
    } catch (NullPointerException e) {
      assertEquals(NullPointerException.class, e.getClass());
    }
  }

  @Test
  public void testIngredientSetters() {
    final Long id = 1L;
    final String name = "Carrot";
    Ingredient ingredient = new Ingredient();
    ingredient.setId(id);
    ingredient.setName(name);

    assertEquals(id, ingredient.getId());
    assertEquals(name, ingredient.getName());
  }

  @Test
  public void testIngredientSettersWithNulls() {
    try {
      Ingredient ingredient = new Ingredient();
      ingredient.setName(null);
      fail();
    } catch (NullPointerException e) {
      assertEquals(NullPointerException.class, e.getClass());
    }
  }
}
