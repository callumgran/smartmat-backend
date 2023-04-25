package edu.ntnu.idatt2106.smartmat.model.shoppinglist;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class representing a shopping list item in the system.
 * The item relates to an ingredient and a shopping list.
 * Not possible to add the same ingredient to the shopping
 * list. Should instead be possible update the amount.
 * @author Carl G.
 * @version 1.0 21.04.2020
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`shopping_list_item`")
public class ShoppingListItem {

  @Id
  @Column(name = "`shopping_list_item_id`", length = 16, nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "`amount`")
  private double amount;

  @Column(name = "`checked`")
  private boolean checked;

  @ManyToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  @JoinColumn(
    name = "`shopping_list`",
    nullable = false,
    referencedColumnName = "`shopping_list_id`"
  )
  private ShoppingList shoppingList;

  @ManyToOne(optional = true)
  @JoinColumn(name = "`ingredient`", nullable = true, referencedColumnName = "`ingredient_id`")
  private Ingredient ingredient;
}
