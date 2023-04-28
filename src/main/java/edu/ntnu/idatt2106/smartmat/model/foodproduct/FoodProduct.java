package edu.ntnu.idatt2106.smartmat.model.foodproduct;

import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class representing a product which can be added to a household
 * It is possible to have several products of the same name.
 * If a product has NULL amount, the amount was not found.
 * If a product is looseWeight, the user have to define
 * the amount of the household product.
 *
 * @author Carl G.
 * @version 1.0 - 20.04.2023
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`food_product`")
public class FoodProduct {

  @Id
  @Column(name = "`id`")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "`name`", nullable = false, length = 64)
  @NonNull
  private String name;

  @Column(name = "`EAN`", unique = true, length = 13, nullable = true)
  private String EAN;

  @Column(name = "`amount`")
  private Double amount;

  @Column(name = "`loose_weight`")
  private boolean looseWeight;

  @OneToMany(mappedBy = "foodProduct")
  private Set<HouseholdFoodProduct> households;

  @ManyToOne(optional = true)
  @JoinColumn(name = "`ingredient`", nullable = true, referencedColumnName = "`ingredient_id`")
  private Ingredient ingredient;

  @OneToMany(mappedBy = "foodProduct")
  private Set<FoodProductHistory> foodProductHistories;
}
