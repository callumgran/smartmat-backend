package edu.ntnu.idatt2106.smartmat.unit.mapper;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdDTO;
import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdMemberDTO;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.mapper.household.HouseholdMapper;
import edu.ntnu.idatt2106.smartmat.mapper.household.HouseholdMemberMapper;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import java.util.HashSet;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Class used to test the HouseholdMapper.
 * @author Callum G.
 * @version 1.0 - 19.04.2023
 */
public class HouseholdMapperTest {

  public static final UUID HOUSEHOLD_ID = UUID.fromString("f76f97b8-dea2-11ed-b5ea-0242ac120002");

  private User user;

  private HouseholdMember householdMember;

  private Household household;

  private HouseholdDTO householdDTO;

  /**
   * Class used to mock the HouseholdMemberMapper.
   */
  class HouseholdMemberMapperImplTest extends HouseholdMemberMapper {

    @Override
    public HouseholdMemberDTO householdMemberToHouseholdMemberDTO(HouseholdMember householdMember) {
      HouseholdMemberDTO householdMemberDTO = HouseholdMemberDTO
        .builder()
        .household(HOUSEHOLD_ID)
        .username(householdMember.getUser().getUsername())
        .firstName(householdMember.getUser().getFirstName())
        .lastName(householdMember.getUser().getLastName())
        .email(householdMember.getUser().getEmail())
        .householdRole(householdMember.getHouseholdRole())
        .build();
      return householdMemberDTO;
    }

    @Override
    public HouseholdMember householdMemberDTOToHouseholdMember(
      HouseholdMemberDTO householdMemberDTO
    ) {
      User user = testUserFactory(TestUserEnum.GOOD);
      Household household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
      household.setId(HOUSEHOLD_ID);
      HouseholdMember householdMember = HouseholdMember
        .builder()
        .household(household)
        .user(user)
        .householdRole(HouseholdRole.MEMBER)
        .build();

      return householdMember;
    }
  }

  @Before
  public void setUp() {
    user = testUserFactory(TestUserEnum.GOOD);

    household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);
    household.setId(HOUSEHOLD_ID);

    householdMember =
      HouseholdMember
        .builder()
        .household(household)
        .user(user)
        .householdRole(HouseholdRole.MEMBER)
        .build();

    household.getMembers().add(householdMember);

    householdDTO =
      HouseholdDTO
        .builder()
        .id(household.getId().toString())
        .name(household.getName())
        .members(new HashSet<>())
        .foodProducts(new HashSet<>())
        .build();

    HouseholdMemberDTO householdMemberDTO = HouseholdMemberDTO
      .builder()
      .household(HOUSEHOLD_ID)
      .username(householdMember.getUser().getUsername())
      .firstName(householdMember.getUser().getFirstName())
      .lastName(householdMember.getUser().getLastName())
      .email(householdMember.getUser().getEmail())
      .householdRole(householdMember.getHouseholdRole())
      .build();

    householdDTO.getMembers().add(householdMemberDTO);

    HouseholdMemberMapperImplTest householdMemberMapperImplTest = new HouseholdMemberMapperImplTest();
    ReflectionTestUtils.setField(
      HouseholdMapper.INSTANCE,
      "householdMemberMapper",
      householdMemberMapperImplTest
    );
  }

  @Test
  public void householdToHouseholdDTO() {
    HouseholdDTO householdDTO = HouseholdMapper.INSTANCE.householdToHouseholdDTO(household);
    assertEquals(household.getId().toString(), householdDTO.getId());
    assertEquals(household.getName(), householdDTO.getName());
    assertEquals(household.getMembers().size(), householdDTO.getMembers().size());
    assertEquals(household.getFoodProducts().size(), householdDTO.getFoodProducts().size());
  }

  @Test
  public void householdDTOToHousehold() {
    Household household = null;
    try {
      household = HouseholdMapper.INSTANCE.householdDTOToHousehold(householdDTO);
    } catch (Exception e) {
      fail(e.getMessage());
      return;
    }
    assertEquals(householdDTO.getId(), household.getId().toString());
    assertEquals(householdDTO.getName(), household.getName());
    assertEquals(householdDTO.getMembers().size(), household.getMembers().size());
  }
}
