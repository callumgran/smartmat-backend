package edu.ntnu.idatt2106.smartmat.dto.shoppinglist;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for a new item in a shopping list.
 * @author Carl G. & Callum G.
 * @version 1.1 - 26.04.2023.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateShoppingListItemDTO {

  @NonNull
  @NotBlank
  private String name;

  @NotBlank
  @Min(1)
  private double amount;

  @NotBlank
  @NonNull
  private Long ingredientId;

  @NotBlank
  @NonNull
  private UUID shoppingListId;
}
