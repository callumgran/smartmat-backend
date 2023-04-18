package edu.ntnu.idatt2106.smartmat.endpoint.user;

import static edu.ntnu.idatt2106.smartmat.endpoint.EndpointTestHelperFunctions.createAuthenticationToken;
import static edu.ntnu.idatt2106.smartmat.endpoint.EndpointTestHelperFunctions.testUserFactory;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.PrivateUserController;
import edu.ntnu.idatt2106.smartmat.endpoint.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
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

/*
 * Class used to test the private user endpoints.
 *
 * All tests are with authentication as this is already tested and works.
 */
@RunWith(SpringRunner.class)
@WebMvcTest({ PrivateUserController.class, SecurityConfig.class })
public class PrivateUserControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserService userService;

  private User user;

  private User userDoesNotExist;

  private User admin;

  @Before
  public void setUp() throws Exception {
    user = testUserFactory(TestUserEnum.GOOD);

    when(userService.getUserByUsername("testusername")).thenReturn(user);

    userDoesNotExist = testUserFactory(TestUserEnum.NEW);

    when(userService.getUserByUsername("newusername")).thenThrow(UserDoesNotExistsException.class);

    admin = testUserFactory(TestUserEnum.ADMIN);

    when(userService.getUserByUsername("adminusername")).thenReturn(admin);
  }

  @Test
  public void testGetUserThatExists() throws Exception {
    try {
      mvc
        .perform(
          get("/api/v1/private/users/me")
            .with(authentication(createAuthenticationToken(user)))
            .accept(MediaType.APPLICATION_JSON)
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
          get("/api/v1/private/users/me")
            .with(authentication(createAuthenticationToken(userDoesNotExist)))
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testUpdateUserThatExists() {
    try {
      mvc
        .perform(
          patch("/api/v1/private/users/testusername")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"firstName\": \"New Name\"}")
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testUpdateUserThatDoesNotExist() {
    try {
      mvc
        .perform(
          patch("/api/v1/private/users/newusername")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"firstName\": \"New Name\"}")
            .with(authentication(createAuthenticationToken(userDoesNotExist)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testUpdateUserThatIsNotTheUser() {
    try {
      mvc
        .perform(
          patch("/api/v1/private/users/testusername")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"firstName\": \"New Name\"}")
            .with(authentication(createAuthenticationToken(userDoesNotExist)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testUpdateUserThatIsNotUserButIsAdmin() {
    try {
      mvc
        .perform(
          patch("/api/v1/private/users/testusername")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"firstName\": \"New Name\"}")
            .with(authentication(createAuthenticationToken(admin)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }
}
