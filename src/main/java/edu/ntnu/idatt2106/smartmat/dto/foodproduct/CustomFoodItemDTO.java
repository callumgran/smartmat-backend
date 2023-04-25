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
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomFoodItemDTO {

  @NotBlank
  private UUID id;

  @NotBlank
  @NonNull
  private String name;

  @NotBlank
  private int amount;

  @NotBlank
  private boolean checked;

  private UUID shoppingList;

  private UUID household;
}
