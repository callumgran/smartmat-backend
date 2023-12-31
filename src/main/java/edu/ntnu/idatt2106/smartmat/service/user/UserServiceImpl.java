package edu.ntnu.idatt2106.smartmat.service.user;

import edu.ntnu.idatt2106.smartmat.exceptions.DatabaseException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.EmailAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UsernameAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.WrongPasswordException;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import edu.ntnu.idatt2106.smartmat.repository.user.UserRepository;
import java.util.Collection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for user operations on the user repository.
 * This class is responsible for all business logic related to users.
 * Based on the user-service class from the IDATT2105 project.
 * @author Callum G.
 * @version 1.0 - 17.04.2023
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

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

    user.setPassword(PasswordService.hashPassword(user.getPassword()));

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
      throw new DatabaseException("Kan ikke slette bruker");
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
   * @throws WrongPasswordException if the old password is incorrect.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws NullPointerException if user is null.
   */
  @Override
  public User partialUpdate(
    @NonNull User user,
    String email,
    String firstName,
    String lastName,
    String oldPassword,
    String newPassword
  ) throws UserDoesNotExistsException, WrongPasswordException, NullPointerException {
    if (!userRepository.existsById(user.getUsername())) throw new UserDoesNotExistsException();
    if (email != null) user.setEmail(email);
    if (firstName != null) user.setFirstName(firstName);
    if (lastName != null) user.setLastName(lastName);

    if (oldPassword != null && newPassword != null) {
      if (PasswordService.checkPassword(oldPassword, user.getPassword())) user.setPassword(
        PasswordService.hashPassword(newPassword)
      ); else throw new WrongPasswordException();
    } else if (newPassword != null) throw new WrongPasswordException();
    return userRepository.save(user);
  }

  /**
   * Gets all users from the database.
   * @return a list of all users.
   * @throws DatabaseException if an error occurred while getting the users.
   */
  public Collection<User> getAllUsers() throws DatabaseException {
    Collection<User> users = userRepository.findAll();

    if (users.isEmpty()) throw new DatabaseException("Ingen brukere funnet");

    return users;
  }

  /**
   * Method to authenticate a user.
   * @param username the username of the user.
   * @param password the password of the user.
   * @return true if the user is authenticated.
   * @throws UserDoesNotExistsException if the user does not exist.
   * @throws WrongPasswordException if the password is incorrect.
   */
  @Override
  public boolean authenticateUser(String username, String password)
    throws UserDoesNotExistsException, WrongPasswordException {
    User user = getUserByUsername(username);

    if (!PasswordService.checkPassword(password, user.getPassword())) {
      throw new WrongPasswordException("Ugyldig passord");
    }

    return true;
  }
}
