package edu.ntnu.idatt2106.smartmat.controller.foodproduct;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.CreateHouseholdFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.foodproduct.HouseholdFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.foodproduct.UpdateHouseholdFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.PermissionDeniedException;
import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.validation.BadInputException;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.HouseholdFoodProductMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.HouseholdFoodProduct;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.FoodProductService;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.HouseholdFoodProductService;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.statistic.FoodProductHistoryService;
import edu.ntnu.idatt2106.smartmat.utils.PrivilegeUtil;
import edu.ntnu.idatt2106.smartmat.utils.UnitUtils;
import edu.ntnu.idatt2106.smartmat.validation.foodproduct.FoodProductValidation;
import edu.ntnu.idatt2106.smartmat.validation.search.SearchRequestValidation;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.List;
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
 * Controller for household food product endpoints.
 * All food product endpoints are private and require authentication.
 * @author Callum G.
 * @version 1.1 - 05.05.2023
 */
@RestController
@RequestMapping(value = "/api/v1/private/households")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class HouseholdFoodProductController {

  private static final Logger LOGGER = LoggerFactory.getLogger(HouseholdFoodProduct.class);

  private final FoodProductService foodProductService;

  private final HouseholdFoodProductService householdFoodProductService;

  private final HouseholdService householdService;

  private final FoodProductHistoryService foodProductHistoryService;

  /**
   * Get a household food product by id.
   * @param auth The authentication object.
   * @param householdId The id of the household.
   * @param id The id of the food product.
   * @return 200 OK if the household food product was found.
   * @throws PermissionDeniedException If the user does not have permission to access the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws FoodProductNotFoundException If the food product does not exist.
   * @throws NullPointerException If the household id or food product id is null.
   */
  @GetMapping(
    value = "/{householdId}/foodproducts/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Get a household food product by id",
    description = "Get a household food product by its id and the household id, requires authentication and household membership.",
    tags = { "householdfoodproduct" }
  )
  public ResponseEntity<HouseholdFoodProductDTO> getHouseholdFoodProductById(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("householdId") UUID householdId,
    @PathVariable("id") UUID id
  )
    throws PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, FoodProductNotFoundException, NullPointerException {
    if (!PrivilegeUtil.isAdminOrHouseholdMember(auth, householdId, householdService)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å hente ut matvarene i husstanden."
      );
    }

    LOGGER.info("GET /api/v1/private/households/{}/foodproducts/{}", householdId, id);
    HouseholdFoodProductDTO householdFoodProductDTO = HouseholdFoodProductMapper.INSTANCE.householdFoodProductToHouseholdFoodProductDTO(
      householdFoodProductService.getFoodProductById(id)
    );

    LOGGER.info("Found and returning household food product with id: {}", id);
    return ResponseEntity.ok(householdFoodProductDTO);
  }

  /**
   * Method to get a household food product by ean.
   * @param auth The authentication object.
   * @param householdId The id of the household.
   * @param ean The ean of the food product.
   * @return 200 OK if the household food product was found.
   * @throws PermissionDeniedException If the user does not have permission to access the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws FoodProductNotFoundException If the food product does not exist.
   * @throws NullPointerException If the household id or food product ean is null.
   * @throws BadInputException If the ean is not valid.
   */
  @GetMapping(
    value = "/{householdId}/foodproducts/ean/{ean}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Get a household food product by ean",
    description = "Get a household food product by its ean and the household id, requires authentication.",
    tags = { "householdfoodproduct" }
  )
  public ResponseEntity<List<HouseholdFoodProductDTO>> getHouseholdFoodProductByEan(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("householdId") UUID householdId,
    @PathVariable("ean") String ean
  )
    throws PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, FoodProductNotFoundException, NullPointerException, BadInputException {
    if (!PrivilegeUtil.isAdminOrHouseholdMember(auth, householdId, householdService)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å hente ut matvarene i husstanden."
      );
    }

    if (!FoodProductValidation.validateEan(ean)) throw new BadInputException(
      "EAN-verdien er ikke gyldig."
    );

    LOGGER.info("GET /api/v1/private/households/{}/foodproducts/ean/{}", householdId, ean);

    LOGGER.info("Found and returning household food product with ean: {}", ean);
    return ResponseEntity.ok(
      householdFoodProductService
        .findHouseholdFoodProductByIdAndEAN(householdId, ean)
        .stream()
        .map(HouseholdFoodProductMapper.INSTANCE::householdFoodProductToHouseholdFoodProductDTO)
        .toList()
    );
  }

  /**
   * Method to search for a household food product.
   * @param auth The authentication object.
   * @param householdId The id of the household.
   * @param searchRequest The search request.
   * @return 200 OK and the search result if the search was successful.
   * @throws BadInputException If the search request is invalid.
   * @throws PermissionDeniedException If the user does not have permission to access the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws FoodProductNotFoundException If the food product does not exist.
   * @throws NullPointerException If the household id or food product ean is null.
   */
  @PostMapping(
    value = "/{householdId}/foodproducts/search",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Search for a household food product",
    description = "Search for a household food product by its name and the household id, requires authentication and household membership.",
    tags = { "householdfoodproduct" }
  )
  public ResponseEntity<List<HouseholdFoodProductDTO>> searchForHouseholdFoodProduct(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("householdId") UUID householdId,
    @RequestBody SearchRequest searchRequest
  )
    throws BadInputException, PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, FoodProductNotFoundException, NullPointerException {
    if (!SearchRequestValidation.validateSearchRequest(searchRequest)) throw new BadInputException(
      "Søkeverdiene er ikke gyldige."
    );

    if (!PrivilegeUtil.isAdminOrHouseholdMember(auth, householdId, householdService)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å hente ut matvarene i husstanden."
      );
    }

    LOGGER.info("POST /api/v1/private/households/{}/foodproducts/search", householdId);
    List<HouseholdFoodProductDTO> householdFoodProductDTOs = householdFoodProductService
      .searchFoodProducts(searchRequest)
      .stream()
      .map(HouseholdFoodProductMapper.INSTANCE::householdFoodProductToHouseholdFoodProductDTO)
      .toList();

    return ResponseEntity.ok(householdFoodProductDTOs);
  }

  /**
   * Method to create a household food product.
   * @param auth The authentication object.
   * @param householdId The id of the household.
   * @param createHouseholdFoodProductDTO The household food product to create.
   * @return 201 CREATED if the household food product was created.
   * @throws PermissionDeniedException If the user does not have permission to access the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws FoodProductNotFoundException If the food product does not exist.
   * @throws NullPointerException If the household id or food product ean is null.
   * @throws BadInputException If the household food product is invalid.
   */
  @PostMapping(value = "/{householdId}/foodproducts", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Create a household food product",
    description = "Delete a household food product, requires authentication and admin or household privilege.",
    tags = { "householdfoodproduct" }
  )
  public ResponseEntity<HouseholdFoodProductDTO> createHouseholdFoodProduct(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("householdId") UUID householdId,
    @RequestBody CreateHouseholdFoodProductDTO createHouseholdFoodProductDTO
  )
    throws PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, FoodProductNotFoundException, NullPointerException, BadInputException {
    if (!PrivilegeUtil.isAdminOrHouseholdPrivileged(auth, householdId, householdService)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å opprette matvarene i husstanden."
      );
    }

    if (
      !FoodProductValidation.validateCreateHouseholdFoodProduct(
        createHouseholdFoodProductDTO.getFoodProductId(),
        createHouseholdFoodProductDTO.getExpirationDate().toString(),
        createHouseholdFoodProductDTO.getAmountLeft()
      )
    ) throw new BadInputException(
      "Input verdiene for å opprette en matvare i husstanden er ikke gyldige."
    );

    LOGGER.info("POST /api/v1/private/households/{}/foodproducts", householdId);
    LOGGER.info(
      "Creating household food product with foodproduct id: {}",
      createHouseholdFoodProductDTO.getFoodProductId()
    );

    HouseholdFoodProduct householdFoodProduct = HouseholdFoodProductMapper.INSTANCE.createHouseholdFoodProductDTOToHouseholdFoodProduct(
      createHouseholdFoodProductDTO
    );
    householdFoodProduct.setHousehold(householdService.getHouseholdById(householdId));

    LOGGER.info("Set household.");

    FoodProduct foodProduct = foodProductService.getFoodProductById(
      createHouseholdFoodProductDTO.getFoodProductId()
    );

    LOGGER.info(
      "Found food product with id {}: {}",
      createHouseholdFoodProductDTO.getFoodProductId(),
      foodProduct
    );

    householdFoodProduct.setFoodProduct(foodProduct);

    HouseholdFoodProductDTO createdHouseholdFoodProductDTO = HouseholdFoodProductMapper.INSTANCE.householdFoodProductToHouseholdFoodProductDTO(
      householdFoodProductService.saveFoodProduct(householdFoodProduct)
    );

    LOGGER.info(
      "Created and returning household food product with id: {}",
      createdHouseholdFoodProductDTO.getId()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(createdHouseholdFoodProductDTO);
  }

  /**
   * Method to update a household food product.
   * @param auth The authentication object.
   * @param householdId The id of the household.
   * @param householdFoodProductDTO The household food product to update.
   * @return The 200 OK response with the updated household food product or
   * 204 No Content if the household food product was deleted for having no quantity.
   * @throws PermissionDeniedException If the user does not have permission to access the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws FoodProductNotFoundException If the food product does not exist.
   * @throws NullPointerException If the household id or food product ean is null.
   * @throws BadInputException If the household food product is invalid.
   */
  @PutMapping(
    value = "/{householdId}/foodproducts/id/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Update a household food product",
    description = "Delete a household food product, requires authentication and admin or household privilege.",
    tags = { "householdfoodproduct" }
  )
  public ResponseEntity<HouseholdFoodProductDTO> updateHouseholdFoodProduct(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("householdId") UUID householdId,
    @PathVariable("id") UUID id,
    @RequestBody UpdateHouseholdFoodProductDTO householdFoodProductDTO
  )
    throws PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, FoodProductNotFoundException, NullPointerException, BadInputException {
    if (!PrivilegeUtil.isAdminOrHouseholdPrivileged(auth, householdId, householdService)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å oppdatere matvarene i husstanden."
      );
    }

    if (
      !FoodProductValidation.validateUpdateHouseholdFoodProduct(
        householdFoodProductDTO.getFoodProductId(),
        householdFoodProductDTO.getExpirationDate().toString(),
        householdFoodProductDTO.getAmountLeft()
      )
    ) throw new BadInputException("Inputen til matvaren var ikke gyldig.");

    if (!householdFoodProductService.existsById(id)) throw new FoodProductNotFoundException(
      "Matvaren eksisterer ikke."
    );

    LOGGER.info("PUT /api/v1/private/households/{}/foodproducts/id/{}", householdId, id);

    if (!id.equals(householdFoodProductDTO.getId())) throw new BadInputException(
      "Iden til matvaren stemmer ikke overens med iden i URLen."
    );

    if (householdFoodProductDTO.getAmountLeft() <= 0) {
      householdFoodProductService.deleteFoodProductById(id);
      return ResponseEntity.noContent().build();
    }

    HouseholdFoodProduct householdFoodProduct = HouseholdFoodProductMapper.INSTANCE.updateHouseholdFoodProductDTOToHouseholdFoodProduct(
      householdFoodProductDTO
    );

    householdFoodProduct.setHousehold(householdService.getHouseholdById(householdId));

    householdFoodProduct.setFoodProduct(
      foodProductService.getFoodProductById(householdFoodProductDTO.getFoodProductId())
    );

    HouseholdFoodProductDTO updatedHouseholdFoodProductDTO = HouseholdFoodProductMapper.INSTANCE.householdFoodProductToHouseholdFoodProductDTO(
      householdFoodProductService.updateFoodProduct(householdFoodProduct)
    );

    LOGGER.info(
      "Updated and returning household food product with id: {}",
      updatedHouseholdFoodProductDTO.getId()
    );
    return ResponseEntity.ok(updatedHouseholdFoodProductDTO);
  }

  /**
   * Method to delete a household food product.
   * @param auth The authentication object.
   * @param householdId The id of the household.
   * @param id The id of the household food product to delete.
   * @return The deleted household food product.
   * @throws PermissionDeniedException If the user does not have permission to access the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws FoodProductNotFoundException If the food product does not exist.
   * @throws NullPointerException If the household id or food product ean is null.
   */
  @DeleteMapping(
    value = "/{householdId}/foodproducts/id/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Delete a household food product",
    description = "Delete a household food product, requires authentication and admin or household privilege.",
    tags = { "householdfoodproduct" }
  )
  public ResponseEntity<Void> deleteHouseholdFoodProduct(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("householdId") UUID householdId,
    @PathVariable("id") UUID id
  )
    throws PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, FoodProductNotFoundException, NullPointerException {
    if (!PrivilegeUtil.isAdminOrHouseholdPrivileged(auth, householdId, householdService)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å slette matvarene i husstanden."
      );
    }

    LOGGER.info("DELETE /api/v1/private/households/{}/foodproducts/{}", householdId, id);

    householdFoodProductService.deleteFoodProductById(id);

    LOGGER.info("Deleted and returning household food product with id: {}", id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Method to use a household food product.
   * @param auth The authentication object.
   * @param householdId The id of the household.
   * @param id The id of the household food product to use.
   * @return The 200 ok response or 204 No Content if the household food product was deleted for having no quantity.
   * @throws PermissionDeniedException If the user does not have permission to access the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws FoodProductNotFoundException If the food product does not exist.
   * @throws NullPointerException If the household id or food product ean is null.
   */
  @PutMapping(
    value = "/{householdId}/foodproducts/id/{id}/use",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Use a household food product",
    description = "Use a household food product, requires authentication and admin or household privilege.",
    tags = { "householdfoodproduct" }
  )
  public ResponseEntity<HouseholdFoodProductDTO> useHouseholdFoodProduct(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("householdId") UUID householdId,
    @PathVariable("id") UUID id,
    @RequestBody Double amountUsed
  )
    throws PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, FoodProductNotFoundException, NullPointerException {
    if (!PrivilegeUtil.isAdminOrHouseholdPrivileged(auth, householdId, householdService)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å bruke matvarene i husstanden."
      );
    }

    LOGGER.info("PUT /api/v1/private/households/{}/foodproducts/id/{}/use", householdId, id);

    HouseholdFoodProduct householdFoodProduct = householdFoodProductService.getFoodProductById(id);

    householdFoodProduct.setAmountLeft(householdFoodProduct.getAmountLeft() - amountUsed);

    if (householdFoodProduct.getAmountLeft() <= 0) {
      FoodProductHistory foodProductHistory = FoodProductHistory
        .builder()
        .foodProduct(householdFoodProduct.getFoodProduct())
        .household(householdFoodProduct.getHousehold())
        .thrownAmount(0)
        .amount(
          UnitUtils.getNormalizedUnit(
            amountUsed,
            householdFoodProduct.getFoodProduct().getUnit().getToNormalFormConversionFactor(),
            householdFoodProduct.getFoodProduct().getUnit().getUnitType()
          )
        )
        .date(LocalDate.now())
        .build();

      foodProductHistoryService.saveFoodProductHistory(foodProductHistory);
      householdFoodProductService.deleteFoodProductById(id);
      return ResponseEntity.noContent().build();
    }

    HouseholdFoodProductDTO updatedHouseholdFoodProductDTO = HouseholdFoodProductMapper.INSTANCE.householdFoodProductToHouseholdFoodProductDTO(
      householdFoodProductService.updateFoodProduct(householdFoodProduct)
    );

    LOGGER.info(
      "Updated and returning household food product with id: {}",
      updatedHouseholdFoodProductDTO.getId()
    );

    return ResponseEntity.ok().body(updatedHouseholdFoodProductDTO);
  }

  /**
   * Method to throwaway a household food product.
   * @param auth The authentication object.
   * @param householdId The id of the household.
   * @param id The id of the household food product to throw away.
   * @return 204 No Content if the household food product was deleted for having no quantity.
   * @throws PermissionDeniedException If the user does not have permission to access the household.
   * @throws UserDoesNotExistsException If the user does not exist.
   * @throws HouseholdNotFoundException If the household does not exist.
   * @throws FoodProductNotFoundException If the food product does not exist.
   * @throws NullPointerException If the household id or food product ean is null.
   */
  @DeleteMapping(
    value = "/{householdId}/foodproducts/id/{id}/throw",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Use a household food product",
    description = "Use a household food product, requires authentication and admin or household privilege.",
    tags = { "householdfoodproduct" }
  )
  public ResponseEntity<Void> throwHouseholdFoodProduct(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("householdId") UUID householdId,
    @PathVariable("id") UUID id
  )
    throws PermissionDeniedException, UserDoesNotExistsException, HouseholdNotFoundException, FoodProductNotFoundException, NullPointerException {
    if (!PrivilegeUtil.isAdminOrHouseholdPrivileged(auth, householdId, householdService)) {
      throw new PermissionDeniedException(
        "Brukeren har ikke tilgang til å bruke matvarene i husstanden."
      );
    }

    LOGGER.info("PUT /api/v1/private/households/{}/foodproducts/id/{}/use", householdId, id);

    HouseholdFoodProduct householdFoodProduct = householdFoodProductService.getFoodProductById(id);

    FoodProductHistory foodProductHistory = FoodProductHistory
      .builder()
      .foodProduct(householdFoodProduct.getFoodProduct())
      .household(householdFoodProduct.getHousehold())
      .thrownAmount(1)
      .date(LocalDate.now())
      .amount(UnitUtils.getNormalizedUnit(householdFoodProduct))
      .build();

    foodProductHistoryService.saveFoodProductHistory(foodProductHistory);

    LOGGER.info("Deleted household food product with id: {}", id);

    householdFoodProductService.deleteFoodProductById(id);
    return ResponseEntity.noContent().build();
  }
}
