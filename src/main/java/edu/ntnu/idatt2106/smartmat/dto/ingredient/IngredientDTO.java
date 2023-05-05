package edu.ntnu.idatt2106.smartmat.dto.ingredient;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.IngredientFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.unit.UnitDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Class representing an ingredientDTO.
 * @author Tobias. O, Callum G.
 * @version 1.1 - 21.04.2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {

  @NotNull
  @NotBlank
  private Long id;

  @NotBlank
  @NonNull
  private String name;

  private UnitDTO unit;

  @NotBlank
  @NonNull
  private List<IngredientFoodProductDTO> foodProducts;
}
