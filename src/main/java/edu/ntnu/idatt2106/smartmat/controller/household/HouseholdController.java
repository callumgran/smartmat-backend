package edu.ntnu.idatt2106.smartmat.controller.household;

import edu.ntnu.idatt2106.smartmat.dto.household.CreateHouseholdDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.UpdateHouseholdDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.mapper.household.HouseholdMapper;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for household endpoints.
 * Used for all household endpoints.
 * All endpoints are private and require authentication.
 * @author Callum G.
 * @version 1.0 - 19.04.2023
 */
@RestController
@RequestMapping(value = "/api/v1/private/households")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class HouseholdController {

  private static final Logger LOGGER = LoggerFactory.getLogger(HouseholdController.class);

  private final HouseholdService householdService;

  private final UserService userService;

  /**
   * Get a household by id.
   * @param id The id of the household.
   * @return 200 if the household was found and the household.
   * @throws HouseholdDoesNotExistsException If the household does not exist.
   */
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Get a household by id",
    description = "Get a household by id, if the household does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<HouseholdDTO> getHousehold(@PathVariable UUID id)
    throws HouseholdNotFoundException {
    LOGGER.info("Getting household with id: {}", id);
    Household household = householdService.getHouseholdById(id);

    LOGGER.info("Got household with id: {}", id);
    HouseholdDTO householdDTO = HouseholdMapper.INSTANCE.householdToHouseholdDTO(household);

    LOGGER.info("Mapped household with id: {} to householdDTO", id);

    return ResponseEntity.ok(householdDTO);
  }

  /**
   * Create a household.
   * @param householdDTO The household to create.
   * @return 201 if the household was created.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Create a household",
    description = "Create a household, if the user does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<HouseholdDTO> createHousehold(
    @AuthenticationPrincipal Auth auth,
    @RequestBody CreateHouseholdDTO householdDTO
  ) throws UserDoesNotExistsException, HouseholdNotFoundException, HouseholdAlreadyExistsException {
    LOGGER.info("Creating household with name: {}", householdDTO.getName());
    Household household = Household.builder().name(householdDTO.getName()).build();

    User user = userService.getUserByUsername(auth.getUsername());

    HouseholdMember userHousehold = HouseholdMember
      .builder()
      .household(household)
      .user(user)
      .householdRole(HouseholdRole.OWNER)
      .build();
    household.setMembers(Set.of(userHousehold));

    LOGGER.info("Added owner to household with username: {}", auth.getUsername());

    household = householdService.saveHousehold(household);

    LOGGER.info("Created household with name: {}", householdDTO.getName());
    HouseholdDTO householdDTORet = HouseholdMapper.INSTANCE.householdToHouseholdDTO(household);

    LOGGER.info("Mapped household with name: {} to householdDTO", householdDTO.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(householdDTORet);
  }

  /**
   * Method to update a households name.
   * @param householdId The id of the household.
   * @param updateDTO The new name of the household.
   * @return 200 OK if the household was updated and the updated household.
   * @throws NullPointerException If the household does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Update a households name",
    description = "Update a households name, if the household does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<HouseholdDTO> updateHouseholdName(
    @AuthenticationPrincipal Auth auth,
    @RequestBody UpdateHouseholdDTO updateDTO
  ) throws HouseholdNotFoundException, UserDoesNotExistsException {
    // TODO: Check if user is owner of household or is admin.
    LOGGER.info("Updating name of household with id: {}", updateDTO.getId());

    Household updated = householdService.updateHouseholdName(
      UUID.fromString(updateDTO.getId()),
      updateDTO.getName()
    );

    LOGGER.info("Updated name of household with id: {}", updateDTO.getId());
    HouseholdDTO retDto = HouseholdMapper.INSTANCE.householdToHouseholdDTO(updated);

    return ResponseEntity.ok(retDto);
  }

  /**
   * Method to delete a household.
   * @param householdId The id of the household.
   * @return 204 No Content.
   * @throws NullPointerException If the household does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws UserDoesNotExistsException If the user does not exist.
   */
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Delete a household",
    description = "Delete a household, if the household does not exist, an error is thrown. Requires authentication.",
    tags = { "household" }
  )
  public ResponseEntity<String> deleteHousehold(
    @AuthenticationPrincipal Auth auth,
    @PathVariable UUID id
  ) throws HouseholdNotFoundException, UserDoesNotExistsException {
    // TODO: Check if user is owner of household or is admin.
    LOGGER.info("Deleting household with id: {}", id);

    householdService.deleteHouseholdById(id);

    LOGGER.info("Deleted household with id: {}", id);
    return ResponseEntity.noContent().build();
  }
}
