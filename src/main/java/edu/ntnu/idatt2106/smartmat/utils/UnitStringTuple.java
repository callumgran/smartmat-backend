package edu.ntnu.idatt2106.smartmat.utils;

import edu.ntnu.idatt2106.smartmat.model.unit.Unit;

/**
 * Tuple class for unit and string
 *
 * @author Callum G.
 * @version 1.0 02.05.2023
 */
public class UnitStringTuple {

  private final Unit unit;
  private final String string;

  /**
   * Constructor for UnitStringTuple
   *
   * @param unit   The unit
   * @param string The string
   */
  public UnitStringTuple(Unit unit, String string) {
    this.unit = unit;
    this.string = string;
  }

  /**
   * Gets the unit
   *
   * @return The unit
   */
  public Unit getUnit() {
    return unit;
  }

  /**
   * Gets the string
   *
   * @return The string
   */
  public String getString() {
    return string;
  }
}
