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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.shoppinglist.ShoppingListController;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
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

  private Ingredient ingredient;
  private ShoppingListItem shoppingListItem;
  private CustomFoodItem customFoodItem;

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

    ingredient = Ingredient.builder().id(1L).name("Ingredient").build();
    shoppingListItem =
      ShoppingListItem
        .builder()
        .id(UUID.randomUUID())
        .amount(10.3)
        .ingredient(ingredient)
        .shoppingList(shoppingList)
        .build();
    customFoodItem =
      CustomFoodItem
        .builder()
        .id(UUID.randomUUID())
        .name("Ingredient")
        .amount(4)
        .shoppingList(shoppingList)
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
  public void testAddCustomItemToShoppingList() {
    try {
      when(ingredientService.getIngredientsByName(ingredient.getName())).thenReturn(Set.of());
      when(customFoodItemService.saveCustomFoodItem(any(CustomFoodItem.class)))
        .thenReturn(customFoodItem);
      when(shoppingListService.updateShoppingList(shoppingList.getId(), shoppingList))
        .thenReturn(shoppingList);
      mvc
        .perform(
          post(BASE_URL + "/" + shoppingList.getId() + "/add-item")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              String.format(
                "{\"amount\": %s, \"name\": \"%s\"}",
                customFoodItem.getAmount(),
                ingredient.getName()
              )
            )
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testRemoveCustomItemFromShoppingList() {
    try {
      doThrow(ShoppingListItemNotFoundException.class)
        .when(shoppingListItemService)
        .deleteShoppingListItemInShoppingList(shoppingList.getId(), customFoodItem.getId());
      doNothing()
        .when(customFoodItemService)
        .deleteCustomFoodItemInShoppingList(shoppingList.getId(), customFoodItem.getId());
      mvc
        .perform(
          delete(BASE_URL + "/" + shoppingList.getId() + "/delete-item/" + customFoodItem.getId())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNoContent());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testRemoveShoppingListItemFromShoppingList() {
    try {
      doNothing()
        .when(shoppingListItemService)
        .deleteShoppingListItemInShoppingList(shoppingList.getId(), shoppingListItem.getId());
      mvc
        .perform(
          delete(BASE_URL + "/" + shoppingList.getId() + "/delete-item/" + shoppingListItem.getId())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNoContent());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testCheckShoppingListItemFromShoppingList() {
    try {
      when(
        shoppingListItemService.existsByIdInShoppingList(
          shoppingList.getId(),
          shoppingListItem.getId()
        )
      )
        .thenReturn(true);
      when(shoppingListItemService.getItemById(shoppingListItem.getId()))
        .thenReturn(shoppingListItem);
      when(shoppingListItemService.saveShoppingListItem(shoppingListItem))
        .thenReturn(shoppingListItem);
      mvc
        .perform(
          put(BASE_URL + "/" + shoppingList.getId() + "/check-item/" + shoppingListItem.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
      mvc
        .perform(
          put(BASE_URL + "/" + shoppingList.getId() + "/check-item/" + shoppingListItem.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testCheckingItemNotInShoppinglist() {
    UUID randomId = UUID.randomUUID();
    try {
      when(shoppingListItemService.existsByIdInShoppingList(shoppingList.getId(), randomId))
        .thenReturn(false);
      when(shoppingListItemService.existsByIdInShoppingList(shoppingList.getId(), randomId))
        .thenReturn(false);
      mvc
        .perform(
          put(BASE_URL + "/" + shoppingList.getId() + "/check-item/" + randomId)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
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
