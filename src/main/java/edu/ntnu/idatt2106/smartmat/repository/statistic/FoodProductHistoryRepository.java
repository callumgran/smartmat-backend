package edu.ntnu.idatt2106.smartmat.repository.statistic;

import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for the FoodProductHistory entity
 * @author Callum G.
 * @version 1.0 28.04.2023
 */
@Repository
public interface FoodProductHistoryRepository extends JpaRepository<FoodProductHistory, UUID> {
  /**
   * Method to get all food product history by household id
   * @param householdId The household id
   * @return A collection of food product history
   */
  @Query("SELECT fph FROM FoodProductHistory fph WHERE fph.household.id = :householdId")
  Optional<Collection<FoodProductHistory>> findAllByHouseholdId(UUID householdId);

  /**
   * Method to get all food product history by food product id
   * @param foodProductId The food product id
   * @return A collection of food product history
   */
  @Query("SELECT fph FROM FoodProductHistory fph WHERE fph.foodProduct.id = :foodProductId")
  Optional<Collection<FoodProductHistory>> findAllByFoodProductId(Long foodProductId);

  /**
   * Method to get all food product history by household id and food product id
   * @param householdId The household id
   * @param foodProductId The food product id
   * @return A collection of food product history
   */
  @Query(
    "SELECT fph FROM FoodProductHistory fph WHERE fph.household.id = :householdId AND fph.foodProduct.id = :foodProductId"
  )
  Optional<Collection<FoodProductHistory>> findAllByHouseholdIdAndFoodProductId(
    UUID householdId,
    Long foodProductId
  );

  /**
   * Method to get all food product history by household id and between two dates
   * @param householdId The household id
   * @param begin The begin date
   * @param end The end date
   * @return A collection of food product history
   */
  @Query(
    "SELECT fph FROM FoodProductHistory fph WHERE fph.household.id = :householdId AND fph.date BETWEEN :begin AND :end"
  )
  Optional<Collection<FoodProductHistory>> findAllByHouseholdIdAndDateBetween(
    UUID householdId,
    LocalDate begin,
    LocalDate end
  );
}
