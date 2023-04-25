package edu.ntnu.idatt2106.smartmat.dto.household;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CustomFoodItemDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ListingShoppingListDTO;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for household.
 * Used to transfer household data between layers.
 * @author Callum G.
 * @version 1.0 - 19.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class HouseholdDTO {

  @NonNull
  @NotBlank
  private String id;

  @NonNull
  @NotBlank
  private String name;

  @NonNull
  private Set<HouseholdMemberDTO> members;

  private Set<ListingShoppingListDTO> shoppingLists;

  private Set<CustomFoodItemDTO> customFoodItems;
}
