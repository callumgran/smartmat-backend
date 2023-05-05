package edu.ntnu.idatt2106.smartmat.endpoint.statistics;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.createAuthenticationToken;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.statistic.FoodProductHistoryController;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.statistic.FoodProductHistoryService;
import edu.ntnu.idatt2106.smartmat.utils.PrivilegeUtil;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest({ FoodProductHistoryController.class, SecurityConfig.class })
public class FoodProductHistoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private static final String BASE_URL = "/api/v1/private/stats";

  @MockBean
  private FoodProductHistoryService foodProductHistoryService;

  @MockBean
  private HouseholdService householdService;

  private final User user = testUserFactory(TestUserEnum.GOOD);

  private final Unit gram = new Unit(
    "gram",
    "g",
    new HashSet<>(),
    0.001,
    UnitTypeEnum.SOLID,
    new HashSet<>()
  );

  private final Ingredient carrot = new Ingredient(1L, "Carrot", null, null, gram);

  private final FoodProduct foodProduct = FoodProduct
    .builder()
    .id(1L)
    .EAN("1234567890123")
    .image("image")
    .looseWeight(false)
    .ingredient(carrot)
    .amount(2.0)
    .unit(gram)
    .name("Test")
    .build();

  private final FoodProductHistory foodProductHistory = FoodProductHistory
    .builder()
    .id(UUID.randomUUID())
    .household(testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD))
    .amount(1)
    .thrownAmount(0)
    .foodProduct(foodProduct)
    .date(LocalDate.now())
    .build();

  @Test
  public void testGetFoodProductHistoryById() throws Exception {
    when(
      PrivilegeUtil.isAdminOrHouseholdMember(
        new Auth(user.getUsername(), user.getRole()),
        foodProductHistory.getHousehold().getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(foodProductHistoryService.getFoodProductHistoryById(any())).thenReturn(foodProductHistory);
    mockMvc
      .perform(
        get(BASE_URL + "/" + foodProductHistory.getId())
          .with(authentication(createAuthenticationToken(user)))
      )
      .andExpect(status().isOk());
  }

  @Test
  public void testUpdateFoodProductHistory() throws Exception {
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        foodProductHistory.getHousehold().getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(foodProductHistoryService.updateFoodProductHistory(any(), any()))
      .thenReturn(foodProductHistory);
    when(foodProductHistoryService.getFoodProductHistoryById(any())).thenReturn(foodProductHistory);
    mockMvc
      .perform(
        put(BASE_URL + "/" + foodProductHistory.getId())
          .with(authentication(createAuthenticationToken(user)))
          .contentType("application/json")
          .content(
            "{\n" +
            "  \"foodProductId\": 1,\n" +
            "  \"householdId\": \"" +
            foodProductHistory.getHousehold().getId() +
            "\",\n" +
            "  \"thrownAmount\": 0,\n" +
            "  \"date\": \"2021-04-20\"\n" +
            "}"
          )
      )
      .andExpect(status().isOk());
  }

  @Test
  public void testDeleteFoodProductHistory() throws Exception {
    when(
      PrivilegeUtil.isAdminOrHouseholdPrivileged(
        new Auth(user.getUsername(), user.getRole()),
        foodProductHistory.getHousehold().getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(foodProductHistoryService.getFoodProductHistoryById(any())).thenReturn(foodProductHistory);
    doNothing().when(foodProductHistoryService).deleteFoodProductHistory(any());
    mockMvc
      .perform(
        delete(BASE_URL + "/" + foodProductHistory.getId())
          .with(authentication(createAuthenticationToken(user)))
      )
      .andExpect(status().isNoContent());
  }

  @Test
  public void testGetFoodProducHistoryForProduct() throws Exception {
    when(foodProductHistoryService.getAllFoodProductHistoryByFoodProductId(any()))
      .thenReturn(List.of(foodProductHistory));
    mockMvc
      .perform(
        get(BASE_URL + "/foodproduct/" + foodProductHistory.getFoodProduct().getId())
          .with(authentication(createAuthenticationToken(testUserFactory(TestUserEnum.ADMIN))))
      )
      .andExpect(status().isOk());
  }

  @Test
  public void testGetFoodProductHistoryForProductAndHousehold() throws Exception {
    when(
      PrivilegeUtil.isAdminOrHouseholdMember(
        new Auth(user.getUsername(), user.getRole()),
        foodProductHistory.getHousehold().getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(
      foodProductHistoryService.getAllFoodProductHistoryByHouseholdIdAndFoodProductId(any(), any())
    )
      .thenReturn(List.of(foodProductHistory));
    mockMvc
      .perform(
        get(
          BASE_URL +
          "/foodproduct/" +
          foodProductHistory.getFoodProduct().getId() +
          "/household/" +
          foodProductHistory.getHousehold().getId()
        )
          .with(authentication(createAuthenticationToken(user)))
      )
      .andExpect(status().isOk());
  }

  @Test
  public void testGetFoodProductHistoryForHousehold() throws Exception {
    when(
      PrivilegeUtil.isAdminOrHouseholdMember(
        new Auth(user.getUsername(), user.getRole()),
        foodProductHistory.getHousehold().getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(foodProductHistoryService.getAllFoodProductHistoryByHouseholdId(any()))
      .thenReturn(List.of(foodProductHistory));
    mockMvc
      .perform(
        get(BASE_URL + "/household/" + foodProductHistory.getHousehold().getId())
          .with(authentication(createAuthenticationToken(user)))
      )
      .andExpect(status().isOk());
  }

  @Test
  public void testGetTotalWasteForHousehold() throws Exception {
    when(
      PrivilegeUtil.isAdminOrHouseholdMember(
        new Auth(user.getUsername(), user.getRole()),
        foodProductHistory.getHousehold().getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(foodProductHistoryService.getTotalWaste(any())).thenReturn(1.0);
    mockMvc
      .perform(
        get(BASE_URL + "/household/" + foodProductHistory.getHousehold().getId() + "/total")
          .with(authentication(createAuthenticationToken(user)))
      )
      .andExpect(status().isOk());
  }

  @Test
  public void testGetTotalWasteForHouseholdAndYear() throws Exception {
    when(
      PrivilegeUtil.isAdminOrHouseholdMember(
        new Auth(user.getUsername(), user.getRole()),
        foodProductHistory.getHousehold().getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(foodProductHistoryService.getWasteByInPeriod(any(), any(), any())).thenReturn(1.0);
    mockMvc
      .perform(
        get(BASE_URL + "/household/" + foodProductHistory.getHousehold().getId() + "/total/2021")
          .with(authentication(createAuthenticationToken(user)))
      )
      .andExpect(status().isOk());
  }

  @Test
  public void testGetTotalWasteForHouseholdAndYearAndMonth() throws Exception {
    when(
      PrivilegeUtil.isAdminOrHouseholdMember(
        new Auth(user.getUsername(), user.getRole()),
        foodProductHistory.getHousehold().getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(foodProductHistoryService.getWasteByInPeriod(any(), any(), any())).thenReturn(1.0);
    mockMvc
      .perform(
        get(BASE_URL + "/household/" + foodProductHistory.getHousehold().getId() + "/total/2021/4")
          .with(authentication(createAuthenticationToken(user)))
      )
      .andExpect(status().isOk());
  }

  @Test
  public void testGetWasteByMonthInPeriod() throws Exception {
    when(
      PrivilegeUtil.isAdminOrHouseholdMember(
        new Auth(user.getUsername(), user.getRole()),
        foodProductHistory.getHousehold().getId(),
        householdService
      )
    )
      .thenReturn(true);
    when(foodProductHistoryService.getWasteByInPeriod(any(), any(), any())).thenReturn(1.0);
    mockMvc
      .perform(
        get(
          BASE_URL +
          "/household/" +
          foodProductHistory.getHousehold().getId() +
          "/by-month/2021-04-01/2021-05-01"
        )
          .with(authentication(createAuthenticationToken(user)))
      )
      .andExpect(status().isOk());
  }
}
