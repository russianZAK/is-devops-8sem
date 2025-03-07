package project.web.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.web.entities.UserRole;
import project.web.services.UserRoleService;


/**
 * Controller class for managing user roles.
 *
 * This controller provides endpoints for CRUD operations on user roles, including creating, updating,
 * deleting, and retrieving user roles by their IDs.
 *
 * The class utilizes the {@link project.web.services.UserRoleService} for handling user role-related logic.
 */
@Controller
@RequestMapping("/user-role")
@Api(value = "UserRoleController", tags = {"UserRoleController"})
public class UserRoleController {

  @Autowired
  private UserRoleService service;

  /**
   * Creates a new user role.
   *
   * @param role The user role to be created.
   * @return ResponseEntity with the created user role.
   */
  @PostMapping("/save")
  @ApiOperation(value = "Create a new role")
  public ResponseEntity<UserRole> save(@RequestBody UserRole role) {
    service.save(role);
    return ResponseEntity.ok(role);
  }

  /**
   * Updates an existing user role by ID.
   *
   * @param id   The ID of the user role to be updated.
   * @param role The updated user role data.
   * @return ResponseEntity with the updated user role.
   */
  @PutMapping("/update/{id}")
  @ApiOperation(value = "Update an existing role")
  public ResponseEntity<UserRole> update(@PathVariable Long id, @RequestBody UserRole role) {
    service.update(id, role);
    return ResponseEntity.ok(role);
  }

  /**
   * Deletes a user role by ID.
   *
   * @param id The ID of the user role to be deleted.
   * @return ResponseEntity with a status indicating the success of the deletion.
   */
  @DeleteMapping("/delete/{id}")
  @ApiOperation(value = "Delete a role by ID")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    service.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * Retrieves a user role by ID.
   *
   * @param id The ID of the user role to be retrieved.
   * @return ResponseEntity with the retrieved user role.
   */
  @GetMapping("/get/{id}")
  @ApiOperation(value = "Get a role by ID")
  public ResponseEntity<UserRole> getById(@PathVariable Long id) {
    UserRole role = service.getById(id);
    return ResponseEntity.ok(role);
  }
}
