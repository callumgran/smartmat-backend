package edu.ntnu.idatt2106.smartmat.repository.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2106.smartmat.filtering.FieldType;
import edu.ntnu.idatt2106.smartmat.filtering.FilterRequest;
import edu.ntnu.idatt2106.smartmat.filtering.Operator;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.filtering.SearchSpecification;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for the ingredient repository.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class IngredientRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private IngredientRepository ingredientRepository;

  @Test
  public void testFindByName() {
    Ingredient carrot = Ingredient.builder().name("Carrot").build();
    Ingredient carrot2 = Ingredient.builder().name("Carrot").build();
    Ingredient onion = Ingredient.builder().name("Onion").build();

    entityManager.persist(carrot);
    entityManager.persist(carrot2);
    entityManager.persist(onion);
    entityManager.flush();

    List<Ingredient> found = ingredientRepository.findByName("Carrot").get().stream().toList();

    assertEquals(found.size(), 2);
    assertEquals(found.get(0).getName(), "Carrot");
    assertEquals(found.get(1).getName(), "Carrot");
  }

  @Test
  public void testFindByPartialNameInSearchRequest() {
    Ingredient carrot = Ingredient.builder().name("Carrot").build();
    Ingredient carrot2 = Ingredient.builder().name("Carrot").build();
    Ingredient onion = Ingredient.builder().name("Onion").build();

    entityManager.persist(carrot);
    entityManager.persist(carrot2);
    entityManager.persist(onion);
    entityManager.flush();

    SearchRequest searchRequest = SearchRequest
      .builder()
      .filterRequests(
        List.of(
          FilterRequest
            .builder()
            .keyWord("name")
            .value("arro")
            .fieldType(FieldType.STRING)
            .operator(Operator.LIKE)
            .build()
        )
      )
      .build();
    List<Ingredient> found = ingredientRepository
      .findAll(
        new SearchSpecification<Ingredient>(searchRequest),
        SearchSpecification.getPageable(searchRequest)
      )
      .get()
      .toList();

    assertEquals(2, found.size());
    assertEquals(carrot, found.get(0));
    assertEquals(carrot2, found.get(1));
  }
}
