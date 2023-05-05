package edu.ntnu.idatt2106.smartmat.dto.kassalapp;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for Kassal api.
 * Puts the data from KassalAPIProductDTO into a list of products.
 * @author Tobias O.
 * @version 1.0 - 01.05.2023
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class KassalApiProductsListDTO {

  private Long ean;

  private List<KassalApiProductDTO> products;
}
