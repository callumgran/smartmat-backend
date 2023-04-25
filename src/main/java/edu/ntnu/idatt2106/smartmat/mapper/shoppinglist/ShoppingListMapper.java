package edu.ntnu.idatt2106.smartmat.mapper.shoppinglist;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CustomFoodItemDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ListingShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ShoppingListItemDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.CustomFoodItemMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for shopping list.
 * @author Carl G.
 * @version 1.1 - 24.04.2023
 */

@Mapper(componentModel = "spring")
public abstract class ShoppingListMapper {

  public static ShoppingListMapper INSTANCE = Mappers.getMapper(ShoppingListMapper.class);

  @Autowired
  HouseholdService householdService;

  ShoppingListItemMapper shoppingListItemMapper = ShoppingListItemMapper.INSTANCE;
  CustomFoodItemMapper customFoodItemMapper = CustomFoodItemMapper.INSTANCE;

  /**
   * Maps a UUID to a household.
   * @param id the id for the household.
   * @return the household.
   * @throws HouseholdNotFoundException if the household is not found.
   */
  @Named("UUIDToHousehold")
  public Household UUIDStringToHousehold(UUID id) throws HouseholdNotFoundException {
    return householdService.getHouseholdById(id);
  }

  /**
   * Maps a household to a UUID.
   * @param household the household.
   * @return the household.
   */
  @Named("householdToUUID")
  public UUID householdToUUIDStringString(Household household) {
    return household.getId();
  }

  /**
   *
   * @param shoppingListItems
   * @return
   */
  public Set<ShoppingListItemDTO> shoppingListItemsToShoppingListItemDTOs(
    Set<ShoppingListItem> shoppingListItems
  ) {
    if (shoppingListItems == null) {
      return new HashSet<>();
    }
    return shoppingListItems
      .stream()
      .map(shoppingListItemMapper::shoppingListItemsToShoppingListItemDTO)
      .collect(Collectors.toSet());
  }

  public Set<ShoppingListItem> shoppingListItemDTOsToShoppingListItems(
    Set<ShoppingListItemDTO> shoppingListItemDTOs
  ) {
    if (shoppingListItemDTOs == null) {
      return new HashSet<>();
    }
    return shoppingListItemDTOs
      .stream()
      .map(shoppingListItemMapper::shoppingListItemDTOToShoppingListItems)
      .collect(Collectors.toSet());
  }

  public Set<CustomFoodItemDTO> customFoodItemsToCustomFoodItemDTOs(
    Set<CustomFoodItem> shoppingListItems
  ) {
    if (shoppingListItems == null) {
      return new HashSet<>();
    }
    return shoppingListItems
      .stream()
      .map(customFoodItemMapper::customFoodItemsToCustomFoodItemDTO)
      .collect(Collectors.toSet());
  }

  public Set<CustomFoodItem> customFoodItemDTOsToCustomFoodItems(
    Set<CustomFoodItemDTO> customFoodItemListDTOs
  ) {
    if (customFoodItemListDTOs == null) {
      return new HashSet<>();
    }
    return customFoodItemListDTOs
      .stream()
      .map(customFoodItemMapper::customFoodItemDTOToCustomFoodItems)
      .collect(Collectors.toSet());
  }

  @Mappings(
    {
      @Mapping(target = "household", source = "household", qualifiedByName = "householdToUUID"),
      @Mapping(
        target = "shoppingListItems",
        source = "shoppingListItems",
        qualifiedByName = "shoppingListItemsToShoppingListItemDTOs"
      ),
      @Mapping(
        target = "customFoodItems",
        source = "customFoodItems",
        qualifiedByName = "customFoodItemsToCustomFoodItemDTOs"
      ),
    }
  )
  public abstract ShoppingListDTO shoppingListToDTO(ShoppingList shoppingList);

  @Mappings(
    {
      @Mapping(target = "household", source = "household", qualifiedByName = "UUIDToHousehold"),
      @Mapping(
        target = "shoppingListItems",
        source = "shoppingListItems",
        qualifiedByName = "shoppingListItemDTOsToShoppingListItems"
      ),
      @Mapping(
        target = "customFoodItems",
        source = "customFoodItems",
        qualifiedByName = "customFoodItemDTOsToCustomFoodItems"
      ),
    }
  )
  public abstract ShoppingList shoppingListDTOToShoppingList(ShoppingListDTO shoppingListDTO);

  public abstract ListingShoppingListDTO shoppingListToListingShoppingListDTO(
    ShoppingList shoppingList
  );

  @Mappings(
    {
      @Mapping(target = "household", ignore = true),
      @Mapping(target = "shoppingListItems", ignore = true),
      @Mapping(target = "customFoodItems", ignore = true),
    }
  )
  public abstract ShoppingList listingShoppingListDTOToShoppingList(
    ListingShoppingListDTO shoppingListDTO
  );
}
