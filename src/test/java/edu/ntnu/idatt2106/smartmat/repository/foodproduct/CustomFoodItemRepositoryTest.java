package edu.ntnu.idatt2106.smartmat.repository.foodproduct;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomFoodItemRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CustomFoodItemRepository customFoodItemRepository;

  @Test
  public void testGetCustomFoodItemInHousehold() {
    Household household = Household.builder().name("Household").build();
    ShoppingList shoppingList = ShoppingList.builder().household(household).build();
    CustomFoodItem customFoodItem = CustomFoodItem
      .builder()
      .name("CustomFoodItem")
      .amount(1)
      .shoppingList(shoppingList)
      .build();

    entityManager.persist(household);
    entityManager.persist(shoppingList);
    entityManager.persist(customFoodItem);
    entityManager.flush();

    CustomFoodItem foundItem = customFoodItemRepository
      .findByIdInShoppingList(customFoodItem.getId(), shoppingList.getId())
      .get()
      .stream()
      .toList()
      .get(0);

    assertEquals(customFoodItem.getId(), foundItem.getId());
  }

  @Test
  public void testDoNotGetCustomFoodItemInAnotherHousehold() {
    Household differentHousehold = Household.builder().name("Another household").build();
    ShoppingList differentShoppingList = ShoppingList
      .builder()
      .household(differentHousehold)
      .build();
    Household household = Household.builder().name("Household").build();
    ShoppingList shoppingList = ShoppingList.builder().household(household).build();
    CustomFoodItem customFoodItem = CustomFoodItem
      .builder()
      .name("CustomFoodItem")
      .amount(1)
      .shoppingList(shoppingList)
      .build();

    entityManager.persist(differentHousehold);
    entityManager.persist(differentShoppingList);
    entityManager.persist(household);
    entityManager.persist(shoppingList);
    entityManager.persist(customFoodItem);
    entityManager.flush();

    Optional<Collection<CustomFoodItem>> foundItem = customFoodItemRepository.findByIdInShoppingList(
      customFoodItem.getId(),
      differentShoppingList.getId()
    );

    assertTrue(foundItem.get().isEmpty());
  }

  @Test
  public void testGetNoEmptyListWithNoCustomFoodItems() {
    Optional<Collection<CustomFoodItem>> foundItem = customFoodItemRepository.findByIdInShoppingList(
      UUID.randomUUID(),
      UUID.randomUUID()
    );

    assertTrue(foundItem.get().isEmpty());
  }

  @Test
  public void testGetNoCustomFoodItemsInHouseholdButNotShoppingList() {
    Household household = Household.builder().name("Household").build();
    ShoppingList shoppingList = ShoppingList.builder().household(household).build();
    CustomFoodItem customFoodItem = CustomFoodItem
      .builder()
      .name("CustomFoodItem")
      .amount(1)
      .household(household)
      .build();

    entityManager.persist(household);
    entityManager.persist(shoppingList);
    entityManager.persist(customFoodItem);
    entityManager.flush();

    Optional<Collection<CustomFoodItem>> foundItem = customFoodItemRepository.findByIdInShoppingList(
      customFoodItem.getId(),
      shoppingList.getId()
    );

    assertTrue(foundItem.get().isEmpty());
  }
}
