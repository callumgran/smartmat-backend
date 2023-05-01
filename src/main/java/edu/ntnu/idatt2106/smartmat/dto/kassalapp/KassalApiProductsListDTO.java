package edu.ntnu.idatt2106.smartmat.dto.kassalapp;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class KassalApiProductsListDTO {

  private Long ean;

  private List<KassalApiProductDTO> products;
}
