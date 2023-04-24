package edu.ntnu.idatt2106.smartmat.model.recipe;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing the primary key of a household member.
 * @author Simen G.
 * @version 1.0
 * @date 18.4.2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RecipeIngredientId implements Serializable {

  private Recipe recipe;

  private Ingredient ingredient;
}
