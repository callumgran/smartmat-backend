package edu.ntnu.idatt2106.smartmat.mapper.household;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CustomFoodItemDTO;
import edu.ntnu.idatt2106.smartmat.dto.foodproduct.HouseholdFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdMemberDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ListingShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.CustomFoodItemMapper;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.HouseholdFoodProductMapper;
import edu.ntnu.idatt2106.smartmat.mapper.shoppinglist.ShoppingListMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.util.function.ThrowingFunction;

/**
 * Class used to map between User and RegisterDTO.
 * @author Callum G, Thomas S.
 * @version 1.1 - 24.04.2023
 */
@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class HouseholdMapper {

  public static HouseholdMapper INSTANCE = Mappers.getMapper(HouseholdMapper.class);

  private HouseholdMemberMapper householdMemberMapper = HouseholdMemberMapper.INSTANCE;
  private CustomFoodItemMapper customFoodItemMapper = CustomFoodItemMapper.INSTANCE;
  private ShoppingListMapper shoppingListMapper = ShoppingListMapper.INSTANCE;

  /**
   * Maps a household member to a household member DTO.
   * @param members The household member to map.
   * @return The mapped household member DTO.
   */
  @Named("membersToMemberDTOs")
  public Set<HouseholdMemberDTO> memberToMemberDTO(Set<HouseholdMember> members) {
    if (members == null) {
      return new HashSet<>();
    }
    return members
      .stream()
      .map(householdMemberMapper::householdMemberToHouseholdMemberDTO)
      .collect(Collectors.toSet());
  }

  /**
   * Maps a household member to a household member DTO.
   * @param memberDTOs The household member to map.
   * @return The mapped household member DTO.
   * @throws HouseholdNotFoundException If the household is not found.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @Named("memberDTOsToMembers")
  public Set<HouseholdMember> memberDTOToMember(Set<HouseholdMemberDTO> memberDTOs)
    throws HouseholdNotFoundException, UserDoesNotExistsException {
    if (memberDTOs == null) {
      return new HashSet<>();
    }
    ThrowingFunction<HouseholdMemberDTO, HouseholdMember> function =
      householdMemberMapper::householdMemberDTOToHouseholdMember;
    return memberDTOs.stream().map(function).collect(Collectors.toSet());
  }

  /**
   * Maps a string to a UUID.
   * @param id The string to map.
   * @return The mapped UUID.
   */
  @Named("stringToUUID")
  public UUID stringToUUID(String id) {
    return UUID.fromString(id);
  }

  /**
   * Maps a UUID to a string.
   * @param id The UUID to map.
   * @return The mapped string.
   */
  @Named("UUIDToString")
  public String UUIDToString(UUID id) {
    return id.toString();
  }

  @Named("customFoodItemsToCustomFoodItemDTOs")
  public Set<CustomFoodItemDTO> customFoodItemsToCustomFoodItemDTOs(
    Set<CustomFoodItem> customFoodItems
  ) {
    if (customFoodItems == null) {
      return new HashSet<>();
    }
    return customFoodItems
      .stream()
      .map(customFoodItemMapper::customFoodItemsToCustomFoodItemDTO)
      .collect(Collectors.toSet());
  }

  /**
   * Maps a list of custom food item DTOs to a list of custom food items.
   * @param customFoodItemDTOs the custom food item DTOs to map.
   * @return the mapped custom food items.
   */
  @Named("customFoddItemDTOsToCustomgFoodItems")
  public Set<CustomFoodItem> customFoddItemDTOsToCustomgFoodItems(
    Set<CustomFoodItemDTO> customFoodItemDTOs
  ) {
    if (customFoodItemDTOs == null) {
      return new HashSet<>();
    }
    return customFoodItemDTOs
      .stream()
      .map(customFoodItemMapper::customFoodItemDTOToCustomFoodItems)
      .collect(Collectors.toSet());
  }

  /**
   * Maps a list of shopping lists to a list of shopping list DTOs.
   * @param shoppingLists the shopping lists to map.
   * @return the mapped shopping lists.
   */
  @Named("shoppingListsToShoppingListDTOs")
  public Set<ListingShoppingListDTO> shoppingListsToShoppingListDTOs(
    Set<ShoppingList> shoppingLists
  ) {
    if (shoppingLists == null) {
      return new HashSet<>();
    }
    return shoppingLists
      .stream()
      .map(shoppingListMapper::shoppingListToListingShoppingListDTO)
      .collect(Collectors.toSet());
  }

  /**
   * Maps a list of shopping list DTOs to a list of shopping lists.
   * @param shoppingListDTOs the shopping list DTOs to map.
   * @return the mapped shopping list DTOs.
   */
  @Named("shoppingListDTOsToShoppingLists")
  public Set<ShoppingList> shoppingListDTOsToShoppingLists(
    Set<ListingShoppingListDTO> shoppingListDTOs
  ) {
    if (shoppingListDTOs == null) {
      return new HashSet<>();
    }
    return shoppingListDTOs
      .stream()
      .map(shoppingListMapper::listingShoppingListDTOToShoppingList)
      .collect(Collectors.toSet());
  }

  /**
   * Maps a list of food product DTOs to a list of food products.
   * @param householdFoodProductDTOs The food product DTOs to map.
   * @return The mapped food products.
   */
  @Named("foodProductDTOsToFoodProducts")
  public Set<HouseholdFoodProduct> foodProductDTOsToFoodProducts(
    Set<HouseholdFoodProductDTO> householdFoodProductDTOs
  ) {
    if (householdFoodProductDTOs == null) {
      return new HashSet<>();
    }
    return householdFoodProductDTOs
      .stream()
      .map(HouseholdFoodProductMapper.INSTANCE::householdFoodProductDTOToHouseholdFoodProduct)
      .collect(Collectors.toSet());
  }

  /**
   * Maps a list of food products to a list of food product DTOs.
   * @param foodProducts The food products to map.
   * @return The mapped food product DTOs.
   */
  @Named("foodProductsToFoodProductDTOs")
  public Set<HouseholdFoodProductDTO> foodProductsToFoodProductDTOs(
    Set<HouseholdFoodProduct> foodProducts
  ) {
    if (foodProducts == null) {
      return new HashSet<>();
    }
    return foodProducts
      .stream()
      .map(HouseholdFoodProductMapper.INSTANCE::householdFoodProductToHouseholdFoodProductDTO)
      .collect(Collectors.toSet());
  }

  /**
   * Maps a household to a household DTO.
   * @param household The household to map.
   * @return The mapped household DTO.
   */
  @Mappings(
    {
      @Mapping(target = "members", source = "members", qualifiedByName = "membersToMemberDTOs"),
      @Mapping(target = "id", source = "id", qualifiedByName = "UUIDToString"),
      @Mapping(
        target = "foodProducts",
        source = "foodProducts",
        qualifiedByName = "foodProductsToFoodProductDTOs"
      ),
      @Mapping(
        target = "shoppingLists",
        source = "shoppingLists",
        qualifiedByName = "shoppingListsToShoppingListDTOs"
      ),
      @Mapping(
        target = "customFoodItems",
        source = "customFoodItems",
        qualifiedByName = "customFoodItemsToCustomFoodItemDTOs"
      ),
    }
  )
  public abstract HouseholdDTO householdToHouseholdDTO(Household household);

  /**
   * Maps a household DTO to a household.
   * @param householdDTO The household DTO to map.
   * @return The mapped household.
   */
  @Mappings(
    {
      @Mapping(target = "members", source = "members", qualifiedByName = "memberDTOsToMembers"),
      @Mapping(target = "id", source = "id", qualifiedByName = "stringToUUID"),
      @Mapping(
        target = "foodProducts",
        source = "foodProducts",
        qualifiedByName = "foodProductDTOsToFoodProducts"
      ),
      @Mapping(
        target = "shoppingLists",
        source = "shoppingLists",
        qualifiedByName = "shoppingListsDTOsToShoppingLists"
      ),
      @Mapping(
        target = "customFoodItems",
        source = "customFoodItems",
        qualifiedByName = "customFoddItemDTOsToCustomgFoodItems"
      ),
    }
  )
  public abstract Household householdDTOToHousehold(HouseholdDTO householdDTO)
    throws HouseholdNotFoundException, UserDoesNotExistsException;
}
