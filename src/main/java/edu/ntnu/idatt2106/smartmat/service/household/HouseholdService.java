package edu.ntnu.idatt2106.smartmat.service.household;

import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Interface for the household service.
 * @author Callum G.
 * @version 1.0 - 18.4.2023
 */
@Service
public interface HouseholdService {
  boolean householdExists(@NonNull UUID id) throws NullPointerException;

  Household getHouseHoldById(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException;

  Household saveHousehold(@NonNull Household household)
    throws NullPointerException, HouseholdAlreadyExistsException;

  Household updateHouseholdName(@NonNull UUID id, @NonNull String name)
    throws NullPointerException, HouseholdNotFoundException;

  void deleteHousehold(@NonNull Household household)
    throws NullPointerException, HouseholdNotFoundException;

  void deleteHouseholdById(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException;
}
