package edu.ntnu.idatt2106.smartmat.mapper.recipe;

import edu.ntnu.idatt2106.smartmat.dto.recipe.RecipeCreateDTO;
import edu.ntnu.idatt2106.smartmat.dto.recipe.RecipeDTO;
import edu.ntnu.idatt2106.smartmat.dto.recipe.RecipeIngredientDTO;
import edu.ntnu.idatt2106.smartmat.model.recipe.Recipe;
import edu.ntnu.idatt2106.smartmat.model.recipe.RecipeIngredient;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for recipe.
 * @author Simen G., Callum G., Carl G.
 * @version 1.1 - 26.04.2023
 */
@Mapper(componentModel = "spring")
public interface RecipeMapper {
  RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

  /**
   * Maps a string to a UUID.
   * @param id The string to map.
   * @return The mapped UUID.
   */
  @Named("stringToUUID")
  default UUID stringToUUID(String id) {
    return UUID.fromString(id);
  }

  /**
   * Maps a UUID to a string.
   * @param id The UUID to map.
   * @return The mapped string.
   */
  @Named("UUIDToString")
  default String UUIDToString(UUID id) {
    return id.toString();
  }

  /**
   * Maps an ingredientDTO to an ingredient.
   * @param ingredientDTOs The ingredientDTOs to be mapped.
   * @return The mapped ingredient.
   */
  @Named("ingredientDTOsToIngredients")
  default Set<RecipeIngredient> ingredientDTOToIngredient(
    List<RecipeIngredientDTO> ingredientDTOs
  ) {
    return ingredientDTOs
      .stream()
      .map(RecipeIngredientMapper.INSTANCE::recipeIngredientDTOToRecipeIngredient)
      .collect(Collectors.toSet());
  }

  /**
   * Maps an ingredient to an ingredientDTO.
   * @param ingredients The ingredients to be mapped.
   * @return The mapped ingredientDTO.
   */
  @Named("ingredientsToIngredientDTOs")
  default List<RecipeIngredientDTO> ingredientToIngredientDTO(Set<RecipeIngredient> ingredients) {
    return ingredients
      .stream()
      .map(RecipeIngredientMapper.INSTANCE::recipeIngredientToRecipeIngredientDTO)
      .toList();
  }

  /**
   * Maps a recipe to a recipeDTO.
   * @param recipe The recipe to be mapped.
   * @return The mapped recipeDTO.
   */
  @Mappings(
    {
      @Mapping(target = "id", source = "id", qualifiedByName = "stringToUUID"),
      @Mapping(
        target = "ingredients",
        source = "ingredients",
        qualifiedByName = "ingredientsToIngredientDTOs"
      ),
    }
  )
  RecipeDTO recipeToRecipeDTO(Recipe recipe);

  /**
   * Maps a recipeDTO to a recipe.
   * @param recipeDTO The recipeDTO to be mapped.
   * @return The mapped recipe.
   */
  @Mappings(
    {
      @Mapping(target = "id", source = "id", qualifiedByName = "UUIDToString"),
      @Mapping(
        target = "ingredients",
        source = "ingredients",
        qualifiedByName = "ingredientDTOsToIngredients"
      ),
    }
  )
  Recipe recipeDTOToRecipe(RecipeDTO recipeDTO);

  /**
   * Maps a recipeDTO to a recipe.
   * @param recipeDTO The recipeDTO to be mapped.
   * @return The mapped recipe.
   */
  @Mappings(
    { @Mapping(target = "id", ignore = true), @Mapping(target = "ingredients", ignore = true) }
  )
  Recipe recipeCreateDTOToRecipe(RecipeCreateDTO recipeDTO);
}
