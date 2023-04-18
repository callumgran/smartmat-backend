package edu.ntnu.idatt2106.smartmat.exceptions;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * A response object for exceptions
 * that are not handled.
 * Detail is a message about why the exception occured.
 *
 * @author Carl. G
 * @version 1.0 - 18.04.2023
 */
@Data
public class ExceptionResponse {

  @NotBlank
  private final String detail;
}
