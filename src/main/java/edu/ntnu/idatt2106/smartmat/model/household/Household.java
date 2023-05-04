package edu.ntnu.idatt2106.smartmat.model.household;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.CustomFoodItem;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.shoppinglist.ShoppingList;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
 * Class representing a household in the system.
 * A household is a collection of users that share SmartMat shopping lists
 * and food-stores.
 * @author Callum G., Carl G.
 * @version 1.3 - 28.4.2023
 */
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`household`")
public class Household {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "`household_id`", length = 16, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "`name`", length = 64, nullable = false)
  @NonNull
  private String name;

  @OneToMany(
    mappedBy = "household",
    cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }
  )
  private Set<HouseholdMember> members;

  @OneToMany(mappedBy = "household", cascade = { CascadeType.REMOVE })
  private Set<HouseholdFoodProduct> foodProducts;

  @OneToMany(
    mappedBy = "household",
    cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }
  )
  private Set<ShoppingList> shoppingLists;

  @OneToMany(mappedBy = "household")
  private Set<CustomFoodItem> customFoodItems;

  @OneToMany(mappedBy = "household")
  private Set<WeeklyRecipe> WeeklyRecipes;

  @OneToMany(mappedBy = "household")
  private Set<FoodProductHistory> foodProductHistory;
}
