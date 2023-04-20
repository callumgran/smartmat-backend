package edu.ntnu.idatt2106.smartmat.dto.ingredient;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Class representing an ingredientDTO.
 * @author Tobias. O
 * @version 1.0 - 20.04.2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {

  @NotBlank
  private long id;

  @NotBlank
  @NonNull
  private String name;
}
