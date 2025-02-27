package com.ourspace.backend.domain.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implementation of UserDetails interface.
 *
 * @param user the user
 */
public record UserDetailsImpl(User user) implements UserDetails {

  public User getUser() {
    return user;
  }

  /**
   * Returns the authorities granted to the user.
   *
   * @return the authorities granted to the user
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles()
        .stream()
        .flatMap(r -> r.getAuthorities()
            .stream())
        .map(a -> new SimpleGrantedAuthority(a.getName()))
        .toList();
  }

  /**
   * Returns the password used to authenticate the user.
   *
   * @return the password used to authenticate the user
   */
  @Override
  public String getPassword() {
    return user.getPassword();
  }

  /**
   * Returns the username used to authenticate the user.
   *
   * @return the username used to authenticate the user
   */
  @Override
  public String getUsername() {
    return user.getEmail();
  }

  /**
   * Indicates whether the user's account has expired.
   *
   * @return true if the user's account is valid (non-expired), false otherwise
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * Indicates whether the user is locked or unlocked.
   *
   * @return true if the user is not locked, false otherwise
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * Indicates whether the user's credentials (password) has expired.
   *
   * @return true if the user's credentials are valid (non-expired), false
   *         otherwise
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * Indicates whether the user is enabled or disabled.
   *
   * @return true if the user is enabled, false otherwise
   */
  @Override
  public boolean isEnabled() {
    return true;
  }
}
