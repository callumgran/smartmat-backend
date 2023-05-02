package edu.ntnu.idatt2106.smartmat.utils;

import edu.ntnu.idatt2106.smartmat.model.unit.Unit;

/**
 * Tuple class for unit and amount
 *
 * @author Callum G.
 * @version 1.0 02.05.2023
 */
public class UnitAmountTuple {

  private final Unit unit;
  private final Double amount;

  /**
   * Constructor for UnitAmountTuple
   *
   * @param unit   The unit
   * @param amount The amount
   */
  public UnitAmountTuple(Unit unit, Double amount) {
    this.unit = unit;
    this.amount = amount;
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
   * Gets the amount
   *
   * @return The amount
   */
  public Double getAmount() {
    return amount;
  }
}
