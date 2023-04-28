package edu.ntnu.idatt2106.smartmat.model.household;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class representing a temporary ingredient in the system.
 * @author Callum G.
 * @version 1.1 - 28.04.2023
 */
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`temp_used_ingredient`")
@IdClass(TempUsedIngredientId.class)
public class TempUsedIngredient {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "`household_id`", nullable = false, referencedColumnName = "`household_id`")
  private Household household;

  @Id
  @Column(name = "`date_to_use`")
  @NonNull
  private LocalDate dateToUse;

  @OneToMany(mappedBy = "tempUsedIngredient")
  private Set<TempUsedIngredientAmount> tempUsedIngredientAmount;
}
