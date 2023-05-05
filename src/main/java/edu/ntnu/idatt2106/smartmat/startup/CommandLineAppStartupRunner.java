package edu.ntnu.idatt2106.smartmat.startup;

import edu.ntnu.idatt2106.smartmat.dto.user.RegisterDTO;
import edu.ntnu.idatt2106.smartmat.mapper.user.RegisterMapper;
import edu.ntnu.idatt2106.smartmat.model.household.Household;
import edu.ntnu.idatt2106.smartmat.model.household.HouseholdRole;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import edu.ntnu.idatt2106.smartmat.service.household.HouseholdService;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Used to create an admin user if one does not exist.
 * This is done to make sure that there is always an admin user.
 * NOTE: Should not be a part of production.
 *
 * @author Nicolai H. Brand.
 * @version 1.0 - 25.04.2021
 */
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

  @Autowired
  private UserService userService;

  @Autowired
  private HouseholdService householdService;

  @Override
  public void run(String... args) throws Exception {
    if (!userService.usernameExists("admin")) {
      RegisterDTO admin = new RegisterDTO("admin", "admin", "admin", "admin", "admin");
      RegisterDTO privelegedUser = new RegisterDTO(
        "priveleged",
        "priveleged",
        "priveleged",
        "priveleged",
        "priveleged"
      );
      RegisterDTO normalUser = new RegisterDTO("normal", "normal", "normal", "normal", "normal");
      User user = RegisterMapper.INSTANCE.registerDTOtoUser(admin);
      user.setRole(UserRole.ADMIN);
      user = userService.saveUser(user);
      User user2 = RegisterMapper.INSTANCE.registerDTOtoUser(privelegedUser);
      user2.setRole(UserRole.USER);
      user2 = userService.saveUser(user2);
      User user3 = RegisterMapper.INSTANCE.registerDTOtoUser(normalUser);
      user3.setRole(UserRole.USER);
      user3 = userService.saveUser(user3);
      Household household = Household
        .builder()
        .name("Admin Household")
        .members(new HashSet<>())
        .build();
      household = householdService.saveHousehold(household);
      householdService.addHouseholdMember(
        household.getId(),
        user.getUsername(),
        HouseholdRole.OWNER
      );
      householdService.addHouseholdMember(
        household.getId(),
        user2.getUsername(),
        HouseholdRole.MEMBER
      );
      householdService.addHouseholdMember(
        household.getId(),
        user3.getUsername(),
        HouseholdRole.MEMBER
      );
    }
  }
}
