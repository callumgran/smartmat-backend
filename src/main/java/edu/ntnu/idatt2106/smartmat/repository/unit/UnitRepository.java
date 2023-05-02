package edu.ntnu.idatt2106.smartmat.repository.unit;

import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the Unit class.
 * @author Callum G.
 * @version 1.0 01.05.2023
 */
public interface UnitRepository extends JpaRepository<Unit, String> {
  /**
   * Method for finding a unit by its abbreviation.
   * @param abbreviation the abbreviation of the unit.
   * @return the unit with the given abbreviation.
   */
  Optional<Unit> findByAbbreviation(String abbreviation);
}
