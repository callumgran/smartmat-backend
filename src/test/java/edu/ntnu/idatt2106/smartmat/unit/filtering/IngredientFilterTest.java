package edu.ntnu.idatt2106.smartmat.unit.filtering;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2106.smartmat.filtering.FieldType;
import edu.ntnu.idatt2106.smartmat.filtering.FilterRequest;
import edu.ntnu.idatt2106.smartmat.filtering.Operator;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.filtering.SearchSpecification;
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
public class IngredientFilterTest {

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
  public void testIngredientFilterSpecificationEqual() {
    FilterRequest filterRequest = FilterRequest
      .builder()
      .value(ingredient1.getName())
      .fieldType(FieldType.STRING)
      .keyWord("name")
      .operator(Operator.EQUAL)
      .build();

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.getFilterRequests().add(filterRequest);

    searchSpecification = new SearchSpecification<>(searchRequest);

    Page<Ingredient> found = ingredientRepository.findAll(searchSpecification, Pageable.unpaged());

    assertEquals(found.getSize(), 1);
    assertEquals(found.getContent().get(0).getName(), ingredient1.getName());
  }

  @Test
  public void testIngredientFilterSpecificationNotEqual() {
    FilterRequest filterRequest = FilterRequest
      .builder()
      .value(ingredient1.getName())
      .fieldType(FieldType.STRING)
      .keyWord("name")
      .operator(Operator.NOT_EQUAL)
      .build();

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.getFilterRequests().add(filterRequest);

    searchSpecification = new SearchSpecification<>(searchRequest);

    Page<Ingredient> found = ingredientRepository.findAll(searchSpecification, Pageable.unpaged());

    assertEquals(found.getSize(), 3);
    assertEquals(found.getContent().get(0).getName(), ingredient2.getName());
    assertEquals(found.getContent().get(1).getName(), ingredient3.getName());
    assertEquals(found.getContent().get(2).getName(), ingredient4.getName());
  }

  @Test
  public void testIngredientFilterSpecificationGreaterThan() {
    FilterRequest filterRequest = FilterRequest
      .builder()
      .value(ingredient1.getId())
      .fieldType(FieldType.LONG)
      .keyWord("id")
      .operator(Operator.EQUAL)
      .build();

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.getFilterRequests().add(filterRequest);

    searchSpecification = new SearchSpecification<>(searchRequest);

    Page<Ingredient> found = ingredientRepository.findAll(searchSpecification, Pageable.unpaged());

    assertEquals(found.getSize(), 1);
    assertEquals(found.getContent().get(0).getName(), ingredient1.getName());
  }

  @Test
  public void testIngredientFilterSpecificationLessThan() {
    FilterRequest filterRequest = FilterRequest
      .builder()
      .value(ingredient2.getId())
      .fieldType(FieldType.LONG)
      .keyWord("id")
      .operator(Operator.LESS_THAN)
      .build();

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.getFilterRequests().add(filterRequest);

    searchSpecification = new SearchSpecification<>(searchRequest);

    Page<Ingredient> found = ingredientRepository.findAll(searchSpecification, Pageable.unpaged());

    assertEquals(found.getSize(), 1);
    assertEquals(found.getContent().get(0).getName(), ingredient1.getName());
  }

  @Test
  public void testIngredientFilterSpecificationGreaterThanEqual() {
    FilterRequest filterRequest = FilterRequest
      .builder()
      .value(ingredient1.getId())
      .fieldType(FieldType.LONG)
      .keyWord("id")
      .operator(Operator.GREATER_THAN_EQUAL)
      .build();

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.getFilterRequests().add(filterRequest);

    searchSpecification = new SearchSpecification<>(searchRequest);

    Page<Ingredient> found = ingredientRepository.findAll(searchSpecification, Pageable.unpaged());

    assertEquals(found.getSize(), 4);
    assertEquals(found.getContent().get(0).getName(), ingredient1.getName());
    assertEquals(found.getContent().get(1).getName(), ingredient2.getName());
    assertEquals(found.getContent().get(2).getName(), ingredient3.getName());
    assertEquals(found.getContent().get(3).getName(), ingredient4.getName());
  }

  @Test
  public void testIngredientFilterSpecificationLessThanEqual() {
    FilterRequest filterRequest = FilterRequest
      .builder()
      .value(ingredient1.getId())
      .fieldType(FieldType.LONG)
      .keyWord("id")
      .operator(Operator.LESS_THAN_EQUAL)
      .build();

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.getFilterRequests().add(filterRequest);

    searchSpecification = new SearchSpecification<>(searchRequest);

    Page<Ingredient> found = ingredientRepository.findAll(searchSpecification, Pageable.unpaged());

    assertEquals(found.getSize(), 1);
    assertEquals(found.getContent().get(0).getName(), ingredient1.getName());
  }

  @Test
  public void testIngredientFilterSpecificationLike() {
    FilterRequest filterRequest = FilterRequest
      .builder()
      .value("otat")
      .fieldType(FieldType.STRING)
      .keyWord("name")
      .operator(Operator.LIKE)
      .build();

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.getFilterRequests().add(filterRequest);

    searchSpecification = new SearchSpecification<>(searchRequest);

    Page<Ingredient> found = ingredientRepository.findAll(searchSpecification, Pageable.unpaged());

    assertEquals(found.getSize(), 1);
    assertEquals(found.getContent().get(0).getName(), ingredient2.getName());
  }
}
