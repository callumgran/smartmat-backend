package edu.ntnu.idatt2106.smartmat.mapper.ingredient;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.IngredientFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.ingredient.BareIngredientDTO;
import edu.ntnu.idatt2106.smartmat.dto.ingredient.IngredientDTO;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.FoodProductMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for ingredient.
 * @author Tobias. O, Callum G, Thomas S.
 * @version 1.1 - 21.04.2023
 */
@Mapper(componentModel = "spring")
public interface IngredientMapper {
  IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

  /**
   * Maps a set of food products to a list of ingredient food product DTOs.
   * @param foodProducts The food products to be mapped.
   * @return The mapped list of ingredient food product DTOs.
   */
  @Named("foodProductsToIngredientFoodProducts")
  default List<IngredientFoodProductDTO> foodProductsToIngredientFoodProducts(
    Set<FoodProduct> foodProducts
  ) {
    if (foodProducts == null) {
      return new ArrayList<>();
    }
    return foodProducts
      .stream()
      .map(FoodProductMapper.INSTANCE::foodProductToIngredientFoodProductDTO)
      .toList();
  }

  /**
   * Maps a list of ingredient food product DTOs to a set of food products.
   * @param ingredientFoodProducts The ingredient food product DTOs to be mapped.
   * @return The mapped set of food products.
   */
  @Named("ingredientFoodProductsToFoodProducts")
  default Set<FoodProduct> ingredientFoodProductsToFoodProducts(
    List<IngredientFoodProductDTO> ingredientFoodProducts
  ) {
    if (ingredientFoodProducts == null) {
      return new HashSet<>();
    }
    return ingredientFoodProducts
      .stream()
      .map(FoodProductMapper.INSTANCE::ingredientFoodProductDTOToFoodProduct)
      .collect(Collectors.toSet());
  }

  /**
   * Maps an ingredient to an ingredientDTO.
   * @param ingredient The ingredient to be mapped.
   * @return The mapped ingredientDTO.
   */
  @Mappings(
    {
      @Mapping(
        target = "foodProducts",
        source = "foodProducts",
        qualifiedByName = "foodProductsToIngredientFoodProducts"
      ),
    }
  )
  IngredientDTO ingredientToIngredientDTO(Ingredient ingredient);

  /**
   * Maps an ingredientDTO to an ingredient.
   * @param ingredientDTO The ingredientDTO to be mapped.
   * @return The mapped ingredient.
   */
  @Mappings(
    {
      @Mapping(
        target = "foodProducts",
        source = "foodProducts",
        qualifiedByName = "ingredientFoodProductsToFoodProducts"
      ),
      @Mapping(target = "recipes", ignore = true),
    }
  )
  Ingredient ingredientDTOToIngredient(IngredientDTO ingredientDTO);

  /**
   * Maps an ingredient to an BareIngredientDTO.
   * @param ingredient The ingredient to be mapped.
   * @return The mapped BareIngredientDTO.
   */
  BareIngredientDTO ingredientToBareIngredientDTO(Ingredient ingredient);
}
