package edu.ntnu.idatt2106.smartmat.dto.recipe;

import edu.ntnu.idatt2106.smartmat.dto.ingredient.BareIngredientDTO;
import edu.ntnu.idatt2106.smartmat.dto.unit.UnitDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for a recipe ingredient.
 * Displays an ingredient with ingredient data
 * @author Simen G., Carl G.
 * @version 1.1 - 26.04.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDTO {

  @NotBlank
  @NonNull
  private BareIngredientDTO ingredient;

  @NotBlank
  private double amount;

  @NotBlank
  @NonNull
  private UnitDTO unit;
}
