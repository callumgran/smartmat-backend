package edu.ntnu.idatt2106.smartmat.unit.mapper;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdHelperFunctions.testHouseholdFactory;
import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.dto.household.HouseholdMemberDTO;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestHouseholdEnum;
import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.mapper.household.HouseholdMemberMapper;
import edu.ntnu.idatt2106.smartmat.mapper.household.HouseholdMemberMapperImpl;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for HouseholdMemberMapper.
 * @author Callum G.
 * @version 1.0 - 19.04.2023
 */
@RunWith(SpringRunner.class)
public class HouseholdMemberMapperTest {

  @TestConfiguration
  static class HouseholdMemberMapperTestContextConfiguration {

    @Bean
    public HouseholdMemberMapper householdMemberMapper() {
      return new HouseholdMemberMapperImpl();
    }
  }

  @Autowired
  private HouseholdMemberMapper householdMemberMapper;

  @MockBean
  private UserService userService;

  @MockBean
  private HouseholdService householdService;

  private User user;

  private Household household;

  private HouseholdMember member;

  @Before
  public void setUp() {
    user = testUserFactory(TestUserEnum.GOOD);

    household = testHouseholdFactory(TestHouseholdEnum.GOOD_HOUSEHOLD);

    member = new HouseholdMember();
    member.setHousehold(household);
    member.setUser(user);
    member.setHouseholdRole(HouseholdRole.MEMBER);

    try {
      when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
      when(householdService.getHouseholdById(household.getId())).thenReturn(household);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMapHouseholdMemberToHouseholdMemberDTO() {
    HouseholdMemberDTO memberDTO = null;
    try {
      memberDTO = householdMemberMapper.householdMemberToHouseholdMemberDTO(member);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    assertEquals(member.getHousehold().getId(), memberDTO.getHousehold());
    assertEquals(member.getUser().getUsername(), memberDTO.getUsername());
    assertEquals(member.getHouseholdRole(), memberDTO.getHouseholdRole());
  }

  @Test
  public void testMapHouseholdMemberDTOToHouseholdMember() {
    HouseholdMemberDTO memberDTO = new HouseholdMemberDTO();
    memberDTO.setHousehold(household.getId());
    memberDTO.setUsername(user.getUsername());
    memberDTO.setHouseholdRole(HouseholdRole.MEMBER);

    HouseholdMember member = null;
    try {
      member = householdMemberMapper.householdMemberDTOToHouseholdMember(memberDTO);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    assertEquals(memberDTO.getHousehold(), member.getHousehold().getId());
    assertEquals(memberDTO.getUsername(), member.getUser().getUsername());
    assertEquals(memberDTO.getHouseholdRole(), member.getHouseholdRole());
  }
}
