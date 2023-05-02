package edu.ntnu.idatt2106.smartmat.mapper.foodproduct;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.BareFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.foodproduct.FoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.foodproduct.IngredientFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.ingredient.BareIngredientDTO;
import edu.ntnu.idatt2106.smartmat.mapper.ingredient.IngredientMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FoodProductMapper {
  FoodProductMapper INSTANCE = Mappers.getMapper(FoodProductMapper.class);

  /**
   * Maps an ingredient to an ingredientDTO.
   * @param ingredients The ingredient to be mapped.
   * @return The mapped ingredientDTO.
   */
  @Named("ingredientsToBareIngredientDTOs")
  default BareIngredientDTO ingredientToBareIngredientDTO(Ingredient ingredients) {
    return IngredientMapper.INSTANCE.ingredientToBareIngredientDTO(ingredients);
  }

  /**
   * Maps a food product DTO to a food product.
   * @param foodProductDTO The food product DTO to map.
   * @return The mapped food product.
   */
  @Mappings(
    {
      @Mapping(target = "ingredient", ignore = true),
      @Mapping(target = "households", ignore = true),
    }
  )
  FoodProduct foodProductDTOToFoodProduct(FoodProductDTO foodProductDTO);

  /**
   * Maps a bare food product DTO to a food product.
   * @param foodProductDTO The bare food product DTO to map.
   * @return The mapped food product.
   */
  @Mappings(
    {
      @Mapping(target = "ingredient", ignore = true),
      @Mapping(target = "households", ignore = true),
      @Mapping(target = "isNotIngredient", source = "notIngredient"),
    }
  )
  FoodProduct bareFoodProductDTOToFoodProduct(BareFoodProductDTO foodProductDTO);

  /**
   * Maps a food product to a food product DTO.
   * @param foodProduct The food product to map.
   * @return The mapped food product DTO.
   */
  @Mappings(
    {
      @Mapping(
        target = "ingredient",
        source = "ingredient",
        qualifiedByName = "ingredientDTOToIngredient"
      ),
      @Mapping(target = "isNotIngredient", source = "notIngredient"),
    }
  )
  FoodProductDTO foodProductToFoodProductDTO(FoodProduct foodProduct);

  /**
   * Maps a food product to a ingredient food product DTO.
   * @param foodProduct The food product to map.
   * @return The mapped ingredient food product DTO.
   */
  IngredientFoodProductDTO foodProductToIngredientFoodProductDTO(FoodProduct foodProduct);

  /**
   * Maps a ingredient food product DTO to a food product.
   * @param ingredientFoodProductDTO The ingredient food product DTO to map.
   * @return The mapped food product.
   */
  @Mappings(
    {
      @Mapping(target = "ingredient", ignore = true),
      @Mapping(target = "households", ignore = true),
    }
  )
  FoodProduct ingredientFoodProductDTOToFoodProduct(
    IngredientFoodProductDTO ingredientFoodProductDTO
  );
}
