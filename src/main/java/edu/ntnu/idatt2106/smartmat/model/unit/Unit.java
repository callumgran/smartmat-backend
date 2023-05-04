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

  @Column(name = "`abbreviation`", unique = true, length = 16, nullable = false)
  private String abbreviation;

  @OneToMany(mappedBy = "unit")
  private Set<Ingredient> ingredients;

  /**
   * The conversion factor from the unit to the normal form of the unit
   * For example, if the unit is "dl" and the normal form is "l", the conversion factor is 0.1
   * If the unit is "g" and the normal form is "kg", the conversion factor is 0.001
   */
  @Column(name = "`to_normal_form_conversion_factor`", nullable = false)
  private double toNormalFormConversionFactor;

  @Enumerated(EnumType.STRING)
  @Column(name = "`unit_type`", length = 10, nullable = false)
  private UnitTypeEnum unitType;
}
