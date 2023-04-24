package edu.ntnu.idatt2106.smartmat.dto.recipe;

import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeDifficulty;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for Recipe.
 * @author Simen G. & Callum G.
 * @version 1.1 - 21.04.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDTO {

  @NotBlank
  @NonNull
  private String id;

  @NotBlank
  @NonNull
  private String name;

  @NotBlank
  @NonNull
  private String description;

  @NotBlank
  @NonNull
  private List<RecipeIngredientDTO> ingredients;

  @NotBlank
  @NonNull
  private String instructions;

  @NotBlank
  private int estimatedMinutes;

  @NotBlank
  @NonNull
  private RecipeDifficulty recipeDifficulty;
}
