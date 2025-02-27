package com.ourspace.backend.domain.user;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ourspace.backend.core.generic.AbstractServiceImpl;
import com.ourspace.backend.domain.role.Role;
import com.ourspace.backend.domain.role.RoleRepository;

/**
 * Service implementation for User.
 */
@Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {

  private final PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepository roleRepository;

  public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
    super(repository);
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Gets the default user role set.
   *
   * @return the default user role set
   */
  private Set<Role> getUserRoleSet() {
    Set<Role> roles = new HashSet<Role>();
    Role role = roleRepository.findByName("USER");
    roles.add(role);
    return roles;
  }

  /**
   * Loads user details by username (email).
   *
   * @param email the username (email)
   * @return the user details
   * @throws UsernameNotFoundException if the user is not found
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return ((UserRepository) repository).findByEmail(email)
        .map(UserDetailsImpl::new)
        .orElseThrow(() -> new UsernameNotFoundException(email));
  }

  /**
   * Registers a new user.
   *
   * @param user the user to register
   * @return the registered user
   */
  @Override
  public User register(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles(getUserRoleSet());
    return save(user);
  }

  /**
   * Registers a new user without password.
   * This Method can be used for development and testing. the Password for the
   * user will be set to "1234"
   *
   * @param user the user to register
   * @return the registered user
   */
  @Override
  public User registerUser(User user) {
    user.setPassword(passwordEncoder.encode("1234"));
    user.setRoles(getUserRoleSet());
    return save(user);
  }

  /**
   * Updates an existing user by ID.
   *
   * @param id   the ID of the user to update
   * @param user the user to update
   * @return the updated user
   */
  @Override
  public User updateById(UUID id, User user) {
    user.setId(id);
    user.setPassword(repository.findById(id).get().getPassword());
    user.setRoles(repository.findById(id).get().getRoles());
    return save(user);
  }

  public Stream<Character> getRandomSpecialChars(int count) {
    Random random = new SecureRandom();
    IntStream specialChars = random.ints(count, 33, 45);
    return specialChars.mapToObj(data -> (char) data);
  }

}
