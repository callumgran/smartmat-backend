package edu.ntnu.idatt2106.smartmat.repository.user;

import edu.ntnu.idatt2106.smartmat.model.user.User;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for user operations on the database.
 * Based on the user-repository interface from the IDATT2105 project.
 * @author Callum G.
 * @version 1.0 - 17.4.2023
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
  /**
   * Method to find a user by username.
   * @param username the username of the user.
   * @return the username.
   */
  Optional<User> findByUsername(@NonNull String username);

  /**
   * Method to find a user by email.
   * @param email email of user.
   * @return the email.
   */
  Optional<User> findByEmail(@NonNull String email);
}
