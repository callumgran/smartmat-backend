package edu.ntnu.idatt2106.smartmat.helperfunctions;

import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import java.util.HashSet;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Helper functions for endpoint tests.
 * @author Callum G.
 * @version 1.0 - 18.04.2023
 */
public class TestUserHelperFunctions {

  /**
   * Creates a UsernamePasswordAuthenticationToken from a user.
   * @param user The user to create the token from.
   * @return The created token.
   */
  public static UsernamePasswordAuthenticationToken createAuthenticationToken(User user) {
    return new UsernamePasswordAuthenticationToken(
      new Auth(user.getUsername(), user.getRole()),
      null,
      user.getAuthorities()
    );
  }

  public static User testUserFactory(TestUserEnum userType) {
    switch (userType) {
      case GOOD:
        return new User(
          "testusername",
          "test@test.com",
          "Test",
          "Test",
          "Testpassword1",
          UserRole.USER,
          new HashSet<>()
        );
      case BAD:
        return new User(
          "badusername",
          "bad@bad.com",
          "Bad",
          "Bad",
          "Badpassword1",
          UserRole.USER,
          new HashSet<>()
        );
      case ADMIN:
        return new User(
          "adminusername",
          "admin@admin.com",
          "Admin",
          "Admin",
          "Adminpassword1",
          UserRole.ADMIN,
          new HashSet<>()
        );
      case BAD_EMAIL:
        return new User(
          "bademail",
          "badEmail",
          "Bademail",
          "Bademail",
          "Bademailpassword1",
          UserRole.USER,
          new HashSet<>()
        );
      case BAD_PASSWORD:
        return new User(
          "badpassword",
          "badpassword@badpassword.com",
          "Badpassword",
          "Badpassword",
          "badpass",
          UserRole.USER,
          new HashSet<>()
        );
      case NEW:
        return new User(
          "newusername",
          "new@new.com",
          "New",
          "New",
          "Newpassword1",
          UserRole.USER,
          new HashSet<>()
        );
      case UPDATE:
        return new User(
          "updateusername",
          "update@update.com",
          "Update",
          "Update",
          "Updatepassword1",
          UserRole.USER,
          new HashSet<>()
        );
      default:
        return null;
    }
  }
}
