package edu.ntnu.idatt2106.smartmat.dto.household;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for household.
 * Used to transfer household data between layers.
 * @author Callum G.
 * @version 1.0 - 19.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class HouseholdDTO {

  @NonNull
  private String id;

  @NonNull
  private String name;

  @NonNull
  private Set<HouseholdMemberDTO> members;
}
