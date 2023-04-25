package edu.ntnu.idatt2106.smartmat.dto.ingredient;

import edu.ntnu.idatt2106.smartmat.dto.unit.UnitDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Class representing an ingredientDTO for food product.
 * @author Callum G., Carl G.
 * @version 1.1 - 25.04.2023
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

  private UnitDTO unit;
}
