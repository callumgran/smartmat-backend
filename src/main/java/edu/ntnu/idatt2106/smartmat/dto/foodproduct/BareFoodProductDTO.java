package edu.ntnu.idatt2106.smartmat.dto.foodproduct;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for food product.
 * Used to transfer food product data between layers.
 * @author Callum G
 * @version 1.0 - 26.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BareFoodProductDTO {

  @NotBlank
  @NonNull
  private Long id;

  @NonNull
  @NotBlank
  private String name;

  @NonNull
  @NotBlank
  private String EAN;

  @NotBlank
  private double amount;

  @NotBlank
  private boolean looseWeight;

  private Long ingredientId;

  @NonNull
  @NotBlank
  private String image;

  @NotBlank
  private boolean isNotIngredient;
}
