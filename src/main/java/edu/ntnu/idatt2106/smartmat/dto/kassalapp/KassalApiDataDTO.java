package edu.ntnu.idatt2106.smartmat.dto.kassalapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for Kassal api.
 * Updates the data sent from the api.
 * @author Tobias O.
 * @version 1.0 - 01.05.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class KassalApiDataDTO {

  private KassalApiProductsListDTO data;
}
