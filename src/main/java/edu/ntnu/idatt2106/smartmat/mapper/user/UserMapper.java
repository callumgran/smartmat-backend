package edu.ntnu.idatt2106.smartmat.mapper.user;

import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdMemberDTO;
import edu.ntnu.idatt2106.smartmat.dto.user.UserDTO;
import edu.ntnu.idatt2106.smartmat.mapper.household.HouseholdMemberMapper;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Class used to map between User and UserDTO.
 * Based on the UserMapper from the IDATT2105 project.
 * @author Callum G.
 * @version 1.0 - 17.04.2023
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  /**
   * Maps a HouseholdMember to a HouseholdMemberDTO.
   * @param householdMember The HouseholdMember to map.
   * @return The mapped HouseholdMemberDTO.
   */
  @Named("householdMemberToHouseholdMemberDTO")
  default List<HouseholdMemberDTO> householdMemberToHouseholdMemberDTO(
    Collection<HouseholdMember> householdMember
  ) {
    return householdMember
      .stream()
      .map(HouseholdMemberMapper.INSTANCE::householdMemberToHouseholdMemberDTO)
      .toList();
  }

  /**
   * Maps a User to a UserDTO.
   * Ignores the password field.
   * @param user The User to map.
   * @return The mapped UserDTO.
   */
  @Mappings(
    {
      @Mapping(
        target = "households",
        source = "households",
        qualifiedByName = "householdMemberToHouseholdMemberDTO"
      ),
    }
  )
  UserDTO userToUserDTO(User user);
}
