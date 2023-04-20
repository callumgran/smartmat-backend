package edu.ntnu.idatt2106.smartmat.mapper.household;

import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdMemberDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.util.function.ThrowingFunction;

/**
 * Class used to map between User and RegisterDTO.
 * @author Callum G.
 * @version 1.0 - 19.04.2023
 */
@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class HouseholdMapper {

  public static HouseholdMapper INSTANCE = Mappers.getMapper(HouseholdMapper.class);

  private HouseholdMemberMapper householdMemberMapper = HouseholdMemberMapper.INSTANCE;

  /**
   * Maps a household member to a household member DTO.
   * @param member The household member to map.
   * @return The mapped household member DTO.
   */
  @Named("membersToMemberDTOs")
  public Set<HouseholdMemberDTO> memberToMemberDTO(Set<HouseholdMember> members) {
    return members
      .stream()
      .map(householdMemberMapper::householdMemberToHouseholdMemberDTO)
      .collect(Collectors.toSet());
  }

  /**
   * Maps a household member to a household member DTO.
   * @param member The household member to map.
   * @return The mapped household member DTO.
   */
  @Named("memberDTOsToMembers")
  public Set<HouseholdMember> memberDTOToMember(Set<HouseholdMemberDTO> memberDTOs)
    throws HouseholdNotFoundException, UserDoesNotExistsException {
    ThrowingFunction<HouseholdMemberDTO, HouseholdMember> function =
      householdMemberMapper::householdMemberDTOToHouseholdMember;
    return memberDTOs.stream().map(function).collect(Collectors.toSet());
  }

  /**
   * Maps a string to a UUID.
   * @param id The string to map.
   * @return The mapped UUID.
   */
  @Named("stringToUUID")
  public UUID stringToUUID(String id) {
    return UUID.fromString(id);
  }

  /**
   * Maps a UUID to a string.
   * @param id The UUID to map.
   * @return The mapped string.
   */
  @Named("UUIDToString")
  public String UUIDToString(UUID id) {
    return id.toString();
  }

  /**
   * Maps a household to a household DTO.
   * @param household The household to map.
   * @return The mapped household DTO.
   */
  @Mappings(
    {
      @Mapping(target = "members", source = "members", qualifiedByName = "membersToMemberDTOs"),
      @Mapping(target = "id", source = "id", qualifiedByName = "UUIDToString"),
    }
  )
  public abstract HouseholdDTO householdToHouseholdDTO(Household household);

  /**
   * Maps a household DTO to a household.
   * @param householdDTO The household DTO to map.
   * @return The mapped household.
   */
  @Mappings(
    {
      @Mapping(target = "members", source = "members", qualifiedByName = "memberDTOsToMembers"),
      @Mapping(target = "id", source = "id", qualifiedByName = "stringToUUID"),
    }
  )
  public abstract Household householdDTOToHousehold(HouseholdDTO householdDTO)
    throws HouseholdNotFoundException, UserDoesNotExistsException;
}
