package edu.ntnu.idatt2106.smartmat.dto.household;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.ntnu.idatt2106.smartmat.dto.recipe.RecipeDTO;
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
 * @version 1.0 - 01.05.2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyRecipeDTO {

  @NotBlank
  @NonNull
  private RecipeDTO recipe;

  @NotBlank
  @NonNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dateToUse;

  @NotBlank
  private boolean used;

  @NotBlank
  private int portions;
}
