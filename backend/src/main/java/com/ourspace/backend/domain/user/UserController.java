package com.ourspace.backend.domain.user;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourspace.backend.domain.user.dto.UserDTO;
import com.ourspace.backend.domain.user.dto.UserMapper;
import com.ourspace.backend.domain.user.dto.UserRegisterDTO;

import jakarta.validation.Valid;

/**
 * Controller for User endpoints.
 */
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  public UserController(UserService userService, UserMapper userMapper) {
    this.userService = userService;
    this.userMapper = userMapper;
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id the ID of the user to retrieve
   * @return the user DTO
   */
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('USER_READ_ALL')")
  public ResponseEntity<UserDTO> retrieveById(@PathVariable UUID id) {
    User user = userService.findById(id);
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
  }

  /**
   * Retrieves all users.
   *
   * @return the list of user DTOs
   */
  @GetMapping({ "", "/" })
  @PreAuthorize("hasAuthority('USER_READ_ALL')")
  public ResponseEntity<List<UserDTO>> retrieveAll() {
    List<User> users = userService.findAll();
    return new ResponseEntity<>(userMapper.toDTOs(users), HttpStatus.OK);
  }

  /**
   * Registers a new user.
   *
   * @param userRegisterDTO the user registration DTO
   * @return the created user DTO
   */
  @PostMapping("/register")
  public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
    User user = userService.register(userMapper.fromUserRegisterDTO(userRegisterDTO));
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.CREATED);
  }

  /**
   * Registers a new user without password.
   *
   * @param userDTO the user DTO
   * @return the created user DTO
   */
  @PostMapping("/registerUser")
  public ResponseEntity<UserDTO> registerWithoutPassword(@Valid @RequestBody UserDTO userDTO) {
    User user = userService.registerUser(userMapper.fromDTO(userDTO));
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.CREATED);
  }

  /**
   * Updates an existing user by their ID.
   *
   * @param id      the ID of the user to update
   * @param userDTO the user DTO
   * @return the updated user DTO
   */
  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('USER_MODIFY_ALL')  || (hasAuthority('USER_DELETE_OWN') && @userPermissionEvaluator.isOwnUser(#id))")
  public ResponseEntity<UserDTO> updateById(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
    User user = userService.updateById(id, userMapper.fromDTO(userDTO));
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
  }

  /**
   * Deletes a user by their ID.
   *
   * @param id the ID of the user to delete
   * @return the response entity
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('USER_DELETE_ALL') || (hasAuthority('USER_DELETE_OWN') && @userPermissionEvaluator.isOwnUser(#id))")
  public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
    userService.deleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
