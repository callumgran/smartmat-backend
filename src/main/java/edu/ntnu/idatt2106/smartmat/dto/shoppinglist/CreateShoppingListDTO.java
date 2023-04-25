package edu.ntnu.idatt2106.smartmat.dto.shoppinglist;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for creating a shopping list.
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateShoppingListDTO {

  @NotBlank
  @NonNull
  private String household;
}
