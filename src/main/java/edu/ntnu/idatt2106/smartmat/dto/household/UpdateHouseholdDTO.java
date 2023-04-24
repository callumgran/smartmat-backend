package edu.ntnu.idatt2106.smartmat.dto.household;

import jakarta.validation.constraints.NotBlank;
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
  @NotBlank
  private String id;

  @NonNull
  @NotBlank
  private String name;
}
