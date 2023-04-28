package edu.ntnu.idatt2106.smartmat.repository.statistic;

import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the FoodProductHistory entity
 * @author Callum G.
 * @version 1.0 28.04.2023
 */
@Repository
public interface FoodProductHistoryRepository extends JpaRepository<FoodProductHistory, UUID> {}
