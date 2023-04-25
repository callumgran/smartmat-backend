package edu.ntnu.idatt2106.smartmat.repository.shoppinglist;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShoppingListRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ShoppingListRepository shoppingListRepository;

  @Test
  public void testGetCurrentShoppingListByHousehold() {
    Household household = Household.builder().name("Household").build();
    ShoppingList completedList = ShoppingList
      .builder()
      .dateCompleted(LocalDate.now())
      .household(household)
      .build();
    ShoppingList currentList = ShoppingList.builder().household(household).build();

    entityManager.persist(household);
    entityManager.persist(completedList);
    entityManager.persist(currentList);
    entityManager.flush();

    Optional<Collection<ShoppingList>> foundList = shoppingListRepository.getCurrentShoppingListByHousehold(
      household.getId()
    );

    assertTrue(foundList.isPresent());
    assertEquals(currentList.getId(), foundList.get().stream().toList().get(0).getId());
  }

  @Test
  public void testGetCurrentShoppingListByHouseholdNoCurrentList() {
    Household household = Household.builder().name("Household").build();
    ShoppingList completedList = ShoppingList
      .builder()
      .dateCompleted(LocalDate.now())
      .household(household)
      .build();

    entityManager.persist(household);
    entityManager.persist(completedList);
    entityManager.flush();

    Optional<Collection<ShoppingList>> foundList = shoppingListRepository.getCurrentShoppingListByHousehold(
      household.getId()
    );

    assertTrue(foundList.get().isEmpty());
  }
}
