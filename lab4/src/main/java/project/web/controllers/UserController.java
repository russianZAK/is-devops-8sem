package project.web.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.web.entities.IsAdminResponse;
import project.web.entities.User;
import project.web.entities.UserDTO;
import project.web.entities.UserExistsResponse;
import project.web.entities.UserRole;
import project.web.entities.UsernameOrEmailRequest;
import project.web.services.UserService;

/**
 * Controller class for managing user operations.
 * This controller provides endpoints for various user-related operations such as checking the existence of a username or email,
 * retrieving user information by username or email, checking if a user is an admin, and getting a list of all users (admin-only).
 * The class utilizes the {@link project.web.services.UserService} for handling user-related logic.
 */
@Controller
@RequestMapping("/user")
@Api(value = "UserController", tags = {"UserController"})
public class UserController {

  @Autowired
  private UserService service;

  /**
   * Checks if a username already exists.
   *
   * @param username The username to check.
   * @return ResponseEntity with the result indicating whether the username exists.
   */
  @PostMapping("/check-username-exists/{username}")
  public ResponseEntity<?> checkUsernameExists(@PathVariable String username) {
    return ResponseEntity.ok(new UserExistsResponse(service.existsByUsername(username)));
  }

  /**
   * Checks if an email already exists.
   *
   * @param email The email to check.
   * @return ResponseEntity with the result indicating whether the email exists.
   */
  @PostMapping("/check-email-exists/{email}")
  public ResponseEntity<?> checkEmailExists(@PathVariable String email) {
    return ResponseEntity.ok(new UserExistsResponse(service.existsByEmails(email)));
  }

  /**
   * Retrieves user information by username or email.
   *
   * @param usernameOrEmail The request containing the username or email.
   * @return ResponseEntity with the user information.
   */
  @PostMapping("/get-user-by-username-or-email")
  public ResponseEntity<?> getUserByUsernameOrEmail(@RequestBody UsernameOrEmailRequest usernameOrEmail) {
    return ResponseEntity.ok(service.getByUsernameOrEmail(usernameOrEmail.getUsernameOrEmail()));
  }

  /**
   * Checks if a user is an admin.
   *
   * @param usernameOrEmail The request containing the username or email.
   * @return ResponseEntity with the result indicating whether the user is an admin.
   */
  @PostMapping("/check-is-admin")
  public ResponseEntity<?> checkIsAdmin(@RequestBody UsernameOrEmailRequest usernameOrEmail) {
    return ResponseEntity.ok(new IsAdminResponse(service.isAdmin(usernameOrEmail.getUsernameOrEmail())));
  }

  /**
   * Gets a list of all users (admin-only).
   *
   * @return ResponseEntity with the list of all users.
   */
  @GetMapping ("/get-all-users")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getAllUsers() {
    return ResponseEntity.ok(service.getAll());
  }

}
