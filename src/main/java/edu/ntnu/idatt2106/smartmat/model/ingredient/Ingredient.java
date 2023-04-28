package edu.ntnu.idatt2106.smartmat.model.ingredient;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

/**
 * Class representing an ingredient in the system.
 * @author Tobias. O, Carl G.
 * @version 1.1 - 20.04.2023
 */
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`ingredient`")
public class Ingredient {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "`ingredient_id`", nullable = false, updatable = false)
  private Long id;

  @Column(name = "`ingredient_name`", length = 64, nullable = false)
  @NonNull
  private String name;

  @OneToMany(mappedBy = "ingredient", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  private Set<FoodProduct> foodProducts;

  @OneToMany(mappedBy = "ingredient")
  private Set<RecipeIngredient> recipes;

  @ManyToOne(optional = true)
  @JoinColumn(name = "`unit`", nullable = true, referencedColumnName = "`unit_name`")
  private Unit unit;
}
