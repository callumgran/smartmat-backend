package edu.ntnu.idatt2106.smartmat.service.household;

import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.repository.household.HouseholdRepository;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the household service.
 * This class is responsible for all business logic related to households.
 * @author Callum G.
 * @version 1.0 - 18.4.2023
 */
@Service
@RequiredArgsConstructor
public class HouseholdServiceImpl implements HouseholdService {

  @Autowired
  private HouseholdRepository householdRepository;

  /**
   * Method to check if a household exists.
   * @param id The UUID of the respective household.
   * @return True if the household exists, false otherwise.
   * @throws NullPointerException if the id is null.
   */
  @Override
  public boolean householdExists(@NonNull UUID id) throws NullPointerException {
    return householdRepository.existsById(id);
  }

  /**
   * Method to get a household by its id.
   * @param id The UUID of the respective household.
   * @return The household with the id.
   * @throws NullPointerException if the id is null.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Override
  public Household getHouseHoldById(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException {
    return householdRepository.findById(id).orElseThrow(HouseholdNotFoundException::new);
  }

  /**
   * Method to create a household.
   * @param household the household to save.
   * @return The saved household.
   * @throws NullPointerException if the household is null.
   * @throws HouseholdAlreadyExistsException if the household already exists.
   */
  @Override
  public Household saveHousehold(@NonNull Household household)
    throws NullPointerException, HouseholdAlreadyExistsException {
    if (household.getId() != null) throw new HouseholdAlreadyExistsException();

    return householdRepository.save(household);
  }

  /**
   * Method to update a households name.
   * @param id The UUID of the respective household.
   * @param name the household name to update.
   * @return The updated household.
   * @throws NullPointerException if the uuid or name is null.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Override
  public Household updateHouseholdName(@NonNull UUID id, @NonNull String name)
    throws NullPointerException, HouseholdNotFoundException {
    Household household = getHouseHoldById(id);
    household.setName(name);
    return householdRepository.save(household);
  }

  /**
   * Method to delete a household.
   * @param household the household to delete.
   * @throws NullPointerException if the household is null.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Override
  public void deleteHousehold(@NonNull Household household)
    throws NullPointerException, HouseholdNotFoundException {
    if (household == null || household.getId() == null) throw new NullPointerException(
      "Husholdning kan ikke være null eller ha null id"
    );

    if (!householdExists(household.getId())) throw new HouseholdNotFoundException();

    householdRepository.delete(household);
  }

  /**
   * Method to delete a household by its id.
   * @param id The UUID of the respective household.
   * @throws NullPointerException if the id is null.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Override
  public void deleteHouseholdById(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException {
    if (!householdExists(id)) throw new HouseholdNotFoundException();
    householdRepository.deleteById(id);
  }
}
