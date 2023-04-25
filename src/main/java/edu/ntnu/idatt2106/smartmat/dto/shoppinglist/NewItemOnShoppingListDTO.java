package edu.ntnu.idatt2106.smartmat.dto.shoppinglist;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for a new item in a shopping list.
 * Not the same as ShoppingListItemDTO.
 * This is used to create a new item in a shopping list
 * which can be either custom food item or shopping list item.
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewItemOnShoppingListDTO {

  @NonNull
  @NotBlank
  private String name;

  private double amount;
}
