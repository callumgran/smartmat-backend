package edu.ntnu.idatt2106.smartmat.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import edu.ntnu.idatt2106.smartmat.service.user.PasswordService;
import org.junit.Test;

public class PasswordServiceTest {

  @Test
  public void testHashPassword() {
    assertNotEquals(PasswordService.hashPassword("password"), "password");
    assertEquals(PasswordService.hashPassword("password").split(":").length, 2);
  }

  @Test
  public void testHashPasswordThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> PasswordService.hashPassword(null));
  }

  @Test
  public void testHashPasswordThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> PasswordService.hashPassword(""));
  }

  @Test
  public void testCheckPassword() {
    String password = "password";
    String hashedPassword = PasswordService.hashPassword(password);
    assertTrue(PasswordService.checkPassword(password, hashedPassword));
  }

  @Test
  public void testCheckPasswordThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> PasswordService.checkPassword(null, null));
  }

  @Test
  public void testCheckPasswordThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> PasswordService.checkPassword("", ""));
  }
}
