package edu.ntnu.idatt2106.smartmat.dto.shoppinglist;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.FoodProductDTO;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for a item in a basket.
 *
 * @author Callum G.
 * @version 1.0 - 02.05.2023.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasketItemDTO {

  @NotBlank
  @NonNull
  private UUID id;

  @NotBlank
  @NonNull
  private FoodProductDTO foodProduct;

  @NotBlank
  private double amount;

  @NotBlank
  @NonNull
  private UUID basketId;
}
