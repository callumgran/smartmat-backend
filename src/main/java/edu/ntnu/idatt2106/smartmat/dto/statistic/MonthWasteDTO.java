package edu.ntnu.idatt2106.smartmat.dto.statistic;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating food product history with new values
 * @author Callum G.
 * @version 1.1 28.04.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthWasteDTO {

  @NotNull
  private int month;

  @NotNull
  private double waste;
}
