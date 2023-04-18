package edu.ntnu.idatt2106.smartmat.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2106.smartmat.model.user.Role;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.validation.user.AuthValidation;
import org.junit.Before;
import org.junit.Test;

public class AuthValidationTest {

  private Auth goodAuth;

  private Auth badAuth;

  private Auth adminAuth;

  private final String adminUsername = "admin";
  private final String goodUsername = "goodUsername";
  private final String badUsername = "badUsername";

  @Before
  public void setUp() {
    goodAuth = new Auth(goodUsername, Role.USER);
    badAuth = null;
    adminAuth = new Auth(adminUsername, Role.ADMIN);
  }

  @Test
  public void validateAuthReturnsTrueOnGoodAuth() {
    assertTrue(AuthValidation.validateAuth(goodAuth));
  }

  @Test
  public void validateAuthReturnsFalseOnNull() {
    assertFalse(AuthValidation.validateAuth(badAuth));
  }

  @Test
  public void testIsAdminReturnsTrueOnAdmin() {
    assertTrue(AuthValidation.hasRole(adminAuth, Role.ADMIN));
  }

  @Test
  public void testIsAdminReturnsFalseOnUser() {
    assertFalse(AuthValidation.hasRole(goodAuth, Role.ADMIN));
  }

  @Test
  public void testHasRoleOrIsUserReturnsTrueOnUser() {
    assertTrue(AuthValidation.hasRoleOrIsUser(goodAuth, Role.ADMIN, goodUsername));
  }

  @Test
  public void testHasRoleOrIsUserReturnsTrueOnAdmin() {
    assertTrue(AuthValidation.hasRoleOrIsUser(adminAuth, Role.ADMIN, goodUsername));
  }

  @Test
  public void testHasRoleOrIsUserReturnsFalseOnUser() {
    assertFalse(AuthValidation.hasRoleOrIsUser(badAuth, Role.ADMIN, goodUsername));
  }

  @Test
  public void testIsNotUserReturnsTrueOnUser() {
    assertTrue(AuthValidation.isNotUser(goodAuth, badUsername));
  }

  @Test
  public void testIsNotUserReturnsFalseOnUser() {
    assertFalse(AuthValidation.isNotUser(goodAuth, goodUsername));
  }
}
