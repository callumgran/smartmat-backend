package edu.ntnu.idatt2106.smartmat.mapper.recipe;

import edu.ntnu.idatt2106.smartmat.dto.ingredient.IngredientDTO;
import edu.ntnu.idatt2106.smartmat.dto.recipe.RecipeIngredientDTO;
import edu.ntnu.idatt2106.smartmat.mapper.ingredient.IngredientMapper;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for recipe ingredient.
 * @author Callum G.
 * @version 1.0 - 21.04.2023
 */
@Mapper(componentModel = "spring")
public interface RecipeIngredientMapper {
  RecipeIngredientMapper INSTANCE = Mappers.getMapper(RecipeIngredientMapper.class);

  /**
   * Maps an ingredientDTO to an ingredient.
   * @param ingredientDTO The ingredientDTO to be mapped.
   * @return The mapped ingredient.
   */
  @Named("ingredientDTOToIngredient")
  default Ingredient ingredientDTOToIngredient(IngredientDTO ingredientDTO) {
    return IngredientMapper.INSTANCE.ingredientDTOToIngredient(ingredientDTO);
  }

  /**
   * Maps an ingredient to an ingredientDTO.
   * @param ingredient The ingredient to be mapped.
   * @return The mapped ingredientDTO.
   */
  @Named("ingredientToIngredientDTO")
  default IngredientDTO ingredientToIngredientDTO(Ingredient ingredient) {
    return IngredientMapper.INSTANCE.ingredientToIngredientDTO(ingredient);
  }

  /**
   * Maps a recipe ingredient DTO to a recipe ingredient.
   * @param recipeIngredientDTO The recipe ingredient DTO to be mapped.
   * @return The mapped recipe ingredient.
   */
  @Mappings(
    {
      @Mapping(target = "recipe", ignore = true),
      @Mapping(
        target = "ingredient",
        source = "ingredient",
        qualifiedByName = "ingredientDTOToIngredient"
      ),
    }
  )
  RecipeIngredient recipeIngredientDTOToRecipeIngredient(RecipeIngredientDTO recipeIngredientDTO);

  /**
   * Maps a recipe ingredient to a recipe ingredient DTO.
   * @param recipeIngredient The recipe ingredient to be mapped.
   * @return The mapped recipe ingredient DTO.
   */
  @Mappings(
    {
      @Mapping(
        target = "ingredient",
        source = "ingredient",
        qualifiedByName = "ingredientToIngredientDTO"
      ),
    }
  )
  RecipeIngredientDTO recipeIngredientToRecipeIngredientDTO(RecipeIngredient recipeIngredient);
}
