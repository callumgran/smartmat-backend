package edu.ntnu.idatt2106.smartmat.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ParseUnitTest {

  private static final String SOYA_SAUCE = "ABC Salt Soya Saus 600ml";

  private static final String PASTA_SPICE = "Provence Pastakrydder 0,34kg";

  private static final String SALMON = "Laks Porsjoner m/Skinn Paprika&Papaya 2x125g";

  private static final String BURGER = "Burger Big Pack 4stk 600g Meny";

  private static final String COFFEE = "Pulverkaffe Instant 200g First Price";

  private Collection<Unit> units;

  private Unit defaultUnit;

  @Before
  public void setUp() {
    Unit unit = new Unit("kilogram", "kg", new HashSet<>(), 1, UnitTypeEnum.SOLID, new HashSet<>());
    Unit gram = new Unit("gram", "g", new HashSet<>(), 0.001, UnitTypeEnum.SOLID, new HashSet<>());
    Unit stk = new Unit("stk", "stk", new HashSet<>(), 1, UnitTypeEnum.SOLID, new HashSet<>());
    Unit ml = new Unit(
      "milliliter",
      "ml",
      new HashSet<>(),
      0.001,
      UnitTypeEnum.LIQUID,
      new HashSet<>()
    );
    Unit l = new Unit("liter", "l", new HashSet<>(), 1, UnitTypeEnum.LIQUID, new HashSet<>());
    units = new ArrayList<>(List.of(unit, gram, stk, ml, l));
    defaultUnit = stk;
  }

  @Test
  public void testParseSoyaSauce() {
    UnitAmountTuple parsed = ParseUnit.parseUnit(units, SOYA_SAUCE, defaultUnit);
    assertEquals(parsed.getUnit().getName(), "milliliter");
    assertTrue(parsed.getAmount() == 600);
  }

  @Test
  public void testParsePastaSpice() {
    UnitAmountTuple parsed = ParseUnit.parseUnit(units, PASTA_SPICE, defaultUnit);
    assertEquals(parsed.getUnit().getName(), "kilogram");
    assertTrue(parsed.getAmount() == 0.34);
  }

  @Test
  public void testParseSalmon() {
    UnitAmountTuple parsed = ParseUnit.parseUnit(units, SALMON, defaultUnit);
    assertEquals(parsed.getUnit().getName(), "gram");
    assertTrue(parsed.getAmount() == 250);
  }

  @Test
  public void testParseBurger() {
    UnitAmountTuple parsed = ParseUnit.parseUnit(units, BURGER, defaultUnit);
    assertEquals(parsed.getUnit().getName(), "gram");
    assertTrue(parsed.getAmount() == 600);
  }

  @Test
  public void testParseCoffee() {
    UnitAmountTuple parsed = ParseUnit.parseUnit(units, COFFEE, defaultUnit);
    assertEquals(parsed.getUnit().getName(), "gram");
    assertTrue(parsed.getAmount() == 200);
  }
}
