package edu.ntnu.idatt2106.smartmat.model.shoppinglist;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class to represent items from a shopping list,
 * that have been scanned and not yet added to the
 * household inventory.
 *
 * @author Callum G.
 * @version 1.0 02.05.2023
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`basket`")
public class Basket {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "`basket_id`", length = 16, nullable = false, updatable = false)
  private UUID id;

  @OneToOne(optional = false)
  @PrimaryKeyJoinColumn
  private ShoppingList shoppingList;

  @OneToMany(
    mappedBy = "basket",
    cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }
  )
  private List<BasketItem> basketItems;

  @OneToMany(mappedBy = "basket")
  private Set<CustomFoodItem> customFoodItems;
}
