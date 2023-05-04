package edu.ntnu.idatt2106.smartmat.repository.foodproduct;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;
import java.util.HashSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for the FoodProduct repository.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class FoodProductRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private FoodProductRepository foodProductRepository;

  @Test
  public void findByEAN() {
    Ingredient carrot = new Ingredient(null, "Carrot", new HashSet<>(), new HashSet<>(), null);
    Ingredient raisin = new Ingredient(null, "Raisin", new HashSet<>(), new HashSet<>(), null);
    Unit unit = new Unit("kilogram", "kg", new HashSet<>(), 1, UnitTypeEnum.SOLID, new HashSet<>());
    carrot = entityManager.persist(carrot);
    raisin = entityManager.persist(raisin);
    unit = entityManager.persist(unit);

    FoodProduct carrotFoodProduct = new FoodProduct(
      null,
      "Carrot",
      "1234567890123",
      4.0D,
      true,
      null,
      new HashSet<>(),
      carrot,
      null,
      false,
      unit,
      null
    );
    FoodProduct raisinFoodProduct = new FoodProduct(
      null,
      "Raisin",
      "1234567890124",
      2.0D,
      true,
      null,
      new HashSet<>(),
      raisin,
      null,
      false,
      unit,
      null
    );
    entityManager.persist(carrotFoodProduct);
    entityManager.persist(raisinFoodProduct);
    entityManager.flush();

    FoodProduct found = foodProductRepository.findByEAN(carrotFoodProduct.getEAN()).get();

    assertEquals(carrotFoodProduct.getName(), found.getName());
    assertEquals(carrotFoodProduct.getEAN(), found.getEAN());
    assertEquals(carrotFoodProduct.getAmount(), found.getAmount());
    assertEquals(carrotFoodProduct.isLooseWeight(), found.isLooseWeight());
    assertEquals(carrotFoodProduct.getIngredient().getName(), found.getIngredient().getName());
  }
}
