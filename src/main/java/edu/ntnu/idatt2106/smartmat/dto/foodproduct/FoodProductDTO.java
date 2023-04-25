package edu.ntnu.idatt2106.smartmat.dto.foodproduct;

import edu.ntnu.idatt2106.smartmat.dto.ingredient.BareIngredientDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for food product.
 * Used to transfer food product data between layers.
 * @author Callum G, Nicolai H. Brand
 * @version 1.1 - 24.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FoodProductDTO {

  @NonNull
  private Long id;

  @NonNull
  @NotBlank
  private String name;

  private String EAN;

  private double amount;

  private boolean looseWeight;

  @NonNull
  private BareIngredientDTO ingredient;
}
