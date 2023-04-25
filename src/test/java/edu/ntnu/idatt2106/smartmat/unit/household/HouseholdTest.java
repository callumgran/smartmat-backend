package edu.ntnu.idatt2106.smartmat.unit.household;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import java.util.HashSet;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit test for the user model.
 */
public class HouseholdTest {

  Household household;

  @Test
  public void testHouseholdConstructor() {
    final String householdName = "householdName";
    household = new Household(householdName);

    assertEquals(householdName, household.getName());
  }

  @Test
  public void testHouseholdConstructorWithNulls() {
    try {
      household = new Household(null, null, null, null, null, null);
      fail();
    } catch (NullPointerException e) {
      assertEquals(NullPointerException.class, e.getClass());
    }
  }

  @Test
  public void testHouseholdSetters() {
    household = new Household();
    final UUID tmp = UUID.randomUUID();
    final String householdName = "householdName";
    final HashSet<HouseholdMember> members = new HashSet<HouseholdMember>();
    household.setId(tmp);
    household.setName(householdName);
    household.setMembers(members);

    assertEquals(tmp, household.getId());
    assertEquals(householdName, household.getName());
    assertEquals(members, household.getMembers());
  }

  @Test
  public void testHouseholdSetNameWithNull() {
    household = new Household();
    assertThrows(NullPointerException.class, () -> household.setName(null));
  }

  @Test
  public void testHouseholdSetIdWithNull() {
    household = new Household();
    assertDoesNotThrow(() -> household.setId(null));
  }

  @Test
  public void testHouseholdSetMembersWithNull() {
    household = new Household();
    assertDoesNotThrow(() -> household.setMembers(null));
  }
}
