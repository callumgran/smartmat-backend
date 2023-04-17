package edu.ntnu.idatt2106.smartmat.mapper.user;

import edu.ntnu.idatt2106.smartmat.dto.user.UserDTO;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import org.mapstruct.Mapper;
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
   * Maps a User to a UserDTO.
   * Ignores the password field.
   * @param user The User to map.
   * @return The mapped UserDTO.
   */
  UserDTO userToUserDTO(User user);
}
