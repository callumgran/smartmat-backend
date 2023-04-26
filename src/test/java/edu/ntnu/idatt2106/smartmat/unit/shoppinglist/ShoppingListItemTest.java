package edu.ntnu.idatt2106.smartmat.unit.shoppinglist;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import java.util.UUID;
import org.junit.Test;

/**
 * Unit Test for ShoppingListItem
 */
public class ShoppingListItemTest {

  ShoppingListItem shoppingListItem;

  private final UUID id = UUID.randomUUID();

  @Test
  public void testShoppingListItemConstructor() {
    shoppingListItem = new ShoppingListItem(id, 1.0, false, new ShoppingList(), new Ingredient());
    assertEquals(id, shoppingListItem.getId());
    assertEquals(1.0, shoppingListItem.getAmount(), 0.0);
    assertEquals(false, shoppingListItem.isChecked());
  }

  @Test
  public void testShoppingListItemConstructorDoesNotThrowExceptionIdIsNull() {
    assertDoesNotThrow(() ->
      new ShoppingListItem(null, 1.0, false, new ShoppingList(), new Ingredient())
    );
  }

  @Test
  public void testShoppingListItemSetters() {
    shoppingListItem = new ShoppingListItem();
    shoppingListItem.setId(id);
    shoppingListItem.setAmount(1.0);
    shoppingListItem.setChecked(false);
    assertEquals(id, shoppingListItem.getId());
    assertEquals(1.0, shoppingListItem.getAmount(), 0.0);
    assertEquals(false, shoppingListItem.isChecked());
  }

  @Test
  public void testShoppingListItemSettersDoesNotThrowExceptionIdIsNull() {
    shoppingListItem = new ShoppingListItem();
    assertDoesNotThrow(() -> shoppingListItem.setId(null));
  }

  @Test
  public void testShoppingListItemSettersDoesNotThrowExceptionAmount() {
    shoppingListItem = new ShoppingListItem();
    assertDoesNotThrow(() -> shoppingListItem.setAmount(0.0));
  }

  @Test
  public void testShoppingListItemSettersDoesNotThrowExceptionShoppingListdIsNull() {
    shoppingListItem = new ShoppingListItem();
    assertDoesNotThrow(() -> shoppingListItem.setShoppingList(null));
  }

  @Test
  public void testShoppingListItemSettersDoesNotThrowExceptionIngredientIsNull() {
    shoppingListItem = new ShoppingListItem();
    assertDoesNotThrow(() -> shoppingListItem.setIngredient(null));
  }
}
