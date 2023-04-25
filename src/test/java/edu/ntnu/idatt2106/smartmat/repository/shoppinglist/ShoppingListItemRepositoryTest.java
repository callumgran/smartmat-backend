package edu.ntnu.idatt2106.smartmat.repository.shoppinglist;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
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
public class ShoppingListItemRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ShoppingListItemRepository shoppingListItemRepository;

  @Test
  public void testGetShoppingListItemInHousehold() {
    Household household = Household.builder().name("Household").build();
    ShoppingList shoppingList = ShoppingList.builder().household(household).build();
    Ingredient ingredient = Ingredient.builder().name("Ingredient").build();
    ShoppingListItem shoppingListItem = ShoppingListItem
      .builder()
      .ingredient(ingredient)
      .amount(1)
      .shoppingList(shoppingList)
      .build();

    entityManager.persist(household);
    entityManager.persist(shoppingList);
    entityManager.persist(ingredient);
    entityManager.persist(shoppingListItem);
    entityManager.flush();

    ShoppingListItem foundItem = shoppingListItemRepository
      .findByIdInShoppingList(shoppingListItem.getId(), shoppingList.getId())
      .get()
      .stream()
      .toList()
      .get(0);

    assertEquals(shoppingListItem.getId(), foundItem.getId());
  }

  @Test
  public void testDoNotGetShoppingListItemInAnotherHousehold() {
    Household differentHousehold = Household.builder().name("Another household").build();
    ShoppingList differentShoppingList = ShoppingList
      .builder()
      .household(differentHousehold)
      .build();
    Household household = Household.builder().name("Household").build();
    ShoppingList shoppingList = ShoppingList.builder().household(household).build();
    Ingredient ingredient = Ingredient.builder().name("Ingredient").build();
    ShoppingListItem shoppingListItem = ShoppingListItem
      .builder()
      .ingredient(ingredient)
      .amount(1)
      .shoppingList(shoppingList)
      .build();

    entityManager.persist(differentHousehold);
    entityManager.persist(differentShoppingList);
    entityManager.persist(household);
    entityManager.persist(shoppingList);
    entityManager.persist(ingredient);
    entityManager.persist(shoppingListItem);
    entityManager.flush();

    Optional<Collection<ShoppingListItem>> foundItem = shoppingListItemRepository.findByIdInShoppingList(
      shoppingListItem.getId(),
      differentShoppingList.getId()
    );

    assertTrue(foundItem.get().isEmpty());
  }

  @Test
  public void testGetNoEmptyListWithNoShoppingListItems() {
    Optional<Collection<ShoppingListItem>> foundItem = shoppingListItemRepository.findByIdInShoppingList(
      UUID.randomUUID(),
      UUID.randomUUID()
    );

    assertTrue(foundItem.get().isEmpty());
  }
}
