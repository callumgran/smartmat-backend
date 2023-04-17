package edu.ntnu.idatt2106.smartmat.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import edu.ntnu.idatt2106.smartmat.model.user.Role;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class UserTest {

  @Nested
  public class UserConstructorTest {

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
          Role.USER
        );

      assertEquals("username", user.getUsername());
      assertEquals("password", user.getPassword());
      assertEquals("email", user.getEmail());
      assertEquals("firstName", user.getFirstName());
      assertEquals("lastName", user.getLastName());
      assertEquals(Role.USER, user.getRole());
    }

    @Test
    public void testUserConstructorWithNulls() {
      try {
        user =
          new User(
            "username",
            "password",
            null,
            null,
            null,
            Role.USER
          );
        fail();
      } catch (NullPointerException e) {
        assertEquals(NullPointerException.class, e.getClass());
      }
    }
  }

  @Nested
  public class UserSettersTest {

    User user;

    @Test
    public void testUserSetters() {
      user = new User();
      user.setUsername("username");
      user.setPassword("password");
      user.setEmail("email");
      user.setFirstName("firstName");
      user.setLastName("lastName");
      user.setRole(Role.USER);

      assertEquals("username", user.getUsername());
      assertEquals("password", user.getPassword());
      assertEquals("email", user.getEmail());
      assertEquals("firstName", user.getFirstName());
      assertEquals("lastName", user.getLastName());
      assertEquals(Role.USER, user.getRole());
    }

    @Test
    public void testUserSetEmailWithNull() {
      user = new User();
      try {
        user.setEmail(null);
        fail();
      } catch (NullPointerException e) {
        assertEquals(NullPointerException.class, e.getClass());
      }
    }

    @Test
    public void testUserSetUsernameWithNull() {
      user = new User();
      try {
        user.setUsername(null);
        fail();
      } catch (NullPointerException e) {
        assertEquals(NullPointerException.class, e.getClass());
      }
    }

    @Test
    public void testUserSetPasswordWithNull() {
      user = new User();
      try {
        user.setPassword(null);
        fail();
      } catch (NullPointerException e) {
        assertEquals(NullPointerException.class, e.getClass());
      }
    }

    @Test
    public void testUserSetFirstNameWithNull() {
      user = new User();
      try {
        user.setFirstName(null);
        fail();
      } catch (NullPointerException e) {
        assertEquals(NullPointerException.class, e.getClass());
      }
    }

    @Test
    public void testUserSetLastNameWithNull() {
      user = new User();
      try {
        user.setLastName(null);
        fail();
      } catch (NullPointerException e) {
        assertEquals(NullPointerException.class, e.getClass());
      }
    }

    @Test
    public void testUserSetRoleWithNull() {
      user = new User();
      try {
        user.setRole(null);
        fail();
      } catch (NullPointerException e) {
        assertEquals(NullPointerException.class, e.getClass());
      }
    }
  }
}

