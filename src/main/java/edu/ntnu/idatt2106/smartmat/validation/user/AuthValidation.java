package edu.ntnu.idatt2106.smartmat.validation.user;

import edu.ntnu.idatt2106.smartmat.model.user.Role;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.validation.BaseValidation;

/**
 * Class for validation of the Auth class.
 * Not actual authentication validation.
 * This class is used to validate objects of the Auth class.
 * @author Callum G.
 * @version 1.0 - 18.04.2023
 */
public class AuthValidation extends BaseValidation {

  /**
   * Validates the Auth object.
   * @param auth The Auth object to validate.
   * @return True if the Auth object is valid, false otherwise.
   */
  public static boolean validateAuth(Auth auth) {
    if (auth == null) return false;

    return auth.getRole() != null && UserValidation.validateUsername(auth.getUsername());
  }

  /**
   * Validates the Auth object and checks if the Auth object has the correct role.
   * @param auth The Auth object to validate.
   * @param role The role to check for.
   * @return True if the Auth object is valid and has the correct role, false otherwise.
   */
  public static boolean hasRole(Auth auth, Role role) {
    if (validateAuth(auth)) return auth.getRole() == role;

    return false;
  }

  /**
   * Validates the Auth object and checks if the Auth object has the correct role or is the correct user.
   * @param auth The Auth object to validate.
   * @param role The role to check for.
   * @param username The username to check for.
   * @return True if the Auth object is valid and has the correct role or is the correct user, false otherwise.
   */
  public static boolean hasRoleOrIsUser(Auth auth, Role role, String username) {
    if (validateAuth(auth)) return auth.getRole() == role || auth.getUsername().equals(username);

    return false;
  }

  /**
   * Validates the Auth object and checks that the Auth object is not a specified user.
   * @param auth The Auth object to validate.
   * @param username The username to check for.
   * @return True if the Auth object is valid and is not the specified user, false otherwise.
   */
  public static boolean isNotUser(Auth auth, String username) {
    if (validateAuth(auth)) return !auth.getUsername().equals(username);

    return false;
  }
}
