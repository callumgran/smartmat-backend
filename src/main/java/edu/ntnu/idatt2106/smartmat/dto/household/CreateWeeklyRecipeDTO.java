package edu.ntnu.idatt2106.smartmat.dto.household;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Class representing a weekly recipe.
 * @author Callum G.
 * @version 1.0 - 03.05.2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWeeklyRecipeDTO {

  @NotBlank
  private int portions;

  @NotBlank
  @NonNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dateToUse;
}
