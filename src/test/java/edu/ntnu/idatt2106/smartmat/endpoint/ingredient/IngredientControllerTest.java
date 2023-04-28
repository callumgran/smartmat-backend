package edu.ntnu.idatt2106.smartmat.endpoint.ingredient;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ntnu.idatt2106.smartmat.controller.ingredient.IngredientController;
import edu.ntnu.idatt2106.smartmat.exceptions.ingredient.IngredientNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.security.SecurityConfig;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link IngredientController}.
 * All test endpoints are public and don't require authentication, this
 * is already tested, so we only test the controller.
 * @author Callum G.
 * @version 1.0 - 20.04.2023
 */
@RunWith(SpringRunner.class)
@WebMvcTest({ IngredientController.class, SecurityConfig.class })
public class IngredientControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private IngredientService ingredientService;

  private static final String BASE_URL = "/api/v1/public/ingredients";

  private Ingredient carrot;

  @Before
  public void setUp() {
    carrot = new Ingredient(1L, "Carrot", null, null, null);
  }

  @Test
  public void testGetIngredientThatExists() {
    try {
      when(ingredientService.getIngredientById(1)).thenReturn(carrot);
      mvc
        .perform(get(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetIngredientThatDoesNotExist() {
    try {
      when(ingredientService.getIngredientById(1)).thenThrow(new IngredientNotFoundException());
      mvc
        .perform(get(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
