package edu.ntnu.idatt2106.smartmat.endpoint;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.PrivateUserController;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.model.user.Role;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/*
 * Class used to test the private user endpoints.
 */
@RunWith(SpringRunner.class)
@WebMvcTest({ PrivateUserController.class, SecurityConfig.class })
public class PrivateUserControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserService userService;

  @Test
  @WithMockUser(username = "test")
  public void testGetUserThatExistsWithAuth() throws Exception {
    User user = User
      .builder()
      .username("test")
      .password("test")
      .email("test@test")
      .role(Role.USER)
      .firstName("test")
      .lastName("test")
      .build();

    when(userService.getUserByUsername("test")).thenReturn(user);

    try {
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/private/users/me")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  @WithMockUser(username = "test")
  public void testGetUserThatDoesntExist() throws Exception {
    when(userService.getUserByUsername("test")).thenThrow(new UserDoesNotExistsException());

    try {
      mvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/private/users/me")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail();
    }
  }
}
