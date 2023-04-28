package edu.ntnu.idatt2106.smartmat.endpoint.shoppinglist;

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

import edu.ntnu.idatt2106.smartmat.controller.shoppinglist.ShoppingListItemController;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListItemService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
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
@WebMvcTest({ ShoppingListItemController.class, SecurityConfig.class })
public class ShoppingListItemControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ShoppingListItemService shoppingListItemService;

  @MockBean
  private ShoppingListService shoppingListService;

  @MockBean
  private IngredientService ingredientService;

  @MockBean
  private HouseholdService householdService;

  private static final String BASE_URL = "/api/v1/private/shoppinglistitems";

  private Household household;

  private User admin;

  private User user;

  private ShoppingList shoppingList;

  private final UUID shoppingListId = UUID.randomUUID();

  private final UUID shoppingListItemId = UUID.randomUUID();

  private final Long ingredientId = 1L;

  private ShoppingListItem shoppingListItem;

  private Ingredient ingredient;

  @Before
  public void setUp() {
    household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
    admin = testUserFactory(TestUserEnum.ADMIN);
    user = testUserFactory(TestUserEnum.GOOD);
    shoppingList = new ShoppingList(shoppingListId, null, household, new HashSet<>(), null);
    shoppingListItem = new ShoppingListItem(shoppingListItemId, 1, false, shoppingList, null);
    ingredient = new Ingredient(ingredientId, "Test", null, null, new Unit("Test", "Test", null));
  }

  @Test
  public void testAddItemToShoppingListUserIsPrivilegedMemberAndItemIsAdded() throws Exception {
    when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(shoppingList);
    when(
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        user.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(true);
    when(shoppingListItemService.existsById(shoppingListId)).thenReturn(false);
    when(shoppingListItemService.saveShoppingListItem(any(ShoppingListItem.class)))
      .thenReturn(shoppingListItem);
    when(ingredientService.getIngredientById(ingredientId)).thenReturn(ingredient);
    try {
      mvc
        .perform(
          post(BASE_URL)
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\"name\": \"Test\", \"amount\": 1, \"ingredientId\": " +
              ingredientId +
              ",\"shoppingListId\": \"" +
              shoppingListId.toString() +
              "\"}"
            )
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testAddItemToShoppingListUserIsPrivilegedMemberAndItemIsAlreadyInList()
    throws Exception {
    when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(shoppingList);
    when(
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        user.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(true);
    when(shoppingListItemService.existsById(shoppingListId)).thenReturn(true);
    when(shoppingListItemService.saveShoppingListItem(any(ShoppingListItem.class)))
      .thenReturn(shoppingListItem);
    when(ingredientService.getIngredientById(ingredientId)).thenReturn(ingredient);
    try {
      mvc
        .perform(
          post(BASE_URL)
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\"name\": \"Test\", \"amount\": 1, \"ingredientId\": " +
              ingredientId +
              ",\"shoppingListId\": \"" +
              shoppingListId.toString() +
              "\"}"
            )
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testAddItemToShoppingListUserIsNotPrivilegedMember() throws Exception {
    when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(shoppingList);
    when(
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        user.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(false);
    when(shoppingListItemService.existsById(shoppingListId)).thenReturn(false);
    when(shoppingListItemService.saveShoppingListItem(any(ShoppingListItem.class)))
      .thenReturn(shoppingListItem);
    when(ingredientService.getIngredientById(ingredientId)).thenReturn(ingredient);
    try {
      mvc
        .perform(
          post(BASE_URL)
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\"name\": \"Test\", \"amount\": 1, \"ingredientId\": " +
              ingredientId +
              ",\"shoppingListId\": \"" +
              shoppingListId.toString() +
              "\"}"
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
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        user.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(false);
    when(shoppingListItemService.existsById(shoppingListId)).thenReturn(false);
    when(shoppingListItemService.saveShoppingListItem(any(ShoppingListItem.class)))
      .thenReturn(shoppingListItem);
    when(ingredientService.getIngredientById(ingredientId)).thenReturn(ingredient);
    try {
      mvc
        .perform(
          post(BASE_URL)
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\"name\": \"Test\", \"amount\": 1, \"ingredientId\": " +
              ingredientId +
              ",\"shoppingListId\": \"" +
              shoppingListId.toString() +
              "\"}"
            )
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testAddItemToShoppingListUserIsNotMemberButIsAdmin() throws Exception {
    when(shoppingListService.getShoppingListById(shoppingListId)).thenReturn(shoppingList);
    when(shoppingListItemService.existsById(shoppingListId)).thenReturn(false);
    when(shoppingListItemService.saveShoppingListItem(any(ShoppingListItem.class)))
      .thenReturn(shoppingListItem);
    when(ingredientService.getIngredientById(ingredientId)).thenReturn(ingredient);
    try {
      mvc
        .perform(
          post(BASE_URL)
            .with(authentication(createAuthenticationToken(admin)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\"name\": \"Test\", \"amount\": 1, \"ingredientId\": " +
              ingredientId +
              ",\"shoppingListId\": \"" +
              shoppingListId.toString() +
              "\"}"
            )
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testDeleteItemFromShoppingListUserIsPrivilegedAndItemExists() throws Exception {
    doNothing().when(shoppingListItemService).deleteShoppingListItem(shoppingListItemId);
    when(
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        user.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(true);
    try {
      mvc
        .perform(
          delete(
            BASE_URL +
            "/household/" +
            household.getId().toString() +
            "/item/" +
            shoppingListItemId.toString()
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
      .when(shoppingListItemService)
      .deleteShoppingListItem(shoppingListItemId);
    when(
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        user.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(true);
    try {
      mvc
        .perform(
          delete(
            BASE_URL +
            "/household/" +
            household.getId().toString() +
            "/item/" +
            shoppingListItemId.toString()
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
    doNothing().when(shoppingListItemService).deleteShoppingListItem(shoppingListItemId);
    when(
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        user.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(false);
    try {
      mvc
        .perform(
          delete(
            BASE_URL +
            "/household/" +
            household.getId().toString() +
            "/item/" +
            shoppingListItemId.toString()
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
    doNothing().when(shoppingListItemService).deleteShoppingListItem(shoppingListItemId);
    when(
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        admin.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(false);
    try {
      mvc
        .perform(
          delete(
            BASE_URL +
            "/household/" +
            household.getId().toString() +
            "/item/" +
            shoppingListItemId.toString()
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
    when(shoppingListItemService.getItemById(shoppingListItemId)).thenReturn(shoppingListItem);
    when(
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        user.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(true);
    when(shoppingListItemService.updateShoppingListItem(any(ShoppingListItem.class)))
      .thenReturn(shoppingListItem);
    try {
      mvc
        .perform(
          put(BASE_URL + "/check-item/" + shoppingListItemId.toString())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCheckItemThatExistsUserIsNotPrivilegedMember() throws Exception {
    when(shoppingListItemService.getItemById(shoppingListItemId)).thenReturn(shoppingListItem);
    when(
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        user.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(false);
    when(shoppingListItemService.updateShoppingListItem(any(ShoppingListItem.class)))
      .thenReturn(shoppingListItem);
    try {
      mvc
        .perform(
          put(BASE_URL + "/check-item/" + shoppingListItemId.toString())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCheckItemThatDoesNotExistUserIsPrivilegedMember() throws Exception {
    when(shoppingListItemService.getItemById(shoppingListItemId))
      .thenThrow(new ShoppingListItemNotFoundException());
    when(
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        user.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(true);
    when(shoppingListItemService.updateShoppingListItem(any(ShoppingListItem.class)))
      .thenReturn(shoppingListItem);
    try {
      mvc
        .perform(
          put(BASE_URL + "/check-item/" + shoppingListItemId.toString())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCheckItemThatExistsUserIsAdmin() throws Exception {
    when(shoppingListItemService.getItemById(shoppingListItemId)).thenReturn(shoppingListItem);
    when(
      householdService.isHouseholdMemberWithRole(
        household.getId(),
        admin.getUsername(),
        HouseholdRole.PRIVILEGED_MEMBER
      )
    )
      .thenReturn(false);
    when(shoppingListItemService.updateShoppingListItem(any(ShoppingListItem.class)))
      .thenReturn(shoppingListItem);
    try {
      mvc
        .perform(
          put(BASE_URL + "/check-item/" + shoppingListItemId.toString())
            .with(authentication(createAuthenticationToken(admin)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail();
    }
  }
}
