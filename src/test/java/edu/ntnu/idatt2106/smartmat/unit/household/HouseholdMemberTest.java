package edu.ntnu.idatt2106.smartmat.unit.household;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import org.junit.Test;
import org.junit.jupiter.api.Nested;

public class HouseholdMemberTest {

  HouseholdMember householdMember;

  @Nested
  public class HouseholdMemberConstructorTest {

    @Test
    public void testConstructor() {
      householdMember = new HouseholdMember(new Household(), new User(), HouseholdRole.OWNER);

      // Not checking the household and user, since they are tested in their own tests and don't have equals methods.
      assertEquals(HouseholdRole.OWNER, householdMember.getHouseholdRole());
    }

    @Test
    public void testConstructorWithNulls() {
      assertThrows(NullPointerException.class, () -> new HouseholdMember(null, null, null));
    }
  }

  @Nested
  public class HouseholdMemberSettersTest {

    @Test
    public void testSetters() {
      householdMember = new HouseholdMember();
      final Household household = new Household();
      final User user = new User();
      final HouseholdRole role = HouseholdRole.OWNER;
      householdMember.setHousehold(household);
      householdMember.setUser(user);
      householdMember.setHouseholdRole(role);

      assertEquals(household, householdMember.getHousehold());
      assertEquals(user, householdMember.getUser());
      assertEquals(role, householdMember.getHouseholdRole());
    }

    @Test
    public void testSetRoleWithNulls() {
      householdMember = new HouseholdMember();
      assertDoesNotThrow(() -> householdMember.setHouseholdRole(null));
    }

    @Test
    public void testSetHouseholdWithNulls() {
      householdMember = new HouseholdMember();
      assertDoesNotThrow(() -> householdMember.setHousehold(null));
    }

    @Test
    public void testSetUserWithNulls() {
      householdMember = new HouseholdMember();
      assertDoesNotThrow(() -> householdMember.setUser(null));
    }
  }
}
