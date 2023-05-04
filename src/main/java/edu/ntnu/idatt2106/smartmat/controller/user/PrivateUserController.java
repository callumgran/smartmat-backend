package edu.ntnu.idatt2106.smartmat.controller.user;

import edu.ntnu.idatt2106.smartmat.dto.user.UserDTO;
import edu.ntnu.idatt2106.smartmat.dto.user.UserPatchDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.PermissionDeniedException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.WrongPasswordException;
import edu.ntnu.idatt2106.smartmat.exceptions.validation.BadInputException;
import edu.ntnu.idatt2106.smartmat.mapper.user.UserMapper;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import edu.ntnu.idatt2106.smartmat.validation.user.AuthValidation;
import edu.ntnu.idatt2106.smartmat.validation.user.UserValidation;
import io.micrometer.common.lang.NonNull;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<UserDTO> getUser(@NonNull @AuthenticationPrincipal Auth user)
    throws PermissionDeniedException, UserDoesNotExistsException {
    if (!AuthValidation.validateAuth(user)) throw new PermissionDeniedException(
      "Brukeren er ikke autentisert."
    );
    LOGGER.info("GET request for user: {}", user.getUsername());

    UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(
      userService.getUserByUsername(user.getUsername())
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
   * @throws WrongPasswordException If the old password is incorrect.
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
    @NonNull @AuthenticationPrincipal Auth auth,
    @NonNull @PathVariable String username,
    @NonNull @RequestBody UserPatchDTO userUpdateDTO
  )
    throws PermissionDeniedException, UserDoesNotExistsException, WrongPasswordException, BadInputException {
    if (
      !AuthValidation.hasRoleOrIsUser(auth, UserRole.ADMIN, username)
    ) throw new PermissionDeniedException("Brukeren har ikke tilgang til å oppdatere brukeren.");

    if (
      !UserValidation.validatePartialUserUpdate(
        userUpdateDTO.getEmail(),
        userUpdateDTO.getFirstName(),
        userUpdateDTO.getLastName(),
        userUpdateDTO.getOldPassword(),
        userUpdateDTO.getNewPassword()
      )
    ) throw new BadInputException("Dårlig input, sjekk over feltene.");

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
