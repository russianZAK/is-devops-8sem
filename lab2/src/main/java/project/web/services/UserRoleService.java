package project.web.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.web.entities.Role;
import project.web.entities.UserRole;
import project.web.repositories.UserRoleRepository;

/**
 * Service class for managing user roles.
 */
@Service
@Transactional
public class UserRoleService {

  private final UserRoleRepository repository;

  /**
   * Constructor for UserRoleService.
   *
   * @param repository The repository for user roles.
   */
  public UserRoleService(UserRoleRepository repository){
    this.repository = repository;
  }

  /**
   * Find a user role by its associated role.
   *
   * @param role The role to search for.
   * @return The user role associated with the specified role.
   */
  public UserRole findUserRoleByRole(Role role){
    return repository.findUserRoleByRole(role).get();
  }

  /**
   * Get a user role by its ID.
   *
   * @param id The ID of the user role.
   * @return The user role with the specified ID.
   */
  public UserRole getById(Long id) {
    return repository.findById(id).get();
  }

  /**
   * Update an existing user role.
   *
   * @param id The ID of the user role to update.
   * @param entity The updated user role entity.
   * @return The updated user role.
   */
  public UserRole update(Long id, UserRole entity) {
    UserRole existingEntity = repository.findById(id).orElse(null);
    if (existingEntity != null) {
      existingEntity = entity;
    }
    return repository.save(existingEntity);
  }

  /**
   * Save a new user role.
   *
   * @param entity The user role to save.
   */
  public void save(UserRole entity) {
    repository.save(entity);
  }

  /**
   * Check if a user role with the specified role exists.
   *
   * @param role The role to check for existence.
   * @return True if a user role with the specified role exists, otherwise false.
   */
  public boolean existsByRole(Role role){
    return repository.existsByRole(role);
  }

  /**
   * Delete a user role by its ID.
   *
   * @param id The ID of the user role to delete.
   */
  public void deleteById(Long id) {
    repository.deleteById(id);
  }
}