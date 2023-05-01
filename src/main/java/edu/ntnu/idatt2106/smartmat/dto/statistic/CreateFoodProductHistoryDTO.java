package edu.ntnu.idatt2106.smartmat.dto.statistic;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO for creating food product history.
 * @author Callum G.
 * @version 1.1 28.04.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFoodProductHistoryDTO {

  @NotNull
  @NonNull
  private Double thrownAmount;
}
