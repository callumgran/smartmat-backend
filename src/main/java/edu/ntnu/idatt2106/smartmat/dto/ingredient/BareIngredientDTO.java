package edu.ntnu.idatt2106.smartmat.dto.ingredient;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Class representing an ingredientDTO for food product.
 * @author Callum G.
 * @version 1.0 - 21.04.2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BareIngredientDTO {

  @NotBlank
  private Long id;

  @NotBlank
  @NonNull
  private String name;
}
