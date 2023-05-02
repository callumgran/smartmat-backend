package edu.ntnu.idatt2106.smartmat.service.statistic;

import edu.ntnu.idatt2106.smartmat.exceptions.statistic.FoodProductHistoryNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import edu.ntnu.idatt2106.smartmat.repository.statistic.FoodProductHistoryRepository;
import io.micrometer.common.lang.NonNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodProductHistoryServiceImpl implements FoodProductHistoryService {

  @Autowired
  private FoodProductHistoryRepository foodProductHistoryRepository;

  /**
   * Method for checking if a food product history exists by id.
   * @param id The id of the food product history.
   * @return True if the food product history exists, false if not.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public boolean existsById(@NonNull UUID id) throws NullPointerException {
    return foodProductHistoryRepository.existsById(id);
  }

  /**
   * Method for getting a food product history by id.
   * @param id The id of the food product history.
   * @return The food product history.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public FoodProductHistory getFoodProductHistoryById(@NonNull UUID id)
    throws NullPointerException, FoodProductHistoryNotFoundException {
    return foodProductHistoryRepository
      .findById(id)
      .orElseThrow(FoodProductHistoryNotFoundException::new);
  }

  /**
   * Method for saving a food product history.
   * @param foodProductHistory The food product history to save.
   * @return The saved food product history.
   * @throws NullPointerException If the food product history is null.
   */
  @Override
  public FoodProductHistory saveFoodProductHistory(@NonNull FoodProductHistory foodProductHistory)
    throws NullPointerException, IllegalArgumentException {
    if (
      foodProductHistory.getId() != null &&
      foodProductHistoryRepository.existsById(foodProductHistory.getId())
    ) {
      throw new IllegalArgumentException("Food product history already exists");
    }
    return foodProductHistoryRepository.save(foodProductHistory);
  }

  /**
   * Method for updating a food product history.
   * @param id The id of the food product history to update.
   * @param foodProductHistory The food product history to update.
   * @return The updated food product history.
   * @throws NullPointerException If the id or food product history is null.
   */
  @Override
  public FoodProductHistory updateFoodProductHistory(
    @NonNull UUID id,
    @NonNull FoodProductHistory foodProductHistory
  ) throws NullPointerException, FoodProductHistoryNotFoundException {
    if (!foodProductHistoryRepository.existsById(id)) {
      throw new FoodProductHistoryNotFoundException();
    }
    foodProductHistory.setId(id);
    return foodProductHistoryRepository.save(foodProductHistory);
  }

  /**
   * Method for deleting a food product history.
   * @param foodProductHistory The food product history to delete.
   * @throws NullPointerException If the food product history is null.
   */
  @Override
  public void deleteFoodProductHistory(@NonNull FoodProductHistory foodProductHistory)
    throws NullPointerException, FoodProductHistoryNotFoundException {
    if (!foodProductHistoryRepository.existsById(foodProductHistory.getId())) {
      throw new FoodProductHistoryNotFoundException();
    }
    foodProductHistoryRepository.delete(foodProductHistory);
  }

  /**
   * Method for deleting a food product history by id.
   * @param id The id of the food product history to delete.
   * @throws NullPointerException If the id is null.
   */
  @Override
  public void deleteFoodProductHistoryById(@NonNull UUID id)
    throws NullPointerException, FoodProductHistoryNotFoundException {
    if (!foodProductHistoryRepository.existsById(id)) {
      throw new FoodProductHistoryNotFoundException();
    }
    foodProductHistoryRepository.deleteById(id);
  }

  /**
   * Method for getting all food product histories.
   * @return A collection of all food product histories.
   */
  @Override
  public Collection<FoodProductHistory> getAllFoodProductHistory() {
    return foodProductHistoryRepository.findAll();
  }

  /**
   * Method for getting all food product histories by household id.
   * @param householdId The id of the household.
   * @return A collection of all food product histories by household id.
   * @throws NullPointerException If the household id is null.
   */
  @Override
  public Collection<FoodProductHistory> getAllFoodProductHistoryByHouseholdId(
    @NonNull UUID householdId
  ) throws NullPointerException {
    return foodProductHistoryRepository
      .findAllByHouseholdId(householdId)
      .orElseThrow(NullPointerException::new);
  }

  /**
   * Method for getting all food product histories by food product id.
   * @param foodProductId The id of the food product.
   * @return A collection of all food product histories by food product id.
   * @throws NullPointerException If the food product id is null.
   */
  @Override
  public Collection<FoodProductHistory> getAllFoodProductHistoryByFoodProductId(
    @NonNull Long foodProductId
  ) throws NullPointerException {
    return foodProductHistoryRepository
      .findAllByFoodProductId(foodProductId)
      .orElseThrow(NullPointerException::new);
  }

  /**
   * Method for getting all food product histories by household id and food product id.
   * @param householdId The id of the household.
   * @param foodProductId The id of the food product.
   * @return A collection of all food product histories by household id and food product id.
   * @throws NullPointerException If the household id or food product id is null.
   */
  @Override
  public Collection<FoodProductHistory> getAllFoodProductHistoryByHouseholdIdAndFoodProductId(
    @NonNull UUID householdId,
    @NonNull Long foodProductId
  ) throws NullPointerException {
    return foodProductHistoryRepository
      .findAllByHouseholdIdAndFoodProductId(householdId, foodProductId)
      .orElseThrow(NullPointerException::new);
  }

  /**
   * Method to get total waste by household id.
   * @param householdId The id of the household.
   * @return The total waste by household id.
   * @throws NullPointerException If the household id is null.
   */
  @Override
  public double getTotalWaste(@NonNull UUID householdId) throws NullPointerException {
    return foodProductHistoryRepository
      .findAllByHouseholdId(householdId)
      .orElseThrow(NullPointerException::new)
      .stream()
      .mapToDouble(fph -> fph.getAmount())
      .sum();
  }

  /**
   * Method to get total waste by household id in a given time period.
   * @param householdId The id of the household.
   * @param startDate The start date of the time period.
   * @param endDate The end date of the time period.
   * @return The total waste by household id in a given time period.
   * @throws NullPointerException If the household id, start date or end date is null.
   */
  @Override
  public double getWasteByInPeriod(
    @NonNull UUID householdId,
    @NonNull LocalDate startDate,
    @NonNull LocalDate endDate
  ) throws NullPointerException {
    return foodProductHistoryRepository
      .findAllByHouseholdIdAndDateBetween(householdId, startDate, endDate)
      .orElseThrow(NullPointerException::new)
      .stream()
      .mapToDouble(fph -> fph.getAmount())
      .sum();
  }
}
