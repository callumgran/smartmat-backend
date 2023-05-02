package edu.ntnu.idatt2106.smartmat.service.unit;

import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import java.util.Collection;
import org.springframework.stereotype.Service;

/**
 * Service interface for the Unit class.
 * This interface defines the methods the service layer
 * can use to interact with the database layer.
 * @author Callum G.
 * @version 1.0 01.05.2023
 */
@Service
public interface UnitService {
  Collection<Unit> getAllUnits();

  Unit getUnit(String name);

  Unit getUnitByAbbreviation(String abbreviation);
}
