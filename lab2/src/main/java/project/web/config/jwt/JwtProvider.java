package project.web.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.web.entities.User;

/**
 * JwtProvider class handles the generation and validation of JWT (JSON Web Tokens) for user authentication.
 * This class utilizes the io.jsonwebtoken library for JWT operations.
 */
@Slf4j
@Component
public class JwtProvider {

  /**
   * Secret key for generating access tokens.
   */
  private final SecretKey jwtAccessSecret;

  /**
   * Secret key for generating refresh tokens.
   */
  private final SecretKey jwtRefreshSecret;

  /**
   * Constructs a JwtProvider with the specified access and refresh secret keys.
   *
   * @param jwtAccessSecret  Base64-encoded secret key for access tokens.
   * @param jwtRefreshSecret Base64-encoded secret key for refresh tokens.
   */
  public JwtProvider(
      @Value("${jwt.secret.access}") String jwtAccessSecret,
      @Value("${jwt.secret.refresh}") String jwtRefreshSecret
  ) {
    this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
  }

  /**
   * Generates an access token for the given user.
   *
   * @param user The user for whom the access token is generated.
   * @return The generated access token.
   */
  public String generateAccessToken(@NonNull User user) {
    final LocalDateTime now = LocalDateTime.now();
    final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
    final Date accessExpiration = Date.from(accessExpirationInstant);

    Set<String> roles = user.getRoles().stream()
        .map(userRole -> userRole.getRole().name())
        .collect(Collectors.toSet());

    return Jwts.builder()
        .setSubject(user.getUsername())
        .setExpiration(accessExpiration)
        .signWith(jwtAccessSecret)
        .claim("roles", roles)
        .compact();
  }

  /**
   * Generates a refresh token for the given user.
   *
   * @param user The user for whom the refresh token is generated.
   * @return The generated refresh token.
   */
  public String generateRefreshToken(@NonNull User user) {
    final LocalDateTime now = LocalDateTime.now();
    final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
    final Date refreshExpiration = Date.from(refreshExpirationInstant);
    return Jwts.builder()
        .setSubject(user.getUsername())
        .setExpiration(refreshExpiration)
        .signWith(jwtRefreshSecret)
        .compact();
  }

  /**
   * Validates the given access token.
   *
   * @param accessToken The access token to validate.
   * @return True if the access token is valid; false otherwise.
   */
  public boolean validateAccessToken(@NonNull String accessToken) {
    return validateToken(accessToken, jwtAccessSecret);
  }

  /**
   * Validates the given refresh token.
   *
   * @param refreshToken The refresh token to validate.
   * @return True if the refresh token is valid; false otherwise.
   */
  public boolean validateRefreshToken(@NonNull String refreshToken) {
    return validateToken(refreshToken, jwtRefreshSecret);
  }

  private boolean validateToken(@NonNull String token, @NonNull Key secret) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secret)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.error("Token expired", e);
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported jwt", e);
    } catch (MalformedJwtException e) {
      log.error("Malformed jwt", e);
    } catch (SignatureException e) {
      log.error("Invalid signature", e);
    } catch (Exception e) {
      log.error("invalid token", e);
    }
    return false;
  }

  /**
   * Retrieves the claims from the access token.
   *
   * @param token The access token from which to extract claims.
   * @return The claims extracted from the access token.
   */
  public Claims getAccessClaims(@NonNull String token) {
    return getClaims(token, jwtAccessSecret);
  }

  public Claims getRefreshClaims(@NonNull String token) {
    return getClaims(token, jwtRefreshSecret);
  }

  /**
   * Retrieves the claims from the refresh token.
   *
   * @param token The refresh token from which to extract claims.
   * @return The claims extracted from the refresh token.
   */
  private Claims getClaims(@NonNull String token, @NonNull Key secret) {
    return Jwts.parserBuilder()
        .setSigningKey(secret)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
