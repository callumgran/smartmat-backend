package edu.ntnu.idatt2106.smartmat.dto.foodproduct;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for custom food items.
 * Use to create custom food items.
 * @author Callum G.
 * @version 1.0 - 26.04.2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomFoodItemDTO {

  @NotBlank
  @NonNull
  private String name;

  @NotBlank
  private int amount;

  @NotBlank
  @NonNull
  private UUID shoppingListId;
}
