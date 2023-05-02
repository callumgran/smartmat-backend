package edu.ntnu.idatt2106.smartmat.model.statistic;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import io.micrometer.common.lang.NonNull;
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
import lombok.Setter;

/**
 * Class representing a product that has been used in a household
 * Used for statistics and history of a household product
 *
 * @author Callum G.
 * @version 1.0 - 28.04.2023
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`food_product_history`")
public class FoodProductHistory {

  @Id
  @Column(name = "`food_product_history_id`", length = 16, nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "`food_product`", nullable = false, referencedColumnName = "`id`")
  private FoodProduct foodProduct;

  @ManyToOne(optional = false)
  @JoinColumn(name = "`household`", nullable = false, referencedColumnName = "`household_id`")
  private Household household;

  @Column(name = "`thrown_amount`")
  private double thrownAmount;

  @Column(name = "`date`")
  @NonNull
  private LocalDate date;

  @Column(name = "`amount`")
  private double amount;
}
