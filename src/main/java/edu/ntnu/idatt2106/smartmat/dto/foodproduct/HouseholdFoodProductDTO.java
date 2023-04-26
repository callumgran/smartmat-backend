package edu.ntnu.idatt2106.smartmat.dto.foodproduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for household food product.
 * Used to transfer household food product data between layers.
 * @author Callum G, Nicolai H. Brand.
 * @version 1.0 - 24.04.2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HouseholdFoodProductDTO {

  @NonNull
  private String id;

  @NonNull
  private FoodProductDTO foodProduct;

  @NonNull
  @NotBlank
  private String householdId;

  @NonNull
  @NotBlank
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate expirationDate;

  private double amountLeft;
}
