package edu.ntnu.idatt2106.smartmat.controller;

import edu.ntnu.idatt2106.smartmat.dto.user.UserDTO;
import edu.ntnu.idatt2106.smartmat.dto.user.UserPatchDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for private user endpoints.
 * Based on the PrivateUserController from the IDATT2105 project.
 * @author Thomas S, Callum Gran, Nicolai H. B.
 * @version 1.0 - 17.04.2023
 */
@RestController
@RequestMapping(value = "/api/v1/private/users")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class PrivateUserController {

  private static final Logger LOGGER = LoggerFactory.getLogger(PrivateUserController.class);

  private final UserService userService;

  /**
   * Get the authenticated user.
   * @param username The username of the authenticated user.
   * @return The authenticated user.
   * @throws PermissionDeniedException If the auth is null.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Get the authenticated user",
    description = "Get the authenticated user",
    tags = { "user" }
  )
  public ResponseEntity<UserDTO> getUser(@AuthenticationPrincipal Auth auth)
    throws UserDoesNotExistsException {
    LOGGER.info("GET request for user: {}", auth.getUsername());

    UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(
      userService.getUserByUsername(auth.getUsername())
    );

    LOGGER.info("Mapped to UserDTO: {}", userDTO);

    return ResponseEntity.ok(userDTO);
  }

  /**
   * Update the user with the given username.
   * @param auth The authentication object of the user trying to update the user.
   * @param username The username of the user to update.
   * @param userUpdateDTO The user update data transfer object.
   * @return The updated user.
   * @throws PermissionDeniedException If the user is not authorized to update the user.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws BadCredentialsException If the old password is incorrect.
   * @throws BadInputException If the input is invalid.
   */
  @PatchMapping(
    value = "/{username}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Update the user with the given username",
    description = "Update the user with the given username",
    tags = { "user" }
  )
  public ResponseEntity<UserDTO> updateUser(
    @AuthenticationPrincipal Auth auth,
    @PathVariable String username,
    @RequestBody UserPatchDTO userUpdateDTO
  ) throws UserDoesNotExistsException, BadCredentialsException {
    LOGGER.info("PATCH request for user: {}", username);

    User userToUpdate = userService.getUserByUsername(username);

    User updatedUser = userService.partialUpdate(
      userToUpdate,
      userUpdateDTO.getEmail(),
      userUpdateDTO.getFirstName(),
      userUpdateDTO.getLastName(),
      userUpdateDTO.getOldPassword(),
      userUpdateDTO.getNewPassword()
    );
    UserDTO updatedUserDTO = UserMapper.INSTANCE.userToUserDTO(updatedUser);
    LOGGER.info("Mapped to UserDTO: {}", updatedUserDTO);
    return ResponseEntity.ok(updatedUserDTO);
  }
}
