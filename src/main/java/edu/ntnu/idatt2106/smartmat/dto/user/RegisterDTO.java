package edu.ntnu.idatt2106.smartmat.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for registration.
 * Used to transfer registration data between the backend and the application.
 * From the IDATT2105 project.
 * @author Callum G., Carl G.
 * @version 1.0 - 17.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterDTO {

  @NonNull
  @NotBlank
  private String username;

  @NonNull
  @NotBlank
  private String email;

  @NonNull
  @NotBlank
  private String firstName;

  @NonNull
  @NotBlank
  private String lastName;

  @NonNull
  @NotBlank
  private String password;
}
