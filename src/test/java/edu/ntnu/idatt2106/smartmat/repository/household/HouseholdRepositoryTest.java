package edu.ntnu.idatt2106.smartmat.repository.household;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import java.util.Collection;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for the household repository.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class HouseholdRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private HouseholdRepository householdRepository;

  private Household household;

  @Before
  public void setUp() {
    household = testHouseholdFactory(TestHouseholdEnum.MANY_USERS);
    household.setId(null);
    household = entityManager.persist(household);
    entityManager.flush();
  }

  @Test
  public void findHouseholdMembersById() {
    Collection<HouseholdMember> householdMembers = null;
    try {
      householdMembers =
        householdRepository
          .findHouseholdMembersById(household.getId())
          .orElseThrow(RuntimeException::new);
    } catch (RuntimeException e) {
      fail(e.getMessage());
      return;
    }
    assertEquals(household.getMembers().size(), householdMembers.size());
  }

  @Test
  public void findHouseholdMembersByIdThatDoesNotExist() {
    Collection<HouseholdMember> householdMembers = null;
    try {
      householdMembers =
        householdRepository
          .findHouseholdMembersById(UUID.randomUUID())
          .orElseThrow(RuntimeException::new);
    } catch (RuntimeException e) {
      fail(e.getMessage());
      return;
    }
    assertEquals(0, householdMembers.size());
  }

  @Test
  public void findHouseholdMembersByIdAndRoleMember() {
    Collection<HouseholdMember> householdMembers = null;
    try {
      householdMembers =
        householdRepository
          .findHouseholdMembersWithRoleById(household.getId(), HouseholdRole.MEMBER)
          .orElseThrow(RuntimeException::new);
    } catch (RuntimeException e) {
      fail(e.getMessage());
      return;
    }
    assertEquals(5, householdMembers.size());
    assertTrue(
      householdMembers
        .stream()
        .allMatch(householdMember -> householdMember.getHouseholdRole().equals(HouseholdRole.MEMBER)
        )
    );
  }

  @Test
  public void findHouseholdMembersByIdAndRolePrivileged() {
    Collection<HouseholdMember> householdMembers = null;
    try {
      householdMembers =
        householdRepository
          .findHouseholdMembersWithRoleById(household.getId(), HouseholdRole.PRIVILEGED_MEMBER)
          .orElseThrow(RuntimeException::new);
    } catch (RuntimeException e) {
      fail(e.getMessage());
      return;
    }
    assertEquals(4, householdMembers.size());
    assertTrue(
      householdMembers
        .stream()
        .allMatch(householdMember ->
          householdMember.getHouseholdRole().equals(HouseholdRole.PRIVILEGED_MEMBER)
        )
    );
  }

  @Test
  public void findHouseholdOwnerById() {
    Collection<HouseholdMember> householdOwner = null;
    try {
      householdOwner =
        householdRepository
          .findHouseholdOwnerById(household.getId())
          .orElseThrow(RuntimeException::new);
    } catch (RuntimeException e) {
      fail(e.getMessage());
      return;
    }
    assertTrue(
      householdOwner
        .stream()
        .allMatch(householdMember -> householdMember.getHouseholdRole().equals(HouseholdRole.OWNER))
    );
  }

  @Test
  public void deleteHouseholdMemberByUsernameAndId() {
    try {
      householdRepository.deleteHouseholdMemberByIdAndUsername(
        household.getId(),
        ((HouseholdMember) household.getMembers().toArray()[9]).getUser().getUsername()
      );
    } catch (RuntimeException e) {
      fail(e.getMessage());
      return;
    }
    assertEquals(9, householdRepository.findHouseholdMembersById(household.getId()).get().size());
  }

  @Test
  public void deleteAllHouseholdMembersById() {
    try {
      householdRepository.deleteHouseholdMembersById(household.getId());
    } catch (RuntimeException e) {
      fail(e.getMessage());
      return;
    }
    assertEquals(0, householdRepository.findHouseholdMembersById(household.getId()).get().size());
  }

  @Test
  public void findAllByUsername() {
    Collection<Household> households = null;
    try {
      households =
        householdRepository
          .findAllByUsername(
            ((HouseholdMember) household.getMembers().toArray()[0]).getUser().getUsername()
          )
          .orElseThrow(RuntimeException::new);
    } catch (RuntimeException e) {
      fail(e.getMessage());
      return;
    }
    assertEquals(1, households.size());
  }
}
