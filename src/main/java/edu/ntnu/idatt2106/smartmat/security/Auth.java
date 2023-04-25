package edu.ntnu.idatt2106.smartmat.security;

import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class for authorization.
 * Used to store the username and role of a user.
 * @author Callum G.
 * @version 1.0 - 18.04.2023
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Auth {

  private String username;

  private UserRole role;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Auth auth = (Auth) o;
    return username.equals(auth.username) && role == auth.role;
  }
}
