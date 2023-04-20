package edu.ntnu.idatt2106.smartmat.dto.household;

import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for household member.
 * Used to transfer household member data between layers.
 * @author Callum G.
 * @version 1.0 - 19.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class HouseholdMemberDTO {

  @NonNull
  private UUID household;

  @NonNull
  private String username;

  @NonNull
  private HouseholdRole householdRole;
}
