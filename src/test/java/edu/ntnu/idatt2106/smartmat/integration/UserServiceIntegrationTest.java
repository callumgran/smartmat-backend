package edu.ntnu.idatt2106.smartmat.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.DatabaseException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UsernameAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.model.user.Role;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.repository.user.UserRepository;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import edu.ntnu.idatt2106.smartmat.service.user.UserServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test for the user service.
 * Based on the user-service-integration-test class from the IDATT2105 project.
 */
@RunWith(SpringRunner.class)
public class UserServiceIntegrationTest {

  @TestConfiguration
  static class UserServiceTestConfiguration {

    @Bean
    public UserService userService() {
      return new UserServiceImpl();
    }
  }

  @Autowired
  UserService userService;

  @MockBean
  UserRepository userRepository;

  User existingUser;

  User nonExistingUser;

  @Before
  public void setUp() {
    // Positive tests setup
    existingUser =
      new User(
        "username",
        "email",
        "firstName",
        "lastName",
        "password",
        Role.USER
      );

    when(userRepository.findByUsername(existingUser.getUsername()))
      .thenReturn(Optional.of(existingUser));

    when(userRepository.existsById(existingUser.getUsername())).thenReturn(true);

    when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

    when(userRepository.save(existingUser)).thenReturn(existingUser);

    doNothing().when(userRepository).delete(existingUser);

    when(userRepository.findAll()).thenReturn(List.of(existingUser));

    nonExistingUser =
      new User(
        "newUsername",
        "newEmail",
        "newFirstName",
        "newLastName",
        "newPassword",
        Role.USER
      );

    when(userRepository.findByUsername(nonExistingUser.getUsername())).thenReturn(Optional.empty());

    when(userRepository.findByEmail(nonExistingUser.getEmail())).thenReturn(Optional.empty());

    when(userRepository.save(nonExistingUser)).thenReturn(nonExistingUser);

    doNothing().when(userRepository).delete(nonExistingUser);
  }

  @Test
  public void testSaveUser() {
    User newUser;
    try {
      newUser = userService.saveUser(nonExistingUser);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
      return;
    }

    assertEquals(nonExistingUser.getUsername(), newUser.getUsername());
    assertEquals(nonExistingUser.getEmail(), newUser.getEmail());
    assertEquals(nonExistingUser.getFirstName(), newUser.getFirstName());
    assertEquals(nonExistingUser.getLastName(), newUser.getLastName());
    assertEquals(nonExistingUser.getPassword(), newUser.getPassword());
    assertEquals(nonExistingUser.getRole(), newUser.getRole());
  }

  @Test
  public void testSaveUserBadUsername() {
    try {
      userService.saveUser(existingUser);
      fail();
    } catch (Exception e) {
      assertEquals(UsernameAlreadyExistsException.class, e.getClass());
    }
  }

  @Test
  public void testUsernameExists() {
    boolean exists = userService.usernameExists(existingUser.getUsername());
    assertTrue(exists);
  }

  @Test
  public void testUsernameExistsBadUsername() {
    boolean exists = userService.usernameExists(nonExistingUser.getUsername());
    assertFalse(exists);
  }

  @Test
  public void testEmailExists() {
    boolean exists = userService.emailExists(existingUser.getEmail());
    assertTrue(exists);
  }

  @Test
  public void testEmailExistsBadEmail() {
    boolean exists = userService.emailExists(nonExistingUser.getEmail());
    assertFalse(exists);
  }

  @Test
  public void testGetUserByUsername() {
    User newUser;
    try {
      newUser = userService.getUserByUsername(existingUser.getUsername());
    } catch (Exception e) {
      fail();
      return;
    }

    assertEquals(existingUser.getUsername(), newUser.getUsername());
    assertEquals(existingUser.getEmail(), newUser.getEmail());
    assertEquals(existingUser.getFirstName(), newUser.getFirstName());
    assertEquals(existingUser.getLastName(), newUser.getLastName());
    assertEquals(existingUser.getPassword(), newUser.getPassword());
    assertEquals(existingUser.getRole(), newUser.getRole());
  }

  @Test
  public void testGetUserByUsernameBadUsername() {
    try {
      userService.getUserByUsername(nonExistingUser.getUsername());
      fail();
    } catch (Exception e) {
      assertEquals(e.getClass(), UserDoesNotExistsException.class);
    }
  }

  @Test
  public void testGetUserByEmail() {
    User newUser;
    try {
      newUser = userService.getUserByEmail(existingUser.getEmail());
    } catch (Exception e) {
      fail();
      return;
    }

    assertEquals(existingUser.getUsername(), newUser.getUsername());
    assertEquals(existingUser.getEmail(), newUser.getEmail());
    assertEquals(existingUser.getFirstName(), newUser.getFirstName());
    assertEquals(existingUser.getLastName(), newUser.getLastName());
    assertEquals(existingUser.getPassword(), newUser.getPassword());
    assertEquals(existingUser.getRole(), newUser.getRole());
  }

  @Test
  public void testGetUserByEmailBadEmail() {
    try {
      userService.getUserByEmail(nonExistingUser.getEmail());
      fail();
    } catch (Exception e) {
      assertEquals(e.getClass(), UserDoesNotExistsException.class);
    }
  }

  @Test
  public void testDeleteUser() {
    try {
      userService.deleteUser(existingUser);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testDeleteUserByUsername() {
    try {
      userService.deleteUserByUsername(existingUser.getUsername());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testDeleteUserByUsernameBadUsername() {
    try {
      userService.deleteUserByUsername(nonExistingUser.getUsername());
      fail();
    } catch (Exception e) {
      assertEquals(e.getClass(), UserDoesNotExistsException.class);
    }
  }

  @Test
  public void testDeleteUserByEmail() {
    try {
      userService.deleteUserByEmail(existingUser.getEmail());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testDeleteUserByEmailBadEmail() {
    try {
      userService.deleteUserByEmail(nonExistingUser.getEmail());
      fail();
    } catch (Exception e) {
      assertEquals(e.getClass(), UserDoesNotExistsException.class);
    }
  }

  @Test
  public void testUpdateUser() {
    User newUser;
    try {
      newUser = userService.updateUser(existingUser);
    } catch (Exception e) {
      fail();
      return;
    }

    assertEquals(existingUser.getUsername(), newUser.getUsername());
    assertEquals(existingUser.getEmail(), newUser.getEmail());
    assertEquals(existingUser.getFirstName(), newUser.getFirstName());
    assertEquals(existingUser.getLastName(), newUser.getLastName());
    assertEquals(existingUser.getPassword(), newUser.getPassword());
    assertEquals(existingUser.getRole(), newUser.getRole());
  }

  @Test
  public void testUpdateUserBadUsername() {
    assertFalse(userService.usernameExists(nonExistingUser.getUsername()));
    try {
      userService.updateUser(nonExistingUser);
      fail();
    } catch (Exception e) {
      assertEquals(UserDoesNotExistsException.class, e.getClass());
    }
  }

  @Test
  public void testGetAllUsers() {
    try {
      userService.getAllUsers();
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testGetAllUsersEmpty() {
    when(userRepository.findAll()).thenReturn(new ArrayList<User>());

    try {
      userService.getAllUsers();
      fail();
    } catch (Exception e) {
      assertEquals(e.getClass(), DatabaseException.class);
    }
  }
}
