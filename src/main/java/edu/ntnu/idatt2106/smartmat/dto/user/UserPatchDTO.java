package edu.ntnu.idatt2106.smartmat.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for user patch.
 * Used to transfer user patch data between the backend and the application.
 * From the IDATT2105 project.
 * @author Nicolai H. B.
 * @version 1.0 - 17.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class UserPatchDTO {

  private String email;

  private String firstName;

  private String lastName;

  private String oldPassword;

  private String newPassword;
}
