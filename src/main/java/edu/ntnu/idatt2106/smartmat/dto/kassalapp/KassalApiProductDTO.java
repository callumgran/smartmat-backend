package edu.ntnu.idatt2106.smartmat.dto.kassalapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class KassalApiProductDTO {

  private String name;

  private String vendor;

  private String brand;

  private String description;

  private String image;
}
