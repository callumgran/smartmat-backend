package edu.ntnu.idatt2106.smartmat.model.foodproduct;

import edu.ntnu.idatt2106.smartmat.model.household.Household;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class representing a product in the household
 * It is possible to have several products of the same
 * type in the household that have different amounts left or expiration dates.
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
@Table(name = "`household_food_product`")
public class HouseholdFoodProduct {

  @Id
  @Column(name = "`household_food_product_id`", length = 16, nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "`food_product`", nullable = false, referencedColumnName = "`id`")
  @NonNull
  private FoodProduct foodProduct;

  @ManyToOne(optional = false)
  @JoinColumn(name = "`household_id`", nullable = false, referencedColumnName = "`household_id`")
  @NonNull
  private Household household;

  @Column(name = "`expiration_date")
  private LocalDate expirationDate;

  @Column(name = "`amount_left`")
  private double amountLeft;
}
