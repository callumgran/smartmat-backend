package edu.ntnu.idatt2106.smartmat.integration.ingredients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.DatabaseException;
import edu.ntnu.idatt2106.smartmat.exceptions.ingredient.IngredientNotFoundException;
import edu.ntnu.idatt2106.smartmat.filtering.FilterRequest;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.filtering.SearchSpecification;
import edu.ntnu.idatt2106.smartmat.filtering.SortDirection;
import edu.ntnu.idatt2106.smartmat.filtering.SortRequest;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.repository.ingredient.IngredientRepository;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test for the ingredient service.
 */
@RunWith(SpringRunner.class)
public class IngredientServiceIntegrationTest {

  @TestConfiguration
  static class IngredientServiceTestConfiguration {

    @Bean
    public IngredientService ingredientService() {
      return new IngredientServiceImpl();
    }
  }

  @Autowired
  private IngredientService ingredientService;

  @MockBean
  private IngredientRepository ingredientRepository;

  Ingredient carrot;
  Ingredient carrot2;
  Ingredient tomato;

  @Before
  public void setUp() throws IngredientNotFoundException {
    carrot = new Ingredient(1L, "Carrot", null, null);
    carrot2 = new Ingredient(2L, "Carrot", null, null);
    tomato = new Ingredient(3L, "Tomato", null, null);

    when(ingredientRepository.findById(1L)).thenReturn(Optional.of(carrot));
    when(ingredientRepository.findById(2L)).thenReturn(Optional.of(carrot2));
    when(ingredientRepository.findById(3L)).thenReturn(Optional.empty());

    when(ingredientRepository.existsById(1L)).thenReturn(true);
    when(ingredientRepository.existsById(2L)).thenReturn(true);
    when(ingredientRepository.existsById(3L)).thenReturn(false);

    when(ingredientRepository.findByName("Carrot"))
      .thenReturn(Optional.of(List.of(carrot, carrot2)));
    when(ingredientRepository.findByName("Tomato")).thenReturn(Optional.empty());

    when(ingredientRepository.save(carrot)).thenReturn(carrot);

    doNothing().when(ingredientRepository).delete(carrot);
    doNothing().when(ingredientRepository).deleteById(1L);
  }

  @Test
  public void testIngredientExists() {
    assertTrue(ingredientService.ingredientExists(1L));
  }

  @Test
  public void testIngredientDoesNotExist() {
    assertFalse(ingredientService.ingredientExists(3L));
  }

  @Test
  public void testGetIngredientThatExists() throws IngredientNotFoundException {
    Ingredient ingredient = ingredientService.getIngredientById(1);
    assertEquals(carrot, ingredient);
  }

  @Test
  public void testGetIngredientThatDoesNotExist() throws IngredientNotFoundException {
    assertThrows(IngredientNotFoundException.class, () -> ingredientService.getIngredientById(3));
  }

  @Test
  public void testGetIngredientsByNamesThatExists() throws NullPointerException {
    List<Ingredient> ingredients = ingredientService
      .getIngredientsByName("Carrot")
      .stream()
      .toList();
    assertEquals(2, ingredients.size());
    assertEquals(carrot.getName(), ingredients.get(0).getName());
    assertEquals(carrot2.getName(), ingredients.get(1).getName());
  }

  @Test
  public void testGetIngredientsByNamesThatDoesNotExist() throws NullPointerException {
    assertThrows(
      NullPointerException.class,
      () -> ingredientService.getIngredientsByName("Tomato")
    );
  }

  @Test
  public void testSaveIngredient() {
    Ingredient ingredient = ingredientService.saveingredient(carrot);
    assertEquals(carrot.getName(), ingredient.getName());
  }

  @Test
  public void testDeleteIngredient() throws IngredientNotFoundException {
    ingredientService.deleteIngredient(carrot);
  }

  @Test
  public void testDeleteIngredientThatDoesNotExist() throws IngredientNotFoundException {
    assertThrows(
      IngredientNotFoundException.class,
      () -> ingredientService.deleteIngredient(tomato)
    );
  }

  @Test
  public void testDeleteIngredientById() throws IngredientNotFoundException {
    ingredientService.deleteIngredientById(1L);
  }

  @Test
  public void testDeleteIngredientByIdThatDoesNotExist() throws IngredientNotFoundException {
    assertThrows(
      IngredientNotFoundException.class,
      () -> ingredientService.deleteIngredientById(3L)
    );
  }

  @Test
  public void testGetAllIngredients() throws DatabaseException {
    when(ingredientRepository.findAll()).thenReturn(List.of(carrot, carrot2));
    List<Ingredient> ingredients = ingredientService.getAllIngredients().stream().toList();
    assertEquals(2, ingredients.size());
    assertEquals(carrot.getName(), ingredients.get(0).getName());
    assertEquals(carrot2.getName(), ingredients.get(1).getName());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testEmptySearchRequestReturnsEmptyList() {
    SearchRequest searchRequest = SearchRequest.builder().filterRequests(List.of()).build();
    when(ingredientRepository.findAll(any(SearchSpecification.class), any(Pageable.class)))
      .thenReturn(Page.empty());
    Page<Ingredient> ingredients = ingredientService.getIngredientsBySearch(searchRequest);
    assertEquals(0, ingredients.getTotalElements());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testSortRequestReturnsListInOrder() {
    SearchRequest searchRequest = SearchRequest
      .builder()
      .sortRequests(
        List.of(SortRequest.builder().keyWord("name").sortDirection(SortDirection.ASC).build())
      )
      .build();
    Page<Ingredient> ingredients = new PageImpl<>(List.of(carrot, carrot2));
    when(ingredientRepository.findAll(any(SearchSpecification.class), any(Pageable.class)))
      .thenReturn(ingredients);
    Page<Ingredient> ingredientsPage = ingredientService.getIngredientsBySearch(searchRequest);
    assertEquals(2, ingredientsPage.getTotalElements());
    assertEquals(carrot, ingredientsPage.getContent().get(0));
    assertEquals(carrot2, ingredientsPage.getContent().get(1));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFilterIngredientsByNameCanBePartialMatch() {
    SearchRequest searchRequest = SearchRequest
      .builder()
      .filterRequests(List.of(FilterRequest.builder().keyWord("name").value("Car").build()))
      .build();
    Page<Ingredient> ingredients = new PageImpl<>(List.of(carrot, carrot2));
    when(ingredientRepository.findAll(any(SearchSpecification.class), any(Pageable.class)))
      .thenReturn(ingredients);
    Page<Ingredient> ingredientsPage = ingredientService.getIngredientsBySearch(searchRequest);
    assertEquals(2, ingredientsPage.getTotalElements());
    assertEquals(carrot, ingredientsPage.getContent().get(0));
    assertEquals(carrot2, ingredientsPage.getContent().get(1));
  }
}
