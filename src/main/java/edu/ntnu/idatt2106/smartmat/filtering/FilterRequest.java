package edu.ntnu.idatt2106.smartmat.filtering;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base class for all filter requests.
 * Based on the filter request class from the blog post below and IDATT2105 project.
 * @see <a href="https://blog.piinalpin.com/2022/04/searching-and-filtering-using-jpa-specification/">Piinalpin</a>
 * @author Callum G.
 * @version 1.0 - 21.04.2023
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FilterRequest implements Serializable {

  private static final long serialVersionUID = 123456789L;

  private String keyWord;

  private Operator operator;

  private FieldType fieldType;

  private transient Object value;

  private transient Object valueTo;

  private transient List<Object> values;
}
