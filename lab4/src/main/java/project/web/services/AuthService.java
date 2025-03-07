package project.web.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.web.config.jwt.JwtProvider;
import project.web.config.jwt.JwtRequest;
import project.web.config.jwt.JwtResponse;
import project.web.entities.LogoutResponse;
import project.web.entities.RefreshToken;
import project.web.entities.User;
import project.web.entities.UserDTO;
import project.web.repositories.RefreshTokenRepository;

/**
 * Service class for handling authentication-related operations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserService userService;
  private final JwtProvider jwtProvider;

  /**
   * Save a new user and perform login, returning the JWT response.
   *
   * @param userDTO The user DTO containing registration details.
   * @return The JWT response after successful registration and login.
   */
  public JwtResponse saveNewUser(UserDTO userDTO){
    userService.saveNewUser(userDTO);
    return login(new JwtRequest(userDTO.getEmail(), userDTO.getPassword()));
  }

  /**
   * Logout a user by invalidating the provided refresh token.
   *
   * @param refreshToken The refresh token to invalidate.
   * @return The logout response.
   */
  public LogoutResponse logout(@NonNull String refreshToken){
    validateRefreshToken(refreshToken);
    User user = getUserFromRefreshToken(refreshToken);
    refreshTokenRepository.deleteByUserId(user.getId());
    return new LogoutResponse(HttpStatus.OK.value(), "User has been logged out successfully");
  }

  /**
   * Perform login with the provided authentication request and return the JWT response.
   *
   * @param authRequest The authentication request containing username/email and password.
   * @return The JWT response after successful login.
   * @throws UsernameNotFoundException If the username or email is not found.
   * @throws BadCredentialsException If the provided password is incorrect.
   */
  public JwtResponse login(@NonNull JwtRequest authRequest) throws UsernameNotFoundException, BadCredentialsException{
    User user = userService.getByUsernameOrEmail(authRequest.getUsernameOrEmail());

    if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
      throw new BadCredentialsException(String.format("Invalid user password - '%s'", authRequest.getPassword()));
    }
    var accessToken = jwtProvider.generateAccessToken(user);
    var refreshToken = jwtProvider.generateRefreshToken(user);
    var oldToken = refreshTokenRepository.findByUserId(user.getId());

    if (oldToken.isPresent()) {
      oldToken.get().setToken(refreshToken);
      refreshTokenRepository.save(oldToken.get());
    }else {
      refreshTokenRepository.save(
          RefreshToken.builder().userId(user.getId()).token(refreshToken).build());
    }

    return new JwtResponse(accessToken, refreshToken);
  }

  /**
   * Get a new access token using the provided refresh token.
   *
   * @param refreshToken The refresh token to use.
   * @return The JWT response with a new access token.
   * @throws JwtException If the refresh token is invalid.
   * @throws UsernameNotFoundException If the username or email is not found.
   */
  public JwtResponse getAccessToken(@NonNull String refreshToken) throws JwtException, UsernameNotFoundException {
    validateRefreshToken(refreshToken);
    User user = getUserFromRefreshToken(refreshToken);
    String accessToken = jwtProvider.generateAccessToken(user);
    return new JwtResponse(accessToken, null);
  }

  /**
   * Refresh both access and refresh tokens using the provided refresh token.
   *
   * @param refreshToken The refresh token to use.
   * @return The JWT response with new access and refresh tokens.
   * @throws JwtException If the refresh token is invalid.
   * @throws UsernameNotFoundException If the username or email is not found.
   */
  public JwtResponse refresh(@NonNull String refreshToken) throws JwtException, UsernameNotFoundException {
    validateRefreshToken(refreshToken);
    User user = getUserFromRefreshToken(refreshToken);
    RefreshToken refreshTokenEntity = updateRefreshToken(refreshToken, user);
    String accessToken = jwtProvider.generateAccessToken(user);
    String newRefreshToken = refreshTokenEntity.getToken();
    return new JwtResponse(accessToken, newRefreshToken);
  }

  /**
   * Validate the provided refresh token.
   *
   * @param refreshToken The refresh token to validate.
   * @throws JwtException If the refresh token is invalid.
   */
  private void validateRefreshToken(String refreshToken) throws JwtException {
    if (!jwtProvider.validateRefreshToken(refreshToken)) {
      throw new JwtException("Invalid refresh token");
    }
  }

  /**
   * Get a user entity based on the provided refresh token.
   *
   * @param refreshToken The refresh token containing user information.
   * @return The user entity associated with the refresh token.
   * @throws IllegalArgumentException If the refresh token is malformed.
   * @throws UsernameNotFoundException If the username or email is not found.
   */
  private User getUserFromRefreshToken(String refreshToken) throws IllegalArgumentException, UsernameNotFoundException {
    final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
    final String usernameOrEmail = claims.getSubject();
    return userService.getByUsernameOrEmail(usernameOrEmail);
  }

  /**
   * Update the refresh token associated with the user.
   *
   * @param refreshToken The current refresh token.
   * @param user The user entity associated with the refresh token.
   * @return The updated refresh token entity.
   * @throws JwtException If the refresh token is invalid.
   */
  private RefreshToken updateRefreshToken(String refreshToken, User user) throws JwtException {
    Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserId(user.getId());
    if (optionalRefreshToken.isEmpty() || !optionalRefreshToken.get().getToken().equals(refreshToken)) {
      throw new JwtException("Invalid refresh token");
    }
    RefreshToken refreshTokenEntity = optionalRefreshToken.get();
    String newRefreshToken = jwtProvider.generateRefreshToken(user);
    refreshTokenEntity.setToken(newRefreshToken);
    refreshTokenRepository.save(refreshTokenEntity);
    return refreshTokenEntity;
  }
}
