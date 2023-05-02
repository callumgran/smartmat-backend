package edu.ntnu.idatt2106.smartmat.endpoint.foodproduct;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.createAuthenticationToken;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.foodproduct.FoodProductController;
import edu.ntnu.idatt2106.smartmat.dto.kassalapp.KassalApiDataDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.KassalappAPITokenSingleton;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.FoodProductService;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@WebMvcTest({ FoodProductController.class, SecurityConfig.class })
public class FoodProductControllerTest {

  @MockBean
  private RestTemplate restTemplate;

  @Autowired
  private MockMvc mvc;

  @MockBean
  private FoodProductService foodProductService;

  @MockBean
  private IngredientService ingredientService;

  private static final String BASE_URL = "/api/v1/private/foodproducts";

  private User user;

  private Ingredient carrot;

  private FoodProduct carrotProduct;

  @Before
  public void setUp() {
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
        null
      );

    user = testUserFactory(TestUserEnum.GOOD);
  }

  @Test
  public void testGetFoodProductThatExists() {
    try {
      when(foodProductService.getFoodProductById(1L)).thenReturn(carrotProduct);
      mvc
        .perform(
          get(BASE_URL + "/id/1")
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetFoodProductThatDoesNotExist() {
    try {
      when(foodProductService.getFoodProductById(1L)).thenThrow(new FoodProductNotFoundException());
      mvc
        .perform(
          get(BASE_URL + "/id/1")
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetFoodProductByEanThatExists() {
    try {
      when(foodProductService.getFoodProductByEan("123456789")).thenReturn(carrotProduct);
      mvc
        .perform(
          get(BASE_URL + "/ean/123456789")
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetFoodProductByEanThatDoesNotExist() {
    try {
      when(foodProductService.getFoodProductByEan("123456789"))
        .thenThrow(new FoodProductNotFoundException());
      when(
        restTemplate.exchange(
          any(String.class),
          eq(HttpMethod.GET),
          any(HttpEntity.class),
          eq(KassalApiDataDTO.class)
        )
      )
        .thenThrow(new RestClientException("Not found"));
      KassalappAPITokenSingleton kassalappAPITokenSingleton = mock(
        KassalappAPITokenSingleton.class
      );
      when(kassalappAPITokenSingleton.getKassalappAPIToken()).thenReturn("token");
      mvc
        .perform(
          get(BASE_URL + "/ean/123456789")
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(user)))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testUpdateFoodProductThatExists() {
    try {
      when(foodProductService.updateFoodProduct(any(FoodProduct.class))).thenReturn(carrotProduct);
      when(ingredientService.getIngredientById(1L)).thenReturn(carrot);
      mvc
        .perform(
          put(BASE_URL + "/id/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\"id\": 1,\"name\":\"Carrotproduct\",\"ean\":\"1234567890123\",\"amount\": 1.0,\"looseWeight\":true, \"ingredientId\": 1}"
            )
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
        )
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testUpdateFoodProductThatDoesNotExist() {
    try {
      when(foodProductService.updateFoodProduct(any(FoodProduct.class)))
        .thenThrow(new FoodProductNotFoundException());
      when(ingredientService.getIngredientById(1L)).thenReturn(carrot);
      mvc
        .perform(
          put(BASE_URL + "/id/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\"id\": 1,\"name\":\"Carrot\",\"ean\":\"123456789012\",\"amount\": 1.0,\"looseWeight\": true, \"ingredientId\": 1}"
            )
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testCreateFoodProductThatDoesExist() {
    try {
      when(foodProductService.saveFoodProduct(any(FoodProduct.class))).thenReturn(carrotProduct);
      when(ingredientService.getIngredientById(1L)).thenReturn(carrot);
      mvc
        .perform(
          post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\"name\": \"CarrotProduct\",\"ean\": \"123456789\",\"amount\": 1.0,\"looseWeight\": true, \"ingredientId\": 1}"
            )
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
        )
        .andExpect(status().isCreated());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testCreateFoodProductWithoutAdmin() {
    try {
      when(foodProductService.saveFoodProduct(any(FoodProduct.class))).thenReturn(carrotProduct);
      when(ingredientService.getIngredientById(1L)).thenReturn(carrot);
      mvc
        .perform(
          post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
              "{\"name\":\"CarrotProduct\",\"ean\":\"123456789\",\"amount\":1.0,\"looseWeight\":true, \"ingredientId\": 1}"
            )
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.GOOD))))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDeleteFoodProductThatExists() {
    try {
      doNothing().when(foodProductService).deleteFoodProductById(1L);
      mvc
        .perform(
          delete(BASE_URL + "/id/1")
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
        )
        .andExpect(status().isNoContent());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDeleteFoodProductThatDoesNotExist() {
    try {
      doThrow(new FoodProductNotFoundException())
        .when(foodProductService)
        .deleteFoodProductById(1L);
      mvc
        .perform(
          delete(BASE_URL + "/id/1")
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
        )
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDeleteFoodProductWithoutAdmin() {
    try {
      doNothing().when(foodProductService).deleteFoodProductById(1L);
      mvc
        .perform(
          delete(BASE_URL + "/id/1")
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.GOOD))))
        )
        .andExpect(status().isForbidden());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
