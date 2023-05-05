package edu.ntnu.idatt2106.smartmat.unit.filtering;

import static org.junit.Assert.assertEquals;

import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.filtering.SearchSpecification;
import edu.ntnu.idatt2106.smartmat.filtering.SortDirection;
import edu.ntnu.idatt2106.smartmat.filtering.SortRequest;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;
import edu.ntnu.idatt2106.smartmat.repository.ingredient.IngredientRepository;
import edu.ntnu.idatt2106.smartmat.repository.unit.UnitRepository;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IngredientSortTest {

  @Autowired
  private IngredientRepository ingredientRepository;

  @Autowired
  private UnitRepository unitRepository;

  private SearchSpecification<Ingredient> searchSpecification;

  private Ingredient ingredient1;

  private Ingredient ingredient2;

  private Ingredient ingredient3;

  private Ingredient ingredient4;

  private Unit unit;

  private Unit gram;

  @Before
  public void setUp() {
    ingredientRepository.deleteAll();

    unitRepository.deleteAll();

    unit = new Unit("kilogram", "kg", new HashSet<>(), 1, UnitTypeEnum.SOLID, new HashSet<>());
    gram = new Unit("gram", "g", new HashSet<>(), 0.001, UnitTypeEnum.SOLID, new HashSet<>());
    ingredient1 = new Ingredient(1L, "Carrot", null, null, unit);
    ingredient2 = new Ingredient(2L, "Potato", null, null, unit);
    ingredient3 = new Ingredient(3L, "Onion", null, null, unit);
    ingredient4 = new Ingredient(4L, "Garlic", null, null, gram);

    unitRepository.save(unit);
    unitRepository.save(gram);

    ingredient1 = ingredientRepository.save(ingredient1);
    ingredient2 = ingredientRepository.save(ingredient2);
    ingredient3 = ingredientRepository.save(ingredient3);
    ingredient4 = ingredientRepository.save(ingredient4);
  }

  @Test
  public void testSortAsc() {
    SortRequest sortRequest = new SortRequest("id", SortDirection.ASC);
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.getSortRequests().add(sortRequest);

    searchSpecification = new SearchSpecification<>(searchRequest);

    Pageable pageable = SearchSpecification.getPageable(searchRequest);

    Page<Ingredient> found = ingredientRepository.findAll(searchSpecification, pageable);

    assertEquals(4, found.getTotalElements());
    assertEquals(ingredient1.getName(), found.getContent().get(0).getName());
    assertEquals(ingredient2.getName(), found.getContent().get(1).getName());
    assertEquals(ingredient3.getName(), found.getContent().get(2).getName());
    assertEquals(ingredient4.getName(), found.getContent().get(3).getName());
  }

  @Test
  public void testSortDesc() {
    SortRequest sortRequest = new SortRequest("id", SortDirection.DESC);
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.getSortRequests().add(sortRequest);

    searchSpecification = new SearchSpecification<>(searchRequest);

    Pageable pageable = SearchSpecification.getPageable(searchRequest);

    Page<Ingredient> found = ingredientRepository.findAll(searchSpecification, pageable);

    assertEquals(4, found.getTotalElements());
    assertEquals(ingredient4.getName(), found.getContent().get(0).getName());
    assertEquals(ingredient3.getName(), found.getContent().get(1).getName());
    assertEquals(ingredient2.getName(), found.getContent().get(2).getName());
    assertEquals(ingredient1.getName(), found.getContent().get(3).getName());
  }
}
