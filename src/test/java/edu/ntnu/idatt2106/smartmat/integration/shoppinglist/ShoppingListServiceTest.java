package edu.ntnu.idatt2106.smartmat.integration.shoppinglist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.repository.shoppinglist.ShoppingListRepository;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.BasketService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListServiceImpl;
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
public class ShoppingListServiceTest {

  @TestConfiguration
  static class ShoppingListServiceTestConfiguration {

    @Bean
    public ShoppingListService shoppingListService() {
      return new ShoppingListServiceImpl();
    }
  }

  @Autowired
  private ShoppingListService shoppingListService;

  @MockBean
  private ShoppingListRepository shoppingListRepository;

  @MockBean
  private BasketService basketService;

  private Household household;

  private ShoppingList shoppingList;

  @Before
  public void setUp() {
    household = Household.builder().name("Household").id(UUID.randomUUID()).build();
    shoppingList = ShoppingList.builder().household(household).id(UUID.randomUUID()).build();
  }

  @Test
  public void getShoppingListByIdShouldReturnCorrectShoppingList() {
    when(shoppingListRepository.findById(shoppingList.getId()))
      .thenReturn(Optional.of(shoppingList));

    ShoppingList actualShoppingList = null;
    try {
      actualShoppingList = shoppingListService.getShoppingListById(shoppingList.getId());
    } catch (NullPointerException | ShoppingListNotFoundException e) {
      fail(e.getMessage());
    }
    assertEquals(shoppingList, actualShoppingList);
  }

  @Test
  public void getShoppingListByIdShouldThrowShoppingListNotFoundException() {
    when(shoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.empty());

    assertThrows(
      ShoppingListNotFoundException.class,
      () -> {
        shoppingListService.getShoppingListById(shoppingList.getId());
      }
    );
  }

  @Test
  public void testShoppingListExists() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);
    try {
      assertTrue(shoppingListService.shoppingListExists(shoppingList.getId()));
    } catch (NullPointerException | ShoppingListNotFoundException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetShoppingListById() {
    when(shoppingListRepository.findById(shoppingList.getId()))
      .thenReturn(Optional.of(shoppingList));
    try {
      assertEquals(shoppingList, shoppingListService.getShoppingListById(shoppingList.getId()));
    } catch (NullPointerException | ShoppingListNotFoundException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testUpdateNonExistingShoppingList() {
    when(shoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.empty());
    assertThrows(
      ShoppingListNotFoundException.class,
      () -> {
        shoppingListService.updateShoppingList(shoppingList.getId(), shoppingList);
      }
    );
  }

  @Test
  public void testUpdateExistingShoppingList() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);
    when(shoppingListRepository.save(shoppingList)).thenReturn(shoppingList);
    try {
      assertEquals(
        shoppingList,
        shoppingListService.updateShoppingList(shoppingList.getId(), shoppingList)
      );
    } catch (NullPointerException | ShoppingListNotFoundException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testCreateNonExistingShoppingList() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(false);
    when(shoppingListRepository.save(shoppingList)).thenReturn(shoppingList);
    try {
      assertEquals(shoppingList, shoppingListService.saveShoppingList(shoppingList));
    } catch (NullPointerException | ShoppingListAlreadyExistsException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testCreateExistingShoppingList() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);
    assertThrows(
      ShoppingListAlreadyExistsException.class,
      () -> {
        shoppingListService.saveShoppingList(shoppingList);
      }
    );
  }

  @Test
  public void testDeleteExistingShoppingListByShoppingList() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);
    doNothing().when(shoppingListRepository).deleteById(shoppingList.getId());

    assertDoesNotThrow(() -> shoppingListService.deleteShoppingList(shoppingList));
  }

  @Test
  public void testDeleteNonExistingShoppingListByShoppingList() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(false);
    assertThrows(
      ShoppingListNotFoundException.class,
      () -> {
        shoppingListService.deleteShoppingList(shoppingList);
      }
    );
  }

  @Test
  public void testDeleteExistingShoppingListById() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);
    doNothing().when(shoppingListRepository).deleteById(shoppingList.getId());

    assertDoesNotThrow(() -> shoppingListService.deleteShoppingList(shoppingList));
  }

  @Test
  public void testDeleteNonExistingShoppingListById() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(false);
    assertThrows(
      ShoppingListNotFoundException.class,
      () -> {
        shoppingListService.deleteShoppingListById(shoppingList.getId());
      }
    );
  }

  @Test
  public void testGetCurrentShoppingListReturnsShoppingListWithoutDateCompleted() {
    shoppingList.setDateCompleted(null);
    when(shoppingListRepository.getCurrentShoppingListByHousehold(household.getId()))
      .thenReturn(Optional.of(Set.of(shoppingList)));
    try {
      assertEquals(shoppingList, shoppingListService.getCurrentShoppingList(household.getId()));
    } catch (NullPointerException | ShoppingListNotFoundException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetCurrentShoppingListWithoutCurrentShoppingListThrows() {
    when(shoppingListRepository.getCurrentShoppingListByHousehold(household.getId()))
      .thenReturn(Optional.empty());
    assertThrows(
      ShoppingListNotFoundException.class,
      () -> {
        shoppingListService.getCurrentShoppingList(household.getId());
      }
    );
  }
}
