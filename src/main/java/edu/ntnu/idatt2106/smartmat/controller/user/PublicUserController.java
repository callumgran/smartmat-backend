package edu.ntnu.idatt2106.smartmat.controller.user;

import edu.ntnu.idatt2106.smartmat.dto.user.RegisterDTO;
import edu.ntnu.idatt2106.smartmat.dto.user.UserDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.DatabaseException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.EmailAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UsernameAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.validation.BadInputException;
import edu.ntnu.idatt2106.smartmat.mapper.user.RegisterMapper;
import edu.ntnu.idatt2106.smartmat.mapper.user.UserMapper;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import edu.ntnu.idatt2106.smartmat.validation.user.UserValidation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for public user endpoints.
 * Based on the PublicUserController from the IDATT2105 project.
 * @author Thomas S, Callum Gran
 * @version 1.1 - 05.05.2023
 */
@RestController
@RequestMapping(value = "/api/v1/public/users")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class PublicUserController {

  private final UserService userService;

  private static final Logger LOGGER = LoggerFactory.getLogger(PublicUserController.class);

  /**
   * Get a user by username.
   * @param username The username of the user.
   * @return The user.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Get a user by username",
    description = "Get a user by username",
    tags = { "user" }
  )
  public ResponseEntity<UserDTO> getUser(@PathVariable String username)
    throws UserDoesNotExistsException {
    if (!UserValidation.validateUsername(username)) throw new UserDoesNotExistsException(
      "Bruker finnes ikke."
    );

    LOGGER.info("GET request for user: {}", username);
    User user = userService.getUserByUsername(username);

    UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);

    LOGGER.info("Mapped user to userDTO: {}", userDTO);

    return ResponseEntity.ok(userDTO);
  }

  /**
   * Create a new user.
   * @param registerUser The user to create.
   * @return The created user and a 201 status code.
   * @throws BadInputException If the input is invalid.
   * @throws UsernameAlreadyExistsException If the username already exists.
   * @throws EmailAlreadyExistsException If the email already exists.
   * @throws DatabaseException If the database fails.
   */
  @PostMapping(
    value = "",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(summary = "Create a new user", description = "Create a new user", tags = { "user" })
  public ResponseEntity<String> createUser(@RequestBody RegisterDTO registerUser)
    throws BadInputException, UsernameAlreadyExistsException, EmailAlreadyExistsException, DatabaseException {
    if (
      !UserValidation.validateRegistrationForm(
        registerUser.getUsername(),
        registerUser.getEmail(),
        registerUser.getFirstName(),
        registerUser.getLastName(),
        registerUser.getPassword()
      )
    ) throw new BadInputException("Ugyldig input. Se over feltene og prøv igjen.");
    LOGGER.info("POST request for user: {}", registerUser);

    User user = RegisterMapper.INSTANCE.registerDTOtoUser(registerUser);

    LOGGER.info("Mapped registerDTO to user: {}", user.toString());

    user = userService.saveUser(user);

    LOGGER.info("User saved: {}", user.toString());

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
