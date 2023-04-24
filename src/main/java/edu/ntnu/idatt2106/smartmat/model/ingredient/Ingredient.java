package edu.ntnu.idatt2106.smartmat.model.ingredient;

import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

/**
 * Class representing an ingredient in the system.
 * @author Tobias. O
 * @version 1.0 - 20.04.2023
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
  private long id;

  @Column(name = "`ingredient_name`", length = 64, nullable = false)
  @NonNull
  private String name;

  @OneToMany(mappedBy = "ingredient")
  private Set<RecipeIngredient> recipes;
}
