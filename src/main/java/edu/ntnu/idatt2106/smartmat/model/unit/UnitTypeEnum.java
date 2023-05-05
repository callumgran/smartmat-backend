package edu.ntnu.idatt2106.smartmat.model.unit;

/**
 * Enum for the unit type of a food product
 * SOLID = solid food product
 * LIQUID = liquid food product
 * BY_PIECE = food product measured by piece
 * BY_WEIGHT = food product measured by weight
 * TEASPOON = food product measured by teaspoon
 * TABLESPOON = food product measured by tablespoon
 * @author Callum G.
 * @version 1.0 28.04.2023
 */
public enum UnitTypeEnum {
  SOLID,
  LIQUID,
  BY_PIECE;

  private static final double BY_PIECE_TO_KG = 0.25;

  /**
   * Method to get the unit type as a string
   * @return The unit type as a string
   */
  public static UnitTypeEnum fromString(String unitType) {
    switch (unitType) {
      case "SOLID":
        return SOLID;
      case "LIQUID":
        return LIQUID;
      case "BY_PIECE":
        return BY_PIECE;
      default:
        return null;
    }
  }

  /**
   * Method to get the unit type as a string
   * @return The unit type as a string
   */
  public String toString() {
    switch (this) {
      case SOLID:
        return "SOLID";
      case LIQUID:
        return "LIQUID";
      case BY_PIECE:
        return "BY_PIECE";
      default:
        return null;
    }
  }

  /**
   * Method to convert the unit type to kg
   * Standard conversion factor is 1
   * For BY_PIECE, the conversion factor is 4 as a general rule for pieces of food is 250g
   * @param toNormalFormConversionFactor The conversion factor from the unit to the normal form of the unit
   * @return The unit type as kg
   */
  public double toKg(double toNormalFormConversionFactor) {
    switch (this) {
      case SOLID:
        return 1 * toNormalFormConversionFactor;
      case LIQUID:
        return 1 * toNormalFormConversionFactor;
      case BY_PIECE:
        return BY_PIECE_TO_KG * toNormalFormConversionFactor;
      default:
        return 1 * toNormalFormConversionFactor;
    }
  }

  /**
   * Method to convert the unit type from kg
   * Standard conversion factor is 1
   * For BY_PIECE, the conversion factor is 4 as a general rule for pieces of food is 250g
   * @param fromNormalFormConversionFactor The conversion factor from the normal form of the unit to the unit
   * @return The unit type from kg
   */
  public double fromKg(double fromNormalFormConversionFactor) {
    switch (this) {
      case SOLID:
        return 1 / fromNormalFormConversionFactor;
      case LIQUID:
        return 1 / fromNormalFormConversionFactor;
      case BY_PIECE:
        return 4 / (fromNormalFormConversionFactor);
      default:
        return 1 / fromNormalFormConversionFactor;
    }
  }
}
