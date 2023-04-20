package edu.ntnu.idatt2106.smartmat.dto.household;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Class used to create a household.
 * @author Callum G.
 * @version 1.0 - 19.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateHouseholdDTO {

  @NonNull
  private String name;
}
