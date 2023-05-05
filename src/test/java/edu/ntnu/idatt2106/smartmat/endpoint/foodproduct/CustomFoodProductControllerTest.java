package edu.ntnu.idatt2106.smartmat.endpoint.foodproduct;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.foodproduct.CustomFoodProductController;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.CustomFoodItemService;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import edu.ntnu.idatt2106.smartmat.utils.PrivilegeUtil;
import java.util.HashSet;
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

@RunWith(SpringRunner.class)
@WebMvcTest({ CustomFoodProductController.class, SecurityConfig.class })
public class CustomFoodProductControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ShoppingListService shoppingListService;

  @MockBean
  private CustomFoodItemService customFoodItemService;

  @MockBean
  private IngredientService ingredientService;

  @MockBean
  private HouseholdService householdService;

  private static final String BASE_URL = "/api/v1/private/customfooditems";

  private Household household;

  private User admin;

  private User user;

  private ShoppingList shoppingList;

  private final UUID shoppingListId = UUID.randomUUID();

  private final UUID customFoodItemId = UUID.randomUUID();

  private CustomFoodItem customFoodItem;

  @Before
  public void setUp() {
    household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
    admin = testUserFactory(TestUserEnum.ADMIN);
    user = testUserFactory(TestUserEnum.GOOD);
    shoppingList =
      new ShoppingList(shoppingListId, null, household, new HashSet<>(), new HashSet<>(), null);
    customFoodItem =
      new CustomFoodItem(customFoodItemId, "FoodItem", 1, false, shoppingList, household, null);
  }

  @Test
  public void testAddItemToShoppingListUserIsPrivilegedMemberAndItemIsAdded() throws Exception {
    when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(shoppingList);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(customFoodItemService.existsById(shoppingListId)).thenReturn(false);
    when(customFoodItemService.saveCustomFoodItem(any(CustomFoodItem.class)))
      .thenReturn(customFoodItem);
    try {
      mvc
        .perform(
          post(BASE_URL)
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{" +
              "\"name\": \"Test\"," +
              "\"amount\": 1," +
              "\"shoppingListId\": \"" +
              shoppingListId.toString() +
              "\"" +
              "}"
            )
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testAddItemToShoppingListUserIsPrivilegedMemberAndItemIsAlreadyInList()
    throws Exception {
    when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(shoppingList);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(customFoodItemService.saveCustomFoodItem(any(CustomFoodItem.class)))
      .thenReturn(customFoodItem);
    try {
      mvc
        .perform(
          post(BASE_URL)
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{" +
              "\"name\": \"Test\"," +
              "\"amount\": 1," +
              "\"shoppingListId\": \"" +
              shoppingListId.toString() +
              "\"" +
              "}"
            )
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testAddItemToShoppingListUserIsNotPrivilegedMember() throws Exception {
    when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(shoppingList);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(false);
    try {
      mvc
        .perform(
          post(BASE_URL)
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{" +
              "\"name\": \"Test\"," +
              "\"amount\": 1," +
              "\"shoppingListId\": \"" +
              shoppingListId.toString() +
              "\"" +
              "}"
            )
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testAddItemToShoppingListUserIsNotMember() throws Exception {
    when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(shoppingList);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(false);
    try {
      mvc
        .perform(
          post(BASE_URL)
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{" +
              "\"name\": \"Test\"," +
              "\"amount\": 1," +
              "\"shoppingListId\": \"" +
              shoppingListId.toString() +
              "\"" +
              "}"
            )
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testAddItemToShoppingListUserIsAdmin() throws Exception {
    when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(shoppingList);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(customFoodItemService.saveCustomFoodItem(any(CustomFoodItem.class)))
      .thenReturn(customFoodItem);
    try {
      mvc
        .perform(
          post(BASE_URL)
            .with(authentication(createAuthenticationToken(admin)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{" +
              "\"name\": \"Test\"," +
              "\"amount\": 1," +
              "\"shoppingListId\": \"" +
              shoppingListId.toString() +
              "\"" +
              "}"
            )
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testDeleteItemFromShoppingListUserIsPrivilegedAndItemExists() throws Exception {
    doNothing().when(customFoodItemService).deleteCustomFoodItem(customFoodItemId);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(true);
    try {
      mvc
        .perform(
          delete(
            BASE_URL + "/household/" + household.getId() + "/item/" + customFoodItemId.toString()
          )
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNoContent());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testDeleteItemFromShoppingListUserIsPrivilegedAndItemDoesNotExist() throws Exception {
    doThrow(new ShoppingListItemNotFoundException())
      .when(customFoodItemService)
      .deleteCustomFoodItem(customFoodItemId);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(true);
    try {
      mvc
        .perform(
          delete(
            BASE_URL + "/household/" + household.getId() + "/item/" + customFoodItemId.toString()
          )
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testDeleteItemFromShoppingListUserIsNotPrivileged() throws Exception {
    doNothing().when(customFoodItemService).deleteCustomFoodItem(customFoodItemId);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(false);
    try {
      mvc
        .perform(
          delete(
            BASE_URL + "/household/" + household.getId() + "/item/" + customFoodItemId.toString()
          )
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testDeleteItemFromShoppingListUserIsNotInHouseholdButIsAdmin() throws Exception {
    doNothing().when(customFoodItemService).deleteCustomFoodItem(customFoodItemId);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(true);
    try {
      mvc
        .perform(
          delete(
            BASE_URL + "/household/" + household.getId() + "/item/" + customFoodItemId.toString()
          )
            .with(authentication(createAuthenticationToken(admin)))
        )
        .andExpect(status().isNoContent());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCheckItemThatExistsUserIsPrivilegedMember() throws Exception {
    when(customFoodItemService.getItemById(customFoodItemId)).thenReturn(customFoodItem);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(customFoodItemService.updateCustomFoodItem(any(CustomFoodItem.class)))
      .thenReturn(customFoodItem);
    try {
      mvc
        .perform(
          put(BASE_URL + "/check-item/" + customFoodItemId.toString())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCheckItemThatExistsUserIsNotPrivilegedMember() throws Exception {
    when(customFoodItemService.getItemById(customFoodItemId)).thenReturn(customFoodItem);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(false);
    when(customFoodItemService.updateCustomFoodItem(any(CustomFoodItem.class)))
      .thenReturn(customFoodItem);
    try {
      mvc
        .perform(
          put(BASE_URL + "/check-item/" + customFoodItemId.toString())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCheckItemThatDoesNotExistUserIsPrivilegedMember() throws Exception {
    when(customFoodItemService.getItemById(customFoodItemId)).thenReturn(customFoodItem);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(customFoodItemService.updateCustomFoodItem(any(CustomFoodItem.class)))
      .thenThrow(new ShoppingListItemNotFoundException());
    try {
      mvc
        .perform(
          put(BASE_URL + "/check-item/" + customFoodItemId.toString())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCheckItemThatDoesNotExistUserIsNotPrivilegedMember() throws Exception {
    when(customFoodItemService.getItemById(customFoodItemId)).thenReturn(customFoodItem);
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        household.getId(),
        householdService
      )
    )
      .thenReturn(false);
    when(customFoodItemService.updateCustomFoodItem(any(CustomFoodItem.class)))
      .thenThrow(new ShoppingListItemNotFoundException());
    try {
      mvc
        .perform(
          put(BASE_URL + "/check-item/" + customFoodItemId.toString())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail();
    }
  }
}
