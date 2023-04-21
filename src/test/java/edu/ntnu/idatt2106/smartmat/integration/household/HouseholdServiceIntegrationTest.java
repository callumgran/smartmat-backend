package edu.ntnu.idatt2106.smartmat.integration.household;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
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
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.repository.household.HouseholdMemberRepository;
import edu.ntnu.idatt2106.smartmat.repository.household.HouseholdRepository;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdServiceImpl;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
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
  private UserService userService;

  @MockBean
  private HouseholdRepository householdRepository;

  @MockBean
  private HouseholdMemberRepository householdMemberRepository;

  private Household existingHousehold;

  private Household corruptedHousehold;

  private Household newHousehold;

  private HouseholdMember owner;

  private HouseholdMember member;

  private HouseholdMember privelegedMember;

  @Before
  public void setUp() {
    existingHousehold = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
    corruptedHousehold = testHouseholdFactory(TestHouseholdEnum.BAD_HOUSEHOLD);
    newHousehold = testHouseholdFactory(TestHouseholdEnum.NEW_HOUSEHOLD);

    owner = new HouseholdMember();
    owner.setHousehold(existingHousehold);
    owner.setUser(testUserFactory(TestUserEnum.GOOD));
    owner.setHouseholdRole(HouseholdRole.OWNER);

    member = new HouseholdMember();
    member.setHousehold(existingHousehold);
    member.setUser(testUserFactory(TestUserEnum.BAD));
    member.setHouseholdRole(HouseholdRole.MEMBER);

    privelegedMember = new HouseholdMember();
    privelegedMember.setHousehold(existingHousehold);
    privelegedMember.setUser(testUserFactory(TestUserEnum.UPDATE));
    privelegedMember.setHouseholdRole(HouseholdRole.PRIVILEGED_MEMBER);

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

    when(userService.usernameExists(member.getUser().getUsername())).thenReturn(true);
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
      tmp = householdService.getHouseholdById(existingHousehold.getId());
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
      () -> householdService.getHouseholdById(corruptedHousehold.getId())
    );
  }

  @Test
  public void getHouseholdNewHousehold() {
    assertThrows(
      NullPointerException.class,
      () -> householdService.getHouseholdById(newHousehold.getId())
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

  @Test
  public void getHouseholdOwnerExistingHousehold() {
    when(householdRepository.findHouseholdOwnerById(existingHousehold.getId()))
      .thenReturn(Optional.of(Set.of(owner)));
    HouseholdMember tmp = null;
    try {
      tmp = householdService.getHouseholdOwner(existingHousehold.getId());
    } catch (Exception e) {
      fail();
      return;
    }

    assertEquals(owner.getUser().getUsername(), tmp.getUser().getUsername());
    assertEquals(owner.getHousehold().getId(), tmp.getHousehold().getId());
    assertEquals(owner.getHouseholdRole(), tmp.getHouseholdRole());
  }

  @Test
  public void getHouseholdOwnerCorruptedHousehold() {
    when(householdRepository.findHouseholdOwnerById(corruptedHousehold.getId()))
      .thenReturn(Optional.empty());
    assertThrows(
      HouseholdNotFoundException.class,
      () -> householdService.getHouseholdOwner(corruptedHousehold.getId())
    );
  }

  @Test
  public void getHouseholdMembersExistingHousehold() {
    when(householdRepository.findHouseholdMembersById(existingHousehold.getId()))
      .thenReturn(Optional.of(Set.of(owner, member, privelegedMember)));
    Collection<HouseholdMember> tmp = null;
    try {
      tmp = householdService.getHouseholdMembers(existingHousehold.getId());
    } catch (Exception e) {
      fail();
      return;
    }

    assertEquals(3, tmp.size());
    assertTrue(tmp.contains(owner));
    assertTrue(tmp.contains(member));
    assertTrue(tmp.contains(privelegedMember));
  }

  @Test
  public void getHouseholdMembersCorruptedHousehold() {
    when(householdRepository.findHouseholdMembersById(corruptedHousehold.getId()))
      .thenReturn(Optional.empty());
    assertThrows(
      HouseholdNotFoundException.class,
      () -> householdService.getHouseholdMembers(corruptedHousehold.getId())
    );
  }

  @Test
  public void getHouseholdMembersWithRoleExistingHousehold() {
    when(
      householdRepository.findHouseholdMembersWithRoleById(
        existingHousehold.getId(),
        HouseholdRole.MEMBER
      )
    )
      .thenReturn(Optional.of(Set.of(member)));
    Collection<HouseholdMember> tmp = null;
    try {
      tmp =
        householdService.getHouseholdMembersWithRole(
          existingHousehold.getId(),
          HouseholdRole.MEMBER
        );
    } catch (Exception e) {
      fail();
      return;
    }

    assertEquals(1, tmp.size());
    assertTrue(tmp.contains(member));
  }

  @Test
  public void getHouseholdMembersWithRoleCorruptedHousehold() {
    when(
      householdRepository.findHouseholdMembersWithRoleById(
        corruptedHousehold.getId(),
        HouseholdRole.MEMBER
      )
    )
      .thenReturn(Optional.empty());
    assertThrows(
      HouseholdNotFoundException.class,
      () ->
        householdService.getHouseholdMembersWithRole(
          corruptedHousehold.getId(),
          HouseholdRole.MEMBER
        )
    );
  }

  @Test
  public void isHouseholdOwnerExistingHousehold() {
    when(householdRepository.findHouseholdOwnerById(existingHousehold.getId()))
      .thenReturn(Optional.of(Set.of(owner)));
    try {
      assertTrue(
        householdService.isHouseholdOwner(existingHousehold.getId(), owner.getUser().getUsername())
      );
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void isHouseholdOwnerCorruptedHousehold() {
    when(householdRepository.findHouseholdOwnerById(corruptedHousehold.getId()))
      .thenReturn(Optional.empty());
    assertThrows(
      HouseholdNotFoundException.class,
      () ->
        householdService.isHouseholdOwner(corruptedHousehold.getId(), owner.getUser().getUsername())
    );
  }

  @Test
  public void isHouseholdMemberExistingHousehold() {
    when(householdRepository.findHouseholdMembersById(existingHousehold.getId()))
      .thenReturn(Optional.of(Set.of(owner, member, privelegedMember)));
    try {
      assertTrue(
        householdService.isHouseholdMember(
          existingHousehold.getId(),
          member.getUser().getUsername()
        )
      );
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void isHouseholdMemberCorruptedHousehold() {
    when(householdRepository.findHouseholdMembersById(corruptedHousehold.getId()))
      .thenReturn(Optional.empty());
    assertThrows(
      HouseholdNotFoundException.class,
      () ->
        householdService.isHouseholdMember(
          corruptedHousehold.getId(),
          member.getUser().getUsername()
        )
    );
  }

  @Test
  public void isHouseholdMemberWithRoleExistingHousehold() {
    when(
      householdRepository.findHouseholdMembersWithRoleById(
        existingHousehold.getId(),
        HouseholdRole.MEMBER
      )
    )
      .thenReturn(Optional.of(Set.of(member)));
    try {
      assertTrue(
        householdService.isHouseholdMemberWithRole(
          existingHousehold.getId(),
          member.getUser().getUsername(),
          HouseholdRole.MEMBER
        )
      );
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void isHouseholdMemberWithRoleCorruptedHousehold() {
    when(
      householdRepository.findHouseholdMembersWithRoleById(
        corruptedHousehold.getId(),
        HouseholdRole.MEMBER
      )
    )
      .thenReturn(Optional.empty());
    assertThrows(
      HouseholdNotFoundException.class,
      () ->
        householdService.isHouseholdMemberWithRole(
          corruptedHousehold.getId(),
          member.getUser().getUsername(),
          HouseholdRole.MEMBER
        )
    );
  }

  @Test
  public void deleteHouseholdMemberExistingHousehold() {
    doNothing()
      .when(householdRepository)
      .deleteHouseholdMemberByIdAndUsername(
        existingHousehold.getId(),
        member.getUser().getUsername()
      );
    assertDoesNotThrow(() ->
      householdService.deleteHouseholdMember(
        existingHousehold.getId(),
        member.getUser().getUsername()
      )
    );
  }

  @Test
  public void deleteHouseholdMemberCorruptedHousehold() {
    doNothing()
      .when(householdRepository)
      .deleteHouseholdMemberByIdAndUsername(
        corruptedHousehold.getId(),
        member.getUser().getUsername()
      );
    assertThrows(
      HouseholdNotFoundException.class,
      () ->
        householdService.deleteHouseholdMember(
          corruptedHousehold.getId(),
          member.getUser().getUsername()
        )
    );
  }

  @Test
  public void deleteAllHouseholdMembersExistingHousehold() {
    doNothing().when(householdRepository).deleteHouseholdMembersById(existingHousehold.getId());
    assertDoesNotThrow(() -> householdService.deleteAllHouseholdMembers(existingHousehold.getId()));
  }

  @Test
  public void deleteAllHouseholdMembersCorruptedHousehold() {
    doNothing().when(householdRepository).deleteHouseholdMembersById(corruptedHousehold.getId());
    assertThrows(
      HouseholdNotFoundException.class,
      () -> householdService.deleteAllHouseholdMembers(corruptedHousehold.getId())
    );
  }

  @Test
  public void getHouseholdsByUser() {
    when(householdRepository.findAllByUsername(member.getUser().getUsername()))
      .thenReturn(Optional.of(Set.of(existingHousehold, newHousehold)));
    Collection<Household> tmp = null;
    try {
      tmp = householdService.getHouseholdsByUser(member.getUser().getUsername());
    } catch (Exception e) {
      fail();
      return;
    }

    assertEquals(2, tmp.size());
    assertTrue(tmp.contains(existingHousehold));
    assertTrue(tmp.contains(newHousehold));
  }
}
