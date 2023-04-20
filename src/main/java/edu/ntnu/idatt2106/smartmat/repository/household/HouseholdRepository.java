package edu.ntnu.idatt2106.smartmat.repository.household;

import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for household operations on the database.
 * @author Callum G.
 * @version 1.1 - 20.4.2023
 */
@Repository
public interface HouseholdRepository extends JpaRepository<Household, UUID> {
  /**
   * Find a household by its id.
   * @param id The id of the household.
   * @return The household with the given id.
   * @throws NullPointerException If the given id is null.
   */
  @Query(
    "SELECT hm FROM HouseholdMember hm WHERE hm.household.id = ?1 AND hm.householdRole = edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole.OWNER"
  )
  Optional<Collection<HouseholdMember>> findHouseholdOwnerById(@NonNull UUID id);

  /**
   * Find all household members with the given role.
   * @param id The id of the household.
   * @param role The role of the household members.
   * @return A collection of household members with the given role.
   * @throws NullPointerException If the given id or role is null.
   */
  @Query("SELECT hm FROM HouseholdMember hm WHERE hm.household.id = ?1 AND hm.householdRole = ?2")
  Optional<Collection<HouseholdMember>> findHouseholdMembersWithRoleById(
    @NonNull UUID id,
    @NonNull HouseholdRole role
  );

  /**
   * Find all household members.
   * @param id The id of the household.
   * @return A collection of household members.
   * @throws NullPointerException If the given id is null.
   */
  @Query("SELECT hm FROM HouseholdMember hm WHERE hm.household.id = ?1")
  Optional<Collection<HouseholdMember>> findHouseholdMembersById(@NonNull UUID id);

  /**
   * Delete a household member.
   * @param id The id of the household.
   * @param username The username of the household member.
   * @throws NullPointerException If the given id or username is null.
   */
  @Modifying
  @Query("DELETE FROM HouseholdMember hm WHERE hm.household.id = ?1 AND hm.user.username = ?2")
  void deleteHouseholdMemberByIdAndUsername(@NonNull UUID id, @NonNull String username);

  /**
   * Delete all household members.
   * @param id The id of the household.
   * @throws NullPointerException If the given id is null.
   */
  @Modifying
  @Query("DELETE FROM HouseholdMember hm WHERE hm.household.id = ?1")
  void deleteHouseholdMembersById(@NonNull UUID id);
}
