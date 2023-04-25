package edu.ntnu.idatt2106.smartmat.controller.ingredient;

import edu.ntnu.idatt2106.smartmat.dto.ingredient.IngredientDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.ingredient.IngredientNotFoundException;
import edu.ntnu.idatt2106.smartmat.mapper.ingredient.IngredientMapper;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for public ingredient endpoints.
 * @author Tobias O.
 * @version 1.0 - 20.04.2023
 */
@RestController
@RequestMapping("/api/v1/public/ingredients")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class IngredientController {

  private final IngredientService ingredientService;

  private static final Logger LOGGER = LoggerFactory.getLogger(IngredientController.class);

  /**
   * Get an ingredient by ID.
   * @param id long ID of the ingredient.
   * @throws NullPointerException if the ingredient is not found.
   * @return the ingredient.
   */
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<IngredientDTO> getIngredient(@PathVariable long id)
    throws IngredientNotFoundException, NullPointerException {
    LOGGER.info("GET request for ingredient: {}", id);

    Ingredient ingredient = ingredientService.getIngredientById(id);

    IngredientDTO ingredientDTO = IngredientMapper.INSTANCE.ingredientToIngredientDTO(ingredient);

    LOGGER.info("Mapped ingredient to IngredientDTO {}", ingredientDTO);

    return ResponseEntity.ok(ingredientDTO);
  }
}
