package edu.ntnu.idatt2106.smartmat.dto.shoppinglist;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CustomFoodItemDTO;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for a shopping list.
 * @author Tobias O., Carl G.
 * @version 1.0 - 24.04.2023.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListDTO {

  @NotBlank
  @NonNull
  private UUID id;

  private LocalDate dateCompleted;

  @NotBlank
  @NonNull
  private UUID household;

  @NotBlank
  @NonNull
  private Set<ShoppingListItemDTO> shoppingListItems;

  @NotBlank
  @NonNull
  private Set<CustomFoodItemDTO> customFoodItems;
}
