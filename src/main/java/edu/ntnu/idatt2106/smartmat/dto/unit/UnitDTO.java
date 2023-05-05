package edu.ntnu.idatt2106.smartmat.dto.unit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for unit with name and abbreviation
 * @author Simen G. Callum G.
 * @version 1.1 28.04.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnitDTO {

  @NotNull
  @NotBlank
  private String name;

  private String abbreviation;

  private double toNormalFormConversionFactor;
}
