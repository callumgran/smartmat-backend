package edu.ntnu.idatt2106.smartmat.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Class representing a recipe recommendation DTO.
 * It contains a recipe and a recommendation score.
 * @author Callum G.
 * @version 1.0 - 01.05.2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRecommendationDTO {

  @NotBlank
  @NonNull
  private RecipeDTO recipe;

  @NotBlank
  private double score;
}
