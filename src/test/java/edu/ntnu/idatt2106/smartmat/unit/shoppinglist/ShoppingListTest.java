package edu.ntnu.idatt2106.smartmat.unit.shoppinglist;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit test for the shopping list model.
 */
public class ShoppingListTest {

  ShoppingList shoppingList;

  @Test
  public void testShoppingListConstructor() {
    LocalDate date = LocalDate.now();
    Household household = new Household();
    Set<ShoppingListItem> shoppingListItems = new HashSet<>();
    Set<CustomFoodItem> customFoodItems = new HashSet<>();

    shoppingList =
      new ShoppingList(
        UUID.randomUUID(),
        date,
        household,
        shoppingListItems,
        customFoodItems,
        null
      );

    assertEquals(date, shoppingList.getDateCompleted());
    assertEquals(household, shoppingList.getHousehold());
    assertEquals(shoppingListItems, shoppingList.getShoppingListItems());
    assertEquals(customFoodItems, shoppingList.getCustomFoodItems());
  }

  @Test
  public void testShoppingListSetters() {
    shoppingList = new ShoppingList();
    final UUID tmp = UUID.randomUUID();
    final HashSet<ShoppingListItem> shoppingIngredients = new HashSet<ShoppingListItem>();
    final HashSet<CustomFoodItem> customFoodItems = new HashSet<CustomFoodItem>();
    shoppingList.setId(tmp);
    shoppingList.setShoppingListItems(shoppingIngredients);
    shoppingList.setCustomFoodItems(customFoodItems);

    assertEquals(tmp, shoppingList.getId());
    assertEquals(shoppingIngredients, shoppingList.getShoppingListItems());
    assertEquals(customFoodItems, shoppingList.getCustomFoodItems());
  }

  @Test
  public void testShoppingListSetIdWithNull() {
    shoppingList = new ShoppingList();
    assertDoesNotThrow(() -> shoppingList.setId(null));
  }

  @Test
  public void testShoppingListSetShoppingListItemsWithNull() {
    shoppingList = new ShoppingList();
    assertDoesNotThrow(() -> shoppingList.setCustomFoodItems(null));
  }

  @Test
  public void testShoppingListSetCustomFoodItemsWithNull() {
    shoppingList = new ShoppingList();
    assertDoesNotThrow(() -> shoppingList.setCustomFoodItems(null));
  }
}
