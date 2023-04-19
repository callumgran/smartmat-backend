package edu.ntnu.idatt2106.smartmat.endpoint.user;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.TokenController;
import edu.ntnu.idatt2106.smartmat.dto.user.AuthenticateDTO;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest({ TokenController.class, SecurityConfig.class })
public class TokenControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserService userService;

  @Before
  public void setUp() {
    AuthenticateDTO goodAuthDTO = new AuthenticateDTO("test", "test");

    AuthenticateDTO badUsernameDTO = new AuthenticateDTO("bad", "test");

    AuthenticateDTO badPasswordDTO = new AuthenticateDTO("test", "bad");

    AuthenticateDTO badAuthDTO = new AuthenticateDTO("bad", "bad");

    User goodUser = testUserFactory(TestUserEnum.GOOD);

    try {
      when(userService.authenticateUser(goodAuthDTO.getUsername(), goodAuthDTO.getPassword()))
        .thenReturn(true);
      when(userService.authenticateUser(badAuthDTO.getUsername(), badAuthDTO.getPassword()))
        .thenThrow(BadCredentialsException.class);
      when(userService.authenticateUser(badUsernameDTO.getUsername(), badUsernameDTO.getPassword()))
        .thenThrow(BadCredentialsException.class);
      when(userService.authenticateUser(badPasswordDTO.getUsername(), badPasswordDTO.getPassword()))
        .thenThrow(BadCredentialsException.class);
      when(userService.getUserByUsername(goodAuthDTO.getUsername())).thenReturn(goodUser);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetToken() {
    try {
      mvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/public/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"test\", \"password\": \"test\"}")
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testGetTokenBadUsername() {
    try {
      mvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/public/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"bad\", \"password\": \"test\"}")
        )
        .andExpect(status().isUnauthorized());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testGetTokenBadPassword() {
    try {
      mvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/public/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"test\", \"password\": \"bad\"}")
        )
        .andExpect(status().isUnauthorized());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testGetTokenBadAuth() {
    try {
      mvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/public/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"bad\", \"password\": \"bad\"}")
        )
        .andExpect(status().isUnauthorized());
    } catch (Exception e) {
      fail();
    }
  }
}
