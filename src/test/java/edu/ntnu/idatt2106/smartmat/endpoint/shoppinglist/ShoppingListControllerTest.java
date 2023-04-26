package edu.ntnu.idatt2106.smartmat.endpoint.shoppinglist;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.createAuthenticationToken;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.shoppinglist.ShoppingListController;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.CustomFoodItemService;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListItemService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
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

// RunWith is used to provide a bridge between Spring Boot test features and JUnit.
// Whenever we are using any Spring Boot testing features in our JUnit tests, this annotation will be required.
@RunWith(SpringRunner.class)
@WebMvcTest({ ShoppingListController.class, SecurityConfig.class })
public class ShoppingListControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ShoppingListService shoppingListService;

  @MockBean
  private HouseholdService householdService;

  @MockBean
  private IngredientService ingredientService;

  @MockBean
  private ShoppingListItemService shoppingListItemService;

  @MockBean
  private CustomFoodItemService customFoodItemService;

  @MockBean
  private UserService userService;

  private static final String BASE_URL = "/api/v1/private/shoppinglists";

  private ShoppingList shoppingList;
  private Household household;

  private User user;
  private HouseholdMember userHouseholdMember;

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

    shoppingList =
      ShoppingList
        .builder()
        .id(UUID.randomUUID())
        .household(household)
        .shoppingListItems(Set.of())
        .customFoodItems(Set.of())
        .build();

    try {
      when(shoppingListService.getShoppingListById(shoppingList.getId())).thenReturn(shoppingList);
      // Get user
      when(userService.getUserByUsername(user.getUsername())).thenReturn(user);

      // set as household owner and member
      when(householdService.isHouseholdMember(household.getId(), user.getUsername()))
        .thenReturn(true);
      when(householdService.isHouseholdOwner(household.getId(), user.getUsername()))
        .thenReturn(true);

      // Household exists
      when(householdService.householdExists(household.getId())).thenReturn(true);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testShoppingListThatExists() {
    try {
      when(shoppingListService.getShoppingListById(shoppingList.getId())).thenReturn(shoppingList);
      household.setMembers(Set.of(userHouseholdMember));

      mvc
        .perform(
          get(BASE_URL + "/" + shoppingList.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testShoppingListThatDoesNotExist() {
    try {
      when(shoppingListService.getShoppingListById(shoppingList.getId()))
        .thenThrow(new ShoppingListNotFoundException());
      mvc
        .perform(
          get(BASE_URL + "/" + shoppingList.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testCreateNewShoppingList() {
    try {
      when(shoppingListService.getCurrentShoppingList(shoppingList.getHousehold().getId()))
        .thenThrow(new ShoppingListNotFoundException());
      when(householdService.getHouseholdById(any(UUID.class))).thenReturn(household);
      when(shoppingListService.saveShoppingList(any(ShoppingList.class))).thenReturn(shoppingList);
      mvc
        .perform(
          post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(String.format("{\"household\": \"%s\"}", shoppingList.getHousehold().getId()))
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
