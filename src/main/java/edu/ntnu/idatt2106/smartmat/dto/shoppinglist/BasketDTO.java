package edu.ntnu.idatt2106.smartmat.dto.shoppinglist;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CustomFoodItemDTO;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for a basket.
 *
 * @author Callum G.
 * @version 1.0 - 02.05.2023.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasketDTO {

  private UUID id;

  private UUID shoppingListId;

  private List<BasketItemDTO> basketItems;

  private List<CustomFoodItemDTO> customFoodItems;
}
