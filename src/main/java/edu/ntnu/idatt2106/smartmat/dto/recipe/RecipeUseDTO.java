package edu.ntnu.idatt2106.smartmat.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for using Recipe.
 * @author Simen G. & Callum G.
 * @version 1.0 - 03.05.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeUseDTO {

  @NotBlank
  private int portions;

  @NotBlank
  @NonNull
  private UUID householdId;
}
