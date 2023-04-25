package edu.ntnu.idatt2106.smartmat.model.foodproduct;

import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import io.micrometer.common.lang.NonNull;
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
 * Class representing a custom shopping list item in the system.
 * Amount is int to choose how many times it should be scanned
 * @author Carl G.
 * @version 1.0 21.04.2020
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`custom_food_item`")
public class CustomFoodItem {

  @Id
  @Column(name = "`custom_food_item_id`", length = 16, nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "`name`", nullable = false)
  @NonNull
  private String name;

  @Column(name = "`amount`")
  private int amount;

  @Column(name = "`checked`")
  private boolean checked;

  @ManyToOne(optional = true)
  @JoinColumn(
    name = "`shopping_list`",
    nullable = true,
    referencedColumnName = "`shopping_list_id`"
  )
  private ShoppingList shoppingList;

  @ManyToOne(optional = true)
  private Household household;
}
