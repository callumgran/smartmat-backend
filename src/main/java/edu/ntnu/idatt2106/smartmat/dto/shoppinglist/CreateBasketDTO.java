package edu.ntnu.idatt2106.smartmat.dto.shoppinglist;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Object for creating a basket.
 * @author Callum G.
 * @version 1.0 - 03.05.2023.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBasketDTO {

  @NotBlank
  @NonNull
  private UUID shoppingListId;
}
