package edu.ntnu.idatt2106.smartmat.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating a Recipe ingredient.
 * This is used when creating and updating a recipe.
 * @author Carl G.
 * @version 1.0 - 26.04.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateRecipeIngredientDTO {

  @NotBlank
  private long ingredient;

  @NotBlank
  private double amount;
}
