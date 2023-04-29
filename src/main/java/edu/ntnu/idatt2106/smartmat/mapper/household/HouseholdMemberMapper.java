package edu.ntnu.idatt2106.smartmat.mapper.household;

import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdMemberDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class used to map between HouseholdMember and HouseholdMemberDTO.
 * @author Callum G, Nicolai H. Brand.
 * @version 1.0 - 29.04.2023
 */
@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class HouseholdMemberMapper {

  public static HouseholdMemberMapper INSTANCE = Mappers.getMapper(HouseholdMemberMapper.class);

  @Autowired
  private UserService userService;

  @Autowired
  private HouseholdService householdService;

  /**
   * Maps a user to a username.
   * @param user The user to map.
   * @return The mapped username.
   */
  @Named("userToUsername")
  public String userToUsername(User user) {
    return user.getUsername();
  }

  /**
   * Maps a user to a first name.
   * @param user The user to map.
   * @return The mapped first name.
   */
  @Named("userToFirstName")
  public String userToFirstName(User user) {
    return user.getFirstName();
  }

  /**
   * Maps a user to a last name.
   * @param user The user to map.
   * @return The mapped last name.
   */
  @Named("userToLastName")
  public String userToLastName(User user) {
    return user.getLastName();
  }

  /**
   * Maps a user to an email.
   * @param user The user to map.
   * @return The mapped email.
   */
  @Named("userToEmail")
  public String userToEmail(User user) {
    return user.getEmail();
  }

  /**
   * Maps a username to a user.
   * @param username The username to map.
   * @return The mapped user.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @Named("usernameToUser")
  public User usernameToUser(String username) throws UserDoesNotExistsException {
    return userService.getUserByUsername(username);
  }

  /**
   * Maps a household to a household id.
   * @param household The household to map.
   * @return The mapped household id.
   */
  @Named("householdToId")
  public UUID householdToId(Household household) {
    return household.getId();
  }

  /**
   * Maps a household id to a household.
   * @param id The household id to map.
   * @return The mapped household.
   * @throws HouseholdNotFoundException If the household does not exist.
   */
  @Named("idToHousehold")
  public Household idToHousehold(UUID id) throws HouseholdNotFoundException {
    return householdService.getHouseholdById(id);
  }

  /**
   * Maps a household member DTO to a household member.
   * @param householdMemberDTO The household member DTO to map.
   * @return The mapped household member.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   */
  @Mappings(
    {
      @Mapping(target = "user", source = "username", qualifiedByName = "usernameToUser"),
      @Mapping(target = "household", source = "household", qualifiedByName = "idToHousehold"),
    }
  )
  public abstract HouseholdMember householdMemberDTOToHouseholdMember(
    HouseholdMemberDTO householdMemberDTO
  ) throws UserDoesNotExistsException, HouseholdNotFoundException;

  /**
   * Maps a household member to a household member DTO.
   * @param householdMember The household member to map.
   * @return The mapped household member DTO.
   */
  @Mappings(
    {
      @Mapping(target = "username", source = "user", qualifiedByName = "userToUsername"),
      @Mapping(target = "firstName", source = "user", qualifiedByName = "userToFirstName"),
      @Mapping(target = "lastName", source = "user", qualifiedByName = "userToLastName"),
      @Mapping(target = "email", source = "user", qualifiedByName = "userToEmail"),
      @Mapping(target = "household", source = "household", qualifiedByName = "householdToId"),
    }
  )
  public abstract HouseholdMemberDTO householdMemberToHouseholdMemberDTO(
    HouseholdMember householdMember
  );
}
