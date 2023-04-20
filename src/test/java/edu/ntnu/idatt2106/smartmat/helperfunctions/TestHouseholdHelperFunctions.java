package edu.ntnu.idatt2106.smartmat.helperfunctions;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;

import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import java.util.HashSet;
import java.util.UUID;

/**
 * Helper functions for household tests.
 * @author Callum G.
 * @version 1.0 - 19.4.2023
 */
public class TestHouseholdHelperFunctions {

  public static enum TestHouseholdNameEnum {
    GOOD_NAME("Good name"),
    BAD_NAME("Bad name"),
    NULL_NAME(null);

    private final String name;

    /**
     * Constructor for the enum.
     * @param name The name of the household.
     */
    TestHouseholdNameEnum(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  /**
   * Creates a household with the given name and number of members.
   * @param householdName The name of the household.
   * @param numberOfMembers The number of members to add to the household.
   * @return The created household.
   */
  private static Household createHousehold(String householdName, int numberOfMembers) {
    Household household = new Household();
    household.setId(UUID.randomUUID());
    household.setName("TestHousehold");
    household.setMembers(new HashSet<HouseholdMember>());
    for (int i = 0; i < numberOfMembers; i++) {
      User user = testUserFactory(TestUserEnum.GOOD);
      user.setUsername(user.getUsername() + i);
      user.setEmail(user.getEmail() + i);
      HouseholdMember member = new HouseholdMember();
      member.setHousehold(household);
      member.setUser(user);
      if (i == 0) member.setHouseholdRole(HouseholdRole.OWNER); else if (
        i < 5
      ) member.setHouseholdRole(HouseholdRole.PRIVILEGED_MEMBER); else member.setHouseholdRole(
        HouseholdRole.MEMBER
      );
      household.getMembers().add(member);
      user.getHouseholds().add(member);
    }
    return household;
  }

  /**
   * Creates a household based on the given type.
   * @param householdType The type of household to create.
   * @return The created household.
   */
  public static Household testHouseholdFactory(TestHouseholdEnum householdType) {
    switch (householdType) {
      case GOOD_HOUSEHOLD:
        return createHousehold(TestHouseholdNameEnum.GOOD_NAME.toString(), 0);
      case NEW_HOUSEHOLD:
        return new Household(TestHouseholdNameEnum.GOOD_NAME.toString());
      case BAD_HOUSEHOLD:
        return createHousehold(TestHouseholdNameEnum.BAD_NAME.toString(), 0);
      case NULL_HOUSEHOLD:
        return null;
      case NULL_ID:
        Household household = createHousehold(TestHouseholdNameEnum.GOOD_NAME.toString(), 0);
        household.setId(null);
        return household;
      case NULL_NAME:
        household = createHousehold(TestHouseholdNameEnum.NULL_NAME.toString(), 0);
        return household;
      case NULL_USERS:
        household = createHousehold(TestHouseholdNameEnum.GOOD_NAME.toString(), 0);
        household.setMembers(null);
        return household;
      case NO_USERS:
        return createHousehold(TestHouseholdNameEnum.GOOD_NAME.toString(), 0);
      case ONE_USER:
        return createHousehold(TestHouseholdNameEnum.GOOD_NAME.toString(), 1);
      case MANY_USERS:
        return createHousehold(TestHouseholdNameEnum.GOOD_NAME.toString(), 10);
      default:
        return null;
    }
  }
}
