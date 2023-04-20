package edu.ntnu.idatt2106.smartmat.dto.user;

import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdMemberDTO;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for user.
 * Used to transfer user data between the backend and the application.
 * From the IDATT2105 project.
 * @author Callum G.
 * @version 1.0 - 17.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDTO {

  @NonNull
  private String username;

  @NonNull
  private String email;

  @NonNull
  private String firstName;

  @NonNull
  private String lastName;

  @NonNull
  private UserRole role;

  @NonNull
  private List<HouseholdMemberDTO> households;
}
