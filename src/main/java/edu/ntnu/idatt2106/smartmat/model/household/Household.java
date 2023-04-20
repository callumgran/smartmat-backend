package edu.ntnu.idatt2106.smartmat.model.household;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class representing a household in the system.
 * A household is a collection of users that share SmartMat shopping lists
 * and food-stores.
 * @author Callum G.
 * @version 1.1
 * @date 20.4.2023
 */
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`household`")
public class Household {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "`household_id`", length = 16, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "`name`", length = 64, nullable = false)
  @NonNull
  private String name;

  @OneToMany(mappedBy = "household", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  private Set<HouseholdMember> members;
}
