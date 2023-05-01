package edu.ntnu.idatt2106.smartmat.dto.kassalapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class KassalApiDataDTO {

  private KassalApiProductsListDTO data;
}
