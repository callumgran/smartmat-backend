package edu.ntnu.idatt2106.smartmat.mapper.ingredient;

import edu.ntnu.idatt2106.smartmat.dto.ingredient.IngredientDTO;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for ingredient.
 * @author Tobias. O
 * @version 1.0 - 20.04.2023
 */
@Mapper(componentModel = "spring")
public interface IngredientMapper {
  IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

  /**
   * Maps an ingredient to an ingredientDTO.
   * @param ingredient The ingredient to be mapped.
   * @return The mapped ingredientDTO.
   */
  IngredientDTO ingredientToIngredientDTO(Ingredient ingredient);

  /**
   * Maps an ingredientDTO to an ingredient.
   * @param ingredientDTO The ingredientDTO to be mapped.
   * @return The mapped ingredient.
   */
  @Mappings({ @Mapping(target = "recipes", ignore = true) })
  Ingredient ingredientDTOToIngredient(IngredientDTO ingredientDTO);
}
