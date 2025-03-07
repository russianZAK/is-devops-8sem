package project.web.entities;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Custom implementation of the UserDetails interface for representing the currently authenticated user.
 */
@Getter
@NoArgsConstructor
public class MyUserPrincipal implements UserDetails {
  private User user;

  /**
   * Constructs a new MyUserPrincipal instance with the provided User object.
   * @param user The User object representing the authenticated user.
   */
  public MyUserPrincipal(User user) {
    this.user = user;
  }

  /**
   * Returns the authorities granted to the user.
   * @return The collection of GrantedAuthority objects representing the user's authorities.
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles().stream().map(UserRole::getRole).collect(Collectors.toSet());
  }

  /**
   * Returns the user's password.
   * @return The user's password.
   */
  @Override
  public String getPassword() {
    return user.getPassword();
  }

  /**
   * Returns the user's username.
   * @return The user's username.
   */
  @Override
  public String getUsername() {
    return user.getUsername();
  }

  /**
   * Indicates whether the user's account has expired.
   * @return {@code true} if the user's account is non-expired, {@code false} otherwise.
   */
  @Override
  public boolean isAccountNonExpired() {
    return user.isAccountNonExpired();
  }

  /**
   * Indicates whether the user's account is locked.
   * @return {@code true} if the user's account is non-locked, {@code false} otherwise.
   */
  @Override
  public boolean isAccountNonLocked() {
    return user.isAccountNonLocked();
  }

  /**
   * Indicates whether the user's credentials (password) have expired.
   * @return {@code true} if the user's credentials are non-expired, {@code false} otherwise.
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return user.isCredentialsNonExpired();
  }

  /**
   * Indicates whether the user is enabled or disabled.
   * @return {@code true} if the user is enabled, {@code false} otherwise.
   */
  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }

}
