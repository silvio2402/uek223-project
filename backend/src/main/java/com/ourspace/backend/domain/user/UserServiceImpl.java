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

@Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {

  private final PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
    super(repository);
    this.passwordEncoder = passwordEncoder;
  }

  private Set<Role> getUserRoleSet() {
    Set<Role> roles = new HashSet();
    Role role = roleRepository.findById(UUID.fromString("c6aee32d-8c35-4481-8b3e-a876a39b0c02")).get();
    roles.add(role);
    return roles;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return ((UserRepository) repository).findByEmail(email)
        .map(UserDetailsImpl::new)
        .orElseThrow(() -> new UsernameNotFoundException(email));
  }

  @Override
  public User register(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles(getUserRoleSet());
    return save(user);
  }

  @Override
  // This Method can be used for development and testing. the Password for the
  // user will be set to "1234"
  public User registerUser(User user) {
    user.setPassword(passwordEncoder.encode("1234"));
    user.setRoles(getUserRoleSet());
    return save(user);
  }

  public Stream<Character> getRandomSpecialChars(int count) {
    Random random = new SecureRandom();
    IntStream specialChars = random.ints(count, 33, 45);
    return specialChars.mapToObj(data -> (char) data);
  }

}
