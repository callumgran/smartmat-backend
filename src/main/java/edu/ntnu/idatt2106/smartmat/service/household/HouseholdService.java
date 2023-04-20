package edu.ntnu.idatt2106.smartmat.service.household;

import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import java.util.Collection;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Interface for the household service.
 * @author Callum G.
 * @version 1.2 - 20.4.2023
 */
@Service
public interface HouseholdService {
  boolean householdExists(@NonNull UUID id) throws NullPointerException;

  Household getHouseholdById(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException;

  Household saveHousehold(@NonNull Household household)
    throws NullPointerException, HouseholdAlreadyExistsException;

  Household updateHouseholdName(@NonNull UUID id, @NonNull String name)
    throws NullPointerException, HouseholdNotFoundException;

  void deleteHousehold(@NonNull Household household)
    throws NullPointerException, HouseholdNotFoundException;

  void deleteHouseholdById(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException;

  HouseholdMember getHouseholdOwner(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException;

  Collection<HouseholdMember> getHouseholdMembers(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException;

  Collection<HouseholdMember> getHouseholdMembersWithRole(
    @NonNull UUID id,
    @NonNull HouseholdRole role
  ) throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException;

  boolean isHouseholdOwner(@NonNull UUID id, @NonNull String username)
    throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException;

  boolean isHouseholdMember(@NonNull UUID id, @NonNull String username)
    throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException;

  boolean isHouseholdMemberWithRole(
    @NonNull UUID id,
    @NonNull String username,
    @NonNull HouseholdRole role
  ) throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException;

  void deleteHouseholdMember(@NonNull UUID id, @NonNull String username)
    throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException;

  void deleteAllHouseholdMembers(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException;

  Collection<Household> getHouseholdsByUser(@NonNull String username)
    throws NullPointerException, UserDoesNotExistsException;
}
