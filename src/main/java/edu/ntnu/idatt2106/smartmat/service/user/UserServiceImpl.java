package edu.ntnu.idatt2106.smartmat.service.user;

import edu.ntnu.idatt2106.smartmat.exceptions.DatabaseException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.EmailAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UsernameAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.repository.user.UserRepository;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * Service class for user operations on the user repository.
 * This class is responsible for all business logic related to users.
 * @author Callum G.
 * @version 1.0 - 17.04.2023
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  /**
   * Checks if a user with the given username exists.
   * @param username the username to check.
   * @return predicate if user exists.
   * @throws NullPointerException if username is null
   */
  public boolean usernameExists(@NonNull String username) throws NullPointerException {
    return userRepository.findByUsername(username).isPresent();
  }

  /**
   * Checks if a user with the given email exists.
   * @param email the email to check.
   * @return predicate if user exists.
   * @throws NullPointerException if email is null
   */
  public boolean emailExists(@NonNull String email) throws NullPointerException {
    return userRepository.findByEmail(email).isPresent();
  }

  /**
   * Gets the user with the given username.
   * @param username the username to check.
   * @return user with the given username.
   * @throws UserDoesNotExistsException if user does not exist.
   */
  public User getUserByUsername(@NonNull String username) throws UserDoesNotExistsException {
    return userRepository.findByUsername(username).orElseThrow(UserDoesNotExistsException::new);
  }

  /**
   * Gets the user with the given email.
   * @param email the email to check.
   * @return user with the given email.
   * @throws UserDoesNotExistsException if user does not exist.
   */
  public User getUserByEmail(@NonNull String email) throws UserDoesNotExistsException {
    return userRepository.findByEmail(email).orElseThrow(UserDoesNotExistsException::new);
  }

  /**
   * Saves a user to the database.
   * @param user the user to save.
   * @return the saved user.
   * @throws UsernameAlreadyExistsException if a user with the given username already exists.
   * @throws EmailAlreadyExistsException if a user with the given email already exists.
   * @throws DatabaseException if an error occurred while saving the user.
   * @throws NullPointerException if user is null.
   */
  public User saveUser(@NonNull User user)
    throws UsernameAlreadyExistsException, EmailAlreadyExistsException, DatabaseException, NullPointerException {
    if (usernameExists(user.getUsername())) throw new UsernameAlreadyExistsException();

    if (emailExists(user.getEmail())) throw new EmailAlreadyExistsException();

    user.setPassword(hashPassword(user.getPassword()));

    return userRepository.save(user);
  }

  /**
   * Deletes a user from the database.
   * @param user the user to delete.
   * @throws DatabaseException if an error occurred while deleting the user.
   * @throws NullPointerException if user is null.
   */
  public void deleteUser(@NonNull User user) throws DatabaseException {
    try {
      userRepository.delete(user);
    } catch (Exception e) {
      throw new DatabaseException("canNotDeleteUser");
    }
  }

  /**
   * Deletes a user from the database by username.
   * @param username the username of the user to delete.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws NullPointerException if username is null.
   */
  public void deleteUserByUsername(@NonNull String username) throws UserDoesNotExistsException {
    userRepository.delete(getUserByUsername(username));
  }

  /**
   * Deletes a user from the database by email.
   * @param email the email of the user to delete.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws NullPointerException if email is null.
   */
  public void deleteUserByEmail(@NonNull String email) throws UserDoesNotExistsException {
    userRepository.delete(getUserByEmail(email));
  }

  /**
   * Updates a user in the database.
   * @param user the user to update.
   * @return the updated user.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws NullPointerException if user is null.
   */
  public User updateUser(@NonNull User user) throws UserDoesNotExistsException {
    if (!userRepository.existsById(user.getUsername())) throw new UserDoesNotExistsException();
    user.setPassword(hashPassword(user.getPassword()));

    return userRepository.save(user);
  }

  /**
   * Method to partially update a user.
   * @param user the user to update.
   * @param email the new email.
   * @param firstName the new first name.
   * @param lastName the new last name.
   * @param oldPassword the old password.
   * @param newPassword the new password.
   * @return the updated user.
   * @throws BadCredentialsException if the old password is incorrect.
   */
  @Override
  public User partialUpdate(
    @NonNull User user,
    String email,
    String firstName,
    String lastName,
    String oldPassword,
    String newPassword
  ) throws BadCredentialsException {
    if (email != null) user.setEmail(email);
    if (firstName != null) user.setFirstName(firstName);
    if (lastName != null) user.setLastName(lastName);

    if (oldPassword != null && newPassword != null) {
      if (checkPassword(oldPassword, user.getPassword())) user.setPassword(
        hashPassword(newPassword)
      ); else throw new BadCredentialsException("Invalid password");
    } else if (newPassword != null) throw new BadCredentialsException(
      "Old password is required to update password"
    );
    return userRepository.save(user);
  }

  /**
   * Gets all users from the database.
   * @return a list of all users.
   * @throws DatabaseException if an error occurred while getting the users.
   */
  public List<User> getAllUsers() throws DatabaseException {
    List<User> users = userRepository.findAll();

    if (users.isEmpty()) throw new DatabaseException("No users found in the database.");

    return users;
  }

  /**
   * Method to authenticate a user.
   * @param username the username of the user.
   * @param password the password of the user.
   * @return true if the user is authenticated.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws BadCredentialsException if the password is incorrect.
   */
  @Override
  public boolean authenticateUser(String username, String password)
    throws UserDoesNotExistsException, BadCredentialsException {
    User user = getUserByUsername(username);
    if (user == null) {
      throw new UserDoesNotExistsException();
    }
    if (!checkPassword(password, user.getPassword())) {
      throw new BadCredentialsException("Invalid password");
    }

    return true;
  }

  /**
   * Method to hash a password.
   * @param password the password to hash.
   * @return the hashed password.
   */
  public static String hashPassword(String password) {
    String salt = BCrypt.gensalt(); // generate a random salt value
    return salt + ":" + BCrypt.hashpw(password, salt);
  }

  /**
   * Method to check a password.
   * @param password the password to check.
   * @param hashedPassword the hashed password to check against.
   * @return true if the password is correct.
   */
  private boolean checkPassword(String password, String hashedPassword) {
    logger.info(password + " -> " + hashedPassword);
    String[] parts = hashedPassword.split(":");
    String salt = parts[0];
    String hashedPasswordFromDB = parts[1];
    String hashedPasswordToCheck = BCrypt.hashpw(password, salt);
    logger.info(hashedPasswordToCheck + " == " + hashedPasswordFromDB);
    return hashedPasswordFromDB.equals(hashedPasswordToCheck);
  }
}
