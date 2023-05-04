package edu.ntnu.idatt2106.smartmat.utils;

import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.validation.user.AuthValidation;
import java.util.UUID;

/**
 * Utility class for checking privileges
 *
 * @author Callum G.
 * @version 1.0 03.05.2023
 */
public class PrivilegeUtil {

  /**
   * Checks if the user is an admin or a household member
   *
   * @param auth            The auth object
   * @param householdId     The household id
   * @param householdService The household service
   * @return True if the user is an admin or a household member
   * @throws UserDoesNotExistsException If the user does not exist
   * @throws HouseholdNotFoundException If the household does not exist
   * @throws NullPointerException      If the auth object is null
   */
  public static boolean isAdminOrHouseholdMember(
    Auth auth,
    UUID householdId,
    HouseholdService householdService
  ) throws UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    return (
      AuthValidation.hasRole(auth, UserRole.ADMIN) ||
      householdService.isHouseholdMember(householdId, auth.getUsername())
    );
  }

  /**
   * Checks if the user is an admin or a household privileged member
   *
   * @param auth            The auth object
   * @param householdId     The household id
   * @param householdService The household service
   * @return True if the user is an admin or a household privileged member
   * @throws UserDoesNotExistsException If the user does not exist
   * @throws HouseholdNotFoundException If the household does not exist
   * @throws NullPointerException      If the auth object is null
   */
  public static boolean isAdminOrHouseholdPrivileged(
    Auth auth,
    UUID householdId,
    HouseholdService householdService
  ) throws UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    return (
      householdService.isHouseholdMemberWithRole(
        householdId,
        auth.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      ) ||
      isAdminOrHouseholdOwner(auth, householdId, householdService)
    );
  }

  /**
   * Checks if the user is an admin or a household owner
   *
   * @param auth            The auth object
   * @param householdId     The household id
   * @param householdService The household service
   * @return True if the user is an admin or a household owner
   * @throws UserDoesNotExistsException If the user does not exist
   * @throws HouseholdNotFoundException If the household does not exist
   * @throws NullPointerException      If the auth object is null
   */
  public static boolean isAdminOrHouseholdOwner(
    Auth auth,
    UUID householdId,
    HouseholdService householdService
  ) throws UserDoesNotExistsException, HouseholdNotFoundException, NullPointerException {
    return (
      AuthValidation.hasRole(auth, UserRole.ADMIN) ||
      householdService.isHouseholdOwner(householdId, auth.getUsername())
    );
  }
}
