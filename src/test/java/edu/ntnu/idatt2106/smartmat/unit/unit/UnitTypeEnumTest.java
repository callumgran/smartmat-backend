package edu.ntnu.idatt2106.smartmat.unit.unit;

import static org.junit.Assert.assertTrue;

import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;
import java.util.HashSet;
import org.junit.Test;

public class UnitTypeEnumTest {

  Unit unit = new Unit("kilogram", "kg", new HashSet<>(), 1, UnitTypeEnum.SOLID, new HashSet<>());
  Unit l = new Unit("liter", "l", new HashSet<>(), 1, UnitTypeEnum.LIQUID, new HashSet<>());
  Unit piece = new Unit("stykk", "stk", new HashSet<>(), 1, UnitTypeEnum.BY_PIECE, new HashSet<>());

  @Test
  public void testFromString() {
    assertTrue(UnitTypeEnum.fromString("SOLID") == UnitTypeEnum.SOLID);
    assertTrue(UnitTypeEnum.fromString("LIQUID") == UnitTypeEnum.LIQUID);
    assertTrue(UnitTypeEnum.fromString("BY_PIECE") == UnitTypeEnum.BY_PIECE);
  }

  @Test
  public void testFromStringFail() {
    assertTrue(UnitTypeEnum.fromString("SOLI") == null);
    assertTrue(UnitTypeEnum.fromString("LIQU") == null);
    assertTrue(UnitTypeEnum.fromString("BY_PIE") == null);
  }

  @Test
  public void testToString() {
    assertTrue(UnitTypeEnum.SOLID.toString().equals("SOLID"));
    assertTrue(UnitTypeEnum.LIQUID.toString().equals("LIQUID"));
    assertTrue(UnitTypeEnum.BY_PIECE.toString().equals("BY_PIECE"));
  }

  @Test
  public void testToKg() {
    assertTrue(unit.getUnitType().toKg(1) == 1);
    assertTrue(l.getUnitType().toKg(1) == 1);
    assertTrue(piece.getUnitType().toKg(1) == 0.25);
  }

  @Test
  public void testFromKg() {
    assertTrue(unit.getUnitType().fromKg(1) == 1);
    assertTrue(l.getUnitType().fromKg(1) == 1);
    assertTrue(piece.getUnitType().fromKg(1) == 4);
  }
}
