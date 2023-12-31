package edu.ntnu.idatt2106.smartmat.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for creating a Recipe.
 * @author Callum G., Carl G.
 * @version 1.1 - 26.04.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeCreateDTO {

  @NotBlank
  @NonNull
  private String name;

  @NotBlank
  @NonNull
  private String description;

  @NotBlank
  @NonNull
  private List<CreateUpdateRecipeIngredientDTO> ingredients;

  @NotBlank
  @NonNull
  private String instructions;

  @NotBlank
  private int estimatedMinutes;

  @NotBlank
  @NonNull
  private String recipeDifficulty;

  private String image;
}
