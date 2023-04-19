package edu.ntnu.idatt2106.smartmat.repository.user;

import static edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserHelperFunctions.testUserFactory;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2106.smartmat.helperfunctions.TestUserEnum;
import edu.ntnu.idatt2106.smartmat.model.user.User;
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
    User user = testUserFactory(TestUserEnum.GOOD);
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
    User user = testUserFactory(TestUserEnum.GOOD);
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
