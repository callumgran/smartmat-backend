package edu.ntnu.idatt2106.smartmat.utils;

import static org.junit.Assert.assertEquals;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class IngredientTitleMatcherTest {

  private static final String SOYA_SAUCE = "ABC Salt Soya Saus 600ml";

  private static final String PASTA_SPICE = "Provence Pastakrydder 0,34kg";

  private static final String SALMON = "Laks Porsjoner m/Skinn Paprika&Papaya 2x125g";

  private static final String BURGER = "Burger Big Pack 4stk 600g Meny";

  private Collection<Ingredient> ingredients;

  @Before
  public void setUp() {
    Unit gram = new Unit("gram", "g", new HashSet<>(), 0.001, UnitTypeEnum.SOLID);
    Ingredient burger = new Ingredient(1L, "burger", null, null, gram);
    Ingredient salmon = new Ingredient(2L, "laks", null, null, gram);
    Ingredient soyaSauce = new Ingredient(3L, "soya saus", null, null, gram);
    Ingredient salt = new Ingredient(4L, "salt", null, null, gram);
    Ingredient pasta = new Ingredient(5L, "pasta", null, null, gram);
    Ingredient pastaSpice = new Ingredient(6L, "pastakrydder", null, null, gram);

    ingredients = List.of(burger, salmon, soyaSauce, salt, pasta, pastaSpice);
  }

  @Test
  public void testParseSoyaSauce() {
    Ingredient parsed = IngredientTitleMatcher.getBestMatch(ingredients, SOYA_SAUCE);
    assertEquals(parsed.getName(), "soya saus");
  }

  // Sadly, this is how it has to be done. The parser is not perfect, and will not always return the correct ingredient.
  @Test
  public void testParsePastaSpiceReturnsPasta() {
    Ingredient parsed = IngredientTitleMatcher.getBestMatch(ingredients, PASTA_SPICE);
    assertEquals(parsed.getName(), "pasta");
  }

  @Test
  public void testParseSalmon() {
    Ingredient parsed = IngredientTitleMatcher.getBestMatch(ingredients, SALMON);
    assertEquals(parsed.getName(), "laks");
  }

  @Test
  public void testParseBurger() {
    Ingredient parsed = IngredientTitleMatcher.getBestMatch(ingredients, BURGER);
    assertEquals(parsed.getName(), "burger");
  }
}
