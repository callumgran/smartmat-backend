package edu.ntnu.idatt2106.smartmat.unit.user;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

/**
 * Unit test for the user model.
 * Based on the user-model-test class from the IDATT2105 project.
 */
public class UserTest {

  User user;

  @Test
  public void testUserConstructor() {
    user =
      new User(
        "username",
        "email",
        "firstName",
        "lastName",
        "password",
        UserRole.USER,
        new HashSet<>()
      );

    assertEquals("username", user.getUsername());
    assertEquals("password", user.getPassword());
    assertEquals("email", user.getEmail());
    assertEquals("firstName", user.getFirstName());
    assertEquals("lastName", user.getLastName());
    assertEquals(UserRole.USER, user.getRole());
  }

  @Test
  public void testUserConstructorWithNulls() {
    assertThrows(
      NullPointerException.class,
      () -> new User("username", null, null, null, "password", UserRole.USER, new HashSet<>())
    );
  }

  @Test
  public void testUserSetters() {
    user = new User();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail("email");
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setRole(UserRole.USER);

    assertEquals("username", user.getUsername());
    assertEquals("password", user.getPassword());
    assertEquals("email", user.getEmail());
    assertEquals("firstName", user.getFirstName());
    assertEquals("lastName", user.getLastName());
    assertEquals(UserRole.USER, user.getRole());
  }

  @Test
  public void testUserSetEmailWithNull() {
    user = new User();
    assertThrows(NullPointerException.class, () -> user.setEmail(null));
  }

  @Test
  public void testUserSetUsernameWithNull() {
    user = new User();
    assertThrows(NullPointerException.class, () -> user.setUsername(null));
  }

  @Test
  public void testUserSetPasswordWithNull() {
    user = new User();
    assertThrows(NullPointerException.class, () -> user.setPassword(null));
  }

  @Test
  public void testUserSetFirstNameWithNull() {
    user = new User();
    assertThrows(NullPointerException.class, () -> user.setFirstName(null));
  }

  @Test
  public void testUserSetLastNameWithNull() {
    user = new User();
    assertThrows(NullPointerException.class, () -> user.setLastName(null));
  }

  @Test
  public void testUserSetRoleWithNull() {
    user = new User();
    assertThrows(NullPointerException.class, () -> user.setRole(null));
  }

  @Test
  public void testUserSetMembersWithNull() {
    user = new User();
    assertDoesNotThrow(() -> user.setHouseholds(null));
  }
}
