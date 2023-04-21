package edu.ntnu.idatt2106.smartmat.repository.household;

import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for household member operations on the database.
 * @author Callum G.
 * @version 1.0 - 20.4.2023
 */
@Repository
public interface HouseholdMemberRepository
  extends JpaRepository<HouseholdMember, HouseholdMemberId> {}
