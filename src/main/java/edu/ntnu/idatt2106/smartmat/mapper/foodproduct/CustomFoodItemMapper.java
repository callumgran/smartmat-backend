package edu.ntnu.idatt2106.smartmat.mapper.foodproduct;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CustomFoodItemDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for custom food and shopping list.
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */

@Mapper(componentModel = "spring")
public abstract class CustomFoodItemMapper {

  @Autowired
  ShoppingListService shoppingListService;

  @Autowired
  HouseholdService householdService;

  public static CustomFoodItemMapper INSTANCE = Mappers.getMapper(CustomFoodItemMapper.class);

  /**
   * Maps a UUID to a shopping list.
   * @param id the id for the shopping list.
   * @return the shopping list.
   * @throws ShoppingListNotFoundException if the shopping list does not exist.
   */
  @Named("UUIDtoShoppingList")
  public ShoppingList UUIDToShoppingList(UUID id) throws ShoppingListNotFoundException {
    return id == null ? null : shoppingListService.getShoppingListById(id);
  }

  /**
   * Maps a shopping list to a UUID.
   * @param shoppingList the shopping list.
   * @return the shopping list by id.
   */
  @Named("shoppingListToUUID")
  public UUID shoppingListToUUID(ShoppingList shoppingList) {
    return shoppingList == null ? null : shoppingList.getId();
  }

  /**
   * Maps a UUID to a household.
   * @param id the household id.
   * @return the household.
   * @throws HouseholdNotFoundException if the household does not exist.
   */
  @Named("UUIDtoHousehold")
  public Household UUIDToHousehold(UUID id) throws HouseholdNotFoundException {
    return id == null ? null : householdService.getHouseholdById(id);
  }

  /**
   * Maps a household to a UUID.
   * @param household the household.
   * @return the household.
   */
  @Named("householdToUUID")
  public UUID householdToUUID(Household household) {
    return household == null ? null : household.getId();
  }

  /**
   * Maps custom food items to a custom food items DTO.
   * @param customFoodItem the custom food item.
   * @return the custom food item.
   */
  @Mappings(
    {
      @Mapping(
        target = "shoppingList",
        source = "shoppingList",
        qualifiedByName = "shoppingListToUUID"
      ),
      @Mapping(target = "household", source = "household", qualifiedByName = "householdToUUID"),
    }
  )
  public abstract CustomFoodItemDTO customFoodItemsToCustomFoodItemDTO(
    CustomFoodItem customFoodItem
  );

  /**
   * Maps custom food items DTO to custom food items.
   * @param customFoodItemDTO the custom food DTO.
   * @return the custom food item.
   */
  @Mappings(
    {
      @Mapping(
        target = "shoppingList",
        source = "shoppingList",
        qualifiedByName = "UUIDtoShoppingList"
      ),
      @Mapping(target = "household", source = "household", qualifiedByName = "UUIDtoHousehold"),
    }
  )
  public abstract CustomFoodItem customFoodItemDTOToCustomFoodItems(
    CustomFoodItemDTO customFoodItemDTO
  );
}
