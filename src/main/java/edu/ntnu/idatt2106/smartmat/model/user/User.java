package edu.ntnu.idatt2106.smartmat.model.user;

import edu.ntnu.idatt2106.smartmat.model.household.HouseholdMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Class representing a user in the system.
 * Implements UserDetails to allow Spring Security to use this class for authentication.
 * @author Callum G.
 * @version 1.1 20.4.2023
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "`user`")
public class User implements UserDetails {

  @Id
  @Column(name = "`username`", length = 32, nullable = false)
  @NonNull
  private String username;

  @Column(name = "`email`", unique = true, length = 64, nullable = false)
  @NonNull
  private String email;

  @Column(name = "`first_name`", length = 64, nullable = false)
  @NonNull
  private String firstName;

  @Column(name = "`last_name`", length = 64, nullable = false)
  @NonNull
  private String lastName;

  @Column(name = "`password`", nullable = false)
  @NonNull
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "`role`", nullable = false)
  @NonNull
  private UserRole role;

  @OneToMany(mappedBy = "user")
  private Set<HouseholdMember> households;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return (
      "User{" +
      "username='" +
      username +
      '\'' +
      ", email='" +
      email +
      '\'' +
      ", firstName='" +
      firstName +
      '\'' +
      ", lastName='" +
      lastName +
      '\'' +
      ", password='" +
      password +
      '\'' +
      ", role=" +
      role +
      '}'
    );
  }
}
