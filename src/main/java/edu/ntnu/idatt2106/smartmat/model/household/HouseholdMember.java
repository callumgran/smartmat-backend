package edu.ntnu.idatt2106.smartmat.model.household;

import edu.ntnu.idatt2106.smartmat.model.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Class linking a user to a household.
 * A household member is a user that is a member of a household.
 * @author Callum G.
 * @version 1.0 - 18.4.2023
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(
  name = "`household_member`",
  uniqueConstraints = { @UniqueConstraint(columnNames = { "`household_id`", "`user`" }) }
)
@IdClass(HouseholdMemberId.class)
public class HouseholdMember {

  @Id
  @ManyToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  @JoinColumn(name = "`household_id`", nullable = false, referencedColumnName = "`household_id`")
  @NonNull
  private Household household;

  @Id
  @ManyToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  @JoinColumn(name = "`user`", nullable = false, referencedColumnName = "`username`")
  @NonNull
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(name = "`household_role`", nullable = false)
  @NonNull
  private HouseholdRole householdRole;
}
