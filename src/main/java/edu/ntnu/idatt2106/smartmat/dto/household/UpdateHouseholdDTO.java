package edu.ntnu.idatt2106.smartmat.dto.household;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Class used to update a household.
 * @author Callum G.
 * @version 1.0 - 19.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateHouseholdDTO {

  @NonNull
  private String id;

  @NonNull
  private String name;
}
