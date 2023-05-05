package edu.ntnu.idatt2106.smartmat.integration.statistic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.statistic.FoodProductHistoryNotFoundException;
import edu.ntnu.idatt2106.smartmat.model.statistic.FoodProductHistory;
import edu.ntnu.idatt2106.smartmat.repository.statistic.FoodProductHistoryRepository;
import edu.ntnu.idatt2106.smartmat.service.statistic.FoodProductHistoryService;
import edu.ntnu.idatt2106.smartmat.service.statistic.FoodProductHistoryServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FoodProductHistoryIntegrationTest {

  @TestConfiguration
  static class UnitServiceIntegrationTestConfiguration {

    @Bean
    public FoodProductHistoryService foodProductHistoryService() {
      return new FoodProductHistoryServiceImpl();
    }
  }

  @Autowired
  private FoodProductHistoryService foodProductHistoryService;

  @MockBean
  private FoodProductHistoryRepository foodProductHistoryRepository;

  private final UUID id = UUID.randomUUID();

  private final UUID householdId = UUID.randomUUID();

  private final Long foodProductId = 1L;

  private FoodProductHistory foodProductHistory;

  @Before
  public void setUp() {
    foodProductHistory = new FoodProductHistory();
    foodProductHistory.setId(id);
  }

  @Test
  public void testExistsByIdExisting() {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(true);
    assertTrue(foodProductHistoryService.existsById(id));
  }

  @Test
  public void testExistsByIdNotExisting() {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(false);
    assertFalse(foodProductHistoryService.existsById(id));
  }

  @Test
  public void testGetFoodProductHistoryByIdExisting() throws Exception {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(true);
    when(foodProductHistoryRepository.findById(id)).thenReturn(Optional.of(foodProductHistory));
    assertEquals(
      foodProductHistory.getId(),
      foodProductHistoryService.getFoodProductHistoryById(id).getId()
    );
  }

  @Test
  public void testGetFoodProductHistoryByIdNotExisting() throws Exception {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(false);
    assertThrows(
      FoodProductHistoryNotFoundException.class,
      () -> foodProductHistoryService.getFoodProductHistoryById(id)
    );
  }

  @Test
  public void testSaveFoodProductHistory() {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(false);
    when(foodProductHistoryRepository.save(foodProductHistory)).thenReturn(foodProductHistory);
    assertEquals(
      foodProductHistory.getId(),
      foodProductHistoryService.saveFoodProductHistory(foodProductHistory).getId()
    );
  }

  @Test
  public void testSaveFoodProductHistoryAlreadyExists() {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(true);
    when(foodProductHistoryRepository.save(foodProductHistory)).thenReturn(foodProductHistory);
    assertThrows(
      IllegalArgumentException.class,
      () -> foodProductHistoryService.saveFoodProductHistory(foodProductHistory)
    );
  }

  @Test
  public void testSaveFoodProductHistoryThrowsNull() {
    assertThrows(
      NullPointerException.class,
      () -> foodProductHistoryService.saveFoodProductHistory(null)
    );
  }

  @Test
  public void testDeleteFoodProductHistoryExisting() throws Exception {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(true);
    doNothing().when(foodProductHistoryRepository).delete(foodProductHistory);
    assertDoesNotThrow(() -> foodProductHistoryService.deleteFoodProductHistory(foodProductHistory)
    );
  }

  @Test
  public void testDeleteFoodProductHistoryNotExisting() throws Exception {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(false);
    assertThrows(
      FoodProductHistoryNotFoundException.class,
      () -> foodProductHistoryService.deleteFoodProductHistory(foodProductHistory)
    );
  }

  @Test
  public void testDeleteFoodProductHistoryByIdExisting() throws Exception {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(true);
    doNothing().when(foodProductHistoryRepository).deleteById(id);
    assertDoesNotThrow(() -> foodProductHistoryService.deleteFoodProductHistoryById(id));
  }

  @Test
  public void testDeleteFoodProductHistoryByIdNotExisting() throws Exception {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(false);
    assertThrows(
      FoodProductHistoryNotFoundException.class,
      () -> foodProductHistoryService.deleteFoodProductHistoryById(id)
    );
  }

  @Test
  public void testUpdateFoodProductHistoryExisting() throws Exception {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(true);
    when(foodProductHistoryRepository.save(foodProductHistory)).thenReturn(foodProductHistory);
    assertEquals(
      foodProductHistory.getId(),
      foodProductHistoryService.updateFoodProductHistory(id, foodProductHistory).getId()
    );
  }

  @Test
  public void testUpdateFoodProductHistoryNotExisting() throws Exception {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(false);
    assertThrows(
      FoodProductHistoryNotFoundException.class,
      () -> foodProductHistoryService.updateFoodProductHistory(id, foodProductHistory)
    );
  }

  @Test
  public void testUpdateFoodProductHistoryThrowsNull() throws Exception {
    when(foodProductHistoryRepository.existsById(id)).thenReturn(true);
    assertThrows(
      NullPointerException.class,
      () -> foodProductHistoryService.updateFoodProductHistory(id, null)
    );
  }

  @Test
  public void testGetAllFoodProductHistory() {
    when(foodProductHistoryRepository.findAll()).thenReturn(List.of(foodProductHistory));
    assertEquals(
      foodProductHistory.getId(),
      foodProductHistoryService.getAllFoodProductHistory().stream().toList().get(0).getId()
    );
  }

  @Test
  public void testGetAllFoodProductHistoryEmpty() {
    when(foodProductHistoryRepository.findAll()).thenReturn(List.of());
    assertTrue(foodProductHistoryService.getAllFoodProductHistory().isEmpty());
  }

  @Test
  public void testGetAllFoodProductHistoryByFoodProductId() {
    when(foodProductHistoryRepository.findAllByFoodProductId(foodProductId))
      .thenReturn(Optional.of(List.of(foodProductHistory)));
    assertEquals(
      foodProductHistory.getId(),
      foodProductHistoryService
        .getAllFoodProductHistoryByFoodProductId(foodProductId)
        .stream()
        .toList()
        .get(0)
        .getId()
    );
  }

  @Test
  public void testGetAllFoodProductHistoryByHouseholdIdAndFoodProductId() {
    when(
      foodProductHistoryRepository.findAllByHouseholdIdAndFoodProductId(householdId, foodProductId)
    )
      .thenReturn(Optional.of(List.of(foodProductHistory)));
    assertEquals(
      foodProductHistory.getId(),
      foodProductHistoryService
        .getAllFoodProductHistoryByHouseholdIdAndFoodProductId(householdId, foodProductId)
        .stream()
        .toList()
        .get(0)
        .getId()
    );
  }
}
