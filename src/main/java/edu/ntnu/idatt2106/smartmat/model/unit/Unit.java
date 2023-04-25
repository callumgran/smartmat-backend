package edu.ntnu.idatt2106.smartmat.model.unit;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

/**
 * Entity Unit with name and abbreviation
 * @author Simen G.
 * @version 1.0 25.04.2023
 */
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`unit`")
public class Unit {

  @Id
  @Column(name = "`unit_name`", length = 64, nullable = false)
  @NonNull
  private String name;

  @Column(name = "`abbreviation`", length = 16, nullable = true)
  private String abbreviation;

  @OneToMany(mappedBy = "unit")
  private Set<Ingredient> ingredients;
}
