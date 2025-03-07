package project.web.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.web.entities.User;


/**
 * Repository interface for managing users in the database.
 *
 * <p>This interface extends {@link org.springframework.data.jpa.repository.JpaRepository} to provide
 * CRUD (Create, Read, Update, Delete) operations for {@link project.web.entities.User} entities.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   /**
    * Find a user by username.
    *
    * @param username The username of the user to find.
    * @return An {@link java.util.Optional} containing the found {@link project.web.entities.User},
    *         or an empty optional if not found.
    */
   Optional<User> findByUsername(String username);

   /**
    * Find a user by email.
    *
    * @param email The email of the user to find.
    * @return An {@link java.util.Optional} containing the found {@link project.web.entities.User},
    *         or an empty optional if not found.
    */
   Optional<User> findByEmail(String email);

   /**
    * Check if a user with the given username exists.
    *
    * @param username The username to check.
    * @return {@code true} if a user with the given username exists, {@code false} otherwise.
    */
   boolean existsByUsername(String username);

   /**
    * Check if a user with the given email exists.
    *
    * @param email The email to check.
    * @return {@code true} if a user with the given email exists, {@code false} otherwise.
    */
   boolean existsByEmail(String email);
}