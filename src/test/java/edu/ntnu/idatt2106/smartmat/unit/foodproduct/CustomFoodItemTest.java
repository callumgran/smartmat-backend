package edu.ntnu.idatt2106.smartmat.unit.foodproduct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import java.util.UUID;
import org.junit.Test;

public class CustomFoodItemTest {

  CustomFoodItem customFoodItem;

  @Test
  public void testCustomFoodItemConstructor() {
    final String customFoodItemName = "customFoodItemName";
    final UUID customFoodItemUUID = UUID.randomUUID();
    final int customFoodItemAmount = 10;
    final boolean customFoodItemIsCheck = false;

    customFoodItem =
      new CustomFoodItem(
        customFoodItemUUID,
        customFoodItemName,
        customFoodItemAmount,
        customFoodItemIsCheck,
        null,
        null,
        null
      );

    assertEquals(customFoodItemName, customFoodItem.getName());
    assertEquals(customFoodItemUUID, customFoodItem.getId());
    assertEquals(customFoodItemAmount, customFoodItem.getAmount());
    assertEquals(customFoodItemIsCheck, customFoodItem.isChecked());
  }

  @Test
  public void testCustomFoodItemConstructorNullName() {
    final UUID customFoodItemUUID = UUID.randomUUID();
    final int customFoodItemAmount = 10;
    final boolean customFoodItemIsCheck = false;

    assertThrows(
      NullPointerException.class,
      () ->
        new CustomFoodItem(
          customFoodItemUUID,
          null,
          customFoodItemAmount,
          customFoodItemIsCheck,
          null,
          null,
          null
        )
    );
  }

  @Test
  public void testCustomFoodItemSetters() {
    customFoodItem = new CustomFoodItem();
    final String customFoodItemName = "customFoodItemName";
    final UUID customFoodItemUUID = UUID.randomUUID();
    final int customFoodItemAmount = 10;
    final boolean customFoodItemIsCheck = false;

    customFoodItem.setName(customFoodItemName);
    customFoodItem.setId(customFoodItemUUID);
    customFoodItem.setAmount(customFoodItemAmount);
    customFoodItem.setChecked(customFoodItemIsCheck);

    assertEquals(customFoodItemName, customFoodItem.getName());
    assertEquals(customFoodItemUUID, customFoodItem.getId());
    assertEquals(customFoodItemAmount, customFoodItem.getAmount());
    assertEquals(customFoodItemIsCheck, customFoodItem.isChecked());
  }

  @Test
  public void testCustomFoodItemSettersNullName() {
    customFoodItem = new CustomFoodItem();
    assertThrows(NullPointerException.class, () -> customFoodItem.setName(null));
  }

  @Test
  public void testCustomFoodItemSettersNullId() {
    customFoodItem = new CustomFoodItem();
    assertDoesNotThrow(() -> customFoodItem.setId(null));
  }

  @Test
  public void testCustomFoodItemSettersNullShoppingList() {
    customFoodItem = new CustomFoodItem();
    assertDoesNotThrow(() -> customFoodItem.setShoppingList(null));
  }

  @Test
  public void testCustomFoodItemSettersNullHousehold() {
    customFoodItem = new CustomFoodItem();
    assertDoesNotThrow(() -> customFoodItem.setHousehold(null));
  }
}
