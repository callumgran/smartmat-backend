package edu.ntnu.idatt2106.smartmat.startup;

import edu.ntnu.idatt2106.smartmat.dto.user.RegisterDTO;
import edu.ntnu.idatt2106.smartmat.mapper.user.RegisterMapper;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.model.user.UserRole;
import edu.ntnu.idatt2106.smartmat.service.user.UserService;
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

  @Override
  public void run(String... args) throws Exception {
    if (!userService.usernameExists("admin")) {
      RegisterDTO admin = new RegisterDTO("admin", "admin", "admin", "admin", "admin");
      User user = RegisterMapper.INSTANCE.registerDTOtoUser(admin);
      user.setRole(UserRole.ADMIN);
      userService.saveUser(user);
    }
  }
}
