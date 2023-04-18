package edu.ntnu.idatt2106.smartmat.endpoint;

import static edu.ntnu.idatt2106.smartmat.endpoint.EndpointTestHelperFunctions.testUserFactory;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.PublicUserController;
import edu.ntnu.idatt2106.smartmat.dto.user.RegisterDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UsernameAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.mapper.user.RegisterMapper;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Class used to test the public user endpoints.
 * Based on the PublicUserControllerTest class from the IDATT2105 project.
 */
@RunWith(SpringRunner.class)
@WebMvcTest({ PublicUserController.class, SecurityConfig.class })
public class PublicUserControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserService userService;

  @MockBean
  private RegisterMapper mapper;

  private User user;

  private User badUser;

  @Before
  public void setUp() {
    user = testUserFactory(TestUserEnum.GOOD);
    badUser = testUserFactory(TestUserEnum.BAD);

    try {
      when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
      when(userService.getUserByUsername(badUser.getUsername()))
        .thenThrow(UserDoesNotExistsException.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetUserThatExists() throws Exception {
    try {
      mvc
        .perform(
          MockMvcRequestBuilders.get("/api/v1/public/users/test").accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testGetUserThatDoesNotExist() throws Exception {
    try {
      mvc
        .perform(
          MockMvcRequestBuilders.get("/api/v1/public/users/bad").accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCreateUser() throws Exception {
    System.out.println("USER: " + user.getEmail());
    when(mapper.registerDTOtoUser(any(RegisterDTO.class))).thenReturn(user);
    when(userService.saveUser(any(User.class))).thenReturn(user);
    try {
      mvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/public/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{" +
              "  \"username\": \"test\"," +
              "  \"password\": \"Password1\"," +
              "  \"email\": \"test@test.com\"," +
              "  \"firstName\": \"test\"," +
              "  \"lastName\": \"test\"" +
              "}"
            )
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCreateUserThatAlreadyExists() throws Exception {
    when(mapper.registerDTOtoUser(any(RegisterDTO.class))).thenReturn(badUser);
    when(userService.saveUser(any(User.class))).thenThrow(UsernameAlreadyExistsException.class);
    try {
      mvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/public/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{" +
              "  \"username\": \"bad\"," +
              "  \"password\": \"Password1\"," +
              "  \"email\": \"bad@bad.com\"," +
              "  \"firstName\": \"bad\"," +
              "  \"lastName\": \"bad\"" +
              "}"
            )
        )
        .andExpect(status().isConflict());
    } catch (Exception e) {
      fail();
    }
  }
}
