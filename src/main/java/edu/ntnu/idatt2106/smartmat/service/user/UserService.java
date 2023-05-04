package edu.ntnu.idatt2106.smartmat.service.user;

import edu.ntnu.idatt2106.smartmat.exceptions.DatabaseException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.EmailAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UserDoesNotExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.UsernameAlreadyExistsException;
import edu.ntnu.idatt2106.smartmat.exceptions.user.WrongPasswordException;
import edu.ntnu.idatt2106.smartmat.model.user.User;
import java.util.Collection;
import org.springframework.stereotype.Service;

/**
 * Interface for the user service.
 * Based on the user-service interface from the IDATT2105 project.
 * @author Callum G.
 * @version 1.0 - 17.4.2023
 */
@Service
public interface UserService {
  public boolean usernameExists(String username) throws NullPointerException;

  public boolean emailExists(String email) throws NullPointerException;

  public User getUserByUsername(String username) throws UserDoesNotExistsException;

  public User getUserByEmail(String email) throws UserDoesNotExistsException;

  public User saveUser(User user)
    throws UsernameAlreadyExistsException, EmailAlreadyExistsException, DatabaseException;

  public void deleteUser(User user) throws DatabaseException;

  public void deleteUserByUsername(String username) throws UserDoesNotExistsException;

  public void deleteUserByEmail(String email) throws UserDoesNotExistsException;

  public User updateUser(User user) throws UserDoesNotExistsException;

  public User partialUpdate(
    User user,
    String email,
    String firstName,
    String lastName,
    String oldPassword,
    String newPassword
  ) throws UserDoesNotExistsException, WrongPasswordException, NullPointerException;

  public Collection<User> getAllUsers() throws DatabaseException;

  public boolean authenticateUser(String username, String password)
    throws UserDoesNotExistsException, WrongPasswordException;
}
