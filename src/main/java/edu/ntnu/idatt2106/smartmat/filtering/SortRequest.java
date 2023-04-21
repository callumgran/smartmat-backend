package edu.ntnu.idatt2106.smartmat.filtering;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base class for all sort requests.
 * @see https://blog.piinalpin.com/2022/04/searching-and-filtering-using-jpa-specification/
 * @author Callum G.
 * @version 1.0 - 18.3.2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortRequest implements Serializable {

  private static final long serialVersionUID = 987654321L;

  private String keyWord;

  private SortDirection sortDirection;
}
