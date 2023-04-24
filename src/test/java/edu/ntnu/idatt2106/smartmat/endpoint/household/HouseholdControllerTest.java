package edu.ntnu.idatt2106.smartmat.endpoint.household;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.createAuthenticationToken;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.household.HouseholdController;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link HouseholdController}.
 * All test endpoints are private and require authentication, this
 * is already tested, so we only test the controller.
 * @author Callum G.
 * @version 1.0 - 19.04.2023
 */
@RunWith(SpringRunner.class)
@WebMvcTest({ HouseholdController.class, SecurityConfig.class })
public class HouseholdControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private HouseholdService householdService;

  @MockBean
  private UserService userService;

  private static final String BASE_URL = "/api/v1/private/households";

  private User user;

  private HouseholdMember userHouseholdMember;

  private Household household;

  private Household newHousehold;

  @Before
  public void setUp() {
    user = testUserFactory(TestUserEnum.GOOD);

    household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);

    userHouseholdMember =
      HouseholdMember
        .builder()
        .user(user)
        .household(household)
        .householdRole(HouseholdRole.OWNER)
        .build();

    newHousehold = testHouseholdFactory(TestHouseholdEnum.NEW_HOUSEHOLD);

    try {
      // Get user
      when(userService.getUserByUsername(user.getUsername())).thenReturn(user);

      // Update user
      when(userService.updateUser(any(User.class))).thenReturn(user);

      // Household exists
      when(householdService.householdExists(household.getId())).thenReturn(true);
      when(householdService.householdExists(newHousehold.getId())).thenReturn(false);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testSaveHousehold() {
    try {
      when(householdService.saveHousehold(any(Household.class))).thenReturn(newHousehold);
      newHousehold.setId(UUID.randomUUID());
      newHousehold.setMembers(Set.of(userHouseholdMember));
      mvc
        .perform(
          post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"TestHousehold\"}")
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetExistingHouseHold() {
    try {
      when(householdService.getHouseholdById(household.getId())).thenReturn(household);
      mvc
        .perform(
          get(BASE_URL + "/" + household.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetNonExistingHouseHold() {
    try {
      when(householdService.getHouseholdById(household.getId()))
        .thenThrow(new HouseholdNotFoundException());
      mvc
        .perform(
          get(BASE_URL + "/" + household.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testUpdateHouseholdName() {
    try {
      when(householdService.getHouseholdById(household.getId())).thenReturn(household);
      when(householdService.updateHouseholdName(household.getId(), "NewName"))
        .thenReturn(household);
      when(householdService.isHouseholdOwner(household.getId(), user.getUsername()))
        .thenReturn(true);
      household.setName("NewName");
      mvc
        .perform(
          put(BASE_URL + "/" + household.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(String.format("{\"id\": \"%s\", \"name\": \"NewName\"}", household.getId()))
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testUpdateHouseholdNameNotOwner() {
    try {
      when(householdService.getHouseholdById(household.getId())).thenReturn(household);
      when(householdService.updateHouseholdName(household.getId(), "NewName"))
        .thenReturn(household);
      when(householdService.isHouseholdOwner(household.getId(), user.getUsername()))
        .thenReturn(false);
      household.setName("NewName");
      mvc
        .perform(
          put(BASE_URL + "/" + household.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(String.format("{\"id\": \"%s\", \"name\": \"NewName\"}", household.getId()))
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testUpdateHouseholdNameWithNonExistingHousehold() {
    try {
      when(householdService.updateHouseholdName(household.getId(), "NewName"))
        .thenThrow(new HouseholdNotFoundException());
      when(householdService.isHouseholdOwner(household.getId(), user.getUsername()))
        .thenThrow(new HouseholdNotFoundException());
      mvc
        .perform(
          put(BASE_URL + "/" + household.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(String.format("{\"id\": \"%s\", \"name\": \"NewName\"}", household.getId()))
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDeleteHousehold() {
    try {
      doNothing().when(householdService).deleteHouseholdById(household.getId());
      when(householdService.isHouseholdOwner(household.getId(), user.getUsername()))
        .thenReturn(true);
      mvc
        .perform(
          delete(String.format("%s/%s", BASE_URL, household.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNoContent());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDeleteNonExistingHousehold() {
    try {
      doThrow(new HouseholdNotFoundException())
        .when(householdService)
        .deleteHouseholdById(household.getId());
      when(householdService.isHouseholdOwner(household.getId(), user.getUsername()))
        .thenThrow(new HouseholdNotFoundException());
      mvc
        .perform(
          delete(String.format("%s/%s", BASE_URL, household.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetHouseholdsByUser() {
    try {
      when(householdService.getHouseholdsByUser(user.getUsername())).thenReturn(Set.of(household));
      mvc
        .perform(
          get(BASE_URL + "/user/" + user.getUsername())
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDeleteHouseholdOwner() {
    try {
      doNothing().when(householdService).deleteHouseholdById(household.getId());
      when(householdService.isHouseholdOwner(household.getId(), user.getUsername()))
        .thenReturn(true);
      mvc
        .perform(
          delete(String.format("%s/%s", BASE_URL, household.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNoContent());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testAddUserThatAlreadyExists() {
    try {
      when(householdService.getHouseholdById(household.getId())).thenReturn(household);
      when(householdService.isHouseholdOwner(household.getId(), user.getUsername()))
        .thenReturn(true);
      when(householdService.isHouseholdMember(household.getId(), user.getUsername()))
        .thenReturn(true);
      when(
        householdService.addHouseholdMember(
          household.getId(),
          user.getUsername(),
          HouseholdRole.MEMBER
        )
      )
        .thenReturn(new HouseholdMember(household, user, HouseholdRole.MEMBER));
      mvc
        .perform(
          post(String.format("%s/%s/user/%s", BASE_URL, household.getId(), user.getUsername()))
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isConflict());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
