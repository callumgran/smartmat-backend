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
 * Class representing an ingredientDTO for food product.
 * @author Callum G., Carl G.
 * @version 1.1 - 25.04.2023
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
}
