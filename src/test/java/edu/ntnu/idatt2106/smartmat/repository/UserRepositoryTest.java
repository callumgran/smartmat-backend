package edu.ntnu.idatt2106.smartmat.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2106.smartmat.model.user.Role;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.repository.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for the user repository.
 * Based on the user-repository-test class from the IDATT2105 project.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testFindByUsername() {
    User user = new User("username", "email", "firstName", "lastName", "password", Role.USER);
    entityManager.persist(user);
    entityManager.flush();

    User found = userRepository.findByUsername(user.getUsername()).get();

    assertEquals(user.getUsername(), found.getUsername());
  }

  @Test
  public void testFindByUserNameThatDoesNotExist() {
    assertFalse(userRepository.findByUsername("username").isPresent());
  }

  @Test
  public void testFindByEmail() {
    User user = new User("username", "email", "firstName", "lastName", "password", Role.USER);
    entityManager.persist(user);
    entityManager.flush();

    User found = userRepository.findByEmail(user.getEmail()).get();

    assertEquals(user.getEmail(), found.getEmail());
  }

  @Test
  public void testFindByEmailThatDoesNotExist() {
    assertFalse(userRepository.findByEmail("email").isPresent());
  }
}
