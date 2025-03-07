package project.web.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.web.entities.RefreshToken;

/**
 * Repository interface for managing refresh tokens in the database.
 *
 * <p>This interface extends {@link org.springframework.data.jpa.repository.JpaRepository} to provide
 * CRUD (Create, Read, Update, Delete) operations for {@link project.web.entities.RefreshToken} entities.</p>
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  /**
   * Find a refresh token by user ID.
   *
   * @param userId The ID of the user associated with the refresh token.
   * @return An {@link java.util.Optional} containing the found {@link project.web.entities.RefreshToken},
   *         or an empty optional if not found.
   */
  Optional<RefreshToken> findByUserId(Long userId);

  /**
   * Delete a refresh token by user ID.
   *
   * @param userId The ID of the user associated with the refresh token to be deleted.
   */
  void deleteByUserId(Long userId);
}