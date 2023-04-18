package edu.ntnu.idatt2106.smartmat.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for authentication.
 * Used to transfer login data between the backend and the application.
 * From the IDATT2105 project.
 * @author Callum G.
 * @version 1.0 - 17.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AuthenticateDTO {

  @NonNull
  private String username;

  @NonNull
  private String password;
}
