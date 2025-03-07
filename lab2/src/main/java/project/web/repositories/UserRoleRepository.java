package project.web.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.web.entities.Role;
import project.web.entities.UserRole;

/**
 * Repository interface for managing user roles in the database.
 *
 * <p>This interface extends {@link org.springframework.data.jpa.repository.JpaRepository} to provide
 * CRUD (Create, Read, Update, Delete) operations for {@link project.web.entities.UserRole} entities.</p>
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
  /**
   * Find a user role by role.
   *
   * @param role The role to find.
   * @return An {@link java.util.Optional} containing the found {@link project.web.entities.UserRole},
   *         or an empty optional if not found.
   */
  Optional<UserRole> findUserRoleByRole(Role role);

  /**
   * Check if a user role with the given role exists.
   *
   * @param role The role to check.
   * @return {@code true} if a user role with the given role exists, {@code false} otherwise.
   */
  boolean existsByRole(Role role);
}
