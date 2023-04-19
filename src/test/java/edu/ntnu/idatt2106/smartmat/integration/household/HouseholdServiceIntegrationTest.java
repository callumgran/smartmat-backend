package edu.ntnu.idatt2106.smartmat.integration.household;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.household.HouseholdNotFoundException;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.repository.household.HouseholdRepository;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdServiceImpl;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test for the household service.
 * @author Callum G.
 * @version 1.0 - 19.4.2023
 */
@RunWith(SpringRunner.class)
public class HouseholdServiceIntegrationTest {

  @TestConfiguration
  static class HouseholdServiceIntegrationTestConfiguration {

    @Bean
    public HouseholdService householdService() {
      return new HouseholdServiceImpl();
    }
  }

  @Autowired
  private HouseholdService householdService;

  @MockBean
  private HouseholdRepository householdRepository;

  private Household existingHousehold;

  private Household corruptedHousehold;

  private Household newHousehold;

  @Before
  public void setUp() {
    existingHousehold = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
    corruptedHousehold = testHouseholdFactory(TestHouseholdEnum.BAD_HOUSEHOLD);
    newHousehold = testHouseholdFactory(TestHouseholdEnum.NEW_HOUSEHOLD);

    when(householdRepository.existsById(existingHousehold.getId())).thenReturn(true);
    when(householdRepository.existsById(corruptedHousehold.getId())).thenReturn(false);
    when(householdRepository.existsById(newHousehold.getId())).thenReturn(false);

    when(householdRepository.save(existingHousehold)).thenReturn(existingHousehold);
    when(householdRepository.save(corruptedHousehold)).thenReturn(corruptedHousehold);
    when(householdRepository.save(newHousehold)).thenReturn(newHousehold);

    when(householdRepository.findById(existingHousehold.getId()))
      .thenReturn(Optional.of(existingHousehold));
    when(householdRepository.findById(corruptedHousehold.getId())).thenReturn(Optional.empty());
    when(householdRepository.findById(newHousehold.getId())).thenReturn(Optional.empty());

    doNothing().when(householdRepository).delete(existingHousehold);
    doNothing().when(householdRepository).delete(corruptedHousehold);
    doNothing().when(householdRepository).delete(newHousehold);

    doNothing().when(householdRepository).deleteById(existingHousehold.getId());
    doNothing().when(householdRepository).deleteById(corruptedHousehold.getId());
    doNothing().when(householdRepository).deleteById(newHousehold.getId());
  }

  @Test
  public void householdExistsExistingHousehold() {
    assertTrue(householdService.householdExists(existingHousehold.getId()));
  }

  @Test
  public void householdExistsCorruptedHousehold() {
    assertFalse(householdService.householdExists(corruptedHousehold.getId()));
  }

  @Test
  public void householdExistsNewHousehold() {
    assertThrows(
      NullPointerException.class,
      () -> householdService.householdExists(newHousehold.getId())
    );
  }

  @Test
  public void createHouseholdExistingHousehold() {
    assertThrows(
      HouseholdAlreadyExistsException.class,
      () -> householdService.saveHousehold(existingHousehold)
    );
  }

  @Test
  public void createHouseholdCorruptedHousehold() {
    assertThrows(
      HouseholdAlreadyExistsException.class,
      () -> householdService.saveHousehold(corruptedHousehold)
    );
  }

  @Test
  public void createHouseholdNewHousehold() {
    assertDoesNotThrow(() -> householdService.saveHousehold(newHousehold));
  }

  @Test
  public void getHouseholdExistingHousehold() {
    Household tmp = null;
    try {
      tmp = householdService.getHouseHoldById(existingHousehold.getId());
    } catch (HouseholdNotFoundException e) {
      fail();
      return;
    }

    assertEquals(existingHousehold.getId(), tmp.getId());
    assertEquals(existingHousehold.getName(), tmp.getName());
    assertEquals(existingHousehold.getMembers().size(), tmp.getMembers().size());
  }

  @Test
  public void getHouseholdCorruptedHousehold() {
    assertThrows(
      HouseholdNotFoundException.class,
      () -> householdService.getHouseHoldById(corruptedHousehold.getId())
    );
  }

  @Test
  public void getHouseholdNewHousehold() {
    assertThrows(
      NullPointerException.class,
      () -> householdService.getHouseHoldById(newHousehold.getId())
    );
  }

  @Test
  public void deleteHouseholdExistingHousehold() {
    assertDoesNotThrow(() -> householdService.deleteHousehold(existingHousehold));
  }

  @Test
  public void deleteHouseholdCorruptedHousehold() {
    assertThrows(
      HouseholdNotFoundException.class,
      () -> householdService.deleteHousehold(corruptedHousehold)
    );
  }

  @Test
  public void deleteHouseholdNewHousehold() {
    assertThrows(NullPointerException.class, () -> householdService.deleteHousehold(newHousehold));
  }

  @Test
  public void deleteHouseholdByIdExistingHousehold() {
    assertDoesNotThrow(() -> householdService.deleteHouseholdById(existingHousehold.getId()));
  }

  @Test
  public void deleteHouseholdByIdCorruptedHousehold() {
    assertThrows(
      HouseholdNotFoundException.class,
      () -> householdService.deleteHouseholdById(corruptedHousehold.getId())
    );
  }

  @Test
  public void deleteHouseholdByIdNewHousehold() {
    assertThrows(
      NullPointerException.class,
      () -> householdService.deleteHouseholdById(newHousehold.getId())
    );
  }
}
