package edu.ntnu.idatt2106.smartmat.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
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
    goodAuth = new Auth(goodUsername, UserRole.USER);
    badAuth = null;
    adminAuth = new Auth(adminUsername, UserRole.ADMIN);
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
    assertTrue(AuthValidation.hasRole(adminAuth, UserRole.ADMIN));
  }

  @Test
  public void testIsAdminReturnsFalseOnUser() {
    assertFalse(AuthValidation.hasRole(goodAuth, UserRole.ADMIN));
  }

  @Test
  public void testHasRoleOrIsUserReturnsTrueOnUser() {
    assertTrue(AuthValidation.hasRoleOrIsUser(goodAuth, UserRole.ADMIN, goodUsername));
  }

  @Test
  public void testHasRoleOrIsUserReturnsTrueOnAdmin() {
    assertTrue(AuthValidation.hasRoleOrIsUser(adminAuth, UserRole.ADMIN, goodUsername));
  }

  @Test
  public void testHasRoleOrIsUserReturnsFalseOnUser() {
    assertFalse(AuthValidation.hasRoleOrIsUser(badAuth, UserRole.ADMIN, goodUsername));
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
