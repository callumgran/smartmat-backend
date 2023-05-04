package edu.ntnu.idatt2106.smartmat.integration.shoppinglist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.Basket;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.BasketItem;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;
import edu.ntnu.idatt2106.smartmat.repository.shoppinglist.ShoppingListRepository;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.BasketService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListServiceImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    household =
      Household
        .builder()
        .name("Household")
        .customFoodItems(new HashSet<>())
        .id(UUID.randomUUID())
        .build();
    shoppingList =
      ShoppingList
        .builder()
        .household(household)
        .id(UUID.randomUUID())
        .customFoodItems(new HashSet<>())
        .shoppingListItems(new HashSet<>())
        .build();
  }

  @Test
  public void testExistsByIdExistingShoppingList() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);
    assertTrue(shoppingListService.shoppingListExists(shoppingList.getId()));
  }

  @Test
  public void testExistsByIdNonExistingShoppingList() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(false);
    assertFalse(shoppingListService.shoppingListExists(shoppingList.getId()));
  }

  @Test
  public void testGetShoppingListByIdExistingShoppingList() throws Exception {
    when(shoppingListRepository.findById(shoppingList.getId()))
      .thenReturn(Optional.of(shoppingList));
    assertEquals(shoppingList, shoppingListService.getShoppingListById(shoppingList.getId()));
  }

  @Test
  public void testGetShoppingListByIdNonExistingShoppingList() {
    when(shoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.empty());
    assertThrows(
      ShoppingListNotFoundException.class,
      () -> {
        shoppingListService.getShoppingListById(shoppingList.getId());
      }
    );
  }

  @Test
  public void testUpdateShoppingListExistingShoppingList() throws Exception {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);
    when(shoppingListRepository.save(shoppingList)).thenReturn(shoppingList);
    assertEquals(
      shoppingList,
      shoppingListService.updateShoppingList(shoppingList.getId(), shoppingList)
    );
  }

  @Test
  public void testUpdateShoppingListNonExistingShoppingList() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(false);
    assertThrows(
      ShoppingListNotFoundException.class,
      () -> {
        shoppingListService.updateShoppingList(shoppingList.getId(), shoppingList);
      }
    );
  }

  @Test
  public void testSaveShoppingListNonExistingShoppingList() throws Exception {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(false);
    when(shoppingListRepository.save(shoppingList)).thenReturn(shoppingList);
    assertEquals(shoppingList, shoppingListService.saveShoppingList(shoppingList));
  }

  @Test
  public void testSaveShoppingListExistingShoppingList() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);
    assertThrows(
      ShoppingListAlreadyExistsException.class,
      () -> {
        shoppingListService.saveShoppingList(shoppingList);
      }
    );
  }

  @Test
  public void testSaveShoppingListNullShoppingList() {
    assertThrows(
      NullPointerException.class,
      () -> {
        shoppingListService.saveShoppingList(null);
      }
    );
  }

  @Test
  public void testDeleteShoppingListExistingShoppingList() throws Exception {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);
    doNothing().when(shoppingListRepository).delete(shoppingList);
    assertDoesNotThrow(() -> {
      shoppingListService.deleteShoppingList(shoppingList);
    });
  }

  @Test
  public void testDeleteShoppingListNonExistingShoppingList() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(false);
    assertThrows(
      ShoppingListNotFoundException.class,
      () -> {
        shoppingListService.deleteShoppingList(shoppingList);
      }
    );
  }

  @Test
  public void testDeleteShoppingListNullShoppingList() {
    assertThrows(
      NullPointerException.class,
      () -> {
        shoppingListService.deleteShoppingList(null);
      }
    );
  }

  @Test
  public void testDeleteShoppingListByIdExistingShoppingList() throws Exception {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);
    doNothing().when(shoppingListRepository).deleteById(shoppingList.getId());
    assertDoesNotThrow(() -> {
      shoppingListService.deleteShoppingListById(shoppingList.getId());
    });
  }

  @Test
  public void testDeleteShoppingListByIdNonExistingShoppingList() {
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(false);
    assertThrows(
      ShoppingListNotFoundException.class,
      () -> {
        shoppingListService.deleteShoppingListById(shoppingList.getId());
      }
    );
  }

  @Test
  public void testDeleteShoppingListByIdNullShoppingList() {
    assertThrows(
      NullPointerException.class,
      () -> {
        shoppingListService.deleteShoppingListById(null);
      }
    );
  }

  @Test
  public void testGetCurrentShoppingListByHouseholdExistingShoppingList() throws Exception {
    when(shoppingListRepository.getCurrentShoppingListByHousehold(household.getId()))
      .thenReturn(Optional.of(List.of(shoppingList)));
    assertEquals(shoppingList, shoppingListService.getCurrentShoppingList(household.getId()));
  }

  @Test
  public void testGetCurrentShoppingListByHouseholdNonExistingShoppingList() {
    when(shoppingListRepository.getCurrentShoppingListByHousehold(household.getId()))
      .thenReturn(Optional.empty());
    assertThrows(
      ShoppingListNotFoundException.class,
      () -> {
        shoppingListService.getCurrentShoppingList(household.getId());
      }
    );
  }

  @Test
  public void testGetCurrentShoppingListWithDiff() throws Exception {
    Unit unit = new Unit("kilogram", "kg", new HashSet<>(), 1, UnitTypeEnum.SOLID);
    Unit gram = new Unit("gram", "g", new HashSet<>(), 0.001, UnitTypeEnum.SOLID);
    Ingredient carrot = new Ingredient(1L, "Carrot", null, null, unit);
    Ingredient potato = new Ingredient(2L, "Potato", null, null, unit);
    Ingredient onion = new Ingredient(3L, "Onion", null, null, unit);

    FoodProduct carrotFoodProduct = new FoodProduct(
      1L,
      "Bama Carrot",
      "123456789123",
      1000.0D,
      false,
      null,
      null,
      carrot,
      null,
      false,
      gram,
      null
    );
    FoodProduct potatoFoodProduct = new FoodProduct(
      2L,
      "Bama Potato",
      "123456789123",
      1000.0D,
      false,
      null,
      null,
      potato,
      null,
      false,
      gram,
      null
    );
    FoodProduct onionFoodProduct = new FoodProduct(
      3L,
      "Bama Onion",
      "123456789123",
      1000.0D,
      false,
      null,
      null,
      onion,
      null,
      false,
      gram,
      null
    );

    ShoppingListItem carrotShoppingListItem = new ShoppingListItem(
      UUID.randomUUID(),
      3.0D,
      false,
      shoppingList,
      carrot
    );

    ShoppingListItem potatoShoppingListItem = new ShoppingListItem(
      UUID.randomUUID(),
      3.0D,
      false,
      shoppingList,
      potato
    );

    ShoppingListItem onionShoppingListItem = new ShoppingListItem(
      UUID.randomUUID(),
      5.0D,
      false,
      shoppingList,
      onion
    );

    shoppingList
      .getShoppingListItems()
      .addAll(Set.of(carrotShoppingListItem, potatoShoppingListItem, onionShoppingListItem));

    Basket basket = new Basket(UUID.randomUUID(), shoppingList, new ArrayList<>(), new HashSet<>());

    BasketItem carrotBasketItem = new BasketItem(
      UUID.randomUUID(),
      carrotFoodProduct,
      1.0D,
      basket
    );

    BasketItem potatoBasketItem = new BasketItem(
      UUID.randomUUID(),
      potatoFoodProduct,
      1.0D,
      basket
    );

    BasketItem onionBasketItem = new BasketItem(UUID.randomUUID(), onionFoodProduct, 1.0D, basket);

    basket.getBasketItems().addAll(List.of(carrotBasketItem, potatoBasketItem, onionBasketItem));

    CustomFoodItem hotChocolateCustomFoodItem = new CustomFoodItem(
      UUID.randomUUID(),
      "Hot Chocoloate",
      1,
      false,
      shoppingList,
      null,
      null
    );

    CustomFoodItem chocolateCustomFoodItem = new CustomFoodItem(
      UUID.randomUUID(),
      "Chocolate",
      1,
      false,
      shoppingList,
      null,
      basket
    );

    shoppingList
      .getCustomFoodItems()
      .addAll(Set.of(hotChocolateCustomFoodItem, chocolateCustomFoodItem));

    basket.getCustomFoodItems().addAll(Set.of(chocolateCustomFoodItem));

    when(shoppingListRepository.getCurrentShoppingListByHousehold(household.getId()))
      .thenReturn(Optional.of(List.of(shoppingList)));
    when(basketService.getBasketByShoppingListId(shoppingList.getId())).thenReturn(basket);
    when(shoppingListRepository.findById(shoppingList.getId()))
      .thenReturn(Optional.of(shoppingList));
    when(shoppingListRepository.existsById(shoppingList.getId())).thenReturn(true);

    ShoppingList diff = shoppingListService.getShoppingListWithDiff(shoppingList.getId());

    List<CustomFoodItem> customFoodItems = diff
      .getCustomFoodItems()
      .stream()
      .sorted((a, b) -> a.getAmount() > b.getAmount() ? 1 : -1)
      .toList();
    List<ShoppingListItem> shoppingListItems = diff
      .getShoppingListItems()
      .stream()
      .sorted((a, b) -> a.getAmount() > b.getAmount() ? 1 : -1)
      .toList();

    assertEquals(2, customFoodItems.size());
    assertEquals(0, customFoodItems.get(0).getAmount());
    assertEquals(1, customFoodItems.get(1).getAmount());

    assertEquals(3, shoppingListItems.size());
    assertTrue(2 == shoppingListItems.get(0).getAmount());
    assertTrue(2 == shoppingListItems.get(1).getAmount());
    assertTrue(4 == shoppingListItems.get(2).getAmount());
  }
}
