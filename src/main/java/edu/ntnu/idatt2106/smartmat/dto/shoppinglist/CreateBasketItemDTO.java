package edu.ntnu.idatt2106.smartmat.dto.shoppinglist;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for a new item in a basket.
 * @author Callum G.
 * @version 1.0 - 02.05.2023.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBasketItemDTO {

  @NotBlank
  @NonNull
  private Long foodProductId;

  @NotBlank
  private double amount;
}
