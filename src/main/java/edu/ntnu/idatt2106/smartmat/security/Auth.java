package edu.ntnu.idatt2106.smartmat.security;

import edu.ntnu.idatt2106.smartmat.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for authorization.
 * Used to store the username and role of a user.
 * @author Callum G.
 * @version 1.0 - 18.04.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Auth {

  private String username;

  private Role role;
}
