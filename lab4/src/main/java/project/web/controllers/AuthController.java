package project.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.web.config.jwt.JwtRefreshTokenRequest;
import project.web.config.jwt.JwtRequest;
import project.web.entities.UserDTO;
import project.web.services.AuthService;

/**
 * Controller class handling authentication-related endpoints.
 * This controller provides endpoints for user registration, login, logout, and token refreshing.
 * The class utilizes the {@link project.web.services.AuthService} for handling authentication logic.
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  /**
   * Handles the user registration endpoint.
   * @param userDTO The user data to be registered.
   * @return ResponseEntity with the result of the registration process.
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Validated UserDTO userDTO){
    return ResponseEntity.ok(authService.saveNewUser(userDTO));
  }

  /**
   * Handles the user login endpoint.
   * @param request The JWT login request containing credentials.
   * @return ResponseEntity with the result of the login process.
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Validated JwtRequest request){
    return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
  }

  /**
   * Handles the user logout endpoint.
   * @param request The JWT refresh token request for logout.
   * @return ResponseEntity with the result of the logout process.
   */
  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestBody @Validated JwtRefreshTokenRequest request){
    return new ResponseEntity<>(authService.logout(request.getRefreshToken()), HttpStatus.OK);
  }

  /**
   * Handles the request for obtaining a new access token using a refresh token.
   * @param request The JWT refresh token request for obtaining a new access token.
   * @return ResponseEntity with the new access token.
   */
  @PostMapping("/get-new-access-token")
  public ResponseEntity<?> getNewAccessToken(@RequestBody @Validated JwtRefreshTokenRequest request) {
    return new ResponseEntity<>(authService.getAccessToken(request.getRefreshToken()), HttpStatus.OK);
  }

  /**
   * Handles the refresh token endpoint for obtaining a new refresh token.
   * @param request The JWT refresh token request for obtaining a new refresh token.
   * @return ResponseEntity with the new refresh token.
   */
  @PostMapping("/refresh-token")
  public ResponseEntity<?> getNewRefreshToken(@RequestBody @Validated JwtRefreshTokenRequest request){
    return new ResponseEntity<>(authService.refresh(request.getRefreshToken()), HttpStatus.OK);
  }
}
