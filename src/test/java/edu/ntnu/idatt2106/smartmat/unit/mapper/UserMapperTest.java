package edu.ntnu.idatt2106.smartmat.unit.mapper;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2106.smartmat.dto.user.UserDTO;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.mapper.user.UserMapper;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

  @Nested
  public class UserToUserDTOTest {

    @Test
    public void testUserToUserDTO() {
      User user = testUserFactory(TestUserEnum.GOOD);

      UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);

      assertEquals(user.getUsername(), userDTO.getUsername());
      assertEquals(user.getEmail(), userDTO.getEmail());
      assertEquals(user.getFirstName(), userDTO.getFirstName());
      assertEquals(user.getLastName(), userDTO.getLastName());
      assertEquals(user.getRole(), userDTO.getRole());
    }
  }
}
