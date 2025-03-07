package project.web.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.web.entities.MyUserPrincipal;
import project.web.entities.Role;
import project.web.entities.User;
import project.web.entities.UserDTO;
import project.web.entities.UserRole;
import project.web.exceptions.UserAlreadyExistsException;
import project.web.repositories.UserRepository;

/**
 * Service class for managing user-related operations and integrating with the user repository.
 */
@Service
@AllArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

  private final UserRepository repository;
  private final UserRoleService userRoleService;
  private final ModelMapper modelMapper = new ModelMapper();

  /**
   * Load a user by username or email for authentication purposes.
   *
   * @param usernameOrEmail The username or email of the user.
   * @return UserDetails representing the authenticated user.
   * @throws UsernameNotFoundException If the user is not found.
   */
  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    Optional<User> user = findByUsernameOrEmail(usernameOrEmail);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException(
          String.format("User with username '%s' not found", usernameOrEmail));
    }
    return new MyUserPrincipal(user.get());
  }

  /**
   * Get a user by username or email.
   *
   * @param usernameOrEmail The username or email of the user.
   * @return The user with the specified username or email.
   */
  public User getByUsernameOrEmail(String usernameOrEmail) {
    Optional<User> user = findByUsernameOrEmail(usernameOrEmail);

    if (user.isEmpty()) {
      throw new UsernameNotFoundException(
          String.format("User with username '%s' not found", usernameOrEmail));
    }
    return user.get();
  }

  /**
   * Check if a user with the specified username exists.
   *
   * @param username The username to check for existence.
   * @return True if a user with the specified username exists, otherwise false.
   */
  public boolean existsByUsername(String username){
    return repository.existsByUsername(username);
  }

  /**
   * Check if a user with the specified email exists.
   *
   * @param email The email to check for existence.
   * @return True if a user with the specified email exists, otherwise false.
   */
  public boolean existsByEmails(String email){
    return repository.existsByEmail(email);
  }

  /**
   * Get a user by ID.
   *
   * @param id The ID of the user.
   * @return The user with the specified ID.
   * @throws BadCredentialsException If the user with the specified ID is not found.
   */
  public User getById(Long id) {
    Optional<User> user = repository.findById(id);
    if (user.isEmpty()) {
      throw new BadCredentialsException(String.format("User with id '%d' not found", id));
    }
    return user.get();
  }

  /**
   * Check if a user has the 'ADMIN' role.
   *
   * @param usernameOrEmail The username or email of the user.
   * @return True if the user has the 'ADMIN' role, otherwise false.
   */
  public boolean isAdmin(String usernameOrEmail){
    User user = getByUsernameOrEmail(usernameOrEmail);
    Set<UserRole> roles = user.getRoles();
    return roles.contains(userRoleService.findUserRoleByRole(Role.ROLE_ADMIN));
  }

  /**
   * Save a new user entity.
   *
   * @param entity The user entity to save.
   */
  public void save(User entity) {
    entity.getRoles().forEach(userRoleService::save);
    repository.save(entity);
  }

  /**
   * Get a list of user DTOs representing all users.
   *
   * @return List of user DTOs.
   */
  public List<UserDTO> getAll() {
    List<User> users = repository.findAll();

    List<UserDTO> usersDTO = users.stream().map(user -> {
      UserDTO userDTO = modelMapper.map(user, UserDTO.class);
      userDTO.setPassword("");
      return userDTO;
    }).collect(Collectors.toList());

    return usersDTO;
  }

  /**
   * Save a new user based on the provided user DTO.
   *
   * @param userDTO The user DTO containing user information.
   * @return The newly created user.
   * @throws UserAlreadyExistsException If the user with the specified username or email already exists.
   */
  public User saveNewUser(UserDTO userDTO) {
    User newUser = getNewUserFromDTO(userDTO);

    if (!userRoleService.existsByRole(Role.ROLE_USER)){
      userRoleService.save(new UserRole(1L, Role.ROLE_USER));
    }

    newUser.setRoles(Collections.singleton(userRoleService.findUserRoleByRole(Role.ROLE_USER)));

    if (repository.existsByUsername(newUser.getUsername())){
      throw new UserAlreadyExistsException(String.format("User '%s' already exists", newUser.getUsername()));
    }

    if (repository.existsByEmail(newUser.getEmail())){
      throw new UserAlreadyExistsException(String.format("User with email '%s' already exists", newUser.getEmail()));
    }

    save(newUser);

    return newUser;
  }

  /**
   * Find a user by username or email.
   *
   * @param usernameOrEmail The username or email of the user.
   * @return Optional containing the user with the specified username or email, or empty if not found.
   */
  private Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
    return Optional.ofNullable(repository.findByUsername(usernameOrEmail)
        .orElseGet(() -> repository.findByEmail(usernameOrEmail)
            .orElseThrow(() -> new BadCredentialsException(String.format("User with username or email '%s' not found", usernameOrEmail)))));
  }

  /**
   * Get a user entity from a user DTO.
   *
   * @param userDTO The user DTO to map.
   * @return The user entity.
   */
  private User getNewUserFromDTO(UserDTO userDTO) {
    User newUser = new User();
    modelMapper.map(userDTO, newUser);
    newUser.setAccountNonExpired(true);
    newUser.setAccountNonLocked(true);
    newUser.setEnabled(true);
    newUser.setCredentialsNonExpired(true);
    return newUser;
  }
}