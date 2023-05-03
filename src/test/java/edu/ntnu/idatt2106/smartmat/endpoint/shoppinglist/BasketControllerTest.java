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

import edu.ntnu.idatt2106.smartmat.controller.shoppinglist.BasketController;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.Basket;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.CustomFoodItemService;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.FoodProductService;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.BasketService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
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

@RunWith(SpringRunner.class)
@WebMvcTest({ BasketController.class, SecurityConfig.class })
public class BasketControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BasketService basketService;

  @MockBean
  private HouseholdService householdService;

  @MockBean
  private FoodProductService foodProductService;

  @MockBean
  private CustomFoodItemService customFoodItemService;

  @MockBean
  private ShoppingListService shoppingListService;

  private static final String BASE_URL = "/api/v1/private/basket";

  private User user;

  private ShoppingList shoppingList;

  private Household household;

  private Basket basket;

  private Method isAdminOrHouseholdPrivileged;

  private final UUID basketId = UUID.randomUUID();

  @Before
  public void setUp() throws Exception {
    user = testUserFactory(TestUserEnum.GOOD);
    household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);

    shoppingList =
      ShoppingList
        .builder()
        .id(UUID.randomUUID())
        .household(household)
        .shoppingListItems(Set.of())
        .customFoodItems(Set.of())
        .basket(Basket.builder().id(UUID.randomUUID()).build())
        .build();

    basket =
      Basket
        .builder()
        .id(basketId)
        .shoppingList(shoppingList)
        .basketItems(new ArrayList<>())
        .customFoodItems(new HashSet<>())
        .build();

    isAdminOrHouseholdPrivileged =
      BasketController.class.getDeclaredMethod(
          "isAdminOrHouseholdPrivileged",
          Auth.class,
          UUID.class
        );
    isAdminOrHouseholdPrivileged.setAccessible(true);
  }

  @Test
  public void testCreateBasket() throws Exception {
    when(basketService.createBasket(any(Basket.class))).thenReturn(basket);
    when(shoppingListService.getShoppingListById(any(UUID.class))).thenReturn(shoppingList);
    when(householdService.getHouseholdById(any(UUID.class))).thenReturn(household);
    when(
      isAdminOrHouseholdPrivileged.invoke(
        new BasketController(
          basketService,
          householdService,
          foodProductService,
          customFoodItemService,
          shoppingListService
        ),
        new Auth(user.getUsername(), user.getRole()),
        household.getId()
      )
    )
      .thenReturn(true);

    try {
      mockMvc
        .perform(
          post(BASE_URL + "/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"shoppingListId\": \"" + shoppingList.getId() + "\" }")
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCreateBasketWithInvalidShoppingListId() throws Exception {
    when(basketService.createBasket(any(Basket.class))).thenReturn(basket);
    when(shoppingListService.getShoppingListById(any(UUID.class)))
      .thenThrow(new ShoppingListNotFoundException());
    when(householdService.getHouseholdById(any(UUID.class))).thenReturn(household);
    when(
      isAdminOrHouseholdPrivileged.invoke(
        new BasketController(
          basketService,
          householdService,
          foodProductService,
          customFoodItemService,
          shoppingListService
        ),
        new Auth(user.getUsername(), user.getRole()),
        household.getId()
      )
    )
      .thenReturn(true);

    try {
      mockMvc
        .perform(
          post(BASE_URL + "/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"shoppingListId\": \"" + shoppingList.getId() + "\" }")
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCreateBasketWithInvalidHouseholdId() throws Exception {
    when(basketService.createBasket(any(Basket.class))).thenReturn(basket);
    when(shoppingListService.getShoppingListById(any(UUID.class))).thenReturn(shoppingList);
    when(
      isAdminOrHouseholdPrivileged.invoke(
        new BasketController(
          basketService,
          householdService,
          foodProductService,
          customFoodItemService,
          shoppingListService
        ),
        new Auth(user.getUsername(), user.getRole()),
        household.getId()
      )
    )
      .thenThrow(new HouseholdNotFoundException());
    try {
      mockMvc
        .perform(
          post(BASE_URL + "/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"shoppingListId\": \"" + shoppingList.getId() + "\" }")
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCreateBasketNotPrivileged() throws Exception {
    when(basketService.createBasket(any(Basket.class))).thenReturn(basket);
    when(shoppingListService.getShoppingListById(any(UUID.class))).thenReturn(shoppingList);
    when(householdService.getHouseholdById(any(UUID.class))).thenReturn(household);
    when(
      isAdminOrHouseholdPrivileged.invoke(
        new BasketController(
          basketService,
          householdService,
          foodProductService,
          customFoodItemService,
          shoppingListService
        ),
        new Auth(user.getUsername(), user.getRole()),
        household.getId()
      )
    )
      .thenReturn(false);

    try {
      mockMvc
        .perform(
          post(BASE_URL + "/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"shoppingListId\": \"" + shoppingList.getId() + "\" }")
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail();
    }
  }
  // @Test
  // public void testAddItemToBasket() throws Exception {
  //   when(basketService.getBasketById(any(UUID.class))).thenReturn(basket);
  //   when(foodProductService.getFoodProductById(any(UUID.class))).thenReturn(testFoodProductFactory(TestFoodProductEnum.GOOD));
  //   when(
  //     isAdminOrHouseholdPrivileged.invoke(
  //       new BasketController(
  //         basketService,
  //         householdService,
  //         foodProductService,
  //         customFoodItemService,
  //         shoppingListService
  //       ),
  //       new Auth(user.getUsername(), user.getRole()),
  //       household.getId()
  //     )
  //   )
  //     .thenReturn(true);

  //   try {
  //     mockMvc
  //       .perform(
  //         post(BASE_URL + "/" + basketId + "/item")
  //           .contentType(MediaType.APPLICATION_JSON)
  //           .content("{ \"foodProductId\": \"" + basketId + "\" }")
  //           .with(authentication(createAuthenticationToken(user)))
  //       )
  //       .andExpect(status().isCreated());
  //   } catch (Exception e) {
  //     fail();
  //   }
  // }
}
