package edu.ntnu.idatt2106.smartmat.model.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that represents a recipe recommendation.
 * It contains a recipe and a recommendation score.
 * @author Callum G.
 * @version 1.0 01.05.2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeRecommendation {

  private Recipe recipe;

  private double score;
}
