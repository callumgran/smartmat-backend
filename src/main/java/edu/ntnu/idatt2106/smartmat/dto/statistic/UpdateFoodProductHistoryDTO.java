package edu.ntnu.idatt2106.smartmat.dto.statistic;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO for updating food product history with new values
 * @author Callum G.
 * @version 1.1 28.04.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateFoodProductHistoryDTO {

  @NotNull
  @NonNull
  private Long foodProductId;

  @NotNull
  @NonNull
  private UUID householdId;

  @NotNull
  @NonNull
  private Double thrownAmount;

  @NotNull
  @NonNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;
}
