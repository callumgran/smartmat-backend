package edu.ntnu.idatt2106.smartmat.dto.foodproduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data transfer object for household food product.
 * Used to create food product data between layers.
 * @author Callum G
 * @version 1.0 - 25.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateHouseholdFoodProductDTO {

  @NonNull
  private Long foodProductId;

  @NonNull
  @NotBlank
  @JsonFormat(pattern = "yyyy-MM-dd")
  private String expirationDate;

  private double amountLeft;
}
