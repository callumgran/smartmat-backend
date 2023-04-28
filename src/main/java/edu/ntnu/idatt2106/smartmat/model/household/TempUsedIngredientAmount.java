package edu.ntnu.idatt2106.smartmat.model.household;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * Class representing the amount of a temporary used ingredient in a household.
 * @author Callum G.
 * @version 1.0 - 28.4.2023
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`temp_used_ingredient_amount`")
public class TempUsedIngredientAmount {

  @Id
  @ManyToOne(optional = false)
  private TempUsedIngredient tempUsedIngredient;

  @Id
  @ManyToOne(optional = false)
  private Ingredient ingredient;

  @NonNull
  private Double amount;
}
