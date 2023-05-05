package edu.ntnu.idatt2106.smartmat.unit.filtering;

import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2106.smartmat.filtering.FieldType;
import java.time.LocalDate;
import org.junit.Test;

public class FieldTypeTest {

  @Test
  public void testParseBoolean() {
    String value = "true";

    FieldType fieldType = FieldType.BOOLEAN;
    Object result = fieldType.parse(value);

    assertTrue(result instanceof Boolean);
  }

  @Test
  public void testParseInteger() {
    String value = "1";

    FieldType fieldType = FieldType.INTEGER;
    Object result = fieldType.parse(value);

    assertTrue(result instanceof Integer);
  }

  @Test
  public void testParseString() {
    String value = "test";

    FieldType fieldType = FieldType.STRING;
    Object result = fieldType.parse(value);

    assertTrue(result instanceof String);
  }

  @Test
  public void testParseDouble() {
    String value = "1.0";

    FieldType fieldType = FieldType.DOUBLE;
    Object result = fieldType.parse(value);

    assertTrue(result instanceof Double);
  }

  @Test
  public void testParseLong() {
    String value = "1";

    FieldType fieldType = FieldType.LONG;
    Object result = fieldType.parse(value);

    assertTrue(result instanceof Long);
  }

  @Test
  public void testParseFloat() {
    String value = "1.0";

    FieldType fieldType = FieldType.FLOAT;
    Object result = fieldType.parse(value);

    assertTrue(result instanceof Float);
  }

  @Test
  public void testParseCharacter() {
    String value = "a";

    FieldType fieldType = FieldType.CHARACTER;
    Object result = fieldType.parse(value);

    assertTrue(result instanceof Character);
  }

  @Test
  public void testParseDate() {
    String value = "2021-01-01";

    FieldType fieldType = FieldType.DATE;
    Object result = fieldType.parse(value);

    assertTrue(result instanceof LocalDate);
  }
}
