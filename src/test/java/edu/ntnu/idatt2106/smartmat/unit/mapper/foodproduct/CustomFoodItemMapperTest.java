package edu.ntnu.idatt2106.smartmat.unit.mapper.foodproduct;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CustomFoodItemDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.CustomFoodItemMapper;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.CustomFoodItemMapperImpl;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CustomFoodItemMapperTest {

  @TestConfiguration
  static class CustomFoodItemMapperTestContextConfiguration {

    @Bean
    public CustomFoodItemMapper customFoodItemMapper() {
      return new CustomFoodItemMapperImpl();
    }
  }

  @MockBean
  private ShoppingListService shoppingListService;

  @MockBean
  private HouseholdService householdService;

  @Autowired
  private CustomFoodItemMapper customFoodItemMapper;

  private ShoppingList shoppingList;

  private CustomFoodItem customFoodItemShoppingList;
  private CustomFoodItem customFoodItemHousehold;

  @Before
  public void setUp() {
    Household household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
    shoppingList = ShoppingList.builder().id(UUID.randomUUID()).household(household).build();
    customFoodItemShoppingList =
      CustomFoodItem
        .builder()
        .name("In shopping list")
        .amount(100)
        .shoppingList(shoppingList)
        .build();
    customFoodItemHousehold =
      CustomFoodItem.builder().name("In household").amount(100).household(household).build();
  }

  @Test
  public void testFromShoppingListCustomFoodItemDTOToEntity() {
    CustomFoodItemDTO customFoodItemDTO = CustomFoodItemDTO
      .builder()
      .name("In shopping list")
      .amount(100)
      .shoppingList(shoppingList.getId())
      .build();
    try {
      when(shoppingListService.getShoppingListById(shoppingList.getId())).thenReturn(shoppingList);
    } catch (NullPointerException | ShoppingListNotFoundException e) {
      fail(e.getMessage());
    }
    CustomFoodItem customFoodItem = customFoodItemMapper.customFoodItemDTOToCustomFoodItems(
      customFoodItemDTO
    );
    assertEquals(customFoodItemDTO.getName(), customFoodItem.getName());
    assertTrue(customFoodItemDTO.getAmount() == customFoodItem.getAmount());
    assertEquals(customFoodItemDTO.getShoppingList(), customFoodItem.getShoppingList().getId());
    assertNull(customFoodItem.getHousehold());
  }

  @Test
  public void testFromHouseholdCustomFoodItemDTOToEntity() {
    CustomFoodItemDTO customFoodItemDTO = CustomFoodItemDTO
      .builder()
      .name("In household")
      .amount(100)
      .household(customFoodItemHousehold.getHousehold().getId())
      .build();
    try {
      when(householdService.getHouseholdById(customFoodItemHousehold.getHousehold().getId()))
        .thenReturn(customFoodItemHousehold.getHousehold());
    } catch (Exception e) {
      fail(e.getMessage());
    }
    CustomFoodItem customFoodItem = customFoodItemMapper.customFoodItemDTOToCustomFoodItems(
      customFoodItemDTO
    );
    assertEquals(customFoodItemDTO.getName(), customFoodItem.getName());
    assertTrue(customFoodItemDTO.getAmount() == customFoodItem.getAmount());
    assertEquals(customFoodItemDTO.getHousehold(), customFoodItem.getHousehold().getId());
    assertNull(customFoodItem.getShoppingList());
  }

  @Test
  public void testFromShoppingListCustomFoodItemToDTO() {
    CustomFoodItemDTO customFoodItemDTO = customFoodItemMapper.customFoodItemsToCustomFoodItemDTO(
      customFoodItemShoppingList
    );
    assertEquals(customFoodItemDTO.getName(), customFoodItemShoppingList.getName());
    assertTrue(customFoodItemDTO.getAmount() == customFoodItemShoppingList.getAmount());
    assertEquals(
      customFoodItemDTO.getShoppingList(),
      customFoodItemShoppingList.getShoppingList().getId()
    );
    assertNull(customFoodItemDTO.getHousehold());
  }

  @Test
  public void testFromHouseholdCustomFoodItemToDTO() {
    CustomFoodItemDTO customFoodItemDTO = customFoodItemMapper.customFoodItemsToCustomFoodItemDTO(
      customFoodItemHousehold
    );
    assertEquals(customFoodItemDTO.getName(), customFoodItemHousehold.getName());
    assertTrue(customFoodItemDTO.getAmount() == customFoodItemHousehold.getAmount());
    assertEquals(customFoodItemDTO.getHousehold(), customFoodItemHousehold.getHousehold().getId());
    assertNull(customFoodItemDTO.getShoppingList());
  }

  @Test
  public void testHouseholdAndShoppingListNullToDTO() {
    CustomFoodItemDTO customFoodItemDTO = customFoodItemMapper.customFoodItemsToCustomFoodItemDTO(
      CustomFoodItem.builder().name("Test").amount(100).build()
    );
    assertEquals(customFoodItemDTO.getName(), "Test");
    assertTrue(customFoodItemDTO.getAmount() == 100);
    assertNull(customFoodItemDTO.getHousehold());
    assertNull(customFoodItemDTO.getShoppingList());
  }
}
