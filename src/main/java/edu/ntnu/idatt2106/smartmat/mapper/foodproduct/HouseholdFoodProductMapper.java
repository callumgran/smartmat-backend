package edu.ntnu.idatt2106.smartmat.mapper.foodproduct;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CreateHouseholdFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.foodproduct.FoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.foodproduct.HouseholdFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.foodproduct.UpdateHouseholdFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for mapping between food product DTOs and food products.
 * Uses MapStruct to generate the implementation.
 * @author Callum G.
 * @version 1.0 05.05.2023
 */
@Mapper(componentModel = "spring")
public interface HouseholdFoodProductMapper {
  static HouseholdFoodProductMapper INSTANCE = Mappers.getMapper(HouseholdFoodProductMapper.class);

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
   * Maps a food product DTO to a food product.
   * @param foodProductDTO The food product DTO to map.
   * @return The mapped food product.
   */
  @Named("foodProductDTOTofoodProduct")
  default FoodProduct foodProductDTOTofoodProduct(FoodProductDTO foodProductDTO) {
    return FoodProductMapper.INSTANCE.foodProductDTOToFoodProduct(foodProductDTO);
  }

  /**
   * Maps a food product to a food product DTO.
   * @param foodProduct The food product to map.
   * @return The mapped food product DTO.
   */
  @Named("foodProductToFoodProductDTO")
  default FoodProductDTO foodProductToFoodProductDTO(FoodProduct foodProduct) {
    return FoodProductMapper.INSTANCE.foodProductToFoodProductDTO(foodProduct);
  }

  /**
   * Maps a food product DTO to a food product.
   * @param householdFoodProduct The food product DTO to map.
   * @return The mapped food product.
   */
  @Mappings(
    {
      @Mapping(target = "id", source = "id", qualifiedByName = "stringToUUID"),
      @Mapping(target = "household", ignore = true),
      @Mapping(
        target = "foodProduct",
        source = "foodProduct",
        qualifiedByName = "foodProductDTOTofoodProduct"
      ),
    }
  )
  HouseholdFoodProduct householdFoodProductDTOToHouseholdFoodProduct(
    HouseholdFoodProductDTO householdFoodProduct
  );

  /**
   * Maps a food product to a food product DTO.
   * @param householdFoodProduct The food product to map.
   * @return The mapped food product DTO.
   */
  @Mappings(
    {
      @Mapping(target = "id", source = "id", qualifiedByName = "UUIDToString"),
      @Mapping(target = "householdId", source = "household.id", qualifiedByName = "UUIDToString"),
      @Mapping(
        target = "foodProduct",
        source = "foodProduct",
        qualifiedByName = "foodProductToFoodProductDTO"
      ),
    }
  )
  HouseholdFoodProductDTO householdFoodProductToHouseholdFoodProductDTO(
    HouseholdFoodProduct householdFoodProduct
  );

  /**
   * Maps a create food product DTO to a food product.
   * @param createHouseholdFoodProductDTO The food product DTO to map.
   * @return The mapped food product.
   */
  @Mappings(
    {
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "household", ignore = true),
      @Mapping(target = "foodProduct", ignore = true),
    }
  )
  HouseholdFoodProduct createHouseholdFoodProductDTOToHouseholdFoodProduct(
    CreateHouseholdFoodProductDTO createHouseholdFoodProductDTO
  );

  /**
   * Maps a update food product DTO to a food product.
   * @param householdFoodProductDTO The food product DTO to map.
   * @return The mapped food product.
   */
  @Mappings(
    {
      @Mapping(target = "household", ignore = true),
      @Mapping(target = "foodProduct", ignore = true),
    }
  )
  HouseholdFoodProduct updateHouseholdFoodProductDTOToHouseholdFoodProduct(
    UpdateHouseholdFoodProductDTO householdFoodProductDTO
  );
}
