package edu.ntnu.idatt2106.smartmat.repository.foodproduct;

import static org.junit.Assert.assertEquals;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import java.time.LocalDate;
import java.util.HashSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HouseholdFoodProductRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private HouseholdFoodProductRepository foodProductRepository;

  @Test
  public void findByEAN() {
    Ingredient carrot = new Ingredient(null, "Carrot", new HashSet<>(), new HashSet<>(), null);
    Ingredient raisin = new Ingredient(null, "Raisin", new HashSet<>(), new HashSet<>(), null);

    carrot = entityManager.persist(carrot);
    raisin = entityManager.persist(raisin);

    FoodProduct carrotFoodProduct = new FoodProduct(
      null,
      "Carrot",
      "1234567890123",
      4.0D,
      true,
      new HashSet<>(),
      carrot,
      null
    );
    FoodProduct raisinFoodProduct = new FoodProduct(
      null,
      "Raisin",
      "1234567890124",
      2.0D,
      true,
      new HashSet<>(),
      raisin,
      null
    );

    entityManager.persist(carrotFoodProduct);
    entityManager.persist(raisinFoodProduct);

    Household household = new Household(
      null,
      "Household",
      new HashSet<>(),
      new HashSet<>(),
      new HashSet<>(),
      new HashSet<>(),
      new HashSet<>(),
      new HashSet<>()
    );

    household = entityManager.persist(household);

    HouseholdFoodProduct hfpCarrot = new HouseholdFoodProduct(
      null,
      carrotFoodProduct,
      household,
      LocalDate.now(),
      2.0D
    );
    HouseholdFoodProduct hfpRaisin = new HouseholdFoodProduct(
      null,
      raisinFoodProduct,
      household,
      LocalDate.now(),
      5.0D
    );

    entityManager.persist(hfpCarrot);
    entityManager.persist(hfpRaisin);

    entityManager.flush();

    HouseholdFoodProduct found = foodProductRepository
      .findHouseholdFoodProductByHouseholdAndEAN(household.getId(), carrotFoodProduct.getEAN())
      .orElseThrow(NullPointerException::new)
      .stream()
      .findFirst()
      .get();

    assertEquals(hfpCarrot.getFoodProduct().getName(), found.getFoodProduct().getName());
    assertEquals(hfpCarrot.getFoodProduct().getEAN(), found.getFoodProduct().getEAN());
    assertEquals(hfpCarrot.getHousehold().getId(), found.getHousehold().getId());
    assertEquals(hfpCarrot.getAmountLeft(), found.getAmountLeft(), 2.0D);
  }
}
