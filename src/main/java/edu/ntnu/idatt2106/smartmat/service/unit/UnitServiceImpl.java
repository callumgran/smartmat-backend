package edu.ntnu.idatt2106.smartmat.service.unit;

import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.repository.unit.UnitRepository;
import java.util.Collection;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service interface for the Unit class.
 * This is the implementation that is used in the service layer.
 * @author Callum G.
 * @version 1.0 01.05.2023
 */
@Service
public class UnitServiceImpl implements UnitService {

  @Autowired
  private UnitRepository unitRepository;

  /**
   * Method for getting all units in the database.
   * @return all units in the database.
   */
  @Override
  public Collection<Unit> getAllUnits() {
    return unitRepository.findAll();
  }

  /**
   * Method for getting a unit by its name.
   * @param name the name of the unit.
   * @return the unit with the given name.
   * @throws NullPointerException if the name is null.
   */
  @Override
  public Unit getUnit(@NonNull String name) throws NullPointerException {
    return unitRepository.findById(name).orElseThrow(NullPointerException::new);
  }

  /**
   * Method for getting a unit by its abbreviation.
   * @param abbreviation the abbreviation of the unit.
   * @return the unit with the given abbreviation.
   */
  @Override
  public Unit getUnitByAbbreviation(@NonNull String abbreviation) {
    return unitRepository.findByAbbreviation(abbreviation).orElseThrow(NullPointerException::new);
  }
}
