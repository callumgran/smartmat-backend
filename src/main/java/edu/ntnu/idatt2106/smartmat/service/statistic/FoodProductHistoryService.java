package edu.ntnu.idatt2106.smartmat.service.statistic;

import edu.ntnu.idatt2106.smartmat.exceptions.statistic.FoodProductHistoryNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Interface for the food product history.
 * @author Callum G.
 * @version 1.0 - 28.04.2023
 */
@Service
public interface FoodProductHistoryService {
  boolean existsById(@NonNull UUID id) throws NullPointerException;

  FoodProductHistory getFoodProductHistoryById(@NonNull UUID id)
    throws NullPointerException, FoodProductHistoryNotFoundException;

  FoodProductHistory saveFoodProductHistory(@NonNull FoodProductHistory foodProductHistory)
    throws NullPointerException, IllegalArgumentException;

  FoodProductHistory updateFoodProductHistory(
    @NonNull UUID id,
    @NonNull FoodProductHistory foodProductHistory
  ) throws NullPointerException, FoodProductHistoryNotFoundException;

  void deleteFoodProductHistory(@NonNull FoodProductHistory foodProductHistory)
    throws NullPointerException, FoodProductHistoryNotFoundException;

  void deleteFoodProductHistoryById(@NonNull UUID id)
    throws NullPointerException, FoodProductHistoryNotFoundException;

  Collection<FoodProductHistory> getAllFoodProductHistory();

  Collection<FoodProductHistory> getAllFoodProductHistoryByHouseholdId(@NonNull UUID householdId)
    throws NullPointerException;

  Collection<FoodProductHistory> getAllFoodProductHistoryByFoodProductId(
    @NonNull Long foodProductId
  ) throws NullPointerException;

  Collection<FoodProductHistory> getAllFoodProductHistoryByHouseholdIdAndFoodProductId(
    @NonNull UUID householdId,
    @NonNull Long foodProductId
  ) throws NullPointerException;

  double getTotalWaste(@NonNull UUID householdId) throws NullPointerException;

  double getWasteByInPeriod(
    @NonNull UUID householdId,
    @NonNull LocalDate begin,
    @NonNull LocalDate end
  ) throws NullPointerException;
}
