package edu.ntnu.idatt2106.smartmat.unit.mapper.shoppinglist;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ShoppingListItemDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.mapper.shoppinglist.ShoppingListItemMapper;
import edu.ntnu.idatt2106.smartmat.mapper.shoppinglist.ShoppingListItemMapperImpl;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
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
public class ShoppingListItemMapperTest {

  @TestConfiguration
  static class ShoppingListItemMapperTestContextConfiguration {

    @Bean
    public ShoppingListItemMapper shoppingListItemMapper() {
      return new ShoppingListItemMapperImpl();
    }
  }

  @MockBean
  private ShoppingListService shoppingListService;

  @Autowired
  private ShoppingListItemMapper shoppingListItemMapper;

  private Ingredient ingredient;

  private ShoppingList shoppingList;

  private ShoppingListItem shoppingListItem;

  @Before
  public void setUp() {
    ingredient = Ingredient.builder().id(1L).name("Ingredient").build();
    Household household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
    shoppingList = ShoppingList.builder().id(UUID.randomUUID()).household(household).build();
    shoppingListItem =
      ShoppingListItem
        .builder()
        .amount(100.5)
        .ingredient(ingredient)
        .shoppingList(shoppingList)
        .build();
  }

  @Test
  public void testShoppingListItemMapperToDTO() {
    ShoppingListItemDTO dto = shoppingListItemMapper.shoppingListItemsToShoppingListItemDTO(
      shoppingListItem
    );
    assertEquals(ingredient.getId(), dto.getIngredient().getId());
    assertEquals(shoppingList.getId(), dto.getShoppingList());
    assertTrue(shoppingListItem.getAmount() == dto.getAmount());
  }

  @Test
  public void testShoppingListItemMapperToEntity() {
    try {
      when(shoppingListService.getShoppingListById(shoppingList.getId())).thenReturn(shoppingList);
    } catch (NullPointerException | ShoppingListNotFoundException e) {
      fail(e.getMessage());
    }

    ShoppingListItemDTO dto = shoppingListItemMapper.shoppingListItemsToShoppingListItemDTO(
      shoppingListItem
    );
    ShoppingListItem entity = shoppingListItemMapper.shoppingListItemDTOToShoppingListItems(dto);

    assertEquals(ingredient.getId(), entity.getIngredient().getId());
    assertEquals(shoppingList.getId(), entity.getShoppingList().getId());
    assertTrue(shoppingListItem.getAmount() == entity.getAmount());
  }
}
