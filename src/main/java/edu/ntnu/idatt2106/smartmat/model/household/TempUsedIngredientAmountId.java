package edu.ntnu.idatt2106.smartmat.model.household;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing the primary key of a temporary used ingredient amount in a household.
 * @author Callum G.
 * @version 1.0 - 28.4.2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TempUsedIngredientAmountId implements Serializable {

  private TempUsedIngredient tempUsedIngredient;

  private Ingredient ingredient;
}
