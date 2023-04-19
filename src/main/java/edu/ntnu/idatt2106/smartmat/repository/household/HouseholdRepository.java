package edu.ntnu.idatt2106.smartmat.repository.household;

import edu.ntnu.idatt2106.smartmat.model.household.Household;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for household operations on the database.
 * @author Callum G.
 * @version 1.0 - 18.4.2023
 */
@Repository
public interface HouseholdRepository extends JpaRepository<Household, UUID> {}
