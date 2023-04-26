package edu.ntnu.idatt2106.smartmat.integration.shoppinglist;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListItemNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.repository.shoppinglist.ShoppingListItemRepository;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListItemService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListItemServiceImpl;
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
public class ShoppingListItemServiceTest {

  @TestConfiguration
  static class ShoppingListItemTestConfiguration {

    @Bean
    public ShoppingListItemService shoppingListItemService() {
      return new ShoppingListItemServiceImpl();
    }
  }

  @Autowired
  private ShoppingListItemService shoppingListItemService;

  @MockBean
  private ShoppingListItemRepository shoppingListItemRepository;

  private Household household;

  private ShoppingList shoppingList;

  private ShoppingListItem shoppingListItem;

  @Before
  public void setUp() {
    household = Household.builder().name("Household").id(UUID.randomUUID()).build();
    shoppingList = ShoppingList.builder().household(household).id(UUID.randomUUID()).build();
    shoppingListItem =
      ShoppingListItem
        .builder()
        .shoppingList(shoppingList)
        .ingredient(Ingredient.builder().id(1L).name("Ingredient").build())
        .id(UUID.randomUUID())
        .build();
  }

  @Test
  public void testShoppingListItemExists() {
    when(shoppingListItemRepository.existsById(shoppingListItem.getId())).thenReturn(true);
    assertTrue(shoppingListItemService.existsById(shoppingListItem.getId()));
  }

  @Test
  public void testShoppingListItemExistsNonExisting() {
    when(shoppingListItemRepository.existsById(shoppingListItem.getId())).thenReturn(false);
    assertFalse(shoppingListItemService.existsById(shoppingListItem.getId()));
  }

  @Test
  public void testGetShoppingListItemExists() {
    when(shoppingListItemRepository.existsById(shoppingListItem.getId())).thenReturn(true);
    when(shoppingListItemRepository.findById(shoppingListItem.getId()))
      .thenReturn(java.util.Optional.of(shoppingListItem));
    assertDoesNotThrow(() -> shoppingListItemService.getItemById(shoppingListItem.getId()));
  }

  @Test
  public void testGetShoppingListItemNonExisting() {
    when(shoppingListItemRepository.existsById(shoppingListItem.getId())).thenReturn(false);
    assertThrows(
      ShoppingListItemNotFoundException.class,
      () -> shoppingListItemService.getItemById(shoppingListItem.getId())
    );
  }

  @Test
  public void testCreatingNonExistingShoppingListItem() {
    when(shoppingListItemRepository.existsById(shoppingListItem.getId())).thenReturn(false);
    when(shoppingListItemRepository.save(shoppingListItem)).thenReturn(shoppingListItem);
    assertDoesNotThrow(() -> shoppingListItemService.saveShoppingListItem(shoppingListItem));
  }

  @Test
  public void testCreatingExistingShoppingListItem() {
    when(shoppingListItemRepository.existsById(shoppingListItem.getId())).thenReturn(true);
    when(shoppingListItemRepository.save(shoppingListItem)).thenReturn(shoppingListItem);
    assertThrows(
      IllegalArgumentException.class,
      () -> shoppingListItemService.saveShoppingListItem(shoppingListItem)
    );
  }

  @Test
  public void testDeleteExistingShoppingListItem() {
    when(shoppingListItemRepository.existsById(shoppingListItem.getId())).thenReturn(true);
    doNothing().when(shoppingListItemRepository).deleteById(shoppingListItem.getId());
    assertDoesNotThrow(() ->
      shoppingListItemService.deleteShoppingListItem(shoppingListItem.getId())
    );
  }

  @Test
  public void testDeleteNonExistingShoppingListItem() {
    when(shoppingListItemRepository.existsById(shoppingListItem.getId())).thenReturn(false);
    assertThrows(
      ShoppingListItemNotFoundException.class,
      () -> shoppingListItemService.deleteShoppingListItem(shoppingListItem.getId())
    );
  }

  @Test
  public void testUpdateExistingShoppingListItem() {
    when(shoppingListItemRepository.existsById(shoppingListItem.getId())).thenReturn(true);
    when(shoppingListItemRepository.save(shoppingListItem)).thenReturn(shoppingListItem);
    assertDoesNotThrow(() -> shoppingListItemService.updateShoppingListItem(shoppingListItem));
  }

  @Test
  public void testUpdateNonExistingShoppingListItem() {
    when(shoppingListItemRepository.existsById(shoppingListItem.getId())).thenReturn(false);
    assertThrows(
      ShoppingListItemNotFoundException.class,
      () -> shoppingListItemService.updateShoppingListItem(shoppingListItem)
    );
  }
}
