package edu.ntnu.idatt2106.smartmat.model.shoppinglist;

import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
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
 * Class to represent an item in a basket
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
@Table(name = "`basket_item`")
public class BasketItem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "`basket_item_id`", length = 16, nullable = false, updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "`food_product_id`", nullable = true, referencedColumnName = "`id`")
  private FoodProduct foodProduct;

  @Column(name = "`amount`")
  private double amount;

  @ManyToOne(optional = false)
  @JoinColumn(name = "`basket_id`", nullable = false, referencedColumnName = "`basket_id`")
  private Basket basket;
}
