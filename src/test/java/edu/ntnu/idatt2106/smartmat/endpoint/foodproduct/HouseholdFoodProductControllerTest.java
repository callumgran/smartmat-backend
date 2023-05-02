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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.foodproduct.HouseholdFoodProductController;
import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.FoodProductService;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.HouseholdFoodProductService;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.statistic.FoodProductHistoryService;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
@WebMvcTest({ HouseholdFoodProductController.class, SecurityConfig.class })
public class HouseholdFoodProductControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private HouseholdFoodProductService householdFoodProductService;

  @MockBean
  private HouseholdService householdService;

  @MockBean
  private FoodProductService foodProductService;

  @MockBean
  private FoodProductHistoryService foodProductHistoryService;

  private static final String BASE_URL = "/api/v1/private/households";

  private static final UUID HOUSEHOLD_ID = UUID.randomUUID();

  private static final String HOUSEHOLD_ID_STRING = HOUSEHOLD_ID.toString();

  private static final UUID FOOD_PRODUCT_ID = UUID.randomUUID();

  private static final String FOOD_PRODUCT_ID_STRING = FOOD_PRODUCT_ID.toString();

  private static final String BASE_HOUSEHOLD_URL = BASE_URL + "/" + HOUSEHOLD_ID_STRING;

  private static final String BASE_HOUSEHOLD_FOOD_PRODUCT_URL =
    BASE_HOUSEHOLD_URL + "/foodproducts/" + FOOD_PRODUCT_ID_STRING;

  private User user;

  private User admin;

  private Ingredient carrot;

  private FoodProduct carrotProduct;

  private Household household;

  private HouseholdFoodProduct carrotHouseholdProduct;

  private Method isAdminOrHouseholdMember;

  private Method isAdminOrHouseholdPrivileged;

  private Method isAdminOrHouseholdOwner;

  @Before
  public void setUp() throws NoSuchMethodException, SecurityException {
    Unit unit = new Unit("kilogram", "kg", new HashSet<>(), 1, UnitTypeEnum.SOLID);
    user = testUserFactory(TestUserEnum.GOOD);
    admin = testUserFactory(TestUserEnum.ADMIN);
    carrot = new Ingredient(1L, "Carrot", null, null, null);
    carrotProduct =
      new FoodProduct(
        1L,
        "CarrotProduct",
        "123456789",
        1.0,
        false,
        null,
        null,
        carrot,
        null,
        false,
        unit,
        null
      );
    household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
    household.setId(HOUSEHOLD_ID);
    carrotHouseholdProduct =
      new HouseholdFoodProduct(FOOD_PRODUCT_ID, carrotProduct, household, LocalDate.now(), 1D);

    isAdminOrHouseholdMember =
      HouseholdFoodProductController.class.getDeclaredMethod(
          "isAdminOrHouseholdMember",
          Auth.class,
          UUID.class
        );
    isAdminOrHouseholdMember.setAccessible(true);

    isAdminOrHouseholdPrivileged =
      HouseholdFoodProductController.class.getDeclaredMethod(
          "isAdminOrHouseholdPrivileged",
          Auth.class,
          UUID.class
        );
    isAdminOrHouseholdPrivileged.setAccessible(true);

    isAdminOrHouseholdOwner =
      HouseholdFoodProductController.class.getDeclaredMethod(
          "isAdminOrHouseholdOwner",
          Auth.class,
          UUID.class
        );
    isAdminOrHouseholdOwner.setAccessible(true);
  }

  @Test
  public void testGetHouseholdFoodProductExistingProductAndHouseholdAndUserIsHouseholdMember() {
    try {
      when(
        isAdminOrHouseholdMember.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(true);
      when(householdFoodProductService.getFoodProductById(FOOD_PRODUCT_ID))
        .thenReturn(carrotHouseholdProduct);
      mvc
        .perform(
          get(BASE_HOUSEHOLD_FOOD_PRODUCT_URL).with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testGetHouseholdFoodProductExistingProductAndHouseholdAndUserIsNotHouseholdMember() {
    try {
      when(
        isAdminOrHouseholdMember.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(false);
      when(householdFoodProductService.getFoodProductById(FOOD_PRODUCT_ID))
        .thenReturn(carrotHouseholdProduct);
      mvc
        .perform(
          get(BASE_HOUSEHOLD_FOOD_PRODUCT_URL).with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testGetHouseholdFoodProductExistingProductAndHouseholdAndUserIsAdmin() {
    try {
      when(householdFoodProductService.getFoodProductById(FOOD_PRODUCT_ID))
        .thenReturn(carrotHouseholdProduct);
      mvc
        .perform(
          get(BASE_HOUSEHOLD_FOOD_PRODUCT_URL)
            .with(authentication(createAuthenticationToken(admin)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testGetHouseholdFoodProductNonExistingProductAndUserIsHouseholdMember() {
    try {
      when(
        isAdminOrHouseholdMember.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(true);
      when(householdFoodProductService.getFoodProductById(FOOD_PRODUCT_ID))
        .thenThrow(new FoodProductNotFoundException());
      mvc
        .perform(
          get(BASE_HOUSEHOLD_FOOD_PRODUCT_URL).with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testGetHouseholdFoodProductNonExistingProductAndUserIsNotHouseholdMember() {
    try {
      when(
        isAdminOrHouseholdMember.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(false);
      when(householdFoodProductService.getFoodProductById(FOOD_PRODUCT_ID))
        .thenThrow(new FoodProductNotFoundException());
      mvc
        .perform(
          get(BASE_HOUSEHOLD_FOOD_PRODUCT_URL).with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testGetHouseholdFoodProductByEanExistingAndUserIsHouseholdMember() {
    try {
      when(
        isAdminOrHouseholdMember.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(true);
      when(
        householdFoodProductService.findHouseholdFoodProductByIdAndEAN(
          HOUSEHOLD_ID,
          carrotProduct.getEAN()
        )
      )
        .thenReturn(List.of(carrotHouseholdProduct));
      mvc
        .perform(
          get(BASE_HOUSEHOLD_URL + "/foodproducts/ean/" + carrotProduct.getEAN())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testGetHouseholdFoodProductByEanExistingAndUserIsNotHouseholdMember() {
    try {
      when(
        isAdminOrHouseholdMember.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(false);
      when(
        householdFoodProductService.findHouseholdFoodProductByIdAndEAN(
          HOUSEHOLD_ID,
          carrotProduct.getEAN()
        )
      )
        .thenReturn(List.of(carrotHouseholdProduct));
      mvc
        .perform(
          get(BASE_HOUSEHOLD_URL + "/foodproducts/ean/" + carrotProduct.getEAN())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testGetHouseholdFoodProductByEanExistingAndUserIsAdmin() {
    try {
      when(
        householdFoodProductService.findHouseholdFoodProductByIdAndEAN(
          HOUSEHOLD_ID,
          carrotProduct.getEAN()
        )
      )
        .thenReturn(List.of(carrotHouseholdProduct));
      mvc
        .perform(
          get(BASE_HOUSEHOLD_URL + "/foodproducts/ean/" + carrotProduct.getEAN())
            .with(authentication(createAuthenticationToken(admin)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testGetHouseholdFoodProductByEanNonExistingAndUserIsHouseholdMember() {
    try {
      when(
        isAdminOrHouseholdMember.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(true);
      when(
        householdFoodProductService.findHouseholdFoodProductByIdAndEAN(
          HOUSEHOLD_ID,
          carrotProduct.getEAN()
        )
      )
        .thenThrow(new FoodProductNotFoundException());
      mvc
        .perform(
          get(BASE_HOUSEHOLD_URL + "/foodproducts/ean/" + carrotProduct.getEAN())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testGetHouseholdFoodProductByEanNonExistingAndUserIsNotHouseholdMember() {
    try {
      when(
        isAdminOrHouseholdMember.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(false);
      when(
        householdFoodProductService.findHouseholdFoodProductByIdAndEAN(
          HOUSEHOLD_ID,
          carrotProduct.getEAN()
        )
      )
        .thenThrow(new FoodProductNotFoundException());
      mvc
        .perform(
          get(BASE_HOUSEHOLD_URL + "/foodproducts/ean/" + carrotProduct.getEAN())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateHouseholdFoodProductUserIsPrivilegedMember() {
    try {
      when(
        isAdminOrHouseholdPrivileged.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(true);
      when(householdFoodProductService.saveFoodProduct(any(HouseholdFoodProduct.class)))
        .thenReturn(carrotHouseholdProduct);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          post(BASE_HOUSEHOLD_URL + "/foodproducts")
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              String.format(
                "{\"foodProductId\": %d, \"expirationDate\": \"2023-12-02\", \"amountLeft\": 1}",
                carrotProduct.getId()
              )
            )
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateHouseholdFoodProductUserIsNotPrivilegedMember() {
    try {
      when(
        isAdminOrHouseholdPrivileged.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(false);
      when(householdFoodProductService.saveFoodProduct(any(HouseholdFoodProduct.class)))
        .thenReturn(carrotHouseholdProduct);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          post(BASE_HOUSEHOLD_URL + "/foodproducts")
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              String.format(
                "{\"foodProductId\": %d, \"expirationDate\": \"2023-12-02\", \"amountLeft\": 1}",
                carrotProduct.getId()
              )
            )
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateHouseholdFoodProductUserIsOwner() {
    try {
      when(
        isAdminOrHouseholdOwner.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(true);
      when(householdFoodProductService.saveFoodProduct(any(HouseholdFoodProduct.class)))
        .thenReturn(carrotHouseholdProduct);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          post(BASE_HOUSEHOLD_URL + "/foodproducts")
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              String.format(
                "{\"foodProductId\": %d, \"expirationDate\": \"2023-12-02\", \"amountLeft\": 1}",
                carrotProduct.getId()
              )
            )
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateHouseholdFoodProductUserIsNotHouseholdMember() {
    try {
      when(
        isAdminOrHouseholdOwner.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(false);
      when(householdFoodProductService.saveFoodProduct(any(HouseholdFoodProduct.class)))
        .thenReturn(carrotHouseholdProduct);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          post(BASE_HOUSEHOLD_URL + "/foodproducts")
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              String.format(
                "{\"foodProductId\": %d, \"expirationDate\": \"2023-12-02\", \"amountLeft\": 1}",
                carrotProduct.getId()
              )
            )
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateHouseholdFoodProductUserIsNotMemberButIsAdmin() {
    try {
      when(
        isAdminOrHouseholdOwner.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(false);
      when(householdFoodProductService.saveFoodProduct(any(HouseholdFoodProduct.class)))
        .thenReturn(carrotHouseholdProduct);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          post(BASE_HOUSEHOLD_URL + "/foodproducts")
            .with(authentication(createAuthenticationToken(admin)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              String.format(
                "{\"foodProductId\": %d, \"expirationDate\": \"2023-12-02\", \"amountLeft\": 1}",
                carrotProduct.getId()
              )
            )
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testUpdateHouseholdFoodProductUserIsPrivilegedMemberAndProductExists() {
    try {
      when(
        isAdminOrHouseholdPrivileged.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(true);
      when(householdFoodProductService.updateFoodProduct(any(HouseholdFoodProduct.class)))
        .thenReturn(carrotHouseholdProduct);
      when(householdFoodProductService.existsById(carrotHouseholdProduct.getId())).thenReturn(true);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          put(BASE_HOUSEHOLD_URL + "/foodproducts/id/" + carrotHouseholdProduct.getId())
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              String.format(
                "{\"id\": \"%s\", \"foodProductId\": %d, \"expirationDate\": \"2023-12-02\", \"amountLeft\": 1}",
                carrotHouseholdProduct.getId(),
                carrotProduct.getId()
              )
            )
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testUpdateHouseholdFoodProductUserIsPrivilegedMemberAndProductDoesNotExist() {
    try {
      when(
        isAdminOrHouseholdPrivileged.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(true);
      when(householdFoodProductService.updateFoodProduct(any(HouseholdFoodProduct.class)))
        .thenReturn(carrotHouseholdProduct);
      when(householdFoodProductService.existsById(carrotHouseholdProduct.getId()))
        .thenReturn(false);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          put(BASE_HOUSEHOLD_URL + "/foodproducts/id/" + carrotHouseholdProduct.getId())
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              String.format(
                "{\"id\": \"%s\", \"foodProductId\": %d, \"expirationDate\": \"2023-12-02\", \"amountLeft\": 1}",
                carrotHouseholdProduct.getId(),
                carrotProduct.getId()
              )
            )
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testUpdateHouseholdFoodProductUserIsMemberAndProductExists() {
    try {
      when(
        isAdminOrHouseholdPrivileged.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(false);
      when(householdFoodProductService.updateFoodProduct(any(HouseholdFoodProduct.class)))
        .thenReturn(carrotHouseholdProduct);
      when(householdFoodProductService.existsById(carrotHouseholdProduct.getId())).thenReturn(true);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          put(BASE_HOUSEHOLD_URL + "/foodproducts/id/" + carrotHouseholdProduct.getId())
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              String.format(
                "{\"id\": \"%s\", \"foodProductId\": %d, \"expirationDate\": \"2023-12-02\", \"amountLeft\": 1}",
                carrotHouseholdProduct.getId(),
                carrotProduct.getId()
              )
            )
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testUpdateHouseholdFoodProductUserIsMemberAndProductDoesNotExist() {
    try {
      when(
        isAdminOrHouseholdPrivileged.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(false);
      when(householdFoodProductService.updateFoodProduct(any(HouseholdFoodProduct.class)))
        .thenThrow(new FoodProductNotFoundException());
      when(householdFoodProductService.existsById(carrotHouseholdProduct.getId()))
        .thenReturn(false);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          put(BASE_HOUSEHOLD_URL + "/foodproducts/id/" + carrotHouseholdProduct.getId())
            .with(authentication(createAuthenticationToken(user)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              String.format(
                "{\"id\": \"%s\", \"foodProductId\": %d, \"expirationDate\": \"2023-12-02\", \"amountLeft\": 1}",
                carrotHouseholdProduct.getId(),
                carrotProduct.getId()
              )
            )
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testUpdateHouseholdFoodProductUserIsMemberButIsAdminAndProductExists() {
    try {
      when(householdFoodProductService.updateFoodProduct(any(HouseholdFoodProduct.class)))
        .thenReturn(carrotHouseholdProduct);
      when(householdFoodProductService.existsById(carrotHouseholdProduct.getId())).thenReturn(true);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          put(BASE_HOUSEHOLD_URL + "/foodproducts/id/" + carrotHouseholdProduct.getId())
            .with(authentication(createAuthenticationToken(admin)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              String.format(
                "{\"id\": \"%s\", \"foodProductId\": %d, \"expirationDate\": \"2023-12-02\", \"amountLeft\": 1}",
                carrotHouseholdProduct.getId(),
                carrotProduct.getId()
              )
            )
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testDeleteHouseholdFoodProductUserIsPrivilegedMemberAndProductExists() {
    try {
      when(
        isAdminOrHouseholdPrivileged.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(true);
      doNothing()
        .when(householdFoodProductService)
        .deleteFoodProductById(carrotHouseholdProduct.getId());
      when(householdFoodProductService.getFoodProductById(carrotHouseholdProduct.getId()))
        .thenReturn(carrotHouseholdProduct);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          delete(BASE_HOUSEHOLD_URL + "/foodproducts/id/" + carrotHouseholdProduct.getId())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNoContent());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testDeleteHouseholdFoodProductUserIsPrivilegedMemberAndProductDoesNotExist() {
    try {
      when(
        isAdminOrHouseholdPrivileged.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(true);
      doThrow(new FoodProductNotFoundException())
        .when(householdFoodProductService)
        .deleteFoodProductById(carrotHouseholdProduct.getId());
      when(householdFoodProductService.getFoodProductById(carrotHouseholdProduct.getId()))
        .thenReturn(carrotHouseholdProduct);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          delete(BASE_HOUSEHOLD_URL + "/foodproducts/id/" + carrotHouseholdProduct.getId())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testDeleteHouseholdFoodProductUserIsNotPrivilegedMemberAndProductExists() {
    try {
      when(
        isAdminOrHouseholdPrivileged.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(false);
      doNothing()
        .when(householdFoodProductService)
        .deleteFoodProductById(carrotHouseholdProduct.getId());
      when(householdFoodProductService.getFoodProductById(carrotHouseholdProduct.getId()))
        .thenReturn(carrotHouseholdProduct);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          delete(BASE_HOUSEHOLD_URL + "/foodproducts/id/" + carrotHouseholdProduct.getId())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testDeleteHouseholdFoodProductUserIsNotPrivilegedMemberAndProductDoesNotExist() {
    try {
      when(
        isAdminOrHouseholdPrivileged.invoke(
          new HouseholdFoodProductController(
            foodProductService,
            householdFoodProductService,
            householdService,
            foodProductHistoryService
          ),
          new Auth(user.getUsername(), user.getRole()),
          HOUSEHOLD_ID
        )
      )
        .thenReturn(false);
      doThrow(new FoodProductNotFoundException())
        .when(householdFoodProductService)
        .deleteFoodProductById(carrotHouseholdProduct.getId());
      when(householdFoodProductService.getFoodProductById(carrotHouseholdProduct.getId()))
        .thenReturn(carrotHouseholdProduct);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          delete(BASE_HOUSEHOLD_URL + "/foodproducts/id/" + carrotHouseholdProduct.getId())
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testDeleteHouseholdFoodProductUserIsAdminAndProductExists() {
    try {
      doNothing()
        .when(householdFoodProductService)
        .deleteFoodProductById(carrotHouseholdProduct.getId());
      when(householdFoodProductService.getFoodProductById(carrotHouseholdProduct.getId()))
        .thenReturn(carrotHouseholdProduct);
      when(foodProductService.getFoodProductById(carrotProduct.getId())).thenReturn(carrotProduct);
      when(householdService.getHouseholdById(HOUSEHOLD_ID)).thenReturn(household);
      mvc
        .perform(
          delete(BASE_HOUSEHOLD_URL + "/foodproducts/id/" + carrotHouseholdProduct.getId())
            .with(authentication(createAuthenticationToken(admin)))
        )
        .andExpect(status().isNoContent());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }
}
