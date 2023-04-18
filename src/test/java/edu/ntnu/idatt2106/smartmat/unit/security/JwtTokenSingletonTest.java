package edu.ntnu.idatt2106.smartmat.unit.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import edu.ntnu.idatt2106.smartmat.security.JwtTokenSingleton;
import org.junit.Test;

public class JwtTokenSingletonTest {

  @Test
  public void testGetInstance() {
    JwtTokenSingleton jwtTokenSingleton = JwtTokenSingleton.getInstance();
    assertNotNull(jwtTokenSingleton);
  }

  @Test
  public void testGetJwtTokenSecret() {
    JwtTokenSingleton jwtTokenSingleton = JwtTokenSingleton.getInstance();
    assertNotNull(jwtTokenSingleton.getJwtTokenSecret());
  }

  @Test
  public void testGetJwtTokenSecretIsSame() {
    JwtTokenSingleton jwtTokenSingleton = JwtTokenSingleton.getInstance();
    JwtTokenSingleton jwtTokenSingleton2 = JwtTokenSingleton.getInstance();
    assertEquals(jwtTokenSingleton.getJwtTokenSecret(), jwtTokenSingleton2.getJwtTokenSecret());
  }

  @Test
  public void testHashCodeIsTheSame() {
    JwtTokenSingleton jwtTokenSingleton = JwtTokenSingleton.getInstance();
    JwtTokenSingleton jwtTokenSingleton2 = JwtTokenSingleton.getInstance();
    assertEquals(jwtTokenSingleton.hashCode(), jwtTokenSingleton2.hashCode());
  }
}
