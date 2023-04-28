package edu.ntnu.idatt2106.smartmat.model.household;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing the primary key of a WeeklyRecipe in a household.
 * @author Callum G.
 * @version 1.1 - 28.4.2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WeeklyRecipeId implements Serializable {

  private Household household;

  private LocalDate dateToUse;
}
