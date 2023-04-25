package edu.ntnu.idatt2106.smartmat.mapper.household;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CustomFoodItemDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdMemberDTO;
import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ListingShoppingListDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.CustomFoodItemMapper;
import edu.ntnu.idatt2106.smartmat.mapper.shoppinglist.ShoppingListMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
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
 * @author Callum G.
 * @version 1.0 - 19.04.2023
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
   * @param member The household member to map.
   * @return The mapped household member DTO.
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
   * Maps a household to a household DTO.
   * @param household The household to map.
   * @return The mapped household DTO.
   */
  @Mappings(
    {
      @Mapping(target = "members", source = "members", qualifiedByName = "membersToMemberDTOs"),
      @Mapping(target = "id", source = "id", qualifiedByName = "UUIDToString"),
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
