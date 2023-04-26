package edu.ntnu.idatt2106.smartmat.integration.foodproduct;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.repository.foodproduct.CustomFoodItemRepository;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.CustomFoodItemService;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.CustomFoodItemServiceImpl;
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
public class CustomFoodItemServiceTest {

  @TestConfiguration
  static class UserServiceTestConfiguration {

    @Bean
    public CustomFoodItemService customFoodItemService() {
      return new CustomFoodItemServiceImpl();
    }
  }

  @Autowired
  private CustomFoodItemService customFoodItemService;

  @MockBean
  private CustomFoodItemRepository customFoodItemRepository;

  private Household household;

  private ShoppingList shoppingList;

  private CustomFoodItem customFoodItem;

  @Before
  public void setUp() {
    household = Household.builder().name("Household").id(UUID.randomUUID()).build();
    shoppingList = ShoppingList.builder().household(household).id(UUID.randomUUID()).build();
    customFoodItem =
      CustomFoodItem
        .builder()
        .shoppingList(shoppingList)
        .name("Custom food")
        .id(UUID.randomUUID())
        .build();
  }

  @Test
  public void testCustomFoodItemExistsExisting() {
    when(customFoodItemRepository.existsById(customFoodItem.getId())).thenReturn(true);
    assertTrue(customFoodItemService.existsById(customFoodItem.getId()));
  }

  @Test
  public void testCustomFoodItemNoneExisting() {
    when(customFoodItemRepository.existsById(customFoodItem.getId())).thenReturn(false);
    assertFalse(customFoodItemService.existsById(customFoodItem.getId()));
  }

  @Test
  public void testGetCustomFoodItemExisting() {
    when(customFoodItemRepository.existsById(customFoodItem.getId())).thenReturn(true);
    when(customFoodItemRepository.findById(customFoodItem.getId()))
      .thenReturn(java.util.Optional.of(customFoodItem));
    assertDoesNotThrow(() -> customFoodItemService.getItemById(customFoodItem.getId()));
  }

  @Test
  public void testGetCustomFoodItemNonExisting() {
    when(customFoodItemRepository.existsById(customFoodItem.getId())).thenReturn(false);
    assertThrows(
      ShoppingListItemNotFoundException.class,
      () -> customFoodItemService.getItemById(customFoodItem.getId())
    );
  }

  @Test
  public void testCreatingNonExistingCustomFoodItem() {
    when(customFoodItemRepository.existsById(customFoodItem.getId())).thenReturn(false);
    when(customFoodItemRepository.save(customFoodItem)).thenReturn(customFoodItem);
    assertDoesNotThrow(() -> customFoodItemService.saveCustomFoodItem(customFoodItem));
  }

  @Test
  public void testCreatingExistingCustomFoodItem() {
    when(customFoodItemRepository.existsById(customFoodItem.getId())).thenReturn(true);
    when(customFoodItemRepository.save(customFoodItem)).thenReturn(customFoodItem);
    assertThrows(
      IllegalArgumentException.class,
      () -> customFoodItemService.saveCustomFoodItem(customFoodItem)
    );
  }

  @Test
  public void testDeleteExistingCustomFoodItem() {
    when(customFoodItemRepository.existsById(customFoodItem.getId())).thenReturn(true);
    doNothing().when(customFoodItemRepository).deleteById(customFoodItem.getId());
    assertDoesNotThrow(() -> customFoodItemService.deleteCustomFoodItem(customFoodItem.getId()));
  }

  @Test
  public void testDeleteNonExistingCustomFoodItem() {
    when(customFoodItemRepository.existsById(customFoodItem.getId())).thenReturn(false);
    assertThrows(
      ShoppingListItemNotFoundException.class,
      () -> customFoodItemService.deleteCustomFoodItem(customFoodItem.getId())
    );
  }

  @Test
  public void testUpdateExistingCustomFoodItem() {
    when(customFoodItemRepository.existsById(customFoodItem.getId())).thenReturn(true);
    when(customFoodItemRepository.save(customFoodItem)).thenReturn(customFoodItem);
    assertDoesNotThrow(() -> customFoodItemService.updateCustomFoodItem(customFoodItem));
  }

  @Test
  public void testUpdateNonExistingCustomFoodItem() {
    when(customFoodItemRepository.existsById(customFoodItem.getId())).thenReturn(false);
    assertThrows(
      ShoppingListItemNotFoundException.class,
      () -> customFoodItemService.updateCustomFoodItem(customFoodItem)
    );
  }
}
