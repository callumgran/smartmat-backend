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
 * Data transfer object for temperary ingredient used.
 * Used to transfer data between layers.
 * @author Callum G.
 * @version 1.0 - 28.04.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TmpIngredientUsedDTO {

  @NonNull
  @NotBlank
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;
}
