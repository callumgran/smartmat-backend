package edu.ntnu.idatt2106.smartmat.dto.shoppinglist;

import edu.ntnu.idatt2106.smartmat.dto.ingredient.BareIngredientDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for shopping list items.
 * @author Callum G.
 * @version 1.0 - 03.05.2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeShoppingListItemDTO {

  @NotBlank
  private double amount;

  @NotBlank
  private boolean checked;

  private BareIngredientDTO ingredient;
}
