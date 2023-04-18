package edu.ntnu.idatt2106.smartmat.security;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.PrivateUserController;
import edu.ntnu.idatt2106.smartmat.model.user.Role;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Class used to test the security of endpoints that require authentication.
 */
@RunWith(SpringRunner.class)
@WebMvcTest({ PrivateUserController.class, SecurityConfig.class })
public class SecureEndpointTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserService userService;

  @Test
  @WithMockUser(username = "user")
  public void testSecureEndpointWithUserIsAuthorized() {
    User user = User
      .builder()
      .username("user")
      .firstName("user")
      .lastName("user")
      .password("password")
      .email("user@user")
      .role(Role.USER)
      .build();
    try {
      when(userService.getUserByUsername("user")).thenReturn(user);
      mvc
        .perform(get("/api/v1/private/users/me").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testSecureEndpointWithoutUserIsUnauthorized() {
    try {
      mvc
        .perform(get("/api/v1/private/users/me").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
