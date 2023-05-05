package edu.ntnu.idatt2106.smartmat.controller.foodproduct;

import edu.ntnu.idatt2106.smartmat.dto.foodproduct.BareFoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.foodproduct.FoodProductDTO;
import edu.ntnu.idatt2106.smartmat.dto.kassalapp.KassalApiDataDTO;
import edu.ntnu.idatt2106.smartmat.dto.kassalapp.KassalApiProductDTO;
import edu.ntnu.idatt2106.smartmat.exceptions.DatabaseException;
import edu.ntnu.idatt2106.smartmat.exceptions.PermissionDeniedException;
import edu.ntnu.idatt2106.smartmat.exceptions.foodproduct.FoodProductNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.ingredient.IngredientNotFoundException;
import edu.ntnu.idatt2106.smartmat.exceptions.validation.BadInputException;
import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.mapper.foodproduct.FoodProductMapper;
import edu.ntnu.idatt2106.smartmat.model.foodproduct.FoodProduct;
import edu.ntnu.idatt2106.smartmat.model.ingredient.Ingredient;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import edu.ntnu.idatt2106.smartmat.security.Auth;
import edu.ntnu.idatt2106.smartmat.security.KassalappAPITokenSingleton;
import edu.ntnu.idatt2106.smartmat.service.foodproduct.FoodProductService;
import edu.ntnu.idatt2106.smartmat.service.ingredient.IngredientService;
import edu.ntnu.idatt2106.smartmat.service.unit.UnitService;
import edu.ntnu.idatt2106.smartmat.utils.IngredientTitleMatcher;
import edu.ntnu.idatt2106.smartmat.utils.ParseUnit;
import edu.ntnu.idatt2106.smartmat.utils.UnitAmountTuple;
import edu.ntnu.idatt2106.smartmat.validation.foodproduct.FoodProductValidation;
import edu.ntnu.idatt2106.smartmat.validation.search.SearchRequestValidation;
import edu.ntnu.idatt2106.smartmat.validation.user.AuthValidation;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Controller for food product endpoints.
 * All food product endpoints are private and require authentication.
 *
 * @author Callum G, Nicolai H, Brand,
 * @version 1.4 - 05.05.2023
 */
@RestController
@RequestMapping(value = "/api/v1/private/foodproducts")
@EnableAutoConfiguration
@RequiredArgsConstructor
public class FoodProductController {

  private static final Logger LOGGER = LoggerFactory.getLogger(FoodProductController.class);

  private final FoodProductService foodProductService;

  private final IngredientService ingredientService;

  private final UnitService unitService;

  private final RestTemplate restTemplate;

  /**
   * Gets a food product by its id.
   *
   * @param id The id of the food product to get.
   * @return a 200 OK response with the food product.
   * @throws FoodProductNotFoundException If the food product is not found.
   * @throws NullPointerException         If the id is null.
   */
  @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Get a food product by id",
    description = "Get a food product by its id, requires authentication.",
    tags = { "foodproduct" }
  )
  public ResponseEntity<FoodProductDTO> getFoodProductById(@PathVariable("id") Long id)
    throws FoodProductNotFoundException, NullPointerException {
    LOGGER.info("GET /api/v1/private/foodproducts/{}", id);
    FoodProductDTO foodProductDTO = FoodProductMapper.INSTANCE.foodProductToFoodProductDTO(
      foodProductService.getFoodProductById(id)
    );

    LOGGER.info("Found and returning food product with id: {}", id);
    return ResponseEntity.ok(foodProductDTO);
  }

  /**
   * Gets a food product by its EAN.
   *
   * @param EAN The EAN of the food product to get.
   * @return a 200 OK response with the food product.
   * @throws FoodProductNotFoundException If the food product is not found.
   * @throws NullPointerException         If the EAN is null.
   * @throws BadInputException            If the EAN is invalid.
   * @throws DatabaseException            If the food product is not found in the database.
   */
  @GetMapping(value = "/ean/{EAN}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Get a food product by EAN",
    description = "Get a food product by its EAN, requires authentication.",
    tags = { "foodproduct" }
  )
  public ResponseEntity<FoodProductDTO> getFoodProductByEAN(@PathVariable("EAN") String EAN)
    throws FoodProductNotFoundException, NullPointerException, BadInputException, DatabaseException {
    if (!FoodProductValidation.validateEan(EAN)) throw new BadInputException(
      "EAN koden er ugyldig"
    );
    LOGGER.info("GET https://kassal.app/api/v1/products/ean/{}", EAN);

    FoodProduct foodProduct;

    boolean isFirstTime = false;

    try {
      foodProduct = foodProductService.getFoodProductByEan(EAN);
    } catch (FoodProductNotFoundException e) {
      // Log that the food product was not found in your own database
      LOGGER.error("Food product not found in internal database: {}", EAN);

      // Hvis matvaren ikke finnes i din egen database, hent den fra Kassal API
      String kassalApiUrl = "https://kassal.app/api/v1/products/ean/" + EAN;
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(KassalappAPITokenSingleton.getInstance().getKassalappAPIToken());
      HttpEntity<String> entity = new HttpEntity<>(headers);
      ResponseEntity<KassalApiDataDTO> response;
      try {
        response =
          restTemplate.exchange(kassalApiUrl, HttpMethod.GET, entity, KassalApiDataDTO.class);
      } catch (Exception e1) {
        throw new FoodProductNotFoundException("Matvaren ble ikke funnet i vår eksterne database");
      }

      // logga responsen for å debugg
      LOGGER.info("Kassal API Response: {}", response);

      if (response.getStatusCode() == HttpStatus.OK) {
        LOGGER.info("Body from kassal: ", response.getBody());
        List<KassalApiProductDTO> foundFoodProducts = response.getBody().getData().getProducts();
        KassalApiProductDTO chosenKassalFoodProduct = foundFoodProducts.get(0);
        LOGGER.info("Kassal API chosen food product: {}", chosenKassalFoodProduct);

        String productName = chosenKassalFoodProduct.getName();

        UnitAmountTuple amountTuple = ParseUnit.parseUnit(
          unitService.getAllUnits(),
          productName,
          unitService.getUnitByAbbreviation("stk")
        );

        Ingredient foundIngredient = IngredientTitleMatcher.getBestMatch(
          ingredientService.getAllIngredients(),
          productName
        );

        String productImage = chosenKassalFoodProduct.getImage();

        LOGGER.info("Found unit: {}", amountTuple.getUnit().getAbbreviation());
        LOGGER.info("Found amount: {}", amountTuple.getAmount());

        foodProduct =
          FoodProduct
            .builder()
            .EAN(EAN)
            .name(productName)
            .image(productImage)
            .amount(amountTuple.getAmount())
            .unit(amountTuple.getUnit())
            .isNotIngredient(false)
            .ingredient(foundIngredient)
            .build();

        foodProductService.saveFoodProduct(foodProduct);
        isFirstTime = true;
      } else {
        // logg response status for debeugging
        LOGGER.error("Kassal API Response Status: {}", response.getStatusCode());
        LOGGER.error("Kassal API Response Body: {}", response.getBody());
        throw new FoodProductNotFoundException(
          "Matvaren ble ikke funnet i både intern og ekstern database: " + EAN
        );
      }
    }
    FoodProductDTO foodProductDTO = FoodProductMapper.INSTANCE.foodProductToFoodProductDTO(
      foodProduct
    );
    foodProductDTO.setFirstTime(isFirstTime);
    return ResponseEntity.ok(foodProductDTO);
  }

  /**
   * Updates a food product.
   * A temporary solution to have frontend update food product
   * Then frontend can set the ingredient of the food product
   * This will only be applicable in the start when many food products are
   * directly from Kassal.app.
   * @param auth           The authentication of the user.
   * @param id             The id of the food product to update.
   * @param foodProductDTO The food product to update.
   * @return a 200 OK response with the updated food product.
   * @throws FoodProductNotFoundException If the food product is not found.
   * @throws IngredientNotFoundException  If the ingredient is not found.
   * @throws BadInputException            If the input is invalid.
   * @throws NullPointerException         If the food product is null.
   */
  @PutMapping(
    value = "/id/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Update a food product",
    description = "Update a food product, requires authentication.",
    tags = { "foodproduct" }
  )
  public ResponseEntity<FoodProductDTO> updateFoodProduct(
    @AuthenticationPrincipal Auth auth,
    @PathVariable long id,
    @RequestBody BareFoodProductDTO foodProductDTO
  )
    throws FoodProductNotFoundException, IngredientNotFoundException, NullPointerException, BadInputException {
    LOGGER.info("Food product update request: {}", foodProductDTO);

    if (
      !FoodProductValidation.validateUpdateFoodProduct(
        foodProductDTO.getId(),
        foodProductDTO.getEAN(),
        foodProductDTO.getName(),
        foodProductDTO.getAmount(),
        foodProductDTO.isLooseWeight()
      )
    ) throw new BadInputException("Ugyldig input for oppdatering av en matvare");

    LOGGER.info("GET /api/v1/private/foodproducts/update");

    FoodProduct foodProduct = FoodProductMapper.INSTANCE.bareFoodProductDTOToFoodProduct(
      foodProductDTO
    );
    foodProduct.setId(id);
    if (foodProductDTO.getIngredientId() != null) {
      foodProduct.setIngredient(
        ingredientService.getIngredientById(foodProductDTO.getIngredientId())
      );
    }

    FoodProductDTO updatedFoodProductDTO = FoodProductMapper.INSTANCE.foodProductToFoodProductDTO(
      foodProductService.updateFoodProduct(foodProduct)
    );

    return ResponseEntity.ok(updatedFoodProductDTO);
  }

  /**
   * Deletes a food product.
   *
   * @param auth The authentication of the user.
   * @param id The id of the food product to delete.
   * @return a 204 NO CONTENT response.
   * @throws PermissionDeniedException    If the user does not have permission to delete the food product.
   * @throws FoodProductNotFoundException If the food product is not found.
   * @throws NullPointerException         If the id is null.
   */
  @DeleteMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Delete a food product",
    description = "Delete a food product, requires authentication.",
    tags = { "foodproduct" }
  )
  public ResponseEntity<Void> deleteFoodProduct(
    @AuthenticationPrincipal Auth auth,
    @PathVariable long id
  ) throws PermissionDeniedException, FoodProductNotFoundException, NullPointerException {
    if (!AuthValidation.hasRole(auth, UserRole.ADMIN)) throw new PermissionDeniedException(
      "Du har ikke tilgang til denne ressursen."
    );

    LOGGER.info("GET /api/v1/private/foodproducts/delete/{}", id);

    foodProductService.deleteFoodProductById(id);

    LOGGER.info("Deleted and returning food product with id: {}", id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Creates a food product.
   *
   * @param auth           The authentication of the user.
   * @param foodProductDTO The food product to create.
   * @return a 201 CREATED response with the created food product.
   * @throws PermissionDeniedException   If the user does not have permission to create the food product.
   * @throws NullPointerException        If the food product is null.
   * @throws IngredientNotFoundException If the ingredient is not found.
   * @throws BadInputException           If the input is invalid.
   */
  @PostMapping(
    value = "",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Create a food product",
    description = "Create a food product, requires authentication.",
    tags = { "foodproduct" }
  )
  public ResponseEntity<FoodProductDTO> createFoodProduct(
    @AuthenticationPrincipal Auth auth,
    @RequestBody BareFoodProductDTO foodProductDTO
  )
    throws PermissionDeniedException, NullPointerException, IngredientNotFoundException, BadInputException {
    if (!AuthValidation.hasRole(auth, UserRole.ADMIN)) throw new PermissionDeniedException(
      "Du har ikke tilgang til denne ressursen."
    );

    if (
      !FoodProductValidation.validateCreateFoodProduct(
        foodProductDTO.getEAN(),
        foodProductDTO.getName(),
        foodProductDTO.getAmount(),
        foodProductDTO.isLooseWeight(),
        foodProductDTO.getIngredientId()
      )
    ) throw new BadInputException("Ugyldig input for å lage matvare.");

    LOGGER.info("GET /api/v1/private/foodproducts/create");

    FoodProduct foodProduct = FoodProductMapper.INSTANCE.bareFoodProductDTOToFoodProduct(
      foodProductDTO
    );

    foodProduct.setIngredient(
      ingredientService.getIngredientById(foodProductDTO.getIngredientId())
    );

    FoodProductDTO createdFoodProductDTO = FoodProductMapper.INSTANCE.foodProductToFoodProductDTO(
      foodProductService.saveFoodProduct(foodProduct)
    );

    LOGGER.info("Created and returning food product with id: {}", createdFoodProductDTO.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(createdFoodProductDTO);
  }

  /**
   * Searches for food products.
   *
   * @param auth           The authentication of the user.
   * @param searchRequest The search request.
   * @return a 200 OK response with the found food products.
   * @throws BadInputException    If the search request is invalid.
   * @throws NullPointerException If the search request is null.
   */
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
    summary = "Search for food products",
    description = "Search for food products, requires authentication.",
    tags = { "foodproduct" }
  )
  public ResponseEntity<List<FoodProductDTO>> searchFoodProducts(
    @AuthenticationPrincipal Auth auth,
    @RequestBody SearchRequest searchRequest
  ) throws BadInputException, NullPointerException {
    if (SearchRequestValidation.validateSearchRequest(searchRequest)) throw new BadInputException(
      "Ugyldig søkeforespørsel."
    );

    LOGGER.info("GET /api/v1/private/foodproducts/search");

    List<FoodProductDTO> foodProductDTOs = foodProductService
      .searchFoodProductsPaginated(searchRequest)
      .stream()
      .map(FoodProductMapper.INSTANCE::foodProductToFoodProductDTO)
      .toList();

    LOGGER.info("Found and returning {} food products", foodProductDTOs.size());
    return ResponseEntity.ok(foodProductDTOs);
  }

  /**
   * Gets an ingredient by as a food product with loose weight.
   *
   * @param ingredientId The id of the ingredient.
   * @return a 200 OK response with the found food product.
   * @throws FoodProductNotFoundException If the food product is not found.
   * @throws IngredientNotFoundException If the ingredient is not found.
   * @throws NullPointerException        If the id is null.
   * @throws BadInputException           If the input is invalid.
   * @throws DatabaseException           If something goes wrong with the database.
   */
  @GetMapping(
    value = "/loose-ingredient/{ingredient-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(
    summary = "Get food products with loose ingredient",
    description = "Get food products with loose ingredient, requires authentication.",
    tags = { "foodproduct" }
  )
  public ResponseEntity<FoodProductDTO> getLooseFoodProductByIngredient(
    @AuthenticationPrincipal Auth auth,
    @PathVariable("ingredient-id") Long ingredientId
  )
    throws FoodProductNotFoundException, IngredientNotFoundException, NullPointerException, BadInputException, DatabaseException {
    LOGGER.info("GET /api/v1/private/foodproducts/loose-ingredient/{}", ingredientId);

    Ingredient ingredient = ingredientService.getIngredientById(ingredientId);

    LOGGER.info("Found ingredient with id: {}", ingredient.getId());

    FoodProduct foodProduct = foodProductService.getLooseFoodProductByIngredient(ingredient);

    LOGGER.info("Found and returning food product with id: {}", foodProduct.getId());

    FoodProductDTO foodProductDTO = FoodProductMapper.INSTANCE.foodProductToFoodProductDTO(
      foodProduct
    );

    return ResponseEntity.ok(foodProductDTO);
  }
}
