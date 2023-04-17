package edu.ntnu.idatt2106.smartmat.security;

import edu.ntnu.idatt2106.smartmat.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for authorization.
 * Contains username and role and is used to create a JWT token.
 * @author Callum G.
 * @version 1.0 - 17.04.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Auth {

  private String username;

  private Role role;
}
