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
 * DTO for updating a shopping list
 * Can update household and date completed.
 * @author Tobias O.
 * @version 1.0 - 21.04.2023.
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateShoppingListDTO {

  @NotBlank
  @NonNull
  private UUID id;

  @NotBlank
  @NonNull
  private UUID household;

  private LocalDate dateCompleted;
}
