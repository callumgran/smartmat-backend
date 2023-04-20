package edu.ntnu.idatt2106.smartmat.mapper.user;

import edu.ntnu.idatt2106.smartmat.dto.user.RegisterDTO;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Class used to map between User and RegisterDTO.
 * Based on the UserMapper from the IDATT2105 project.
 * @author Callum G.
 * @version 1.1 - 19.04.2023
 */
@Mapper(componentModel = "spring")
public interface RegisterMapper {
  RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

  /**
   * Maps a registration DTO to a user.
   * Sets the role to UserRole.USER by default.
   * @param registerDTO A user registration in the form of a DTO
   * @return A user object with the USER role
   */
  @Mappings(
    { @Mapping(target = "role", constant = "USER"), @Mapping(target = "households", ignore = true) }
  )
  User registerDTOtoUser(RegisterDTO registerDTO);
}
