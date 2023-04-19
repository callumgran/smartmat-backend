package edu.ntnu.idatt2106.smartmat.model.household;

/**
 * Enum representing the role of a user in a household.
 * @author Callum G.
 * @version 1.0 - 18.4.2023
 */
public enum HouseholdRole {
  /**
   * The owner of the household.
   */
  OWNER,
  /**
   * A privileged member of the household.
   * Privileged members can add and remove items from the shopping list.
   */
  PRIVILEGED_MEMBER,
  /**
   * A regular member of the household.
   * Regular members can only view the shopping list and come up with suggestions.
   */
  MEMBER,
}
