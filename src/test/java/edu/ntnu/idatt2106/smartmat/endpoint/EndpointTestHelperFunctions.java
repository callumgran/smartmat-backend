package edu.ntnu.idatt2106.smartmat.endpoint;

import edu.ntnu.idatt2106.smartmat.model.user.Role;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Helper functions for endpoint tests.
 * @author Callum G.
 * @version 1.0 - 18.04.2023
 */
public class EndpointTestHelperFunctions {

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
          Role.USER
        );
      case BAD:
        return new User("badusername", "bad@bad.com", "Bad", "Bad", "Badpassword1", Role.USER);
      case ADMIN:
        return new User(
          "adminusername",
          "admin@admin.com",
          "Admin",
          "admin",
          "Adminpassword1",
          Role.ADMIN
        );
      case BAD_EMAIL:
        return new User(
          "bademail",
          "badEmail",
          "Bademail",
          "Bademail",
          "Bademailpassword1",
          Role.USER
        );
      case BAD_PASSWORD:
        return new User(
          "badpassword",
          "badpassword@badpassword.com",
          "Badpassword",
          "Badpassword",
          "badpass",
          Role.USER
        );
      case NEW:
        return new User("newusername", "new@new.com", "New", "New", "Newpassword1", Role.USER);
      default:
        return null;
    }
  }
}
