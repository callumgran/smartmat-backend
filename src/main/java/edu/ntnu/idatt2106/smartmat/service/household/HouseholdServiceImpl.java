package edu.ntnu.idatt2106.smartmat.service.household;

import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.repository.household.HouseholdMemberRepository;
import edu.ntnu.idatt2106.smartmat.repository.household.HouseholdRepository;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import java.util.Collection;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the household service.
 * This class is responsible for all business logic related to households.
 * @author Callum G.
 * @version 1.2 - 20.4.2023
 */
@Service
@RequiredArgsConstructor
public class HouseholdServiceImpl implements HouseholdService {

  @Autowired
  private HouseholdRepository householdRepository;

  @Autowired
  private HouseholdMemberRepository householdMemberRepository;

  @Autowired
  private UserService userService;

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
  public Household getHouseholdById(@NonNull UUID id)
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
    Household household = getHouseholdById(id);
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
    deleteAllHouseholdMembers(id);
    householdRepository.deleteById(id);
  }

  /**
   * Method to get the owner of a household.
   * @param id The UUID of the respective household.
   * @return The owner of the household.
   * @throws NullPointerException if the id is null.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Override
  public HouseholdMember getHouseholdOwner(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!householdExists(id)) throw new HouseholdNotFoundException();
    return householdRepository
      .findHouseholdOwnerById(id)
      .orElseThrow(UserDoesNotExistsException::new)
      .stream()
      .findFirst()
      .orElseThrow(UserDoesNotExistsException::new);
  }

  /**
   * Method to get the members of a with a specific role.
   * @param id The UUID of the respective household.
   * @param role The role to filter by.
   * @return The members of the household with the role.
   * @throws NullPointerException if the household is null.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Override
  public Collection<HouseholdMember> getHouseholdMembersWithRole(
    @NonNull UUID id,
    @NonNull HouseholdRole role
  ) throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!householdExists(id)) throw new HouseholdNotFoundException();
    return householdRepository
      .findHouseholdMembersWithRoleById(id, role)
      .orElseThrow(UserDoesNotExistsException::new);
  }

  /**
   * Method to get the members of a household.
   * @param id The UUID of the respective household.
   * @return The members of the household.
   * @throws NullPointerException if the household is null.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Override
  public Collection<HouseholdMember> getHouseholdMembers(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!householdExists(id)) throw new HouseholdNotFoundException();
    return householdRepository
      .findHouseholdMembersById(id)
      .orElseThrow(UserDoesNotExistsException::new);
  }

  /**
   * Method to check if a user is the owner of a household.
   * @param householdId The UUID of the respective household.
   * @param userId The UUID of the respective user.
   * @return True if the user is the owner, false otherwise.
   * @throws NullPointerException if the household or user is null.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Override
  public boolean isHouseholdOwner(@NonNull UUID id, @NonNull String username)
    throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!householdExists(id)) throw new HouseholdNotFoundException();
    return getHouseholdOwner(id).getUser().getUsername().equals(username);
  }

  /**
   * Method to check if a user is a member of a household.
   * @param householdId The UUID of the respective household.
   * @param userId The UUID of the respective user.
   * @return True if the user is a member, false otherwise.
   * @throws NullPointerException if the household or user is null.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Override
  public boolean isHouseholdMember(@NonNull UUID id, @NonNull String username)
    throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!householdExists(id)) throw new HouseholdNotFoundException();
    return getHouseholdMembers(id)
      .stream()
      .anyMatch(householdMember -> householdMember.getUser().getUsername().equals(username));
  }

  /**
   * Method to check if a user is a member of a household with a specific role.
   * @param householdId The UUID of the respective household.
   * @param userId The UUID of the respective user.
   * @param role The role to filter by.
   * @return True if the user is a member with the role, false otherwise.
   * @throws NullPointerException if the household or user is null.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Override
  public boolean isHouseholdMemberWithRole(
    @NonNull UUID id,
    @NonNull String username,
    @NonNull HouseholdRole role
  ) throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!householdExists(id)) throw new HouseholdNotFoundException();
    return getHouseholdMembersWithRole(id, role)
      .stream()
      .anyMatch(householdMember -> householdMember.getUser().getUsername().equals(username));
  }

  /**
   * Method to remove a user from a household.
   * @param householdId The UUID of the respective household.
   * @param username The username of the respective user.
   * @throws NullPointerException if the household or user is null.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws UserDoesNotExistsException if the user is not found.
   */
  @Override
  public void deleteHouseholdMember(@NonNull UUID id, @NonNull String username)
    throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!householdExists(id)) throw new HouseholdNotFoundException();
    if (!userService.usernameExists(username)) throw new UserDoesNotExistsException();
    householdRepository.deleteHouseholdMemberByIdAndUsername(id, username);
  }

  /**
   * Method to delete all members of a household.
   * @param householdId The UUID of the respective household.
   * @throws NullPointerException if the household is null.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Override
  public void deleteAllHouseholdMembers(@NonNull UUID id)
    throws NullPointerException, HouseholdNotFoundException {
    if (!householdExists(id)) throw new HouseholdNotFoundException();
    householdRepository.deleteHouseholdMembersById(id);
  }

  /**
   * Method to get all households for a user.
   * @param username The username of the respective user.
   * @return The households of the user.
   * @throws NullPointerException if the username is null.
   * @throws UserDoesNotExistsException if the user is not found.
   */
  @Override
  public Collection<Household> getHouseholdsByUser(@NonNull String username)
    throws NullPointerException, UserDoesNotExistsException {
    if (!userService.usernameExists(username)) throw new UserDoesNotExistsException();
    return householdRepository.findAllByUsername(username).orElseThrow(NullPointerException::new);
  }

  /**
   * Method to add a user to a household.
   * @param householdId The UUID of the respective household.
   * @param username The username of the respective user.
   * @param role The role of the user in the household.
   * @return The household member.
   * @throws NullPointerException if the household or user is null.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws UserDoesNotExistsException if the user is not found.
   */
  @Override
  public HouseholdMember addHouseholdMember(
    @NonNull UUID id,
    @NonNull String username,
    @NonNull HouseholdRole role
  ) throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!householdExists(id)) throw new HouseholdNotFoundException();
    if (!userService.usernameExists(username)) throw new UserDoesNotExistsException();
    return householdMemberRepository.save(
      new HouseholdMember(getHouseholdById(id), userService.getUserByUsername(username), role)
    );
  }

  /**
   * Method to update a household member.
   * @param householdId The UUID of the respective household.
   * @param username The username of the respective user.
   * @param role The new role of the user in the household.
   * @return The household member.
   * @throws NullPointerException if the household or user is null.
   * @throws HouseholdNotFoundException if the household is not found.
   * @throws UserDoesNotExistsException if the user is not found.
   */
  @Override
  public HouseholdMember updateHouseholdMember(
    @NonNull UUID id,
    @NonNull String username,
    @NonNull HouseholdRole role
  ) throws NullPointerException, HouseholdNotFoundException, UserDoesNotExistsException {
    if (!householdExists(id)) throw new HouseholdNotFoundException();
    if (!userService.usernameExists(username)) throw new UserDoesNotExistsException();
    HouseholdMember householdMember = householdRepository
      .findHouseholdMemberByIdAndUsername(id, username)
      .get();
    householdMember.setHouseholdRole(role);
    return householdMemberRepository.save(householdMember);
  }
}
