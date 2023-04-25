package edu.ntnu.idatt2106.smartmat.model.shoppinglist;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class to represent a shopping list.
 * A household can have several shopping lists.
 * Separates shopping list items and custom food items
 * to enable users to add items which are not implemented
 * as an ingredient.
 * @author Tobias O., Carl G.
 * @version 1.0 21.04.2023.
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`shopping_list`")
public class ShoppingList {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "`shopping_list_id`", length = 16, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "`date_completed`", length = 64, nullable = true)
  private LocalDate dateCompleted;

  @ManyToOne(optional = false)
  private Household household;

  @OneToMany(mappedBy = "shoppingList")
  private Set<ShoppingListItem> shoppingListItems;

  @OneToMany(mappedBy = "shoppingList")
  private Set<CustomFoodItem> customFoodItems;
}
