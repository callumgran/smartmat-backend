package edu.ntnu.idatt2106.smartmat.mapper.statistic;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.FoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.statistic.FoodProductHistoryDTO;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.FoodProductMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for shopping list items.
 * @author Callum G.
 * @version 1.0 - 01.05.2023.
 */
@Mapper(componentModel = "spring")
public interface FoodProductHistoryMapper {
  FoodProductHistoryMapper INSTANCE = Mappers.getMapper(FoodProductHistoryMapper.class);

  /**
   * Gets the household id from the food product history.
   * @param foodProductHistory the food product history to get the household id from.
   * @return the household id.
   */
  @Named("householdId")
  default UUID householdId(FoodProductHistory foodProductHistory) {
    return foodProductHistory.getHousehold().getId();
  }

  /**
   * Maps a food product history to a food product history DTO.
   * @param foodProductHistoryDTO the food product history to map.
   * @return the mapped food product history DTO.
   */
  @Mappings(
    {
      @Mapping(target = "thrownAmountInPercentage", source = "thrownAmount"),
      @Mapping(target = "householdId", source = ".", qualifiedByName = "householdId"),
    }
  )
  FoodProductHistoryDTO foodProductHistoryToFoodProductHistoryDTO(
    FoodProductHistory foodProductHistoryDTO
  );
}
