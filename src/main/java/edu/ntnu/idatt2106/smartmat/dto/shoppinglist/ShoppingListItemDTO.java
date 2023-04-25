package edu.ntnu.idatt2106.smartmat.dto.shoppinglist;

import edu.ntnu.idatt2106.smartmat.dto.ingredient.IngredientDTO;
import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for shopping list items.
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListItemDTO {

  @NotBlank
  @NonNull
  private UUID id;

  @NotBlank
  private double amount;

  @NotBlank
  private boolean checked;

  @NotBlank
  @NonNull
  private UUID shoppingList;

  private IngredientDTO ingredient;
}
