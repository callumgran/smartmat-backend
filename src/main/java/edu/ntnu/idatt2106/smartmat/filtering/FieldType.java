package edu.ntnu.idatt2106.smartmat.filtering;

import java.lang.Object;
import java.time.LocalDate;

/**
 * Enum for filtering operators.
 * Based on the field type enum from the blog post below and IDATT2105 project.
 * @see https://blog.piinalpin.com/2022/04/searching-and-filtering-using-jpa-specification/
 * @author Callum G.
 * @version 1.0 - 21.04.2023
 */
public enum FieldType {
  BOOLEAN {
    public Object parse(String value) {
      return Boolean.parseBoolean(value);
    }
  },
  STRING {
    public Object parse(String value) {
      return value;
    }
  },
  INTEGER {
    public Object parse(String value) {
      return Integer.parseInt(value);
    }
  },
  LONG {
    public Object parse(String value) {
      return Long.parseLong(value);
    }
  },
  DOUBLE {
    public Object parse(String value) {
      return Double.parseDouble(value);
    }
  },
  FLOAT {
    public Object parse(String value) {
      return Float.parseFloat(value);
    }
  },
  CHARACTER {
    public Object parse(String value) {
      return value.charAt(0);
    }
  },
  DATE {
    public Object parse(String value) {
      return LocalDate.parse(value);
    }
  };

  public abstract Object parse(String value);
}
