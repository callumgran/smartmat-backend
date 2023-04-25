package edu.ntnu.idatt2106.smartmat.integration.foodproduct;

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
import java.util.Optional;
import java.util.Set;
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
  public void testCustomFoodItemExists() {
    when(
      customFoodItemRepository.findByIdInShoppingList(customFoodItem.getId(), shoppingList.getId())
    )
      .thenReturn(Optional.of(java.util.Set.of(customFoodItem)));
    assertTrue(
      customFoodItemService.existsByIdInShoppingList(shoppingList.getId(), customFoodItem.getId())
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
    assertDoesNotThrow(() -> customFoodItemService.saveCustomFoodItem(customFoodItem));
  }

  @Test
  public void testDeleteExistingCustomFoodItem() {
    when(
      customFoodItemRepository.findByIdInShoppingList(customFoodItem.getId(), shoppingList.getId())
    )
      .thenReturn(Optional.of(Set.of(customFoodItem)));
    doNothing().when(customFoodItemRepository).deleteById(customFoodItem.getId());
    assertDoesNotThrow(() ->
      customFoodItemService.deleteCustomFoodItemInShoppingList(
        shoppingList.getId(),
        customFoodItem.getId()
      )
    );
  }

  @Test
  public void testDeleteNonExistingCustomFoodItem() {
    when(
      customFoodItemRepository.findByIdInShoppingList(customFoodItem.getId(), shoppingList.getId())
    )
      .thenReturn(Optional.empty());
    assertThrows(
      ShoppingListItemNotFoundException.class,
      () ->
        customFoodItemService.deleteCustomFoodItemInShoppingList(
          shoppingList.getId(),
          customFoodItem.getId()
        )
    );
  }
}
