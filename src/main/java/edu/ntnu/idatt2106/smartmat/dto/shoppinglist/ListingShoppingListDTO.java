package edu.ntnu.idatt2106.smartmat.dto.shoppinglist;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for listing shopping lists.
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingShoppingListDTO {

  @NotBlank
  @NonNull
  private UUID id;

  private LocalDate dateCompleted;
}
