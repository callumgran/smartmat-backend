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
  protected static UsernamePasswordAuthenticationToken createAuthenticationToken(User user) {
    return new UsernamePasswordAuthenticationToken(
      new Auth(user.getUsername(), user.getRole()),
      null,
      user.getAuthorities()
    );
  }

  protected static User testUserFactory(TestUserEnum userType) {
    switch (userType) {
      case GOOD:
        return new User("test", "test@test.com", "test", "test", "test", Role.USER);
      case BAD:
        return new User("bad", "bad@bad.com", "bad", "bad", "bad", Role.USER);
      case ADMIN:
        return new User("admin", "admin@admin.com", "admin", "admin", "admin", Role.ADMIN);
      case BAD_EMAIL:
        return new User("badEmail", "badEmail", "badEmail", "badEmail", "badEmail", Role.USER);
      case BAD_PASSWORD:
        return new User(
          "badPassword",
          "bad@bad.com",
          "badPassword",
          "badPassword",
          "badPassword",
          Role.USER
        );
      case NEW:
        return new User("new", "new@new.com", "new", "new", "new", Role.USER);
      default:
        return null;
    }
  }
}
