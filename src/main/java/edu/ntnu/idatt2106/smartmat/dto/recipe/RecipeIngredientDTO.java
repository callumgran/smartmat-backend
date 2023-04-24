package edu.ntnu.idatt2106.smartmat.dto.recipe;

import edu.ntnu.idatt2106.smartmat.dto.ingredient.IngredientDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDTO {

  @NotBlank
  @NonNull
  private IngredientDTO ingredient;

  @NotBlank
  private int amount;
}
