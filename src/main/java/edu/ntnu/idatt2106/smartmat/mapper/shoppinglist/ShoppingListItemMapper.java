package edu.ntnu.idatt2106.smartmat.mapper.shoppinglist;

import edu.ntnu.idatt2106.smartmat.dto.shoppinglist.ShoppingListItemDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.shoppinglist.ShoppingListNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingListItem;
import edu.ntnu.idatt2106.smartmat.service.shoppinglist.ShoppingListService;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for shopping list items.
 * @author Carl G.
 * @version 1.0 - 24.04.2023.
 */
@Mapper(componentModel = "spring")
public abstract class ShoppingListItemMapper {

  @Autowired
  ShoppingListService shoppingListService;

  public static ShoppingListItemMapper INSTANCE = Mappers.getMapper(ShoppingListItemMapper.class);

  /**
   * Maps a UUID to a shopping list.
   * @param id the id for the shopping list.
   * @return the shopping list.
   * @throws ShoppingListNotFoundException if the shopping list is not found.
   */
  @Named("UUIDtoShoppingList")
  public ShoppingList UUIDToShoppingList(UUID id) throws ShoppingListNotFoundException {
    return shoppingListService.getShoppingListById(id);
  }

  /**
   * Maps a shopping list to a UUID.
   * @param shoppingList the shopping list.
   * @return the id for the shopping list.
   */
  @Named("shoppingListToUUID")
  public UUID shoppingListToUUID(ShoppingList shoppingList) {
    return shoppingList.getId();
  }

  @Mappings(
    {
      @Mapping(
        target = "shoppingList",
        source = "shoppingList",
        qualifiedByName = "shoppingListToUUID"
      ),
    }
  )
  public abstract ShoppingListItemDTO shoppingListItemsToShoppingListItemDTO(
    ShoppingListItem shoppingListItem
  );

  @Mappings(
    {
      @Mapping(
        target = "shoppingList",
        source = "shoppingList",
        qualifiedByName = "UUIDtoShoppingList"
      ),
    }
  )
  public abstract ShoppingListItem shoppingListItemDTOToShoppingListItems(
    ShoppingListItemDTO shoppingListItemDTO
  );
}
