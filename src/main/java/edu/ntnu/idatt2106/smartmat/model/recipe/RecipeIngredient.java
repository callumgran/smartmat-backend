package edu.ntnu.idatt2106.smartmat.model.recipe;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * Class linking an ingredient to a recipe.
 * Ingredients is a part of a recipe, and a recipe needs ingredients.
 * @author Simen G.
 * @version 1.0 - 20.4.2023
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(
  name = "`recipe_ingredient`",
  uniqueConstraints = { @UniqueConstraint(columnNames = { "`recipe_id`", "`ingredient`" }) }
)
@IdClass(RecipeIngredientId.class)
public class RecipeIngredient {

  @Id
  @ManyToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  @JoinColumn(name = "`recipe_id`", nullable = false, referencedColumnName = "`id`")
  @NonNull
  private Recipe recipe;

  @Id
  @ManyToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  @JoinColumn(name = "`ingredient`", nullable = false, referencedColumnName = "`ingredient_id`")
  @NonNull
  private Ingredient ingredient;

  @Column(name = "`amount`", nullable = false)
  @NonNull
  private Double amount;
}
